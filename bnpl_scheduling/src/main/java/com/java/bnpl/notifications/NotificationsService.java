package com.java.bnpl.notifications;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import com.java.bnpl.exception.CustomeException;
import com.java.bnpl.ucodeutility.QueryBuilder;
import com.java.bnpl.ucodeutility.FieldType;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.PagingData;

@Service
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private EntityManager entityManager;

    
    public NotificationsService(NotificationsRepository notificationsRepository,EntityManager entityManager){
        this.notificationsRepository = notificationsRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Notifications>> getNotificationss(NotificationsQuery notificationsQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildNotificationsQuery(notificationsQuery);
        
       int size = notificationsQuery.getLimit()>0 ?notificationsQuery.getLimit():20;
       int page = notificationsQuery.getPage()>0 ?notificationsQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Notifications> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Notifications getNotifications(Long id){
        NotificationsQuery notificationsQuery=new NotificationsQuery();
        notificationsQuery.setId(id);
        notificationsQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         notificationsQuery.setCreatedBy(username);
         */
        List<Query> query=buildNotificationsQuery(notificationsQuery);
        List<Notifications> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Notifications> getNotificationsSuggestions(NotificationsQuery notificationsQuery){
       
        String[] cols={"notificationcontent"};
        notificationsQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildNotificationsQuery(notificationsQuery);
        List<Notifications> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Notifications> getNotificationsAll(NotificationsQuery notificationsQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildNotificationsQuery(notificationsQuery);
        List<Notifications> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Notifications addNewNotifications(Notifications notifications) {
       
        return notificationsRepository.save(notifications);
    }



    public void deleteNotifications(Long id) {
        NotificationsQuery notificationsQuery=new NotificationsQuery();
        notificationsQuery.setId(id);
        notificationsQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         notificationsQuery.setCreateby_mode(QueryEnum.equals);
         notificationsQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildNotificationsQuery(notificationsQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        notificationsRepository.deleteById(id);
    }

     @Transactional
     public Notifications updateNotifications(Long id, Notifications notifications) {
        Notifications check = notificationsRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(notifications.getSenderID()!=null && notifications.getSenderID().length()>0 && !Objects.equals(check.getSenderID(),notifications.getSenderID())){
        
        check.setSenderID(notifications.getSenderID());
    }
                             
    if(notifications.getRecipientID()!=null && notifications.getRecipientID().length()>0 && !Objects.equals(check.getRecipientID(),notifications.getRecipientID())){
        
        check.setRecipientID(notifications.getRecipientID());
    }
                             
    if(notifications.getNotificationType()!=null && notifications.getNotificationType().length()>0 && !Objects.equals(check.getNotificationType(),notifications.getNotificationType())){
        
        check.setNotificationType(notifications.getNotificationType());
    }
                             
    if(notifications.getNotificationChannel()!=null && notifications.getNotificationChannel().length()>0 && !Objects.equals(check.getNotificationChannel(),notifications.getNotificationChannel())){
        
        check.setNotificationChannel(notifications.getNotificationChannel());
    }
                             
    if(notifications.getNotificationContent()!=null && notifications.getNotificationContent().length()>0 && !Objects.equals(check.getNotificationContent(),notifications.getNotificationContent())){
        
        check.setNotificationContent(notifications.getNotificationContent());
    }

        if(notifications.getReadDate()!=null  && !Objects.equals(check.getReadDate(),notifications.getReadDate())){
            check.setReadDate(notifications.getReadDate());
        }
                             
    if(notifications.getCreateBy()!=null && notifications.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),notifications.getCreateBy())){
        
        check.setCreateBy(notifications.getCreateBy());
    }

        if(notifications.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),notifications.getCreateAt())){
            check.setCreateAt(notifications.getCreateAt());
        }
                             
    if(notifications.getUpdateBy()!=null && notifications.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),notifications.getUpdateBy())){
        
        check.setUpdateBy(notifications.getUpdateBy());
    }

        if(notifications.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),notifications.getUpdateAt())){
            check.setUpdateAt(notifications.getUpdateAt());
        }

        if(notifications.getStatus()!=null  && !Objects.equals(check.getStatus(),notifications.getStatus())){
            check.setStatus(notifications.getStatus());
        }
     return notificationsRepository.save(check);

    }

   
    private List<Query> buildNotificationsQuery(NotificationsQuery notificationsQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Notifications s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Notifications s where 1=1 ");
        
        QueryBuilder qb_NotificationContentArray = new QueryBuilder("notificationContent",notificationsQuery.getNotificationContent_array());
        if(qb_NotificationContentArray.getSql().length()>0){
            sb.append( qb_NotificationContentArray.getSql());
            sbTotal.append(qb_NotificationContentArray.getSql());
        }
        QueryBuilder qb_NotificationContent = new QueryBuilder("notificationContent",notificationsQuery.getNotificationContent(),notificationsQuery.getNotificationContent_mode());
        if(qb_NotificationContent.getSql().length()>0){
            sb.append( qb_NotificationContent.getSql());
            sbTotal.append(qb_NotificationContent.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",notificationsQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(notificationsQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",notificationsQuery.getId(),notificationsQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",notificationsQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",notificationsQuery.getCreateBy(),notificationsQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",notificationsQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",notificationsQuery.getUpdateBy(),notificationsQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",notificationsQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(notificationsQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",notificationsQuery.getStatus(),notificationsQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(notificationsQuery.getKeywordColumns(),notificationsQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(notificationsQuery.getSortBy()!=null && !notificationsQuery.getSortBy().isEmpty()){
            if(notificationsQuery.getSortDirection()!=null &&  notificationsQuery.getSortDirection().toString()!=""){
                String ascDesc = notificationsQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+notificationsQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+notificationsQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Notifications.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Notifications.class);
        // Fill Parameters values 

        
        if(notificationsQuery.getNotificationContent()!=null){
            query.setParameter("notificationContent",qb_NotificationContent.getValue());
            queryTotal.setParameter("notificationContent",qb_NotificationContent.getValue());
        }
        

        if(notificationsQuery.getId_array()!=null){
            if(notificationsQuery.getId_array().length==2){
                query.setParameter("id1",notificationsQuery.getId_array()[0]);
                queryTotal.setParameter("id1",notificationsQuery.getId_array()[0]);

                query.setParameter("id2",notificationsQuery.getId_array()[1]);
                queryTotal.setParameter("id2",notificationsQuery.getId_array()[1]);
            }
        }
        
        if(notificationsQuery.getId()!=null){
            query.setParameter("id",notificationsQuery.getId());
            queryTotal.setParameter("id",notificationsQuery.getId());
        }
        

        if(notificationsQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(notificationsQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(notificationsQuery.getStatus_array()!=null){
            if(notificationsQuery.getStatus_array().length==2){
                query.setParameter("status1",notificationsQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",notificationsQuery.getStatus_array()[0]);

                query.setParameter("status2",notificationsQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",notificationsQuery.getStatus_array()[1]);
            }
        }
        
        if(notificationsQuery.getStatus()!=null){
            query.setParameter("status",notificationsQuery.getStatus());
            queryTotal.setParameter("status",notificationsQuery.getStatus());
        }
        

          if(notificationsQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  