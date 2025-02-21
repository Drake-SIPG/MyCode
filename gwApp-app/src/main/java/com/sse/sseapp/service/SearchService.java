package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.SearchDataReqBody;
import com.sse.sseapp.form.request.SearchHasDataReqBody;
import com.sse.sseapp.form.request.SearchInfoReqBody;
import com.sse.sseapp.form.request.SearchStockReqBody;
import com.sse.sseapp.form.response.SearchDataResBody;
import com.sse.sseapp.form.response.SearchHasDataResBody;
import com.sse.sseapp.form.response.SearchInfoResBody;
import com.sse.sseapp.form.response.SearchStockResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.dto.SearchDataDto;
import com.sse.sseapp.proxy.query.dto.SearchInfoDto;
import com.sse.sseapp.proxy.query.dto.SearchStockDto;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    ProxyProvider proxyProvider;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ISysConfigFeign sysConfigFeign;
    @Autowired
    HomeNewService homeNewService;

    public String[] searchHot() {
        String url = this.sysConfigFeign.getConfigKey(AppConstants.SEARCH_HOT_URL);
        String body = HttpRequest.get(url + DateUtil.current()).execute().body().trim();
        if (ObjectUtil.isNotEmpty(body) && body.endsWith(",")) {
            body = body.substring(0, body.lastIndexOf(","));
        }
        return body.split(",");
    }

    public SearchStockResBody searchStock(BaseRequest<SearchStockReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        SearchStockReqBody reqContent = reqBody.getReqContent();
        reqContent.setSEC_CODE(StringFilter(reqContent.getSEC_CODE()));
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        SearchStockDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SEARCH_STOCK, data, base, new TypeReference<SearchStockDto>() {
        });

        List<SearchStockDto.DataDTO> list = ObjectUtil.defaultIfNull(result.getResult(), Lists.newArrayList());
        list.removeIf(dataDTO -> "RCM".equals(dataDTO.getSEC_TYPE()));
        List<SearchStockResBody.DataDTO> resultList = list.stream().map(this::searchStockTrans).collect(Collectors.toList());
        SearchStockResBody resBody = new SearchStockResBody();
        resBody.setList(switchType(resultList));
        return resBody;
    }

    public SearchInfoResBody searchInfo(BaseRequest<SearchInfoReqBody> reqBody) {
        SearchInfoDto result = searchInfo0(reqBody);
        SearchInfoResBody resBody = new SearchInfoResBody();
        List<SearchInfoDto.DataDTO.KnowledgeListDTO> list = result.getData().getKnowledgeList();
        for (SearchInfoDto.DataDTO.KnowledgeListDTO dto : ObjectUtil.defaultIfNull(list, Collections.<SearchInfoDto.DataDTO.KnowledgeListDTO>emptyList())) {
            //获取随机图片放入
            String imageUrl = homeNewService.getRandomImageForNotice(dto.getId().toString());
            if (StrUtil.isNotEmpty(imageUrl)) {
                dto.setImageUrl(imageUrl);
            }
            for (SearchInfoDto.DataDTO.KnowledgeListDTO.ExtendDTO item : ObjectUtil.defaultIfNull(dto.getExtend(), Collections.<SearchInfoDto.DataDTO.KnowledgeListDTO.ExtendDTO>emptyList())) {
                if (Objects.equals(item.getName(), "CURL")) {
                    dto.setUrl(item.getValue());
                }
            }
        }
        if (ObjectUtil.isEmpty(list)) {
            resBody.setKnowledgeList(new ArrayList<>());
        } else {
            resBody.setKnowledgeList(list);
        }
        return resBody;
    }

    @SneakyThrows
    public SearchDataResBody searchData(BaseRequest<SearchDataReqBody> reqBody) {
        SearchDataResDto resData = searchData0(reqBody);
        SearchDataResBody resBody = new SearchDataResBody();
        if (ObjectUtil.isNotEmpty(resData.getAnswerList())) {
            resBody.setAnswerList(resData.getAnswerList());
        } else {
            resBody.setAnswerList(new ArrayList<>());
        }
        resBody.setMatchedList(resData.getMatchedList());
        if (ObjectUtil.isNotEmpty(resBody.getAnswerList())) {
            for (SearchDataResDto.AnswerListDTO answerListDTO : resBody.getAnswerList()) {
                List<Map<String, String>> list = answerListDTO.getAnswer().stream().map(this::changeKey).collect(Collectors.toList());
                answerListDTO.setAnswer(list);
            }
        }
        return resBody;
    }

    @SneakyThrows
    public SearchHasDataResBody searchHasData(BaseRequest<SearchHasDataReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        SearchHasDataReqBody reqContent = reqBody.getReqContent();
        SearchHasDataResBody resBody = new SearchHasDataResBody();

        BaseRequest<SearchDataReqBody> dataReq = new BaseRequest<>();
        dataReq.setBase(base);
        SearchDataReqBody dataReqBody = new SearchDataReqBody();
        dataReqBody.setSearchword(StringFilter(reqContent.getSearchword()));
        dataReq.setReqContent(dataReqBody);
//        SearchDataResDto dataResDto = searchData0(dataReq);
//        if (ObjectUtil.isNotEmpty(dataResDto.getAnswerList())) {
//            List<SearchDataResDto.AnswerListDTO> answerListDTOList = dataResDto.getAnswerList();
//            int quantity = 0;
//            for (SearchDataResDto.AnswerListDTO answerListDTO : answerListDTOList) {
//                if (IterUtil.isNotEmpty(answerListDTO.getAnswer())) {
//                    quantity++;
//                }
//            }
//            if (quantity > 0) {
//                resBody.setDataResult(true);
//            } else {
//                resBody.setDataResult(false);
//            }
//        } else {
//            resBody.setDataResult(false);
//        }
        resBody.setDataResult(false);
        boolean xwResult = infoReq0(base, reqContent, "xw");
        resBody.setNewsResult(xwResult);

        boolean ggResult = infoReq0(base, reqContent, "gg");
        resBody.setNoticeResult(ggResult);

        boolean gzResult = infoReq0(base, reqContent, "gz");
        resBody.setRuleResult(gzResult);

        boolean jgResult = infoReq0(base, reqContent, "jg");
        resBody.setSupervisionResult(jgResult);

        return resBody;
    }

    private Map<String, String> changeKey(Map<String, String> map) {
        LinkedHashMap<String, String> result = new LinkedHashMap(map.size());
        map.forEach((k, v) -> {
            String[] split = k.split("\\.");
            result.put(split[split.length - 1], v);
        });
        return result;
    }

    private boolean infoReq0(ReqBaseVO base, SearchHasDataReqBody reqContent, String type) {
        BaseRequest<SearchInfoReqBody> ggReq = new BaseRequest<>();
        ggReq.setBase(base);
        SearchInfoReqBody ggReqBody = new SearchInfoReqBody();
        ggReqBody.setPage("0");
        ggReqBody.setPageSize("1");
        ggReqBody.setSearchword(StringFilter(reqContent.getSearchword()));
        ggReqBody.setSearchType(type);
        ggReqBody.setKeywordPosition(reqContent.getKeywordPosition());
        ggReqBody.setOrderByKey(reqContent.getOrderByKey());
        ggReqBody.setBeginDate(reqContent.getBeginDate());
        ggReqBody.setEndDate(reqContent.getEndDate());
        ggReqBody.setKeywordPosition(reqContent.getKeywordPosition());
        ggReq.setReqContent(ggReqBody);
        SearchInfoDto ggResDto = searchInfo0(ggReq);
        return ggResDto.getData().getTotalSize() > 0;
    }

    private SearchInfoDto searchInfo0(BaseRequest<SearchInfoReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        SearchInfoReqBody reqContent = reqBody.getReqContent();

        String searchword = StringFilter(reqContent.getSearchword());
        String pageSize = reqContent.getPageSize();
        String pageNo = reqContent.getPage();
        String searchtarget = reqContent.getSearchtarget();
        String searchType = reqContent.getSearchType();
        String beginDate = reqContent.getBeginDate();
        String endDate = reqContent.getEndDate();
        String orderByKey = reqContent.getOrderByKey();
        String orderByDirection = reqContent.getOrderByDirection();
        String keywordPosition = reqContent.getKeywordPosition();
        SearchInfoReqDto reqDto = new SearchInfoReqDto();
        if (StrUtil.isNotBlank(searchword)) {
            reqDto.setKeyword(searchword);
        }
        if (StrUtil.isNotBlank(pageNo)) {
            reqDto.setPage(pageNo);
        }
        if (StrUtil.isNotBlank(pageSize)) {
            reqDto.setLimit(pageSize);
        }
        if (StrUtil.isNotBlank(searchtarget)) {
            reqDto.setKeywordPosition(searchtarget);
        }
        String channelcode = channelcodeTrans(searchType);
        reqDto.setChannelCode(channelcode);
        if (StrUtil.isNotBlank(beginDate)) {
            reqDto.setPublishTimeStart(beginDate + " 00:00:00");
        }
        if (StrUtil.isNotBlank(endDate)) {
            reqDto.setPublishTimeEnd(endDate + " 23:59:59");
        }
        if (StrUtil.isNotBlank(orderByKey)) {
            reqDto.setOrderByKey(orderByKey);
        }
        if (StrUtil.isNotBlank(orderByDirection)) {
            reqDto.setOrderByDirection(orderByDirection);
        }
        if (StrUtil.isNotBlank(keywordPosition)) {
            reqDto.setKeywordPosition(keywordPosition);
        }

        Map<String, Object> data = BeanUtil.beanToMap(reqDto);
        SearchInfoDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SEARCH_DOC, data, base, new TypeReference<SearchInfoDto>() {
        });
        if (!Objects.equals("0", result.getCode())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    private SearchDataResDto searchData0(BaseRequest<SearchDataReqBody> reqBody) throws JsonProcessingException {
        ReqBaseVO base = reqBody.getBase();
        SearchDataReqBody reqContent = reqBody.getReqContent();
        String searchword = StringFilter(reqContent.getSearchword());
        String searchtarget = reqContent.getSearchtarget();
        SearchDataReqDto reqDto = new SearchDataReqDto();
        reqDto.setQuestion(searchword);
        reqDto.setSearchName(searchtarget);
        Map<String, Object> data = BeanUtil.beanToMap(reqDto);
        SearchDataDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SEARCH_DATA, data, base, new TypeReference<SearchDataDto>() {
        });
        SearchDataResDto resData = new SearchDataResDto();
        if (ObjectUtil.isNotEmpty(result) && ObjectUtil.isNotEmpty(result.getData())) {
            resData = objectMapper.readValue(result.getData(), SearchDataResDto.class);
        }
        return resData;
    }

    private String channelcodeTrans(String searType) {

        switch (searType) {
            case "xw":
                return sysConfigFeign.getConfigKey("wx-channelcode");
            case "gz":
                return sysConfigFeign.getConfigKey("gz-channelcode");
            case "jg":
                return sysConfigFeign.getConfigKey("jg-channelcode");
            case "gg":
                return sysConfigFeign.getConfigKey("gg-channelcode");
            default:
                return sysConfigFeign.getConfigKey("default-channelcode");
        }
    }

    private SearchStockResBody.DataDTO searchStockTrans(SearchStockDto.DataDTO data) {
        SearchStockResBody.DataDTO dto = new SearchStockResBody.DataDTO();
        dto.setSEC_CODE(data.getSEC_CODE());
        dto.setSEC_TYPE(data.getSEC_TYPE());
        dto.setSEC_NAME_FULL(data.getSEC_NAME_FULL());
        dto.setSUB_TYPE(data.getSUB_TYPE());
        return dto;
    }


    @Data
    public static class SearchInfoReqDto {
        private String keyword = "";

        private String keywordPosition = "title,paper_content";

        private String page = "0";

        private String limit = "10";

        private String publishTimeStart;

        private String publishTimeEnd;

        private String spaceId = "3";

        private String siteName = "sse";

        private String channelCode;

        private String orderByDirection = "DESC";

        private String orderByKey = "create_time";

        private String trackId;

        private String channelId = "10001";
    }

    @Data
    public static class SearchDataReqDto {
        private String question;
        private String searchName = "dataSearchNew";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class SearchDataResDto {
        @JsonProperty("error_code")
        private String errorCode;
        @JsonProperty("error_message")
        private String errorMessage;
        @JsonProperty("answer_list")
        private List<AnswerListDTO> answerList;
        @JsonProperty("matchedList")
        private List<MatchedListDTO> matchedList;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public static class AnswerListDTO {
            @JsonProperty("token")
            private String token;
            @JsonProperty("total")
            private Integer total;
            @JsonProperty("company_col")
            private String companyCol;
            @JsonProperty("intention_name")
            private String intentionName;
            @JsonProperty("answer")
            private List<Map<String, String>> answer;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public static class MatchedListDTO {
            @JsonProperty("columnSort")
            private String columnSort;
            @JsonProperty("columnUrl")
            private String columnUrl;
            @JsonProperty("columnName")
            private String columnName;
        }
    }

    /**
     * 过滤特殊字符
     *
     * @param key 搜索关键字
     * @return 过滤后字段
     */
    private static String StringFilter(String key) {
//        String regEx = "/[\\[\\]\\,\\{\\}\\(\\)\\，\\\"\\\"\\、\\~\\`\\;\\—\\…\\/\\$\\@\\=\\>\\<\\!\\&\\*\\%\\^\\￥\\#\\!\\！\\+\\?\\'\\\\]|javascript|prompt|alert|vbscript/";
        String regEx = "[^a-zA-Z0-9\\u4E00-\\u9FA5]+|javascript|prompt|alert|vbscript";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(key);
        return m.replaceAll("").trim();
    }

    /**
     * 转换type
     *
     * @param dtoList 要转换的list
     * @return 过滤后字段
     */
    private static List<SearchStockResBody.DataDTO> switchType(List<SearchStockResBody.DataDTO> dtoList) {
        //为空直接返回
        if (ObjectUtil.isEmpty(dtoList)) {
            return dtoList;
        }
        for (SearchStockResBody.DataDTO dataDTO : dtoList) {
            switch (dataDTO.getSEC_TYPE()) {
                case "EU":
                    //如果是期权、基金
                    if (StrUtil.equals(dataDTO.getSUB_TYPE(), "EBS") || StrUtil.equals(dataDTO.getSUB_TYPE(), "LOF")) {
                        dataDTO.setSEC_TYPE("FUN");
                    }
                    break;
                case "ES":
                    //如果是主板A股、主板B股、科创版
                    if (StrUtil.equals(dataDTO.getSUB_TYPE(), "ASH") || StrUtil.equals(dataDTO.getSUB_TYPE(), "BSH") || StrUtil.equals(dataDTO.getSUB_TYPE(), "KSH")) {
                        dataDTO.setSEC_TYPE("EQU");
                    }
                    break;
                case "D":
                    //如果是债券回购
                    if (StrUtil.equals(dataDTO.getSUB_TYPE(), "GBF") || StrUtil.equals(dataDTO.getSUB_TYPE(), "CRP")) {
                        dataDTO.setSEC_TYPE("BON");
                    }
                    break;
                default:
                    break;
            }
        }
        return dtoList;
    }

}
