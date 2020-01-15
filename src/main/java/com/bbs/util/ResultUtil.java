package com.bbs.util;

import com.bbs.entity.Status;
import com.bbs.entity.Result;

public class ResultUtil {

    public static<T> Result<T> success(T t){
        Result<T> result = new Result<>();
        result.setStatus(StatusEnum.SUCCESS);
        result.setData(t);
        return result;
    }

    public static Status success(){
        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    public static Status info(int code, String msg) {
        Status status = new Status();
        status.setCode(code);
        status.setMsg(msg);
        return status;
    }

    public static Status info(StatusEnum status) {
        Status messageVO = new Status();
        messageVO.setCode(status.code());
        messageVO.setMsg(status.msg());
        return messageVO;
    }
}
