package com.swbg.mlivestreaming.provider;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryHashMapSerializer implements Serializer {

    private ConcurrentHashMap<String, Object> fields;


    public MemoryHashMapSerializer(int initialCapacity) {
        if (initialCapacity <= 0) {
            this.fields = new ConcurrentHashMap<>();
        } else {
            this.fields = new ConcurrentHashMap<>(initialCapacity);
        }
    }

    @Override
    public void save(String key, Object value) {
        if (value != null) {
            fields.put(key, value);
        }
    }

    @Override
    public Object get(String key) {
        return fields.get(key);
    }

    @Override
    public void saveAll(Map<String, ?> map) {
        fields.putAll(map);
    }

    @Override
    public Map<String, ?> getAll() {
        return fields;
    }

    @Override
    public void clear() {
        fields.clear();
    }
}
