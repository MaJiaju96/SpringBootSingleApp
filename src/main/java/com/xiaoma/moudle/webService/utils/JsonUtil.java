package com.xiaoma.moudle.webService.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xiaoma.moudle.webService.exception.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;

public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public JsonUtil() {
    }

    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static byte[] toJsonAsBytes(Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> T parse(String content, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(InputStream in, Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
        try {
            if (!StringUtil.startsWithIgnoreCase(content, "[")) {
                content = "[" + content + "]";
            }

            List<Map<String, Object>> list = (List) getInstance().readValue(content, new TypeReference<List<Map<String, Object>>>() {
            });
            List<T> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Map<String, Object> map = (Map) var4.next();
                result.add(toPojo(map, valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return (Map) getInstance().readValue(content, Map.class);
        } catch (IOException var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map = (Map) getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
            Map<String, T> result = new HashMap(16);
            Iterator var4 = map.entrySet().iterator();

            while (var4.hasNext()) {
                Entry<String, Map<String, Object>> entry = (Entry) var4.next();
                result.put(entry.getKey(), toPojo((Map) entry.getValue(), valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static JsonNode readTree(String jsonString) {
        try {
            return getInstance().readTree(jsonString);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static JsonNode readTree(InputStream in) {
        try {
            return getInstance().readTree(in);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static JsonNode readTree(byte[] content) {
        try {
            return getInstance().readTree(content);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static JsonNode readTree(JsonParser jsonParser) {
        try {
            return (JsonNode) getInstance().readTree(jsonParser);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;
        private static final Locale CHINA;

        public JacksonObjectMapper() {
            super.setLocale(CHINA);
            super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            super.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
            super.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            super.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
            super.findAndRegisterModules();
            super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            super.configure(Feature.ALLOW_SINGLE_QUOTES, true);
            super.getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
            super.registerModule(new JavaTimeModule());
            super.findAndRegisterModules();
        }

        public ObjectMapper copy() {
            return super.copy();
        }

        static {
            CHINA = Locale.CHINA;
        }
    }

    private static class JacksonHolder {
        private static ObjectMapper INSTANCE = new JacksonObjectMapper();

        private JacksonHolder() {
        }
    }


    //比较两个jsonobject对象是否相等
    public static Boolean compareJsonObject(JSONObject jsonObject1, JSONObject jsonObject2) {
        if(jsonObject1==null&&jsonObject2==null){
            return true;
        }else if((jsonObject1==null&&jsonObject2!=null)||(jsonObject1!=null&&jsonObject2==null)) {
            return false;
        }
        Set<Entry<String, Object>> entries1 = jsonObject1.entrySet();
        Set<Entry<String, Object>> entries2 = jsonObject2.entrySet();
        //先判断数据长度是否一致
        if (entries1.size() != entries2.size()) {
            return false;
        }
        for (Entry<String, Object> entry : entries1) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (StringUtil.equals("outgoing", key)||StringUtil.equals("bounds", key)) {
                continue;
            }
            if (value instanceof Integer) {
                Integer integer1 = (Integer) value;
                Integer integer2 = jsonObject2.getInteger(key);
                if ((integer1==null&&integer2!=null)||(integer1!=null&&integer2==null)||integer1.compareTo(integer2) != 0) {
                    return false;
                }
            } else if (value instanceof String) {
                String string1 = (String) value;
                String string2 = jsonObject2.getString(key);
                if (!StringUtil.equals(string1, string2)) {
                    return false;
                }
            } else if (value instanceof Double) {
                Double double1 = (Double) value;
                Double double2 = jsonObject2.getDouble(key);
                if ((double1==null&&double2!=null)||(double1!=null&&double2==null)||double1.compareTo(double2) != 0) {
                    return false;
                }
            } else if (value instanceof Float) {
                Float aFloat1 = (Float) value;
                Float aFloat2 = jsonObject2.getFloat(key);
                if ((aFloat1==null&&aFloat2!=null)||(aFloat1!=null&&aFloat2==null)||aFloat1.compareTo(aFloat2) != 0) {
                    return false;
                }
            } else if (value instanceof Boolean) {
                Boolean aBoolean1 = (Boolean) value;
                Boolean aBoolean2 = jsonObject2.getBoolean(key);
                if (aBoolean1 != aBoolean2) {
                    return false;
                }
            } else if (value instanceof Date) {
                Date date1 = (Date) value;
                Date date2 = jsonObject2.getDate(key);
                if ((date1==null&&date2!=null)||(date1!=null&&date2==null)||date1.compareTo(date2) != 0) {
                    return false;
                }
            } else if (value instanceof Long) {
                Long aLong1 = (Long) value;
                Long aLong2 = jsonObject2.getLong(key);
                if ((aLong1==null&&aLong2!=null)||(aLong1!=null&&aLong2==null)||aLong1.compareTo(aLong2) != 0) {
                    return false;
                }
            } else if (value instanceof JSONObject) {
                Boolean aBoolean = compareJsonObject((JSONObject) value, jsonObject2.getJSONObject(key));
                if (!aBoolean) {
                    return false;
                }
            } else if (value instanceof JSONArray) {
                Boolean aBoolean = compareJSONArray((JSONArray) value, jsonObject2.getJSONArray(key));
                if (!aBoolean) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @param array1
     * @param array2
     * @function 比较两个JSONArray元素, 内容是否一致
     * @return 一致返回true, 不一致返回false
     */
    public static boolean compareJSONArray(JSONArray array1, JSONArray array2) {
        if (array1 == null && array2 == null) {
            return true;
        } else if (array1 == null && array2 != null) {
            return false;
        } else if (array1 != null && array2 == null) {
            return false;
        } else if (array1.size() != array2.size()) {
            return false;
        }
        for (int index = 0; index < array1.size(); index++) {
            //array1的第index个元素还是JSONArray,则遍历array2的所有元素,递归比较...
            Object o1 = array1.get(index);
            Object o2 = array2.get(index);
            Class<?> aClass = o1.getClass();
            //判断类型是否一样
            if (o1.getClass()==o2.getClass()) {
                if (o1 instanceof Integer) {
                    Integer integer1 = (Integer) o1;
                    Integer integer2 = (Integer) o2;
                    if (integer1.compareTo(integer2) != 0) {
                        return false;
                    }
                } else if (o1 instanceof String) {
                    String string1 = (String) o1;
                    String string2 = (String) o2;
                    if (!StringUtil.equals(string1, string2)) {
                        return false;
                    }
                } else if (o1 instanceof Double) {
                    Double double1 = (Double) o1;
                    Double double2 =  (Double) o2;
                    if (double1.compareTo(double2) != 0) {
                        return false;
                    }
                } else if (o1 instanceof Float) {
                    Float aFloat1 = (Float) o1;
                    Float aFloat2 =  (Float) o2;
                    if (aFloat1.compareTo(aFloat2) != 0) {
                        return false;
                    }
                } else if (o1 instanceof Boolean) {
                    Boolean aBoolean1 = (Boolean) o1;
                    Boolean aBoolean2 = (Boolean) o2;
                    if (aBoolean1 != aBoolean2) {
                        return false;
                    }
                } else if (o1 instanceof Date) {
                    Date date1 = (Date) o1;
                    Date date2 = (Date) o2;
                    if (date1.compareTo(date2) != 0) {
                        return false;
                    }
                } else if (o1 instanceof Long) {
                    Long aLong1 = (Long) o1;
                    Long aLong2 =(Long) o2;
                    if (aLong1.compareTo(aLong2) != 0) {
                        return false;
                    }
                } else if (o1 instanceof JSONObject) {
                    Boolean aBoolean = compareJsonObject((JSONObject) o1, (JSONObject) o2);
                    if (!aBoolean) {
                        return false;
                    }
                } else if (o1 instanceof JSONArray) {
                    Boolean aBoolean = compareJSONArray((JSONArray) o1,(JSONArray) o2);
                    if (!aBoolean) {
                        return false;
                    }
                }
            }else {
                return false;
            }
        }
        return true;
    }

}
