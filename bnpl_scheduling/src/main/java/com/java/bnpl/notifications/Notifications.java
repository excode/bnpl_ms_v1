package com.java.bnpl.notifications;

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
public class Notifications extends Auditable{

    @Id
    
@SequenceGenerator(
    name = "notifications_sequence",
    sequenceName = "notifications_sequence",
    allocationSize = 1
)
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "notifications_sequence"
)
    private Long id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "senderID required")
private String senderID ;

@NotBlank(groups = {IDataAdd.class},message = "recipientID required")
private String recipientID ;

@NotBlank(groups = {IDataAdd.class},message = "notificationType required")
private String notificationType ;

@NotBlank(groups = {IDataAdd.class},message = "notificationChannel required")
private String notificationChannel ;

@NotBlank(groups = {IDataAdd.class},message = "notificationContent required")
private String notificationContent ;

@NotNull(groups = {IDataAdd.class},message = "readDate required")
@FutureOrPresent(groups = {IDataAdd.class,IDataUpdate.class,IDataSearch.class},message = "readDate is not valid date")
private LocalDateTime readDate ;

@NotNull(groups = {IDataAdd.class},message = "status required")
private Double status ;



    public Number getId() {
        return id;
    }
    public void setId(Long val) {
        this.id = val;
    }
        

    public String getSenderID() {
        return senderID;
    }
    public void setSenderID(String val) {
        this.senderID = val;
    }
            

    public String getRecipientID() {
        return recipientID;
    }
    public void setRecipientID(String val) {
        this.recipientID = val;
    }
            

    public String getNotificationType() {
        return notificationType;
    }
    public void setNotificationType(String val) {
        this.notificationType = val;
    }
            

    public String getNotificationChannel() {
        return notificationChannel;
    }
    public void setNotificationChannel(String val) {
        this.notificationChannel = val;
    }
            

    public String getNotificationContent() {
        return notificationContent;
    }
    public void setNotificationContent(String val) {
        this.notificationContent = val;
    }
            

    public LocalDateTime getReadDate() {
        return readDate;
    }
    public void setReadDate(LocalDateTime val) {
        this.readDate = val;
    }
            

    public Double getStatus() {
        return status;
    }
    public void setStatus(Double val) {
        this.status = val;
    }
            


}

