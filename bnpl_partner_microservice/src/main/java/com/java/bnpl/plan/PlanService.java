package com.java.bnpl.plan;
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
public class PlanService {

    private final PlanRepository planRepository;
    private EntityManager entityManager;

    
    public PlanService(PlanRepository planRepository,EntityManager entityManager){
        this.planRepository = planRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Plan>> getPlans(PlanQuery planQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildPlanQuery(planQuery);
        
       int size = planQuery.getLimit()>0 ?planQuery.getLimit():20;
       int page = planQuery.getPage()>0 ?planQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Plan> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Plan getPlan(Long id){
        PlanQuery planQuery=new PlanQuery();
        planQuery.setId(id);
        planQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         planQuery.setCreatedBy(username);
         */
        List<Query> query=buildPlanQuery(planQuery);
        List<Plan> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Plan> getPlanSuggestions(PlanQuery planQuery){
       
        String[] cols={"planname"};
        planQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildPlanQuery(planQuery);
        List<Plan> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Plan> getPlanAll(PlanQuery planQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildPlanQuery(planQuery);
        List<Plan> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Plan addNewPlan(Plan plan) {
       
        return planRepository.save(plan);
    }



    public void deletePlan(Long id) {
        PlanQuery planQuery=new PlanQuery();
        planQuery.setId(id);
        planQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         planQuery.setCreateby_mode(QueryEnum.equals);
         planQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildPlanQuery(planQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        planRepository.deleteById(id);
    }

     @Transactional
     public Plan updatePlan(Long id, Plan plan) {
        Plan check = planRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(plan.getPlanName()!=null && plan.getPlanName().length()>0 && !Objects.equals(check.getPlanName(),plan.getPlanName())){
        
        check.setPlanName(plan.getPlanName());
    }
  

        if(plan.getStatus()!=null   && !Objects.equals(check.getStatus(),plan.getStatus())){
            check.setStatus(plan.getStatus());
        }
                             
    if(plan.getCreateBy()!=null && plan.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),plan.getCreateBy())){
        
        check.setCreateBy(plan.getCreateBy());
    }

        if(plan.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),plan.getCreateAt())){
            check.setCreateAt(plan.getCreateAt());
        }
                             
    if(plan.getUpdateBy()!=null && plan.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),plan.getUpdateBy())){
        
        check.setUpdateBy(plan.getUpdateBy());
    }

        if(plan.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),plan.getUpdateAt())){
            check.setUpdateAt(plan.getUpdateAt());
        }
  

        if(plan.getNoOfInstallment()!=null   && !Objects.equals(check.getNoOfInstallment(),plan.getNoOfInstallment())){
            check.setNoOfInstallment(plan.getNoOfInstallment());
        }

        if(plan.getCommision()!=null  && !Objects.equals(check.getCommision(),plan.getCommision())){
            check.setCommision(plan.getCommision());
        }
     return planRepository.save(check);

    }

   
    private List<Query> buildPlanQuery(PlanQuery planQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Plan s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Plan s where 1=1 ");
        
        QueryBuilder qb_PlanNameArray = new QueryBuilder("planName",planQuery.getPlanName_array());
        if(qb_PlanNameArray.getSql().length()>0){
            sb.append( qb_PlanNameArray.getSql());
            sbTotal.append(qb_PlanNameArray.getSql());
        }
        QueryBuilder qb_PlanName = new QueryBuilder("planName",planQuery.getPlanName(),planQuery.getPlanName_mode());
        if(qb_PlanName.getSql().length()>0){
            sb.append( qb_PlanName.getSql());
            sbTotal.append(qb_PlanName.getSql());
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",planQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(planQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",planQuery.getStatus(),planQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",planQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(planQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",planQuery.getId(),planQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",planQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",planQuery.getCreateBy(),planQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",planQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",planQuery.getUpdateBy(),planQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_NoOfInstallmentArray = new QueryBuilder("noOfInstallment",planQuery.getNoOfInstallment_array());
        if(qb_NoOfInstallmentArray.getSql().length()>0){
            sb.append( qb_NoOfInstallmentArray.getSql());
            sbTotal.append(qb_NoOfInstallmentArray.getSql());
        }
        if(planQuery.getNoOfInstallment()!=null){
            QueryBuilder qb_NoOfInstallment = new QueryBuilder("noOfInstallment",planQuery.getNoOfInstallment(),planQuery.getNoOfInstallment_mode());
            if(qb_NoOfInstallment.getSql().length()>0){
                sb.append( qb_NoOfInstallment.getSql());
                sbTotal.append(qb_NoOfInstallment.getSql());
            }
        }
        

        QueryBuilder qb_CommisionArray = new QueryBuilder("commision",planQuery.getCommision_array());
        if(qb_CommisionArray.getSql().length()>0){
            sb.append( qb_CommisionArray.getSql());
            sbTotal.append(qb_CommisionArray.getSql());
        }
        if(planQuery.getCommision()!=null){
            QueryBuilder qb_Commision = new QueryBuilder("commision",planQuery.getCommision(),planQuery.getCommision_mode());
            if(qb_Commision.getSql().length()>0){
                sb.append( qb_Commision.getSql());
                sbTotal.append(qb_Commision.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(planQuery.getKeywordColumns(),planQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(planQuery.getSortBy()!=null && !planQuery.getSortBy().isEmpty()){
            if(planQuery.getSortDirection()!=null &&  planQuery.getSortDirection().toString()!=""){
                String ascDesc = planQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+planQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+planQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Plan.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Plan.class);
        // Fill Parameters values 

        
        if(planQuery.getPlanName()!=null){
            query.setParameter("planName",qb_PlanName.getValue());
            queryTotal.setParameter("planName",qb_PlanName.getValue());
        }
        

        if(planQuery.getStatus_array()!=null){
            if(planQuery.getStatus_array().length==2){
                query.setParameter("status1",planQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",planQuery.getStatus_array()[0]);

                query.setParameter("status2",planQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",planQuery.getStatus_array()[1]);
            }
        }
        
        if(planQuery.getStatus()!=null){
            query.setParameter("status",planQuery.getStatus());
            queryTotal.setParameter("status",planQuery.getStatus());
        }
        

        if(planQuery.getId_array()!=null){
            if(planQuery.getId_array().length==2){
                query.setParameter("id1",planQuery.getId_array()[0]);
                queryTotal.setParameter("id1",planQuery.getId_array()[0]);

                query.setParameter("id2",planQuery.getId_array()[1]);
                queryTotal.setParameter("id2",planQuery.getId_array()[1]);
            }
        }
        
        if(planQuery.getId()!=null){
            query.setParameter("id",planQuery.getId());
            queryTotal.setParameter("id",planQuery.getId());
        }
        

        if(planQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(planQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(planQuery.getNoOfInstallment_array()!=null){
            if(planQuery.getNoOfInstallment_array().length==2){
                query.setParameter("noOfInstallment1",planQuery.getNoOfInstallment_array()[0]);
                queryTotal.setParameter("noOfInstallment1",planQuery.getNoOfInstallment_array()[0]);

                query.setParameter("noOfInstallment2",planQuery.getNoOfInstallment_array()[1]);
                queryTotal.setParameter("noOfInstallment2",planQuery.getNoOfInstallment_array()[1]);
            }
        }
        
        if(planQuery.getNoOfInstallment()!=null){
            query.setParameter("noOfInstallment",planQuery.getNoOfInstallment());
            queryTotal.setParameter("noOfInstallment",planQuery.getNoOfInstallment());
        }
        

        if(planQuery.getCommision_array()!=null){
            if(planQuery.getCommision_array().length==2){
                query.setParameter("commision1",planQuery.getCommision_array()[0]);
                queryTotal.setParameter("commision1",planQuery.getCommision_array()[0]);

                query.setParameter("commision2",planQuery.getCommision_array()[1]);
                queryTotal.setParameter("commision2",planQuery.getCommision_array()[1]);
            }
        }
        
        if(planQuery.getCommision()!=null){
            query.setParameter("commision",planQuery.getCommision());
            queryTotal.setParameter("commision",planQuery.getCommision());
        }
        

          if(planQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  