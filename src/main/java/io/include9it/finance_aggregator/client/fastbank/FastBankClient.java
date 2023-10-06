package io.include9it.finance_aggregator.client.fastbank;

import io.include9it.finance_aggregator.client.RestClient;
import io.include9it.finance_aggregator.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FastBankClient {

    private final RestClient<FastBankResponse> restClient;

    @Value("${fastbank.api.url}")
    private String apiUrl;

    public FastBankResponse submitApplication(FastBankApplication application) {
        return restClient.post(
                apiUrl,
                application,
                RestUtil.getHeader(),
                FastBankResponse.class
        );
    }

    public FastBankResponse fetchOffer(String applicationId) {
        var url = apiUrl + "/" + applicationId;

        return restClient.get(
                url,
                FastBankResponse.class,
                RestUtil.getHeader()
        );
    }
}
