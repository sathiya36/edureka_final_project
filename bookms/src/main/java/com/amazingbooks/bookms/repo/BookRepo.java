package com.amazingbooks.bookms.repo;

import com.amazingbooks.bookms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book,Integer> {

    List<Book> findByisbn(String isbn);
  //  Object findByisbn(String isbn);

}
