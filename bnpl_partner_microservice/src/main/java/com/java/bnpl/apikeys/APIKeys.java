package com.java.bnpl.apikeys;

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
public class APIKeys extends Auditable {

    @Id

    @SequenceGenerator(name = "apikeys_sequence", sequenceName = "apikeys_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apikeys_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "keyValue required")
    private String keyValue;

    @NotBlank(groups = { IDataAdd.class }, message = "permissions required")
    private String permissions;

    @NotNull(groups = { IDataAdd.class }, message = "issueDate required")
    @FutureOrPresent(groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "issueDate is not valid date")
    private LocalDateTime issueDate;

    @NotNull(groups = { IDataAdd.class }, message = "expiryDate required")
    @FutureOrPresent(groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "expiryDate is not valid date")
    private LocalDateTime expiryDate;

    @NotNull(groups = { IDataAdd.class }, message = "partnerId required")
    private Integer partnerId;

    @NotNull(groups = { IDataAdd.class }, message = "status required")
    private Integer status;

    public Number getId() {
        return id;
    }

    public void setId(Long val) {
        this.id = val;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String val) {
        this.keyValue = val;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String val) {
        this.permissions = val;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime val) {
        this.issueDate = val;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime val) {
        this.expiryDate = val;
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
