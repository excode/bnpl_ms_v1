package com.java.bnpl.refunds;
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
public class RefundsService {

    private final RefundsRepository refundsRepository;
    private EntityManager entityManager;

    
    public RefundsService(RefundsRepository refundsRepository,EntityManager entityManager){
        this.refundsRepository = refundsRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Refunds>> getRefundss(RefundsQuery refundsQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildRefundsQuery(refundsQuery);
        
       int size = refundsQuery.getLimit()>0 ?refundsQuery.getLimit():20;
       int page = refundsQuery.getPage()>0 ?refundsQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Refunds> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Refunds getRefunds(Long id){
        RefundsQuery refundsQuery=new RefundsQuery();
        refundsQuery.setId(id);
        refundsQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         refundsQuery.setCreatedBy(username);
         */
        List<Query> query=buildRefundsQuery(refundsQuery);
        List<Refunds> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Refunds> getRefundsSuggestions(RefundsQuery refundsQuery){
       
        String[] cols={"transactionid","reason"};
        refundsQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildRefundsQuery(refundsQuery);
        List<Refunds> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Refunds> getRefundsAll(RefundsQuery refundsQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildRefundsQuery(refundsQuery);
        List<Refunds> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Refunds addNewRefunds(Refunds refunds) {
       
        return refundsRepository.save(refunds);
    }



    public void deleteRefunds(Long id) {
        RefundsQuery refundsQuery=new RefundsQuery();
        refundsQuery.setId(id);
        refundsQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         refundsQuery.setCreateby_mode(QueryEnum.equals);
         refundsQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildRefundsQuery(refundsQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        refundsRepository.deleteById(id);
    }

     @Transactional
     public Refunds updateRefunds(Long id, Refunds refunds) {
        Refunds check = refundsRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(refunds.getTransactionID()!=null && refunds.getTransactionID().length()>0 && !Objects.equals(check.getTransactionID(),refunds.getTransactionID())){
        
        check.setTransactionID(refunds.getTransactionID());
    }

        if(refunds.getRefundAmount()!=null  && !Objects.equals(check.getRefundAmount(),refunds.getRefundAmount())){
            check.setRefundAmount(refunds.getRefundAmount());
        }
                             
    if(refunds.getReason()!=null && refunds.getReason().length()>0 && !Objects.equals(check.getReason(),refunds.getReason())){
        
        check.setReason(refunds.getReason());
    }

        if(refunds.getRefundDate()!=null  && !Objects.equals(check.getRefundDate(),refunds.getRefundDate())){
            check.setRefundDate(refunds.getRefundDate());
        }
                             
    if(refunds.getCreateBy()!=null && refunds.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),refunds.getCreateBy())){
        
        check.setCreateBy(refunds.getCreateBy());
    }

        if(refunds.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),refunds.getCreateAt())){
            check.setCreateAt(refunds.getCreateAt());
        }
                             
    if(refunds.getUpdateBy()!=null && refunds.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),refunds.getUpdateBy())){
        
        check.setUpdateBy(refunds.getUpdateBy());
    }

        if(refunds.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),refunds.getUpdateAt())){
            check.setUpdateAt(refunds.getUpdateAt());
        }
  

        if(refunds.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),refunds.getPartnerId())){
            check.setPartnerId(refunds.getPartnerId());
        }
  

        if(refunds.getStatus()!=null   && !Objects.equals(check.getStatus(),refunds.getStatus())){
            check.setStatus(refunds.getStatus());
        }
  

        if(refunds.getCustomerId()!=null   && !Objects.equals(check.getCustomerId(),refunds.getCustomerId())){
            check.setCustomerId(refunds.getCustomerId());
        }
     return refundsRepository.save(check);

    }

   
    private List<Query> buildRefundsQuery(RefundsQuery refundsQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Refunds s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Refunds s where 1=1 ");
        
        QueryBuilder qb_TransactionIDArray = new QueryBuilder("transactionID",refundsQuery.getTransactionID_array());
        if(qb_TransactionIDArray.getSql().length()>0){
            sb.append( qb_TransactionIDArray.getSql());
            sbTotal.append(qb_TransactionIDArray.getSql());
        }
        QueryBuilder qb_TransactionID = new QueryBuilder("transactionID",refundsQuery.getTransactionID(),refundsQuery.getTransactionID_mode());
        if(qb_TransactionID.getSql().length()>0){
            sb.append( qb_TransactionID.getSql());
            sbTotal.append(qb_TransactionID.getSql());
        }
        

        QueryBuilder qb_RefundAmountArray = new QueryBuilder("refundAmount",refundsQuery.getRefundAmount_array());
        if(qb_RefundAmountArray.getSql().length()>0){
            sb.append( qb_RefundAmountArray.getSql());
            sbTotal.append(qb_RefundAmountArray.getSql());
        }
        if(refundsQuery.getRefundAmount()!=null){
            QueryBuilder qb_RefundAmount = new QueryBuilder("refundAmount",refundsQuery.getRefundAmount(),refundsQuery.getRefundAmount_mode());
            if(qb_RefundAmount.getSql().length()>0){
                sb.append( qb_RefundAmount.getSql());
                sbTotal.append(qb_RefundAmount.getSql());
            }
        }
        

        QueryBuilder qb_ReasonArray = new QueryBuilder("reason",refundsQuery.getReason_array());
        if(qb_ReasonArray.getSql().length()>0){
            sb.append( qb_ReasonArray.getSql());
            sbTotal.append(qb_ReasonArray.getSql());
        }
        QueryBuilder qb_Reason = new QueryBuilder("reason",refundsQuery.getReason(),refundsQuery.getReason_mode());
        if(qb_Reason.getSql().length()>0){
            sb.append( qb_Reason.getSql());
            sbTotal.append(qb_Reason.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",refundsQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(refundsQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",refundsQuery.getId(),refundsQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",refundsQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",refundsQuery.getCreateBy(),refundsQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",refundsQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",refundsQuery.getUpdateBy(),refundsQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",refundsQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(refundsQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",refundsQuery.getPartnerId(),refundsQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",refundsQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(refundsQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",refundsQuery.getStatus(),refundsQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

        QueryBuilder qb_CustomerIdArray = new QueryBuilder("customerId",refundsQuery.getCustomerId_array());
        if(qb_CustomerIdArray.getSql().length()>0){
            sb.append( qb_CustomerIdArray.getSql());
            sbTotal.append(qb_CustomerIdArray.getSql());
        }
        if(refundsQuery.getCustomerId()!=null){
            QueryBuilder qb_CustomerId = new QueryBuilder("customerId",refundsQuery.getCustomerId(),refundsQuery.getCustomerId_mode());
            if(qb_CustomerId.getSql().length()>0){
                sb.append( qb_CustomerId.getSql());
                sbTotal.append(qb_CustomerId.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(refundsQuery.getKeywordColumns(),refundsQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(refundsQuery.getSortBy()!=null && !refundsQuery.getSortBy().isEmpty()){
            if(refundsQuery.getSortDirection()!=null &&  refundsQuery.getSortDirection().toString()!=""){
                String ascDesc = refundsQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+refundsQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+refundsQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Refunds.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Refunds.class);
        // Fill Parameters values 

        
        if(refundsQuery.getTransactionID()!=null){
            query.setParameter("transactionID",qb_TransactionID.getValue());
            queryTotal.setParameter("transactionID",qb_TransactionID.getValue());
        }
        

        if(refundsQuery.getRefundAmount_array()!=null){
            if(refundsQuery.getRefundAmount_array().length==2){
                query.setParameter("refundAmount1",refundsQuery.getRefundAmount_array()[0]);
                queryTotal.setParameter("refundAmount1",refundsQuery.getRefundAmount_array()[0]);

                query.setParameter("refundAmount2",refundsQuery.getRefundAmount_array()[1]);
                queryTotal.setParameter("refundAmount2",refundsQuery.getRefundAmount_array()[1]);
            }
        }
        
        if(refundsQuery.getRefundAmount()!=null){
            query.setParameter("refundAmount",refundsQuery.getRefundAmount());
            queryTotal.setParameter("refundAmount",refundsQuery.getRefundAmount());
        }
        

        if(refundsQuery.getReason()!=null){
            query.setParameter("reason",qb_Reason.getValue());
            queryTotal.setParameter("reason",qb_Reason.getValue());
        }
        

        if(refundsQuery.getId_array()!=null){
            if(refundsQuery.getId_array().length==2){
                query.setParameter("id1",refundsQuery.getId_array()[0]);
                queryTotal.setParameter("id1",refundsQuery.getId_array()[0]);

                query.setParameter("id2",refundsQuery.getId_array()[1]);
                queryTotal.setParameter("id2",refundsQuery.getId_array()[1]);
            }
        }
        
        if(refundsQuery.getId()!=null){
            query.setParameter("id",refundsQuery.getId());
            queryTotal.setParameter("id",refundsQuery.getId());
        }
        

        if(refundsQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(refundsQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(refundsQuery.getPartnerId_array()!=null){
            if(refundsQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",refundsQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",refundsQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",refundsQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",refundsQuery.getPartnerId_array()[1]);
            }
        }
        
        if(refundsQuery.getPartnerId()!=null){
            query.setParameter("partnerId",refundsQuery.getPartnerId());
            queryTotal.setParameter("partnerId",refundsQuery.getPartnerId());
        }
        

        if(refundsQuery.getStatus_array()!=null){
            if(refundsQuery.getStatus_array().length==2){
                query.setParameter("status1",refundsQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",refundsQuery.getStatus_array()[0]);

                query.setParameter("status2",refundsQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",refundsQuery.getStatus_array()[1]);
            }
        }
        
        if(refundsQuery.getStatus()!=null){
            query.setParameter("status",refundsQuery.getStatus());
            queryTotal.setParameter("status",refundsQuery.getStatus());
        }
        

        if(refundsQuery.getCustomerId_array()!=null){
            if(refundsQuery.getCustomerId_array().length==2){
                query.setParameter("customerId1",refundsQuery.getCustomerId_array()[0]);
                queryTotal.setParameter("customerId1",refundsQuery.getCustomerId_array()[0]);

                query.setParameter("customerId2",refundsQuery.getCustomerId_array()[1]);
                queryTotal.setParameter("customerId2",refundsQuery.getCustomerId_array()[1]);
            }
        }
        
        if(refundsQuery.getCustomerId()!=null){
            query.setParameter("customerId",refundsQuery.getCustomerId());
            queryTotal.setParameter("customerId",refundsQuery.getCustomerId());
        }
        

          if(refundsQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  