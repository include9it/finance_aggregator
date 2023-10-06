package io.include9it.finance_aggregator.api;

import io.include9it.finance_aggregator.dto.ApplicationResponse;
import io.include9it.finance_aggregator.dto.FormRequest;
import io.include9it.finance_aggregator.service.FinancingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/forms")
@RequiredArgsConstructor
public class FormController {

    private final FinancingService service;

    @PostMapping
    public ApplicationResponse formSubmit(@Valid @RequestBody FormRequest formRequest) {
        return service.formSubmit(formRequest);
    }

    @GetMapping(path = "{applicationId}")
    public ApplicationResponse fetchOffers(@PathVariable("applicationId") UUID id) {
        return service.fetchOffers(id);
    }
}
