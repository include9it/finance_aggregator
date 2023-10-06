package io.include9it.finance_aggregator.mapper;

import io.include9it.finance_aggregator.db.ApplicationEntity;
import io.include9it.finance_aggregator.db.OfferEntity;
import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.Offer;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
    public ApplicationEntity map(Application application) {
        var builder = ApplicationEntity.builder()
                .serviceName(application.serviceName())
                .id(application.id())
                .status(application.status());

        if (application.offer() != null) {
            builder.offer(map(application.offer()));
        }

        return builder.build();
    }

    public OfferEntity map(Offer offer) {
        return OfferEntity.builder()
                .firstRepaymentDate(offer.firstRepaymentDate())
                .annualPercentageRate(offer.annualPercentageRate())
                .monthlyPaymentAmount(offer.monthlyPaymentAmount())
                .numberOfPayments(offer.numberOfPayments())
                .totalRepaymentAmount(offer.totalRepaymentAmount())
                .build();
    }
}
