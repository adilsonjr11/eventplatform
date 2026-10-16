package br.com.itau.eventplatform.infrastructure.kafka;

import br.com.itau.eventplatform.api.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, EventRequest>
            kafkaTemplate;

    public void send(EventRequest event) {

        kafkaTemplate.send(
                "transaction-events",
                event.transactionId(),
                event
        );
    }
}
