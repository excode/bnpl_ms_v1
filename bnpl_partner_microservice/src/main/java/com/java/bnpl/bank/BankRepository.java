package com.java.bnpl.bank;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface BankRepository extends JpaRepository<Bank,Long>{


Optional<Bank>  findById(Long id);
    
}