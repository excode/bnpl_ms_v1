package com.java.bnpl.role;
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
public class RoleService {

    private final RoleRepository roleRepository;
    private EntityManager entityManager;

    
    public RoleService(RoleRepository roleRepository,EntityManager entityManager){
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Role>> getRoles(RoleQuery roleQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildRoleQuery(roleQuery);
        
       int size = roleQuery.getLimit()>0 ?roleQuery.getLimit():20;
       int page = roleQuery.getPage()>0 ?roleQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Role> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Role getRole(Long id){
        RoleQuery roleQuery=new RoleQuery();
        roleQuery.setId(id);
        roleQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         roleQuery.setCreatedBy(username);
         */
        List<Query> query=buildRoleQuery(roleQuery);
        List<Role> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Role> getRoleSuggestions(RoleQuery roleQuery){
       
        String[] cols={};
        roleQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildRoleQuery(roleQuery);
        List<Role> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Role> getRoleAll(RoleQuery roleQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildRoleQuery(roleQuery);
        List<Role> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Role addNewRole(Role role) {
       
        return roleRepository.save(role);
    }



    public void deleteRole(Long id) {
        RoleQuery roleQuery=new RoleQuery();
        roleQuery.setId(id);
        roleQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         roleQuery.setCreateby_mode(QueryEnum.equals);
         roleQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildRoleQuery(roleQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        roleRepository.deleteById(id);
    }

     @Transactional
     public Role updateRole(Long id, Role role) {
        Role check = roleRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(role.getName()!=null && role.getName().length()>0 && !Objects.equals(check.getName(),role.getName())){
        
        check.setName(role.getName());
    }
                             
    if(role.getCreateBy()!=null && role.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),role.getCreateBy())){
        
        check.setCreateBy(role.getCreateBy());
    }

        if(role.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),role.getCreateAt())){
            check.setCreateAt(role.getCreateAt());
        }
                             
    if(role.getUpdateBy()!=null && role.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),role.getUpdateBy())){
        
        check.setUpdateBy(role.getUpdateBy());
    }

        if(role.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),role.getUpdateAt())){
            check.setUpdateAt(role.getUpdateAt());
        }
  

        if(role.getStatus()!=null   && !Objects.equals(check.getStatus(),role.getStatus())){
            check.setStatus(role.getStatus());
        }
     return roleRepository.save(check);

    }

   
    private List<Query> buildRoleQuery(RoleQuery roleQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Role s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Role s where 1=1 ");
        
        QueryBuilder qb_IdArray = new QueryBuilder("id",roleQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(roleQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",roleQuery.getId(),roleQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",roleQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",roleQuery.getCreateBy(),roleQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",roleQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",roleQuery.getUpdateBy(),roleQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",roleQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(roleQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",roleQuery.getStatus(),roleQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(roleQuery.getKeywordColumns(),roleQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(roleQuery.getSortBy()!=null && !roleQuery.getSortBy().isEmpty()){
            if(roleQuery.getSortDirection()!=null &&  roleQuery.getSortDirection().toString()!=""){
                String ascDesc = roleQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+roleQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+roleQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Role.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Role.class);
        // Fill Parameters values 

        
        if(roleQuery.getId_array()!=null){
            if(roleQuery.getId_array().length==2){
                query.setParameter("id1",roleQuery.getId_array()[0]);
                queryTotal.setParameter("id1",roleQuery.getId_array()[0]);

                query.setParameter("id2",roleQuery.getId_array()[1]);
                queryTotal.setParameter("id2",roleQuery.getId_array()[1]);
            }
        }
        
        if(roleQuery.getId()!=null){
            query.setParameter("id",roleQuery.getId());
            queryTotal.setParameter("id",roleQuery.getId());
        }
        

        if(roleQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(roleQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(roleQuery.getStatus_array()!=null){
            if(roleQuery.getStatus_array().length==2){
                query.setParameter("status1",roleQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",roleQuery.getStatus_array()[0]);

                query.setParameter("status2",roleQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",roleQuery.getStatus_array()[1]);
            }
        }
        
        if(roleQuery.getStatus()!=null){
            query.setParameter("status",roleQuery.getStatus());
            queryTotal.setParameter("status",roleQuery.getStatus());
        }
        

          if(roleQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  