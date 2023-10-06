package io.include9it.finance_aggregator.client.fastbank;

import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.FormRequest;
import org.springframework.stereotype.Component;

@Component
public class FastBankMapper {

    FastBankApplication map(FormRequest formRequest) {
        return new FastBankApplication(
                formRequest.phone(),
                formRequest.email(),
                formRequest.monthlyIncome(),
                formRequest.monthlyExpenses(),
                formRequest.dependents(),
                formRequest.agreeToDataSharing(),
                formRequest.amount()
        );
    }

    Application map(FastBankResponse response, String serviceName) {
        return new Application(
                serviceName,
                response.applicationId(),
                response.status(),
                response.offer()
        );
    }
}
