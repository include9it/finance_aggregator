package io.include9it.finance_aggregator.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record FormRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String surname,
        @NotEmpty
        @Pattern(regexp = "\\+[0-9]{11,15}", message = "must be a valid phone")
        String phone,
        @NotEmpty
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "must be valid email")
        String email,
        @NotNull
        @PositiveOrZero
        BigDecimal monthlyIncome,
        @NotNull
        @PositiveOrZero
        BigDecimal monthlyExpenses,
        @NotNull
        @PositiveOrZero
        Integer dependents,
        @NotNull
        MaritalStatus maritalStatus,
        @NotNull
        Boolean agreeToBeScored,
        @NotNull
        Boolean agreeToDataSharing,
        @NotNull
        @Positive
        BigDecimal amount
) {
}
