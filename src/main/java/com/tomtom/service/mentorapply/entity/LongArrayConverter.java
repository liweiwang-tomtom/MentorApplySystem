package com.tomtom.service.mentorapply.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Converter
public class LongArrayConverter implements AttributeConverter<long[], String> {

    @Override
    public String convertToDatabaseColumn(long[] attribute) {
        if (attribute == null || attribute.length == 0) {
            return null;
        }
        return Arrays.stream(attribute)
            .mapToObj(String::valueOf)
            .collect(Collectors.joining(","));
    }

    @Override
    public long[] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new long[0];
        }
        return Arrays.stream(dbData.split(","))
            .mapToLong(Long::parseLong)
            .toArray();
    }
}
