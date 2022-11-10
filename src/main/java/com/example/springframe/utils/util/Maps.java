package com.example.springframe.utils.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 构建Map对象，支持链式构建
 */
@Slf4j
public final class Maps<K, V> {
    private final Map<K, V> values;

    public static Maps<Object, Object> of() {
        return new Maps<>(new LinkedHashMap<>());
    }

    public static <K, V> Maps<K, V> of(Class<K> key, Class<V> value) {
        return new Maps<>(new LinkedHashMap<K, V>());
    }

    public static <K, V> Maps<K, V> of(Class<K> key, Class<V> value, int initialCapacity) {
        return new Maps<>(new LinkedHashMap<K, V>(initialCapacity));
    }

    public static Maps<String, Object> ofSO() {
        return new Maps<>(new LinkedHashMap<String, Object>());
    }

    public static Maps<String, Object> ofSO(int initialCapacity) {
        return new Maps<>(new LinkedHashMap<String, Object>(initialCapacity));
    }

    public static Maps<String, String> ofSS() {
        return new Maps<>(new LinkedHashMap<String, String>());
    }

    public static Maps<String, String> ofSS(int initialCapacity) {
        return new Maps<>(new LinkedHashMap<String, String>(initialCapacity));
    }

    public static Map<Object, Object> by(final Object key, final Object value) {
        Objects.requireNonNull(key, "参数【key】是必须的");
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new HashMap<Object, Object>(1) {
            private static final long serialVersionUID = 4983257585114540206L;

            {
                put(key, value);
            }
        };
    }

    public static Map<String, Object> bySO(final String key, final Object value) {
        Objects.requireNonNull(key, "参数【key】是必须的");
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new HashMap<String, Object>(1) {
            private static final long serialVersionUID = -5600444915370162489L;

            {
                put(key, value);
            }
        };
    }

    public static Map<String, String> bySS(final String key, final String value) {
        Objects.requireNonNull(key, "参数【key】是必须的");
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new HashMap<String, String>(1) {
            private static final long serialVersionUID = 5814527523007359079L;

            {
                put(key, value);
            }
        };
    }

    private Maps() {
        this(null);
    }

    public Maps(Map<K, V> values) {
        this.values = Objects.isNull(values) ? new LinkedHashMap<>() : values;
    }

    public Maps<K, V> put(K key, V value) {
        if (Objects.nonNull(key) && Objects.nonNull(value)) {
            this.values.put(key, value);
        }
        return this;
    }

    public Maps<K, V> put(boolean hasTrue, K key, V value) {
        if (hasTrue && Objects.nonNull(key) && Objects.nonNull(value)) {
            this.values.put(key, value);
        }
        return this;
    }

    public Maps<K, V> putAll(Map<K, V> values) {
        if (Objects.nonNull(values)) {
            this.values.putAll(values);
        }
        return this;
    }

    public Map<K, V> build() {
        return this.values;
    }

    public JSONObject buildJSONObject() {
        return (JSONObject) JSONObject.toJSON(this.values);
    }

    public Map<String, Object> jsonKey() {
        return Maps.bySO("json", json());
    }

    public String json(SerializerFeature... features) {
        return JSON.toJSONString(this.values, features);
    }

    public static void main(String[] args) {
        { // demo
            log.info(
                    Maps.ofSS()
                            .put("111", "JX")
                            .put("222", "Jack")
                            .json(SerializerFeature.PrettyFormat)
            );
            log.info(
                    Maps.of(Integer.class, String.class)
                            .put(1, "JX")
                            .put(2, "Jack")
                            .json(SerializerFeature.PrettyFormat)
            );
//            log.info(
//                        Maps.of(Integer.class, Item.class)
//                            .put(1, Item.builder().label("name").value("JX").build())
//                            .put(2, Item.builder().label("name").value("Jack").build())
//                            .json(SerializerFeature.PrettyFormat)
//            );
//            log.info(
//                    Maps.of(Integer.class, Item.class)
//                            .putAll(Maps.of(Integer.class, Item.class)
//                                    .put(3, Item.builder().label("name").value("JX").build())
//                                    .put(4, Item.builder().label("name").value("Jack").build())
//                                    .build())
//                            .putAll(null)
//                            .put(1, Item.builder().label("name").value("JX").build())
//                            .put(2, Item.builder().label("name").value("Jack").build())
//                            .json(SerializerFeature.PrettyFormat)
//            );
//            Maps<Integer, Item> builder = Maps.of(Integer.class, Item.class);
//            for (int i = 0; i < 5; i++) {
//                builder.put(i, Item.builder().label("value"+i).value(i).build());
//            }
//            log.info(builder.json());
        }

        System.out.println(Maps.ofSO()
                .put("key", "测试")
                .buildJSONObject());
        System.out.println(Maps.ofSS()
                .put("key", "测试")
                .buildJSONObject());
        System.out.println(Maps.of(Integer.class, String.class)
                .put(1, "测试")
                .buildJSONObject());
        System.out.println(Maps.of(Long.class, String.class)
                .put(1L, "测试")
                .buildJSONObject());
    }

}
