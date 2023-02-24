package com.amazingbooks.issuermsclient;

import com.amazingbooks.bookms.model.Book;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class IssuerClientResource {

    private static final Logger LOGGER= LoggerFactory.getLogger(IssuerClientResource.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("issuer-client/issuedbooks")
    @HystrixCommand(fallbackMethod = "getIssuedBooksFromFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            })
    public Object getIssuedBooksFromIssuerms() {
        LOGGER.info("Calling Issuer Microservice - To Get All Issued Books");
        return restTemplate.getForObject("http://issuerms/issuedbooks", Object.class);
    }

    @PostMapping("/issuebooks")
    @HystrixCommand(fallbackMethod = "issuebooksFromFallback",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
            }
    )
    public Object issuebooksinissuerms(@RequestBody Book book) {
        LOGGER.info("Calling Issuer Microservice - To Issue a book");
        return restTemplate.postForObject("http://issuerms/issuebooks",book,Book.class);

    }


    private Object getIssuedBooksFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object issuebooksFromFallback() {
        LOGGER.error("Circuit Breaker Invoked");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }


}
