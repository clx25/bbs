package com.own.entity.database;

import com.own.annotation.FieldAlias;
import lombok.Data;

/**
 * board表
 */
@Data
public class BoardDO {
    //版块id
    @FieldAlias("boardId")
    private int id;
    //版块名
    private String boardName;
    //描述
    private String description;
    //是否开启
    private boolean enable;
}
