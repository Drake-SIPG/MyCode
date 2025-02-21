package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import com.sse.sseapp.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/5/26 14:19
 */
@Service
public class AllStatusCountService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    MainStatusProccessService mainStatusProccess;

    @Autowired
    NewRegistrationSystemService registrationSystemService;

    @Autowired
    RedisService redisService;

    @Autowired
    CommonService commonService;

    public JSONObject getMergeAllStatusCountList(BaseRequest<MergeMarketTypeCountReqBody> mergeMarketTypeCount) {

        Map<String, Object> param = BeanUtil.beanToMap(mergeMarketTypeCount.getReqContent());
        Map<String, Object> o = new HashMap<>();
        Map<String, List<Map<String, Object>>> dataArray = new HashMap<>();
        param.put("token", "QUERY");
        SoaResponse<Map<String, Object>> marketTypeMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MERGE_MARKET_TYPE_COUNT_LIST, param, mergeMarketTypeCount.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });

        List<Map<String, Object>> marketTypeList = marketTypeMap.getList();
        List<Map<String, Object>> marketType = new ArrayList<>();
        // 处理板块数据
        for (Map<String, Object> map : marketTypeList) {
            Map<String, Object> data = new HashMap<>();
            String issueMarketType = String.valueOf(map.get("issueMarketType"));
            String num = String.valueOf(map.get("num"));
            data.put("num", num);
            data.put("issueMarketType", issueMarketType);
            marketType.add(data);
        }
        dataArray.put("marketTypeData", marketType);
        param.put("token", "APPMQUERY");
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MERGE_ALL_STATUS_COUNT_LIST, param, mergeMarketTypeCount.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });

        List<Map<String, Object>> dataList = dataMap.getList();
        List<Map<String, Object>> allStatusInfo = new ArrayList<>();
        Map<String, Map<String, DictVO>> dictMap = new HashMap<>();
//        if(null == dictMap || dictMap.size() <= 0){
//            dictMap = registrationSystemService.cacheAllDict();
//        }

        // 项目状态集合
        Map<String, DictVO> investorProStatusMap = commonService.getDictFromRedis(AppConstants.MERGE_MAIN_STATUS).get(AppConstants.MERGE_MAIN_STATUS);
        // 注册结果集合
        Map<String, DictVO> zcjgMap = commonService.getDictFromRedis(AppConstants.MERGE_ZCJG_STATUS).get(AppConstants.MERGE_ZCJG_STATUS);
        //重组委会议结果结合
        Map<String, DictVO> czwhyjgMap = commonService.getDictFromRedis(AppConstants.MERGE_CZWHYJG_STATUS).get(AppConstants.MERGE_CZWHYJG_STATUS);

        List<String> mainStatusList = new ArrayList<>(investorProStatusMap.keySet());
        List<String> zcjgStatusList = new ArrayList<>(zcjgMap.keySet());
        List<String> czwhyjgStatusList = new ArrayList<>(czwhyjgMap.keySet());

        bubbleSort(mainStatusList);
        bubbleSort(zcjgStatusList);
        bubbleSort(czwhyjgStatusList);

        // 主状态方法特殊处理，将5-暂缓审议放到3-已回复后面，如果状态位置有所变更，此代码需要修改
        if (mainStatusList.contains("5")) {
            mainStatusList.remove("5");
            mainStatusList.add(3, "5");
        }

        // 组装返回对象
        for (String mainStatus : mainStatusList) {
            mainStatusProccess.mergeMainStatusProccess(dataList, mainStatus, allStatusInfo, dictMap);
            // 50=注册结果
            if ("50".equals(mainStatus)) {
                mainStatusProccess.mergeSubStatusProcess(dataList, mainStatus, zcjgStatusList, allStatusInfo, dictMap);
            }
        }
        mainStatusProccess.handelCzwhhjgData(czwhyjgStatusList, allStatusInfo);
        dataArray.put("statistics", allStatusInfo);
        o.put("data", dataArray);
        return JSONUtil.parseObj(o);
    }

    public JSONObject getRegAllStatusCountList(BaseRequest<RegMarketTypeCountReqBody> regMarketTypeCount) {
        Map<String, Object> param = BeanUtil.beanToMap(regMarketTypeCount.getReqContent());
        Map<String, Object> o = new HashMap<>();
        Map<String, List<Map<String, Object>>> dataArray = new HashMap<>();
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ALL_STATUS_COUNT_LIST, param, regMarketTypeCount.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        param.put("token", "QUERY");
        SoaResponse<Map<String, Object>> marketTypeMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_REG_MARKET_TYPE_COUNT_LIST, param, regMarketTypeCount.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> marketTypeList = marketTypeMap.getList();
        List<Map<String, Object>> marketType = new ArrayList<>();

        // 处理板块数据
        for (Map<String, Object> map : marketTypeList) {
            Map<String, Object> data = new HashMap<>();
            String issueMarketType = String.valueOf(map.get("issueMarketType"));
            String num = String.valueOf(map.get("num"));
            data.put("num", num);
            data.put("issueMarketType", issueMarketType);
            marketType.add(data);
        }

        dataArray.put("marketTypeData", marketType);
        List<Map<String, Object>> dataList = dataMap.getList();

        List<Map<String, Object>> allStatusInfo = new ArrayList<>();

        Map<String, Map<String, DictVO>> dictMap = new HashMap<>();

        // 项目状态集合
        Map<String, DictVO> investorProStatusMap = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM).get(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
        // 上市委会议结果集合
        Map<String, DictVO> sswhyjgMap = commonService.getDictFromRedis(AppConstants.SSWHYJG_NUM).get(AppConstants.SSWHYJG_NUM);
        // 注册结果集合
        Map<String, DictVO> zcjgMap = commonService.getDictFromRedis(AppConstants.ZCJG_NUM).get(AppConstants.ZCJG_NUM);
        // 中止子状态集合
        Map<String, DictVO> zzzztMap = commonService.getDictFromRedis(AppConstants.ZZZZT_NUM).get(AppConstants.ZZZZT_NUM);

        List<String> mainStatusList = new ArrayList<>(investorProStatusMap.keySet());
        List<String> sswhyStatusList = new ArrayList<>(sswhyjgMap.keySet());
        List<String> zcjgStatusList = new ArrayList<>(zcjgMap.keySet());
        List<String> zzzztStatusList = new ArrayList<>(zzzztMap.keySet());

        bubbleSort(mainStatusList);
        bubbleSort(sswhyStatusList);
        bubbleSort(zcjgStatusList);
        bubbleSort(zzzztStatusList);

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

        // 组装返回对象
        for (String mainStatus : mainStatusList) {
            mainStatusProccess.regMainStatusProccess(dataList, mainStatus, allStatusInfo, dictMap);
            if ("3".equals(mainStatus) || "9".equals(mainStatus)) {
                mainStatusProccess.regSubStatusProcess(dataList, mainStatus, sswhyStatusList, allStatusInfo, dictMap);
            } else if ("5".equals(mainStatus)) {
                mainStatusProccess.regSubStatusProcess(dataList, mainStatus, zcjgStatusList, allStatusInfo, dictMap);
            }
        }
        dataArray.put("statistics", allStatusInfo);
        o.put("data", dataArray);
        return JSONUtil.parseObj(o);
    }


    public JSONObject getRefinancingAllStatusCountList(BaseRequest<RefinancingAllStatusCountListReqBody> request) {
        Map<String, Object> param = BeanUtil.beanToMap(request.getReqContent());
        Map<String, Object> o = new HashMap<>();
        Map<String, List<Map<String, Object>>> dataArray = new HashMap<>();
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCBZRZ_ALL_STATUS_COUNT_LIST, param, request.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        param.put("token", "QUERY");
        SoaResponse<Map<String, Object>> marketTypeMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ZRZ_MARKET_TYPE_COUNT_LIST, param, request.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> marketTypeList = marketTypeMap.getList();
        List<Map<String, Object>> marketType = new ArrayList<>();
        // 处理板块数据
        for (Map<String, Object> map : marketTypeList) {
            Map<String, Object> data = new HashMap<>();
            String issueMarketType = String.valueOf(map.get("issueMarketType"));
            String num = String.valueOf(map.get("num"));
            data.put("num", num);
            data.put("issueMarketType", issueMarketType);
            marketType.add(data);
        }
        dataArray.put("marketTypeData", marketType);
        List<Map<String, Object>> dataList = dataMap.getList();

        List<Map<String, Object>> allStatusInfo = new ArrayList<>();

        Map<String, Map<String, DictVO>> dictMap = new HashMap<>();

        //
        Map<String, DictVO> investorProStatusMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_MAIN_STATUS).get(AppConstants.KCBZRZ_MAIN_STATUS);
        //
        Map<String, DictVO> zcjgMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_ZRZZCJG_STATUS).get(AppConstants.KCBZRZ_ZRZZCJG_STATUS);

        List<String> mainStatusList = new ArrayList<>(investorProStatusMap.keySet());
        List<String> zcjgStatusList = new ArrayList<>(zcjgMap.keySet());

        bubbleSort(mainStatusList);
        bubbleSort(zcjgStatusList);

        // 主状态方法特殊处理，将5-暂缓审议放到3-已回复后面，如果状态位置有所变更，此代码需要修改
        if (mainStatusList.contains("5")) {
            mainStatusList.remove("5");
            mainStatusList.add(3, "5");
        }
        // 组装返回对象
        for (String mainStatus : mainStatusList) {
            mainStatusProccess.refinancingMainStatusProccess(dataList, mainStatus, allStatusInfo, dictMap);
            // 50=注册结果
            if ("50".equals(mainStatus)) {
                mainStatusProccess.refinancingSubStatusProcess(dataList, mainStatus, zcjgStatusList, allStatusInfo, dictMap);
            }
        }
        dataArray.put("statistics", allStatusInfo);
        o.put("data", dataArray);
        return JSONUtil.parseObj(o);
    }

    public JSONObject getSwitchBoardAllStatusCountList(BaseRequest<SwitchBoardAllStatusCountListReqBody> request) {
        Map<String, Object> param = BeanUtil.beanToMap(request);
        Map<String, Object> o = new HashMap<>();
        Map<String, List<Map<String, Object>>> dataArray = new HashMap<>();
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCBTB_STATUS_COUNT_LIST, param, request.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> dataList = dataMap.getList();

        List<Map<String, Object>> allStatusInfo = new ArrayList<>();

        Map<String, Map<String, DictVO>> dictMap = new HashMap<>();

        //获取字典表中转板上市审核主状态
        Map<String, DictVO> investorProStatusMap = commonService.getDictFromRedis(AppConstants.KCBTB_MAIN_STATUS).get(AppConstants.KCBTB_MAIN_STATUS);
        //获取字典表中转板上市审核结果子状态
        Map<String, DictVO> shjgMap = commonService.getDictFromRedis(AppConstants.KCBTB_TBSHJG_STATUS).get(AppConstants.KCBTB_TBSHJG_STATUS);
        //获取字典表中转板上市上市委审议子状态
        Map<String, DictVO> sswsyMap = commonService.getDictFromRedis(AppConstants.KCBTB_TBSSWSY_STATUS).get(AppConstants.KCBTB_TBSSWSY_STATUS);

        List<String> mainStatusList = new ArrayList<>(investorProStatusMap.keySet());
        List<String> shjgStatusList = new ArrayList<>(shjgMap.keySet());
        List<String> sswsyStatusList = new ArrayList<>(sswsyMap.keySet());

        bubbleSort(mainStatusList);
        bubbleSort(shjgStatusList);

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

        // 组装返回对象
        for (String mainStatus : mainStatusList) {
            mainStatusProccess.switchBoardMainStatusProccess(dataList, mainStatus, allStatusInfo, dictMap);
            // 50=审核结果
            if ("50".equals(mainStatus)) {
                mainStatusProccess.switchBoardSubStatusProcess(dataList, mainStatus, shjgStatusList, allStatusInfo, dictMap, null);
            }
        }

        Map<String, Object> statusNumMap = mainStatusProccess.handelSswhhjgData(allStatusInfo);

        // 上市委审议加子状态返回
        for (Map<String, Object> statusMap : allStatusInfo) {
            if (statusMap.get("status") != null && (statusMap.get("status").toString().contains("3") || statusMap.get("status").toString().contains("4") ||
                    statusMap.get("status").toString().contains("5"))) {
                // 99=上市委审议
                mainStatusProccess.switchBoardSubStatusProcess(dataList, "3,4,5", sswsyStatusList, allStatusInfo, dictMap, statusNumMap);
            }
        }
        dataArray.put("statistics", allStatusInfo);
        o.put("data", dataArray);
        return JSONUtil.parseObj(o);
    }


    /**
     * 冒泡排序（字符串数字排序）
     */
    private static void bubbleSort(List<String> sourceList) {
        for (int i = 0; i < sourceList.size() - 1; i++) {
            for (int j = 0; j < sourceList.size() - 1 - i; j++) {
                int tmp1 = Integer.parseInt(sourceList.get(j));
                int tmp2 = Integer.parseInt(sourceList.get(j + 1));

                if (tmp1 > tmp2) {
                    String temp = sourceList.get(j);
                    sourceList.set(j, sourceList.get(j + 1));
                    sourceList.set(j + 1, temp);
                }
            }
        }
    }


    public JSONObject getDrAllStatusCountList(BaseRequest<DrAllStatusCountListReqBody> request) {
        Map<String, Object> param = BeanUtil.beanToMap(request.getReqContent());
        Map<String, Object> o = new HashMap<>();
        Map<String, List<Map<String, Object>>> dataArray = new HashMap<>();
        param.put("token", "QUERY");
        SoaResponse<Map<String, Object>> marketTypeMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_DR_MARKET_TYPE_COUNT_LIST, param, request.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });

        List<Map<String, Object>> marketTypeList = marketTypeMap.getList();
        List<Map<String, Object>> marketType = new ArrayList<>();
        // 处理板块数据
        for (Map<String, Object> map : marketTypeList) {
            Map<String, Object> data = new HashMap<>();
            String issueMarketType = String.valueOf(map.get("issueMarketType"));
            String num = String.valueOf(map.get("num"));
            data.put("num", num);
            data.put("issueMarketType", issueMarketType);
            marketType.add(data);
        }
        dataArray.put("marketTypeData", marketType);
        param.put("token", "QUERY");
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_DR_ALL_STATUS_COUNT_LIST, param, request.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });

        List<Map<String, Object>> dataList = dataMap.getList();
        List<Map<String, Object>> allStatusInfo = new ArrayList<>();
        Map<String, Map<String, DictVO>> dictMap = new HashMap<>();
//        if(null == dictMap || dictMap.size() <= 0){
//            dictMap = registrationSystemService.cacheAllDict();
//        }

        // 项目状态集合
        Map<String, DictVO> investorProStatusMap = commonService.getDictFromRedis(AppConstants.DR_MAIN_STATUS).get(AppConstants.DR_MAIN_STATUS);
        // 注册结果集合
        Map<String, DictVO> zcjgMap = commonService.getDictFromRedis(AppConstants.DR_ZCJG_STATUS).get(AppConstants.DR_ZCJG_STATUS);

        List<String> mainStatusList = new ArrayList<>(investorProStatusMap.keySet());
        List<String> zcjgStatusList = new ArrayList<>(zcjgMap.keySet());

        bubbleSort(mainStatusList);
        bubbleSort(zcjgStatusList);

        // 主状态方法特殊处理，将5-暂缓审议放到3-已回复后面，如果状态位置有所变更，此代码需要修改
        if (mainStatusList.contains("5")) {
            mainStatusList.remove("5");
            mainStatusList.add(3, "5");
        }

        // 组装返回对象
        for (String mainStatus : mainStatusList) {
            mainStatusProccess.mergeMainStatusProccess(dataList, mainStatus, allStatusInfo, dictMap);
            // 50=注册结果
            if ("50".equals(mainStatus)) {
                mainStatusProccess.mergeSubStatusProcess(dataList, mainStatus, zcjgStatusList, allStatusInfo, dictMap);
            }
        }

        dataArray.put("statistics", allStatusInfo);
        o.put("data", dataArray);
        return JSONUtil.parseObj(o);
    }

}







