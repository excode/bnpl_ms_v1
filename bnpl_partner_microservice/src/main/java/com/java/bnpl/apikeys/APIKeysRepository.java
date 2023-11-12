package com.java.bnpl.apikeys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface APIKeysRepository extends JpaRepository<APIKeys,Long>{


Optional<APIKeys>  findById(Long id);
    
}