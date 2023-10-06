package io.include9it.finance_aggregator.mapper;

import io.include9it.finance_aggregator.db.ApplicationBundleEntity;
import io.include9it.finance_aggregator.db.ApplicationEntity;
import io.include9it.finance_aggregator.db.OfferEntity;
import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.ApplicationResponse;
import io.include9it.finance_aggregator.dto.Offer;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {
    public ApplicationResponse map(ApplicationBundleEntity entity) {
        return new ApplicationResponse(
                entity.getId(),
                entity.getApplications().stream().map(this::map).toList()
        );
    }

    Application map(ApplicationEntity entity) {
        var offerEntity = entity.getOffer();

        return new Application(
                entity.getServiceName(),
                entity.getId(),
                entity.getStatus(),
                offerEntity != null ? map(offerEntity) : null
        );
    }

    Offer map(OfferEntity entity) {
        return new Offer(
                entity.getMonthlyPaymentAmount(),
                entity.getTotalRepaymentAmount(),
                entity.getNumberOfPayments(),
                entity.getAnnualPercentageRate(),
                entity.getFirstRepaymentDate()
        );
    }
}
