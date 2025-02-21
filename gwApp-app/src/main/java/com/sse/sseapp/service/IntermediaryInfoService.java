package com.sse.sseapp.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/5/26 9:39
 */
@Service
public class IntermediaryInfoService {
    @Autowired
    ProxyProvider proxyProvider;

    private static final String FILE_BASE_PATH = "http://10.10.10.20:8085";

    /**
     * 并购重组保荐机构列表
     */
    public JSONObject getMergeInterInfoList() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> o = new HashMap<>();
        param.put("type", "1");
        param.put("pageSize", 0);
        param.put("token", "APPMQUERY");
        param.put("pageNo", "1");
        o.put("fileBasePath", FILE_BASE_PATH);
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MERGE_INTER_INFO_LIST, param, null, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> dataList = dataMap.getList();

        Map<String, List<Map<String, String>>> intermediaryInfo = new HashMap<>();
        for (Map<String, Object> map : dataList) {
            Map<String, String> data = new HashMap<>();
            String fullName = String.valueOf(map.get("name"));
            String shortName = map.get("shortName") == null ? null : String.valueOf(map.get("shortName"));
            String num = String.valueOf(map.get("num"));
            String pinYin;
            if (StrUtil.isEmpty(shortName)) {
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
        return JSONUtil.parseObj(o);
    }

    /**
     * 保荐机构列表
     */
    public JSONObject getIntermediaryInfoList() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> o = new HashMap<>();
        param.put("type", "1");
        param.put("pageSize", 0);
        param.put("siteId", "45");
        param.put("token", "APPMQUERY");
        param.put("pageNo", "1");
        o.put("fileBasePath", FILE_BASE_PATH);
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_INTERMEDIARY_INFO_LIST, param, null, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> dataList = dataMap.getList();
        Map<String, List<Map<String, String>>> intermediaryInfo = new HashMap<>();
        for (Map<String, Object> map : dataList) {
            Map<String, String> data = new HashMap<>();
            String fullName = String.valueOf(map.get("name"));
            String shortName = map.get("shortName") == null ? null : String.valueOf(map.get("shortName"));
            String num = String.valueOf(map.get("num"));
            String pinYin;
            if (StrUtil.isEmpty(shortName)) {
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
        return JSONUtil.parseObj(o);
    }


    public JSONObject getRefinancingInterInfoList() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> o = new HashMap<>();
        param.put("type", "1");
        param.put("pageSize", 0);
        param.put("token", "APPMQUERY");
        param.put("pageNo", "1");
        o.put("fileBasePath", FILE_BASE_PATH);
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ZRZ_INTER_INFO_LIST, param, null, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> dataList = dataMap.getList();

        Map<String, List<Map<String, String>>> intermediaryInfo = new HashMap<>();
        for (Map<String, Object> map : dataList) {
            Map<String, String> data = new HashMap<>();
            String fullName = String.valueOf(map.get("name"));
            String shortName = map.get("shortName") == null ? null : String.valueOf(map.get("shortName"));
            String num = String.valueOf(map.get("num"));
            String pinYin;
            if (StrUtil.isEmpty(shortName)) {
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
        return JSONUtil.parseObj(o);
    }

    public JSONObject getSwitchBoardInterInfoList() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> o = new HashMap<>();
        param.put("type", "1");
        param.put("pageSize", 0);
        param.put("token", "APPMQUERY");
        param.put("pageNo", "1");
        o.put("fileBasePath", FILE_BASE_PATH);
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCKTB_INTER_INFO_LIST, param, null, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> dataList = dataMap.getList();

        Map<String, List<Map<String, String>>> intermediaryInfo = new HashMap<>();
        for (Map<String, Object> map : dataList) {
            Map<String, String> data = new HashMap<>();
            String fullName = String.valueOf(map.get("name"));
            String shortName = map.get("shortName") == null ? null : String.valueOf(map.get("shortName"));
            String num = String.valueOf(map.get("num"));
            String pinYin;
            if (StrUtil.isEmpty(shortName)) {
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
        return JSONUtil.parseObj(o);
    }

    public JSONObject getDrInterInfoList() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> o = new HashMap<>();
        param.put("type", "1");
        param.put("pageSize", 0);
        param.put("token", "APPMQUERY");
        param.put("pageNo", "1");
        o.put("fileBasePath", FILE_BASE_PATH);
        SoaResponse<Map<String, Object>> dataMap = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_DR_INTER_INFO_LIST, param, null, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        List<Map<String, Object>> dataList = dataMap.getList();

        Map<String, List<Map<String, String>>> intermediaryInfo = new HashMap<>();
        for (Map<String, Object> map : dataList) {
            Map<String, String> data = new HashMap<>();
            String fullName = String.valueOf(map.get("interName"));
            String shortName = map.get("interAbbr") == null ? null : String.valueOf(map.get("interAbbr"));
            String num = String.valueOf(map.get("interId"));
            String pinYin;
            if (StrUtil.isEmpty(shortName)) {
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
        return JSONUtil.parseObj(o);
    }
}
