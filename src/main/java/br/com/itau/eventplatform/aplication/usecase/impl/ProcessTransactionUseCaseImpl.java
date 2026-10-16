package br.com.itau.eventplatform.aplication.usecase.impl;

import br.com.itau.eventplatform.aplication.service.TransactionService;
import br.com.itau.eventplatform.aplication.usecase.ProcessTransactionUseCase;
import br.com.itau.eventplatform.api.request.EventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessTransactionUseCaseImpl
        implements ProcessTransactionUseCase {

    private final TransactionService transactionService;

    @Override
    public void execute(EventRequest event) {

        log.info(
                "Iniciando processamento da transação {}",
                event.transactionId());

        transactionService.process(event);

        log.info(
                "Finalizado processamento da transação {}",
                event.transactionId());
    }
}