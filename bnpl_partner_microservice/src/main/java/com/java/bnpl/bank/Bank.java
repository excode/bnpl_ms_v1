package com.java.bnpl.bank;

import com.java.bnpl.config.Auditable;
import com.java.bnpl.interfaces.IDataAdd;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class Bank extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "bank_sequence",
    sequenceName = "bank_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "bank_sequence"
)
    private Long id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "name required")
private String name ;

@NotBlank(groups = {IDataAdd.class},message = "accountName required")
private String accountName ;

@NotBlank(groups = {IDataAdd.class},message = "swift required")
private String swift ;

@NotNull(groups = {IDataAdd.class},message = "accountNumber required")
private Double accountNumber ;

@NotBlank(groups = {IDataAdd.class},message = "city required")
private String city ;

@NotBlank(groups = {IDataAdd.class},message = "country required")
private String country ;

@NotBlank(groups = {IDataAdd.class},message = "address required")
private String address ;

@NotNull(groups = {IDataAdd.class},message = "postcode required")
private Double postcode ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public String getName() {
        return name;
    }
    public void setName(String val) {
        this.name = val;
    }
            

    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String val) {
        this.accountName = val;
    }
            

    public String getSwift() {
        return swift;
    }
    public void setSwift(String val) {
        this.swift = val;
    }
            

    public Double getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(Double val) {
        this.accountNumber = val;
    }
            

    public String getCity() {
        return city;
    }
    public void setCity(String val) {
        this.city = val;
    }
            

    public String getCountry() {
        return country;
    }
    public void setCountry(String val) {
        this.country = val;
    }
            

    public String getAddress() {
        return address;
    }
    public void setAddress(String val) {
        this.address = val;
    }
            

    public Double getPostcode() {
        return postcode;
    }
    public void setPostcode(Double val) {
        this.postcode = val;
    }
            


}

