package com.java.bnpl.dues;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface DuesRepository extends JpaRepository<Dues,Long>{


Optional<Dues>  findById(Long id);
    
}