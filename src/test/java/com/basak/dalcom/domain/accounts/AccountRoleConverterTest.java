package com.basak.dalcom.domain.accounts;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRoleConverterTest {
    private final AccountRoleConverter converter = new AccountRoleConverter();

    @Test
    public void enumToIntegerTest() {
        // Given
        AccountRole userRole = AccountRole.USER;
        AccountRole coachRole = AccountRole.COACH;

        // When
        Integer userConvertedValue = 1;
        Integer coachConvertedValue = 2;

        // Then
        assertThat(converter.convertToDatabaseColumn(userRole))
                .isEqualTo(userConvertedValue);
        assertThat(converter.convertToDatabaseColumn(coachRole))
                .isEqualTo(coachConvertedValue);
    }

    @Test
    public void integerToEnumTest() {
        // Given
        Integer userEnumValue = 1;
        Integer coachEnumValue = 2;

        // When
        AccountRole userRole = AccountRole.USER;
        AccountRole coachRole = AccountRole.COACH;

        // THen
        assertThat(converter.convertToEntityAttribute(userEnumValue))
                .isEqualTo(userRole);
        assertThat(converter.convertToEntityAttribute(coachEnumValue))
                .isEqualTo(coachRole);
    }
}
