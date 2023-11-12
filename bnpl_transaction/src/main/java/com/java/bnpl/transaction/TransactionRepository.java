package com.java.bnpl.transaction;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>{


Optional<Transaction>  findById(Long id);
    
}