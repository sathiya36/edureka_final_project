package com.amazingbooks.issuerms.repo;

import com.amazingbooks.issuerms.model.Issuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerRepo extends JpaRepository<Issuer,Integer> {
}
