package com.xin.web.utils.convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.xin.web.base.BaseEnum;
import com.xin.web.utils.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

/**
 * json工具类
 * 实现JSON String<->Java Object的Mapper.
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 *
 * @author creator mafh 2019/11/26 17:00
 * @author updater
 * @version 1.0.0
 */
public class JsonUtils extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 当前类的实例持有者（静态内部类，延迟加载，懒汉式，线程安全的单例模式）
     */
    private static final class JsonUtilsHolder {
        private static final JsonUtils INSTANCE = new JsonUtils();
    }

    private JsonUtils() {
        // 为null时不序列化
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 允许单引号
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许不带引号的字段名称
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 设置时区
        this.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 遇到空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen,
                                  SerializerProvider provider) throws IOException {
                jgen.writeString("");
            }
        });
        // 统一默认Date类型转换格式
        this.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date value, JsonGenerator jgen,
                                  SerializerProvider provider) throws IOException {
                if (value != null) {
                    jgen.writeString(DateUtils.formatDate(value, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        }));
        this.registerModule(new SimpleModule().addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String dateString = p.getValueAsString();
                if (StringUtils.isEmpty(dateString)) {
                    return null;
                }
                Date dateTime = DateUtils.getDateTime(dateString, "yyyy-MM-dd HH:mm:ss");
                if (dateTime == null) {
                    logger.warn("日期格式必须为: yyyy-MM-dd HH:mm:ss, 接受到的日期数据为: " + dateString);
                    return null;
                }
                return dateTime;
            }
        }));
        // 统一默认 Timestamp 类型格式转换
        this.registerModule(new SimpleModule().addSerializer(Timestamp.class, new JsonSerializer<Timestamp>() {
            @Override
            public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value != null) {
                    Date date = new Date(value.getTime());
                    gen.writeString(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
                }
            }
        }));
        this.registerModule(new SimpleModule().addDeserializer(Timestamp.class, new JsonDeserializer<Timestamp>() {
            @Override
            public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String valueAsString = p.getValueAsString();
                if (StringUtils.isEmpty(valueAsString)) {
                    return null;
                }
                Date dateTime = DateUtils.getDateTime(valueAsString, "yyyy-MM-dd HH:mm:ss");
                if (dateTime == null) {
                    logger.warn("日期格式必须为: yyyy-MM-dd HH:mm:ss, 接受到的日期数据为: " + valueAsString);
                    return null;
                }
                return new Timestamp(dateTime.getTime());
            }
        }));
        // 支持枚举对象
        this.registerModule(new SimpleModule().addSerializer(BaseEnum.class, new JsonSerializer<BaseEnum>() {
            @Override
            public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value != null) {
                    gen.writeObject(value.getValue());
                }
            }
        }));
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    private String toJsonString(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    private void toJsonStream(OutputStream outputStream, Object object) {
        try {
            this.writeValue(outputStream, object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
        }
    }

    /**
     * 输出JSONP格式数据.
     */
    private String toJsonpString(String functionName, Object object) {
        return toJsonString(new JSONPObject(functionName, object));
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     */
    private <T> T fromJsonString(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     */
    private <T> T fromJsonStream(InputStream jsonString, Class<T> clazz) {
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     */
    private <T> T fromJsonString(String jsonString, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, typeReference);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函数createCollectionType构造类型,然后调用本函数.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJsonString(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造泛型的Collection Type如:
     * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    private JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 当JSON里只含有Bean的部分属性時，更新一个已存在Bean，只覆盖该部分的属性.
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) this.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 设定是否使用Enum的toString函数来读写Enum,
     * 为False实时使用Enum的name()函数来读写Enum, 默认为False.
     * 注意本函数一定要在Mapper创建后, 所有的读写动作之前调用.
     */
    public JsonUtils enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return this;
    }

    /**
     * 获取当前实例
     */
    private static JsonUtils getInstance() {
        return JsonUtilsHolder.INSTANCE;
    }

    //-------------------------------------- 静态方法 ------------------------------------//

    /**
     * 对象转换为JSON字符串
     */
    public static String toJson(Object object) {
        return JsonUtils.getInstance().toJsonString(object);
    }

    /**
     * 对象转换为JSON字符串,并输出
     */
    public static void toJson(OutputStream outputStream, Object object) {
        JsonUtils.getInstance().toJsonStream(outputStream, object);
    }


    /**
     * 对象转换为JSONP字符串
     */
    public static String toJsonp(String functionName, Object object) {
        return JsonUtils.getInstance().toJsonpString(functionName, object);
    }

    /**
     * JSON字符串转换为对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, Class<?> clazz) {
        return (T) JsonUtils.getInstance().fromJsonString(jsonString, clazz);
    }

    /**
     * JSON字符串转换为对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(InputStream jsonStream, Class<?> clazz) {
        return (T) JsonUtils.getInstance().fromJsonStream(jsonStream, clazz);
    }

    /**
     * JSON字符串转换为对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
        return JsonUtils.getInstance().fromJsonString(jsonString, typeReference);
    }
}
