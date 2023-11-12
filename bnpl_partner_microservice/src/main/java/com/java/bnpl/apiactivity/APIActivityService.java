package com.java.bnpl.apiactivity;
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
public class APIActivityService {

    private final APIActivityRepository aPIActivityRepository;
    private EntityManager entityManager;

    
    public APIActivityService(APIActivityRepository aPIActivityRepository,EntityManager entityManager){
        this.aPIActivityRepository = aPIActivityRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<APIActivity>> getAPIActivitys(APIActivityQuery aPIActivityQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildAPIActivityQuery(aPIActivityQuery);
        
       int size = aPIActivityQuery.getLimit()>0 ?aPIActivityQuery.getLimit():20;
       int page = aPIActivityQuery.getPage()>0 ?aPIActivityQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<APIActivity> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public APIActivity getAPIActivity(Long id){
        APIActivityQuery aPIActivityQuery=new APIActivityQuery();
        aPIActivityQuery.setId(id);
        aPIActivityQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         aPIActivityQuery.setCreatedBy(username);
         */
        List<Query> query=buildAPIActivityQuery(aPIActivityQuery);
        List<APIActivity> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<APIActivity> getAPIActivitySuggestions(APIActivityQuery aPIActivityQuery){
       
        String[] cols={"apicallname","errordetails"};
        aPIActivityQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAPIActivityQuery(aPIActivityQuery);
        List<APIActivity> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<APIActivity> getAPIActivityAll(APIActivityQuery aPIActivityQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAPIActivityQuery(aPIActivityQuery);
        List<APIActivity> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public APIActivity addNewAPIActivity(APIActivity aPIActivity) {
       
        return aPIActivityRepository.save(aPIActivity);
    }



    public void deleteAPIActivity(Long id) {
        APIActivityQuery aPIActivityQuery=new APIActivityQuery();
        aPIActivityQuery.setId(id);
        aPIActivityQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         aPIActivityQuery.setCreateby_mode(QueryEnum.equals);
         aPIActivityQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildAPIActivityQuery(aPIActivityQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        aPIActivityRepository.deleteById(id);
    }

     @Transactional
     public APIActivity updateAPIActivity(Long id, APIActivity aPIActivity) {
        APIActivity check = aPIActivityRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
          

        if(aPIActivity.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),aPIActivity.getPartnerId())){
            check.setPartnerId(aPIActivity.getPartnerId());
        }
                             
    if(aPIActivity.getAPIKeyID()!=null && aPIActivity.getAPIKeyID().length()>0 && !Objects.equals(check.getAPIKeyID(),aPIActivity.getAPIKeyID())){
        
        check.setAPIKeyID(aPIActivity.getAPIKeyID());
    }
                             
    if(aPIActivity.getAPICallName()!=null && aPIActivity.getAPICallName().length()>0 && !Objects.equals(check.getAPICallName(),aPIActivity.getAPICallName())){
        
        check.setAPICallName(aPIActivity.getAPICallName());
    }
                             
    if(aPIActivity.getAPICallResult()!=null && aPIActivity.getAPICallResult().length()>0 && !Objects.equals(check.getAPICallResult(),aPIActivity.getAPICallResult())){
        
        check.setAPICallResult(aPIActivity.getAPICallResult());
    }
                             
    if(aPIActivity.getErrorDetails()!=null && aPIActivity.getErrorDetails().length()>0 && !Objects.equals(check.getErrorDetails(),aPIActivity.getErrorDetails())){
        
        check.setErrorDetails(aPIActivity.getErrorDetails());
    }
                             
    if(aPIActivity.getCreateBy()!=null && aPIActivity.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),aPIActivity.getCreateBy())){
        
        check.setCreateBy(aPIActivity.getCreateBy());
    }

        if(aPIActivity.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),aPIActivity.getCreateAt())){
            check.setCreateAt(aPIActivity.getCreateAt());
        }
                             
    if(aPIActivity.getUpdateBy()!=null && aPIActivity.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),aPIActivity.getUpdateBy())){
        
        check.setUpdateBy(aPIActivity.getUpdateBy());
    }

        if(aPIActivity.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),aPIActivity.getUpdateAt())){
            check.setUpdateAt(aPIActivity.getUpdateAt());
        }
     return aPIActivityRepository.save(check);

    }

   
    private List<Query> buildAPIActivityQuery(APIActivityQuery aPIActivityQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM APIActivity s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM APIActivity s where 1=1 ");
        
        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",aPIActivityQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(aPIActivityQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",aPIActivityQuery.getPartnerId(),aPIActivityQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_APICallNameArray = new QueryBuilder("aPICallName",aPIActivityQuery.getAPICallName_array());
        if(qb_APICallNameArray.getSql().length()>0){
            sb.append( qb_APICallNameArray.getSql());
            sbTotal.append(qb_APICallNameArray.getSql());
        }
        QueryBuilder qb_APICallName = new QueryBuilder("aPICallName",aPIActivityQuery.getAPICallName(),aPIActivityQuery.getAPICallName_mode());
        if(qb_APICallName.getSql().length()>0){
            sb.append( qb_APICallName.getSql());
            sbTotal.append(qb_APICallName.getSql());
        }
        

        QueryBuilder qb_ErrorDetailsArray = new QueryBuilder("errorDetails",aPIActivityQuery.getErrorDetails_array());
        if(qb_ErrorDetailsArray.getSql().length()>0){
            sb.append( qb_ErrorDetailsArray.getSql());
            sbTotal.append(qb_ErrorDetailsArray.getSql());
        }
        QueryBuilder qb_ErrorDetails = new QueryBuilder("errorDetails",aPIActivityQuery.getErrorDetails(),aPIActivityQuery.getErrorDetails_mode());
        if(qb_ErrorDetails.getSql().length()>0){
            sb.append( qb_ErrorDetails.getSql());
            sbTotal.append(qb_ErrorDetails.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",aPIActivityQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(aPIActivityQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",aPIActivityQuery.getId(),aPIActivityQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",aPIActivityQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",aPIActivityQuery.getCreateBy(),aPIActivityQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",aPIActivityQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",aPIActivityQuery.getUpdateBy(),aPIActivityQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(aPIActivityQuery.getKeywordColumns(),aPIActivityQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(aPIActivityQuery.getSortBy()!=null && !aPIActivityQuery.getSortBy().isEmpty()){
            if(aPIActivityQuery.getSortDirection()!=null &&  aPIActivityQuery.getSortDirection().toString()!=""){
                String ascDesc = aPIActivityQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+aPIActivityQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+aPIActivityQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),APIActivity.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),APIActivity.class);
        // Fill Parameters values 

        
        if(aPIActivityQuery.getPartnerId_array()!=null){
            if(aPIActivityQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",aPIActivityQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",aPIActivityQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",aPIActivityQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",aPIActivityQuery.getPartnerId_array()[1]);
            }
        }
        
        if(aPIActivityQuery.getPartnerId()!=null){
            query.setParameter("partnerId",aPIActivityQuery.getPartnerId());
            queryTotal.setParameter("partnerId",aPIActivityQuery.getPartnerId());
        }
        

        if(aPIActivityQuery.getAPICallName()!=null){
            query.setParameter("aPICallName",qb_APICallName.getValue());
            queryTotal.setParameter("aPICallName",qb_APICallName.getValue());
        }
        

        if(aPIActivityQuery.getErrorDetails()!=null){
            query.setParameter("errorDetails",qb_ErrorDetails.getValue());
            queryTotal.setParameter("errorDetails",qb_ErrorDetails.getValue());
        }
        

        if(aPIActivityQuery.getId_array()!=null){
            if(aPIActivityQuery.getId_array().length==2){
                query.setParameter("id1",aPIActivityQuery.getId_array()[0]);
                queryTotal.setParameter("id1",aPIActivityQuery.getId_array()[0]);

                query.setParameter("id2",aPIActivityQuery.getId_array()[1]);
                queryTotal.setParameter("id2",aPIActivityQuery.getId_array()[1]);
            }
        }
        
        if(aPIActivityQuery.getId()!=null){
            query.setParameter("id",aPIActivityQuery.getId());
            queryTotal.setParameter("id",aPIActivityQuery.getId());
        }
        

        if(aPIActivityQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(aPIActivityQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

          if(aPIActivityQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  