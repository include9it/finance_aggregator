package io.include9it.finance_aggregator.client.solidbank;

import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.FormRequest;
import io.include9it.finance_aggregator.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolidBankService implements ApplicationService {

    private final SolidBankClient client;
    private final SolidBankMapper mapper;

    @Override
    public Application submitApplication(FormRequest formRequest) {
        SolidBankApplication request = mapper.map(formRequest);
        SolidBankResponse response = client.submitApplication(request);

        return mapper.map(response, getServiceName());
    }

    @Override
    public Application fetchOffer(String applicationId) {
        SolidBankResponse response = client.fetchOffer(applicationId);

        return mapper.map(response, getServiceName());
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }
}
