package com.amazingbooks.issuerms.api;

import com.amazingbooks.issuerms.model.Book;
import com.amazingbooks.issuerms.model.Issuer;
import com.amazingbooks.issuerms.repo.IssuerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class IssuerResource {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String baseURL = "http://localhost:9091/books//";
    private static final Logger LOGGER = LoggerFactory.getLogger(IssuerResource.class);

    private static int total;

    @Autowired
    private IssuerRepo issuerRepo;

    @GetMapping("/issuedbooks")
    public List<Issuer> getAllBooks() {
        LOGGER.info("Get All Books Issued");
        return issuerRepo.findAll();
    }

    @GetMapping("/issuedbooks/{id}")
    public ResponseEntity<Issuer> getBook(@PathVariable Integer id) {
        LOGGER.info("Get Issued Book for the ID:", id);
        Optional<Issuer> bookFound = issuerRepo.findById(id);
        if (bookFound.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookFound.get());
    }

    @PostMapping("/issuebooks")
    public ResponseEntity<Issuer> issueBook(@RequestBody Issuer issuer) throws URISyntaxException {
        LOGGER.info("Issuing Book:");
        int totalBooksAvailable = 0;
        getBookByISBN(issuer.getIsbn());
        totalBooksAvailable=total;
        if (issuer.getNoOfCopies()<=totalBooksAvailable) {
            LOGGER.info("Book Available for Issuing :");
            Issuer issuersaved=issuerRepo.save(issuer);
            LOGGER.info("Issuer Details Saved :");
           updateBook(issuer.getIsbn(),issuer.getNoOfCopies());
        }

        else {
            LOGGER.warn("Book Not Available for Issuing :");

        }

        return null;
    }

    private String updateBook(String isbn, int noOfCopies) {
        Map map = new HashMap<String, String>();
        map.put("isbn", isbn);
        map.put("issuedCopies", noOfCopies);
        LOGGER.info("Update call :");
        restTemplate.put("http://bookms-client:8091/books-client/updatebooksbyisbn/{isbn}",map,isbn);
        LOGGER.info("Update call Sucess:");
        return null;
    }

    public int getBookByISBN(String isbn) {
        Book[] result = restTemplate.getForEntity("http://bookms-client:9091/books-client/booksbyisbn/{isbn}", Book[].class, isbn).getBody();
            LOGGER.info("After REST Call Sting:");
            //LOGGER.info("After REST Call Sting:",result[0].
             total = result[0].getTotalCopies();
        return total;
    }




}