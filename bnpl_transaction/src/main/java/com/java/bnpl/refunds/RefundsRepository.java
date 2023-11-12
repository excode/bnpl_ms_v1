package com.java.bnpl.refunds;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface RefundsRepository extends JpaRepository<Refunds,Long>{


Optional<Refunds>  findById(Long id);
    
}