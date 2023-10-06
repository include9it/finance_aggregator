package io.include9it.finance_aggregator.service;

import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.FormRequest;

public interface ApplicationService {

    Application submitApplication(FormRequest formRequest);

    Application fetchOffer(String applicationId);

    String getServiceName(); // Do not change service class name it stored in DB
}
