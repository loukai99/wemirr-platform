package com.wemirr.framework.boot.sensitive;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * 数据脱敏JSON序列化工具
 *
 * @author Levin
 */
@Slf4j
@NoArgsConstructor
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private Sensitive sensitive;

    public SensitiveSerialize(final Sensitive sensitive) {
        this.sensitive = sensitive;
    }


    @SneakyThrows
    private String handler(Sensitive sensitive, String original) {
        if (sensitive == null) {
            return original;
        }
        try {
            final Sensitive.Type type = sensitive.type();
            switch (type) {
                case STRATEGY -> {
                    SensitiveStrategy instance = sensitive.strategy().getDeclaredConstructor().newInstance();
                    return instance.serialize(original);
                }
                case UN_KNOW -> {
                    return StrUtil.hide(original, sensitive.startInclude(), sensitive.endInclude());
                }
                case CHINESE_NAME -> {
                    return DesensitizedUtil.chineseName(original);
                }
                case ID_CARD -> {
                    return DesensitizedUtil.idCardNum(original, 1, 2);
                }
                case FIXED_PHONE -> {
                    return DesensitizedUtil.fixedPhone(original);
                }
                case MOBILE_PHONE -> {
                    return DesensitizedUtil.mobilePhone(original);
                }
                case ADDRESS -> {
                    return DesensitizedUtil.address(original, 8);
                }
                case EMAIL -> {
                    return DesensitizedUtil.email(original);
                }
                case PASSWORD -> {
                    return DesensitizedUtil.password(original);
                }
                case CAR_LICENSE -> {
                    return DesensitizedUtil.carLicense(original);
                }
                case BANK_CARD -> {
                    return DesensitizedUtil.bankCard(original);
                }
                case IPV4 -> {
                    return DesensitizedUtil.ipv4(original);
                }
                case IPV6 -> {
                    return DesensitizedUtil.ipv6(original);
                }
                default -> {
                    return original;
                }
            }
        } catch (Exception ex) {
            log.error("处理脱敏异常", ex);
        }
        return original;
    }


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(handler(sensitive, value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            return new SensitiveSerialize(annotation);
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}