package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.core.utils.DateUtils;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import com.sse.sseapp.proxy.sseroadshow.dto.GetRoadShowListByPageDto;
import com.sse.sseapp.redis.service.RedisService;
import com.sse.sseapp.util.VersionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 接口
 *
 * @author zhengyaosheng
 * @date 2023-03-28
 */
@Service
@Slf4j
public class HomeNewService {

    @Autowired
    private ISysConfigFeign sysConfigFeign;

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PersonService personService;

    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 首页推荐接口
     */
    public RespBean<?> getRecommendDataList(BaseRequest<RecommendDataReqBody> body) {
        ReqBaseVO base = body.getBase();
        Map<String, Object> o = new HashMap<>();
        try {
            String pageNo = body.getReqContent().getPageNo();
            if (Objects.isNull(pageNo)) {
                throw new AppException("参数校验失败");
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            int i = 0;
            // 获取15条公告数据
            List<Map<String, Object>> noticeList = getRecommendNoticeList(100, pageNo, 15, "", "", base);
            //如果是第一页数据，需要调用出现一次的公告和今日新股接口
            if ("1".equals(pageNo)) {
                //获取一条最新的上交所新闻
                List<Map<String, Object>> newsList = getRecommendNoticeList(5, "1", 1, "", "createTime|desc,docId|desc", base);
                if (ObjectUtil.isNotEmpty(newsList) && newsList.size() == 1) {
                    resultList.add(newsList.get(0));
                    o.put("sseNewsDocId", newsList.get(0).get("noticeId"));
                }
                //首次放3条公告
                for (Map<String, Object> stringObjectMap : noticeList) {
                    if (i < 3) {
                        resultList.add(stringObjectMap);
                        i++;
                    } else {
                        break;
                    }
                }
                //获取最新直播数据
                Map<String, Object> roadShowList = new HashMap<>();
                roadShowList.put("roadShowList", getRoadShowData(""));
                roadShowList.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_ROADSHOW);
                resultList.add(roadShowList);
                //第二次放3条公告
                for (int j = 3; j < noticeList.size(); j++) {
                    if (i < 6) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                resultList.add(getEntranceConfigForList(AppConstants.EDU_ENTRANCE).get(0));
                //第三次放3条公告
                for (int j = 6; j < noticeList.size(); j++) {
                    if (i < 9) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                //存放一条研究报告公告数据
                List<Map<String, Object>> researchNoticeList = getRecommendNoticeList(6, pageNo, 1, "", "", base);
                if (ObjectUtil.isNotEmpty(researchNoticeList) && researchNoticeList.size() == 1) {
                    resultList.add(researchNoticeList.get(0));
                }
                //第四次放3条公告
                for (int j = 9; j < noticeList.size(); j++) {
                    if (i < 12) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                //存放一条广播剧公告数据
                //第五次放3条公告
                for (int j = 12; j < noticeList.size(); j++) {
                    if (i < 15) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                //存放一条浦江学院公告数据
            } else {
                String sseNewsDocId = body.getReqContent().getSseNewsDocId();
                log.info("===========sseNewsDocId传入参数==================" + sseNewsDocId);
                //非第一页，sseNewsDocId参数时必填的
                if (sseNewsDocId == null || "".equals(sseNewsDocId)) {
                    throw new AppException("非第一页，sseNewsDocId参数时必填的");
                }
                o.put("sseNewsDocId", sseNewsDocId);
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 0) {
                    //首次放3条公告
                    for (Map<String, Object> stringObjectMap : noticeList) {
                        if (i < 3) {
                            resultList.add(stringObjectMap);
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 3) {
                    //第二次放3条公告
                    for (int j = 3; j < noticeList.size(); j++) {
                        if (i < 6) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 6) {
                    //第三次放3条公告
                    for (int j = 6; j < noticeList.size(); j++) {
                        if (i < 9) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                //存放一条研究报告公告数据
                List<Map<String, Object>> researchNoticeList = getRecommendNoticeList(6, pageNo, 1, "", "", base);
                if (ObjectUtil.isNotEmpty(researchNoticeList) && researchNoticeList.size() == 1) {
                    resultList.add(researchNoticeList.get(0));
                }
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 9) {
                    //第四次放3条公告
                    for (int j = 9; j < noticeList.size(); j++) {
                        if (i < 12) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                //存放一条广播剧公告数据
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 12) {
                    //第五次放3条公告
                    for (int j = 12; j < noticeList.size(); j++) {
                        if (i < 15) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                //存放一条浦江学院公告数据
            }
            //删除与第一条重复的上交所新闻公告
            for (int j = 0; j < resultList.size(); j++) {
                Map<String, Object> dataMap = resultList.get(j);
                //如果是第一页有重复数据，第一条不删除，删除除第一条以外的重复数据
                if ("1".equals(pageNo)) {
                    if (j != 0 && dataMap.get("noticeId") != null && dataMap.get("noticeId").equals(o.get("sseNewsDocId"))) {
                        resultList.remove(j);
                        break;
                    }
                } else {
                    if (dataMap.get("noticeId") != null && dataMap.get("noticeId").equals(o.get("sseNewsDocId"))) {
                        resultList.remove(j);
                        break;
                    }
                }
            }
            o.put("noticeList", resultList);
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("首页推荐接口获取失败：{}", e.getMessage());
            throw new AppException("首页推荐接口获取失败!");
        }
    }


    /**
     * 首页推荐接口
     */
    public RespBean<?> getRecommendDataListCMS(BaseRequest<RecommendDataReqBody> body) {
        ReqBaseVO base = body.getBase();
        Map<String, Object> o = new HashMap<>();
        try {
            String pageNo = body.getReqContent().getPageNo();
            if (Objects.isNull(pageNo)) {
                throw new AppException("参数校验失败");
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            int i = 0;
            // 获取15条公告数据
            List<Map<String, Object>> noticeList = getRecommendNoticeListCMS(100, pageNo, 15, "", "", base);
            //如果是第一页数据，需要调用出现一次的公告和今日新股接口
            if ("1".equals(pageNo)) {
                //获取一条最新的上交所新闻
                List<Map<String, Object>> newsList = getRecommendNoticeListCMS(5, "1", 1, "", "createTime|desc,docId|desc", base);
                if (ObjectUtil.isNotEmpty(newsList) && newsList.size() == 1) {
                    resultList.add(newsList.get(0));
                    o.put("sseNewsDocId", newsList.get(0).get("noticeId"));
                }
                //首次放3条公告
                for (Map<String, Object> stringObjectMap : noticeList) {
                    if (i < 3) {
                        resultList.add(stringObjectMap);
                        i++;
                    } else {
                        break;
                    }
                }
                //获取最新直播数据
                Map<String, Object> roadShowList = new HashMap<>();
                roadShowList.put("roadShowList", getRoadShowData(""));
                roadShowList.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_ROADSHOW);
                resultList.add(roadShowList);
                //第二次放3条公告
                for (int j = 3; j < noticeList.size(); j++) {
                    if (i < 6) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                resultList.add(getEntranceConfigForList(AppConstants.EDU_ENTRANCE).get(0));
                //第三次放3条公告
                for (int j = 6; j < noticeList.size(); j++) {
                    if (i < 9) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                //存放一条研究报告公告数据
                List<Map<String, Object>> researchNoticeList = getRecommendNoticeListCMS(6, pageNo, 1, "", "", base);
                if (ObjectUtil.isNotEmpty(researchNoticeList) && researchNoticeList.size() == 1) {
                    resultList.add(researchNoticeList.get(0));
                }
                //第四次放3条公告
                for (int j = 9; j < noticeList.size(); j++) {
                    if (i < 12) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                //存放一条广播剧公告数据
                //第五次放3条公告
                for (int j = 12; j < noticeList.size(); j++) {
                    if (i < 15) {
                        resultList.add(noticeList.get(j));
                        i++;
                    } else {
                        break;
                    }
                }
                //存放一条浦江学院公告数据
            } else {
                String sseNewsDocId = body.getReqContent().getSseNewsDocId();
                log.info("===========sseNewsDocId传入参数==================" + sseNewsDocId);
                //非第一页，sseNewsDocId参数时必填的
                if (sseNewsDocId == null || "".equals(sseNewsDocId)) {
                    throw new AppException("非第一页，sseNewsDocId参数时必填的");
                }
                o.put("sseNewsDocId", sseNewsDocId);
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 0) {
                    //首次放3条公告
                    for (Map<String, Object> stringObjectMap : noticeList) {
                        if (i < 3) {
                            resultList.add(stringObjectMap);
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 3) {
                    //第二次放3条公告
                    for (int j = 3; j < noticeList.size(); j++) {
                        if (i < 6) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 6) {
                    //第三次放3条公告
                    for (int j = 6; j < noticeList.size(); j++) {
                        if (i < 9) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                //存放一条研究报告公告数据
                List<Map<String, Object>> researchNoticeList = getRecommendNoticeListCMS(6, pageNo, 1, "", "", base);
                if (ObjectUtil.isNotEmpty(researchNoticeList) && researchNoticeList.size() == 1) {
                    resultList.add(researchNoticeList.get(0));
                }
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 9) {
                    //第四次放3条公告
                    for (int j = 9; j < noticeList.size(); j++) {
                        if (i < 12) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                //存放一条广播剧公告数据
                if (ObjectUtil.isNotEmpty(noticeList) && noticeList.size() > 12) {
                    //第五次放3条公告
                    for (int j = 12; j < noticeList.size(); j++) {
                        if (i < 15) {
                            resultList.add(noticeList.get(j));
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                //存放一条浦江学院公告数据
            }
            //删除与第一条重复的上交所新闻公告
            for (int j = 0; j < resultList.size(); j++) {
                Map<String, Object> dataMap = resultList.get(j);
                //如果是第一页有重复数据，第一条不删除，删除除第一条以外的重复数据
                if ("1".equals(pageNo)) {
                    if (j != 0 && dataMap.get("noticeId") != null && dataMap.get("noticeId").equals(o.get("sseNewsDocId"))) {
                        resultList.remove(j);
                        break;
                    }
                } else {
                    if (dataMap.get("noticeId") != null && dataMap.get("noticeId").equals(o.get("sseNewsDocId"))) {
                        resultList.remove(j);
                        break;
                    }
                }
            }
            o.put("noticeList", resultList);
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("首页推荐CMS接口获取失败：{}", e.getMessage());
            throw new AppException("首页推荐CMS接口获取失败!");
        }
    }


    private List<Map<String, Object>> getRecommendNoticeListCMS(Integer noticeType, String pageNo, Integer pageSize, String stockCode, String orderByStr, ReqBaseVO base) throws Exception {
        Map<String, String> map = commonService.getActive("application");
        List<AnnouncementResBody> noticeList = getRecommendNoticeAllListCMS(noticeType, pageNo, pageSize, stockCode, orderByStr, base);
        List<Map<String, Object>> noticeDataList = new ArrayList<>();
        for (AnnouncementResBody noticeMap : noticeList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("noticeId", noticeMap.getDocId());
            dataMap.put("noticeTitle", noticeMap.getDocTitle());
            String channelId = noticeMap.getChannelId() == null ? "" : noticeMap.getChannelId();
            dataMap.put("noticeType", getNoticeTypeByChannelId(channelId, noticeMap.getExtGGLX(), "", map));
            String appVersion = base.getAppVersion();
            String noticeDate = null;
            if (StringUtils.isNotBlank(appVersion)) {
                if(!VersionUtil.geTargetVersion(appVersion, "5.3.0")) {
                    noticeDate = DateUtils.timeStrFormat(noticeMap.getCmsOpDate() == null ? "" : noticeMap.getCmsOpDate());
                } else {
                    noticeDate = DateUtils.timeStrFormat(noticeMap.getCreateTime() == null ? "" : noticeMap.getCreateTime());
                }
            } else {
                noticeDate = DateUtils.timeStrFormat(noticeMap.getCreateTime() == null ? "" : noticeMap.getCreateTime());
            }
            dataMap.put("noticeDate", noticeDate);
            dataMap.put("noticeContent", noticeMap.getExtINTRODUCTION());
            dataMap.put("gpdm", noticeMap.getStockcode());
            dataMap.put("startTime", "");
            dataMap.put("noticeStatus", "");
            dataMap.put("noticeFileType", noticeMap.getDocType());
            dataMap.put("endDate", "");
            dataMap.put("dateFormat", "");
            //研究报告、上市公司公告随机获取一张图片作为公告图片
            if (Util.getObjStrV(map.get("sseCompanyNoticeChannelId")).contains(channelId) ||
                    Util.getObjStrV(map.get("sseResearchNoticeChannelId")).contains(channelId)) {
                if (ObjectUtil.isNotEmpty(noticeMap.getDocSize())) {
                    String fileSize = noticeMap.getDocSize();
                    dataMap.put("noticeImageUrl", getRandomImageForNotice(fileSize));
                    if (Util.getObjStrV(map.get("sseResearchNoticeChannelId")).contains(channelId)) {
                        dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICERESEARCH);
                    } else {
                        dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICEIMAGE);
                    }
                } else {
                    dataMap.put("noticeImageUrl", "");
                    dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICE);
                }
            } else {
                dataMap.put("noticeImageUrl", "");
                dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICE);
            }

            dataMap.put("noticeDetailUrl", noticeMap.getDocURL());
            dataMap.put("companyName", "");
            dataMap.put("endTime", "");
            dataMap.put("eduViedoRequestUrl", "");
            noticeDataList.add(dataMap);
        }
        return noticeDataList;
    }

    public RespBean<?> getSseVoicesDataListCMS(BaseRequest<SseVoicesReqBody> body) {
        ReqBaseVO base = body.getBase();
        try {
            log.info("===========APP改版首页上交所之声CMS接口开始==================");
            Integer pageNo = 1;
            if (ObjectUtil.isNotEmpty(body.getReqContent().getPageNo())) {
                pageNo = body.getReqContent().getPageNo();
            }
            if (Objects.isNull(body.getReqContent().getNoticeType())) {
                throw new AppException("参数校验失败");
            }
            //公告类型，2：全部  5：上交所新闻 7:上交所公告 8：监管 9:上交所新闻发布会
            Integer noticeType = body.getReqContent().getNoticeType();
            Integer pageSize = body.getReqContent().getPageSize();
            if (ObjectUtil.isEmpty(pageSize)) {
                if (noticeType == 2) {
                    pageSize = 100;
                } else {
                    pageSize = 20;
                }
            }
            log.info("======pageNo传参" + pageNo + "===noticeType传参" + noticeType + "===pageSize传参" + pageSize + "=======");
            Map<String, Object> o = new HashMap<>();
            //按照公告类型查询公告信息
            List<AnnouncementResBody> noticeList = getRecommendNoticeAllListCMS(noticeType, pageNo.toString(), pageSize, "", "", base);
            List<Map<String, Object>> resultList = new ArrayList<>();
            if (noticeList != null && noticeList.size() > 0) {
                Map<String, String> map = commonService.getActive("application");
                //循环处理公告数据，返回接口需要的字段
                for (AnnouncementResBody noticeMap : noticeList) {
                    Map<String, Object> noticeDataMap = new HashMap<>();
                    noticeDataMap.put("noticeTitle", noticeMap.getDocTitle());
                    String channelId = Objects.isNull(noticeMap.getChannelId()) ? "" : noticeMap.getChannelId();
                    noticeDataMap.put("noticeType", getNoticeTypeByChannelId(channelId, "", "voice", map));
                    String noticeDate = Objects.isNull(noticeMap.getCmsOpDate()) ? "" : noticeMap.getCmsOpDate();
                    noticeDataMap.put("noticeDate", DateUtils.transferFormat(noticeDate, "yyyy/MM/dd"));
                    String noticeArticleDate = Objects.isNull(noticeMap.getCreateTime()) ? "" : noticeMap.getCreateTime();
                    if (noticeArticleDate.length() > 10) {
                        noticeArticleDate = noticeArticleDate.substring(0, 10);
                    }
                    noticeArticleDate = DateUtils.timeDateFormat(noticeArticleDate, "yyyy-MM-dd", "yyyy/MM/dd");
                    noticeDataMap.put("noticeDateWeek", DateUtils.dateToWeek(noticeArticleDate));
                    noticeDataMap.put("noticeTime", DateUtils.transferFormat(noticeDate, "HH:mm"));
                    noticeDataMap.put("noticeContent", noticeMap.getExtTYPE());
                    noticeDataMap.put("noticeDetailUrl", noticeMap.getDocURL());
                    noticeDataMap.put("noticeId", noticeMap.getDocId());
                    noticeDataMap.put("noticeFileType", noticeMap.getDocType());
                    noticeDataMap.put("gpdm", noticeMap.getStockcode());
                    noticeDataMap.put("noticeArticleDate", noticeArticleDate);
                    resultList.add(noticeDataMap);
                }
            }
            o.put("noticeList", resultList);
            log.info("===========APP改版首页上交所之声CMS接口结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("APP改版首页上交所之声CMS接口异常", e.fillInStackTrace());
            throw new AppException("接口调用失败");
        }
    }

    /**
     * app改版公告相关接口CMS
     *
     * @param noticeType 公告类型：1-首页-推荐相关公告 2:首页-上交所之声相关公告 3:首页-7X24相关公告 4：首页-自选相关公告 5：上交所新闻
     * @param pageNo     要查询的页数
     */
    public List<AnnouncementResBody> getRecommendNoticeAllListCMS(Integer noticeType, String pageNo,
                                                                  Integer pageSize, String stockCode, String orderByStr, ReqBaseVO base) {
        Map<String, Object> param = new HashMap<>();
        param.put("pageNo", pageNo);
        param.put("channelId", commonService.getActive("getRecommendNoticeList").get(noticeType.toString()));
        if (ObjectUtil.isEmpty(pageSize)) {
            param.put("pageSize", 20);
        } else {
            param.put("pageSize", pageSize);
        }
        if (ObjectUtil.isNotEmpty(orderByStr)) {
            param.put("order", orderByStr);
        } else {
            if (StringUtils.isNotBlank(base.getAppVersion())) {
                if(!VersionUtil.geTargetVersion(base.getAppVersion(), "5.3.0")) {
                    if(noticeType == 9){
                        param.put("order", "createTime|desc");
                    } else {
                        param.put("order", "cmsOpDate|desc");
                    }
                } else {
                    //2023.8.24 修改排序 param.put("order", "cmsOpDate|desc,docId|desc");
                    if(noticeType == 6 ||noticeType == 7 || noticeType == 8 || noticeType == 9 ){
                        param.put("order", "createTime|desc");
                    } else {
                        param.put("order", "cmsOpDate|desc");
                    }
                }
            } else {
                //2023.8.24 修改排序 param.put("order", "cmsOpDate|desc,docId|desc");
                if (noticeType == 6 || noticeType == 7 || noticeType == 8 || noticeType == 9) {
                    param.put("order", "createTime|desc");
                } else {
                    param.put("order", "cmsOpDate|desc");
                }
            }
        }
        if (ObjectUtil.isNotEmpty(stockCode)) {
            param.put("stockcode", stockCode);
        }
        param.put("siteId", "28");
        SoaResponse<AnnouncementResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, base, new TypeReference<SoaResponse<AnnouncementResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result.getList();
    }

    /**
     * 通过系统配置ID获取配置内容并将配置内容转换为list
     *
     * @param configNo
     */
    private List<Map<String, Object>> getEntranceConfigForList(String configNo) {
        List<Map<String, Object>> resultList = new ArrayList<>(0);
        String configValue = this.sysConfigFeign.getConfigKey(configNo);
        if (ObjectUtil.isEmpty(configValue)) {
            return resultList;
        }
        String[] configValues = configValue.split(";");
        for (String value : configValues) {
            String[] comfigValueItems = value.split("\\|");
            // 异常配置
            if (comfigValueItems.length < 2) {
                continue;
            }
            // 图片入口
            if (comfigValueItems.length == 2) {
                Map<String, Object> configValueMap = new HashMap<>();
                // 入口图片
                configValueMap.put("stockName", comfigValueItems[0]);
                // 入口请求地址
                configValueMap.put("stockCode", comfigValueItems[1]);
                resultList.add(configValueMap);
            } else {
                // 复杂入口，例：视频入口
                Map<String, String> map = commonService.getActive("application");
                Map<String, Object> configValueMap = new HashMap<>();
                // 入口图片
                configValueMap.put("noticeImageUrl", comfigValueItems[0]);
                // 入口请求地址
                configValueMap.put("eduViedoRequestUrl", comfigValueItems[1]);
                // 入口为视频时，视频标题
                configValueMap.put("noticeTitle", comfigValueItems[2]);
                // 入口为视频时，视频播放地址
                configValueMap.put("noticeDetailUrl", comfigValueItems[3]);
                configValueMap.put("noticeId", "");
                configValueMap.put("noticeType", map.get("sseEduName"));
                configValueMap.put("noticeDate", "");
                configValueMap.put("noticeContent", "");
                configValueMap.put("gpdm", "");
                // 入口为视频时，视频时长
                configValueMap.put("startTime", comfigValueItems[4]);
                configValueMap.put("endTime", "");
                configValueMap.put("noticeStatus", "");
                configValueMap.put("noticeFileType", map.get("sseEduType"));
                configValueMap.put("companyName", "");
                configValueMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_EDU);
                configValueMap.put("endDate", "");
                configValueMap.put("dateFormat", "");
                resultList.add(configValueMap);
            }
        }
        return resultList;
    }

    private List<Map<String, Object>> getRecommendNoticeList(Integer noticeType, String pageNo, Integer pageSize, String stockCode, String orderByStr, ReqBaseVO base) throws Exception {
        Map<String, String> map = commonService.getActive("application");
        List<AnnouncementResBody> noticeList = getRecommendNoticeAllList(noticeType, pageNo, pageSize, stockCode, orderByStr, base);
        List<Map<String, Object>> noticeDataList = new ArrayList<>();
        for (AnnouncementResBody noticeMap : noticeList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("noticeId", noticeMap.getDocId());
            dataMap.put("noticeTitle", noticeMap.getDocTitle());
            String channelId = noticeMap.getChannelId() == null ? "" : noticeMap.getChannelId();
            dataMap.put("noticeType", getNoticeTypeByChannelId(channelId, noticeMap.getExtGGLX(), "", map));
            String noticeDate = null;
            if (StringUtils.isNotBlank(base.getAppVersion())) {
                if (!VersionUtil.geTargetVersion(base.getAppVersion(), "5.3.0")) {
                    noticeDate = DateUtils.timeStrFormat(noticeMap.getCmsOpDate() == null ? "" : noticeMap.getCmsOpDate());
                } else {
                    noticeDate = DateUtils.timeStrFormat(noticeMap.getCreateTime() == null ? "" : noticeMap.getCreateTime());
                }
            } else {
                noticeDate = DateUtils.timeStrFormat(noticeMap.getCreateTime() == null ? "" : noticeMap.getCreateTime());
            }
            dataMap.put("noticeDate", noticeDate);
            dataMap.put("noticeContent", noticeMap.getExtINTRODUCTION());
            dataMap.put("gpdm", noticeMap.getStockcode());
            dataMap.put("startTime", "");
            dataMap.put("noticeStatus", "");
            dataMap.put("noticeFileType", noticeMap.getDocType());
            dataMap.put("endDate", "");
            dataMap.put("dateFormat", "");
            //研究报告、上市公司公告随机获取一张图片作为公告图片
            if (Util.getObjStrV(map.get("sseCompanyNoticeChannelId")).contains(channelId) ||
                    Util.getObjStrV(map.get("sseResearchNoticeChannelId")).contains(channelId)) {
                if (ObjectUtil.isNotEmpty(noticeMap.getDocSize())) {
                    String fileSize = noticeMap.getDocSize();
                    dataMap.put("noticeImageUrl", getRandomImageForNotice(fileSize));
                    if (Util.getObjStrV(map.get("sseResearchNoticeChannelId")).contains(channelId)) {
                        dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICERESEARCH);
                    } else {
                        dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICEIMAGE);
                    }
                } else {
                    dataMap.put("noticeImageUrl", "");
                    dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICE);
                }
            } else {
                dataMap.put("noticeImageUrl", "");
                dataMap.put("noticeDataType", AppConstants.RECOMMEND_NOTICE_DATA_TYPE_NOTICE);
            }

            dataMap.put("noticeDetailUrl", noticeMap.getDocURL());
            dataMap.put("companyName", "");
            dataMap.put("endTime", "");
            dataMap.put("eduViedoRequestUrl", "");
            noticeDataList.add(dataMap);
        }
        return noticeDataList;
    }

    public String getNoticeTypeByChannelId(String channelId, Object extGGLX, String sourceType, Map<String, String> map) {
        if (Util.getObjStrV(map.get("sseNewsChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseNewsNameVoice"));
        } else if (Util.getObjStrV(map.get("sseNoticeChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseNoticeName"));
        } else if (Util.getObjStrV(map.get("sseSuperviseDynamicChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseSuperviseDynamicName"));
        } else if (Util.getObjStrV(map.get("sseSuperviseMeasuresChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseSuperviseMeasuresName"));
        } else if (Util.getObjStrV(map.get("sseSuperviseInquiryChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseSuperviseInquiryName"));
        } else if (Util.getObjStrV(map.get("sseCompanyNoticeChannelId")).contains(channelId)) {
            //推荐显示公告子类，7X24显示为公司公告,20210907推荐也显示为公司公告
            return Util.getObjStrV(map.get("sseCompanyNoticeName"));
        } else if (Util.getObjStrV(map.get("sseNewsReleaseChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseNewsReleaseName"));
        } else if (Util.getObjStrV(map.get("sseResearchNoticeChannelId")).contains(channelId)) {
            return Util.getObjStrV(map.get("sseResearchNoticeName"));
        }
        return "";
    }

    /**
     * 获取最新一天路演直播、仪式、投资教育培训直播数据
     */
    public List<RoadShowDataResBody> getRoadShowData(String stockCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("typeList", "1|2|3|4|5|7|8|11|25|13|20");
        if (ObjectUtil.isNotEmpty(stockCode)) {
            param.put("stockCode", stockCode);
        }
        param.put("pageIndex", 1);
        param.put("pageSize", 100);
        // 获取 获取最新一天路演直播、仪式、投资教育培训直播数据
        GetRoadShowListByPageDto<InternalRoadShowResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_INTERNAL_ROAD_SHOW_LIST, param, null, new TypeReference<GetRoadShowListByPageDto<InternalRoadShowResBody>>() {
        });

        if (!Objects.equals("1", StrUtil.toString(result.getStatus()))) {
            throw new AppException(result.getReason());
        }
        List<InternalRoadShowResBody> raodShowList = result.getRoadshows();
        if("prod".equals(this.active)){
            raodShowList.forEach(InternalRoadShowResBody-> InternalRoadShowResBody.setDetailUrl(InternalRoadShowResBody.getDetailUrl().replace("http","https")));
        }
        //存放最新一天路演相关数据
        List<InternalRoadShowResBody> newDayRoadShowList = new ArrayList<>();
        //存放最新一天进行中的相关直播
        List<RoadShowDataResBody> newDayOngoingList = new ArrayList<>();
        //存放最新一天未开始的直播
        List<RoadShowDataResBody> newDayNostartList = new ArrayList<>();
        //存放最新一天已结束的直播
        List<RoadShowDataResBody> newDayEndList = new ArrayList<>();
        //最终返回路演直播数据
        List<RoadShowDataResBody> returnList = new ArrayList<>();
        if (raodShowList == null || raodShowList.size() == 0) {
            return null;
        }
        //获取路演相关直播最新开始时间
        String localDateTime = null;
        for (InternalRoadShowResBody roadShowMap : raodShowList) {
            if (ObjectUtil.isNotEmpty(roadShowMap.getStartDate())) {
                String localDateTimeNext = roadShowMap.getStartDate();
                if (localDateTime != null) {
                    if (localDateTimeNext != null && DateUtils.compare_date(localDateTime, localDateTimeNext, "yyyy-MM-dd") < 0) {
                        localDateTime = localDateTimeNext;
                    }
                } else {
                    localDateTime = localDateTimeNext;
                }
            }
        }
        //只获取最新一天的路演相关直播
        for (InternalRoadShowResBody roadShowMap : raodShowList) {
            if (ObjectUtil.isNotEmpty(roadShowMap.getStartDate())) {
                String localDateTimeNext = roadShowMap.getStartDate();
                if (localDateTime != null && localDateTimeNext != null && DateUtils.compare_date(localDateTime, localDateTimeNext, "yyyy-MM-dd") == 0) {
                    newDayRoadShowList.add(roadShowMap);
                }
            }
        }
        //培训视频只保留投教培训,type_id：14董秘资格培训，15业务培训，26科创板专题培训,并根据状态存放到对应的list中
        for (InternalRoadShowResBody roadShowMap : newDayRoadShowList) {
            RoadShowDataResBody roadShow = new RoadShowDataResBody();
            roadShow.setNoticeType(roadShowMap.getTypeName());
            roadShow.setNoticeTitle(roadShowMap.getTitle());
            roadShow.setStartTime(roadShowMap.getStartTime());
            roadShow.setEndTime(roadShowMap.getEndTime());
            roadShow.setNoticeDate(roadShowMap.getStartDate());
            roadShow.setNoticeStatus(roadShowMap.getStatus());
            roadShow.setNoticeId(roadShowMap.getId());
            roadShow.setGpdm(roadShowMap.getStockCode());
            roadShow.setNoticeContent(roadShowMap.getTitleImage());
            roadShow.setNoticeImageUrl(roadShowMap.getHomeImage());
            roadShow.setNoticeDetailUrl(roadShowMap.getDetailUrl());
            roadShow.setCompanyName(roadShowMap.getUname());
            roadShow.setNoticeFileType("");
            roadShow.setEduViedoRequestUrl("");
            roadShow.setEndDate(roadShowMap.getEndDate());
            roadShow.setDateFormat(roadShowMap.getDateFormat());
            if (roadShowMap.getStatus() != null) {
                if ("1".equals(roadShowMap.getStatus())) {
                    newDayNostartList.add(roadShow);
                } else if ("2".equals(roadShowMap.getStatus())) {
                    newDayOngoingList.add(roadShow);
                } else if ("3".equals(roadShowMap.getStatus())) {
                    newDayEndList.add(roadShow);
                }
            }
        }
        //按照开始时间正序排序
        //按照startTime字段升序排序
        newDayNostartList.sort((o1, o2) -> {
            String localDateTime1 = o1 != null ? o1.getStartTime() : "";
            String localDateTime2 = o2.getStartTime() != null ? o2.getStartTime() : "";
            return DateUtils.compare_date(localDateTime1, localDateTime2, "HH:mm");
        });
        //按照startTime字段升序排序
        newDayOngoingList.sort((o1, o2) -> {
            String localDateTime12 = o1.getStartTime() != null ? o1.getStartTime() : "";
            String localDateTime2 = o2.getStartTime() != null ? o2.getStartTime() : "";
            return DateUtils.compare_date(localDateTime12, localDateTime2, "HH:mm");
        });
        //按照startTime字段升序排序
        newDayEndList.sort((o1, o2) -> {
            String localDateTime13 = o1.getStartTime() != null ? o1.getStartTime() : "";
            String localDateTime2 = o2.getStartTime() != null ? o2.getStartTime() : "";
            return DateUtils.compare_date(localDateTime13, localDateTime2, "HH:mm");
        });
        returnList.addAll(newDayOngoingList);
        returnList.addAll(newDayNostartList);
        returnList.addAll(newDayEndList);
        return removeDuplicateOrder(returnList);
    }

    /**
     * 按照路演id给路演直播去重
     *
     * @param orderList
     */
    public List<RoadShowDataResBody> removeDuplicateOrder(List<RoadShowDataResBody> orderList) {
        Set<RoadShowDataResBody> set = new TreeSet<>((a, b) -> {
            int compareToResult = 1;
            if (ObjectUtil.isNotEmpty(a.getNoticeId()) && ObjectUtil.isNotEmpty(b.getNoticeId())
                    && ObjectUtil.equals(a.getNoticeId(), b.getNoticeId())) {
                compareToResult = 0;
            }
            return compareToResult;
        });
        set.addAll(orderList);
        return new ArrayList<>(set);
    }

    public RespBean<?> getResearchReportList(BaseRequest<RecommendDataReqBody> body) {
        ReqBaseVO base = body.getBase();
        try {
            log.info("===========查询研究报告公告数据==================");
            Map<String, Object> o = new HashMap<>();
            String pageNo = body.getReqContent().getPageNo();
            log.info("===========pageNo传入参数==================" + pageNo);
            if (Objects.isNull(pageNo)) {
                return RespBean.error("参数校验失败！");
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            //存放一条研究报告公告数据
            List<Map<String, Object>> researchNoticeList = getRecommendNoticeList(6, pageNo, 20, "", "", base);
            if (researchNoticeList != null && researchNoticeList.size() > 0) {
                for (Map<String, Object> researchNoticeMap : researchNoticeList) {
                    resultList.add(researchNoticeMap);
                }
            }
            o.put("noticeList", resultList);
            log.info("===========查询研究报告公告数据结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("查询研究报告公告数据接口异常", e.fillInStackTrace());
            throw new AppException("查询研究报告公告数据接口异常!");
        }
    }


    public RespBean<?> getResearchReportListCMS(BaseRequest<RecommendDataReqBody> body) {
        ReqBaseVO base = body.getBase();
        try {
            log.info("===========查询研究报告公告数据==================");
            Map<String, Object> o = new HashMap<>();
            String pageNo = body.getReqContent().getPageNo();
            log.info("===========pageNo传入参数==================" + pageNo);
            if (Objects.isNull(pageNo)) {
                return RespBean.error("参数校验失败！");
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            //存放一条研究报告公告数据
            List<Map<String, Object>> researchNoticeList = getRecommendNoticeListCMS(6, pageNo, 20, "", "", base);
            if (researchNoticeList != null && researchNoticeList.size() > 0) {
                for (Map<String, Object> researchNoticeMap : researchNoticeList) {
                    resultList.add(researchNoticeMap);
                }
            }
            o.put("noticeList", resultList);
            log.info("===========查询研究报告公告数据结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("查询研究报告公告数据接口异常", e.fillInStackTrace());
            throw new AppException("查询研究报告公告数据接口异常!");
        }
    }

    public RespBean<?> getSseVoicesDataList(BaseRequest<SseVoicesReqBody> body) {
        ReqBaseVO base = body.getBase();
        try {
            log.info("===========APP改版首页上交所之声接口开始==================");
            Integer pageNo = 1;
            if (ObjectUtil.isNotEmpty(body.getReqContent().getPageNo())) {
                pageNo = body.getReqContent().getPageNo();
            }
            if (Objects.isNull(body.getReqContent().getNoticeType())) {
                throw new AppException("参数校验失败");
            }
            //公告类型，2：全部  5：上交所新闻 7:上交所公告 8：监管 9:上交所新闻发布会
            Integer noticeType = body.getReqContent().getNoticeType();
            Integer pageSize = body.getReqContent().getPageSize();
            if (ObjectUtil.isEmpty(pageSize)) {
                if (noticeType == 2) {
                    pageSize = 100;
                } else {
                    pageSize = 20;
                }
            }
            log.info("======pageNo传参" + pageNo + "===noticeType传参" + noticeType + "===pageSize传参" + pageSize + "=======");
            Map<String, Object> o = new HashMap<>();
            //按照公告类型查询公告信息
            List<AnnouncementResBody> noticeList = getRecommendNoticeAllList(noticeType, pageNo.toString(), pageSize, "", "", base);
            List<Map<String, Object>> resultList = new ArrayList<>();
            if (noticeList != null && noticeList.size() > 0) {
                Map<String, String> map = commonService.getActive("application");
                //循环处理公告数据，返回接口需要的字段
                for (AnnouncementResBody noticeMap : noticeList) {
                    Map<String, Object> noticeDataMap = new HashMap<>();
                    noticeDataMap.put("noticeTitle", noticeMap.getDocTitle());
                    String channelId = Objects.isNull(noticeMap.getChannelId()) ? "" : noticeMap.getChannelId();
                    noticeDataMap.put("noticeType", getNoticeTypeByChannelId(channelId, "", "voice", map));
                    String noticeDate = Objects.isNull(noticeMap.getCmsOpDate()) ? "" : noticeMap.getCmsOpDate();
                    noticeDataMap.put("noticeDate", DateUtils.transferFormat(noticeDate, "yyyy/MM/dd"));
                    String noticeArticleDate = Objects.isNull(noticeMap.getCreateTime()) ? "" : noticeMap.getCreateTime();
                    if (noticeArticleDate.length() > 10) {
                        noticeArticleDate = noticeArticleDate.substring(0, 10);
                    }
                    noticeArticleDate = DateUtils.timeDateFormat(noticeArticleDate, "yyyy-MM-dd", "yyyy/MM/dd");
                    noticeDataMap.put("noticeDateWeek", DateUtils.dateToWeek(noticeArticleDate));
                    noticeDataMap.put("noticeTime", DateUtils.transferFormat(noticeDate, "HH:mm"));
                    noticeDataMap.put("noticeContent", noticeMap.getExtTYPE());
                    noticeDataMap.put("noticeDetailUrl", noticeMap.getDocURL());
                    noticeDataMap.put("noticeId", noticeMap.getDocId());
                    noticeDataMap.put("noticeFileType", noticeMap.getDocType());
                    noticeDataMap.put("gpdm", noticeMap.getStockcode());
                    noticeDataMap.put("noticeArticleDate", noticeArticleDate);
                    resultList.add(noticeDataMap);
                }
            }
            o.put("noticeList", resultList);
            log.info("===========APP改版首页上交所之声接口结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("APP改版首页上交所之声接口异常", e.fillInStackTrace());
            throw new AppException("接口调用失败");
        }
    }

    /**
     * app改版公告相关接口
     *
     * @param noticeType 公告类型：1-首页-推荐相关公告 2:首页-上交所之声相关公告 3:首页-7X24相关公告 4：首页-自选相关公告 5：上交所新闻
     * @param pageNo     要查询的页数
     */
    public List<AnnouncementResBody> getRecommendNoticeAllList(Integer noticeType, String pageNo,
                                                               Integer pageSize, String stockCode, String orderByStr, ReqBaseVO base) {
        Map<String, Object> param = new HashMap<>();
        param.put("pageNo", pageNo);
        param.put("channelId", commonService.getActive("getRecommendNoticeList").get(noticeType.toString()));
        if (ObjectUtil.isEmpty(pageSize)) {
            param.put("pageSize", 20);
        } else {
            param.put("pageSize", pageSize);
        }
        if (ObjectUtil.isNotEmpty(orderByStr)) {
            param.put("order", orderByStr);
        } else {
            if (StringUtils.isNotBlank(base.getAppVersion())) {
                if(!VersionUtil.geTargetVersion(base.getAppVersion(), "5.3.0")) {
                    if(noticeType == 9){
                        param.put("order", "createTime|desc");
                    } else {
                        param.put("order", "cmsOpDate|desc");
                    }
                } else {
                    //2023.8.24 修改排序 param.put("order", "cmsOpDate|desc,docId|desc");
                    if(noticeType == 6 ||noticeType == 7 || noticeType == 8 || noticeType == 9 ){
                        param.put("order", "createTime|desc");
                    } else {
                        param.put("order", "cmsOpDate|desc");
                    }
                }
            } else {
                //2023.8.24 修改排序 param.put("order", "cmsOpDate|desc,docId|desc");
                if(noticeType == 6 ||noticeType == 7 || noticeType == 8 || noticeType == 9 ){
                    param.put("order", "createTime|desc");
                } else {
                    param.put("order", "cmsOpDate|desc");
                }
            }

        }
        if (ObjectUtil.isNotEmpty(stockCode)) {
            param.put("stockcode", stockCode);
        }
        param.put("siteId", "28");
        SoaResponse<AnnouncementResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_RECOMMEND_NOTICE_LIST, param, base, new TypeReference<SoaResponse<AnnouncementResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result.getList();
    }

    /**
     * 随机获取一张公告图片
     *
     * @param noticeId
     */
    public String getRandomImageForNotice(String noticeId) {
        Map<String, String> map = commonService.getActive("application");
        String sseNoticeImageUrl = map.get("sseNoticeImageUrl");
        String sseNoticeImageSuffix = map.get("sseNoticeImageSuffix");
        long imageCount = this.sysConfigFeign.getConfigKey(AppConstants.APP_IMAGE_COUNT) == null ? 0L : Long.parseLong(this.sysConfigFeign.getConfigKey(AppConstants.APP_IMAGE_COUNT));
        if (imageCount == 0) {
            return "";
        }
        long noticeIdValue = Long.parseLong(noticeId);
        long imageName = noticeIdValue % imageCount + 1;
        return sseNoticeImageUrl + imageName + sseNoticeImageSuffix;
    }

    /**
     * 今日新股接口
     */
    public RespBean<?> getNewSharesDataList(BaseRequest<RecommendDataReqBody> baseRequest) {
        try {
            //获取今日新股数据
            Map<String, Object> params = new HashMap<>();
            // 不分页
            params.put("pageSize", 0);
            params.put("onlineIssuanceStartDate", DateUtils.getNowDateStr());
            params.put("onlineIssuanceEndDate", DateUtils.getNowDateStr());
            params.put("order", "onlineIssuanceDate|asc,id|asc");
            params.put("pageNo", 1);
            params.put("tradeMarket", "SH");
            //2023.5.25 mateng 修改进入新股获取报错
            params.put("token", "APPMQUERY");

            String stockType = baseRequest.getReqContent().getStockType();
            if (ObjectUtil.isNotEmpty(stockType) ) {
                params.put("stockType", stockType);
            }

            // 调用三方接口获取今日新股数据
            SoaResponse<NewSharesDataResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_HOME_PAGE_INFO, params, baseRequest.getBase(), new TypeReference<SoaResponse<NewSharesDataResBody>>() {
            });
            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            //调用股票搜索接口 获取股票productType和productSubType
//            BaseRequest<MarketMainSharesReqBody> request = new BaseRequest<>();
//            request.setBase(baseRequest.getBase());
//            for (NewSharesDataResBody resBody : result.getList()) {
//                MarketMainSharesReqBody marketMainSharesReqBody = new MarketMainSharesReqBody();
//                marketMainSharesReqBody.setProductAbbr("");
//                marketMainSharesReqBody.setProductCode(resBody.getStockCode());
//                request.setReqContent(marketMainSharesReqBody);
//                List<MarketMainSharesResBody> marketMainSharesResBodies = personService.marketMainSharesList(request);
//
//                if (ObjectUtil.isNotEmpty(marketMainSharesResBodies)) {
//                    if (StrUtil.isNotEmpty(marketMainSharesResBodies.get(0).getProductType())) {
//                        resBody.setProductType(marketMainSharesResBodies.get(0).getProductType());
//                    }
//                    if (StrUtil.isNotEmpty(marketMainSharesResBodies.get(0).getProductSubType())) {
//                        resBody.setProductSubType(marketMainSharesResBodies.get(0).getProductSubType());
//                    }
//                }
//            }
            for (NewSharesDataResBody newSharesDataResBody : result.getList()) {
                String code = newSharesDataResBody.getStockCode();
                if (code.startsWith("6")) {
                    newSharesDataResBody.setProductType("EQU");
                    newSharesDataResBody.setProductSubType("ASH");
                } else if (code.startsWith("9")) {
                    newSharesDataResBody.setProductType("EQU");
                    newSharesDataResBody.setProductSubType("BSH");
                } else if (code.startsWith("5")) {
                    newSharesDataResBody.setProductType("FUN");
                    newSharesDataResBody.setProductSubType("LOF");
                } else {
                    newSharesDataResBody.setProductType("BON");
                    newSharesDataResBody.setProductSubType("CBD");
                }
            }


            return RespBean.success(result.getList());
        } catch (Exception e) {
            log.error("今日新股接口获取失败");
            throw new AppException("今日新股接口获取失败");
        }
    }

    public Map<String, Object> getIndexDataList() {
        try {
            log.info("===========APP改版首页指数列表接口开始==================");
            List<Map<String, Object>> indexDataList = getEntranceConfigForList(AppConstants.APP_INDEX);
            Map<String, Object> o = new HashMap<>();
            o.put("indexList", indexDataList);
            log.info("===========APP改版首页指数列表接口结束==================");
            return o;
        } catch (Exception e) {
            log.error("APP改版首页指数列表接口异常", e.fillInStackTrace());
            throw new AppException("接口调用失败");
        }
    }

    public RespBean<?> getRealtimeInforData(BaseRequest<SseVoicesReqBody> body) {
        try {
            log.info("===========APP改版首页7X24小时接口开始==================");
            String pageNo = body.getReqContent().getPageNo().toString();
            //公告类型，3：全部  7:上交所公告 8：监管 10:上市公司公告
            String noticeType = body.getReqContent().getNoticeType().toString();
            //必填参数不能为空
            if (ObjectUtil.isEmpty(pageNo) || ObjectUtil.isEmpty(noticeType)) {
                throw new AppException("参数校验失败");
            }
            Integer pageSize = body.getReqContent().getPageSize();
            if (ObjectUtil.isEmpty(pageSize)) {
                pageSize = 20;
            }
            log.info("======pageNo传参" + pageNo + "===noticeType传参" + noticeType + "===pageSize传参" + pageSize + "=======");
            Map<String, Object> o = new HashMap<>();
            //按照公告类型查询公告信息
            List<AnnouncementResBody> noticeList = getRecommendNoticeAllList(Integer.parseInt(noticeType), pageNo, pageSize, "", "", body.getBase());
            List<Map<String, Object>> resultList = new ArrayList<>();
            if (noticeList != null && noticeList.size() > 0) {
                Map<String, String> map = commonService.getActive("application");
                //循环处理公告数据，返回接口需要的字段
                for (AnnouncementResBody noticeMap : noticeList) {
                    Map<String, Object> noticeDataMap = new HashMap<>();
                    noticeDataMap.put("noticeTitle", noticeMap.getDocTitle());
                    String channelId = Objects.isNull(noticeMap.getChannelId()) ? "" : noticeMap.getChannelId();
                    noticeDataMap.put("noticeType", getNoticeTypeByChannelId(channelId, "", "voice", map));
                    String noticeDate = Objects.isNull(noticeMap.getCmsOpDate()) ? "" : noticeMap.getCmsOpDate();
                    noticeDataMap.put("noticeDate", DateUtils.transferFormat(noticeDate, "yyyy/MM/dd"));
                    String noticeArticleDate = Objects.isNull(noticeMap.getCreateTime()) ? "" : noticeMap.getCreateTime();
                    if (noticeArticleDate.length() > 10) {
                        noticeArticleDate = noticeArticleDate.substring(0, 10);
                    }
                    noticeArticleDate = DateUtils.timeDateFormat(noticeArticleDate, "yyyy-MM-dd", "yyyy/MM/dd");
                    noticeDataMap.put("noticeDateWeek", DateUtils.dateToWeek(noticeArticleDate));
                    noticeDataMap.put("noticeTime", DateUtils.transferFormat(noticeDate, "HH:mm"));
                    noticeDataMap.put("noticeContent", noticeMap.getExtINTRODUCTION());
                    noticeDataMap.put("noticeUrl", noticeMap.getDocURL());
                    noticeDataMap.put("noticeId", noticeMap.getDocId());
                    noticeDataMap.put("noticeFileType", noticeMap.getDocType());
                    noticeDataMap.put("gpdm", noticeMap.getStockcode());
                    noticeDataMap.put("noticeArticleDate", noticeArticleDate);
                    noticeDataMap.put("category", noticeMap.getExtWTFL());
                    if (ObjectUtil.isEmpty(noticeMap.getExtTeacher())) {
                        noticeDataMap.put("involveObject", "");
                    } else {
                        noticeDataMap.put("involveObject", noticeMap.getExtTeacher());
                    }
                    resultList.add(noticeDataMap);
                }
            }
            o.put("noticeList", resultList);
            log.info("===========APP改版首页7X24小时接口结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException("接口调用失败");
        }
    }

    public RespBean<?> getIpoRuleData(BaseRequest<IpoRuleDataReqBody> body) {
        Integer pageNo = body.getReqContent().getPageNo();
        Integer securityType = body.getReqContent().getSecurityType();
        if (ObjectUtil.isEmpty(pageNo) || ObjectUtil.isEmpty(securityType)) {
            throw new AppException("参数校验失败");
        }
        try {
            log.info("===========APP改版信息披露IPO规则接口开始==================");
            Integer pageSize = body.getReqContent().getPageSize();
            log.info("===securityType传入参数===" + securityType + "===pageNo传入参数===" + pageNo + "===pageSize传入参数===" + pageSize);
            //按照公告类型查询公告信息
            List<AnnouncementResBody> noticeList = getRecommendNoticeAllList(securityType, pageNo.toString(), pageSize, "", "", body.getBase());
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, Object> o = new HashMap<>();
            if (noticeList != null && noticeList.size() > 0) {
                //循环处理公告数据，返回接口需要的字段
                for (AnnouncementResBody noticeMap : noticeList) {
                    Map<String, Object> noticeDataMap = new HashMap<>();
                    noticeDataMap.put("noticeTitle", noticeMap.getDocTitle());
                    String noticeDate = Objects.isNull(noticeMap.getCmsOpDate()) ? "" : noticeMap.getCmsOpDate();
                    noticeDataMap.put("noticeDate", DateUtils.transferFormat(noticeDate, "yyyy-MM-dd"));
                    noticeDataMap.put("noticeUrl", noticeMap.getDocURL());
                    noticeDataMap.put("noticeId", noticeMap.getDocId());
                    noticeDataMap.put("gpdm", noticeMap.getStockcode());
                    resultList.add(noticeDataMap);
                }
            }
            o.put("noticeList", resultList);
            log.info("===========APP改版信息披露IPO规则接口结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException("接口调用失败");
        }

    }

    public RespBean<?> getIpoInquiryData(BaseRequest<IpoInquiryDataReqBody> body) {
        try {
            log.info("===========APP改版信息披露IPO询价接口开始==================");
            Integer pageNo = body.getReqContent().getPageNo();
            Integer pageSize = body.getReqContent().getPageSize();
            //0主板，2科创板，全部不传
            String stockType = body.getReqContent().getStockType();
            if (ObjectUtil.isNotEmpty(stockType) && "2".equals(stockType)) {
                stockType = "1," + stockType;
            }
            //必填参数不能为空
            if (ObjectUtil.isEmpty(pageNo)) {
                throw new AppException("参数校验失败");
            }
            log.info("===pageNo传参" + pageNo + "===pageSize传参" + pageSize + "===stockType传参" + stockType);
            Map<String, Object> param = new HashMap<>();
            param.put("pageNo", pageNo);
            param.put("pageSize", pageSize);
            param.put("stockType", stockType);
            param.put("token", "APPMQUERY");
            param.put("tradeMarket", "SH");
            SoaResponse<IpoInquiryResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_IPO_LIST, param, body.getBase(), new TypeReference<SoaResponse<IpoInquiryResBody>>() {
            });
            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnCode());
            }
            List<IpoInquiryResBody> inquiryList = result.getList();
            Map<String, Object> o = new HashMap<>();
            for (IpoInquiryResBody inquiryMapData : inquiryList) {
                if (ObjectUtil.isNotEmpty(inquiryMapData.getOfflineIssuanceStartDate())) {
                    inquiryMapData.setOfflineIssuanceStartDate(DateUtils.timeDateFormat(inquiryMapData.getOfflineIssuanceStartDate() + "", "yyyyMMdd", "yyyy-MM-dd"));
                }
                if (ObjectUtil.isNotEmpty(inquiryMapData.getOfflineIssuanceEndDate())) {
                    inquiryMapData.setOfflineIssuanceEndDate(DateUtils.timeDateFormat(inquiryMapData.getOfflineIssuanceEndDate() + "", "yyyyMMdd", "yyyy-MM-dd"));
                }
                if (ObjectUtil.isNotEmpty(inquiryMapData.getOnlineIssuanceDate())) {
                    inquiryMapData.setOnlineIssuanceDate(DateUtils.timeDateFormat(inquiryMapData.getOnlineIssuanceDate() + "", "yyyyMMdd", "yyyy-MM-dd"));
                }
                if (ObjectUtil.isNotEmpty(inquiryMapData.getPaymentStartDate())) {
                    inquiryMapData.setPaymentStartDate(DateUtils.timeDateFormat(inquiryMapData.getPaymentStartDate() + "", "yyyyMMdd", "yyyy-MM-dd"));
                }
                if (ObjectUtil.isNotEmpty(inquiryMapData.getPaymentEndDate())) {
                    inquiryMapData.setPaymentEndDate(DateUtils.timeDateFormat(inquiryMapData.getPaymentEndDate() + "", "yyyyMMdd", "yyyy-MM-dd"));
                }
            }
            o.put("inquiryList", inquiryList);
            o.put("total", result.getTotal());
            o.put("pageNo", result.getPageNum());
            log.info("===========APP改版信息披露IPO询价接口结束==================");
            return RespBean.success(o);
        } catch (Exception e) {
            log.error("APP改版信息披露IPO询价接口异常",e.fillInStackTrace());
            throw new AppException("接口调用失败");
        }
    }

    public List<GetCompNoticeListDtoResBody> getCompNoticeList(BaseRequest<GetCompNoticeListReqBody> body) {
        GetCompNoticeListReqBody reqContent = body.getReqContent();
        //如果必传参数为空 则报错
        if (StrUtil.isEmpty(reqContent.getPageNo()) && StrUtil.isEmpty(reqContent.getSecurityType())) {
            throw new AppException("PageNo或SecurityType未传");
        }
        //如果不传日期区间参数，默认查询30天的公告数据
        if (StrUtil.isEmpty(reqContent.getSsedateStart())) {
            reqContent.setSsedateStart(getThirtyDate());
        }
        if (StrUtil.isEmpty(reqContent.getSsedateEnd())) {
            reqContent.setSsedateEnd(getTomorrowDate());
        }
        Map<String, Object> params = BeanUtil.beanToMap(reqContent);
        SoaResponse<GetCompNoticeListDtoResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_GET_COMP_NOTICE_LIST, params, body.getBase(), new TypeReference<SoaResponse<GetCompNoticeListDtoResBody>>() {
        });
        List<GetCompNoticeListDtoResBody> noticeList = result.getList();
        for (GetCompNoticeListDtoResBody compNoticeListDto : noticeList) {
            if (StrUtil.isNotEmpty(compNoticeListDto.getBulletinId())) {
                String bulletinId = compNoticeListDto.getBulletinId();
                bulletinId = getNumBulletinId(bulletinId);
                if (StrUtil.isEmpty(bulletinId)) {
                    compNoticeListDto.setNoticeImageUrl("");
                } else {
                    compNoticeListDto.setNoticeImageUrl(getRandomImageForNotice(bulletinId));
                }
            } else {
                compNoticeListDto.setNoticeImageUrl("");
            }
        }
        return noticeList;
    }

    /**
     * 获取29天前的yyyy-MM-dd格式日期
     *
     * @return
     */
    private static String getThirtyDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -29);
        return new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
    }

    /**
     * 获取yyyy-MM-dd格式的明天日期
     *
     * @return
     */
    @SuppressWarnings("static-access")
    private static String getTomorrowDate() {
        //取时间
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(calendar.DATE, 1);
        //这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 获取bulletinId字段最后6个长度内容，替换A到F字母为对应的数字并返回
     *
     * @param bulletinId
     * @return
     */
    private String getNumBulletinId(String bulletinId) {
        bulletinId = bulletinId.substring(bulletinId.length() - 6, bulletinId.length());
        bulletinId = bulletinId.replaceAll("a", "1")
                .replaceAll("b", "2")
                .replaceAll("c", "3")
                .replaceAll("d", "4")
                .replaceAll("e", "5")
                .replaceAll("f", "6")
                .replaceAll("A", "1")
                .replaceAll("B", "2")
                .replaceAll("C", "3")
                .replaceAll("D", "4")
                .replaceAll("E", "5")
                .replaceAll("F", "6");

        return bulletinId;
    }


}
