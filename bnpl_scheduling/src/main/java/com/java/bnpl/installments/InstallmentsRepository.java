package com.java.bnpl.installments;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface InstallmentsRepository extends JpaRepository<Installments,Long>{


Optional<Installments>  findById(Long id);
    
}