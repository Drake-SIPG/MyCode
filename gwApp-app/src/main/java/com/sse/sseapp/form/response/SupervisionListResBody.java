package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author mateng
 * @since 2023/7/20 15:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class SupervisionListResBody extends RespContentVO {

    /**
     * extTeacher : 一般股东
     * cmsOpDate : 2023-07-19 15:48:49
     * extZQDM : null
     * docTitle : 公司监管-通报批评-回归测试04
     * extNAME : null
     * docKeyword : null
     * extbakValue1 : null
     * extpicUrl : null
     * extmultiple : null
     * extGGLX : null
     * extXGGG : null
     * extbakValue3 : null
     * extGGDL : null
     * extbakValue2 : null
     * extSSE_LHJH_XL_NAME : null
     * saveTime : 2023-07-19 15:52:02
     * extSSE_YJCB_SFJB : null
     * stockcode : 688000
     * extWTFL : 监管关注
     * extZQGS : null
     * extSSE_YJCB_SZJB_VALUE : null
     * extGGNF : null
     * docSize : 235605
     * extFXSYL : null
     * extWENTI : null
     * abstractSummary : null
     * extFXJG : null
     * extvideoUploadLinkUrl : null
     * extLINK : null
     * extGJZ : null
     * extSSE_YJCB_XL_NAME : null
     * docId : 4335684_28_9845
     * extTIME : null
     * extGPDM : null
     * extStarttest_link : null
     * extuploadVideoUrl : null
     * extWENHAO : null
     * extFXSL : null
     * extDateShow : null
     * cmsDocType : 1
     * extimageUrl : null
     * extE_KEYWORDS : null
     * extGSJC : 虹软科技
     * channelId : 9845
     * extDWDM : null
     * parentChannelId : 9839
     * extLANMU : null
     * extCBRQ : null
     * extSSRQ : null
     * docType : pdf
     * extuploadFileUrl : null
     * extYUYAN : null
     * extSECURITY_CODE : 688000
     * extsummaryImageUrl : null
     * extuploadPDFUrl : null
     * extyesOrNo : null
     * extFWJG : null
     * extVITALITEM : null
     * extINTRODUCTION : null
     * extSSE_YEAR_MONTH_VALUE : null
     * extbPicUrl : null
     * createTime : 2023-07-19 17:12:55
     * extclassRed : null
     * extLAIYUAN : null
     * docURL : www108.sse.com.cn/disclosure/credibility/supervision/measures/criticism/c/4828545282307030100001.pdf
     * siteId : 28
     * extuploadPPTUrl : null
     * extTYPE : 监管关注
     * extE_TITLE : null
     */

    private String extTeacher;
    private String cmsOpDate;
    private Object extZQDM;
    private String docTitle;
    private Object extNAME;
    private Object docKeyword;
    private Object extbakValue1;
    private Object extpicUrl;
    private Object extmultiple;
    private Object extGGLX;
    private Object extXGGG;
    private Object extbakValue3;
    private Object extGGDL;
    private Object extbakValue2;
    private Object extSSE_LHJH_XL_NAME;
    private String saveTime;
    private Object extSSE_YJCB_SFJB;
    private String stockcode;
    private String extWTFL;
    private Object extZQGS;
    private Object extSSE_YJCB_SZJB_VALUE;
    private Object extGGNF;
    private String docSize;
    private Object extFXSYL;
    private Object extWENTI;
    private Object abstractSummary;
    private Object extFXJG;
    private Object extvideoUploadLinkUrl;
    private Object extLINK;
    private Object extGJZ;
    private Object extSSE_YJCB_XL_NAME;
    private String docId;
    private Object extTIME;
    private Object extGPDM;
    private Object extStarttest_link;
    private Object extuploadVideoUrl;
    private Object extWENHAO;
    private Object extFXSL;
    private Object extDateShow;
    private String cmsDocType;
    private Object extimageUrl;
    private Object extE_KEYWORDS;
    private String extGSJC;
    private String channelId;
    private Object extDWDM;
    private String parentChannelId;
    private Object extLANMU;
    private Object extCBRQ;
    private Object extSSRQ;
    private String docType;
    private Object extuploadFileUrl;
    private Object extYUYAN;
    private String extSECURITY_CODE;
    private Object extsummaryImageUrl;
    private Object extuploadPDFUrl;
    private Object extyesOrNo;
    private Object extFWJG;
    private Object extVITALITEM;
    private Object extINTRODUCTION;
    private Object extSSE_YEAR_MONTH_VALUE;
    private Object extbPicUrl;
    private String createTime;
    private Object extclassRed;
    private Object extLAIYUAN;
    private String docURL;
    private String siteId;
    private Object extuploadPPTUrl;
    private String extTYPE;
    private Object extE_TITLE;

    @JsonGetter("date")
    public String getCreateTime() {
        return createTime;
    }

    @JsonSetter("createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
}
