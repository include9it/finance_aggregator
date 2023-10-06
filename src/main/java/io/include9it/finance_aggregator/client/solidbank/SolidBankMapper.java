package io.include9it.finance_aggregator.client.solidbank;

import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.FormRequest;
import org.springframework.stereotype.Component;

@Component
public class SolidBankMapper {

    SolidBankApplication map(FormRequest formRequest) {
        return new SolidBankApplication(
                formRequest.phone(),
                formRequest.email(),
                formRequest.monthlyIncome(),
                formRequest.monthlyExpenses(),
                formRequest.maritalStatus(),
                formRequest.agreeToBeScored(),
                formRequest.amount()
        );
    }

    Application map(SolidBankResponse response, String serviceName) {
        return new Application(
                serviceName,
                response.applicationId(),
                response.status(),
                response.offer()
        );
    }
}
