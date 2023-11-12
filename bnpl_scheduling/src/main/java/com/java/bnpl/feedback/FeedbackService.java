package com.java.bnpl.feedback;
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
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private EntityManager entityManager;

    
    public FeedbackService(FeedbackRepository feedbackRepository,EntityManager entityManager){
        this.feedbackRepository = feedbackRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Feedback>> getFeedbacks(FeedbackQuery feedbackQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildFeedbackQuery(feedbackQuery);
        
       int size = feedbackQuery.getLimit()>0 ?feedbackQuery.getLimit():20;
       int page = feedbackQuery.getPage()>0 ?feedbackQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Feedback> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Feedback getFeedback(Long id){
        FeedbackQuery feedbackQuery=new FeedbackQuery();
        feedbackQuery.setId(id);
        feedbackQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         feedbackQuery.setCreatedBy(username);
         */
        List<Query> query=buildFeedbackQuery(feedbackQuery);
        List<Feedback> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Feedback> getFeedbackSuggestions(FeedbackQuery feedbackQuery){
       
        String[] cols={"feedbacktext"};
        feedbackQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildFeedbackQuery(feedbackQuery);
        List<Feedback> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Feedback> getFeedbackAll(FeedbackQuery feedbackQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildFeedbackQuery(feedbackQuery);
        List<Feedback> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Feedback addNewFeedback(Feedback feedback) {
       
        return feedbackRepository.save(feedback);
    }



    public void deleteFeedback(Long id) {
        FeedbackQuery feedbackQuery=new FeedbackQuery();
        feedbackQuery.setId(id);
        feedbackQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         feedbackQuery.setCreateby_mode(QueryEnum.equals);
         feedbackQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildFeedbackQuery(feedbackQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        feedbackRepository.deleteById(id);
    }

     @Transactional
     public Feedback updateFeedback(Long id, Feedback feedback) {
        Feedback check = feedbackRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
          

        if(feedback.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),feedback.getPartnerId())){
            check.setPartnerId(feedback.getPartnerId());
        }

        if(feedback.getRating()!=null  && !Objects.equals(check.getRating(),feedback.getRating())){
            check.setRating(feedback.getRating());
        }
                             
    if(feedback.getFeedbackText()!=null && feedback.getFeedbackText().length()>0 && !Objects.equals(check.getFeedbackText(),feedback.getFeedbackText())){
        
        check.setFeedbackText(feedback.getFeedbackText());
    }

        if(feedback.getSubmissionDate()!=null  && !Objects.equals(check.getSubmissionDate(),feedback.getSubmissionDate())){
            check.setSubmissionDate(feedback.getSubmissionDate());
        }
                             
    if(feedback.getCreateBy()!=null && feedback.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),feedback.getCreateBy())){
        
        check.setCreateBy(feedback.getCreateBy());
    }

        if(feedback.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),feedback.getCreateAt())){
            check.setCreateAt(feedback.getCreateAt());
        }
                             
    if(feedback.getUpdateBy()!=null && feedback.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),feedback.getUpdateBy())){
        
        check.setUpdateBy(feedback.getUpdateBy());
    }

        if(feedback.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),feedback.getUpdateAt())){
            check.setUpdateAt(feedback.getUpdateAt());
        }
     return feedbackRepository.save(check);

    }

   
    private List<Query> buildFeedbackQuery(FeedbackQuery feedbackQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Feedback s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Feedback s where 1=1 ");
        
        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",feedbackQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(feedbackQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",feedbackQuery.getPartnerId(),feedbackQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_RatingArray = new QueryBuilder("rating",feedbackQuery.getRating_array());
        if(qb_RatingArray.getSql().length()>0){
            sb.append( qb_RatingArray.getSql());
            sbTotal.append(qb_RatingArray.getSql());
        }
        if(feedbackQuery.getRating()!=null){
            QueryBuilder qb_Rating = new QueryBuilder("rating",feedbackQuery.getRating(),feedbackQuery.getRating_mode());
            if(qb_Rating.getSql().length()>0){
                sb.append( qb_Rating.getSql());
                sbTotal.append(qb_Rating.getSql());
            }
        }
        

        QueryBuilder qb_FeedbackTextArray = new QueryBuilder("feedbackText",feedbackQuery.getFeedbackText_array());
        if(qb_FeedbackTextArray.getSql().length()>0){
            sb.append( qb_FeedbackTextArray.getSql());
            sbTotal.append(qb_FeedbackTextArray.getSql());
        }
        QueryBuilder qb_FeedbackText = new QueryBuilder("feedbackText",feedbackQuery.getFeedbackText(),feedbackQuery.getFeedbackText_mode());
        if(qb_FeedbackText.getSql().length()>0){
            sb.append( qb_FeedbackText.getSql());
            sbTotal.append(qb_FeedbackText.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",feedbackQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(feedbackQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",feedbackQuery.getId(),feedbackQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",feedbackQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",feedbackQuery.getCreateBy(),feedbackQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",feedbackQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",feedbackQuery.getUpdateBy(),feedbackQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(feedbackQuery.getKeywordColumns(),feedbackQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(feedbackQuery.getSortBy()!=null && !feedbackQuery.getSortBy().isEmpty()){
            if(feedbackQuery.getSortDirection()!=null &&  feedbackQuery.getSortDirection().toString()!=""){
                String ascDesc = feedbackQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+feedbackQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+feedbackQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Feedback.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Feedback.class);
        // Fill Parameters values 

        
        if(feedbackQuery.getPartnerId_array()!=null){
            if(feedbackQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",feedbackQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",feedbackQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",feedbackQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",feedbackQuery.getPartnerId_array()[1]);
            }
        }
        
        if(feedbackQuery.getPartnerId()!=null){
            query.setParameter("partnerId",feedbackQuery.getPartnerId());
            queryTotal.setParameter("partnerId",feedbackQuery.getPartnerId());
        }
        

        if(feedbackQuery.getRating_array()!=null){
            if(feedbackQuery.getRating_array().length==2){
                query.setParameter("rating1",feedbackQuery.getRating_array()[0]);
                queryTotal.setParameter("rating1",feedbackQuery.getRating_array()[0]);

                query.setParameter("rating2",feedbackQuery.getRating_array()[1]);
                queryTotal.setParameter("rating2",feedbackQuery.getRating_array()[1]);
            }
        }
        
        if(feedbackQuery.getRating()!=null){
            query.setParameter("rating",feedbackQuery.getRating());
            queryTotal.setParameter("rating",feedbackQuery.getRating());
        }
        

        if(feedbackQuery.getFeedbackText()!=null){
            query.setParameter("feedbackText",qb_FeedbackText.getValue());
            queryTotal.setParameter("feedbackText",qb_FeedbackText.getValue());
        }
        

        if(feedbackQuery.getId_array()!=null){
            if(feedbackQuery.getId_array().length==2){
                query.setParameter("id1",feedbackQuery.getId_array()[0]);
                queryTotal.setParameter("id1",feedbackQuery.getId_array()[0]);

                query.setParameter("id2",feedbackQuery.getId_array()[1]);
                queryTotal.setParameter("id2",feedbackQuery.getId_array()[1]);
            }
        }
        
        if(feedbackQuery.getId()!=null){
            query.setParameter("id",feedbackQuery.getId());
            queryTotal.setParameter("id",feedbackQuery.getId());
        }
        

        if(feedbackQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(feedbackQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

          if(feedbackQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  