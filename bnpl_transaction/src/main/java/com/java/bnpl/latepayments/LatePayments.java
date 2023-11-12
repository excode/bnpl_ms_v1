package com.java.bnpl.latepayments;

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
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class LatePayments extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "latepayments_sequence",
    sequenceName = "latepayments_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "latepayments_sequence"
)
    private Long id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "customerID required")
private Integer customerID ;

@NotNull(groups = {IDataAdd.class},message = "transactionID required")
private Integer transactionID ;

@NotNull(groups = {IDataAdd.class},message = "latePaymentAmount required")
private Double latePaymentAmount ;

@NotNull(groups = {IDataAdd.class},message = "latePaymentDate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "latePaymentDate is not valid date")
private LocalDateTime latePaymentDate ;

@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Integer partnerId ;

@NotNull(groups = {IDataAdd.class},message = "status required")
private Integer status ;



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
            

    public Integer getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(Integer val) {
        this.transactionID = val;
    }
            

    public Double getLatePaymentAmount() {
        return latePaymentAmount;
    }
    public void setLatePaymentAmount(Double val) {
        this.latePaymentAmount = val;
    }
            

    public LocalDateTime getLatePaymentDate() {
        return latePaymentDate;
    }
    public void setLatePaymentDate(LocalDateTime val) {
        this.latePaymentDate = val;
    }
            

    public Integer getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Integer val) {
        this.partnerId = val;
    }
            

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer val) {
        this.status = val;
    }
            


}

