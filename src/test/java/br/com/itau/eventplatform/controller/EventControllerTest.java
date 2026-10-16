package br.com.itau.eventplatform.controller;

import br.com.itau.eventplatform.api.controller.EventController;
import br.com.itau.eventplatform.api.request.EventRequest;
import br.com.itau.eventplatform.infrastructure.kafka.EventProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventProducer producer;

    @Test
    void shouldAcceptTransactionEvent() throws Exception {

        EventRequest request =
                new EventRequest(
                        "PIX-20261013-123456789",
                        "CORR-20261013-ABC123",
                        "0001-12345-6",
                        "0001-98765-4",
                        new BigDecimal("150.00")
                );

        mockMvc.perform(
                        post("/api/v1/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isAccepted());
    }
}