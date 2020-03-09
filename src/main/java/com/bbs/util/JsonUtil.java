package com.bbs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static  ObjectMapper om=new ObjectMapper();

    public static String toJson(Object o) throws JsonProcessingException {
        return om.writeValueAsString(o);
    }
}
