package com.java.bnpl.apikeys;
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
public class APIKeysService {

    private final APIKeysRepository aPIKeysRepository;
    private EntityManager entityManager;

    
    public APIKeysService(APIKeysRepository aPIKeysRepository,EntityManager entityManager){
        this.aPIKeysRepository = aPIKeysRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<APIKeys>> getAPIKeyss(APIKeysQuery aPIKeysQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildAPIKeysQuery(aPIKeysQuery);
        
       int size = aPIKeysQuery.getLimit()>0 ?aPIKeysQuery.getLimit():20;
       int page = aPIKeysQuery.getPage()>0 ?aPIKeysQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<APIKeys> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public APIKeys getAPIKeys(Long id){
        APIKeysQuery aPIKeysQuery=new APIKeysQuery();
        aPIKeysQuery.setId(id);
        aPIKeysQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         aPIKeysQuery.setCreatedBy(username);
         */
        List<Query> query=buildAPIKeysQuery(aPIKeysQuery);
        List<APIKeys> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<APIKeys> getAPIKeysSuggestions(APIKeysQuery aPIKeysQuery){
       
        String[] cols={"keyvalue","permissions"};
        aPIKeysQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAPIKeysQuery(aPIKeysQuery);
        List<APIKeys> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<APIKeys> getAPIKeysAll(APIKeysQuery aPIKeysQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAPIKeysQuery(aPIKeysQuery);
        List<APIKeys> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public APIKeys addNewAPIKeys(APIKeys aPIKeys) {
       
        return aPIKeysRepository.save(aPIKeys);
    }



    public void deleteAPIKeys(Long id) {
        APIKeysQuery aPIKeysQuery=new APIKeysQuery();
        aPIKeysQuery.setId(id);
        aPIKeysQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         aPIKeysQuery.setCreateby_mode(QueryEnum.equals);
         aPIKeysQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildAPIKeysQuery(aPIKeysQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        aPIKeysRepository.deleteById(id);
    }

     @Transactional
     public APIKeys updateAPIKeys(Long id, APIKeys aPIKeys) {
        APIKeys check = aPIKeysRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(aPIKeys.getKeyValue()!=null && aPIKeys.getKeyValue().length()>0 && !Objects.equals(check.getKeyValue(),aPIKeys.getKeyValue())){
        
        check.setKeyValue(aPIKeys.getKeyValue());
    }
                             
    if(aPIKeys.getPermissions()!=null && aPIKeys.getPermissions().length()>0 && !Objects.equals(check.getPermissions(),aPIKeys.getPermissions())){
        
        check.setPermissions(aPIKeys.getPermissions());
    }

        if(aPIKeys.getIssueDate()!=null  && !Objects.equals(check.getIssueDate(),aPIKeys.getIssueDate())){
            check.setIssueDate(aPIKeys.getIssueDate());
        }

        if(aPIKeys.getExpiryDate()!=null  && !Objects.equals(check.getExpiryDate(),aPIKeys.getExpiryDate())){
            check.setExpiryDate(aPIKeys.getExpiryDate());
        }
                             
    if(aPIKeys.getCreateBy()!=null && aPIKeys.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),aPIKeys.getCreateBy())){
        
        check.setCreateBy(aPIKeys.getCreateBy());
    }

        if(aPIKeys.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),aPIKeys.getCreateAt())){
            check.setCreateAt(aPIKeys.getCreateAt());
        }
                             
    if(aPIKeys.getUpdateBy()!=null && aPIKeys.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),aPIKeys.getUpdateBy())){
        
        check.setUpdateBy(aPIKeys.getUpdateBy());
    }

        if(aPIKeys.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),aPIKeys.getUpdateAt())){
            check.setUpdateAt(aPIKeys.getUpdateAt());
        }
  

        if(aPIKeys.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),aPIKeys.getPartnerId())){
            check.setPartnerId(aPIKeys.getPartnerId());
        }
  

        if(aPIKeys.getStatus()!=null   && !Objects.equals(check.getStatus(),aPIKeys.getStatus())){
            check.setStatus(aPIKeys.getStatus());
        }
     return aPIKeysRepository.save(check);

    }

   
    private List<Query> buildAPIKeysQuery(APIKeysQuery aPIKeysQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM APIKeys s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM APIKeys s where 1=1 ");
        
        QueryBuilder qb_KeyValueArray = new QueryBuilder("keyValue",aPIKeysQuery.getKeyValue_array());
        if(qb_KeyValueArray.getSql().length()>0){
            sb.append( qb_KeyValueArray.getSql());
            sbTotal.append(qb_KeyValueArray.getSql());
        }
        QueryBuilder qb_KeyValue = new QueryBuilder("keyValue",aPIKeysQuery.getKeyValue(),aPIKeysQuery.getKeyValue_mode());
        if(qb_KeyValue.getSql().length()>0){
            sb.append( qb_KeyValue.getSql());
            sbTotal.append(qb_KeyValue.getSql());
        }
        

        QueryBuilder qb_PermissionsArray = new QueryBuilder("permissions",aPIKeysQuery.getPermissions_array());
        if(qb_PermissionsArray.getSql().length()>0){
            sb.append( qb_PermissionsArray.getSql());
            sbTotal.append(qb_PermissionsArray.getSql());
        }
        QueryBuilder qb_Permissions = new QueryBuilder("permissions",aPIKeysQuery.getPermissions(),aPIKeysQuery.getPermissions_mode());
        if(qb_Permissions.getSql().length()>0){
            sb.append( qb_Permissions.getSql());
            sbTotal.append(qb_Permissions.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",aPIKeysQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(aPIKeysQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",aPIKeysQuery.getId(),aPIKeysQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",aPIKeysQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",aPIKeysQuery.getCreateBy(),aPIKeysQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",aPIKeysQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",aPIKeysQuery.getUpdateBy(),aPIKeysQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",aPIKeysQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(aPIKeysQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",aPIKeysQuery.getPartnerId(),aPIKeysQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",aPIKeysQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(aPIKeysQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",aPIKeysQuery.getStatus(),aPIKeysQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(aPIKeysQuery.getKeywordColumns(),aPIKeysQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(aPIKeysQuery.getSortBy()!=null && !aPIKeysQuery.getSortBy().isEmpty()){
            if(aPIKeysQuery.getSortDirection()!=null &&  aPIKeysQuery.getSortDirection().toString()!=""){
                String ascDesc = aPIKeysQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+aPIKeysQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+aPIKeysQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),APIKeys.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),APIKeys.class);
        // Fill Parameters values 

        
        if(aPIKeysQuery.getKeyValue()!=null){
            query.setParameter("keyValue",qb_KeyValue.getValue());
            queryTotal.setParameter("keyValue",qb_KeyValue.getValue());
        }
        

        if(aPIKeysQuery.getPermissions()!=null){
            query.setParameter("permissions",qb_Permissions.getValue());
            queryTotal.setParameter("permissions",qb_Permissions.getValue());
        }
        

        if(aPIKeysQuery.getId_array()!=null){
            if(aPIKeysQuery.getId_array().length==2){
                query.setParameter("id1",aPIKeysQuery.getId_array()[0]);
                queryTotal.setParameter("id1",aPIKeysQuery.getId_array()[0]);

                query.setParameter("id2",aPIKeysQuery.getId_array()[1]);
                queryTotal.setParameter("id2",aPIKeysQuery.getId_array()[1]);
            }
        }
        
        if(aPIKeysQuery.getId()!=null){
            query.setParameter("id",aPIKeysQuery.getId());
            queryTotal.setParameter("id",aPIKeysQuery.getId());
        }
        

        if(aPIKeysQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(aPIKeysQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(aPIKeysQuery.getPartnerId_array()!=null){
            if(aPIKeysQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",aPIKeysQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",aPIKeysQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",aPIKeysQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",aPIKeysQuery.getPartnerId_array()[1]);
            }
        }
        
        if(aPIKeysQuery.getPartnerId()!=null){
            query.setParameter("partnerId",aPIKeysQuery.getPartnerId());
            queryTotal.setParameter("partnerId",aPIKeysQuery.getPartnerId());
        }
        

        if(aPIKeysQuery.getStatus_array()!=null){
            if(aPIKeysQuery.getStatus_array().length==2){
                query.setParameter("status1",aPIKeysQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",aPIKeysQuery.getStatus_array()[0]);

                query.setParameter("status2",aPIKeysQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",aPIKeysQuery.getStatus_array()[1]);
            }
        }
        
        if(aPIKeysQuery.getStatus()!=null){
            query.setParameter("status",aPIKeysQuery.getStatus());
            queryTotal.setParameter("status",aPIKeysQuery.getStatus());
        }
        

          if(aPIKeysQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  