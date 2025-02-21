package com.sse.sseapp.domain.push;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
@Data
public class AppPushMessage {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 消息类型
     * 1:通知栏消息
     * 2 :APP内部消息
     */
    @TableField("msg_type")
    @NotBlank(message = "消息类型不能为空")
    private String msgType;

    /**
     * 消息标题
     */
    @TableField("title")
    @NotBlank(message = "消息标题不能为空")
    @Size(max = 40, message = "消息标题不能超过40个字符")
    private String title;

    /**
     * 消息内容
     */
    @TableField("content")
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 110, message = "消息内容不能超过110个字符")
    private String content;

    /**
     * 推送点击类型
     * 1: 打开APP内部功能页面
     * 2: 打开指定H5
     * 3: 打开指定小程序
     * 4: 跳转指定菜单
     * 5：项目订阅推送
     */
    @TableField("click_type")
    @NotBlank(message = "推送点击类型不能为空")
    private String clickType;

    /**
     * 点击链接
     * clickType=1   传固定参数
     * clickType=2   传需打开url地址
     * clickType=3   传小程序id
     * clickType=4   跳转菜单下标,从0开始
     */
    @TableField("click_url")
    private String clickUrl;

    /**
     * 是否独立模式
     * clickType=3   是否以独立模式打开小程序
     * 默认true
     */
    @TableField("standalone_mode")
    private Boolean standaloneMode;

    /**
     * 手机号
     * 推送手机号，以 | 分隔，最多支持1000个手机号；为空则表示全部推送。（备注：仅限于在上交所APP使用通行证登录过的设备）
     */
    @TableField("phone_no")
    private String phoneNo;

    /**
     * 来源系统（使用上交所APP推送系统分配的系统ID）
     */
    @TableField("`from`")
    @NotBlank(message = "来源系统不能为空")
    private String from;

    /**
     * 所属功能（使用上交所APP推送系统分配的功能ID）
     */
    @TableField("`function`")
    @NotBlank(message = "所属功能不能为空")
    private String function;

    /**
     * 推送时间
     * 为空立即推送(yyyy-MM-dd HH:mm:ss)
     */
    @TableField("publish_time")
    private String publishTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 推送状态（0：待推送，1：成功，2：失败，3：作废，4：已统计）
     */
    @TableField("publish_status")
    private String publishStatus;

    /**
     * 失败次数
     */
    @TableField("fail_number")
    private Integer failNumber;

    /**
     * 极光返回id
     */
    @TableField("msg_id")
    private String msgId;

    /**
     * 极光返回sendno
     */
    @TableField("send_no")
    private String sendNo;
}
