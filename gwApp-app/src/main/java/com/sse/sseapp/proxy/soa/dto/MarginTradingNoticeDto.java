package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author mateng
 * @since 2023/7/13 15:30
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MarginTradingNoticeDto extends RespContentVO {


    /**
     * date : 2023-05-05 16:27:40
     * extTeacher : null
     * cmsOpDate : 2023-06-12 10:39:11
     * extZQDM : 688309
     * type : shtml
     * extNAME : null
     * docKeyword : 融资融券标的证券调整
     * extbakValue1 : null
     * extpicUrl : null
     * extmultiple : null
     * extGGLX : null
     * extXGGG : null
     * extbakValue3 : null
     * extGGDL : 上证所相关公告
     * extbakValue2 : null
     * extSSE_LHJH_XL_NAME : null
     * saveTime : 2023-06-12 10:42:00
     * extSSE_YJCB_SFJB : null
     * stockcode : 688309
     * extWTFL : null
     * extZQGS : null
     * extSSE_YJCB_SZJB_VALUE : null
     * extGGNF : 2023
     * extFXSYL : null
     * extWENTI : null
     * abstractSummary : null
     * extFXJG : null
     * size : 2581
     * extvideoUploadLinkUrl : null
     * extLINK : null
     * extGJZ : null
     * extSSE_YJCB_XL_NAME : null
     * extTIME : null
     * extGPDM : null
     * title : 关于融资融券标的证券调整的公告
     * extStarttest_link : null
     * extuploadVideoUrl : null
     * DOCID : 5720968_28_8432
     * extWENHAO : null
     * extFXSL : null
     * extDateShow : null
     * cmsDocType : 0
     * extimageUrl : null
     * extE_KEYWORDS : null
     * extGSJC : null
     * channelId : 8432
     * extDWDM : null
     * parentChannelId : 8431
     * extLANMU : null
     * extCBRQ : null
     * extSSRQ : null
     * extuploadFileUrl : null
     * extYUYAN : null
     * extSECURITY_CODE : null
     * extsummaryImageUrl : null
     * extuploadPDFUrl : null
     * url : www.sse.com.cn/disclosure/magin/announcement/ssereport/c/c_20230505_5720968.shtml
     * extyesOrNo : null
     * extFWJG : null
     * extVITALITEM : null
     * extINTRODUCTION : null
     * extSSE_YEAR_MONTH_VALUE : null
     * extbPicUrl : null
     * extclassRed : null
     * extLAIYUAN : null
     * siteId : 28
     * extuploadPPTUrl : null
     * extTYPE : null
     * extE_TITLE : null
     */

    private String createTime;
    private Object extTeacher;
    private String cmsOpDate;
    private String extZQDM;
    private String docType;
    private Object extNAME;
    private String docKeyword;
    private Object extbakValue1;
    private Object extpicUrl;
    private Object extmultiple;
    private Object extGGLX;
    private Object extXGGG;
    private Object extbakValue3;
    private String extGGDL;
    private Object extbakValue2;
    private Object extSSE_LHJH_XL_NAME;
    private String saveTime;
    private Object extSSE_YJCB_SFJB;
    private String stockcode;
    private Object extWTFL;
    private Object extZQGS;
    private Object extSSE_YJCB_SZJB_VALUE;
    private String extGGNF;
    private Object extFXSYL;
    private Object extWENTI;
    private Object abstractSummary;
    private Object extFXJG;
    private String docSize;
    private Object extvideoUploadLinkUrl;
    private Object extLINK;
    private Object extGJZ;
    private Object extSSE_YJCB_XL_NAME;
    private Object extTIME;
    private Object extGPDM;
    private String docTitle;
    private Object extStarttest_link;
    private Object extuploadVideoUrl;
    private String docId;
    private Object extWENHAO;
    private Object extFXSL;
    private Object extDateShow;
    private String cmsDocType;
    private Object extimageUrl;
    private Object extE_KEYWORDS;
    private Object extGSJC;
    private String channelId;
    private Object extDWDM;
    private String parentChannelId;
    private Object extLANMU;
    private Object extCBRQ;
    private Object extSSRQ;
    private Object extuploadFileUrl;
    private Object extYUYAN;
    private Object extSECURITY_CODE;
    private Object extsummaryImageUrl;
    private Object extuploadPDFUrl;
    private String docURL;
    private Object extyesOrNo;
    private Object extFWJG;
    private Object extVITALITEM;
    private Object extINTRODUCTION;
    private Object extSSE_YEAR_MONTH_VALUE;
    private Object extbPicUrl;
    private Object extclassRed;
    private Object extLAIYUAN;
    private String siteId;
    private Object extuploadPPTUrl;
    private Object extTYPE;
    private Object extE_TITLE;

    @JsonGetter("date")
    public String getCreateTime() {
        return createTime;
    }
    @JsonSetter("createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    @JsonGetter("type")
    public String getDocType() {
        return docType;
    }
    @JsonSetter("docType")
    public void setDocType(String docType) {
        this.docType = docType;
    }
    @JsonGetter("size")
    public String getDocSize() {
        return docSize;
    }
    @JsonSetter("docSize")
    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }
    @JsonGetter("title")
    public String getDocTitle() {
        return docTitle;
    }
    @JsonSetter("docTitle")
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }
    @JsonGetter("DOCID")
    public String getDocId() {
        return docId;
    }
    @JsonSetter("docId")
    public void setDocId(String docId) {
        this.docId = docId;
    }
    @JsonGetter("url")
    public String getDocURL() {
        return docURL;
    }
    @JsonSetter("docURL")
    public void setDocURL(String docURL) {
        this.docURL = docURL;
    }
}
