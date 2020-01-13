package com.own.servie;

import com.own.entity.Result;
import com.own.entity.vo.BoardVO;

import com.own.exception.customexception.NotFoundException;

import java.util.List;


public interface BoardService {

    /**
     * 获取启用的版块
     *
     * @return 返回页面的启用的版块列表
     */
    Result<List<BoardVO>> listBoard();

    /**
     * 从数据库或缓存中获取版块信息
     *
     * @param boardId 版块id
     * @return 版块简单信息实体类
     */
    Result<BoardVO> getBoard(int boardId) throws NotFoundException;

}
