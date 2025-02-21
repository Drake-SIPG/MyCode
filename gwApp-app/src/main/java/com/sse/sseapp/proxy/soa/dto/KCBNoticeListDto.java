package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author mateng
 * @since 2023/7/19 14:07
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class KCBNoticeListDto extends RespContentVO {


    /**
     * extTeacher : null
     * cmsOpDate : 2023-03-10 19:40:01
     * extZQDM : 688599
     * docTitle : 关于天合光能股份有限公司可转换公司债券上市交易的公告
     * extNAME : null
     * docKeyword : null
     * extbakValue1 : 2023-03-10 19:45:00
     * extpicUrl : null
     * extmultiple : null
     * extGGLX : null
     * extXGGG : null
     * extbakValue3 : null
     * extGGDL : null
     * extbakValue2 : null
     * extSSE_LHJH_XL_NAME : null
     * extSSE_YJCB_SFJB : null
     * stockcode : 688599
     * extWTFL : null
     * extZQGS : null
     * extSSE_YJCB_SZJB_VALUE : null
     * extGGNF : null
     * docSize : 0
     * extFXSYL : null
     * extWENTI : null
     * abstractSummary : null
     * extFXJG : null
     * extvideoUploadLinkUrl : null
     * extLINK : null
     * extGJZ : null
     * extSSE_YJCB_XL_NAME : null
     * docId : 5717724_28_11794
     * extTIME : null
     * extGPDM : null
     * extStarttest_link : null
     * extuploadVideoUrl : null
     * extWENHAO : 上证公告（可转债上市）【2023】014号
     * extFXSL : null
     * extDateShow : null
     * cmsDocType : 0
     * extimageUrl : null
     * extE_KEYWORDS : null
     * extGSJC : 天合光能
     * channelId : 11794
     * extDWDM : null
     * parentChannelId : 8320
     * extLANMU : null
     * extCBRQ : null
     * extSSRQ : null
     * docType : shtml
     * extuploadFileUrl : null
     * extYUYAN : null
     * extSECURITY_CODE : null
     * extsummaryImageUrl : null
     * extuploadPDFUrl : null
     * extyesOrNo : null
     * extFWJG : null
     * extVITALITEM : null
     * extINTRODUCTION : #@ensp;#@ensp;#@ensp;#@ensp;根据《上海证券交易所科创板股票上市规则》相关规定，天合光能股份有限公司发行的88.64751亿元可转换公司债券将于2023年03月15日起在本所市场上市交易，证券代码为“118031”，证券简称为“天23转债”。<br/><br/>#@ensp;#@ensp;#@ensp;#@ensp;上海证券交易所<br/><br/>#@ensp;#@ensp;#@ensp;#@ensp;二〇二三年三月十一日
     * extSSE_YEAR_MONTH_VALUE : null
     * extbPicUrl : null
     * createTime : 2023-03-10 19:35:31
     * extclassRed : null
     * extLAIYUAN : null
     * docURL : www108.sse.com.cn/disclosure/announcement/listing/stock/c/c_20230310_88623558.shtml
     * siteId : 28
     * extuploadPPTUrl : null
     * extTYPE : null
     * extE_TITLE : null
     */

    private String extTeacher;
    private String cmsOpDate;
    private String extZQDM;
    private String docTitle;
    private String extNAME;
    private String docKeyword;
    private String extbakValue1;
    private String extpicUrl;
    private String extmultiple;
    private String extGGLX;
    private String extXGGG;
    private String extbakValue3;
    private String extGGDL;
    private String extbakValue2;
    private String extSSE_LHJH_XL_NAME;
    private String extSSE_YJCB_SFJB;
    private String stockcode;
    private String extWTFL;
    private String extZQGS;
    private String extSSE_YJCB_SZJB_VALUE;
    private String extGGNF;
    private String docSize;
    private String extFXSYL;
    private String extWENTI;
    private String abstractSummary;
    private String extFXJG;
    private String extvideoUploadLinkUrl;
    private String extLINK;
    private String extGJZ;
    private String extSSE_YJCB_XL_NAME;
    private String docId;
    private String extTIME;
    private String extGPDM;
    private String extStarttest_link;
    private String extuploadVideoUrl;
    private String extWENHAO;
    private String extFXSL;
    private String extDateShow;
    private String cmsDocType;
    private String extimageUrl;
    private String extE_KEYWORDS;
    private String extGSJC;
    private String channelId;
    private String extDWDM;
    private String parentChannelId;
    private String extLANMU;
    private String extCBRQ;
    private String extSSRQ;
    private String docType;
    private String extuploadFileUrl;
    private String extYUYAN;
    private String extSECURITY_CODE;
    private String extsummaryImageUrl;
    private String extuploadPDFUrl;
    private String extyesOrNo;
    private String extFWJG;
    private String extVITALITEM;
    private String extINTRODUCTION;
    private String extSSE_YEAR_MONTH_VALUE;
    private String extbPicUrl;
    private String createTime;
    private String extclassRed;
    private String extLAIYUAN;
    private String docURL;
    private String siteId;
    private String extuploadPPTUrl;
    private String extTYPE;
    private String extE_TITLE;
    private Integer readCount;

    @JsonGetter("title")
    public String getDocTitle() {
        return docTitle;
    }

    @JsonSetter("docTitle")
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    @JsonGetter("size")
    public String getDocSize() {
        return docSize;
    }

    @JsonSetter("docSize")
    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }

    @JsonGetter("DOCID")
    public String getDocId() {
        return docId;
    }

    @JsonSetter("docId")
    public void setDocId(String docId) {
        this.docId = docId;
    }

    @JsonGetter("type")
    public String getDocType() {
        return docType;
    }
    @JsonSetter("docType")
    public void setDocType(String docType) {
        this.docType = docType;
    }

    @JsonGetter("url")
    public String getDocURL() {
        return docURL;
    }

    @JsonSetter("docURL")
    public void setDocURL(String docURL) {
        this.docURL = docURL;
    }

    @JsonGetter("date")
    public String getCreateTime() {
        return createTime;
    }

    @JsonSetter("createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
