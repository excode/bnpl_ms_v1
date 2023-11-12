package com.java.bnpl.paymentlinks;
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
public class PaymentLinksService {

    private final PaymentLinksRepository paymentLinksRepository;
    private EntityManager entityManager;

    
    public PaymentLinksService(PaymentLinksRepository paymentLinksRepository,EntityManager entityManager){
        this.paymentLinksRepository = paymentLinksRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<PaymentLinks>> getPaymentLinkss(PaymentLinksQuery paymentLinksQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildPaymentLinksQuery(paymentLinksQuery);
        
       int size = paymentLinksQuery.getLimit()>0 ?paymentLinksQuery.getLimit():20;
       int page = paymentLinksQuery.getPage()>0 ?paymentLinksQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<PaymentLinks> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public PaymentLinks getPaymentLinks(Long id){
        PaymentLinksQuery paymentLinksQuery=new PaymentLinksQuery();
        paymentLinksQuery.setId(id);
        paymentLinksQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         paymentLinksQuery.setCreatedBy(username);
         */
        List<Query> query=buildPaymentLinksQuery(paymentLinksQuery);
        List<PaymentLinks> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<PaymentLinks> getPaymentLinksSuggestions(PaymentLinksQuery paymentLinksQuery){
       
        String[] cols={"paymentlinkurl"};
        paymentLinksQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildPaymentLinksQuery(paymentLinksQuery);
        List<PaymentLinks> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<PaymentLinks> getPaymentLinksAll(PaymentLinksQuery paymentLinksQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildPaymentLinksQuery(paymentLinksQuery);
        List<PaymentLinks> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public PaymentLinks addNewPaymentLinks(PaymentLinks paymentLinks) {
       
        return paymentLinksRepository.save(paymentLinks);
    }



    public void deletePaymentLinks(Long id) {
        PaymentLinksQuery paymentLinksQuery=new PaymentLinksQuery();
        paymentLinksQuery.setId(id);
        paymentLinksQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         paymentLinksQuery.setCreateby_mode(QueryEnum.equals);
         paymentLinksQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildPaymentLinksQuery(paymentLinksQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        paymentLinksRepository.deleteById(id);
    }

     @Transactional
     public PaymentLinks updatePaymentLinks(Long id, PaymentLinks paymentLinks) {
        PaymentLinks check = paymentLinksRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
          

        if(paymentLinks.getTransactionID()!=null   && !Objects.equals(check.getTransactionID(),paymentLinks.getTransactionID())){
            check.setTransactionID(paymentLinks.getTransactionID());
        }
  

        if(paymentLinks.getCustomerID()!=null   && !Objects.equals(check.getCustomerID(),paymentLinks.getCustomerID())){
            check.setCustomerID(paymentLinks.getCustomerID());
        }
                             
    if(paymentLinks.getPaymentLinkURL()!=null && paymentLinks.getPaymentLinkURL().length()>0 && !Objects.equals(check.getPaymentLinkURL(),paymentLinks.getPaymentLinkURL())){
        
        check.setPaymentLinkURL(paymentLinks.getPaymentLinkURL());
    }
                             
    if(paymentLinks.getCreateBy()!=null && paymentLinks.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),paymentLinks.getCreateBy())){
        
        check.setCreateBy(paymentLinks.getCreateBy());
    }

        if(paymentLinks.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),paymentLinks.getCreateAt())){
            check.setCreateAt(paymentLinks.getCreateAt());
        }
                             
    if(paymentLinks.getUpdateBy()!=null && paymentLinks.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),paymentLinks.getUpdateBy())){
        
        check.setUpdateBy(paymentLinks.getUpdateBy());
    }

        if(paymentLinks.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),paymentLinks.getUpdateAt())){
            check.setUpdateAt(paymentLinks.getUpdateAt());
        }

        if(paymentLinks.getPartnerId()!=null  && !Objects.equals(check.getPartnerId(),paymentLinks.getPartnerId())){
            check.setPartnerId(paymentLinks.getPartnerId());
        }

        if(paymentLinks.getPaymentAmount()!=null  && !Objects.equals(check.getPaymentAmount(),paymentLinks.getPaymentAmount())){
            check.setPaymentAmount(paymentLinks.getPaymentAmount());
        }
  

        if(paymentLinks.getStatus()!=null   && !Objects.equals(check.getStatus(),paymentLinks.getStatus())){
            check.setStatus(paymentLinks.getStatus());
        }
     return paymentLinksRepository.save(check);

    }

   
    private List<Query> buildPaymentLinksQuery(PaymentLinksQuery paymentLinksQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM PaymentLinks s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM PaymentLinks s where 1=1 ");
        
        QueryBuilder qb_TransactionIDArray = new QueryBuilder("transactionID",paymentLinksQuery.getTransactionID_array());
        if(qb_TransactionIDArray.getSql().length()>0){
            sb.append( qb_TransactionIDArray.getSql());
            sbTotal.append(qb_TransactionIDArray.getSql());
        }
        if(paymentLinksQuery.getTransactionID()!=null){
            QueryBuilder qb_TransactionID = new QueryBuilder("transactionID",paymentLinksQuery.getTransactionID(),paymentLinksQuery.getTransactionID_mode());
            if(qb_TransactionID.getSql().length()>0){
                sb.append( qb_TransactionID.getSql());
                sbTotal.append(qb_TransactionID.getSql());
            }
        }
        

        QueryBuilder qb_CustomerIDArray = new QueryBuilder("customerID",paymentLinksQuery.getCustomerID_array());
        if(qb_CustomerIDArray.getSql().length()>0){
            sb.append( qb_CustomerIDArray.getSql());
            sbTotal.append(qb_CustomerIDArray.getSql());
        }
        if(paymentLinksQuery.getCustomerID()!=null){
            QueryBuilder qb_CustomerID = new QueryBuilder("customerID",paymentLinksQuery.getCustomerID(),paymentLinksQuery.getCustomerID_mode());
            if(qb_CustomerID.getSql().length()>0){
                sb.append( qb_CustomerID.getSql());
                sbTotal.append(qb_CustomerID.getSql());
            }
        }
        

        QueryBuilder qb_PaymentLinkURLArray = new QueryBuilder("paymentLinkURL",paymentLinksQuery.getPaymentLinkURL_array());
        if(qb_PaymentLinkURLArray.getSql().length()>0){
            sb.append( qb_PaymentLinkURLArray.getSql());
            sbTotal.append(qb_PaymentLinkURLArray.getSql());
        }
        QueryBuilder qb_PaymentLinkURL = new QueryBuilder("paymentLinkURL",paymentLinksQuery.getPaymentLinkURL(),paymentLinksQuery.getPaymentLinkURL_mode());
        if(qb_PaymentLinkURL.getSql().length()>0){
            sb.append( qb_PaymentLinkURL.getSql());
            sbTotal.append(qb_PaymentLinkURL.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",paymentLinksQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(paymentLinksQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",paymentLinksQuery.getId(),paymentLinksQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",paymentLinksQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",paymentLinksQuery.getCreateBy(),paymentLinksQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",paymentLinksQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",paymentLinksQuery.getUpdateBy(),paymentLinksQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",paymentLinksQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(paymentLinksQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",paymentLinksQuery.getPartnerId(),paymentLinksQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_PaymentAmountArray = new QueryBuilder("paymentAmount",paymentLinksQuery.getPaymentAmount_array());
        if(qb_PaymentAmountArray.getSql().length()>0){
            sb.append( qb_PaymentAmountArray.getSql());
            sbTotal.append(qb_PaymentAmountArray.getSql());
        }
        if(paymentLinksQuery.getPaymentAmount()!=null){
            QueryBuilder qb_PaymentAmount = new QueryBuilder("paymentAmount",paymentLinksQuery.getPaymentAmount(),paymentLinksQuery.getPaymentAmount_mode());
            if(qb_PaymentAmount.getSql().length()>0){
                sb.append( qb_PaymentAmount.getSql());
                sbTotal.append(qb_PaymentAmount.getSql());
            }
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",paymentLinksQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(paymentLinksQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",paymentLinksQuery.getStatus(),paymentLinksQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(paymentLinksQuery.getKeywordColumns(),paymentLinksQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(paymentLinksQuery.getSortBy()!=null && !paymentLinksQuery.getSortBy().isEmpty()){
            if(paymentLinksQuery.getSortDirection()!=null &&  paymentLinksQuery.getSortDirection().toString()!=""){
                String ascDesc = paymentLinksQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+paymentLinksQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+paymentLinksQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),PaymentLinks.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),PaymentLinks.class);
        // Fill Parameters values 

        
        if(paymentLinksQuery.getTransactionID_array()!=null){
            if(paymentLinksQuery.getTransactionID_array().length==2){
                query.setParameter("transactionID1",paymentLinksQuery.getTransactionID_array()[0]);
                queryTotal.setParameter("transactionID1",paymentLinksQuery.getTransactionID_array()[0]);

                query.setParameter("transactionID2",paymentLinksQuery.getTransactionID_array()[1]);
                queryTotal.setParameter("transactionID2",paymentLinksQuery.getTransactionID_array()[1]);
            }
        }
        
        if(paymentLinksQuery.getTransactionID()!=null){
            query.setParameter("transactionID",paymentLinksQuery.getTransactionID());
            queryTotal.setParameter("transactionID",paymentLinksQuery.getTransactionID());
        }
        

        if(paymentLinksQuery.getCustomerID_array()!=null){
            if(paymentLinksQuery.getCustomerID_array().length==2){
                query.setParameter("customerID1",paymentLinksQuery.getCustomerID_array()[0]);
                queryTotal.setParameter("customerID1",paymentLinksQuery.getCustomerID_array()[0]);

                query.setParameter("customerID2",paymentLinksQuery.getCustomerID_array()[1]);
                queryTotal.setParameter("customerID2",paymentLinksQuery.getCustomerID_array()[1]);
            }
        }
        
        if(paymentLinksQuery.getCustomerID()!=null){
            query.setParameter("customerID",paymentLinksQuery.getCustomerID());
            queryTotal.setParameter("customerID",paymentLinksQuery.getCustomerID());
        }
        

        if(paymentLinksQuery.getPaymentLinkURL()!=null){
            query.setParameter("paymentLinkURL",qb_PaymentLinkURL.getValue());
            queryTotal.setParameter("paymentLinkURL",qb_PaymentLinkURL.getValue());
        }
        

        if(paymentLinksQuery.getId_array()!=null){
            if(paymentLinksQuery.getId_array().length==2){
                query.setParameter("id1",paymentLinksQuery.getId_array()[0]);
                queryTotal.setParameter("id1",paymentLinksQuery.getId_array()[0]);

                query.setParameter("id2",paymentLinksQuery.getId_array()[1]);
                queryTotal.setParameter("id2",paymentLinksQuery.getId_array()[1]);
            }
        }
        
        if(paymentLinksQuery.getId()!=null){
            query.setParameter("id",paymentLinksQuery.getId());
            queryTotal.setParameter("id",paymentLinksQuery.getId());
        }
        

        if(paymentLinksQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(paymentLinksQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(paymentLinksQuery.getPartnerId_array()!=null){
            if(paymentLinksQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",paymentLinksQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",paymentLinksQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",paymentLinksQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",paymentLinksQuery.getPartnerId_array()[1]);
            }
        }
        
        if(paymentLinksQuery.getPartnerId()!=null){
            query.setParameter("partnerId",paymentLinksQuery.getPartnerId());
            queryTotal.setParameter("partnerId",paymentLinksQuery.getPartnerId());
        }
        

        if(paymentLinksQuery.getPaymentAmount_array()!=null){
            if(paymentLinksQuery.getPaymentAmount_array().length==2){
                query.setParameter("paymentAmount1",paymentLinksQuery.getPaymentAmount_array()[0]);
                queryTotal.setParameter("paymentAmount1",paymentLinksQuery.getPaymentAmount_array()[0]);

                query.setParameter("paymentAmount2",paymentLinksQuery.getPaymentAmount_array()[1]);
                queryTotal.setParameter("paymentAmount2",paymentLinksQuery.getPaymentAmount_array()[1]);
            }
        }
        
        if(paymentLinksQuery.getPaymentAmount()!=null){
            query.setParameter("paymentAmount",paymentLinksQuery.getPaymentAmount());
            queryTotal.setParameter("paymentAmount",paymentLinksQuery.getPaymentAmount());
        }
        

        if(paymentLinksQuery.getStatus_array()!=null){
            if(paymentLinksQuery.getStatus_array().length==2){
                query.setParameter("status1",paymentLinksQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",paymentLinksQuery.getStatus_array()[0]);

                query.setParameter("status2",paymentLinksQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",paymentLinksQuery.getStatus_array()[1]);
            }
        }
        
        if(paymentLinksQuery.getStatus()!=null){
            query.setParameter("status",paymentLinksQuery.getStatus());
            queryTotal.setParameter("status",paymentLinksQuery.getStatus());
        }
        

          if(paymentLinksQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  