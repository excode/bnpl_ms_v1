package com.java.bnpl.installments;

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
public class Installments extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "installments_sequence",
    sequenceName = "installments_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "installments_sequence"
)
    private Long id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "transactionID required")
private Integer transactionID ;

private Integer installmentNumber ;

private Integer installmentAmount ;

@NotNull(groups = {IDataAdd.class},message = "dueDate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "dueDate is not valid date")
private LocalDateTime dueDate ;

@NotNull(groups = {IDataAdd.class},message = "latePaymentFee required")
private Double latePaymentFee ;

@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Integer partnerId ;

@NotNull(groups = {IDataAdd.class},message = "status required")
private Integer status ;

@NotBlank(groups = {IDataAdd.class},message = "customerId required")
private String customerId ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public Integer getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(Integer val) {
        this.transactionID = val;
    }
            

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }
    public void setInstallmentNumber(Integer val) {
        this.installmentNumber = val;
    }
            

    public Integer getInstallmentAmount() {
        return installmentAmount;
    }
    public void setInstallmentAmount(Integer val) {
        this.installmentAmount = val;
    }
            

    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDateTime val) {
        this.dueDate = val;
    }
            

    public Double getLatePaymentFee() {
        return latePaymentFee;
    }
    public void setLatePaymentFee(Double val) {
        this.latePaymentFee = val;
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
            

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String val) {
        this.customerId = val;
    }
            


}

