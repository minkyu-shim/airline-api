package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
