package br.com.itau.eventplatform.service;

import br.com.itau.eventplatform.api.request.EventRequest;
import br.com.itau.eventplatform.aplication.service.IdempotencyService;
import br.com.itau.eventplatform.aplication.service.TransactionService;
import br.com.itau.eventplatform.domain.model.Transaction;
import br.com.itau.eventplatform.domain.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private IdempotencyService idempotencyService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Deve salvar a transação e marcar como processada quando o evento for inédito")
    void shouldSaveTransactionWhenNotProcessed() {
        EventRequest request = new EventRequest(
                "PIX-20261013-123456",
                "CORR-20261013-ABC123",
                "acc-1",
                "acc-2",
                BigDecimal.TEN
        );

        when(idempotencyService.alreadyProcessed(anyString())).thenReturn(false);

        transactionService.process(request);

        verify(repository, times(1)).save(any(Transaction.class));
        verify(idempotencyService, times(1)).markProcessed("PIX-20261013-123456");
    }

    @Test
    @DisplayName("Deve ignorar, não salvar e não marcar quando a transação já foi processada")
    void shouldIgnoreDuplicatedTransaction() {
        // Arrange
        EventRequest request = new EventRequest(
                "PIX-20261013-123456",
                "CORR-20261013-ABC123",
                "acc-1",
                "acc-2",
                BigDecimal.TEN
        );

        // Força explicitamente o retorno para TRUE (evento já processado)
        doReturn(true).when(idempotencyService).alreadyProcessed(anyString());

        // Act
        transactionService.process(request);

        // Assert
        verify(repository, never()).save(any(Transaction.class));
        verify(idempotencyService, never()).markProcessed(anyString());
    }
}