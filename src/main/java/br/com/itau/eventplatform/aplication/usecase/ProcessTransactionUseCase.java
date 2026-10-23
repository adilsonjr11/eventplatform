package br.com.itau.eventplatform.aplication.usecase;

import br.com.itau.eventplatform.api.request.EventRequest;

public interface ProcessTransactionUseCase {
    void execute(EventRequest event);
}