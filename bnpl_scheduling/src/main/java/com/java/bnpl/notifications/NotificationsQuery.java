package com.java.bnpl.notifications;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class NotificationsQuery  extends Notifications{
	@Getter @Setter private QueryEnum senderID_mode ;
	@Getter @Setter private String[] senderID_array ;
	@Getter @Setter private QueryEnum recipientID_mode ;
	@Getter @Setter private String[] recipientID_array ;
	@Getter @Setter private QueryEnum notificationType_mode ;
	@Getter @Setter private String[] notificationType_array ;
	@Getter @Setter private QueryEnum notificationChannel_mode ;
	@Getter @Setter private String[] notificationChannel_array ;
	@Getter @Setter private QueryEnum notificationContent_mode ;
	@Getter @Setter private String[] notificationContent_array ;
	@Getter @Setter private QueryEnum readDate_mode ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum status_mode ;
	@Getter @Setter private Number[] status_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}