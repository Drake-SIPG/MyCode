package com.sse.sseapp.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.StockOptionDayReqBody;
import com.sse.sseapp.form.response.StockOptionDayResBody;
import com.sse.sseapp.form.response.StockOptionResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_UP;


@Service
public class StockOptionService {

    @Autowired
    ProxyProvider proxyProvider;

    public StockOptionDayResBody getStockOptionDay(BaseRequest<StockOptionDayReqBody> body) {
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("jsonCallBack", "jsonpCallback27028");
        params.put("isPagination", true);
        params.put("sqlId", "COMMON_SSE_ZQPZ_YSP_QQ_SJTJ_MRTJ_CX");
        params.put("tradeDate", body.getReqContent().getTradeDate());
        params.put("pageHelp.pageSize", 1000);
        params.put("pageHelp.pageNo", 1);
        params.put("pageHelp.beginPage", 1);
        params.put("pageHelp.cacheSize", 1);
        params.put("pageHelp.endPage", 5);
        params.put("_", DateUtil.current());
        StockOptionResBody result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_QUERY_GET_STOCK_OPTION_DAY, params, null, new TypeReference<StockOptionResBody>() {
        });

        List<StockOptionResBody.ResultDTO> newResult = null;
        if (ObjectUtil.isNotEmpty(result) && ObjectUtil.isNotEmpty(result.getResult())
                && result.getResult().size() > 0) {
            newResult = result.getResult().stream().map(resultDTO -> {
                resultDTO.setTotalVolume(resultDTO.getTotalVolume().replace(",", ""));
                resultDTO.setCallVolume(resultDTO.getCallVolume().replace(",", ""));
                resultDTO.setLeavesQty(resultDTO.getLeavesQty().replace(",", ""));
                resultDTO.setPutVolume(resultDTO.getPutVolume().replace(",", ""));
                resultDTO.setLeavesCallQty(resultDTO.getLeavesCallQty().replace(",", ""));
                resultDTO.setLeavesPutQty(resultDTO.getLeavesPutQty().replace(",", ""));
                return resultDTO;
            }).collect(Collectors.toList());
        }
        StockOptionDayResBody resBody = new StockOptionDayResBody();
        if (ObjectUtil.isNotEmpty(newResult)) {
            BigDecimal totalVolume = null;
            BigDecimal totalQty = null;
            for (StockOptionResBody.ResultDTO resultDTO : result.getResult()) {
                if (ObjectUtil.isNotEmpty(totalVolume)) {
                    totalVolume = totalVolume.add(new BigDecimal(resultDTO.getTotalVolume().replace(",", "")));
                } else {
                    totalVolume = new BigDecimal(resultDTO.getTotalVolume().replace(",", ""));
                }
                if (ObjectUtil.isNotEmpty(totalQty)) {
                    totalQty = totalQty.add(new BigDecimal(resultDTO.getLeavesQty().replace(",", "")));
                } else {
                    totalQty = new BigDecimal(resultDTO.getLeavesQty().replace(",", ""));
                }
                resultDTO.setCallVolume(NumberUtil.decimalFormat("#0.00", new BigDecimal(resultDTO.getCallVolume()).divide(new BigDecimal(10000)), HALF_UP));
                resultDTO.setLeavesQty(NumberUtil.decimalFormat("#0.00", new BigDecimal(resultDTO.getLeavesQty()).divide(new BigDecimal(10000)), HALF_UP));
                resultDTO.setPutVolume(NumberUtil.decimalFormat("#0.00", new BigDecimal(resultDTO.getPutVolume()).divide(new BigDecimal(10000)), HALF_UP));
                resultDTO.setTotalVolume(NumberUtil.decimalFormat("#0.00", new BigDecimal(resultDTO.getTotalVolume()).divide(new BigDecimal(10000)), HALF_UP));
                resultDTO.setLeavesCallQty(NumberUtil.decimalFormat("#0.00", new BigDecimal(resultDTO.getLeavesCallQty()).divide(new BigDecimal(10000)), HALF_UP));
                resultDTO.setLeavesPutQty(NumberUtil.decimalFormat("#0.00", new BigDecimal(resultDTO.getLeavesPutQty()).divide(new BigDecimal(10000)), HALF_UP));
            }
            String totalVolumeStr = NumberUtil.decimalFormat("#0.00", new BigDecimal(totalVolume.toString()).divide(new BigDecimal(10000)), HALF_UP);
            String totalQtyStr = NumberUtil.decimalFormat("#0.00", new BigDecimal(totalQty.toString()).divide(new BigDecimal(10000)), HALF_UP);
            resBody.setTotalVolume(totalVolumeStr);
            resBody.setTotalQty(totalQtyStr);
            resBody.setData(newResult);
        }
        return resBody;
    }
}
