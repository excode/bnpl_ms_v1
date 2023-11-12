package com.java.bnpl.users;

import com.java.bnpl.config.Auditable;
import com.java.bnpl.interfaces.IDataAdd;
import com.java.bnpl.interfaces.IDataUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class Users extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "users_sequence",
    sequenceName = "users_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "users_sequence"
)
    private String id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "userType required")
private Integer userType ;

@NotBlank(groups = {IDataAdd.class},message = "lastname required")
@Size(min=1,max=50,groups = {IDataAdd.class,IDataUpdate.class},message = "lastname length error: should be between 1 and 50 chars")
private String lastname ;

private String emailOTP ;

private String firstname ;

@NotBlank(groups = {IDataAdd.class},message = "password required")
@Size(min=6,max=20,groups = {IDataAdd.class,IDataUpdate.class},message = "password length error: should be between 6 and 20 chars")
private String password ;

@NotBlank(groups = {IDataAdd.class},message = "email required")
@Email(groups = {IDataAdd.class,IDataUpdate.class},message = "email should be valid email address")
private String email ;

@NotBlank(groups = {IDataAdd.class},message = "mobile required")
@Size(min=8,max=20,groups = {IDataAdd.class,IDataUpdate.class},message = "mobile length error: should be between 8 and 20 chars")
private String mobile ;

private Integer emailOTPExpires ;

@NotBlank(groups = {IDataAdd.class},message = "role required")
private String role ;



    public String getId() {
        return id;
    }
    public void setId(String val) {
        this.id = val;
    }
        

    public Integer getUserType() {
        return userType;
    }
    public void setUserType(Integer val) {
        this.userType = val;
    }
            

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String val) {
        this.lastname = val;
    }
            

    public String getEmailOTP() {
        return emailOTP;
    }
    public void setEmailOTP(String val) {
        this.emailOTP = val;
    }
            

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String val) {
        this.firstname = val;
    }
            

    public String getPassword() {
        return password;
    }
    public void setPassword(String val) {
        this.password = val;
    }
            

    public String getEmail() {
        return email;
    }
    public void setEmail(String val) {
        this.email = val;
    }
            

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String val) {
        this.mobile = val;
    }
            

    public Integer getEmailOTPExpires() {
        return emailOTPExpires;
    }
    public void setEmailOTPExpires(Integer val) {
        this.emailOTPExpires = val;
    }
            

    public String getRole() {
        return role;
    }
    public void setRole(String val) {
        this.role = val;
    }
            


}

