package com.java.bnpl.paymentlinks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import java.time.format.DateTimeParseException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT

 import jakarta.persistence.Id;
 import jakarta.persistence.SequenceGenerator;
 import jakarta.persistence.Table;
 import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.GenerationType;
     
import jakarta.persistence.Column;
import com.java.bnpl.config.Auditable;
import com.java.bnpl.interfaces.IDataAdd;
import com.java.bnpl.interfaces.IDataSearch;
import com.java.bnpl.interfaces.IDataUpdate;
import com.java.bnpl.validation.ValidDate;
@Entity
@Table
@NoArgsConstructor
public class PaymentLinks extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "paymentlinks_sequence",
    sequenceName = "paymentlinks_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "paymentlinks_sequence"
)
    private Long id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "transactionID required")
private Integer transactionID ;

@NotNull(groups = {IDataAdd.class},message = "customerID required")
private Integer customerID ;

@NotBlank(groups = {IDataAdd.class},message = "paymentLinkURL required")
private String paymentLinkURL ;

@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Double partnerId ;

@NotNull(groups = {IDataAdd.class},message = "paymentAmount required")
private Double paymentAmount ;

@NotNull(groups = {IDataAdd.class},message = "status required")
private Integer status ;



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
            

    public Integer getCustomerID() {
        return customerID;
    }
    public void setCustomerID(Integer val) {
        this.customerID = val;
    }
            

    public String getPaymentLinkURL() {
        return paymentLinkURL;
    }
    public void setPaymentLinkURL(String val) {
        this.paymentLinkURL = val;
    }
            

    public Double getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Double val) {
        this.partnerId = val;
    }
            

    public Double getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(Double val) {
        this.paymentAmount = val;
    }
            

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer val) {
        this.status = val;
    }
            


}

