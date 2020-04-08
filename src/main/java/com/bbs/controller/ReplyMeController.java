package com.bbs.controller;

import com.bbs.entity.Status;
import com.bbs.entity.Result;
import com.bbs.entity.vo.ReplyMeVO;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.service.ReplyMeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@Api(tags = "消息控制器")
public class ReplyMeController {


    private final ReplyMeService replyMeService;

    @ApiOperation("获取收到的回复消息")
    @ApiImplicitParam(name = "status", allowableValues = "history,unread"
            , value = "获取未读消息还是历史消息", paramType = "header", required = true)

    @GetMapping(value = "/replyMe", headers = {"status"})
    public Result<List<ReplyMeVO>> getHistoryMessage(@RequestHeader
                                                     @NotNull(message = "需要状态信息")
                                                     @Pattern(regexp = "(history|unread)", message = "状态信息错误")
                                                             String status) {
        return replyMeService.listReplyMe(status);
    }


    @ApiOperation("标记消息为已读")
    @ApiImplicitParam(value = "回复id", required = true)

    @PutMapping({"/replyMe", "/replyMe/{replyId}"})
    public Status markRead(@PathVariable(required = false)
                           @Min(value = 1, message = "无效的replyId") Integer replyId) throws NotFoundException {
        return replyMeService.markRead(replyId);
    }


    @ApiOperation("删除回复消息")
    @ApiImplicitParam(value = "回复id", required = true)

    @DeleteMapping("/replyMe/{replyId}")
    public Status deleteMsg(@PathVariable
                            @Min(value = 1, message = "无效的replyId") Integer replyId) throws NotFoundException {
        return replyMeService.deleteReplyMe(replyId);
    }


}
