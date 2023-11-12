package com.java.bnpl.feedback;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long>{


Optional<Feedback>  findById(Long id);
    
}