package com.amazingbooks.issuerms.api;

import com.amazingbooks.issuerms.model.Book;
import com.amazingbooks.issuerms.model.Issuer;
import com.amazingbooks.issuerms.repo.IssuerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class IssuerResource {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String baseURL = "http://localhost:9091/books//";
    private static final Logger LOGGER = LoggerFactory.getLogger(IssuerResource.class);

    @Autowired
    private IssuerRepo issuerRepo;

    @GetMapping("/issuedbooks")
    public List<Issuer> getAllBooks() {
        LOGGER.info("Get All Books Issued");
        return issuerRepo.findAll();
    }

    @GetMapping("/issuedbooks/{id}")
    public ResponseEntity<Issuer> getBook(@PathVariable Integer id) {
        LOGGER.info("Get Issued Book for the ID:" , id);
        Optional<Issuer> bookFound =issuerRepo.findById(id);
        if (bookFound.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookFound.get());
    }

    @PostMapping("/issuebooks")
    ResponseEntity<Object> issueBook(@RequestBody Issuer issuer) throws URISyntaxException {
        LOGGER.info("Issuing Book:");
        getBookByISBN(issuer.getIsbn());


        //LOGGER.info("After REST Call:",book.getTotalCopies());
        //Issuer bookissuedSaved = issuerRepo.save(issuer);
        //return ResponseEntity.created(new URI(issuer.getId().toString())).body(bookissuedSaved);
        //LOGGER.info("Result:", result.toString());
       // return restTemplate.getForObject("http://bookms/books/{id}", Object.class,id);

       // return result.toString();
        return null;
    }

    private void getBookByISBN(String isbn) {
       // String response= restTemplate.getForObject("http://localhost:9091//booksbyisbn/{isbn}", String.class,isbn);

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9091//booksbyisbn/{isbn}", String.class,isbn);
        String productsJson = response.getBody();
        LOGGER.info("After REST Call:",productsJson.getBytes(StandardCharsets.UTF_8));

       // Book[] book= restTemplate.getForEntity("http://localhost:9091//booksbyisbn/{isbn}", Book[].class,isbn).getBody();
        //LOGGER.info("After REST Call");

    }

}

