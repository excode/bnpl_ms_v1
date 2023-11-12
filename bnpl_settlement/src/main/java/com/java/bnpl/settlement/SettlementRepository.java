package com.java.bnpl.settlement;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface SettlementRepository extends JpaRepository<Settlement,Long>{


Optional<Settlement>  findById(Long id);
    
}