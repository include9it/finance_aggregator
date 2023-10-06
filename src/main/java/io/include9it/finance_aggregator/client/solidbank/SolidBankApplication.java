package io.include9it.finance_aggregator.client.solidbank;

import io.include9it.finance_aggregator.dto.MaritalStatus;

import java.math.BigDecimal;

public record SolidBankApplication(
        String phone,
        String email,
        BigDecimal monthlyIncome,
        BigDecimal monthlyExpenses,
        MaritalStatus maritalStatus,
        Boolean agreeToBeScored,
        BigDecimal amount
) {
}
