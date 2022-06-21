package com.mindhub.homebanking.DTO;

public class CardApplicationDTO {
    private String number;

    private int cvv;

    private double amount;

    private String descripcion;

    private String email;



    public CardApplicationDTO() {
    }


    public CardApplicationDTO(String number, int cvv, double amount, String descripcion, String email) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.descripcion = descripcion;
        this.email = email;
    }


    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEmail() {
        return email;
    }

}
