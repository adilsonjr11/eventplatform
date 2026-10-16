package br.com.itau.eventplatform.domain.repository;

import br.com.itau.eventplatform.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, String> {
}
