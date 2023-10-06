package io.include9it.finance_aggregator.dto;

import java.util.List;
import java.util.UUID;

public record ApplicationResponse(
        UUID id,
        List<Application> applications
) {
}
