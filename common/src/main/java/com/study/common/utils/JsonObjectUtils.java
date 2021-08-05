package com.study.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.study.common.model.vo.UserInfoVo;

import java.util.Map;

public class JsonObjectUtils {

    //Map和实体类直接的转换
    public static <T>T mapToEntity(Map map,Class<T> tClass){
        T object= JSON.parseObject(JSON.toJSONString(map), new TypeReference<T>(){});
        System.out.println(object);
        return object;
    }
    // 将 实体类 转换为 Map
    public static Map entityToMap(Object object,Map map){
        map = JSON.parseObject(JSON.toJSONString(object), Map.class);
        return map;
    }

}
