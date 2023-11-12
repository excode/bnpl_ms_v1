package com.java.bnpl.transaction;

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
public class Transaction extends Auditable {

    @Id

    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    private Long id;

    @NotNull(groups = { IDataAdd.class }, message = "customerId required")
    private Integer customerId;

    @NotNull(groups = { IDataAdd.class }, message = "transactionDate required")
    @FutureOrPresent(groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "transactionDate is not valid date")
    private LocalDateTime transactionDate;

    private Double transactionAmount;

    @NotNull(groups = { IDataAdd.class }, message = "status required")
    private Integer status;

    @NotBlank(groups = { IDataAdd.class }, message = "paymentMethod required")
    private String paymentMethod;

    @NotNull(groups = { IDataAdd.class }, message = "nextPaymentDate required")
    @FutureOrPresent(groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "nextPaymentDate is not valid date")
    private LocalDateTime nextPaymentDate;

    @NotNull(groups = { IDataAdd.class }, message = "partnerId required")
    private Integer partnerId;

    @NotNull(groups = { IDataAdd.class }, message = "plan required")
    private Integer plan;

    public Number getId() {
        return id;
    }

    public void setId(Long val) {
        this.id = val;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer val) {
        this.customerId = val;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime val) {
        this.transactionDate = val;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double val) {
        this.transactionAmount = val;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer val) {
        this.status = val;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String val) {
        this.paymentMethod = val;
    }

    public LocalDateTime getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDateTime val) {
        this.nextPaymentDate = val;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer val) {
        this.partnerId = val;
    }

    public Integer getPlan() {
        return plan;
    }

    public void setPlan(Integer val) {
        this.plan = val;
    }

}
