package com.sse.sseapp.app.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ly on 2015/9/11.
 */
public class StockNumber {

    public Map<String, Integer> productSubTypeMap = new HashMap<String, Integer>();
    public Map<String, Integer> productTypeMap = new HashMap<String, Integer>();

    public StockNumber() {
        setStockNum();
        setProductTypeMap();
    }

    public void setStockNum() {
        productSubTypeMap.put("GBV", 0);
        productSubTypeMap.put("GBF", 1);
        productSubTypeMap.put("GBZ", 2);
        productSubTypeMap.put("DST", 3);
        productSubTypeMap.put("DVP", 4);
        productSubTypeMap.put("CBV", 5);
        productSubTypeMap.put("CBF", 6);
        productSubTypeMap.put("CCV", 7);
        productSubTypeMap.put("CCF", 8);
        productSubTypeMap.put("CPV", 9);
        productSubTypeMap.put("CPF", 10);
        productSubTypeMap.put("FBV", 11);
        productSubTypeMap.put("FBF", 12);
        productSubTypeMap.put("CRP", 13);
        productSubTypeMap.put("BRP", 14);
        productSubTypeMap.put("ORP", 15);
        productSubTypeMap.put("CBD", 16);
        productSubTypeMap.put("BBL", 17);
        productSubTypeMap.put("AMP", 18);
        productSubTypeMap.put("WIT", 20);
        productSubTypeMap.put("OBD", 21);
        productSubTypeMap.put("CEF", 22);
        productSubTypeMap.put("OEF", 23);
        productSubTypeMap.put("EBS", 24);
        productSubTypeMap.put("FBL", 25);
        productSubTypeMap.put("LOF", 26);
        productSubTypeMap.put("OFN", 27);
        productSubTypeMap.put("ASH", 28);
        productSubTypeMap.put("BSH", 29);
        productSubTypeMap.put("CSH", 30);
        productSubTypeMap.put("EBL", 31);
        productSubTypeMap.put("OPS", 32);
        productSubTypeMap.put("PPS", 33);
        productSubTypeMap.put("OEQ", 34);
        productSubTypeMap.put("OEQI", 35);
        productSubTypeMap.put("CIW", 36);
        productSubTypeMap.put("COV", 37);
        productSubTypeMap.put("CER", 38);
        productSubTypeMap.put("OWR", 39);
        productSubTypeMap.put("FIX", 40);
        productSubTypeMap.put("FEQ", 41);
        productSubTypeMap.put("FBD", 42);
        productSubTypeMap.put("OFT", 43);
        productSubTypeMap.put("KSH", 44);
        productSubTypeMap.put("RET", 45);
        productSubTypeMap.put("TCB", 46);
    }

    public void setProductTypeMap() {
        productTypeMap.put("BON", 0);
        productTypeMap.put("FUN", 1);
        productTypeMap.put("FUND", 11);
        productTypeMap.put("EQU", 2);
        productTypeMap.put("EQUITY", 21);
        productTypeMap.put("WAR", 3);
        productTypeMap.put("FUT", 4);
        //add
        productTypeMap.put("BUYBACK", 5);
    }

    public Map<String, Integer> getProductSubTypeMap() {
        return productSubTypeMap;
    }

    public Map<String, Integer> getProductTypeMap() {
        return productTypeMap;
    }
}
