package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.feign.system.ISysProjectSubscriptionFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.NoticeListResBody;
import com.sse.sseapp.form.response.RegisterNoticeResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.MySoaResponse;
import com.sse.sseapp.proxy.soa.SoaResponse;
import com.sse.sseapp.proxy.soa.dto.KCBNoticeListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterService {

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ISysProjectSubscriptionFeign sysProjectSubscriptionFeign;

    public RespBean<?> getProjectStatisticInfo(BaseRequest<ProjectStatisticInfoReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        // 获取板块类型参数，1：主板，2：科创板
        String issueMarketType = reqBody.getReqContent().getIssueMarketType();
        param.put("issueMarketType", issueMarketType);
        param.put("token", "APPMQUERY");
        // 按项目状态统计
        List<Map<String, Object>> statusList = new ArrayList<>();
        Map<String, Object> data = BeanUtil.beanToMap(reqBody.getReqContent());
        // 调用 获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ALL_STATUS_COUNT_LIST, data, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        List<Map<String, Object>> statusListTmp = result.getList();
        Map<String, Map<String, DictVO>> dictMapOne = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
        Map<String, Map<String, DictVO>> dictMapTwo = commonService.getDictFromRedis(AppConstants.SSWHYJG_NUM);
        Map<String, Map<String, DictVO>> dictMapThree = commonService.getDictFromRedis(AppConstants.SSWHYJG_NUM);
        Map<String, Map<String, DictVO>> dictMapFour = commonService.getDictFromRedis(AppConstants.ZCJG_NUM);
        Map<String, Map<String, DictVO>> dictMapFive = commonService.getDictFromRedis(AppConstants.ZZZZT_NUM);
        // 投资者项目主状态集合
        Map<String, DictVO> investorProStatusMap = dictMapOne.get(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
        // 上市委会议
        Map<String, DictVO> sswhyStatusMap = dictMapTwo.get(AppConstants.SSWHYJG_NUM);
        // 复审委会议
        Map<String, DictVO> fswhyStatusMap = dictMapThree.get(AppConstants.SSWHYJG_NUM);
        // 注册结果
        Map<String, DictVO> zcjgStatusMap = dictMapFour.get(AppConstants.ZCJG_NUM);
        // 中止子状态
        Map<String, DictVO> zzzztStatusMap = dictMapFive.get(AppConstants.ZZZZT_NUM);
        // 转换为List
        List<String> mainStatusList = new ArrayList<>(investorProStatusMap.keySet());
        List<String> sswhyStatusList = new ArrayList<>(sswhyStatusMap.keySet());
        List<String> fswhyStatusList = new ArrayList<>(fswhyStatusMap.keySet());
        List<String> zcjgStatusList = new ArrayList<>(zcjgStatusMap.keySet());
        List<String> zzzztStatusList = new ArrayList<>(zzzztStatusMap.keySet());
        // 排序
        Util.bubbleSort(mainStatusList);
        Util.bubbleSort(sswhyStatusList);
        Util.bubbleSort(fswhyStatusList);
        Util.bubbleSort(zcjgStatusList);
        Util.bubbleSort(zzzztStatusList);
        // 调整顺序
        // 主状态方法特殊处理，将9-复审委会议放到3-上市委会议后面，如果状态位置有所变更，此代码需要修改
        if (mainStatusList.contains("9")) {
            mainStatusList.remove("9");
            mainStatusList.add(3, "9");
        }
        // 主状态方法特殊处理，将10-补充审核放到5-注册结果后面，如果状态位置有所变更，此代码需要修改
        if (mainStatusList.contains("10")) {
            mainStatusList.remove("10");
            mainStatusList.add(5, "10");
        }
        // 上市委会议去除复审委会议内容及调整字典位置
        sswhyStatusList.remove("4");
        sswhyStatusList.remove("5");
        // 调整字典位置
        if (sswhyStatusList.contains("6")) {
            sswhyStatusList.remove("6");
            sswhyStatusList.add(0, "6");
        }

        // 复审委会议去除上市委会议内容
        fswhyStatusList.remove("1");
        fswhyStatusList.remove("3");
        fswhyStatusList.remove("6");

        Map<String, Integer> statusTotalCountMap = new HashMap<>();
        // 循环投资者项目主状态（数据字典）
        for (String mainStatus : mainStatusList) {
            // 存在子状态特殊处理
            if ("3".equals(mainStatus)) {
                for (String sswhyStatus : sswhyStatusList) {
                    String statusDesc = sswhyStatusMap.get(sswhyStatus).getDictSubEntryName();
                    statisticStatusJoin(mainStatus + "-" + sswhyStatus, statusList, statusListTmp, statusDesc, statusTotalCountMap);
                }
            } else if ("9".equals(mainStatus)) {
                for (String fswhyStatus : fswhyStatusList) {
                    String statusDesc = fswhyStatusMap.get(fswhyStatus).getDictSubEntryName();
                    statisticStatusJoin(mainStatus + "-" + fswhyStatus, statusList, statusListTmp, statusDesc, statusTotalCountMap);
                }
            } else if ("5".equals(mainStatus)) {
                for (String zcjgStatus : zcjgStatusList) {
                    String statusDesc = zcjgStatusMap.get(zcjgStatus).getDictSubEntryName();
                    statisticStatusJoin(mainStatus + "-" + zcjgStatus, statusList, statusListTmp, statusDesc, statusTotalCountMap);
                }
            } else {
                String statusDesc = investorProStatusMap.get(mainStatus).getDictSubEntryName();
                statisticStatusJoin(mainStatus, statusList, statusListTmp, statusDesc, statusTotalCountMap);
            }
        }
        resultMap.put("statusList", statusList);
        resultMap.put("statusTotalCount", statusTotalCountMap.get("statusTotalCount"));
        // 按行业统计
        // 不分页
        param.put("pageSize", 0);
        SoaResponse<Map<String, Object>> csrcResp = getCsrcStatisticInfo(param, base);
        List<Map<String, Object>> csrcList = csrcResp.getList();
        // 展示前5条
        resultMap.put("csrcList", statisticMainDataProcess(csrcList, 5));
        resultMap.put("csrcTotalCount", statisticTotalCountProcess(csrcList));
        // 按地区统计
        // 不分页
        param.put("pageSize", 0);
        SoaResponse<Map<String, Object>> provinceResp = getProvinceStatisticInfo(param, base);
        List<Map<String, Object>> provinceList = provinceResp.getList();
        // 展示前4条
        resultMap.put("provinceList", statisticMainDataProcess(provinceList, 5));
        resultMap.put("provinceTotalCount", statisticTotalCountProcess(provinceList));
        // 按融资金额统计
        // 不分页
        param.put("pageSize", 0);
        SoaResponse<Map<String, Object>> planIssueResp = getPlanIssueStatisticInfo(param, base);
        List<Map<String, Object>> planIssueList = planIssueResp.getList();
        resultMap.put("planIssueList", planIssueList);
        // 按保荐券商统计
        // 不分页
        param.put("pageSize", 0);
        // 1= 保荐机构 2=会计事务所 3=律师事务所
        param.put("type", "1");
        SoaResponse<Map<String, Object>> sponsorResp = getIntermediaryStatisticInfo(param, base);
        List<Map<String, Object>> sponsorList = sponsorResp.getList();
        // 展示前5条
        resultMap.put("sponsorList", statisticMainDataProcess(sponsorList, 5));
        resultMap.put("sponsorTotalCount", statisticTotalCountProcess(sponsorList));
        // 按会计事务所统计
        // 不分页
        param.put("pageSize", 0);
        // 1= 保荐机构 2=会计事务所 3=律师事务所
        param.put("type", "2");
        SoaResponse<Map<String, Object>> accountantResp = getIntermediaryStatisticInfo(param, base);
        List<Map<String, Object>> accountantList = accountantResp.getList();
        // 展示前5条
        resultMap.put("accountantList", statisticMainDataProcess(accountantList, 5));
        resultMap.put("accountantTotalCount", statisticTotalCountProcess(accountantList));
        // 按律师事务所统计
        // 不分页
        param.put("pageSize", 0);
        // 1= 保荐机构 2=会计事务所 3=律师事务所
        param.put("type", "3");
        SoaResponse<Map<String, Object>> lawyerResp = getIntermediaryStatisticInfo(param, base);
        List<Map<String, Object>> lawyerList = lawyerResp.getList();
        // 展示前5条
        resultMap.put("lawyerList", statisticMainDataProcess(lawyerList, 5));
        resultMap.put("lawyerTotalCount", statisticTotalCountProcess(lawyerList));
        return RespBean.success(resultMap);
    }

    private SoaResponse<Map<String, Object>> getIntermediaryStatisticInfo(Map<String, Object> param, ReqBaseVO base) {
        Object token = param.get("token");
        if (ObjectUtil.isEmpty(token)) {
            param.put("token", "APPMQUERY");
        }
        // 调用 获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_INTERMEDIARY_STATISTIC_INFO, param, base, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<Map<String, Object>> getPlanIssueStatisticInfo(Map<String, Object> param, ReqBaseVO base) {
        Object token = param.get("token");
        if (ObjectUtil.isEmpty(token)) {
            param.put("token", "APPMQUERY");
        }
        // 调用 获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PLAN_ISSUE_STATISTIC_INFO, param, base, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<Map<String, Object>> getProvinceStatisticInfo(Map<String, Object> param, ReqBaseVO base) {
        Object token = param.get("token");
        if (ObjectUtil.isEmpty(token)) {
            param.put("token", "APPMQUERY");
        }
        // 调用 获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PROVINCE_STATISTIC_INFO, param, base, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<Map<String, Object>> getCsrcStatisticInfo(Map<String, Object> param, ReqBaseVO base) {
        Object token = param.get("token");
        if (ObjectUtil.isEmpty(token)) {
            param.put("token", "APPMQUERY");
        }
        // 调用 获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_CSRC_STATISTIC_INFO, param, base, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    /**
     * 统计状态返回对象组装
     *
     * @param statusCode    主状态或者子状态代码，子状态为 主状态-子状态
     * @param statusList    返回List
     * @param statusListTmp SOA返回的统计List
     * @param statusDesc    主状态或者子状态翻译
     */
    private void statisticStatusJoin(String statusCode, List<Map<String, Object>> statusList,
                                     List<Map<String, Object>> statusListTmp, String statusDesc, Map<String, Integer> statusTotalCountMap) {
        // 是否有状态匹配
        boolean isMatch = false;

        for (Map<String, Object> statusMap : statusListTmp) {
            Object projectStatus = statusMap.get("status");
            Integer projectNum = (Integer) statusMap.get("num");

            // 该状态有项目信息
            if (statusCode.equals(projectStatus)) {
                Map<String, Object> statusTmpMap = new HashMap<>();
                statusTmpMap.put("projectCount", projectNum);
                statusTmpMap.put("status", projectStatus);
                statusTmpMap.put("statusDesc", statusDesc);
                statusList.add(statusTmpMap);
                if (!statusTotalCountMap.isEmpty()) {
                    statusTotalCountMap.put("statusTotalCount", statusTotalCountMap.get("statusTotalCount") + projectNum);
                } else {
                    statusTotalCountMap.put("statusTotalCount", projectNum);
                }
                isMatch = true;
                break;
            }
        }

        // 无状态匹配
        if (!isMatch) {
            Map<String, Object> statusTmpMap = new HashMap<>();
            statusTmpMap.put("projectCount", 0);
            statusTmpMap.put("status", statusCode);
            statusTmpMap.put("statusDesc", statusDesc);
            statusList.add(statusTmpMap);
        }
    }

    /**
     * 科创版数据统计首页数据处理
     */
    private List<Map<String, Object>> statisticMainDataProcess(List<Map<String, Object>> sourceList, int showCount) {
        List<Map<String, Object>> targetList;

        // 首页展示showCount条
        if (sourceList != null && sourceList.size() >= showCount) {
            targetList = sourceList.subList(0, showCount);
        } else {
            targetList = new ArrayList<>();
        }

        return targetList;
    }

    /**
     * 科创版数据统计首页项目总数处理
     */
    private int statisticTotalCountProcess(List<Map<String, Object>> sourceList) {
        int totalCount = 0;
        for (Map<String, Object> map : sourceList) {
            int projectCount = (Integer) map.get("projectCount");
            totalCount += projectCount;
        }

        return totalCount;
    }

    public RespBean<?> getMoreCsrcStatistic(BaseRequest<ProjectStatisticInfoReqBody> reqBody) {
        Map<String, Object> map = BeanUtil.beanToMap(reqBody.getReqContent());
        map.put("pageNo", reqBody.getReqContent().getPage());
        map.remove("page");
        SoaResponse<Map<String, Object>> csrcResp = getCsrcStatisticInfo(map, reqBody.getBase());
        return RespBean.success(csrcResp);
    }

    public RespBean<?> getMoreProvinceStatistic(BaseRequest<ProjectStatisticInfoReqBody> reqBody) {
        Map<String, Object> map = BeanUtil.beanToMap(reqBody.getReqContent());
        map.put("pageNo", reqBody.getReqContent().getPage());
        map.remove("page");
        SoaResponse<Map<String, Object>> csrcResp = getProvinceStatisticInfo(map, reqBody.getBase());
        return RespBean.success(csrcResp);
    }

    public RespBean<?> getMoreIntermediaryStatistic(BaseRequest<IntermediaryStatisticReqBody> reqBody) {
        Map<String, Object> map = BeanUtil.beanToMap(reqBody.getReqContent());
        map.put("pageNo", reqBody.getReqContent().getPage());
        map.remove("page");
        SoaResponse<Map<String, Object>> csrcResp = getIntermediaryStatisticInfo(map, reqBody.getBase());
        return RespBean.success(csrcResp);
    }

    public RespBean<?> getRegisterProjectList(BaseRequest<RegisterProjectListReqBody> reqBody) {
        try {
            Map<String, Object> param;
            Map<String, Object> o = new HashMap<>();
            o.put("fileBasePath", commonService.getActive("application").get("fileBasePath"));
            param = BeanUtil.beanToMap(reqBody.getReqContent());
            String permitId = reqBody.getReqContent().getPassId();
            String stockAuditNum = reqBody.getReqContent().getStockAuditNum();
            String intermediaryIds = reqBody.getReqContent().getIntermediaryIds();
            // 融资金额排序
            String planIssueCapitalOrder = reqBody.getReqContent().getPlanIssueCapitalOrder();
            // 受理日期排序
            String auditApplyDateOrder = reqBody.getReqContent().getAuditApplyDateOrder();
            // 更新日期排序
            String updateDateOrder = reqBody.getReqContent().getUpdateDateOrder();
            // 项目状态
            String currStatus = reqBody.getReqContent().getCurrStatus();
            // 项目状态 currStatus=3 上市委会议会议结果
            String commitiResult = reqBody.getReqContent().getCommitiResult();
            // 项目状态 currStatus=5 注册结果
            String registeResult = reqBody.getReqContent().getRegisteResult();

            // 项目ID
            if (ObjectUtil.isNotEmpty(stockAuditNum)) {
                param.put("stockAuditNum", stockAuditNum);
            }
            // 保荐机构
            if (ObjectUtil.isNotEmpty(intermediaryIds)) {
                param.put("intermediaryId", intermediaryIds);
            }
            // 融资金额排序
            if (ObjectUtil.isNotEmpty(planIssueCapitalOrder)) {
                param.put("order", "planIssueCapital|" + planIssueCapitalOrder);
            }
            // 受理日期排序
            if (ObjectUtil.isNotEmpty(auditApplyDateOrder)) {
                param.put("order", "auditApplyDate|" + auditApplyDateOrder);
            }
            // 更新日期排序
            if (ObjectUtil.isNotEmpty(updateDateOrder)) {
                param.put("order", "updateDate|" + updateDateOrder);
            }
            // 项目状态
            if (ObjectUtil.isNotEmpty(currStatus)) {
                param.put("currStatus", currStatus);
            }
            // 项目状态 currStatus=3 上市委会议会议结果
            if (ObjectUtil.isNotEmpty(commitiResult)) {
                param.put("commitiResult", commitiResult);
            }
            // 项目状态 currStatus=5 注册结果
            if (ObjectUtil.isNotEmpty(registeResult)) {
                param.put("registeResult", registeResult);
            }
            // 调用 获取服务器返回结果
            SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_REGISTER_PROJECT_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
            });

            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            if (SoaConstants.RETURN_CORRECT_CODE.equals(result.getReturnCode())) {
                SysProjectSubscription subscription = new SysProjectSubscription();
                subscription.setPassId(permitId);
                List<SysProjectSubscription> subscribeList = sysProjectSubscriptionFeign.getSubscribeListNew(subscription);
                List<Map<String, Object>> list = result.getList();
                Map<String, Object> newData = new HashMap<>();
                List<Map<String, Object>> newList = new ArrayList<>();
                for (Map<String, Object> map : list) {
                    String currStatusRep = String.valueOf(map.get("currStatus"));
                    String currStatusDec = projectStatusDesc(currStatusRep, map);
                    map.put("currStatusDec", currStatusDec);
                    boolean subscribeB = false;
                    if (null != permitId && permitId.trim().length() > 0) {
                        for (SysProjectSubscription subscribe : subscribeList) {
                            if(ArrayUtil.isAllEmpty(subscribe)){
                                continue;
                            }
                            String stockAuditNumSOA = String.valueOf(map.get("stockAuditNum"));
                            if ((subscribe.getProjectId()).equals(stockAuditNumSOA)) {
                                subscribeB = true;
                            }
                        }
                        if (subscribeB) {
                            map.put("subscribe", "1");
                        } else {
                            map.put("subscribe", "0");
                        }
                    } else {
                        map.put("subscribe", "0");
                    }

                    newList.add(map);
                }
                newData.put("page", result.getPageNum());
                newData.put("list", newList);
                o.put("data", newData);
                return RespBean.success(o);
            } else {
                throw new AppException("getRegisterProjectList:远程返回了错误的状态码");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 发行上市投资者及保荐人（暂时和投资者一样）项目状态翻译，子状态为空翻译主状态
     *
     * @param mainStatus 主状态
     * @param projectMap 项目MAP对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public String projectStatusDesc(String mainStatus, Map<String, Object> projectMap) {
        String currStatusDec;
        Map<String, Map<String, DictVO>> dictMapOne = commonService.getDictFromRedis(AppConstants.SSWHYJG_NUM);
        Map<String, Map<String, DictVO>> dictMapTwo = commonService.getDictFromRedis(AppConstants.ZCJG_NUM);
        Map<String, Map<String, DictVO>> dictMapThree = commonService.getDictFromRedis(AppConstants.ZZZZT_NUM);
        Map<String, Map<String, DictVO>> dictMapFour = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
        Map<String, Map<String, DictVO>> dictMapFive = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
        if ("3".equals(mainStatus) || "9".equals(mainStatus)) {
            currStatusDec = getConstDescByCode(dictMapOne, AppConstants.SSWHYJG_NUM, String.valueOf(projectMap.get("commitiResult")));
        } else if ("5".equals(mainStatus)) {
            currStatusDec = getConstDescByCode(dictMapTwo, AppConstants.ZCJG_NUM, String.valueOf(projectMap.get("registeResult")));
        } else if ("7".equals(mainStatus)) {
            currStatusDec = getConstDescByCode(dictMapThree, AppConstants.ZZZZT_NUM, String.valueOf(projectMap.get("suspendStatus")));
        } else {
            currStatusDec = getConstDescByCode(dictMapFour, AppConstants.INVESTOR_PROJECT_STATUS_NUM, mainStatus);
        }
        // 子状态为空翻译主状态
        if (ObjectUtil.isEmpty(currStatusDec)) {
            currStatusDec = getConstDescByCode(dictMapFive, AppConstants.INVESTOR_PROJECT_STATUS_NUM, mainStatus);
        }

        return currStatusDec;
    }

    public static String getConstDescByCode(Map<String, Map<String, DictVO>> dictMap, String type, String code) {
        if (ObjectUtil.isEmpty(type) || ObjectUtil.isEmpty(code)) {
            return "";
        }
        Map<String, DictVO> cvoHt = dictMap.get(type);
        if (null != cvoHt) {
            DictVO cvo = cvoHt.get(code);
            if (null != cvo) {
                return cvo.getDictSubEntryName();
            }
        }
        return "";
    }

    public RespBean<?> getRegisterNoticeListNew(BaseRequest<RegisterNoticeListReqBody> reqBody) {
        try {
            Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
            Map<String, Object> o = new HashMap<>();
            o.put("fileBasePath", commonService.getActive("application").get("fileBasePath"));
            SoaResponse<RegisterNoticeResBody> result = getRegisterNoticeList(param, reqBody.getBase());
            o.put("data", result);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }


    public RespBean<?> getRegisterNoticeListNewCMS(BaseRequest<RegisterNoticeListReqBody> reqBody) {
        try {
            Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
            Map<String, Object> o = new HashMap<>();
            o.put("fileBasePath", commonService.getActive("application").get("fileBasePath"));
            SoaResponse<RegisterNoticeResBody> result = getRegisterNoticeListCMS(param, reqBody.getBase());
            o.put("data", result);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public String setChannelIdByType(String type) {
        return commonService.getActive("getRegisterNoticeList").get(type);
    }

    public RespBean<?> getIntermediaryInfoList(BaseRequest<IntermediaryInfoListReqBody> reqBody) {
        try {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> o = new HashMap<>();
            param.put("type", "1");
            param.put("pageSize", 0);
            o.put("fileBasePath", commonService.getActive("application").get("fileBasePath"));
            // 调用 获取服务器返回结果
            SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_INTERMEDIARY_INFO_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
            });
            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            List<Map<String, Object>> dataList = result.getList();
            Map<String, List<Map<String, String>>> intermediaryInfo = new HashMap<>();
            for (Map<String, Object> map : dataList) {
                Map<String, String> data = new HashMap<>();
                String fullName = String.valueOf(map.get("name"));
                String shortName = map.get("shortName") == null ? null : String.valueOf(map.get("shortName"));
                String num = String.valueOf(map.get("num"));

                String pinYin;
                if (ObjectUtil.isEmpty(shortName)) {
                    data.put("name", fullName);
                    pinYin = PinyinUtil.getPinyin(fullName);
                } else {
                    data.put("name", shortName);
                    pinYin = PinyinUtil.getPinyin(shortName);
                }
                String firstCode = pinYin.substring(0, 1).toUpperCase();

                List<Map<String, String>> list = intermediaryInfo.get(firstCode);
                if (null == list) {
                    list = new ArrayList<>();
                }
                data.put("num", num);
                data.put("pinyin", pinYin);
                list.add(data);
                intermediaryInfo.put(firstCode, list);
            }
            o.put("data", intermediaryInfo);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }


    public RespBean<?> getKCBHomeList(BaseRequest<CommonReqBody> reqBody) {
        try {
            ReqBaseVO base = reqBody.getBase();
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> o = new HashMap<>();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //199:监管信息全部
            param.put("businessType", "199");
            param.put("type", "0");
            SoaResponse<KCBNoticeListDto> jgxxMap = geKCBNoticeList(param, base);
            List<KCBNoticeListDto> jgxxList = jgxxMap.getList();
            o.put("jgxx", jgxxList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //299:上交所公告全部
            param.put("businessType", "299");
            param.put("type", "1");
            SoaResponse<KCBNoticeListDto> sjsggMap = geKCBNoticeList(param, base);
            List<KCBNoticeListDto> sjsggList = sjsggMap.getList();
            o.put("sjsgg", sjsggList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //13:公司公告全部
            param.put("type", "13");
            SoaResponse<NoticeListResBody> gsggMap = noticeList(param, base);
            List<NoticeListResBody> gsggList = gsggMap.getList();
            o.put("gsgg", gsggList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //12:新闻动态全部
            param.put("type", "12");
            SoaResponse<NoticeListResBody> xwdtMap = noticeList(param, base);
            List<NoticeListResBody> xwdtList = xwdtMap.getList();
            o.put("xwdt", xwdtList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //499:披露科创板长期投资者权益全部
//            param.put("type", "499");
//            SoaResponse<RegisterNoticeResBody> kcbcqtzzqyMap = getRegisterNoticeList(param, base);
//            List<RegisterNoticeResBody> kcbcqtzzqyList = kcbcqtzzqyMap.getList();
//            o.put("kcbcqtzzqy", kcbcqtzzqyList);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public RespBean<?> getKCBHomeListCMS(BaseRequest<CommonReqBody> reqBody) {
        try {
            ReqBaseVO base = reqBody.getBase();
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> o = new HashMap<>();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //199:监管信息全部
            param.put("businessType", "199");
            param.put("type", "0");
            SoaResponse<KCBNoticeListDto> jgxxMap = geKCBNoticeListCMS(param, base);
            List<KCBNoticeListDto> jgxxList = jgxxMap.getList();
            o.put("jgxx", jgxxList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //299:上交所公告全部
            param.put("businessType", "299");
            param.put("type", "1");

            SoaResponse<KCBNoticeListDto> sjsggMap = geKCBNoticeListCMS(param, base);
            List<KCBNoticeListDto> sjsggList = sjsggMap.getList();
            o.put("sjsgg", sjsggList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //13:公司公告全部
            param.put("type", "13");
            SoaResponse<NoticeListResBody> gsggMap = noticeListCMS(param, base);
            List<NoticeListResBody> gsggList = gsggMap.getList();
            o.put("gsgg", gsggList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //12:新闻动态全部
            param.put("type", "12");
            SoaResponse<NoticeListResBody> xwdtMap = noticeListCMS(param, base);
            List<NoticeListResBody> xwdtList = xwdtMap.getList();
            o.put("xwdt", xwdtList);

            param.clear();
            param.put("pageNo", 1);
            param.put("pageSize", 2);
            //499:披露科创板长期投资者权益全部
            param.put("type", "499");
            SoaResponse<RegisterNoticeResBody> kcbcqtzzqyMap = getRegisterNoticeListCMS(param, base);
            List<RegisterNoticeResBody> kcbcqtzzqyList = kcbcqtzzqyMap.getList();
            o.put("kcbcqtzzqy", kcbcqtzzqyList);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }


    private SoaResponse<RegisterNoticeResBody> getRegisterNoticeList(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> map = commonService.getActive("getRegisterNoticeList");
        if (ObjectUtil.isNotEmpty(param.get("type"))) {
            param.put("channelId", map.get(param.get("type")));
        } else {
            param.put("channelId", map.get("default"));
        }
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", map.get("order"));
        }
        if (ObjectUtil.isEmpty(param.get("siteId"))) {
            param.put("siteId", map.get("siteId"));
        }
        if (ObjectUtil.isEmpty(param.get("pageNo"))) {
            param.put("pageNo", 1);
        }
        // 调用 获取服务器返回结果
        SoaResponse<RegisterNoticeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST, param, base, new TypeReference<SoaResponse<RegisterNoticeResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;

    }


    private SoaResponse<RegisterNoticeResBody> getRegisterNoticeListCMS(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> map = commonService.getActive("getRegisterNoticeList");
        if (ObjectUtil.isNotEmpty(param.get("type"))) {
            param.put("channelId", map.get(param.get("type")));
        } else {
            param.put("channelId", map.get("default"));
        }
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", map.get("order"));
        }
        if (ObjectUtil.isEmpty(param.get("siteId"))) {
            param.put("siteId", map.get("siteId"));
        }
        if (ObjectUtil.isEmpty(param.get("pageNo"))) {
            param.put("pageNo", 1);
        }
        // 调用 获取服务器返回结果
        SoaResponse<RegisterNoticeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, base, new TypeReference<SoaResponse<RegisterNoticeResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;

    }

    private SoaResponse<NoticeListResBody> noticeList(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> map = commonService.getActive("noticeList");
        if (ObjectUtil.isNotEmpty(param.get("type"))) {
            param.put("channelId", map.get(param.get("type")));
        } else {
            param.put("channelId", map.get("default"));
        }
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", map.get("order"));
        }
        if (ObjectUtil.isEmpty(param.get("siteId"))) {
            param.put("siteId", map.get("siteId"));
        }
        // 调用 获取服务器返回结果
        SoaResponse<NoticeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST, param, base, new TypeReference<SoaResponse<NoticeListResBody>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<NoticeListResBody> noticeListCMS(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> map = commonService.getActive("noticeList");
        if (ObjectUtil.isNotEmpty(param.get("type"))) {
            param.put("channelId", map.get(param.get("type")));
        } else {
            param.put("channelId", map.get("default"));
        }
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", map.get("order"));
        }
        if (ObjectUtil.isEmpty(param.get("siteId"))) {
            param.put("siteId", map.get("siteId"));
        }
        // 调用 获取服务器返回结果
        SoaResponse<NoticeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, base, new TypeReference<SoaResponse<NoticeListResBody>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }


    private SoaResponse<KCBNoticeListDto> geKCBNoticeList(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> map = commonService.getActive("geKCBNoticeList");
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", map.get("order"));
        }
        param.put("token", "APPMQUERY");
        param.put("siteId", "28");
        if (ObjectUtil.isNotEmpty(param.get("businessType"))) {
            param.put("channelId", map.get(param.get("businessType")));
        } else {
            param.put("channelId", map.get("default"));
        }
        param.remove("businessType");
        SoaResponse<KCBNoticeListDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCB_NOTICE_LIST, param, base, new TypeReference<SoaResponse<KCBNoticeListDto>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }


    private SoaResponse<KCBNoticeListDto> geKCBNoticeListCMS(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> map = commonService.getActive("geKCBNoticeList");
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", map.get("order"));
        }
        param.put("token", "APPMQUERY");
        param.put("siteId", "28");
        if (ObjectUtil.isNotEmpty(param.get("businessType"))) {
            param.put("channelId", map.get(param.get("businessType")));
        } else {
            param.put("channelId", map.get("default"));
        }
        param.remove("businessType");
        SoaResponse<KCBNoticeListDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCB_NOTICE_LIST_CMS, param, base, new TypeReference<SoaResponse<KCBNoticeListDto>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }


    public RespBean<?> geKCBNoticeListNew(BaseRequest<KCBNoticeListReqBody> reqBody) {
        try {
            Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
            String type = reqBody.getReqContent().getType();
            // 类型映射
            param.put("businessType", type);
            // 上交所公告type=1，其他type=0
            if ("200".equals(type)) {
                param.put("type", "1");
            } else {
                param.put("type", "0");
            }
            SoaResponse<KCBNoticeListDto> data = geKCBNoticeList(param, reqBody.getBase());
            Map<String, Object> docParams = new HashMap<>();
            StringBuffer sbDocs = new StringBuffer();
            List<KCBNoticeListDto> arrNotice = data.getList();
            List<KCBNoticeListDto> jaRes = new ArrayList<>();
            if (arrNotice.size() > 0) {
                //1.拼接DOCID
                String docId;
                for (KCBNoticeListDto map : arrNotice) {
                    docId = map.getDocId();
                    sbDocs.append(docId).append(",");
                }
                docParams.put("docId", sbDocs.deleteCharAt(sbDocs.length() - 1));
                //2.请求上游，获取阅读数
                MySoaResponse<Map<String, Object>> resReadCount = getBulletinReadCount(docParams, reqBody.getBase());
                //3.组装阅读数返回
                List<Map<String, Object>> arrReadCount = resReadCount.getList();
                if (arrReadCount.size() > 0) {
                    for (KCBNoticeListDto joNotice : arrNotice) {
                        docId = joNotice.getDocId();
                        int readCount = 0;
                        for (Map<String, Object> joReadCount : arrReadCount) {
                            if ((docId.equals(joReadCount.get("docId").toString()))) {
                                readCount = Util.getObjIntV(joReadCount.get("readCount"));
                                break;
                            }
                        }
                        joNotice.setReadCount(readCount);
                        jaRes.add(joNotice);
                    }
                } else {
                    jaRes = arrNotice;
                }
            }
            data.setList(jaRes);
            return RespBean.success(data);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }

    }


    public RespBean<?> geKCBNoticeListNewCMS(BaseRequest<KCBNoticeListReqBody> reqBody) {
        try {
            Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
            String type = reqBody.getReqContent().getType();
            // 类型映射
            param.put("businessType", type);
            // 上交所公告type=1，其他type=0
            if ("200".equals(type)) {
                param.put("type", "1");
            } else {
                param.put("type", "0");
            }
            SoaResponse<KCBNoticeListDto> data = geKCBNoticeListCMS(param, reqBody.getBase());
            Map<String, Object> docParams = new HashMap<>();
            StringBuffer sbDocs = new StringBuffer();
            List<KCBNoticeListDto> arrNotice = data.getList();
            List<KCBNoticeListDto> jaRes = new ArrayList<>();
            if (arrNotice.size() > 0) {
                //1.拼接DOCID
                String docId;
                for (KCBNoticeListDto map : arrNotice) {
                    docId = map.getDocId();
                    sbDocs.append(docId).append(",");
                }
                docParams.put("docId", sbDocs.deleteCharAt(sbDocs.length() - 1));
                //2.请求上游，获取阅读数
                MySoaResponse<Map<String, Object>> resReadCount = getBulletinReadCount(docParams, reqBody.getBase());
                //3.组装阅读数返回
                List<Map<String, Object>> arrReadCount = resReadCount.getList();
                if (arrReadCount.size() > 0) {
                    for (KCBNoticeListDto joNotice : arrNotice) {
                        docId = joNotice.getDocId();
                        int readCount = 0;
                        for (Map<String, Object> joReadCount : arrReadCount) {
                            if ((docId.equals(joReadCount.get("docId").toString()))) {
                                readCount = Util.getObjIntV(joReadCount.get("readCount"));
                                break;
                            }
                        }
                        joNotice.setReadCount(readCount);
                        jaRes.add(joNotice);
                    }
                } else {
                    jaRes = arrNotice;
                }
            }
            data.setList(jaRes);
            return RespBean.success(data);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }

    }


    public RespBean<?> getNoticeList(BaseRequest<KCBNoticeNewListReqBody> reqBody) {
        try {
            Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
            String type = reqBody.getReqContent().getChannelId();

            if (ObjectUtil.isEmpty(param.get("order"))) {
                param.put("order", param.get("order"));
            }
            param.put("token", "APPMQUERY");
            param.put("siteId", "28");
            // 类型映射
            Map<String, String> configMap = commonService.getActive("noticeList");
            if (ObjectUtil.isNotEmpty(type)) {
                param.put("channelId", configMap.get(type));
            } else {
                param.put("channelId", configMap.get("default"));
            }
            SoaResponse<KCBNoticeListDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCB_NOTICE_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<KCBNoticeListDto>>() {
            });

            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            Map<String, Object> docParams = new HashMap<>();
            StringBuffer sbDocs = new StringBuffer();
            List<KCBNoticeListDto> arrNotice = result.getList();
            List<KCBNoticeListDto> jaRes = new ArrayList<>();
            if (arrNotice.size() > 0) {
                //1.拼接DOCID
                String docId;
                for (KCBNoticeListDto kcbNoticeListDto : arrNotice) {
                    docId = kcbNoticeListDto.getDocId();
                    sbDocs.append(docId).append(",");
                }
                docParams.put("docId", sbDocs.deleteCharAt(sbDocs.length() - 1));
                //2.请求上游，获取阅读数
                MySoaResponse<Map<String, Object>> resReadCount = getBulletinReadCount(docParams, reqBody.getBase());
                //3.组装阅读数返回
                List<Map<String, Object>> arrReadCount = resReadCount.getList();
                if (arrReadCount.size() > 0) {
                    for (KCBNoticeListDto joNotice : arrNotice) {
                        docId = joNotice.getDocId();
                        int readCount = 0;
                        for (Map<String, Object> joReadCount : arrReadCount) {
                            if ((docId.equals(joReadCount.get("docId").toString()))) {
                                readCount = Util.getObjIntV(joReadCount.get("readCount"));
                                break;
                            }
                        }
                        joNotice.setReadCount(readCount);
                        jaRes.add(joNotice);
                    }
                } else {
                    jaRes = arrNotice;
                }
            }
            result.setList(jaRes);
            return RespBean.success(result);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }

    }

    public MySoaResponse<Map<String, Object>> getBulletinReadCount(Map<String, Object> docParams, ReqBaseVO base) {
        if (ObjectUtil.isEmpty(docParams.get("token"))) {
            docParams.put("token", "APPMQUERY");
        }
        MySoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_BULLETIN_READ_COUNT, docParams, base, new TypeReference<MySoaResponse<Map<String, Object>>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public RespBean<?> getNoticeListCMS(BaseRequest<KCBNoticeNewListReqBody> reqBody) {
        try {
            Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
            String type = reqBody.getReqContent().getChannelId();

            if (ObjectUtil.isEmpty(param.get("order"))) {
                param.put("order", param.get("order"));
            }
            param.put("token", "APPMQUERY");
            param.put("siteId", "28");
            // 类型映射
            Map<String, String> configMap = commonService.getActive("noticeList");
            if (ObjectUtil.isNotEmpty(type)) {
                param.put("channelId", configMap.get(type));
            } else {
                param.put("channelId", configMap.get("default"));
            }
            SoaResponse<KCBNoticeListDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCB_NOTICE_LIST_CMS, param, reqBody.getBase(), new TypeReference<SoaResponse<KCBNoticeListDto>>() {
            });

            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            Map<String, Object> docParams = new HashMap<>();
            StringBuffer sbDocs = new StringBuffer();
            List<KCBNoticeListDto> arrNotice = result.getList();
            List<KCBNoticeListDto> jaRes = new ArrayList<>();
            if (arrNotice.size() > 0) {
                //1.拼接DOCID
                String docId;
                for (KCBNoticeListDto kcbNoticeListDto : arrNotice) {
                    docId = kcbNoticeListDto.getDocId();
                    sbDocs.append(docId).append(",");
                }
                docParams.put("docId", sbDocs.deleteCharAt(sbDocs.length() - 1));
                //2.请求上游，获取阅读数
                MySoaResponse<Map<String, Object>> resReadCount = getBulletinReadCount(docParams, reqBody.getBase());
                //3.组装阅读数返回
                List<Map<String, Object>> arrReadCount = resReadCount.getList();
                if (arrReadCount.size() > 0) {
                    for (KCBNoticeListDto joNotice : arrNotice) {
                        docId = joNotice.getDocId();
                        int readCount = 0;
                        for (Map<String, Object> joReadCount : arrReadCount) {
                            if ((docId.equals(joReadCount.get("docId").toString()))) {
                                readCount = Util.getObjIntV(joReadCount.get("readCount"));
                                break;
                            }
                        }
                        joNotice.setReadCount(readCount);
                        jaRes.add(joNotice);
                    }
                } else {
                    jaRes = arrNotice;
                }
            }
            result.setList(jaRes);
            return RespBean.success(result);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }

    }
}
