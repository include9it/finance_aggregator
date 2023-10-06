package io.include9it.finance_aggregator.client.solidbank;

import io.include9it.finance_aggregator.client.RestClient;
import io.include9it.finance_aggregator.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolidBankClient {

    private final RestClient<SolidBankResponse> restClient;

    @Value("${solidbank.api.url}")
    private String apiUrl;

    public SolidBankResponse submitApplication(SolidBankApplication application) {
        return restClient.post(
                apiUrl,
                application,
                RestUtil.getHeader(),
                SolidBankResponse.class
        );
    }

    public SolidBankResponse fetchOffer(String applicationId) {
        var url = apiUrl + "/" + applicationId;

        return restClient.get(
                url,
                SolidBankResponse.class,
                RestUtil.getHeader()
        );
    }
}
