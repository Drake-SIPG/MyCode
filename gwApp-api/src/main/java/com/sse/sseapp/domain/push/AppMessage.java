package com.sse.sseapp.domain.push;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sse.sseapp.dto.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息推送MQ消费记录表
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppMessage extends BaseEntity<AppMessage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 消息发送id
     */
    @TableField("msg_id")
    private String msgId;

    /**
     * 状态（1：已接收，2：已发送，3：已统计，4：发送失败）
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 类型（1点位，2公告）
     */
    @TableField("type")
    private String type;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 用于确认发送对象类型。如无法提供请予以说明发送对象处理逻辑
     */
    @TableField("sendType")
    private Integer sendType;

    /**
     * 消息发送时间
     */
    @TableField("sendTime")
    private Date sendTime;

    /**
     * 唯一消息id
     */
    @TableField("uuid")
    private String uuid;

    /**
     * 交易日期 格式yyyy/MM/dd
     */
    @TableField("point_trade_date")
    private String pointTradeDate;

    /**
     * 交易时间 格式HH:mm:ss
     */
    @TableField("point_trade_time")
    private String pointTradeTime;

    /**
     * 当前价格
     */
    @TableField("point_current_price")
    private String pointCurrentPrice;

    /**
     * 振幅
     */
    @TableField("point_amplitude")
    private String pointAmplitude;

    /**
     * 股票代码
     */
    @TableField("point_stock_code")
    private String pointStockCode;

    /**
     * 股票简称
     */
    @TableField("point_stock_name")
    private String pointStockName;

    /**
     * 涨跌额
     */
    @TableField("point_up_and_down")
    private String pointUpAndDown;

    /**
     * 文档id
     */
    @TableField("announcement_CDOCCODE")
    private String announcementCdoccode;

    /**
     * 文档标题
     */
    @TableField("announcement_CTITLE")
    private String announcementCtitle;

    /**
     * 摘要
     */
    @TableField("announcement_CSUMMARY")
    private String announcementCsummary;

    /**
     * 关键字
     */
    @TableField("announcement_KEYWORD")
    private String announcementKeyword;

    /**
     * 文章时间
     */
    @TableField("announcement_CRELEASETIME")
    private String announcementCreleasetime;

    /**
     * 公司名称
     */
    @TableField("announcement_GSJC")
    private String announcementGsjc;

    /**
     * 证券代码
     */
    @TableField("announcement_ZQDM")
    private String announcementZqdm;

    /**
     * 服务器相对路径
     */
    @TableField("announcement_CURL")
    private String announcementCurl;

    /**
     * 栏目编号
     */
    @TableField("announcement_CSITECODE")
    private String announcementCsitecode;

    /**
     * 是否重大消息，1是 0否
     */
    @TableField("announcement_ISIMPORTANT")
    private String announcementIsimportant;

    /**
     * 文件类型, 如pdf, doc, docx
     */
    @TableField("announcement_FILETYPE")
    private String announcementFiletype;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String beginTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;


    @Override
    public Serializable pkVal() {
        return null;
    }

}
