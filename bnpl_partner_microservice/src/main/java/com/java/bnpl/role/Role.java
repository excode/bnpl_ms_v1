package com.java.bnpl.role;

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
public class Role extends Auditable {

    @Id

    @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "name required")
    private String name;

    @NotNull(groups = { IDataAdd.class }, message = "status required")
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer val) {
        this.status = val;
    }

}
