package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetDerivativeNoticeListResBody extends RespContentVO {

    /**
     * extTeacher : null
     * cmsOpDate : 2023-06-20 11:23:36
     * extZQDM : null
     * docTitle : 披露-关于双十一回归测试人民币普通股股票科创板上市交易的公告-2023年6月20日
     * extNAME : null
     * docKeyword : 披露
     * extbakValue1 : null
     * extpicUrl : null
     * extmultiple : null
     * extGGLX : null
     * extXGGG : null
     * extbakValue3 : null
     * extGGDL : null
     * extbakValue2 : null
     * extSSE_LHJH_XL_NAME : null
     * saveTime : 2023-06-20 11:27:01
     * extSSE_YJCB_SFJB : null
     * stockcode : null
     * extWTFL : null
     * extZQGS : null
     * extSSE_YJCB_SZJB_VALUE : null
     * extGGNF : null
     * docSize : 3052
     * extFXSYL : null
     * extWENTI : null
     * abstractSummary : null
     * extFXJG : null
     * extvideoUploadLinkUrl : null
     * extLINK : null
     * extGJZ : null
     * extSSE_YJCB_XL_NAME : null
     * docId : 4333821_28_12716
     * extTIME : null
     * extGPDM : null
     * extStarttest_link : null
     * extuploadVideoUrl : null
     * extWENHAO : null
     * extFXSL : null
     * extDateShow : null
     * cmsDocType : 0
     * extimageUrl : null
     * extE_KEYWORDS : null
     * extGSJC : null
     * channelId : 12716
     * extDWDM : null
     * parentChannelId : 12715
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
     * extINTRODUCTION : null
     * extSSE_YEAR_MONTH_VALUE : null
     * extbPicUrl : null
     * createTime : 2023-06-22 11:23:21
     * extclassRed : null
     * extLAIYUAN : null
     * docURL : www108.sse.com.cn/disclosure/optioninfo/update/c/c_20230620_4333821.shtml
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
    private String saveTime;
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
