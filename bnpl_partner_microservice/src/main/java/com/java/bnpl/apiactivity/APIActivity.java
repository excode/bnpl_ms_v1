package com.java.bnpl.apiactivity;

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
public class APIActivity extends Auditable {

    @Id

    @SequenceGenerator(name = "apiactivity_sequence", sequenceName = "apiactivity_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apiactivity_sequence")
    private Long id;

    @NotNull(groups = { IDataAdd.class }, message = "partnerId required")
    private Integer partnerId;

    @NotBlank(groups = { IDataAdd.class }, message = "apiKeyId required")
    private String apiKeyId;

    @NotBlank(groups = { IDataAdd.class }, message = "apiCallName required")
    private String apiCallName;

    @NotBlank(groups = { IDataAdd.class }, message = "apiCallRequest required")
    private String apiCallRequest;

    @NotBlank(groups = { IDataAdd.class }, message = "errorDetails required")
    private String errorDetails;

    public Number getId() {
        return id;
    }

    public void setId(Long val) {
        this.id = val;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer val) {
        this.partnerId = val;
    }

    public String getAPIKeyID() {
        return apiKeyId;
    }

    public void setAPIKeyID(String val) {
        this.apiKeyId = val;
    }

    public String getAPICallName() {
        return apiCallName;
    }

    public void setAPICallName(String val) {
        this.apiCallName = val;
    }

    public String getAPICallResult() {
        return apiCallRequest;
    }

    public void setAPICallResult(String val) {
        this.apiCallRequest = val;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String val) {
        this.errorDetails = val;
    }
    

}
