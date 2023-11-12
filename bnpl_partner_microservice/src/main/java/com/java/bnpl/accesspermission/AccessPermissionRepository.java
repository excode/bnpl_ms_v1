package com.java.bnpl.accesspermission;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface AccessPermissionRepository extends JpaRepository<AccessPerm,Long>{


Optional<AccessPerm>  findById(Long id);
    
}