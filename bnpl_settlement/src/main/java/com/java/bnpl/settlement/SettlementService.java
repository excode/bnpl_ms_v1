package com.java.bnpl.settlement;
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
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private EntityManager entityManager;

    
    public SettlementService(SettlementRepository settlementRepository,EntityManager entityManager){
        this.settlementRepository = settlementRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Settlement>> getSettlements(SettlementQuery settlementQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildSettlementQuery(settlementQuery);
        
       int size = settlementQuery.getLimit()>0 ?settlementQuery.getLimit():20;
       int page = settlementQuery.getPage()>0 ?settlementQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Settlement> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Settlement getSettlement(Long id){
        SettlementQuery settlementQuery=new SettlementQuery();
        settlementQuery.setId(id);
        settlementQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         settlementQuery.setCreatedBy(username);
         */
        List<Query> query=buildSettlementQuery(settlementQuery);
        List<Settlement> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Settlement> getSettlementSuggestions(SettlementQuery settlementQuery){
       
        String[] cols={"account"};
        settlementQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildSettlementQuery(settlementQuery);
        List<Settlement> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Settlement> getSettlementAll(SettlementQuery settlementQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildSettlementQuery(settlementQuery);
        List<Settlement> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Settlement addNewSettlement(Settlement settlement) {
       
        return settlementRepository.save(settlement);
    }



    public void deleteSettlement(Long id) {
        SettlementQuery settlementQuery=new SettlementQuery();
        settlementQuery.setId(id);
        settlementQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         settlementQuery.setCreateby_mode(QueryEnum.equals);
         settlementQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildSettlementQuery(settlementQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        settlementRepository.deleteById(id);
    }

     @Transactional
     public Settlement updateSettlement(Long id, Settlement settlement) {
        Settlement check = settlementRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(settlement.getAccount()!=null && settlement.getAccount().length()>0 && !Objects.equals(check.getAccount(),settlement.getAccount())){
        
        check.setAccount(settlement.getAccount());
    }

        if(settlement.getDuedate()!=null  && !Objects.equals(check.getDuedate(),settlement.getDuedate())){
            check.setDuedate(settlement.getDuedate());
        }
  

        if(settlement.getStatus()!=null   && !Objects.equals(check.getStatus(),settlement.getStatus())){
            check.setStatus(settlement.getStatus());
        }
                             
    if(settlement.getCreateBy()!=null && settlement.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),settlement.getCreateBy())){
        
        check.setCreateBy(settlement.getCreateBy());
    }

        if(settlement.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),settlement.getCreateAt())){
            check.setCreateAt(settlement.getCreateAt());
        }
                             
    if(settlement.getUpdateBy()!=null && settlement.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),settlement.getUpdateBy())){
        
        check.setUpdateBy(settlement.getUpdateBy());
    }

        if(settlement.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),settlement.getUpdateAt())){
            check.setUpdateAt(settlement.getUpdateAt());
        }
  

        if(settlement.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),settlement.getPartnerId())){
            check.setPartnerId(settlement.getPartnerId());
        }
     return settlementRepository.save(check);

    }

   
    private List<Query> buildSettlementQuery(SettlementQuery settlementQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Settlement s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Settlement s where 1=1 ");
        
        QueryBuilder qb_AccountArray = new QueryBuilder("account",settlementQuery.getAccount_array());
        if(qb_AccountArray.getSql().length()>0){
            sb.append( qb_AccountArray.getSql());
            sbTotal.append(qb_AccountArray.getSql());
        }
        QueryBuilder qb_Account = new QueryBuilder("account",settlementQuery.getAccount(),settlementQuery.getAccount_mode());
        if(qb_Account.getSql().length()>0){
            sb.append( qb_Account.getSql());
            sbTotal.append(qb_Account.getSql());
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",settlementQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(settlementQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",settlementQuery.getStatus(),settlementQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",settlementQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(settlementQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",settlementQuery.getId(),settlementQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",settlementQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",settlementQuery.getCreateBy(),settlementQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",settlementQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",settlementQuery.getUpdateBy(),settlementQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",settlementQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(settlementQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",settlementQuery.getPartnerId(),settlementQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(settlementQuery.getKeywordColumns(),settlementQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(settlementQuery.getSortBy()!=null && !settlementQuery.getSortBy().isEmpty()){
            if(settlementQuery.getSortDirection()!=null &&  settlementQuery.getSortDirection().toString()!=""){
                String ascDesc = settlementQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+settlementQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+settlementQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Settlement.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Settlement.class);
        // Fill Parameters values 

        
        if(settlementQuery.getAccount()!=null){
            query.setParameter("account",qb_Account.getValue());
            queryTotal.setParameter("account",qb_Account.getValue());
        }
        

        if(settlementQuery.getStatus_array()!=null){
            if(settlementQuery.getStatus_array().length==2){
                query.setParameter("status1",settlementQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",settlementQuery.getStatus_array()[0]);

                query.setParameter("status2",settlementQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",settlementQuery.getStatus_array()[1]);
            }
        }
        
        if(settlementQuery.getStatus()!=null){
            query.setParameter("status",settlementQuery.getStatus());
            queryTotal.setParameter("status",settlementQuery.getStatus());
        }
        

        if(settlementQuery.getId_array()!=null){
            if(settlementQuery.getId_array().length==2){
                query.setParameter("id1",settlementQuery.getId_array()[0]);
                queryTotal.setParameter("id1",settlementQuery.getId_array()[0]);

                query.setParameter("id2",settlementQuery.getId_array()[1]);
                queryTotal.setParameter("id2",settlementQuery.getId_array()[1]);
            }
        }
        
        if(settlementQuery.getId()!=null){
            query.setParameter("id",settlementQuery.getId());
            queryTotal.setParameter("id",settlementQuery.getId());
        }
        

        if(settlementQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(settlementQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(settlementQuery.getPartnerId_array()!=null){
            if(settlementQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",settlementQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",settlementQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",settlementQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",settlementQuery.getPartnerId_array()[1]);
            }
        }
        
        if(settlementQuery.getPartnerId()!=null){
            query.setParameter("partnerId",settlementQuery.getPartnerId());
            queryTotal.setParameter("partnerId",settlementQuery.getPartnerId());
        }
        

          if(settlementQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  