package com.java.bnpl.refunds;

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
public class Refunds extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "refunds_sequence",
    sequenceName = "refunds_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "refunds_sequence"
)
    private Long id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "transactionID required")
private String transactionID ;

@NotNull(groups = {IDataAdd.class},message = "refundAmount required")
private Double refundAmount ;

@NotBlank(groups = {IDataAdd.class},message = "reason required")
private String reason ;

@NotNull(groups = {IDataAdd.class},message = "refundDate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "refundDate is not valid date")
private LocalDateTime refundDate ;

@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Integer partnerId ;

@NotNull(groups = {IDataAdd.class},message = "status required")
private Integer status ;

@NotNull(groups = {IDataAdd.class},message = "customerId required")
private Integer customerId ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public String getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(String val) {
        this.transactionID = val;
    }
            

    public Double getRefundAmount() {
        return refundAmount;
    }
    public void setRefundAmount(Double val) {
        this.refundAmount = val;
    }
            

    public String getReason() {
        return reason;
    }
    public void setReason(String val) {
        this.reason = val;
    }
            

    public LocalDateTime getRefundDate() {
        return refundDate;
    }
    public void setRefundDate(LocalDateTime val) {
        this.refundDate = val;
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
            

    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer val) {
        this.customerId = val;
    }
            


}

