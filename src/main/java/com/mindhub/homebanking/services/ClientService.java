package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClienDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClienDTO> getClientsDto();
    Client getclientCurrent(Authentication authentication);
    ClienDTO getclientDto(long id);
    void saveClient(Client client);
    Client getClientByEmail(String email);
    Client getClientById(long id);
    void deleteClient(long id);
}