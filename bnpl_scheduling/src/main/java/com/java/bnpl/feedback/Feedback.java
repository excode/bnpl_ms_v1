package com.java.bnpl.feedback;

import java.time.LocalDateTime;

import com.java.bnpl.config.Auditable;
import com.java.bnpl.interfaces.IDataAdd;
import com.java.bnpl.interfaces.IDataSearch;
import com.java.bnpl.interfaces.IDataUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class Feedback extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "feedback_sequence",
    sequenceName = "feedback_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "feedback_sequence"
)
    private Long id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Integer partnerId ;

@NotNull(groups = {IDataAdd.class},message = "rating required")
@DecimalMax(value="5",groups = {IDataAdd.class,IDataUpdate.class},message = "rating range error: maximum value allowed is  5")
private Double rating ;

@NotBlank(groups = {IDataAdd.class},message = "feedbackText required")
private String feedbackText ;

@NotNull(groups = {IDataAdd.class},message = "submissionDate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "submissionDate is not valid date")
private LocalDateTime submissionDate ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public Integer getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Integer val) {
        this.partnerId = val;
    }
            

    public Double getRating() {
        return rating;
    }
    public void setRating(Double val) {
        this.rating = val;
    }
            

    public String getFeedbackText() {
        return feedbackText;
    }
    public void setFeedbackText(String val) {
        this.feedbackText = val;
    }
            

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    public void setSubmissionDate(LocalDateTime val) {
        this.submissionDate = val;
    }
            


}

