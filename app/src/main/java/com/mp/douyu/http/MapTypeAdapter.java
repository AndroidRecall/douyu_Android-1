package com.mp.douyu.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

/**
 *
 * Created by HT on 2017/8/31.
 * Map序列化
 */

class MapTypeAdapter extends TypeAdapter<Map<String, Object>> {

    private Gson gson = new GsonBuilder().create();
    @Override
    public void write(JsonWriter out, Map<String, Object> map) throws IOException {
        if (map == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        for (Map.Entry<String, Object> entry: map.entrySet()){
            String key = entry.getKey();
            out.name(key);
            Object value = entry.getValue();
            if (value instanceof Map) {
                write(out, (Map<String, Object>) value);
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                adapter.write(out, value);
            }
        }
        out.endObject();
    }

    @Override
    public Map<String, Object> read(JsonReader in) throws IOException {

        return null;
    }
}
