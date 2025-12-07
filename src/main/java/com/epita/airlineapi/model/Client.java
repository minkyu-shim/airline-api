package com.epita.airlineapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Client {

    @Id
    private String passportNumber;

    public Client() {

    }

    public Client(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(passportNumber, client.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(passportNumber);
    }
}
