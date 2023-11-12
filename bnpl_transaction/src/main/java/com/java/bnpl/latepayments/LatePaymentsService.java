package com.java.bnpl.latepayments;
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
public class LatePaymentsService {

    private final LatePaymentsRepository latePaymentsRepository;
    private EntityManager entityManager;

    
    public LatePaymentsService(LatePaymentsRepository latePaymentsRepository,EntityManager entityManager){
        this.latePaymentsRepository = latePaymentsRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<LatePayments>> getLatePaymentss(LatePaymentsQuery latePaymentsQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildLatePaymentsQuery(latePaymentsQuery);
        
       int size = latePaymentsQuery.getLimit()>0 ?latePaymentsQuery.getLimit():20;
       int page = latePaymentsQuery.getPage()>0 ?latePaymentsQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<LatePayments> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public LatePayments getLatePayments(Long id){
        LatePaymentsQuery latePaymentsQuery=new LatePaymentsQuery();
        latePaymentsQuery.setId(id);
        latePaymentsQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         latePaymentsQuery.setCreatedBy(username);
         */
        List<Query> query=buildLatePaymentsQuery(latePaymentsQuery);
        List<LatePayments> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<LatePayments> getLatePaymentsSuggestions(LatePaymentsQuery latePaymentsQuery){
       
        String[] cols={};
        latePaymentsQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildLatePaymentsQuery(latePaymentsQuery);
        List<LatePayments> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<LatePayments> getLatePaymentsAll(LatePaymentsQuery latePaymentsQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildLatePaymentsQuery(latePaymentsQuery);
        List<LatePayments> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public LatePayments addNewLatePayments(LatePayments latePayments) {
       
        return latePaymentsRepository.save(latePayments);
    }



    public void deleteLatePayments(Long id) {
        LatePaymentsQuery latePaymentsQuery=new LatePaymentsQuery();
        latePaymentsQuery.setId(id);
        latePaymentsQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         latePaymentsQuery.setCreateby_mode(QueryEnum.equals);
         latePaymentsQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildLatePaymentsQuery(latePaymentsQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        latePaymentsRepository.deleteById(id);
    }

     @Transactional
     public LatePayments updateLatePayments(Long id, LatePayments latePayments) {
        LatePayments check = latePaymentsRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
          

        if(latePayments.getCustomerID()!=null   && !Objects.equals(check.getCustomerID(),latePayments.getCustomerID())){
            check.setCustomerID(latePayments.getCustomerID());
        }
  

        if(latePayments.getTransactionID()!=null   && !Objects.equals(check.getTransactionID(),latePayments.getTransactionID())){
            check.setTransactionID(latePayments.getTransactionID());
        }

        if(latePayments.getLatePaymentAmount()!=null  && !Objects.equals(check.getLatePaymentAmount(),latePayments.getLatePaymentAmount())){
            check.setLatePaymentAmount(latePayments.getLatePaymentAmount());
        }

        if(latePayments.getLatePaymentDate()!=null  && !Objects.equals(check.getLatePaymentDate(),latePayments.getLatePaymentDate())){
            check.setLatePaymentDate(latePayments.getLatePaymentDate());
        }
                             
    if(latePayments.getCreateBy()!=null && latePayments.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),latePayments.getCreateBy())){
        
        check.setCreateBy(latePayments.getCreateBy());
    }

        if(latePayments.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),latePayments.getCreateAt())){
            check.setCreateAt(latePayments.getCreateAt());
        }
                             
    if(latePayments.getUpdateBy()!=null && latePayments.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),latePayments.getUpdateBy())){
        
        check.setUpdateBy(latePayments.getUpdateBy());
    }

        if(latePayments.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),latePayments.getUpdateAt())){
            check.setUpdateAt(latePayments.getUpdateAt());
        }
  

        if(latePayments.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),latePayments.getPartnerId())){
            check.setPartnerId(latePayments.getPartnerId());
        }
  

        if(latePayments.getStatus()!=null   && !Objects.equals(check.getStatus(),latePayments.getStatus())){
            check.setStatus(latePayments.getStatus());
        }
     return latePaymentsRepository.save(check);

    }

   
    private List<Query> buildLatePaymentsQuery(LatePaymentsQuery latePaymentsQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM LatePayments s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM LatePayments s where 1=1 ");
        
        QueryBuilder qb_CustomerIDArray = new QueryBuilder("customerID",latePaymentsQuery.getCustomerID_array());
        if(qb_CustomerIDArray.getSql().length()>0){
            sb.append( qb_CustomerIDArray.getSql());
            sbTotal.append(qb_CustomerIDArray.getSql());
        }
        if(latePaymentsQuery.getCustomerID()!=null){
            QueryBuilder qb_CustomerID = new QueryBuilder("customerID",latePaymentsQuery.getCustomerID(),latePaymentsQuery.getCustomerID_mode());
            if(qb_CustomerID.getSql().length()>0){
                sb.append( qb_CustomerID.getSql());
                sbTotal.append(qb_CustomerID.getSql());
            }
        }
        

        QueryBuilder qb_TransactionIDArray = new QueryBuilder("transactionID",latePaymentsQuery.getTransactionID_array());
        if(qb_TransactionIDArray.getSql().length()>0){
            sb.append( qb_TransactionIDArray.getSql());
            sbTotal.append(qb_TransactionIDArray.getSql());
        }
        if(latePaymentsQuery.getTransactionID()!=null){
            QueryBuilder qb_TransactionID = new QueryBuilder("transactionID",latePaymentsQuery.getTransactionID(),latePaymentsQuery.getTransactionID_mode());
            if(qb_TransactionID.getSql().length()>0){
                sb.append( qb_TransactionID.getSql());
                sbTotal.append(qb_TransactionID.getSql());
            }
        }
        

        QueryBuilder qb_LatePaymentAmountArray = new QueryBuilder("latePaymentAmount",latePaymentsQuery.getLatePaymentAmount_array());
        if(qb_LatePaymentAmountArray.getSql().length()>0){
            sb.append( qb_LatePaymentAmountArray.getSql());
            sbTotal.append(qb_LatePaymentAmountArray.getSql());
        }
        if(latePaymentsQuery.getLatePaymentAmount()!=null){
            QueryBuilder qb_LatePaymentAmount = new QueryBuilder("latePaymentAmount",latePaymentsQuery.getLatePaymentAmount(),latePaymentsQuery.getLatePaymentAmount_mode());
            if(qb_LatePaymentAmount.getSql().length()>0){
                sb.append( qb_LatePaymentAmount.getSql());
                sbTotal.append(qb_LatePaymentAmount.getSql());
            }
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",latePaymentsQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(latePaymentsQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",latePaymentsQuery.getId(),latePaymentsQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",latePaymentsQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",latePaymentsQuery.getCreateBy(),latePaymentsQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",latePaymentsQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",latePaymentsQuery.getUpdateBy(),latePaymentsQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",latePaymentsQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(latePaymentsQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",latePaymentsQuery.getPartnerId(),latePaymentsQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",latePaymentsQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(latePaymentsQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",latePaymentsQuery.getStatus(),latePaymentsQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(latePaymentsQuery.getKeywordColumns(),latePaymentsQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(latePaymentsQuery.getSortBy()!=null && !latePaymentsQuery.getSortBy().isEmpty()){
            if(latePaymentsQuery.getSortDirection()!=null &&  latePaymentsQuery.getSortDirection().toString()!=""){
                String ascDesc = latePaymentsQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+latePaymentsQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+latePaymentsQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),LatePayments.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),LatePayments.class);
        // Fill Parameters values 

        
        if(latePaymentsQuery.getCustomerID_array()!=null){
            if(latePaymentsQuery.getCustomerID_array().length==2){
                query.setParameter("customerID1",latePaymentsQuery.getCustomerID_array()[0]);
                queryTotal.setParameter("customerID1",latePaymentsQuery.getCustomerID_array()[0]);

                query.setParameter("customerID2",latePaymentsQuery.getCustomerID_array()[1]);
                queryTotal.setParameter("customerID2",latePaymentsQuery.getCustomerID_array()[1]);
            }
        }
        
        if(latePaymentsQuery.getCustomerID()!=null){
            query.setParameter("customerID",latePaymentsQuery.getCustomerID());
            queryTotal.setParameter("customerID",latePaymentsQuery.getCustomerID());
        }
        

        if(latePaymentsQuery.getTransactionID_array()!=null){
            if(latePaymentsQuery.getTransactionID_array().length==2){
                query.setParameter("transactionID1",latePaymentsQuery.getTransactionID_array()[0]);
                queryTotal.setParameter("transactionID1",latePaymentsQuery.getTransactionID_array()[0]);

                query.setParameter("transactionID2",latePaymentsQuery.getTransactionID_array()[1]);
                queryTotal.setParameter("transactionID2",latePaymentsQuery.getTransactionID_array()[1]);
            }
        }
        
        if(latePaymentsQuery.getTransactionID()!=null){
            query.setParameter("transactionID",latePaymentsQuery.getTransactionID());
            queryTotal.setParameter("transactionID",latePaymentsQuery.getTransactionID());
        }
        

        if(latePaymentsQuery.getLatePaymentAmount_array()!=null){
            if(latePaymentsQuery.getLatePaymentAmount_array().length==2){
                query.setParameter("latePaymentAmount1",latePaymentsQuery.getLatePaymentAmount_array()[0]);
                queryTotal.setParameter("latePaymentAmount1",latePaymentsQuery.getLatePaymentAmount_array()[0]);

                query.setParameter("latePaymentAmount2",latePaymentsQuery.getLatePaymentAmount_array()[1]);
                queryTotal.setParameter("latePaymentAmount2",latePaymentsQuery.getLatePaymentAmount_array()[1]);
            }
        }
        
        if(latePaymentsQuery.getLatePaymentAmount()!=null){
            query.setParameter("latePaymentAmount",latePaymentsQuery.getLatePaymentAmount());
            queryTotal.setParameter("latePaymentAmount",latePaymentsQuery.getLatePaymentAmount());
        }
        

        if(latePaymentsQuery.getId_array()!=null){
            if(latePaymentsQuery.getId_array().length==2){
                query.setParameter("id1",latePaymentsQuery.getId_array()[0]);
                queryTotal.setParameter("id1",latePaymentsQuery.getId_array()[0]);

                query.setParameter("id2",latePaymentsQuery.getId_array()[1]);
                queryTotal.setParameter("id2",latePaymentsQuery.getId_array()[1]);
            }
        }
        
        if(latePaymentsQuery.getId()!=null){
            query.setParameter("id",latePaymentsQuery.getId());
            queryTotal.setParameter("id",latePaymentsQuery.getId());
        }
        

        if(latePaymentsQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(latePaymentsQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(latePaymentsQuery.getPartnerId_array()!=null){
            if(latePaymentsQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",latePaymentsQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",latePaymentsQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",latePaymentsQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",latePaymentsQuery.getPartnerId_array()[1]);
            }
        }
        
        if(latePaymentsQuery.getPartnerId()!=null){
            query.setParameter("partnerId",latePaymentsQuery.getPartnerId());
            queryTotal.setParameter("partnerId",latePaymentsQuery.getPartnerId());
        }
        

        if(latePaymentsQuery.getStatus_array()!=null){
            if(latePaymentsQuery.getStatus_array().length==2){
                query.setParameter("status1",latePaymentsQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",latePaymentsQuery.getStatus_array()[0]);

                query.setParameter("status2",latePaymentsQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",latePaymentsQuery.getStatus_array()[1]);
            }
        }
        
        if(latePaymentsQuery.getStatus()!=null){
            query.setParameter("status",latePaymentsQuery.getStatus());
            queryTotal.setParameter("status",latePaymentsQuery.getStatus());
        }
        

          if(latePaymentsQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  