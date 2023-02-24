package com.amazingbooks.bookmsclient;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.amazingbooks.bookms.model.Book;
@RestController
public class BookmsClientResource {

    private static final Logger LOGGER= LoggerFactory.getLogger(BookmsClientResource.class);

    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/books-client/books")
    @HystrixCommand(fallbackMethod = "getBooksFromFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            })
    public Object getBooksFromBooksms() {
        LOGGER.info("Calling Books Microservice - To Get All Books");
        return restTemplate.getForObject("http:bookms/books", Object.class);
    }

    @GetMapping("/books-client/books/{id}")
    @HystrixCommand(fallbackMethod = "getbooksByIdFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object getbooksByIdFrombookms(@PathVariable Integer id) {
        LOGGER.info("Calling books Microservice - To Get book by Id");
        return restTemplate.getForObject("http:bookms/books/{id}", Object.class,id);
    }

    @PostMapping("/books-client/books")
    @HystrixCommand(fallbackMethod = "createbooksFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object createbooksinbookms(@RequestBody Book book) {
        LOGGER.info("Calling books Microservice - To Create a book");
        return restTemplate.postForObject("http:bookms/books",book,Book.class);

    }

    @PutMapping("/books-client/books/{id}")
    @HystrixCommand(fallbackMethod = "updatebooksFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object updatebookinbookms(@PathVariable Integer id,@RequestBody Book book) {
        LOGGER.info("Calling books Microservice - To Update a book");
        restTemplate.put("http:bookms/books/{id}",book,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/books-client/updatebooksbyisbn/{isbn}")
    @HystrixCommand(fallbackMethod = "updatebooksbyisbnFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object updatebookbyisbninbookms(@PathVariable Integer isbn,@RequestBody Book book) {
        LOGGER.info("Calling books Microservice - To Update a book by isbn");
        restTemplate.put("http:bookms/updatebooksbyisbn/{isbn}",book,isbn);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/books-client/books/{id}")
    @HystrixCommand(fallbackMethod = "deletebooksFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object deletebooksByIdFrombookms(@PathVariable Integer id) {
        LOGGER.info("Calling books Microservice - To Delete book by Id");
        restTemplate.delete("http:bookms/books/"+id,Object.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/booksbyisbn/{isbn}")
    @HystrixCommand(fallbackMethod = "getbooksByIsbnFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object getbooksByIsbnFrombookms(@PathVariable String isbn) {
        LOGGER.info("Calling books Microservice - To Get book by ISBN");
        return restTemplate.getForObject("http:bookms/booksbyisbn/{isbn}", Object.class,isbn);
    }



    Fall Back Methods

    private Object getBooksFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object getbooksByIdFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object createbooksFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object updatebooksFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object updatebooksbyisbnFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object deletebooksFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
