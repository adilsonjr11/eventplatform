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
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final IdempotencyService idempotencyService;

    public void process(EventRequest event) {

        log.info("Recebido evento {}", event.transactionId());

        if (idempotencyService.alreadyProcessed(
                event.transactionId())) {

            log.warn(
                    "Evento duplicado {} ignorado",
                    event.transactionId());

            return;
        }

        Transaction transaction = new Transaction(
                event.transactionId(),
                event.accountFrom(),
                event.accountTo(),
                event.amount(),
                TransactionStatus.PROCESSED,
                Instant.now()
        );

        repository.save(transaction);

        idempotencyService.markProcessed(
                event.transactionId());

        log.info(
                "Transação {} persistida",
                event.transactionId());
    }
}