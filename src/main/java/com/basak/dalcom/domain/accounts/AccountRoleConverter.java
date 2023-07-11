package com.basak.dalcom.domain.accounts;

import javax.persistence.AttributeConverter;

public class AccountRoleConverter implements AttributeConverter<AccountRole, String> {
    @Override
    public String convertToDatabaseColumn(AccountRole attribute) {
        return AccountRole.valueOf(attribute)
                .orElseThrow(() -> new IllegalStateException("Unknown AccountRole"));
    }

    @Override
    public AccountRole convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        return AccountRole.enumOf(dbData)
                .orElseThrow(() -> new IllegalStateException("Unknown AccountRole Value"));
    }
}
