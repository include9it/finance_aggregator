package io.include9it.finance_aggregator.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.include9it.finance_aggregator.client.fastbank.FastBankService;
import io.include9it.finance_aggregator.client.solidbank.SolidBankService;
import io.include9it.finance_aggregator.dto.ApplicationResponse;
import io.include9it.finance_aggregator.dto.ApplicationStatus;
import io.include9it.finance_aggregator.service.FinancingService;
import io.include9it.finance_aggregator.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class FormControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FinancingService service;


    @Test
    void shouldReturnSuccessOnFormSubmit() throws Exception {
        var json = TestUtil.readResourceFile("response/form_submit_response.json");
        ApplicationResponse response = objectMapper.readValue(json, ApplicationResponse.class);

        when(service.formSubmit(any())).thenReturn(response);

        var request = post("/api/v1/forms")
                .content(TestUtil.readResourceFile("request/valid_form_request.json"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id().toString()))
                .andExpect(jsonPath("$.applications", notNullValue()))
                .andExpect(jsonPath("$.applications[0].serviceName").value(FastBankService.class.getSimpleName()))
                .andExpect(jsonPath("$.applications[0].status").value(ApplicationStatus.DRAFT.toString()))
                .andExpect(jsonPath("$.applications[0].offer").doesNotExist())
                .andExpect(jsonPath("$.applications[1].serviceName").value(SolidBankService.class.getSimpleName()))
                .andExpect(jsonPath("$.applications[1].status").value(ApplicationStatus.DRAFT.toString()))
                .andExpect(jsonPath("$.applications[1].offer").doesNotExist());
    }

    @Test
    void shouldReturnBadRequestToEmptyBody() throws Exception {
        var request = post("/api/v1/forms")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestToIncorrectBody() throws Exception {
        var request = post("/api/v1/forms")
                .content(TestUtil.readResourceFile("request/invalid_form_request.json"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalErrorAfterRuntimeException() throws Exception {
        when(service.formSubmit(any())).thenThrow(RuntimeException.class);

        var request = post("/api/v1/forms")
                .content(TestUtil.readResourceFile("request/valid_form_request.json"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldReturnRequestedApplicationById() throws Exception {
        var json = TestUtil.readResourceFile("response/application_response.json");
        objectMapper.registerModule(new JavaTimeModule());
        ApplicationResponse response = objectMapper.readValue(json, ApplicationResponse.class);

        when(service.fetchOffers(any())).thenReturn(response);

        var request = get("/api/v1/forms/{applicationId}", response.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.id().toString()))
                .andExpect(jsonPath("$.applications[0].offer", notNullValue()))
                .andExpect(jsonPath("$.applications[1].offer").doesNotExist());
    }
}
