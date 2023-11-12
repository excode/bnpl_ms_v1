package com.java.bnpl.accesspermission;
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
public class AccessPermissionService {

    private final AccessPermissionRepository accessPermissionRepository;
    private EntityManager entityManager;

    
    public AccessPermissionService(AccessPermissionRepository accessPermissionRepository,EntityManager entityManager){
        this.accessPermissionRepository = accessPermissionRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<AccessPermission>> getAccessPermissions(AccessPermissionQuery accessPermissionQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildAccessPermissionQuery(accessPermissionQuery);
        
       int size = accessPermissionQuery.getLimit()>0 ?accessPermissionQuery.getLimit():20;
       int page = accessPermissionQuery.getPage()>0 ?accessPermissionQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<AccessPermission> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public AccessPermission getAccessPermission(Long id){
        AccessPermissionQuery accessPermissionQuery=new AccessPermissionQuery();
        accessPermissionQuery.setId(id);
        accessPermissionQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         accessPermissionQuery.setCreatedBy(username);
         */
        List<Query> query=buildAccessPermissionQuery(accessPermissionQuery);
        List<AccessPermission> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<AccessPermission> getAccessPermissionSuggestions(AccessPermissionQuery accessPermissionQuery){
       
        String[] cols={"endpointname","username"};
        accessPermissionQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAccessPermissionQuery(accessPermissionQuery);
        List<AccessPermission> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<AccessPermission> getAccessPermissionAll(AccessPermissionQuery accessPermissionQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAccessPermissionQuery(accessPermissionQuery);
        List<AccessPermission> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public AccessPermission addNewAccessPermission(AccessPermission accessPermission) {
       
        return accessPermissionRepository.save(accessPermission);
    }



    public void deleteAccessPermission(Long id) {
        AccessPermissionQuery accessPermissionQuery=new AccessPermissionQuery();
        accessPermissionQuery.setId(id);
        accessPermissionQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         accessPermissionQuery.setCreateby_mode(QueryEnum.equals);
         accessPermissionQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildAccessPermissionQuery(accessPermissionQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        accessPermissionRepository.deleteById(id);
    }

     @Transactional
     public AccessPermission updateAccessPermission(Long id, AccessPermission accessPermission) {
        AccessPermission check = accessPermissionRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(accessPermission.getEndPointname()!=null && accessPermission.getEndPointname().length()>0 && !Objects.equals(check.getEndPointname(),accessPermission.getEndPointname())){
        
        check.setEndPointname(accessPermission.getEndPointname());
    }

        if(accessPermission.getAdd()!=null  && !Objects.equals(check.getAdd(),accessPermission.getAdd())){
            check.setAdd(accessPermission.getAdd());
        }

        if(accessPermission.getEdit()!=null  && !Objects.equals(check.getEdit(),accessPermission.getEdit())){
            check.setEdit(accessPermission.getEdit());
        }

        if(accessPermission.getRead()!=null  && !Objects.equals(check.getRead(),accessPermission.getRead())){
            check.setRead(accessPermission.getRead());
        }

        if(accessPermission.getDelete()!=null  && !Objects.equals(check.getDelete(),accessPermission.getDelete())){
            check.setDelete(accessPermission.getDelete());
        }
                             
    if(accessPermission.getCreateBy()!=null && accessPermission.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),accessPermission.getCreateBy())){
        
        check.setCreateBy(accessPermission.getCreateBy());
    }

        if(accessPermission.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),accessPermission.getCreateAt())){
            check.setCreateAt(accessPermission.getCreateAt());
        }
                             
    if(accessPermission.getUpdateBy()!=null && accessPermission.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),accessPermission.getUpdateBy())){
        
        check.setUpdateBy(accessPermission.getUpdateBy());
    }

        if(accessPermission.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),accessPermission.getUpdateAt())){
            check.setUpdateAt(accessPermission.getUpdateAt());
        }
                             
    if(accessPermission.getUsername()!=null && accessPermission.getUsername().length()>0 && !Objects.equals(check.getUsername(),accessPermission.getUsername())){
        
        check.setUsername(accessPermission.getUsername());
    }
     return accessPermissionRepository.save(check);

    }

   
    private List<Query> buildAccessPermissionQuery(AccessPermissionQuery accessPermissionQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM AccessPermission s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM AccessPermission s where 1=1 ");
        
        QueryBuilder qb_EndPointnameArray = new QueryBuilder("endPointname",accessPermissionQuery.getEndPointname_array());
        if(qb_EndPointnameArray.getSql().length()>0){
            sb.append( qb_EndPointnameArray.getSql());
            sbTotal.append(qb_EndPointnameArray.getSql());
        }
        QueryBuilder qb_EndPointname = new QueryBuilder("endPointname",accessPermissionQuery.getEndPointname(),accessPermissionQuery.getEndPointname_mode());
        if(qb_EndPointname.getSql().length()>0){
            sb.append( qb_EndPointname.getSql());
            sbTotal.append(qb_EndPointname.getSql());
        }
        

        QueryBuilder qb_Add = new QueryBuilder("add",accessPermissionQuery.getAdd());
        if(qb_Add.getSql().length()>0){
            sb.append( qb_Add.getSql());
            sbTotal.append(qb_Add.getSql());
        }

            

        QueryBuilder qb_Edit = new QueryBuilder("edit",accessPermissionQuery.getEdit());
        if(qb_Edit.getSql().length()>0){
            sb.append( qb_Edit.getSql());
            sbTotal.append(qb_Edit.getSql());
        }

            

        QueryBuilder qb_Read = new QueryBuilder("read",accessPermissionQuery.getRead());
        if(qb_Read.getSql().length()>0){
            sb.append( qb_Read.getSql());
            sbTotal.append(qb_Read.getSql());
        }

            

        QueryBuilder qb_Delete = new QueryBuilder("delete",accessPermissionQuery.getDelete());
        if(qb_Delete.getSql().length()>0){
            sb.append( qb_Delete.getSql());
            sbTotal.append(qb_Delete.getSql());
        }

            

        QueryBuilder qb_IdArray = new QueryBuilder("id",accessPermissionQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(accessPermissionQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",accessPermissionQuery.getId(),accessPermissionQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",accessPermissionQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",accessPermissionQuery.getCreateBy(),accessPermissionQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",accessPermissionQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",accessPermissionQuery.getUpdateBy(),accessPermissionQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_UsernameArray = new QueryBuilder("username",accessPermissionQuery.getUsername_array());
        if(qb_UsernameArray.getSql().length()>0){
            sb.append( qb_UsernameArray.getSql());
            sbTotal.append(qb_UsernameArray.getSql());
        }
        QueryBuilder qb_Username = new QueryBuilder("username",accessPermissionQuery.getUsername(),accessPermissionQuery.getUsername_mode());
        if(qb_Username.getSql().length()>0){
            sb.append( qb_Username.getSql());
            sbTotal.append(qb_Username.getSql());
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(accessPermissionQuery.getKeywordColumns(),accessPermissionQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(accessPermissionQuery.getSortBy()!=null && !accessPermissionQuery.getSortBy().isEmpty()){
            if(accessPermissionQuery.getSortDirection()!=null &&  accessPermissionQuery.getSortDirection().toString()!=""){
                String ascDesc = accessPermissionQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+accessPermissionQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+accessPermissionQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),AccessPermission.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),AccessPermission.class);
        // Fill Parameters values 

        
        if(accessPermissionQuery.getEndPointname()!=null){
            query.setParameter("endPointname",qb_EndPointname.getValue());
            queryTotal.setParameter("endPointname",qb_EndPointname.getValue());
        }
        

            Boolean blnAdd = accessPermissionQuery.getAdd();
            if(blnAdd!=null){
                query.setParameter("add",accessPermissionQuery.getAdd());
                queryTotal.setParameter("add",accessPermissionQuery.getAdd());
            }
            

            Boolean blnEdit = accessPermissionQuery.getEdit();
            if(blnEdit!=null){
                query.setParameter("edit",accessPermissionQuery.getEdit());
                queryTotal.setParameter("edit",accessPermissionQuery.getEdit());
            }
            

            Boolean blnRead = accessPermissionQuery.getRead();
            if(blnRead!=null){
                query.setParameter("read",accessPermissionQuery.getRead());
                queryTotal.setParameter("read",accessPermissionQuery.getRead());
            }
            

            Boolean blnDelete = accessPermissionQuery.getDelete();
            if(blnDelete!=null){
                query.setParameter("delete",accessPermissionQuery.getDelete());
                queryTotal.setParameter("delete",accessPermissionQuery.getDelete());
            }
            

        if(accessPermissionQuery.getId_array()!=null){
            if(accessPermissionQuery.getId_array().length==2){
                query.setParameter("id1",accessPermissionQuery.getId_array()[0]);
                queryTotal.setParameter("id1",accessPermissionQuery.getId_array()[0]);

                query.setParameter("id2",accessPermissionQuery.getId_array()[1]);
                queryTotal.setParameter("id2",accessPermissionQuery.getId_array()[1]);
            }
        }
        
        if(accessPermissionQuery.getId()!=null){
            query.setParameter("id",accessPermissionQuery.getId());
            queryTotal.setParameter("id",accessPermissionQuery.getId());
        }
        

        if(accessPermissionQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(accessPermissionQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(accessPermissionQuery.getUsername()!=null){
            query.setParameter("username",qb_Username.getValue());
            queryTotal.setParameter("username",qb_Username.getValue());
        }
        

          if(accessPermissionQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  