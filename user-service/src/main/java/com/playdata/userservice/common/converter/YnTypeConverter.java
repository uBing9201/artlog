package com.playdata.userservice.common.converter;

import com.playdata.userservice.common.entity.YnType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YnTypeConverter implements AttributeConverter<YnType, String> {

    @Override
    public String convertToDatabaseColumn(YnType attribute) {
        return attribute != null ? attribute.getCode() : null;  // "Y" 또는 "N"
    }

    @Override
    public YnType convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData) ? YnType.YES : YnType.NO;
    }
}