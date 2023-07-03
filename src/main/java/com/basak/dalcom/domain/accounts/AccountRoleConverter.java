package com.basak.dalcom.domain.accounts;

import javax.persistence.AttributeConverter;
import java.util.Optional;

public class AccountRoleConverter implements AttributeConverter<AccountRole, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AccountRole attribute) {
        return AccountRole.valueOf(attribute)
                .orElseThrow(() -> new IllegalStateException("Unknown AccountRole"));
    }

    @Override
    public AccountRole convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        return AccountRole.enumOf(dbData)
                .orElseThrow(() -> new IllegalStateException("Unknown AccountRole Value"));
    }
}
