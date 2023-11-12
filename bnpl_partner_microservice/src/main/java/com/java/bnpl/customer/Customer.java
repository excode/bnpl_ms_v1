package com.java.bnpl.customer;

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
public class Customer extends Auditable {

    @Id
    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "name required")
    private String name;

    @NotBlank(groups = { IDataAdd.class }, message = "address required")
    private String address;

    @NotBlank(groups = { IDataAdd.class }, message = "email required")
    @Email(groups = { IDataAdd.class, IDataUpdate.class }, message = "email should be valid email address")
    private String email;

    @NotBlank(groups = { IDataAdd.class }, message = "phone required")
    @Pattern(regexp = "^\\d{8,16}$", groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "phone should be valid phone/mobile")
    private String phone;

    @NotBlank(groups = { IDataAdd.class }, message = "city required")
    private String city;

    @NotBlank(groups = { IDataAdd.class }, message = "state required")
    private String state;

    @NotBlank(groups = { IDataAdd.class }, message = "postcode required")
    private String postcode;

    @NotNull(groups = { IDataAdd.class }, message = "status required")
    private Double status;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String val) {
        this.address = val;
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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String val) {
        this.postcode = val;
    }

    public Double getStatus() {
        return status;
    }

    public void setStatus(Double val) {
        this.status = val;
    }

}
