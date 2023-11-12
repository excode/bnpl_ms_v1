package com.java.bnpl.partner;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long>{


    @Query("SELECT  s FROM Partner s WHERE s.email = ?1")
    Optional<Partner> findPartnerByEmail(String email) ;
    @Query("SELECT  s FROM Partner s WHERE s.email = ?1  and s.id != ?2")
    Optional<Partner> findPartnerByEmailExcludeSelf(String email,Long id) ;
       

    @Query("SELECT  s FROM Partner s WHERE s.phone = ?1")
    Optional<Partner> findPartnerByPhone(String phone) ;
    @Query("SELECT  s FROM Partner s WHERE s.phone = ?1  and s.id != ?2")
    Optional<Partner> findPartnerByPhoneExcludeSelf(String phone,Long id) ;
       
Optional<Partner>  findById(Long id);
    
}