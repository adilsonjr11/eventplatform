package br.com.itau.eventplatform.aplication.usecase.impl;

import br.com.itau.eventplatform.api.request.EventRequest;
import br.com.itau.eventplatform.aplication.service.IdempotencyService;
import br.com.itau.eventplatform.aplication.usecase.ProcessTransactionUseCase;
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
public class ProcessTransactionUseCaseImpl implements ProcessTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService;

    @Override
    @Transactional
    public void execute(EventRequest event) {
        log.info(
                "CorrelationId={} TransactionId={} Verificando idempotência",
                event.correlationId(),
                event.transactionId()
        );

        // 1. Validação de Idempotência (Redis)
        if (idempotencyService.alreadyProcessed(event.transactionId())) {
            log.warn(
                    "CorrelationId={} TransactionId={} Transação duplicada detectada. Ignorando processamento.",
                    event.correlationId(),
                    event.transactionId()
            );
            return;
        }

        log.info(
                "CorrelationId={} TransactionId={} Processando e salvando transação",
                event.correlationId(),
                event.transactionId()
        );

        // 2. Mapeamento para o modelo de domínio/entidade
        Transaction transaction = Transaction.builder()
                .transactionId(event.transactionId())
                .accountFrom(event.accountFrom())
                .accountTo(event.accountTo())
                .amount(event.amount())
                .status(TransactionStatus.PROCESSED)
                .createdAt(Instant.now())
                .build();

        // 3. Persistência no PostgreSQL
        transactionRepository.save(transaction);

        // 4. Marca como processado no Redis
        idempotencyService.markProcessed(event.transactionId());

        log.info(
                "CorrelationId={} TransactionId={} Transação salva com sucesso na base de dados",
                event.correlationId(),
                event.transactionId()
        );
    }
}