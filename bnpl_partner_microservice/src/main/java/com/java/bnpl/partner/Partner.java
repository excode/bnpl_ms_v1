package com.java.bnpl.partner;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class Partner extends Auditable {

    @Id

    @SequenceGenerator(name = "partner_sequence", sequenceName = "partner_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partner_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "contactName required")
    private String contactName;

    @NotBlank(groups = { IDataAdd.class }, message = "email required")
    @Email(groups = { IDataAdd.class, IDataUpdate.class }, message = "email should be valid email address")
    private String email;

    @NotBlank(groups = { IDataAdd.class }, message = "phone required")
    @Pattern(regexp = "^\\d{8,16}$", groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "phone should be valid phone/mobile")
    private String phone;

    @NotBlank(groups = { IDataAdd.class }, message = "address required")
    private String address;

    @NotBlank(groups = { IDataAdd.class }, message = "bussinessName required")
    private String bussinessName;

    private String logo;

    // @NotBlank(groups = {IDataAdd.class},message = "facebook required")
    // @Pattern(regexp =
    // "^((http|https)://)?([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$",groups =
    // {IDataAdd.class,IDataUpdate.class},message = "facebook is invalid URL")
    private String facebook;

    // @NotBlank(groups = {IDataAdd.class},message = "instagram required")
    // @Pattern(regexp =
    // "^((http|https)://)?([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$",groups =
    // {IDataAdd.class,IDataUpdate.class},message = "instagram is invalid URL")
    private String instagram;

    // @NotBlank(groups = {IDataAdd.class},message = "twitter required")
    // @Pattern(regexp =
    // "^((http|https)://)?([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$",groups =
    // {IDataAdd.class,IDataUpdate.class},message = "twitter is invalid URL")
    private String twitter;

    // @NotBlank(groups = {IDataAdd.class},message = "whatsapp required")
    // @Pattern(regexp =
    // "^((http|https)://)?([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$",groups =
    // {IDataAdd.class,IDataUpdate.class},message = "whatsapp is invalid URL")
    private String whatsapp;

    // @NotBlank(groups = {IDataAdd.class},message = "youtube required")
    // @Pattern(regexp =
    // "^((http|https)://)?([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$",groups =
    // {IDataAdd.class,IDataUpdate.class},message = "youtube is invalid URL")
    private String youtube;

    private String agreement;

    @NotBlank(groups = { IDataAdd.class }, message = "city required")
    private String city;

    @NotBlank(groups = { IDataAdd.class }, message = "state required")
    private String state;

    @NotNull(groups = { IDataAdd.class }, message = "zipcode required")
    private Integer zipcode;

    @NotNull(groups = { IDataAdd.class }, message = "commision required")
    private Double commision;

    @NotNull(groups = { IDataAdd.class }, message = "limit required")
    private Double transactionLimit;

    @NotBlank(groups = { IDataAdd.class }, message = "password required")
    @Size(min = 6, max = 100, groups = { IDataAdd.class,
            IDataUpdate.class }, message = "password length error: should be between 6 and 100 chars")
    private String password;

    public Number getId() {
        return id;
    }

    public void setId(Long val) {
        this.id = val;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String val) {
        this.contactName = val;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String val) {
        this.email = val;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String val) {
        this.phone = val;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String val) {
        this.address = val;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String val) {
        this.bussinessName = val;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String val) {
        this.logo = val;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String val) {
        this.facebook = val;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String val) {
        this.instagram = val;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String val) {
        this.twitter = val;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String val) {
        this.whatsapp = val;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String val) {
        this.youtube = val;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String val) {
        this.agreement = val;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String val) {
        this.city = val;
    }

    public String getState() {
        return state;
    }

    public void setState(String val) {
        this.state = val;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer val) {
        this.zipcode = val;
    }

    public Double getCommision() {
        return commision;
    }

    public void setCommision(Double val) {
        this.commision = val;
    }

    public Double getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(Double val) {
        this.transactionLimit = val;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String val) {
        this.password = val;
    }

    public Partner(Long id, String file, String colName) {
        this.id = id;
        if (colName.toLowerCase().equals("logo")) {
            this.logo = file;
        } else if (colName.toLowerCase().equals("agreement")) {
            this.agreement = file;
        }

    }

}
