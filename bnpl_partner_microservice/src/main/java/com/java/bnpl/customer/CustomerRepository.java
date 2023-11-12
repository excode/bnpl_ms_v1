package com.java.bnpl.customer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{


Optional<Customer>  findById(Long id);
    
}