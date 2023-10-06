package io.include9it.finance_aggregator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Offer(
        BigDecimal monthlyPaymentAmount,
        BigDecimal totalRepaymentAmount,
        Integer numberOfPayments,
        BigDecimal annualPercentageRate,
        LocalDate firstRepaymentDate
) {}
