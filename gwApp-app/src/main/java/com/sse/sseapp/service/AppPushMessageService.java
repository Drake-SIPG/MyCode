package com.sse.sseapp.service;

import cn.hutool.core.util.ObjectUtil;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.domain.push.AppPushMessageFrom;
import com.sse.sseapp.domain.push.AppPushMessageFromFunction;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFromFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFromFunctionFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
@Slf4j
@Service
public class AppPushMessageService {

    @Autowired
    private IAppPushMessageFeign feign;

    @Autowired
    private IAppPushMessageFromFeign appPushMessageFromFeign;

    @Autowired
    private IAppPushMessageFromFunctionFeign appPushMessageFromFunctionFeign;

    public AjaxResult add(AppPushMessage appPushMessage) {
        if (ObjectUtil.equal(appPushMessage.getClickType(), AppPushMessageConstants.CLICK_TYPE_H5)) {
            if (ObjectUtil.isEmpty(appPushMessage.getClickUrl())) {
                throw new AppException("点击链接不能为空");
            } else if (!appPushMessage.getClickUrl().startsWith("http")) {
                throw new AppException("点击链接格式不正确");
            }
        }
        if (ObjectUtil.equal(appPushMessage.getClickType(), AppPushMessageConstants.CLICK_TYPE_SPECIFY_MENU)) {
            List<String> list = Arrays.asList(AppPushMessageConstants.CLICK_MENU_TYPE);
            if (ObjectUtil.isEmpty(appPushMessage.getClickUrl())) {
                throw new AppException("跳转菜单下标不能为空");
            } else if (!list.contains(appPushMessage.getClickUrl())) {
                throw new AppException("跳转菜单下标值在【0、1、2、3、4】中");
            }
        }
        if (ObjectUtil.isNotEmpty(appPushMessage.getPublishTime()) && !isDateValid(appPushMessage.getPublishTime())) {
            throw new AppException("推送时间格式不正确");
        }
        try {
            AppPushMessageFrom appPushMessageFrom = appPushMessageFromFeign.getInfoByFromId(appPushMessage.getFrom());
            if (ObjectUtil.isNotEmpty(appPushMessageFrom)) {
                if (ObjectUtil.equal(appPushMessageFrom.getStatus(), AppPushMessageConstants.APP_PUSH_MESSAGE_FROM_STATUS_NORMAL)) {
                    //根据formId查询功能列表
                    AppPushMessageFromFunction fromFunction = new AppPushMessageFromFunction();
                    fromFunction.setFromId(appPushMessage.getFrom());
                    List<AppPushMessageFromFunction> fromFunctionList = appPushMessageFromFunctionFeign.list(fromFunction);
                    //判断是否有功能权限
                    Optional<AppPushMessageFromFunction> optional = fromFunctionList.stream().filter(a -> ObjectUtil.equal(a.getFunction(), appPushMessage.getFunction())).findAny();
                    if (optional.isPresent()) {
                        if (ObjectUtil.isEmpty(appPushMessage.getPublishStatus())) {
                            appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_WAIT);
                        }
                        log.info("插入消息推送表: {}", appPushMessage);
                        return this.feign.add(appPushMessage);
                    } else {
                        throw new AppException("无权限");
                    }
                } else {
                    throw new AppException("系统ID已被禁用");
                }
            } else {
                throw new AppException("系统ID不存在");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }


    public static boolean isDateValid(String str) {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }
}
