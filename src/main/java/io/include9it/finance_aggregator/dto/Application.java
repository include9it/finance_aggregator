package io.include9it.finance_aggregator.dto;

import org.springframework.lang.Nullable;

public record Application(
        String serviceName,
        String id,
        ApplicationStatus status,
        @Nullable
        Offer offer
) {
    public Application(String serviceName, String id, ApplicationStatus status) {
        this(serviceName, id, status, null);
    }
}
