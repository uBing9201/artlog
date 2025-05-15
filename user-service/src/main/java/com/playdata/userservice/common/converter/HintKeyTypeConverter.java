package com.playdata.userservice.common.converter;

import com.playdata.userservice.common.entity.HintKeyType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class HintKeyTypeConverter implements AttributeConverter<HintKeyType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HintKeyType attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public HintKeyType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? HintKeyType.fromCode(dbData) : null;
    }
}