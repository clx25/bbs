package com.bbs.controller;


import com.bbs.entity.Result;
import com.bbs.entity.vo.BoardVO;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@Api(tags = "版块控制器")
public class BoardController {

    private final BoardService boardService;

    @ApiOperation("获取启用的版块列表")
    @GetMapping("/board")
    public Result<List<BoardVO>> getEnableBoard() {
        return boardService.listBoard();
    }


    @ApiOperation("根据版块id获取版块信息")
    @ApiImplicitParam(value = "版块id",name = "boardId",required = true)
    @GetMapping("/board/{boardId}")
    public Result<BoardVO> getBoard(@PathVariable @Min(value = 1, message = "boardId参数错误") int boardId) throws NotFoundException {
        return boardService.getBoard(boardId);
    }

}
