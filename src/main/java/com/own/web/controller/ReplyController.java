package com.own.web.controller;

import com.github.pagehelper.PageInfo;
import com.own.entity.Result;
import com.own.entity.Status;
import com.own.entity.dto.ReplyDTO;
import com.own.entity.vo.ReplyVO;
import com.own.exception.customexception.NotFoundException;
import com.own.servie.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@AllArgsConstructor
@Validated
@Api(tags = "回复控制器")
public class ReplyController {

    private final ReplyService replyService;


    @ApiOperation("根据帖子id获取回帖数据和分页信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "threadId", value = "版块id", required = true),
            @ApiImplicitParam(name = "pageNum", value = "第几页")})

    @GetMapping("/post")
    public Result<PageInfo<ReplyVO>> getPost(@RequestParam @Min(value = 1, message = "threadId错误") int threadId,
                                             @RequestParam(required = false, defaultValue = "1") int pageNum) {
        return replyService.getPost(threadId, pageNum);
    }


    @ApiOperation("根据回帖id获取楼中楼数据和分页数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "postId", value = "回帖id", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "第几页", paramType = "query")})

    @GetMapping("/lzl")
    public Result<PageInfo<ReplyVO>> getReplyInReply(@RequestParam @Min(value = 1, message = "postId错误") int postId,
                                                     @RequestParam(defaultValue = "1", required = false) int pageNum) throws NotFoundException {
        return replyService.getLzl(postId, pageNum);
    }

    @ApiOperation("添加楼中楼")
    @ApiImplicitParam(name = "postId", paramType = "query", required = true)

    @PostMapping("/lzl")
    public Status addLzl(@RequestParam @Min(value = 1, message = "postId错误") int postId,
                         @RequestBody @Validated ReplyDTO dto) throws Exception {
        return replyService.addLzl(postId, dto);
    }


    @ApiOperation("发表回帖")
    @ApiImplicitParam(name = "threadId", paramType = "query", required = true)

    @PostMapping("/post")
    public Status addPost(@RequestParam @Min(value = 1, message = "threadId错误") int threadId,
                          @RequestBody @Validated ReplyDTO dto) throws Exception {
        return replyService.addPost(threadId, dto);
    }


    @ApiOperation("删除回复")
    @ApiImplicitParam(value = "回复id", paramType = "path", required = true)

    @DeleteMapping("/reply/{replyId}")
    public Status deleteReply(@PathVariable @Min(value = 1, message = "参数错误") int replyId) throws NotFoundException {
        return replyService.deleteReply(replyId);
    }


}
