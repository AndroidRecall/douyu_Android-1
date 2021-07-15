package com.mp.douyu.provider;


import java.util.Map;

public interface Serializer {
    void save(String key, Object value);
    Object get(String key);
    void saveAll(Map<String, ?> map);
    Map<String, ?> getAll();
    void clear();
}
