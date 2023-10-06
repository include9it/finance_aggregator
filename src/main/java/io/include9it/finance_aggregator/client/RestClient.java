package io.include9it.finance_aggregator.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestClient<T> {
    private final RestTemplate restTemplate;

    public T get(String endpoint, Class<T> responseType, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    public List<T> getList(String endpoint, ParameterizedTypeReference<List<T>> responseType, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<T>> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    public <V> T post(String endpoint, V requestBody, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<V> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, responseType);
        return response.getBody();
    }

    public <V> void put(String endpoint, V requestBody, HttpHeaders headers) {
        HttpEntity<V> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(endpoint, HttpMethod.PUT, entity, Void.class);
    }

    public void delete(String endpoint, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, Void.class);
    }
}
