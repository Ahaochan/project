package com.ahao.spring.boot.rabbitmq;

import com.ahao.util.commons.io.JSONHelper;
import com.ahao.util.commons.lang.BeanHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FastJsonMessageConverter extends AbstractMessageConverter implements MessageConverter {
    private static Logger logger = LoggerFactory.getLogger(FastJsonMessageConverter.class);
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final String HEADER_JAVA_TYPE = "java-type";

    private Charset charset = DEFAULT_CHARSET;
    private boolean useRawJson = true;

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        if (message == null) {
            return null;
        }
        MessageProperties messageProperties = message.getMessageProperties();

        // 1. 获取编码类型, 转为字符串类型
        String contentEncoding = messageProperties.getContentEncoding();
        Charset charset = StringUtils.isEmpty(contentEncoding) ? getCharset() : Charset.forName(contentEncoding);
        String jsonString = new String(message.getBody(), charset);
        if(useRawJson) {
            return jsonString;
        }

        // 2. 获取 java 类型, 尝试转为 java 对象
        Map<String, Object> header = messageProperties.getHeaders();
        Object javaType = header.get(HEADER_JAVA_TYPE);
        String javaTypeString = javaType == null ? null : String.valueOf(javaType);
        try {
            Class clazz = Class.forName(javaTypeString);

            Object object = null;
            if (Number.class.isAssignableFrom(clazz)) {
                object = BeanHelper.cast(jsonString, clazz);
            } else if (String.class.isAssignableFrom(clazz)) {
                object = jsonString;
            } else {
                object = JSONHelper.parse(jsonString, clazz);
            }
            return object;
        } catch (Exception e) {
            logger.warn("转化为{}类型错误!", javaType, e);
        }
        return jsonString;
    }

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        // 1. 将对象转为 byte[]
        byte[] bytes;
        if (object == null) {
            bytes = new byte[0];
        } else if (object instanceof String || object instanceof Number) {
            bytes = String.valueOf(object).getBytes(StandardCharsets.UTF_8);
        } else {
            bytes = JSONHelper.toBytes(object);
        }

        // 2. 初始化相关属性
        if (messageProperties == null) {
            messageProperties = new MessageProperties();
        }
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(getCharset().name());
        messageProperties.setContentLength(bytes.length);
        messageProperties.setHeader(HEADER_JAVA_TYPE, object.getClass().getName());
        return new Message(bytes, messageProperties);
    }

    public void setCharset(Charset charset) {
        this.charset = (charset != null) ? charset : DEFAULT_CHARSET;
    }

    public Charset getCharset() {
        return charset;
    }

    public boolean isUseRawJson() {
        return useRawJson;
    }

    public void setUseRawJson(boolean useRawJson) {
        this.useRawJson = useRawJson;
    }
}
