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
public class AccessPermission extends Auditable {

    @Id

    @SequenceGenerator(name = "accespermisson_sequence", sequenceName = "accespermisson_sequence", allocationSize = 1)

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accespermisson_sequence")
    private Long id;

    @NotBlank(groups = { IDataAdd.class }, message = "endPointname required")
    private String endPointname;

    @NotNull(groups = { IDataAdd.class }, message = "add required")

    private Boolean added;
    @Transient
    private String addStr;
    @NotNull(groups = { IDataAdd.class }, message = "edit required")

    private Boolean edited;
    @Transient
    private String editStr;
    @NotNull(groups = { IDataAdd.class }, message = "read required")

    private Boolean readed;
    @Transient
    private String readStr;
    @NotNull(groups = { IDataAdd.class }, message = "delete required")

    private Boolean deleted;
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
        return added;
    }

    public void setAdd(Boolean val) {
        this.added = val;
    }

    public Boolean isAdd() {
        return this.added;
    }

    public String getAddStr() {
        return addStr;
    }

    public void setAddStr(String val) {
        this.addStr = val;
        try {
            this.added = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.added =null
        }
    }

    public Boolean getEdit() {
        return edited;
    }

    public void setEdit(Boolean val) {
        this.edited = val;
    }

    public Boolean isEdit() {
        return this.edited;
    }

    public String getEditStr() {
        return editStr;
    }

    public void setEditStr(String val) {
        this.editStr = val;
        try {
            this.edited = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.edited =null
        }
    }

    public Boolean getRead() {
        return readed;
    }

    public void setRead(Boolean val) {
        this.readed = val;
    }

    public Boolean isRead() {
        return this.readed;
    }

    public String getReadStr() {
        return readStr;
    }

    public void setReadStr(String val) {
        this.readStr = val;
        try {
            this.readed = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.readed =null
        }
    }

    public Boolean getDelete() {
        return deleted;
    }

    public void setDelete(Boolean val) {
        this.deleted = val;
    }

    public Boolean isDelete() {
        return this.deleted;
    }

    public String getDeleteStr() {
        return deleteStr;
    }

    public void setDeleteStr(String val) {
        this.deleteStr = val;
        try {
            this.deleted = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.deleted =null
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String val) {
        this.username = val;
    }

}
