package io.include9it.finance_aggregator.client.fastbank;

import java.math.BigDecimal;

public record FastBankApplication(
        String phoneNumber,
        String email,
        BigDecimal monthlyIncomeAmount,
        BigDecimal monthlyCreditLiabilities,
        Integer dependents,
        Boolean agreeToDataSharing,
        BigDecimal amount
) {
}
