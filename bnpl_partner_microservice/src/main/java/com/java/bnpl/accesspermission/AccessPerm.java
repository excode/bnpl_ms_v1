package com.java.bnpl.accesspermission;

import com.java.bnpl.config.Auditable;
import com.java.bnpl.interfaces.IDataAdd;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

//DB SEPECFIC IMPORT
@Entity
@Table
@NoArgsConstructor
public class AccessPerm extends Auditable {

    @Id

    @SequenceGenerator(name = "accessperm_sequence", sequenceName = "accessperm_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accessperm_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "endPointname required")
    private String endPointname;

    //@NotNull(groups = { IDataAdd.class }, message = "adds required")

    private Boolean adds;
    @Transient
    private String addStr;
    //@NotNull(groups = { IDataAdd.class }, message = "edits required")

    private Boolean edits;
    @Transient
    private String editStr;
    //@NotNull(groups = { IDataAdd.class }, message = "reads required")

    private Boolean reads;
    @Transient
    private String readStr;
    //@NotNull(groups = { IDataAdd.class }, message = "deletes required")

    private Boolean deletes;
    @Transient
    private String deleteStr;
    @NotBlank(groups = { IDataAdd.class }, message = "username required")
    private String username;

    public Number getId() {
        return id;
    }

    public void setId(Long val) {
        this.id = val;
    }

    public String getEndPointname() {
        return endPointname;
    }

    public void setEndPointname(String val) {
        this.endPointname = val;
    }

    public Boolean getAdd() {
        return adds;
    }

    public void setAdd(Boolean val) {
        this.adds = val;
    }

    public Boolean isAdd() {
        return this.adds;
    }

    public String getAddStr() {
        return addStr;
    }

    public void setAddStr(String val) {
        this.addStr = val;
        try {
            this.adds = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.adds =null
        }
    }

    public Boolean getEdit() {
        return edits;
    }

    public void setEdit(Boolean val) {
        this.edits = val;
    }

    public Boolean isEdit() {
        return this.edits;
    }

    public String getEditStr() {
        return editStr;
    }

    public void setEditStr(String val) {
        this.editStr = val;
        try {
            this.edits = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.edits =null
        }
    }

    public Boolean getRead() {
        return reads;
    }

    public void setRead(Boolean val) {
        this.reads = val;
    }

    public Boolean isRead() {
        return this.reads;
    }

    public String getReadStr() {
        return readStr;
    }

    public void setReadStr(String val) {
        this.readStr = val;
        try {
            this.reads = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.reads =null
        }
    }

    public Boolean getDelete() {
        return deletes;
    }

    public void setDelete(Boolean val) {
        this.deletes = val;
    }

    public Boolean isDelete() {
        return this.deletes;
    }

    public String getDeleteStr() {
        return deleteStr;
    }

    public void setDeleteStr(String val) {
        this.deleteStr = val;
        try {
            this.deletes = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.deletes =null
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String val) {
        this.username = val;
    }

}
