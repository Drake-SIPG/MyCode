package com.sse.sseapp.service;

import cn.hutool.core.util.StrUtil;

import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : liuxinyu
 * @date : 2023/5/26 14:51
 */
@Service
public class MainStatusProccessService {
    @Autowired
    RedisService redisService;

    @Autowired
    NewRegistrationSystemService registrationSystemService;

    @Autowired
    CommonService commonService;

    /**
     * 处理重组委会议结果状态项目总数
     */
    public void handelCzwhhjgData(List<String> czwhyjgStatusList, List<Map<String, Object>> allStatusInfo) {
        //用于记录重组委会议结果项目总数
        int czwhhjgNum = 0;

        //统计重组委会议结果状态总数
        for (Map<String, Object> map : allStatusInfo) {
            String statusTmp = String.valueOf(map.get("status"));
            String numTmp = String.valueOf(map.get("num"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }
            if (StrUtil.isEmpty(numTmp)) {
                continue;
            }
            if (czwhyjgStatusList.contains(statusTmp)) {
                czwhhjgNum += Integer.parseInt(numTmp);
            }

        }
        //存放重组委会议结果总数数据
        Map<String, Object> czwhhjgMap = new HashMap<>();
        czwhhjgMap.put("status", "7,8,9,10,11");
        czwhhjgMap.put("statusDesc", "重组委会议结果");
        czwhhjgMap.put("num", czwhhjgNum);
        //去除重组委会议结果相关子状态统计数据
        List<Map<String, Object>> lastStatusInfo = new ArrayList<>();
        for (Map<String, Object> map : allStatusInfo) {
            String status = String.valueOf(map.get("status"));
            if (!czwhyjgStatusList.contains(status) && !"4".contains(status) && !"6".equals(status)) {
                if ("3".equals(status)) {
                    lastStatusInfo.add(map);
                    lastStatusInfo.add(czwhhjgMap);
                } else {
                    lastStatusInfo.add(map);
                }
            }
        }

        allStatusInfo.clear();
        allStatusInfo.addAll(lastStatusInfo);
    }

    /**
     * merge项目主状态处理
     */
    public void mergeMainStatusProccess(List<Map<String, Object>> dataList, String mainStatus,
                                        List<Map<String, Object>> allStatusInfo, Map<String, Map<String, DictVO>> dictMap) {
        boolean flag = true;
        for (Map<String, Object> map : dataList) {
            String statusTmp = String.valueOf(map.get("status"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }
            String[] statuses = statusTmp.split("-");
            String status = statuses[0];
            for (Map<String, Object> statusInfo : allStatusInfo) {
                String ss = (String) statusInfo.get("status");
                if (mainStatus.equals(ss)) {
                    return;
                }
            }
            if (mainStatus.equals(status)) {
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("status", status);
                dictMap = commonService.getDictFromRedis(AppConstants.MERGE_MAIN_STATUS);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.MERGE_MAIN_STATUS, status);
                newMap.put("statusDesc", statusDesc);
                newMap.put("num", map.get("num"));
                allStatusInfo.add(newMap);

                flag = false;
                break;
            }
        }
        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("num", 0);
            map.put("status", mainStatus);
            dictMap = commonService.getDictFromRedis(AppConstants.MERGE_MAIN_STATUS);
            String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.MERGE_MAIN_STATUS, mainStatus);
            map.put("statusDesc", statusDesc);
            allStatusInfo.add(map);
        }
    }

    /**
     * merge子状态处理
     */
    public void mergeSubStatusProcess(List<Map<String, Object>> dataList, String mainStatus,
                                      List<String> subStatusList, List<Map<String, Object>> allStatusInfo,
                                      Map<String, Map<String, DictVO>> dictMap) {
        List<Map<String, Object>> subStatusInfo = new ArrayList<>();
        String dictType = "";
        if ("50".equals(mainStatus)) {
            // 50=注册结果
            dictType = AppConstants.MERGE_ZCJG_STATUS;
        }
        for (String subStatus : subStatusList) {
            boolean flag = true;
            for (Map<String, Object> map : dataList) {
                String statusTmp = String.valueOf(map.get("status"));
                if (StrUtil.isEmpty(statusTmp) || !statusTmp.startsWith(mainStatus + "-")) {
                    continue;
                }
                String[] statuses = statusTmp.split("-");
                if (statuses.length != 2) {
                    continue;
                }
                String subStatusTmp = statuses[1];
                if (subStatus.equals(subStatusTmp)) {
                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put("status", subStatus);
                    dictMap = commonService.getDictFromRedis(dictType);
                    String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                    newMap.put("statusDesc", statusDesc);
                    newMap.put("num", map.get("num"));
                    subStatusInfo.add(newMap);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("num", 0);
                map.put("status", subStatus);
                dictMap = commonService.getDictFromRedis(dictType);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                map.put("statusDesc", statusDesc);
                subStatusInfo.add(map);
            }
        }
        for (Map<String, Object> mainMap : allStatusInfo) {
            String mainStat = String.valueOf(mainMap.get("status"));
            if (mainStatus.equals(mainStat)) {
                mainMap.put("subList", subStatusInfo);
                break;
            }
        }
    }
    /**
     * dr项目主状态处理
     */
    public void drMainStatusProccess(List<Map<String, Object>> dataList, String mainStatus,
                                     List<Map<String, Object>> allStatusInfo, Map<String, Map<String, DictVO>> dictMap) {
        boolean flag = true;
        for (Map<String, Object> map : dataList) {
            String statusTmp = String.valueOf(map.get("status"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }
            String[] statuses = statusTmp.split("-");
            String status = statuses[0];
            for (Map<String, Object> statusInfo : allStatusInfo) {
                String ss = (String) statusInfo.get("status");
                if (mainStatus.equals(ss)) {
                    return;
                }
            }
            if (mainStatus.equals(status)) {
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("status", status);
                dictMap = commonService.getDictFromRedis(AppConstants.DR_MAIN_STATUS);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.DR_MAIN_STATUS, status);
                newMap.put("statusDesc", statusDesc);
                newMap.put("num", map.get("num"));
                allStatusInfo.add(newMap);

                flag = false;
                break;
            }
        }
        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("num", 0);
            map.put("status", mainStatus);
            dictMap = commonService.getDictFromRedis(AppConstants.DR_MAIN_STATUS);
            String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.DR_MAIN_STATUS, mainStatus);
            map.put("statusDesc", statusDesc);
            allStatusInfo.add(map);
        }
    }

    /**
     * dr子状态处理
     */
    public void drSubStatusProcess(List<Map<String, Object>> dataList, String mainStatus,
                                   List<String> subStatusList, List<Map<String, Object>> allStatusInfo,
                                   Map<String, Map<String, DictVO>> dictMap) {
        List<Map<String, Object>> subStatusInfo = new ArrayList<>();
        String dictType = "";
        if ("50".equals(mainStatus)) {
            // 50=注册结果
            dictType = AppConstants.DR_ZCJG_STATUS;
        }
        for (String subStatus : subStatusList) {
            boolean flag = true;
            for (Map<String, Object> map : dataList) {
                String statusTmp = String.valueOf(map.get("status"));
                if (StrUtil.isEmpty(statusTmp) || !statusTmp.startsWith(mainStatus + "-")) {
                    continue;
                }
                String[] statuses = statusTmp.split("-");
                if (statuses.length != 2) {
                    continue;
                }
                String subStatusTmp = statuses[1];
                if (subStatus.equals(subStatusTmp)) {
                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put("status", subStatus);
                    dictMap = commonService.getDictFromRedis(dictType);
                    String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                    newMap.put("statusDesc", statusDesc);
                    newMap.put("num", map.get("num"));
                    subStatusInfo.add(newMap);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("num", 0);
                map.put("status", subStatus);
                dictMap = commonService.getDictFromRedis(dictType);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                map.put("statusDesc", statusDesc);
                subStatusInfo.add(map);
            }
        }
        for (Map<String, Object> mainMap : allStatusInfo) {
            String mainStat = String.valueOf(mainMap.get("status"));
            if (mainStatus.equals(mainStat)) {
                mainMap.put("subList", subStatusInfo);
                break;
            }
        }
    }
    /**
     * reg项目主状态处理
     */
    public void regMainStatusProccess(List<Map<String, Object>> dataList, String mainStatus,
                                      List<Map<String, Object>> allStatusInfo, Map<String, Map<String, DictVO>> dictMap) {
        boolean flag = true;
        for (Map<String, Object> map : dataList) {
            String statusTmp = String.valueOf(map.get("status"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }
            String[] statuses = statusTmp.split("-");
            String status = statuses[0];
            for (Map<String, Object> statusInfo : allStatusInfo) {
                String ss = (String) statusInfo.get("status");
                if (mainStatus.equals(ss)) {
                    return;
                }
            }
            if (mainStatus.equals(status)) {
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("status", status);
                dictMap = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.INVESTOR_PROJECT_STATUS_NUM, status);
                newMap.put("statusDesc", statusDesc);
                newMap.put("num", map.get("num"));
                allStatusInfo.add(newMap);
                flag = false;
                break;
            }
        }
        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("num", 0);
            map.put("status", mainStatus);
            dictMap = commonService.getDictFromRedis(AppConstants.INVESTOR_PROJECT_STATUS_NUM);
            String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.INVESTOR_PROJECT_STATUS_NUM, mainStatus);
            map.put("statusDesc", statusDesc);
            allStatusInfo.add(map);
        }
    }

    /**
     *
     */
    public void regSubStatusProcess(List<Map<String, Object>> dataList, String mainStatus,
                                    List<String> subStatusListTmp, List<Map<String, Object>> allStatusInfo,
                                    Map<String, Map<String, DictVO>> dictMap) {
        List<Map<String, Object>> subStatusInfo = new ArrayList<>();
        String dictType = "";

        List<String> subStatusList = new ArrayList<>();
//    	上市委会议或复审会议结果：
//    	1-上市委会议通过；
//    	3-上市委会议未通过
//    	4-上市委复审会通过
//    	5-上市委复审会不通过
//    	6-暂缓审议（上市委会议）
        // 上市委会议
        if ("3".equals(mainStatus)) {
            dictType = AppConstants.SSWHYJG_NUM;
            subStatusList.add(subStatusListTmp.get(0));
            subStatusList.add(subStatusListTmp.get(1));
            subStatusList.add(subStatusListTmp.get(4));
        } else if ("9".equals(mainStatus)) {
            // 复审委会议
            dictType = AppConstants.SSWHYJG_NUM;
            subStatusList.add(subStatusListTmp.get(2));
            subStatusList.add(subStatusListTmp.get(3));
        } else if ("5".equals(mainStatus)) {
            dictType = AppConstants.ZCJG_NUM;
            subStatusList.addAll(subStatusListTmp);
        } else if ("7".equals(mainStatus)) {
            dictType = AppConstants.ZZZZT_NUM;
            subStatusList.addAll(subStatusListTmp);
        }

        for (String subStatus : subStatusList) {
            boolean flag = true;
            for (Map<String, Object> map : dataList) {
                String statusTmp = String.valueOf(map.get("status"));
                if (StrUtil.isEmpty(statusTmp) || !statusTmp.startsWith(mainStatus + "-")) {
                    continue;
                }
                String[] statuses = statusTmp.split("-");
                if (statuses.length != 2) {
                    continue;
                }
                String subStatusTmp = statuses[1];
                if (subStatus.equals(subStatusTmp)) {

                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put("status", subStatus);
                    dictMap = commonService.getDictFromRedis(dictType);
                    String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                    newMap.put("statusDesc", statusDesc);
                    newMap.put("num", map.get("num"));
                    subStatusInfo.add(newMap);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("num", 0);
                map.put("status", subStatus);
                dictMap = commonService.getDictFromRedis(dictType);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                map.put("statusDesc", statusDesc);
                subStatusInfo.add(map);
            }
        }
        for (Map<String, Object> mainMap : allStatusInfo) {
            String mainStat = String.valueOf(mainMap.get("status"));
            if (mainStatus.equals(mainStat)) {
                mainMap.put("subList", subStatusInfo);
                break;
            }
        }
    }

    /**
     * 再融资项目主状态处理
     */
    public void refinancingMainStatusProccess(List<Map<String, Object>> dataList, String mainStatus,
                                              List<Map<String, Object>> allStatusInfo, Map<String, Map<String, DictVO>> dictMap) {
        boolean flag = true;
        for (Map<String, Object> map : dataList) {
            String statusTmp = String.valueOf(map.get("status"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }
            String[] statuses = statusTmp.split("-");
            String status = statuses[0];
            for (Map<String, Object> statusInfo : allStatusInfo) {
                String ss = (String) statusInfo.get("status");
                if (mainStatus.equals(ss)) {
                    return;
                }
            }
            if (mainStatus.equals(status)) {
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("status", status);
                dictMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_MAIN_STATUS);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.KCBZRZ_MAIN_STATUS, status);
                newMap.put("statusDesc", statusDesc);
                newMap.put("num", map.get("num"));
                allStatusInfo.add(newMap);

                flag = false;
                break;
            }
        }

        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("num", 0);
            map.put("status", mainStatus);
            dictMap = commonService.getDictFromRedis(AppConstants.KCBZRZ_MAIN_STATUS);
            String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.KCBZRZ_MAIN_STATUS, mainStatus);
            map.put("statusDesc", statusDesc);
            allStatusInfo.add(map);
        }
    }

    /**
     * 再融资子状态处理
     */
    public void refinancingSubStatusProcess(List<Map<String, Object>> dataList, String mainStatus,
                                            List<String> subStatusList, List<Map<String, Object>> allStatusInfo,
                                            Map<String, Map<String, DictVO>> dictMap) {
        List<Map<String, Object>> subStatusInfo = new ArrayList<>();
        String dictType = "";
        if ("50".equals(mainStatus)) {
            // 50=注册结果
            dictType = AppConstants.KCBZRZ_ZRZZCJG_STATUS;
        }
        for (String subStatus : subStatusList) {
            boolean flag = true;
            for (Map<String, Object> map : dataList) {
                String statusTmp = String.valueOf(map.get("status"));
                if (StrUtil.isEmpty(statusTmp) || !statusTmp.startsWith(mainStatus + "-")) {
                    continue;
                }
                String[] statuses = statusTmp.split("-");
                if (statuses.length != 2) {
                    continue;
                }
                String subStatusTmp = statuses[1];
                if (subStatus.equals(subStatusTmp)) {

                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put("status", subStatus);
                    dictMap = commonService.getDictFromRedis(dictType);
                    String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                    newMap.put("statusDesc", statusDesc);
                    newMap.put("num", map.get("num"));
                    subStatusInfo.add(newMap);

                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("num", 0);
                map.put("status", subStatus);
                dictMap = commonService.getDictFromRedis(dictType);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                map.put("statusDesc", statusDesc);
                subStatusInfo.add(map);
            }
        }
        for (Map<String, Object> mainMap : allStatusInfo) {
            String mainStat = String.valueOf(mainMap.get("status"));
            if (mainStatus.equals(mainStat)) {
                mainMap.put("subList", subStatusInfo);
                break;
            }
        }
    }

    /**
     * kcbtb项目主状态处理
     */
    public void switchBoardMainStatusProccess(List<Map<String, Object>> dataList, String mainStatus,
                                              List<Map<String, Object>> allStatusInfo, Map<String, Map<String, DictVO>> dictMap) {
        boolean flag = true;
        for (Map<String, Object> map : dataList) {
            String statusTmp = String.valueOf(map.get("status"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }

            String[] statuses = statusTmp.split("-");
            String status = statuses[0];

            for (Map<String, Object> statusInfo : allStatusInfo) {
                String ss = (String) statusInfo.get("status");
                if (mainStatus.equals(ss)) {
                    return;
                }
            }

            if (mainStatus.equals(status)) {
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("status", status);
                dictMap = commonService.getDictFromRedis(AppConstants.KCBTB_MAIN_STATUS);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.KCBTB_MAIN_STATUS, status);
                newMap.put("statusDesc", statusDesc);
                newMap.put("num", map.get("num"));
                allStatusInfo.add(newMap);

                flag = false;
                break;
            }
        }
        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("num", 0);
            map.put("status", mainStatus);
            dictMap = commonService.getDictFromRedis(AppConstants.KCBTB_MAIN_STATUS);
            String statusDesc = registrationSystemService.getConstDescByCode(dictMap, AppConstants.KCBTB_MAIN_STATUS, mainStatus);
            map.put("statusDesc", statusDesc);
            allStatusInfo.add(map);
        }
    }

    /**
     * kcbtb子状态处理
     */
    public void switchBoardSubStatusProcess(List<Map<String, Object>> dataList, String mainStatus,
                                            List<String> subStatusList, List<Map<String, Object>> allStatusInfo,
                                            Map<String, Map<String, DictVO>> dictMap, Map<String, Object> statusNumMap) {
        List<Map<String, Object>> subStatusInfo = new ArrayList<>();
        String dictType = "";
        if ("50".equals(mainStatus)) {
            // 50=审核结果
            dictType = AppConstants.KCBTB_TBSHJG_STATUS;
        } else if ("3,4,5".equals(mainStatus)) {
            dictType = AppConstants.KCBTB_TBSSWSY_STATUS;
        }

        for (String subStatus : subStatusList) {
            boolean flag = true;
            for (Map<String, Object> map : dataList) {
                String statusTmp = String.valueOf(map.get("status"));
                if (StrUtil.isEmpty(statusTmp) || !statusTmp.startsWith(mainStatus + "-")) {
                    continue;
                }
                String[] statuses = statusTmp.split("-");
                if (statuses.length != 2) {
                    continue;
                }
                String subStatusTmp = statuses[1];
                if (subStatus.equals(subStatusTmp)) {

                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put("status", subStatus);
                    dictMap = commonService.getDictFromRedis(dictType);
                    String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                    newMap.put("statusDesc", statusDesc);
                    newMap.put("num", map.get("num"));
                    subStatusInfo.add(newMap);

                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("status", subStatus);
                dictMap = commonService.getDictFromRedis(dictType);
                String statusDesc = registrationSystemService.getConstDescByCode(dictMap, dictType, subStatus);
                map.put("statusDesc", statusDesc);
                if ("3,4,5".equals(mainStatus)) {
                    if ("2".equals(subStatus)) {
                        map.put("num", statusNumMap.get("3"));
                    } else if ("1".equals(subStatus)) {
                        map.put("num", statusNumMap.get("4"));
                    } else if ("3".equals(subStatus)) {
                        map.put("num", statusNumMap.get("5"));
                    }
                } else {
                    map.put("num", 0);
                }
                subStatusInfo.add(map);
            }
        }
        for (Map<String, Object> mainMap : allStatusInfo) {
            String mainStat = String.valueOf(mainMap.get("status"));
            if (mainStatus.equals(mainStat)) {
                mainMap.put("subList", subStatusInfo);
                break;
            }
        }
    }

    /**
     * kcbtb处理上市委会议结果状态项目总数
     */
    public Map<String, Object> handelSswhhjgData(List<Map<String, Object>> allStatusInfo) {
        //用于记录上市委会议结果项目总数
        int sswhhjgNum = 0;
        List<String> sswhyjgStatusList = new ArrayList<>();
        sswhyjgStatusList.add("3");
        sswhyjgStatusList.add("4");
        sswhyjgStatusList.add("5");
        int sswsytgNum = 0;
        int sswshwtgNum = 0;
        int zhsyNum = 0;
        Map<String, Object> statusNumMap = new HashMap<>();

        //统计上市委会议结果状态总数
        for (Map<String, Object> map : allStatusInfo) {
            String statusTmp = String.valueOf(map.get("status"));
            String numTmp = String.valueOf(map.get("num"));
            if (StrUtil.isEmpty(statusTmp)) {
                continue;
            }
            if (StrUtil.isEmpty(numTmp)) {
                continue;
            }

            if (sswhyjgStatusList.contains(statusTmp)) {
                sswhhjgNum += Integer.parseInt(numTmp);
            }

            switch (statusTmp) {
                case "3":
                    sswsytgNum = Integer.parseInt(numTmp);
                    break;
                case "4":
                    sswshwtgNum = Integer.parseInt(numTmp);
                    break;
                case "5":
                    zhsyNum = Integer.parseInt(numTmp);
                    break;
                default:
                    break;
            }
        }

        statusNumMap.put("3", sswsytgNum);
        statusNumMap.put("4", sswshwtgNum);
        statusNumMap.put("5", zhsyNum);
        //存放上市委会议结果总数数据
        Map<String, Object> czwhhjgMap = new HashMap<>();
        czwhhjgMap.put("status", "3,4,5");
        czwhhjgMap.put("statusDesc", "上市委审议");
        czwhhjgMap.put("num", sswhhjgNum);
        //去除重组委会议结果相关子状态统计数据
        List<Map<String, Object>> lastStatusInfo = new ArrayList<>();
        for (Map<String, Object> map : allStatusInfo) {
            String status = String.valueOf(map.get("status"));
            if (!sswhyjgStatusList.contains(status)) {
                //上市委审议位置在状态2已问询后
                if ("2".equals(status)) {
                    lastStatusInfo.add(map);
                    lastStatusInfo.add(czwhhjgMap);
                } else {
                    lastStatusInfo.add(map);
                }
            }
        }
        allStatusInfo.clear();
        allStatusInfo.addAll(lastStatusInfo);
        return statusNumMap;
    }
}
