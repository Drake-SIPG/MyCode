package com.sse.sseapp.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.AddShareHolderCardReqBody;
import com.sse.sseapp.form.request.GetActiviteCode4AppReqBody;
import com.sse.sseapp.form.request.GetShareholderCard4AppReqBody;
import com.sse.sseapp.form.request.GetSpecialShareholderCardStatusReqBody;
import com.sse.sseapp.form.response.AddShareHolderCardResBody;
import com.sse.sseapp.form.response.GetActiviteCode4AppResBody;
import com.sse.sseapp.form.response.GetShareholderCard4AppResBody;
import com.sse.sseapp.form.response.GetSpecialShareholderCardStatusResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.sso2.SSO2Response;
import com.sse.sseapp.proxy.sso2.dto.AddShareHolderCardDto;
import com.sse.sseapp.proxy.sso2.dto.GetActiviteCode4AppDto;
import com.sse.sseapp.proxy.sso2.dto.GetShareholderCard4AppDto;
import com.sse.sseapp.proxy.sso2.dto.GetSpecialShareholderCardStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CardService {

    private static final String CLIENT_ID = "client_id";

    private static final String ACCESS_TOKEN = "access_token";

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    @Autowired
    CommonService commonService;

    public AddShareHolderCardResBody addShareHolderCard(BaseRequest<AddShareHolderCardReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        AddShareHolderCardReqBody reqContent = reqBody.getReqContent();
        ReqBaseVO base = reqBody.getBase();
        HashMap<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put(CLIENT_ID, this.sysConfigFeign.getConfigKey(AppConstants.CLIENT_ID));
        paramMap.put(ACCESS_TOKEN, base.getAccessToken());
        paramMap.put("mobilenum", reqContent.getMobile());
        paramMap.put("mobilecode", reqContent.getVcode());
        paramMap.put("cardnum", reqContent.getPartnerNum());
        paramMap.put("idcard", reqContent.getCardNum());
        paramMap.put("identityType", reqContent.getCardType());
        SSO2Response<AddShareHolderCardDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADD_SHAREHOLDER_CARD, paramMap, base, new TypeReference<SSO2Response<AddShareHolderCardDto>>() {
        });
        if (!Objects.equals("1", result.getStatus())) {
            if ("证件信息不匹配".equals(result.getMsg())) {
                throw new AppException("股东卡号与证件信息不匹配");
            } else {
                throw new AppException(result.getMsg());
            }
        }
        return new AddShareHolderCardResBody();
    }

    public GetActiviteCode4AppResBody getActiviteCode4App(BaseRequest<GetActiviteCode4AppReqBody> reqBody) {
        GetActiviteCode4AppReqBody reqContent = reqBody.getReqContent();
        ReqBaseVO base = reqBody.getBase();
        HashMap<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put(CLIENT_ID, this.sysConfigFeign.getConfigKey(AppConstants.CLIENT_ID));
        paramMap.put(ACCESS_TOKEN, base.getAccessToken());
        paramMap.put("cardnum", reqContent.getPartnerNum());
        SSO2Response<GetActiviteCode4AppDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_AC_TI_VITE_CODE4_APP, paramMap, base, new TypeReference<SSO2Response<GetActiviteCode4AppDto>>() {
        });
        if (!Objects.equals("1", result.getStatus())) {
            throw new AppException(result.getMsg());
        }
        GetActiviteCode4AppDto info = result.getInfo();
        GetActiviteCode4AppResBody resBody = new GetActiviteCode4AppResBody();
        resBody.setData(info.getData());
        resBody.setIsValid(info.getIsValid());
        String time = DateUtil.format(new Date(NumberUtil.parseLong(info.getValidTimeStamp())), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        resBody.setValidTimeStamp(time);
        return resBody;
    }

    public GetShareholderCard4AppResBody getShareholderCard4App(BaseRequest<GetShareholderCard4AppReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        HashMap<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put(CLIENT_ID, this.sysConfigFeign.getConfigKey(AppConstants.CLIENT_ID));
        paramMap.put(ACCESS_TOKEN, base.getAccessToken());
        SSO2Response<GetShareholderCard4AppDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_SHAREHOLDER_CARD4_APP, paramMap, base, new TypeReference<SSO2Response<GetShareholderCard4AppDto>>() {
        });
        if (!Objects.equals("1", result.getStatus())) {
            throw new AppException(result.getMsg());
        }
        GetShareholderCard4AppDto info = result.getInfo();
        List<GetShareholderCard4AppDto.HolderCardDTO> cards = ObjectUtil.defaultIfNull(info.getShareholdercards(), Lists.newArrayList());
        List<GetShareholderCard4AppResBody.CardDTO> data = cards.stream().map(this::turnHolderCardDTO2CardDTO).collect(Collectors.toList());
        GetShareholderCard4AppResBody resBody = new GetShareholderCard4AppResBody();
        resBody.setData(data);
        return resBody;
    }

    public GetSpecialShareholderCardStatusResBody getSpecialShareholderCardStatus(BaseRequest<GetSpecialShareholderCardStatusReqBody> reqBody) {
        GetSpecialShareholderCardStatusReqBody reqContent = reqBody.getReqContent();
        ReqBaseVO base = reqBody.getBase();
        HashMap<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put(CLIENT_ID, this.sysConfigFeign.getConfigKey(AppConstants.CLIENT_ID));
        paramMap.put(ACCESS_TOKEN, base.getAccessToken());
        SSO2Response<GetSpecialShareholderCardStatusDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_SPECIAL_SHAREHOLDER_CARD_STATUS, paramMap, base, new TypeReference<SSO2Response<GetSpecialShareholderCardStatusDto>>() {
        });
        if (!Objects.equals("1", result.getStatus())) {
            throw new AppException(result.getMsg());
        }
        GetSpecialShareholderCardStatusDto info = result.getInfo();
        List<GetShareholderCard4AppDto.HolderCardDTO> cards = ObjectUtil.defaultIfNull(info.getShareholdercards(), Lists.newArrayList());
        GetSpecialShareholderCardStatusResBody resBody = new GetSpecialShareholderCardStatusResBody();
        for (GetShareholderCard4AppDto.HolderCardDTO card : cards) {
            if (Objects.equals(reqContent.getPartnerNum(), card.getCardnum())) {
                resBody.setData(card.getActivestatus());
            }
        }
        return resBody;
    }

    private GetShareholderCard4AppResBody.CardDTO turnHolderCardDTO2CardDTO(GetShareholderCard4AppDto.HolderCardDTO holderCardDTO) {
        GetShareholderCard4AppResBody.CardDTO cardDTO = new GetShareholderCard4AppResBody.CardDTO();
        cardDTO.setPartnerNum(holderCardDTO.getCardnum());
        cardDTO.setActive(holderCardDTO.getActivestatus());
        cardDTO.setCardtype(holderCardDTO.getCardtype());
        return cardDTO;
    }
}
