package com.mindhub.homebanking.Repository;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource //crea los controladores para el repositorio
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByNumber(String number);

}