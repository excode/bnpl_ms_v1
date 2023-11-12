package com.java.bnpl.dues;
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
public class DuesService {

    private final DuesRepository duesRepository;
    private EntityManager entityManager;

    
    public DuesService(DuesRepository duesRepository,EntityManager entityManager){
        this.duesRepository = duesRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Dues>> getDuess(DuesQuery duesQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildDuesQuery(duesQuery);
        
       int size = duesQuery.getLimit()>0 ?duesQuery.getLimit():20;
       int page = duesQuery.getPage()>0 ?duesQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Dues> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Dues getDues(Long id){
        DuesQuery duesQuery=new DuesQuery();
        duesQuery.setId(id);
        duesQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         duesQuery.setCreatedBy(username);
         */
        List<Query> query=buildDuesQuery(duesQuery);
        List<Dues> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Dues> getDuesSuggestions(DuesQuery duesQuery){
       
        String[] cols={"latepaymentfeepolicy"};
        duesQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildDuesQuery(duesQuery);
        List<Dues> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Dues> getDuesAll(DuesQuery duesQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildDuesQuery(duesQuery);
        List<Dues> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Dues addNewDues(Dues dues) {
       
        return duesRepository.save(dues);
    }



    public void deleteDues(Long id) {
        DuesQuery duesQuery=new DuesQuery();
        duesQuery.setId(id);
        duesQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         duesQuery.setCreateby_mode(QueryEnum.equals);
         duesQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildDuesQuery(duesQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        duesRepository.deleteById(id);
    }

     @Transactional
     public Dues updateDues(Long id, Dues dues) {
        Dues check = duesRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
          

        if(dues.getCustomerID()!=null   && !Objects.equals(check.getCustomerID(),dues.getCustomerID())){
            check.setCustomerID(dues.getCustomerID());
        }
                             
    if(dues.getCreateBy()!=null && dues.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),dues.getCreateBy())){
        
        check.setCreateBy(dues.getCreateBy());
    }

        if(dues.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),dues.getCreateAt())){
            check.setCreateAt(dues.getCreateAt());
        }
                             
    if(dues.getUpdateBy()!=null && dues.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),dues.getUpdateBy())){
        
        check.setUpdateBy(dues.getUpdateBy());
    }

        if(dues.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),dues.getUpdateAt())){
            check.setUpdateAt(dues.getUpdateAt());
        }

        if(dues.getTransactionID()!=null  && !Objects.equals(check.getTransactionID(),dues.getTransactionID())){
            check.setTransactionID(dues.getTransactionID());
        }

        if(dues.getDueAmount()!=null  && !Objects.equals(check.getDueAmount(),dues.getDueAmount())){
            check.setDueAmount(dues.getDueAmount());
        }

        if(dues.getDueDate()!=null  && !Objects.equals(check.getDueDate(),dues.getDueDate())){
            check.setDueDate(dues.getDueDate());
        }
                             
    if(dues.getLatePaymentFeePolicy()!=null && dues.getLatePaymentFeePolicy().length()>0 && !Objects.equals(check.getLatePaymentFeePolicy(),dues.getLatePaymentFeePolicy())){
        
        check.setLatePaymentFeePolicy(dues.getLatePaymentFeePolicy());
    }
  

        if(dues.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),dues.getPartnerId())){
            check.setPartnerId(dues.getPartnerId());
        }
     return duesRepository.save(check);

    }

   
    private List<Query> buildDuesQuery(DuesQuery duesQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Dues s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Dues s where 1=1 ");
        
        QueryBuilder qb_CustomerIDArray = new QueryBuilder("customerID",duesQuery.getCustomerID_array());
        if(qb_CustomerIDArray.getSql().length()>0){
            sb.append( qb_CustomerIDArray.getSql());
            sbTotal.append(qb_CustomerIDArray.getSql());
        }
        if(duesQuery.getCustomerID()!=null){
            QueryBuilder qb_CustomerID = new QueryBuilder("customerID",duesQuery.getCustomerID(),duesQuery.getCustomerID_mode());
            if(qb_CustomerID.getSql().length()>0){
                sb.append( qb_CustomerID.getSql());
                sbTotal.append(qb_CustomerID.getSql());
            }
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",duesQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(duesQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",duesQuery.getId(),duesQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",duesQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",duesQuery.getCreateBy(),duesQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",duesQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",duesQuery.getUpdateBy(),duesQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_TransactionIDArray = new QueryBuilder("transactionID",duesQuery.getTransactionID_array());
        if(qb_TransactionIDArray.getSql().length()>0){
            sb.append( qb_TransactionIDArray.getSql());
            sbTotal.append(qb_TransactionIDArray.getSql());
        }
        if(duesQuery.getTransactionID()!=null){
            QueryBuilder qb_TransactionID = new QueryBuilder("transactionID",duesQuery.getTransactionID(),duesQuery.getTransactionID_mode());
            if(qb_TransactionID.getSql().length()>0){
                sb.append( qb_TransactionID.getSql());
                sbTotal.append(qb_TransactionID.getSql());
            }
        }
        

        QueryBuilder qb_DueAmountArray = new QueryBuilder("dueAmount",duesQuery.getDueAmount_array());
        if(qb_DueAmountArray.getSql().length()>0){
            sb.append( qb_DueAmountArray.getSql());
            sbTotal.append(qb_DueAmountArray.getSql());
        }
        if(duesQuery.getDueAmount()!=null){
            QueryBuilder qb_DueAmount = new QueryBuilder("dueAmount",duesQuery.getDueAmount(),duesQuery.getDueAmount_mode());
            if(qb_DueAmount.getSql().length()>0){
                sb.append( qb_DueAmount.getSql());
                sbTotal.append(qb_DueAmount.getSql());
            }
        }
        

        QueryBuilder qb_LatePaymentFeePolicyArray = new QueryBuilder("latePaymentFeePolicy",duesQuery.getLatePaymentFeePolicy_array());
        if(qb_LatePaymentFeePolicyArray.getSql().length()>0){
            sb.append( qb_LatePaymentFeePolicyArray.getSql());
            sbTotal.append(qb_LatePaymentFeePolicyArray.getSql());
        }
        QueryBuilder qb_LatePaymentFeePolicy = new QueryBuilder("latePaymentFeePolicy",duesQuery.getLatePaymentFeePolicy(),duesQuery.getLatePaymentFeePolicy_mode());
        if(qb_LatePaymentFeePolicy.getSql().length()>0){
            sb.append( qb_LatePaymentFeePolicy.getSql());
            sbTotal.append(qb_LatePaymentFeePolicy.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",duesQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(duesQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",duesQuery.getPartnerId(),duesQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(duesQuery.getKeywordColumns(),duesQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(duesQuery.getSortBy()!=null && !duesQuery.getSortBy().isEmpty()){
            if(duesQuery.getSortDirection()!=null &&  duesQuery.getSortDirection().toString()!=""){
                String ascDesc = duesQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+duesQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+duesQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Dues.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Dues.class);
        // Fill Parameters values 

        
        if(duesQuery.getCustomerID_array()!=null){
            if(duesQuery.getCustomerID_array().length==2){
                query.setParameter("customerID1",duesQuery.getCustomerID_array()[0]);
                queryTotal.setParameter("customerID1",duesQuery.getCustomerID_array()[0]);

                query.setParameter("customerID2",duesQuery.getCustomerID_array()[1]);
                queryTotal.setParameter("customerID2",duesQuery.getCustomerID_array()[1]);
            }
        }
        
        if(duesQuery.getCustomerID()!=null){
            query.setParameter("customerID",duesQuery.getCustomerID());
            queryTotal.setParameter("customerID",duesQuery.getCustomerID());
        }
        

        if(duesQuery.getId_array()!=null){
            if(duesQuery.getId_array().length==2){
                query.setParameter("id1",duesQuery.getId_array()[0]);
                queryTotal.setParameter("id1",duesQuery.getId_array()[0]);

                query.setParameter("id2",duesQuery.getId_array()[1]);
                queryTotal.setParameter("id2",duesQuery.getId_array()[1]);
            }
        }
        
        if(duesQuery.getId()!=null){
            query.setParameter("id",duesQuery.getId());
            queryTotal.setParameter("id",duesQuery.getId());
        }
        

        if(duesQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(duesQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(duesQuery.getTransactionID_array()!=null){
            if(duesQuery.getTransactionID_array().length==2){
                query.setParameter("transactionID1",duesQuery.getTransactionID_array()[0]);
                queryTotal.setParameter("transactionID1",duesQuery.getTransactionID_array()[0]);

                query.setParameter("transactionID2",duesQuery.getTransactionID_array()[1]);
                queryTotal.setParameter("transactionID2",duesQuery.getTransactionID_array()[1]);
            }
        }
        
        if(duesQuery.getTransactionID()!=null){
            query.setParameter("transactionID",duesQuery.getTransactionID());
            queryTotal.setParameter("transactionID",duesQuery.getTransactionID());
        }
        

        if(duesQuery.getDueAmount_array()!=null){
            if(duesQuery.getDueAmount_array().length==2){
                query.setParameter("dueAmount1",duesQuery.getDueAmount_array()[0]);
                queryTotal.setParameter("dueAmount1",duesQuery.getDueAmount_array()[0]);

                query.setParameter("dueAmount2",duesQuery.getDueAmount_array()[1]);
                queryTotal.setParameter("dueAmount2",duesQuery.getDueAmount_array()[1]);
            }
        }
        
        if(duesQuery.getDueAmount()!=null){
            query.setParameter("dueAmount",duesQuery.getDueAmount());
            queryTotal.setParameter("dueAmount",duesQuery.getDueAmount());
        }
        

        if(duesQuery.getLatePaymentFeePolicy()!=null){
            query.setParameter("latePaymentFeePolicy",qb_LatePaymentFeePolicy.getValue());
            queryTotal.setParameter("latePaymentFeePolicy",qb_LatePaymentFeePolicy.getValue());
        }
        

        if(duesQuery.getPartnerId_array()!=null){
            if(duesQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",duesQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",duesQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",duesQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",duesQuery.getPartnerId_array()[1]);
            }
        }
        
        if(duesQuery.getPartnerId()!=null){
            query.setParameter("partnerId",duesQuery.getPartnerId());
            queryTotal.setParameter("partnerId",duesQuery.getPartnerId());
        }
        

          if(duesQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  