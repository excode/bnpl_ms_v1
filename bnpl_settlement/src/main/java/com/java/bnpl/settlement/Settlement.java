package com.java.bnpl.settlement;

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
public class Settlement extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "settlement_sequence",
    sequenceName = "settlement_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "settlement_sequence"
)
    private Long id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "account required")
private String account ;

@NotNull(groups = {IDataAdd.class},message = "duedate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "duedate is not valid date")
private LocalDateTime duedate ;

@NotNull(groups = {IDataAdd.class},message = "status required")
private Integer status ;

@NotNull(groups = {IDataAdd.class},message = "partnerId required")
private Integer partnerId ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public String getAccount() {
        return account;
    }
    public void setAccount(String val) {
        this.account = val;
    }
            

    public LocalDateTime getDuedate() {
        return duedate;
    }
    public void setDuedate(LocalDateTime val) {
        this.duedate = val;
    }
            

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer val) {
        this.status = val;
    }
            

    public Integer getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Integer val) {
        this.partnerId = val;
    }
            


}

