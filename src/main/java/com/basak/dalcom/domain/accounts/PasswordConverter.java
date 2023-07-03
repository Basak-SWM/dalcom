package com.basak.dalcom.domain.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@RequiredArgsConstructor
@Converter
@Component
public class PasswordConverter implements AttributeConverter<String, String> {
    private final PasswordHashingStrategy hashingStrategy;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return null;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
