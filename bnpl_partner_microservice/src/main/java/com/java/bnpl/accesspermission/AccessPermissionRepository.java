package com.java.bnpl.accesspermission;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface AccessPermissionRepository extends JpaRepository<AccessPermission,Long>{


Optional<AccessPermission>  findById(Long id);
    
}