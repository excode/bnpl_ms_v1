package com.java.bnpl.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{


Optional<Role>  findById(Long id);
    
}