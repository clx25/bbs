package com.own.web.controller;

import com.github.pagehelper.PageInfo;
import com.own.entity.Result;
import com.own.entity.Status;
import com.own.entity.dto.AddThreadDTO;
import com.own.entity.vo.ThreadVO;
import com.own.exception.customexception.NotFoundException;
import com.own.servie.ThreadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@AllArgsConstructor
@Validated
@Api(tags = "帖子控制器")
public class ThreadController {


    private final ThreadService threadService;


    @ApiOperation("根据帖子id获取回帖")
    @ApiImplicitParams(@ApiImplicitParam(name = "threadId", value = "版块id",required = true))

    @GetMapping("/thread/{threadId}")
    public Result<ThreadVO> getThread(@PathVariable int threadId) throws NotFoundException {
        return threadService.getThread(threadId);
    }


    @ApiOperation("根据版块id查询版块下的帖子")
    @ApiImplicitParams({@ApiImplicitParam(name = "boardId", value = "版块id",required = true),
            @ApiImplicitParam(name = "pageNum", value = "第几页")})

    @GetMapping("/thread")
    public Result<PageInfo<ThreadVO>> listThread(@RequestParam @Min(value = 1, message = "boardId错误") int boardId,
                                                 @RequestParam(required = false, defaultValue = "1") int pageNum) throws Exception {
        return threadService.listThread(boardId, pageNum);
    }


    @ApiOperation("发帖")

    @PostMapping("/thread")
    public Status postThread(@RequestBody @Valid AddThreadDTO dto) {
        return threadService.postThread(dto);
    }
}
