package com.mindhub.homebanking.Repository;

;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource //crea los controladores para el repositorio
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}