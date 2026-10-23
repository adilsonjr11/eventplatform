package br.com.itau.eventplatform.aplication.service;

import br.com.itau.eventplatform.api.request.EventRequest;
import br.com.itau.eventplatform.domain.model.Transaction;
import br.com.itau.eventplatform.domain.model.TransactionStatus;
import br.com.itau.eventplatform.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService; // <- Adicionado aqui

    @Transactional
    public void process(EventRequest event) {
        log.info(
                "CorrelationId={} TransactionId={} Verificando idempotência",
                event.correlationId(),
                event.transactionId()
        );

        // Validação de Idempotência via Redis
        if (idempotencyService.alreadyProcessed(event.transactionId())) {
            log.warn(
                    "CorrelationId={} TransactionId={} Transação duplicada detectada. Ignorando processamento.",
                    event.correlationId(),
                    event.transactionId()
            );
            return; // Interrompe para não processar nem salvar novamente
        }

        log.info(
                "CorrelationId={} TransactionId={} Salvando transação na base de dados",
                event.correlationId(),
                event.transactionId()
        );

        Transaction transaction = Transaction.builder()
                .transactionId(event.transactionId())
                .accountFrom(event.accountFrom())
                .accountTo(event.accountTo())
                .amount(event.amount())
                .status(TransactionStatus.PROCESSED)
                .createdAt(Instant.now())
                .build();

        transactionRepository.save(transaction);

        // Marca como processado no Redis após salvar com sucesso
        idempotencyService.markProcessed(event.transactionId());

        log.info(
                "CorrelationId={} TransactionId={} Transação salva com sucesso no PostgreSQL",
                event.correlationId(),
                event.transactionId()
        );
    }
}