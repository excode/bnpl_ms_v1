package com.java.bnpl.users;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface UsersRepository extends JpaRepository<Users,String>{


    @Query("SELECT  s FROM Users s WHERE s.email = ?1")
    Optional<Users> findUsersByEmail(String email) ;
    @Query("SELECT  s FROM Users s WHERE s.email = ?1  and s.id != ?2")
    Optional<Users> findUsersByEmailExcludeSelf(String email,String id) ;
       

    @Query("SELECT  s FROM Users s WHERE s.mobile = ?1")
    Optional<Users> findUsersByMobile(String mobile) ;
    @Query("SELECT  s FROM Users s WHERE s.mobile = ?1  and s.id != ?2")
    Optional<Users> findUsersByMobileExcludeSelf(String mobile,String id) ;
       
Optional<Users>  findById(String id);
    
}