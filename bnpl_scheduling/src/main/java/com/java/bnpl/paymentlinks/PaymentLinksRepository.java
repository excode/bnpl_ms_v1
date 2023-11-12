package com.java.bnpl.paymentlinks;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface PaymentLinksRepository extends JpaRepository<PaymentLinks,Long>{


Optional<PaymentLinks>  findById(Long id);
    
}