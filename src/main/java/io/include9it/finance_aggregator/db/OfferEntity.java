package io.include9it.finance_aggregator.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offer")
public class OfferEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false)
    private BigDecimal monthlyPaymentAmount;

    @NotNull
    @Column(nullable = false)
    private BigDecimal totalRepaymentAmount;

    @NotNull
    @Column(nullable = false)
    private Integer numberOfPayments;

    @NotNull
    @Column(nullable = false)
    private BigDecimal annualPercentageRate;

    @NotNull
    @Column(nullable = false)
    private LocalDate firstRepaymentDate;
}
