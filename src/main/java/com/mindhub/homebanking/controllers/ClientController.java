package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.DTO.ClienDTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.AccountType.AHORRO;
import static com.mindhub.homebanking.models.AccountType.CORRIENTE;
import static utils.AccountUtils.getRandomNumber;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;



    @GetMapping("/clients")
    public List<ClienDTO> getClientsdDTO() {
        return clientService.getClientsDto();
    }


    @GetMapping("/clients/{id}")
    public ClienDTO getClient(@PathVariable Long id) {
        return clientService.getclientDto(id);
    }


    //la ruta para el nuevo servicio es /api/clients la misma ruta para obtener todos clientes
    @PostMapping(path = "/clients")//metodo post(ya no es http)(crea un objeto) diferencia a cada uno de manera automatica, el psch modifica parte del lobjeto
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientService.getClientByEmail(email) != null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }

        if (password.length() >=6){
            return new ResponseEntity<>("Long password, up to 6 characters", HttpStatus.FORBIDDEN);
        }


        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);

        Account newAccountAhorro = new Account(newClient, "VIN" + getRandomNumber(10000000,99999999), LocalDateTime.now(), 0,true, AHORRO);
        accountService.saveAccount(newAccountAhorro);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/clients/current")
    public ClienDTO getClient(Authentication authentication){
        Client client = clientService.getclientCurrent(authentication);
        return new ClienDTO(client);
    }


    @DeleteMapping("/clients/deleteClient")
    public ResponseEntity<Object> deleteClient (@RequestParam Long id){
        clientService.deleteClient(id);
        return new ResponseEntity<>("Usuario eliminado", HttpStatus.ACCEPTED);
    }


    @PatchMapping("/clients/modify/{id}")
    public ResponseEntity<Object> modifyClient(@PathVariable long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email){
        Client client = clientService.getClientById(id);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
            return new ResponseEntity<>("los datos no pueden estar vacios", HttpStatus.FORBIDDEN);
        }
        if (client == null){
            return new ResponseEntity<>("no se encontro el cliente", HttpStatus.FORBIDDEN);
        }

        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        clientService.saveClient(client);
        return new ResponseEntity<>("modify", HttpStatus.ACCEPTED);
    }

}