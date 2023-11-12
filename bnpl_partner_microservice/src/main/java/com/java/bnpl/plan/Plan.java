package com.java.bnpl.plan;

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
public class Plan extends Auditable {

    @Id

    @SequenceGenerator(name = "plan_sequence", sequenceName = "plan_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "planName required")
    private String planName;

    @NotNull(groups = { IDataAdd.class }, message = "status required")
    private Integer status;

    @NotNull(groups = { IDataAdd.class }, message = "noOfInstallment required")
    private Integer noOfInstallment;

    @NotNull(groups = { IDataAdd.class }, message = "commision required")
    private Double commision;

    public Number getId() {
        return id;
    }

    public void setId(Long val) {
        this.id = val;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String val) {
        this.planName = val;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer val) {
        this.status = val;
    }

    public Integer getNoOfInstallment() {
        return noOfInstallment;
    }

    public void setNoOfInstallment(Integer val) {
        this.noOfInstallment = val;
    }

    public Double getCommision() {
        return commision;
    }

    public void setCommision(Double val) {
        this.commision = val;
    }

}
