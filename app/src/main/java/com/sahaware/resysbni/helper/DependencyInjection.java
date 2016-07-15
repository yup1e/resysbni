package com.sahaware.resysbni.helper;


import java.util.HashMap;
import java.util.Map;

public class DependencyInjection<T> {
    static Map<String, Object> map = new HashMap<String, Object>();
    public static <T extends Object,I>  void  Add(I Interface,T obj) {
        map.put(Interface.toString(),obj);
    }

    public static <T> T Get(Class<T> obj) {
        return (T)map.get(obj.toString()) ;
    }
}
