package com.sse.sseapp.controller;



import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.AskQuestionReqBody;
import com.sse.sseapp.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author: liuxinyu
 * @create-date: 2023/4/3 17:41
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController extends BaseController {


    @Autowired
    QuestionService questionService;

    /**
     *  提问 提交按钮
     */
    @PostMapping("/askQuestion")
    @ResponseBody
    @Log("提问 提交按钮")
    @Decrypt
    @Encrypt
    public RespBean askQuestion(@RequestBody BaseRequest<AskQuestionReqBody> askQuestionReq){
        //输出返回值
        return RespBean.success(questionService.askQuestion(askQuestionReq));
    }



}
