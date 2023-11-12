package com.java.bnpl.config;

import static jakarta.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    
    @CreatedBy
    protected String createBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected Date createAt;

    @LastModifiedBy
    protected String updateBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date updateAt;
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createdBy) {
        this.createBy = createdBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createdDate) {
        this.createAt = createdDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String lastModifiedBy) {
        this.updateBy = lastModifiedBy;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date lastModifiedDate) {
        this.updateAt = lastModifiedDate;
    }
}
