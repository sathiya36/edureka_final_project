package com.amazingbooks.bookms.api;

import com.amazingbooks.bookms.model.Book;
import com.amazingbooks.bookms.repo.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class BookResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookResource.class);

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/hello")
    public String getHello() {
        return "Hello World !";
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        LOGGER.info("Get All Books");
        return bookRepo.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        LOGGER.info("Get Book for the ISBN:" , id);
        Optional<Book> bookFound =bookRepo.findById(id);
        if (bookFound.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookFound.get());
    }

    @GetMapping("/booksbyisbn/{isbn}")
    public ResponseEntity<List<Book>> getBook(@PathVariable String isbn) {
        LOGGER.info("Get Book for the ISBN:" , isbn);
        ResponseEntity<List<Book>> tempvar=new ResponseEntity<List<Book>>(bookRepo.findByisbn(isbn),HttpStatus.OK);
        //return new ResponseEntity<List<Book>>(bookRepo.findByisbn(isbn),HttpStatus.OK);
        LOGGER.info("Response",tempvar.getBody());
        return tempvar;

    }
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) throws URISyntaxException {
        LOGGER.info("Adding New Book:" , book);
        Book bookSaved = bookRepo.save(book);
        return ResponseEntity.created(new URI(book.getId().toString())).body(bookSaved);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook (@PathVariable Integer id,@RequestBody Book book) {
        Optional<Book> bookFound =bookRepo.findById(id);

        if (bookFound.isPresent()) {
            Book _book = bookFound.get();
            _book.setIsbn(book.getIsbn());
            _book.setTitle(book.getTitle());
            _book.setAuthor(book.getAuthor());
            _book.setIssuedCopies(book.getIssuedCopies());
            _book.setTotalCopies(book.getTotalCopies());
            _book.setPublishedDate(book.getPublishedDate());
            return new ResponseEntity<>(bookRepo.save(_book), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updatebooksbyisbn/{isbn}")
    public ResponseEntity<Book> updateBookByISBN (@PathVariable String isbn,@RequestBody Book bookreq) {
        Book[] bookFound = bookRepo.findByisbn(isbn).toArray(new Book[0]);
        int Listsize=bookFound.length;
        if (Listsize>=1)
        {
            for (Book book : bookFound) {
                book.setIssuedCopies(book.getIssuedCopies()+bookreq.getIssuedCopies());
                book.setTotalCopies(book.getTotalCopies()-bookreq.getIssuedCopies());
                bookRepo.save(book);
            }

            return new ResponseEntity<>(bookFound[0], HttpStatus.OK);
        } else {
            LOGGER.info("Book Not Found:");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/books/{id}")
    public ResponseEntity<Book> deleteBook (@PathVariable Integer id) {
        try {
            bookRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
