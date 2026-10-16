package br.com.itau.eventplatform.service;

import br.com.itau.eventplatform.aplication.service.IdempotencyService;
import br.com.itau.eventplatform.aplication.service.TransactionService;
import br.com.itau.eventplatform.api.request.EventRequest;
import br.com.itau.eventplatform.domain.model.Transaction;
import br.com.itau.eventplatform.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private IdempotencyService idempotencyService;

    @InjectMocks
    private TransactionService service;

    @Test
    void shouldSaveTransactionWhenNotProcessed() {

        EventRequest request =
                new EventRequest(
                        "PIX-20261013-123456",
                        "CORR-20261013-ABC123",
                        "0001-12345-6",
                        "0001-98765-4",
                        new BigDecimal("150.00")
                );

        when(idempotencyService.alreadyProcessed(
                request.transactionId()))
                .thenReturn(false);

        service.process(request);

        verify(repository).save(
                any(Transaction.class));

        verify(idempotencyService)
                .markProcessed(
                        request.transactionId());
    }

    @Test
    void shouldIgnoreDuplicatedTransaction() {

        EventRequest request =
                new EventRequest(
                        "PIX-20261013-123456",
                        "CORR-20261013-ABC123",
                        "0001-12345-6",
                        "0001-98765-4",
                        new BigDecimal("150.00")
                );

        when(idempotencyService.alreadyProcessed(
                request.transactionId()))
                .thenReturn(true);

        service.process(request);

        verify(repository, never())
                .save(any());

        verify(idempotencyService, never())
                .markProcessed(any());
    }
}