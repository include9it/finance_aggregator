package io.include9it.finance_aggregator.client.fastbank;

import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.FormRequest;
import io.include9it.finance_aggregator.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FastBankService implements ApplicationService {

    private final FastBankClient client;
    private final FastBankMapper mapper;

    @Override
    public Application submitApplication(FormRequest formRequest) {
        FastBankApplication request = mapper.map(formRequest);
        FastBankResponse response = client.submitApplication(request);

        return mapper.map(response, getServiceName());
    }

    @Override
    public Application fetchOffer(String applicationId) {
        FastBankResponse response = client.fetchOffer(applicationId);

        return mapper.map(response, getServiceName());
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }
}
