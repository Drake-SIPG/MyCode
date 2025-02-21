package com.sse.sseapp.app.core.constant;

/**
 * 产品类型与子类型的常量
 *
 * @author wy
 * @date 2023-07-11
 */
public class SoaProductConstants {

    /**
     * 债券
     */
    public static final String STOCK_TYPE_BON = "BON";

    /**
     * 浮动利率国债
     */
    public static final String PRODUCT_SUB_TYPE_BON_GBV = "GBV";

    /**
     * 固定利率国债
     */
    public static final String PRODUCT_SUB_TYPE_BON_GBF = "GBF";

    /**
     * 无息国债
     */
    public static final String PRODUCT_SUB_TYPE_BON_GBZ = "GBZ";

    /**
     * 国债分销（仅用于分销阶段）
     */
    public static final String PRODUCT_SUB_TYPE_BON_DST = "DST";

    /**
     * 公司债分销
     */
    public static final String PRODUCT_SUB_TYPE_BON_DVP = "DVP";

    /**
     * 浮动利率企业债券（不包括可转换企业债券）
     */
    public static final String PRODUCT_SUB_TYPE_BON_CBV = "CBV";

    /**
     * 固定利率企业债券（不包括可转换企业债券）
     */
    public static final String PRODUCT_SUB_TYPE_BON_CBF = "CBF";

    /**
     * 浮动利率可转换企业债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_CCV = "CCV";

    /**
     * 固定利率可转换企业债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_CCF = "CCF";

    /**
     * 浮动利率债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_CPV = "CPV";

    /**
     * 固定利率公司债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_CPF = "CPF";

    /**
     * 浮动利率金融机构（即银行、保险公司、债券公司）发行债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_FBV = "FBV";

    /**
     * 固定利率金融机构（即银行、保险公司、债券公司）发行债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_FBF = "FBF";

    /**
     * 质押式国债回购
     */
    public static final String PRODUCT_SUB_TYPE_BON_CRP = "CRP";

    /**
     * 质押式企债回购
     */
    public static final String PRODUCT_SUB_TYPE_BON_BRP = "BRP";

    /**
     * 买断式债券回购
     */
    public static final String PRODUCT_SUB_TYPE_BON_ORP = "ORP";

    /**
     * 分离式可转债
     */
    public static final String PRODUCT_SUB_TYPE_BON_CBD = "CBD";

    /**
     * 债券借贷
     */
    public static final String PRODUCT_SUB_TYPE_BON_BBL = "BBL";

    /**
     * 集合资产管理计划
     */
    public static final String PRODUCT_SUB_TYPE_BON_AMP = "AMP";

    /**
     * 国债预发行
     */
    public static final String PRODUCT_SUB_TYPE_BON_WIT = "WIT";

    /**
     * 其它债券
     */
    public static final String PRODUCT_SUB_TYPE_BON_OBD = "OBD";

    /**
     * 基金
     */
    public static final String STOCK_TYPE_FUN = "FUN";

    /**
     * 封闭式基金
     */
    public static final String PRODUCT_SUB_TYPE_FUN_CEF = "CEF";

    /**
     * 开放式基金
     */
    public static final String PRODUCT_SUB_TYPE_FUN_OEF = "OEF";

    /**
     * 交易所交易基金（买卖）
     */
    public static final String PRODUCT_SUB_TYPE_FUN_EBS = "EBS";

    /**
     * 基金借贷
     */
    public static final String PRODUCT_SUB_TYPE_FUN_FBL = "FBL";

    /**
     * LOF基金
     */
    public static final String PRODUCT_SUB_TYPE_FUN_LOF = "LOF";

    /**
     * 其它基金
     */
    public static final String PRODUCT_SUB_TYPE_FUN_OFN = "OFN";

    /**
     * 股票
     */
    public static final String STOCK_TYPE_EQU = "EQU";

    /**
     * 以人民币交易的股票
     */
    public static final String PRODUCT_SUB_TYPE_EQU_ASH = "ASH";

    /**
     * 以美元交易的股票
     */
    public static final String PRODUCT_SUB_TYPE_EQU_BSH = "BSH";

    /**
     * 国际板股票
     */
    public static final String PRODUCT_SUB_TYPE_EQU_CSH = "CSH";

    /**
     * 股票借贷
     */
    public static final String PRODUCT_SUB_TYPE_EQU_EBL = "EBL";

    /**
     * 公开发行优先股
     */
    public static final String PRODUCT_SUB_TYPE_EQU_OPS = "OPS";

    /**
     * 非公开发行优先股
     */
    public static final String PRODUCT_SUB_TYPE_EQU_PPS = "PPS";

    /**
     * 其它股票
     */
    public static final String PRODUCT_SUB_TYPE_EQU_OEQ = "OEQ";

    /**
     * 其它股票，当 External Indicator字段为‘E’代表指数
     */
    public static final String PRODUCT_SUB_TYPE_EQU_OEQI = "OEQI";

    /**
     * 权证
     */
    public static final String STOCK_TYPE_WAR = "WAR";

    /**
     * 企业发行权证
     */
    public static final String PRODUCT_SUB_TYPE_WAR_CIW = "CIW";

    /**
     * 备兑权证
     */
    public static final String PRODUCT_SUB_TYPE_WAR_COV = "COV";

    /**
     * 凭证式权证
     */
    public static final String PRODUCT_SUB_TYPE_WAR_CER = "CER";

    /**
     * 其它权证
     */
    public static final String PRODUCT_SUB_TYPE_WAR_OWR = "OWR";

    /**
     * 期货
     */
    public static final String STOCK_TYPE_FUT = "FUT";

    /**
     * 指数期货
     */
    public static final String PRODUCT_SUB_TYPE_FUT_FIX = "FIX";

    /**
     * 个股期货
     */
    public static final String PRODUCT_SUB_TYPE_FUT_FEQ = "FEQ";

    /**
     * 债券期货
     */
    public static final String PRODUCT_SUB_TYPE_FUT_FBD = "FBD";

    /**
     * 其它期货
     */
    public static final String PRODUCT_SUB_TYPE_FUT_OFT = "OFT";
}
