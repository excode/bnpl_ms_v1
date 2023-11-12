package com.java.bnpl.dues;

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
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class Dues extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "dues_sequence",
    sequenceName = "dues_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "dues_sequence"
)
    private Long id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "customerID required")
private Integer customerID ;

@NotNull(groups = {IDataAdd.class},message = "transactionID required")
private Double transactionID ;

@NotNull(groups = {IDataAdd.class},message = "dueAmount required")
private Double dueAmount ;

@NotNull(groups = {IDataAdd.class},message = "dueDate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "dueDate is not valid date")
private LocalDateTime dueDate ;

@NotBlank(groups = {IDataAdd.class},message = "latePaymentFeePolicy required")
private String latePaymentFeePolicy ;

@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Integer partnerId ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public Integer getCustomerID() {
        return customerID;
    }
    public void setCustomerID(Integer val) {
        this.customerID = val;
    }
            

    public Double getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(Double val) {
        this.transactionID = val;
    }
            

    public Double getDueAmount() {
        return dueAmount;
    }
    public void setDueAmount(Double val) {
        this.dueAmount = val;
    }
            

    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDateTime val) {
        this.dueDate = val;
    }
            

    public String getLatePaymentFeePolicy() {
        return latePaymentFeePolicy;
    }
    public void setLatePaymentFeePolicy(String val) {
        this.latePaymentFeePolicy = val;
    }
            

    public Integer getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Integer val) {
        this.partnerId = val;
    }
            


}

