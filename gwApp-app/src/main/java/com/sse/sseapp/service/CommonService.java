package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.sse.sseapp.app.core.base.RespEnum;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.domain.RespContentVO;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.exception.PermissionException;
import com.sse.sseapp.app.core.exception.ProxyException;
import com.sse.sseapp.app.core.utils.Sm4Util;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.core.constant.CacheConstants;
import com.sse.sseapp.core.text.Convert;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.AddAppStatisticsSetupReqBody;
import com.sse.sseapp.form.request.AllCategoryDataReqBody;
import com.sse.sseapp.form.request.ParsingUrlReqBody;
import com.sse.sseapp.form.response.AddAppStatisticsSetupResBody;
import com.sse.sseapp.form.response.AllCategoryDataResBody;
import com.sse.sseapp.proxy.ProxyConfig;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.CominfoResponse;
import com.sse.sseapp.proxy.cominfo.dto.ParsingUrlDto;
import com.sse.sseapp.proxy.mysoa.MySoaResponse;
import com.sse.sseapp.redis.service.RedisService;
import com.sse.sseapp.util.VersionUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sse.sseapp.core.utils.ServletUtils.getRequest;
import static com.sse.sseapp.proxy.CacheValueType.scode;

/**
 * 公共方法
 *
 * @author zhengyaosheng
 * @date 2023-03-17
 */
@Service
public class CommonService {
    @Autowired
    ProxyProvider proxyProvider;
    @Autowired
    ISysProxyFeign sysProxyFeign;
    @Autowired
    private ISysConfigFeign sysConfigFeign;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysDictDataFeign iSysDictDataFeign;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired(required = false)
    private ParameterConfig mockInterceptor;

    /**
     * 获取key
     *
     * @param defaultKey 取默认加解密key
     * @return 加解密key
     */
    public String getKey(boolean defaultKey) {
        if (defaultKey) {
            String key = sysConfigFeign.getConfigKey(AppConstants.ENCRYPT_KEY);
            if (CharSequenceUtil.isBlank(key)) {
                throw new AppException("默认加密key未配置");
            }
            return key;
        }

        //上锁
        Boolean success = redisTemplate.opsForValue().setIfAbsent("generateScode", "生成scode", 5, TimeUnit.SECONDS);
        if (success != null && success) {
            try {
                String key = Convert.toStr(redisService.getCacheObject(getCacheKey()));
                if (StringUtils.isNotEmpty(key)) {
                    return key;
                }
                key = Sm4Util.generateKey();
                String timeout = sysConfigFeign.getConfigKey(AppConstants.ENCRYPT_KEY_TIMEOUT);
                if (StringUtils.isEmpty(timeout)) {
                    timeout = AppConstants.DEFAULT_ENCRYPT_KEY_TIMEOUT;
                }
                redisService.setCacheObject(getCacheKey(), key, Long.parseLong(timeout), TimeUnit.MINUTES);
                return key;
            } finally {
                //解锁
                redisTemplate.delete("generateScode");
            }
        } else {
            //加锁失败重新调用
            return getKey(false);
        }
    }

    public void cominfoCheck(ReqBaseVO base) {
        if (ObjectUtil.isEmpty(base.getUid())) {
            throw new ProxyException(RespEnum.LOGIN_INFO_INVALID, RespEnum.LOGIN_INFO_INVALID.getMessage());
        }
        String servletPath = getRequest() != null ? getRequest().getServletPath() : "/app";
        Map<String, Object> data = ImmutableMap.of("pathName", servletPath);
        SysProxyConfig info = new SysProxyConfig();
//        if (!VersionUtil.geTargetVersion(base.getAppVersion(), "5.4.0")) {
            info = sysProxyFeign.getInfo("cominfo-common");
//        } else {
//            info = sysProxyFeign.getInfo("cominfo-common-xc");
//        }

        ProxyConfig proxyConfig = new ProxyConfig();
        BeanUtil.copyProperties(info, proxyConfig);
        proxyConfig.setData(data);
        proxyConfig.setBase(base);

        CominfoResponse<RespContentVO> response = proxyProvider.proxy(proxyConfig, new TypeReference<CominfoResponse<RespContentVO>>() {
        });
        if (!Objects.equals(1, response.getState())) {
            throw new PermissionException(response.getMsg());
        }
    }

    /**
     * 设置cache key
     *
     * @return 缓存键key
     */
    private String getCacheKey() {
        return CacheConstants.SYS_CONFIG_KEY + AppConstants.INBORN_ENCRYPT_KEY;
    }

    public ParsingUrlDto parsingUrl(BaseRequest<ParsingUrlReqBody> parsingUrlReqBody) {
        //获取到前端传来的url   对url进行处理    将后缀改成.json的格式
        String url = parsingUrlReqBody.getReqContent().getUrl();
        //网络地址要先下载
        if (!url.contains("http")) {
            url = "http://" + url;
        }
        url = url.replace(".shtml", ".json");
        url = url.replace(".html", ".json");
        url = url.replace(".htm", ".json");

        //请求该url    并得到返回回来的json字符串  并将字符串转换成ParsingUrlDto对象
        String jsonStr = HttpRequest.get(url).execute().body();
        jsonStr = jsonStr.replaceAll("\n", " ");
        return JSONUtil.toBean(jsonStr, ParsingUrlDto.class);
    }

    public RespBean<?> getAllCategoryData(BaseRequest<AllCategoryDataReqBody> baseRequest) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (ObjectUtil.isNotEmpty(data.get("type"))) {
            String[] types = data.get("type").toString().split("_");
            for (String type : types) {
                data.remove("type");
                data.put("type", type);
                SysProxyConfig info = this.sysProxyFeign.getInfo(ApiCodeConstants.SYS_PROXY_CODE_ALL_CATEGORY_DATA);
                ProxyConfig proxyConfig = new ProxyConfig();
                BeanUtil.copyProperties(info, proxyConfig);
                proxyConfig.setData(data);
                proxyConfig.setBase(baseRequest.getBase());
                MySoaResponse<AllCategoryDataResBody> result = proxyProvider.proxy(proxyConfig, new TypeReference<MySoaResponse<AllCategoryDataResBody>>() {
                });
                if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                    throw new AppException(result.getReturnMsg());
                }
                Map<String, Object> item = new HashMap<>();
                item.put("data", result.getList());
                item.put("type", type);
                mapList.add(item);
            }
            return RespBean.success(mapList);
        }
        SysProxyConfig info = this.sysProxyFeign.getInfo(ApiCodeConstants.SYS_PROXY_CODE_ALL_CATEGORY_DATA);
        ProxyConfig proxyConfig = new ProxyConfig();
        BeanUtil.copyProperties(info, proxyConfig);
        proxyConfig.setData(data);
        proxyConfig.setBase(baseRequest.getBase());
        MySoaResponse<AllCategoryDataResBody> result = proxyProvider.proxy(proxyConfig, new TypeReference<MySoaResponse<AllCategoryDataResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        Map<String, Object> item = new HashMap<>();
        item.put("data", result.getList());
        mapList.add(item);
        return RespBean.success(mapList);
    }

    public Map<String, Map<String, DictVO>> getDictFromRedis(String dictType) {
        if (ObjectUtil.isNotEmpty(redisService.getCacheMap("app_sys_dict:" + dictType))) {
            return redisService.getCacheMap("app_sys_dict:" + dictType);
        }
        //如果数据字典缓存中没有该数据字典
        Map<String, Map<String, DictVO>> resultMap = new HashMap<>();
        List<Map<String, Object>> cacheData = iSysDictDataFeign.getDictMap(dictType);

//        List<SysDictData> cacheData = (List<SysDictData>) iSysDictDataFeign.dictType(dictType).get("data");
        List<DictVO> result = new ArrayList<>();
        //将SysDictData集合转换为DictVo集合
        for (Map<String, Object> sysDictData : cacheData) {
            DictVO dictVO = new DictVO();
            dictVO.setDictEntry((String) sysDictData.get("dict_type"));
            dictVO.setDictEntryName((String) sysDictData.get("dict_name"));
            dictVO.setDictSubEntry((String) sysDictData.get("dict_value"));
            dictVO.setDictSubEntryName((String) sysDictData.get("dict_label"));
            dictVO.setDictSubEntrySort(Long.parseLong(String.valueOf(sysDictData.get("dict_sort"))));
            dictVO.setDictStatus((String) sysDictData.get("status"));
            result.add(dictVO);
        }
        //将DictVo集合转为所需要的Map集合的格式
        if (ArrayUtil.isNotEmpty(result)) {
            for (DictVO dvo : result) {
                Map<String, DictVO> value = resultMap.get(dvo.getDictEntry());
                if (null == value) {
                    Map<String, DictVO> sub = new HashMap<>();
                    sub.put(dvo.getDictSubEntry(), dvo);
                    resultMap.put(dvo.getDictEntry(), sub);
                } else {
                    value.put(dvo.getDictSubEntry(), dvo);
                }
            }
        }
        redisService.setCacheMap("app_sys_dict:" + dictType, resultMap);

        return resultMap;
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    @SneakyThrows
    public void init() {
        Map<String, Map<String, String>> map = mockInterceptor.getAllParameterConfig();
        Set<String> s1 = map.keySet();
        for (String key : s1) {
            redisService.setCacheMap(AppConstants.PARAMETER_CONFIG + key, map.get(key));
        }
        redisService.deleteObject(AppConstants.QQXY + ":" + scode.name());
        redisService.deleteObject(AppConstants.COMINFO + ":" + scode.name());
    }

    public Map<String, String> getActive(String code) {
        if (ObjectUtil.isEmpty(this.active)) {
            throw new AppException("配置文件信息获取失败");
        }
        if ("dev".equals(this.active)) {
            code = code + "_dev";
        } else if ("test".equals(this.active)) {
            code = code + "_test";
        } else {
            code = code + "_prod";
        }
        Map<String, String> map = redisService.getCacheMap(AppConstants.PARAMETER_CONFIG + code);
        if (ObjectUtil.isNotEmpty(map)) {
            return map;
        }
        map = mockInterceptor.getParameterConfig(code);
        if (ObjectUtil.isNotEmpty(map)) {
            redisService.setCacheMap(AppConstants.PARAMETER_CONFIG + code, map);
        } else {
            throw new AppException("三方接口参数数据配置信息获取失败");
        }
        return map;
    }

    public String getActive(String code, String key) {
        Map<String, String> map = getActive(code);
        StringBuilder value = new StringBuilder();
        if (ObjectUtil.isNotEmpty(key)) {
            for (int i = 0; i < key.split(",").length; i++) {
                value.append(map.get(key.split(",")[i])).append(",");
            }
            if (ObjectUtil.isNotEmpty(value)) {
                value.deleteCharAt(value.length() - 1);
            }
        }
        return value.toString();
    }

    public String[] whiteList() {
        if (ObjectUtil.isNotEmpty(redisService.getCacheObject("sys_config:app.whiteList"))) {
            String value = redisService.getCacheObject("sys_config:app.whiteList");
            return value.split(",");
        }
        //以下操作是在缓存中没有相对应数据时进行的操作
        if (StringUtils.isNotEmpty(sysConfigFeign.getConfigKey("app.whiteList"))) {
            redisService.setCacheObject("sys_config:app.whiteList", sysConfigFeign.getConfigKey("app.whiteList"));
            return sysConfigFeign.getConfigKey("app.whiteList").split(",");
        } else {
            //如果为空，重试三次
            for (int i = 0; i < 3; i++) {
                if (StringUtils.isNotEmpty(sysConfigFeign.getConfigKey("app.whiteList"))) {
                    redisService.setCacheObject("sys_config:app.whiteList", sysConfigFeign.getConfigKey("app.whiteList"));
                    return sysConfigFeign.getConfigKey("app.whiteList").split(",");
                }
            }
            return new String[]{""};
        }
    }

    public JSONObject urlPrefix() {
        String urlPrefix = redisService.getCacheObject("sys_config:yunhq_url_prefix");
        if (StrUtil.isEmpty(urlPrefix)) {
            urlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        }
        JSONObject result = new JSONObject();
        result.set("urlPrefix", urlPrefix);
        return result;
    }

    public AddAppStatisticsSetupResBody addAppStatisticsSetup(BaseRequest<AddAppStatisticsSetupReqBody> request) {
        Map<String, Object> data = BeanUtil.beanToMap(request.getReqContent());
        CominfoResponse<AddAppStatisticsSetupResBody> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADD_APP_STATISTICS_SETUP, data, request.getBase(), new TypeReference<CominfoResponse<AddAppStatisticsSetupResBody>>() {
        });
        response.getData().setAppVersion(request.getBase().getAppVersion());
        return response.getData();
    }

    public AddAppStatisticsSetupResBody addAppStatisticsSetupNew(BaseRequest<AddAppStatisticsSetupReqBody> request) {
        Map<String, Object> data = BeanUtil.beanToMap(request.getReqContent());
        CominfoResponse<AddAppStatisticsSetupResBody> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADD_APP_STATISTICS_SETUP_NEW, data, request.getBase(), new TypeReference<CominfoResponse<AddAppStatisticsSetupResBody>>() {
        });
        response.getData().setAppVersion(request.getBase().getAppVersion());
        return response.getData();
    }
}
