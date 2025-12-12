package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    // Custom SQL to force the ID change because Hibernate does not allow
    // you to change the Primary Key of an active entity using a setter.
    @Modifying
    @Query("UPDATE Client c SET c.passportNumber = :newId WHERE c.passportNumber = :oldId")
    void updatePassportNumber(String oldId, String newId);
}
