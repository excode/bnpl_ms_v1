package com.java.bnpl.accesspermission;

import com.java.bnpl.config.Auditable;
import com.java.bnpl.interfaces.IDataAdd;

import jakarta.persistence.Column;
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
    @Column(name = "can_add")
    private Boolean canAdd;

    @Transient
    private String addStr;

    @NotNull(groups = { IDataAdd.class }, message = "edit required")
    @Column(name = "can_edit")
    private Boolean canEdit;

    @Transient
    private String editStr;

    @NotNull(groups = { IDataAdd.class }, message = "read required")
    @Column(name = "can_view")
    private Boolean canView;

    @Transient
    private String readStr;

    @NotNull(groups = { IDataAdd.class }, message = "delete required")
    @Column(name = "can_delete")
    private Boolean canDelete;

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
        return canAdd ;
    }

    public void setAdd(Boolean val) {
        this.canAdd  = val;
    }

    public Boolean isAdd() {
        return this.canAdd ;
    }

    public String getAddStr() {
        return addStr;
    }

    public void setAddStr(String val) {
        this.addStr = val;
        try {
            this.canAdd  = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.canAdd  =null
        }
    }

    public Boolean getEdit() {
        return canEdit;
    }

    public void setEdit(Boolean val) {
        this.canEdit = val;
    }

    public Boolean isEdit() {
        return this.canEdit;
    }

    public String getEditStr() {
        return editStr;
    }

    public void setEditStr(String val) {
        this.editStr = val;
        try {
            this.canEdit = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.canEdit =null
        }
    }

    public Boolean getRead() {
        return canView;
    }

    public void setRead(Boolean val) {
        this.canView = val;
    }

    public Boolean isRead() {
        return this.canView;
    }

    public String getReadStr() {
        return readStr;
    }

    public void setReadStr(String val) {
        this.readStr = val;
        try {
            this.canView = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.canView =null
        }
    }

    public Boolean getDelete() {
        return canDelete;
    }

    public void setDelete(Boolean val) {
        this.canDelete = val;
    }

    public Boolean isDelete() {
        return this.canDelete;
    }

    public String getDeleteStr() {
        return deleteStr;
    }

    public void setDeleteStr(String val) {
        this.deleteStr = val;
        try {
            this.canDelete = Boolean.valueOf(val);
        } catch (Exception w) {
            // this.canDelete =null
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String val) {
        this.username = val;
    }

}
