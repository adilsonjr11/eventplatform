package br.com.itau.eventplatform.infrastructure.kafka;

import br.com.itau.eventplatform.aplication.usecase.ProcessTransactionUseCase;
import br.com.itau.eventplatform.api.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final ProcessTransactionUseCase processTransactionUseCase;

    @KafkaListener(
            topics = "transaction-events",
            groupId = "event-platform")
    public void consume(EventRequest event) {

        processTransactionUseCase.execute(event);
    }
}