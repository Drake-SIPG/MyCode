package com.sse.sseapp.form.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class StockOptionDayResBody {

    private String totalVolume;
    private String totalQty;
    private List<StockOptionResBody.ResultDTO> data;

}
