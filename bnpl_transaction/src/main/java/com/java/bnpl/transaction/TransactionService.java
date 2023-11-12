package com.java.bnpl.transaction;
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
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private EntityManager entityManager;

    
    public TransactionService(TransactionRepository transactionRepository,EntityManager entityManager){
        this.transactionRepository = transactionRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Transaction>> getTransactions(TransactionQuery transactionQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildTransactionQuery(transactionQuery);
        
       int size = transactionQuery.getLimit()>0 ?transactionQuery.getLimit():20;
       int page = transactionQuery.getPage()>0 ?transactionQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Transaction> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Transaction getTransaction(Long id){
        TransactionQuery transactionQuery=new TransactionQuery();
        transactionQuery.setId(id);
        transactionQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         transactionQuery.setCreatedBy(username);
         */
        List<Query> query=buildTransactionQuery(transactionQuery);
        List<Transaction> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Transaction> getTransactionSuggestions(TransactionQuery transactionQuery){
       
        String[] cols={"paymentmethod"};
        transactionQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildTransactionQuery(transactionQuery);
        List<Transaction> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Transaction> getTransactionAll(TransactionQuery transactionQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildTransactionQuery(transactionQuery);
        List<Transaction> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Transaction addNewTransaction(Transaction transaction) {
       
        return transactionRepository.save(transaction);
    }



    public void deleteTransaction(Long id) {
        TransactionQuery transactionQuery=new TransactionQuery();
        transactionQuery.setId(id);
        transactionQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         transactionQuery.setCreateby_mode(QueryEnum.equals);
         transactionQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildTransactionQuery(transactionQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        transactionRepository.deleteById(id);
    }

     @Transactional
     public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction check = transactionRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(transaction.getCreateBy()!=null && transaction.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),transaction.getCreateBy())){
        
        check.setCreateBy(transaction.getCreateBy());
    }

        if(transaction.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),transaction.getCreateAt())){
            check.setCreateAt(transaction.getCreateAt());
        }
                             
    if(transaction.getUpdateBy()!=null && transaction.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),transaction.getUpdateBy())){
        
        check.setUpdateBy(transaction.getUpdateBy());
    }

        if(transaction.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),transaction.getUpdateAt())){
            check.setUpdateAt(transaction.getUpdateAt());
        }
  

        if(transaction.getCustomerId()!=null   && !Objects.equals(check.getCustomerId(),transaction.getCustomerId())){
            check.setCustomerId(transaction.getCustomerId());
        }

        if(transaction.getTransactionDate()!=null  && !Objects.equals(check.getTransactionDate(),transaction.getTransactionDate())){
            check.setTransactionDate(transaction.getTransactionDate());
        }

        if(transaction.getTransactionAmount()!=null  && !Objects.equals(check.getTransactionAmount(),transaction.getTransactionAmount())){
            check.setTransactionAmount(transaction.getTransactionAmount());
        }
  

        if(transaction.getStatus()!=null   && !Objects.equals(check.getStatus(),transaction.getStatus())){
            check.setStatus(transaction.getStatus());
        }
                             
    if(transaction.getPaymentMethod()!=null && transaction.getPaymentMethod().length()>0 && !Objects.equals(check.getPaymentMethod(),transaction.getPaymentMethod())){
        
        check.setPaymentMethod(transaction.getPaymentMethod());
    }

        if(transaction.getNextPaymentDate()!=null  && !Objects.equals(check.getNextPaymentDate(),transaction.getNextPaymentDate())){
            check.setNextPaymentDate(transaction.getNextPaymentDate());
        }
  

        if(transaction.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),transaction.getPartnerId())){
            check.setPartnerId(transaction.getPartnerId());
        }
  

        if(transaction.getPlan()!=null   && !Objects.equals(check.getPlan(),transaction.getPlan())){
            check.setPlan(transaction.getPlan());
        }
     return transactionRepository.save(check);

    }

   
    private List<Query> buildTransactionQuery(TransactionQuery transactionQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Transaction s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Transaction s where 1=1 ");
        
        QueryBuilder qb_IdArray = new QueryBuilder("id",transactionQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(transactionQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",transactionQuery.getId(),transactionQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",transactionQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",transactionQuery.getCreateBy(),transactionQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",transactionQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",transactionQuery.getUpdateBy(),transactionQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_CustomerIdArray = new QueryBuilder("customerId",transactionQuery.getCustomerId_array());
        if(qb_CustomerIdArray.getSql().length()>0){
            sb.append( qb_CustomerIdArray.getSql());
            sbTotal.append(qb_CustomerIdArray.getSql());
        }
        if(transactionQuery.getCustomerId()!=null){
            QueryBuilder qb_CustomerId = new QueryBuilder("customerId",transactionQuery.getCustomerId(),transactionQuery.getCustomerId_mode());
            if(qb_CustomerId.getSql().length()>0){
                sb.append( qb_CustomerId.getSql());
                sbTotal.append(qb_CustomerId.getSql());
            }
        }
        

        QueryBuilder qb_TransactionAmountArray = new QueryBuilder("transactionAmount",transactionQuery.getTransactionAmount_array());
        if(qb_TransactionAmountArray.getSql().length()>0){
            sb.append( qb_TransactionAmountArray.getSql());
            sbTotal.append(qb_TransactionAmountArray.getSql());
        }
        if(transactionQuery.getTransactionAmount()!=null){
            QueryBuilder qb_TransactionAmount = new QueryBuilder("transactionAmount",transactionQuery.getTransactionAmount(),transactionQuery.getTransactionAmount_mode());
            if(qb_TransactionAmount.getSql().length()>0){
                sb.append( qb_TransactionAmount.getSql());
                sbTotal.append(qb_TransactionAmount.getSql());
            }
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",transactionQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(transactionQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",transactionQuery.getStatus(),transactionQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

        QueryBuilder qb_PaymentMethodArray = new QueryBuilder("paymentMethod",transactionQuery.getPaymentMethod_array());
        if(qb_PaymentMethodArray.getSql().length()>0){
            sb.append( qb_PaymentMethodArray.getSql());
            sbTotal.append(qb_PaymentMethodArray.getSql());
        }
        QueryBuilder qb_PaymentMethod = new QueryBuilder("paymentMethod",transactionQuery.getPaymentMethod(),transactionQuery.getPaymentMethod_mode());
        if(qb_PaymentMethod.getSql().length()>0){
            sb.append( qb_PaymentMethod.getSql());
            sbTotal.append(qb_PaymentMethod.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",transactionQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(transactionQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",transactionQuery.getPartnerId(),transactionQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_PlanArray = new QueryBuilder("plan",transactionQuery.getPlan_array());
        if(qb_PlanArray.getSql().length()>0){
            sb.append( qb_PlanArray.getSql());
            sbTotal.append(qb_PlanArray.getSql());
        }
        if(transactionQuery.getPlan()!=null){
            QueryBuilder qb_Plan = new QueryBuilder("plan",transactionQuery.getPlan(),transactionQuery.getPlan_mode());
            if(qb_Plan.getSql().length()>0){
                sb.append( qb_Plan.getSql());
                sbTotal.append(qb_Plan.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(transactionQuery.getKeywordColumns(),transactionQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(transactionQuery.getSortBy()!=null && !transactionQuery.getSortBy().isEmpty()){
            if(transactionQuery.getSortDirection()!=null &&  transactionQuery.getSortDirection().toString()!=""){
                String ascDesc = transactionQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+transactionQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+transactionQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Transaction.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Transaction.class);
        // Fill Parameters values 

        
        if(transactionQuery.getId_array()!=null){
            if(transactionQuery.getId_array().length==2){
                query.setParameter("id1",transactionQuery.getId_array()[0]);
                queryTotal.setParameter("id1",transactionQuery.getId_array()[0]);

                query.setParameter("id2",transactionQuery.getId_array()[1]);
                queryTotal.setParameter("id2",transactionQuery.getId_array()[1]);
            }
        }
        
        if(transactionQuery.getId()!=null){
            query.setParameter("id",transactionQuery.getId());
            queryTotal.setParameter("id",transactionQuery.getId());
        }
        

        if(transactionQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(transactionQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(transactionQuery.getCustomerId_array()!=null){
            if(transactionQuery.getCustomerId_array().length==2){
                query.setParameter("customerId1",transactionQuery.getCustomerId_array()[0]);
                queryTotal.setParameter("customerId1",transactionQuery.getCustomerId_array()[0]);

                query.setParameter("customerId2",transactionQuery.getCustomerId_array()[1]);
                queryTotal.setParameter("customerId2",transactionQuery.getCustomerId_array()[1]);
            }
        }
        
        if(transactionQuery.getCustomerId()!=null){
            query.setParameter("customerId",transactionQuery.getCustomerId());
            queryTotal.setParameter("customerId",transactionQuery.getCustomerId());
        }
        

        if(transactionQuery.getTransactionAmount_array()!=null){
            if(transactionQuery.getTransactionAmount_array().length==2){
                query.setParameter("transactionAmount1",transactionQuery.getTransactionAmount_array()[0]);
                queryTotal.setParameter("transactionAmount1",transactionQuery.getTransactionAmount_array()[0]);

                query.setParameter("transactionAmount2",transactionQuery.getTransactionAmount_array()[1]);
                queryTotal.setParameter("transactionAmount2",transactionQuery.getTransactionAmount_array()[1]);
            }
        }
        
        if(transactionQuery.getTransactionAmount()!=null){
            query.setParameter("transactionAmount",transactionQuery.getTransactionAmount());
            queryTotal.setParameter("transactionAmount",transactionQuery.getTransactionAmount());
        }
        

        if(transactionQuery.getStatus_array()!=null){
            if(transactionQuery.getStatus_array().length==2){
                query.setParameter("status1",transactionQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",transactionQuery.getStatus_array()[0]);

                query.setParameter("status2",transactionQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",transactionQuery.getStatus_array()[1]);
            }
        }
        
        if(transactionQuery.getStatus()!=null){
            query.setParameter("status",transactionQuery.getStatus());
            queryTotal.setParameter("status",transactionQuery.getStatus());
        }
        

        if(transactionQuery.getPaymentMethod()!=null){
            query.setParameter("paymentMethod",qb_PaymentMethod.getValue());
            queryTotal.setParameter("paymentMethod",qb_PaymentMethod.getValue());
        }
        

        if(transactionQuery.getPartnerId_array()!=null){
            if(transactionQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",transactionQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",transactionQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",transactionQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",transactionQuery.getPartnerId_array()[1]);
            }
        }
        
        if(transactionQuery.getPartnerId()!=null){
            query.setParameter("partnerId",transactionQuery.getPartnerId());
            queryTotal.setParameter("partnerId",transactionQuery.getPartnerId());
        }
        

        if(transactionQuery.getPlan_array()!=null){
            if(transactionQuery.getPlan_array().length==2){
                query.setParameter("plan1",transactionQuery.getPlan_array()[0]);
                queryTotal.setParameter("plan1",transactionQuery.getPlan_array()[0]);

                query.setParameter("plan2",transactionQuery.getPlan_array()[1]);
                queryTotal.setParameter("plan2",transactionQuery.getPlan_array()[1]);
            }
        }
        
        if(transactionQuery.getPlan()!=null){
            query.setParameter("plan",transactionQuery.getPlan());
            queryTotal.setParameter("plan",transactionQuery.getPlan());
        }
        

          if(transactionQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  