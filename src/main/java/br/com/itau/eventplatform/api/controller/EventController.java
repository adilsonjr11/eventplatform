package br.com.itau.eventplatform.api.controller;

import br.com.itau.eventplatform.api.request.EventRequest;
import br.com.itau.eventplatform.infrastructure.kafka.EventProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventProducer producer;

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody EventRequest request) {

        log.info(
                "CorrelationId={} TransactionId={} Event received",
                request.correlationId(),
                request.transactionId()
        );

        producer.send(request);

        return ResponseEntity.accepted().build();
    }
}