package com.bbs.service;

import com.bbs.entity.Result;
import com.bbs.entity.database.BoardDO;
import com.bbs.entity.vo.BoardVO;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.mapper.BoardMapper;
import com.bbs.util.EntityUtil;
import com.bbs.util.RedisUtil;
import com.bbs.util.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class BoardService {


    private final BoardMapper boardMapper;

    //版块在redis中的key
    private final String BOARD_CACHE_KEY = "board";
    //版块集合在redis中的key
    private final String BOARD_CACHE_LIST_HASH_KEY = "boardList";


    /**
     * 获取所有版块
     *
     * @return 所有版块集合
     */
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


    /**
     * 获取版块
     *
     * @param boardId 版块id
     * @return 版块信息
     * @throws NotFoundException 找不到该版块
     */
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

    /**
     * 获取版块集合
     *
     * @param boardIds 版块id集合
     * @return 版块集合
     */
    public List<BoardDO> listBoardInBoardIds(Set<Integer> boardIds) {
        return boardMapper.listBoardInBoardIds(boardIds);
    }
}
