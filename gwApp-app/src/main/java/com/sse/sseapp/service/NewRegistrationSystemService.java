package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.domain.system.*;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.feign.system.*;
import com.sse.sseapp.form.request.GetListingDataListReqBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author : liuxinyu
 * @date : 2023/5/25 9:21
 */
@Service
@Slf4j
public class NewRegistrationSystemService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysProjectSubscriptionFeign iSysProjectSubscriptionFeign;

    @Autowired
    ISysProjectMergeSubscriptionFeign iSysProjectMergeSubscriptionFeign;

    @Autowired
    ISysProjectKCBZRZSubscriptionFeign iSysProjectKCBZRZSubscriptionFeign;

    @Autowired
    ISysProjectKCBTBSubscriptionFeign iSysProjectKCBTBSubscriptionFeign;

    @Autowired
    ISysProjectDrSubscriptionFeign iSysProjectDrSubscriptionFeign;

    @Autowired
    ISysDictDataFeign iSysDictDataFeign;

    @Autowired
    ISysDictTypeFeign iSysDictTypeFeign;

    @Autowired
    MonthReportService monthReportService;
    @Autowired
    RedisService redisService;

    @Autowired
    CommonService commonService;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    String key;

    /**
     * 注册制相关数据转发接口
     *
     * @param getListingDataList 请求入参
     * @return 数据结果
     */
    public JSONObject getListingDataList(BaseRequest<GetListingDataListReqBody> getListingDataList) {
        Map<String, Object> o = new HashMap<>();
        String url = getListingDataList.getReqContent().getUrl();


        int index = 0;
        for (int i = 0; i < 3; i++) {
            index = url.indexOf("/", index) + 1;
        }
        if (index > 0) {
            key = url.substring(index);
        } else {
            throw new AppException("url异常");
        }

        String queryList = sysConfigFeign.getConfigKey("queryList");
        String queryHost = sysConfigFeign.getConfigKey("queryHost");
        String[] queryListArr = queryList.split("\\,");

        if (Arrays.asList(queryListArr).contains(key)) {
            url = queryHost + key;
        } else {
            throw new AppException("url异常");
        }


        String paramsStr = getListingDataList.getReqContent().getParams();
        //1：首页合并接口，2：项目动态接口
        String reuqestType = getListingDataList.getReqContent().getReuqestType();
        //用于区分当reuqestType=2时，调用的是哪个项目动态接口，在当前项目状态翻译时调用对应的方法
        String sqlIdParam = getListingDataList.getReqContent().getSqlId();

        /*
          判断必填参数是否必填
         */
        if (ObjectUtil.isEmpty(url) || ObjectUtil.isEmpty(paramsStr) || ObjectUtil.isEmpty(reuqestType)) {
            throw new AppException("必填参数为空");
        }
        /*
          根据分隔符分别获取参数并存放到map中
         */
        String[] paramsArr = paramsStr.split("\\;");
        Map<String, Object> params = new HashMap<>();
        for (String paramsArrStr : paramsArr) {
            String[] paramsArrStrs = paramsArrStr.split("=");
            if (paramsArrStrs.length == 1) {
                if (!paramsArrStr.startsWith("=")) {
                    params.put(paramsArrStrs[0], "");
                }
            } else if (paramsArrStrs.length == 2) {
                params.put(paramsArrStrs[0], paramsArrStrs[1]);
            }
        }

        List<JSONObject> dataList = monthReportService.getList(HttpRequest.of(getListingDataList.getReqContent().getUrl())
                .method(Method.valueOf("POST"))
                .header("Referer", "http://query.sse.com.cn")
                .form(params)
                .timeout(50000)
                .execute()
                .body());

        if (ObjectUtil.equals(reuqestType, "1") || ObjectUtil.equals(reuqestType, "2")) {
            if (ArrayUtil.isNotEmpty(dataList)) {
                List<Map<String, Object>> dataFormatList = new ArrayList<>();

                for (JSONObject jsonObject : dataList) {
                    dataFormatList.add(BeanUtil.beanToMap(jsonObject));
                }

                String permitId = getListingDataList.getReqContent().getPassId();
                for (Map<String, Object> item : dataFormatList) {
                    //用于区分业务类型
                    String sqlId = null;
                    String stockAuditNum = String.valueOf(item.get("stockAuditNum"));
                    String currStatus = String.valueOf(item.get("currStatus"));
                    //用于存放当前状态翻译值
                    String currStatusDec = null;

                    if (ObjectUtil.equals(reuqestType, "1")) {
                        sqlId = String.valueOf(item.get("sqlId"));
                    } else if (ObjectUtil.equals(reuqestType, "2")) {
                        sqlId = sqlIdParam;
                    }

                    if (ObjectUtil.isNotEmpty(sqlId)) {
                        if (ObjectUtil.equals(AppConstants.STOCK_SQLID, sqlId) || ObjectUtil.equals(AppConstants.STOCK_ZTT_SQLID, sqlId)) {
                            //发行上市项目状态列表接口状态字段是auditStatus，需要重新获取
                            if (ObjectUtil.equals(AppConstants.STOCK_ZTT_SQLID, sqlId)) {
                                currStatus = String.valueOf(item.get("auditStatus"));
                            }
                            currStatusDec = projectStatusDesc(currStatus, item);
                        } else if (ObjectUtil.equals(AppConstants.MERGE_SQLID, sqlId) || ObjectUtil.equals(AppConstants.MERGE_ZTT_SQLID, sqlId)) {
                            currStatusDec = getMergeStatusDesc(currStatus, item, true);
                        } else if (ObjectUtil.equals(AppConstants.KCBZRZ_SQLID, sqlId) || ObjectUtil.equals(AppConstants.KCBZRZ_ZTT_SQLID, sqlId)) {
                            currStatusDec = getKcbzrzStatusDesc(currStatus, item, true);
                        } else if (ObjectUtil.equals(AppConstants.KCBTB_SQLID, sqlId) || ObjectUtil.equals(AppConstants.KCBTB_ZTT_SQLID, sqlId)) {
                            currStatusDec = getKcbtbStatusDesc(currStatus, item, true);
                        } else if (ObjectUtil.equals(AppConstants.DR_SQLID, sqlId) || ObjectUtil.equals(AppConstants.DR_SQLID, sqlId)) {
                            currStatusDec = getDrStatusDesc(currStatus, item, true);
                        }
                    }

                    item.put("currStatusDec", currStatusDec);

                    List<Object> list = new ArrayList<>();
                    //如果用户信息不为空，获取该用户下订阅的项目信息是否包含当前项目
                    if (StrUtil.isNotBlank(permitId)) {
                        if (ObjectUtil.isNotEmpty(sqlId)) {
                            if (ObjectUtil.equals(AppConstants.STOCK_SQLID, sqlId) || ObjectUtil.equals(AppConstants.STOCK_ZTT_SQLID, sqlId)) {
                                SysProjectSubscription subscribe = new SysProjectSubscription();
                                subscribe.setPassId(permitId);
                                subscribe.setProjectId(stockAuditNum);
                                list = ListUtil.toList(iSysProjectSubscriptionFeign.getSubscribeList(subscribe).get("data"));
                            } else if (ObjectUtil.equals(AppConstants.MERGE_SQLID, sqlId) || ObjectUtil.equals(AppConstants.MERGE_ZTT_SQLID, sqlId)) {
                                SysProjectMergeSubscription mergeSubscribe = new SysProjectMergeSubscription();
                                mergeSubscribe.setPassId(permitId);
                                mergeSubscribe.setProjectId(stockAuditNum);
                                list = ListUtil.toList(iSysProjectMergeSubscriptionFeign.getSubscribeList(mergeSubscribe).get("data"));
                            } else if (ObjectUtil.equals(AppConstants.KCBZRZ_SQLID, sqlId) || ObjectUtil.equals(AppConstants.KCBZRZ_ZTT_SQLID, sqlId)) {
                                SysProjectKCBZRZSubscription kcbzrzSubscribe = new SysProjectKCBZRZSubscription();
                                kcbzrzSubscribe.setPassId(permitId);
                                kcbzrzSubscribe.setProjectId(stockAuditNum);
                                list = ListUtil.toList(iSysProjectKCBZRZSubscriptionFeign.getSubscribeList(kcbzrzSubscribe).get("data"));
                            } else if (ObjectUtil.equals(AppConstants.KCBTB_SQLID, sqlId) || ObjectUtil.equals(AppConstants.KCBTB_ZTT_SQLID, sqlId)) {
                                SysProjectKCBTBSubscription kcbtbSubscribe = new SysProjectKCBTBSubscription();
                                kcbtbSubscribe.setPassId(permitId);
                                kcbtbSubscribe.setProjectId(stockAuditNum);
                                list = ListUtil.toList(iSysProjectKCBTBSubscriptionFeign.getSubscribeList(kcbtbSubscribe).get("data"));
                            } else if (ObjectUtil.equals(AppConstants.DR_SQLID, sqlId) || ObjectUtil.equals(AppConstants.DR_ZTT_SQLID, sqlId)) {
                                SysProjectDrSubscription drSubscription = new SysProjectDrSubscription();
                                drSubscription.setPassId(permitId);
                                drSubscription.setProjectId(stockAuditNum);
                                list = ListUtil.toList(iSysProjectDrSubscriptionFeign.getSubscribeList(drSubscription).get("data"));
                            }
                        }

                        if (ObjectUtil.isNotEmpty(list.get(0))) {
                            item.put("subscribe", "1");
                        } else {
                            item.put("subscribe", "0");
                        }
                    } else {
                        item.put("subscribe", "0");
                    }
                }

                o.put("data", dataFormatList);
            } else {
                o.put("data", "");
            }
        } else {
            o.put("data", dataList);
        }
        return JSONUtil.parseObj(o);
    }

    /**
     * 发行上市投资者及保荐人（暂时和投资者一样）项目状态翻译，子状态为空翻译主状态
     */
    public String projectStatusDesc(String mainStatus, Map<String, Object> projectMap) {
        String currStatusDec;
        Map<String, Map<String, DictVO>> dictMap;

        if (ObjectUtil.equals(mainStatus, "3") || ObjectUtil.equals(mainStatus, "9")) {
            dictMap = commonService.getDictFromRedis(AppConstants.SSWHYJG_NUM);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.SSWHYJG_NUM, String.valueOf(projectMap.get("commitiResult")));
        } else if (ObjectUtil.equals(mainStatus, "5")) {
            dictMap = commonService.getDictFromRedis(AppConstants.ZCJG_NUM);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.ZCJG_NUM, String.valueOf(projectMap.get("registeResult")));
        } else if (ObjectUtil.equals(mainStatus, "7")) {
            dictMap = commonService.getDictFromRedis(AppConstants.ZZZZT_NUM);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.ZZZZT_NUM, String.valueOf(projectMap.get("suspendStatus")));
        } else {
            dictMap = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.INVESTOR_PROJECT_STATUS_NUM, mainStatus);
        }
        // 子状态为空翻译主状态
        if (StrUtil.isEmpty(currStatusDec)) {
            dictMap = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.INVESTOR_PROJECT_STATUS_NUM, mainStatus);
        }
        return currStatusDec;
    }

    /**
     * 并购重组项目状态翻译
     */
    public String getMergeStatusDesc(String currentStatus, Map<String, Object> map, boolean subStatusFlag) {
        String currStatusDec;

        Map<String, Map<String, DictVO>> dictMap;
        // 50=注册结果
        if (ObjectUtil.equals(currentStatus, "50")) {
            dictMap = commonService.getDictFromRedis(AppConstants.MERGE_ZCJG_STATUS);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.MERGE_ZCJG_STATUS, String.valueOf(map.get("registeResult")));
        } else {
            // 主状态
            dictMap = commonService.getDictFromRedis(AppConstants.MERGE_MAIN_STATUS);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.MERGE_MAIN_STATUS, currentStatus);
        }

        // 需要对子状态为空情况特殊处理
        if (subStatusFlag) {
            // 子状态为空翻译主状态
            if (StrUtil.isEmpty(currStatusDec)) {
                dictMap = commonService.getDictFromRedis(AppConstants.MERGE_MAIN_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.MERGE_MAIN_STATUS, currentStatus);
            }
        }

        return currStatusDec;
    }

    /**
     * 再融资项目状态翻译
     */
    public String getKcbzrzStatusDesc(String currentStatus, Map<String, Object> map, boolean subStatusFlag) {
        String currStatusDec;

        Map<String, Map<String, DictVO>> dictMap;

        if (ObjectUtil.equals(currentStatus, "50")) {
            // 50=注册结果
            dictMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_ZRZZCJG_STATUS);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBZRZ_ZRZZCJG_STATUS, String.valueOf(map.get("registeResult")));
        } else if (ObjectUtil.equals(currentStatus, "55")) {
            // 55=中止及财报更新
            dictMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_ZRZZZJCBGX_STATUS);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBZRZ_ZRZZZJCBGX_STATUS, String.valueOf(map.get("suspendStatus")));
        } else {
            // 主状态
            dictMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_MAIN_STATUS);
            currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBZRZ_MAIN_STATUS, currentStatus);
        }

        // 需要对子状态为空情况特殊处理
        if (subStatusFlag) {
            // 子状态为空翻译主状态
            if (StrUtil.isEmpty(currStatusDec)) {
                dictMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_MAIN_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBZRZ_MAIN_STATUS, currentStatus);
            }
        }

        return currStatusDec;
    }

    /**
     * 转板上市项目状态翻译
     */
    public String getKcbtbStatusDesc(String currentStatus, Map<String, Object> map, boolean subStatusFlag) {
        String currStatusDec;
        Map<String, Map<String, DictVO>> dictMap;
        switch (currentStatus) {
            case "50":
                // 50=审核结果
                dictMap = commonService.getDictFromRedis(AppConstants.KCBTB_TBSHJG_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBTB_TBSHJG_STATUS, String.valueOf(map.get("auditResult")));
                break;
            case "55":
                // 55=中止及财报更新
                dictMap = commonService.getDictFromRedis(AppConstants.KCBTB_TBZZJCBGX_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBTB_TBZZJCBGX_STATUS, String.valueOf(map.get("suspendStatus")));
                break;
            default:
                // 主状态
                dictMap = commonService.getDictFromRedis(AppConstants.KCBTB_MAIN_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBTB_MAIN_STATUS, currentStatus);
                break;
        }
        // 需要对子状态为空情况特殊处理
        if (subStatusFlag) {
            // 子状态为空翻译主状态
            if (StrUtil.isEmpty(currStatusDec)) {
                dictMap = commonService.getDictFromRedis(AppConstants.KCBTB_MAIN_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.KCBTB_MAIN_STATUS, currentStatus);
            }
        }

        return currStatusDec;
    }

    public String getDrStatusDesc(String currentStatus, Map<String, Object> map, boolean subStatusFlag) {
        String currStatusDec;
        Map<String, Map<String, DictVO>> dictMap;
        switch (currentStatus) {
            case "50":
                // 50=注册结果
                dictMap = commonService.getDictFromRedis(AppConstants.DR_ZCJG_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.DR_ZCJG_STATUS, String.valueOf(map.get("registeResult")));
                break;
            case "55":
                // 55=中止及财报更新
                dictMap = commonService.getDictFromRedis(AppConstants.DRZZJCBGX_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.DRZZJCBGX_STATUS, String.valueOf(map.get("suspendStatus")));
                break;
            default:
                // 主状态
                dictMap = commonService.getDictFromRedis(AppConstants.DR_MAIN_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.DR_MAIN_STATUS, currentStatus);
                break;
        }

        // 需要对子状态为空情况特殊处理
        if (subStatusFlag) {
            // 子状态为空翻译主状态
            if (StrUtil.isEmpty(currStatusDec)) {
                dictMap = commonService.getDictFromRedis(AppConstants.DR_MAIN_STATUS);
                currStatusDec = getConstDescByCode(dictMap, AppConstants.DR_MAIN_STATUS, currentStatus);
            }
        }

        return currStatusDec;
    }

    /**
     * 根据常量类型 常量代码翻译值
     */
    public String getConstDescByCode(Map<String, Map<String, DictVO>> dictMap, String type, String code) {
        if (ObjectUtil.isEmpty(type) || ObjectUtil.isEmpty(code)) {
            return "";
        }
        Map<String, DictVO> cvoHt = dictMap.get(type);
        if (ObjectUtil.isNotEmpty(cvoHt)) {
            DictVO cvo = cvoHt.get(code);
            if (ObjectUtil.isNotEmpty(cvo)) {
                return cvo.getDictSubEntryName();
            }
        }
        return "";
    }

//    /**
//     * 数据字典缓存
//     */
//    public Map<String, Map<String, DictVO>> cacheAllDict() {
//        Map<String, Map<String, DictVO>> dictMap = new HashMap<>();
//        try{
//            List<DictVO> list = init();
//            if(ArrayUtil.isNotEmpty(list)){
//                for(DictVO dvo : list){
//                    Map<String, DictVO> value = dictMap.get(dvo.getDictEntry());
//                    if(null==value){
//                        Map<String, DictVO> sub = new HashMap<>();
//                        sub.put(dvo.getDictSubEntry(), dvo);
//                        dictMap.put(dvo.getDictEntry(), sub);
//                    }else{
//                        value.put(dvo.getDictSubEntry(), dvo);
//                    }
//                }
//            }
//        }catch (Exception e) {
//            throw new AppException(e);
//        }
//        // 缓存数据字典，时间1小时
//        redisService.setCacheObject("NewRegistrationDictList",dictMap,1L, TimeUnit.HOURS);
//
//
//        return dictMap;
//    }
//
//    /**
//     * 从数据库中获取数据字典
//     */
//    public List<DictVO> init(){
//        List<DictVO> result = new ArrayList<>();
//        for (String string : strings) {
//            List<SysDictData> list = (List<SysDictData>) iSysDictDataFeign.dictType(string).get("data");
//            for (SysDictData sysDictData : list) {
//                DictVO dictVO = new DictVO();
//                SysDictType sysDictType = (SysDictType) iSysDictTypeFeign.getInfo(sysDictData.getDictCode()).get("data");
//                dictVO.setDictEntry(sysDictType.getDictType());
//                dictVO.setDictEntryName(sysDictType.getDictName());
//                dictVO.setDictSubEntry(sysDictData.getDictValue());
//                dictVO.setDictSubEntryName(sysDictData.getDictLabel());
//                dictVO.setDictSubEntrySort(sysDictData.getDictSort());
//                dictVO.setDictStatus(sysDictData.getStatus());
//                result.add(dictVO);
//            }
//        }
//        return result;
//    }

}
