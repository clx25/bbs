package com.own.servie.impl;

import com.own.entity.Result;
import com.own.entity.database.BoardDO;
import com.own.entity.vo.BoardVO;
import com.own.exception.customexception.NotFoundException;
import com.own.mapper.BoardMapper;
import com.own.servie.BoardService;
import com.own.util.EntityUtil;
import com.own.util.RedisUtil;
import com.own.util.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {


    private BoardMapper boardMapper;


    private static final String BOARD_CACHE_KEY = "board";
    private static final String BOARD_CACHE_LIST_HASH_KEY = "boardList";


    @Override
    public Result<List<BoardVO>> listBoard() {

        @SuppressWarnings("unchecked")
        List<BoardDO> boardList = (List<BoardDO>) RedisUtil.hget(BOARD_CACHE_KEY, BOARD_CACHE_LIST_HASH_KEY);

        if (boardList == null) {
            boardList = boardMapper.listEnableBoard();

            RedisUtil.hset(BOARD_CACHE_KEY, BOARD_CACHE_LIST_HASH_KEY, boardList);
        }
        List<BoardVO> boardVOList = EntityUtil.copyToEntityList(boardList, BoardVO.class);

        return ResultUtil.success(boardVOList);
    }


    @Override
    public Result<BoardVO> getBoard(int boardId) throws NotFoundException {

        BoardVO boardVO = (BoardVO) RedisUtil.hget(BOARD_CACHE_KEY, boardId);

        if (boardVO == null) {
            BoardDO board = boardMapper.getBoardById(boardId);
            if (board == null) {
                throw new NotFoundException("版块不存在");
            }

            boardVO = new BoardVO();
            EntityUtil.CopyToEntity(board, boardVO);

            RedisUtil.hset(BOARD_CACHE_KEY, boardId, boardVO);
        }
        return ResultUtil.success(boardVO);
    }


}
