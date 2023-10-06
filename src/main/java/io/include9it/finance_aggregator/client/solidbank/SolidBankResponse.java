package io.include9it.finance_aggregator.client.solidbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.include9it.finance_aggregator.dto.ApplicationStatus;
import io.include9it.finance_aggregator.dto.Offer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record SolidBankResponse(
        @NonNull
        @JsonProperty(value = "id")
        String applicationId,
        @NonNull
        ApplicationStatus status,
        @Nullable
        Offer offer
) {
}
