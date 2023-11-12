package com.java.bnpl.plan;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long>{


Optional<Plan>  findById(Long id);
    
}