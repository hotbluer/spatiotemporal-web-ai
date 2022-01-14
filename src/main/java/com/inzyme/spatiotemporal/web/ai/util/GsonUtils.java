/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.inzyme.spatiotemporal.web.ai.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * 
 * @ClassName: GsonUtils    
 * @Description: Json工具类.
 * @date 2020年3月5日 下午1:11:28    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
 */
public class GsonUtils {
    private static Gson gson = new GsonBuilder().create();

    public static String toJson(Object value) {
        return gson.toJson(value);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonParseException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonParseException {
        return (T) gson.fromJson(json, typeOfT);
    }    

}
