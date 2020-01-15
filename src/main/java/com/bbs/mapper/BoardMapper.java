package com.bbs.mapper;

import com.bbs.entity.database.BoardDO;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface BoardMapper {


    /**
     * 根据版块id获取版块信息
     * @param boardId 版块id
     * @return 版块信息
     */
    BoardDO getBoardById(int boardId);


    /**
     * 获取启用的版块
     * @return 启用的版块信息集合
     */
    List<BoardDO> listEnableBoard();


    /**
     * 根据版块的id集合获取对应的版块信息
     * @param boardIds 版块id集合
     * @return key为版块id，value为版块信息
     */
    @MapKey("id")
    Map<Integer,BoardDO> listBoardInBoardIds(Set<Integer> boardIds);
}
