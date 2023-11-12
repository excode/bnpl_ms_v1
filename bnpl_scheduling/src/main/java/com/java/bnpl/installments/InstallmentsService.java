package com.java.bnpl.installments;
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
public class InstallmentsService {

    private final InstallmentsRepository installmentsRepository;
    private EntityManager entityManager;

    
    public InstallmentsService(InstallmentsRepository installmentsRepository,EntityManager entityManager){
        this.installmentsRepository = installmentsRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Installments>> getInstallmentss(InstallmentsQuery installmentsQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildInstallmentsQuery(installmentsQuery);
        
       int size = installmentsQuery.getLimit()>0 ?installmentsQuery.getLimit():20;
       int page = installmentsQuery.getPage()>0 ?installmentsQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Installments> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Installments getInstallments(Long id){
        InstallmentsQuery installmentsQuery=new InstallmentsQuery();
        installmentsQuery.setId(id);
        installmentsQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         installmentsQuery.setCreatedBy(username);
         */
        List<Query> query=buildInstallmentsQuery(installmentsQuery);
        List<Installments> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Installments> getInstallmentsSuggestions(InstallmentsQuery installmentsQuery){
       
        String[] cols={"customerid"};
        installmentsQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildInstallmentsQuery(installmentsQuery);
        List<Installments> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Installments> getInstallmentsAll(InstallmentsQuery installmentsQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildInstallmentsQuery(installmentsQuery);
        List<Installments> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Installments addNewInstallments(Installments installments) {
       
        return installmentsRepository.save(installments);
    }



    public void deleteInstallments(Long id) {
        InstallmentsQuery installmentsQuery=new InstallmentsQuery();
        installmentsQuery.setId(id);
        installmentsQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         installmentsQuery.setCreateby_mode(QueryEnum.equals);
         installmentsQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildInstallmentsQuery(installmentsQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        installmentsRepository.deleteById(id);
    }

     @Transactional
     public Installments updateInstallments(Long id, Installments installments) {
        Installments check = installmentsRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(installments.getCreateBy()!=null && installments.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),installments.getCreateBy())){
        
        check.setCreateBy(installments.getCreateBy());
    }

        if(installments.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),installments.getCreateAt())){
            check.setCreateAt(installments.getCreateAt());
        }
                             
    if(installments.getUpdateBy()!=null && installments.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),installments.getUpdateBy())){
        
        check.setUpdateBy(installments.getUpdateBy());
    }

        if(installments.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),installments.getUpdateAt())){
            check.setUpdateAt(installments.getUpdateAt());
        }
  

        if(installments.getTransactionID()!=null   && !Objects.equals(check.getTransactionID(),installments.getTransactionID())){
            check.setTransactionID(installments.getTransactionID());
        }
  

        if(installments.getInstallmentNumber()!=null   && !Objects.equals(check.getInstallmentNumber(),installments.getInstallmentNumber())){
            check.setInstallmentNumber(installments.getInstallmentNumber());
        }
  

        if(installments.getInstallmentAmount()!=null   && !Objects.equals(check.getInstallmentAmount(),installments.getInstallmentAmount())){
            check.setInstallmentAmount(installments.getInstallmentAmount());
        }

        if(installments.getDueDate()!=null  && !Objects.equals(check.getDueDate(),installments.getDueDate())){
            check.setDueDate(installments.getDueDate());
        }

        if(installments.getLatePaymentFee()!=null  && !Objects.equals(check.getLatePaymentFee(),installments.getLatePaymentFee())){
            check.setLatePaymentFee(installments.getLatePaymentFee());
        }
  

        if(installments.getPartnerId()!=null   && !Objects.equals(check.getPartnerId(),installments.getPartnerId())){
            check.setPartnerId(installments.getPartnerId());
        }
  

        if(installments.getStatus()!=null   && !Objects.equals(check.getStatus(),installments.getStatus())){
            check.setStatus(installments.getStatus());
        }
                             
    if(installments.getCustomerId()!=null && installments.getCustomerId().length()>0 && !Objects.equals(check.getCustomerId(),installments.getCustomerId())){
        
        check.setCustomerId(installments.getCustomerId());
    }
     return installmentsRepository.save(check);

    }

   
    private List<Query> buildInstallmentsQuery(InstallmentsQuery installmentsQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Installments s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Installments s where 1=1 ");
        
        QueryBuilder qb_IdArray = new QueryBuilder("id",installmentsQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(installmentsQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",installmentsQuery.getId(),installmentsQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",installmentsQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",installmentsQuery.getCreateBy(),installmentsQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",installmentsQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",installmentsQuery.getUpdateBy(),installmentsQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_TransactionIDArray = new QueryBuilder("transactionID",installmentsQuery.getTransactionID_array());
        if(qb_TransactionIDArray.getSql().length()>0){
            sb.append( qb_TransactionIDArray.getSql());
            sbTotal.append(qb_TransactionIDArray.getSql());
        }
        if(installmentsQuery.getTransactionID()!=null){
            QueryBuilder qb_TransactionID = new QueryBuilder("transactionID",installmentsQuery.getTransactionID(),installmentsQuery.getTransactionID_mode());
            if(qb_TransactionID.getSql().length()>0){
                sb.append( qb_TransactionID.getSql());
                sbTotal.append(qb_TransactionID.getSql());
            }
        }
        

        QueryBuilder qb_InstallmentNumberArray = new QueryBuilder("installmentNumber",installmentsQuery.getInstallmentNumber_array());
        if(qb_InstallmentNumberArray.getSql().length()>0){
            sb.append( qb_InstallmentNumberArray.getSql());
            sbTotal.append(qb_InstallmentNumberArray.getSql());
        }
        if(installmentsQuery.getInstallmentNumber()!=null){
            QueryBuilder qb_InstallmentNumber = new QueryBuilder("installmentNumber",installmentsQuery.getInstallmentNumber(),installmentsQuery.getInstallmentNumber_mode());
            if(qb_InstallmentNumber.getSql().length()>0){
                sb.append( qb_InstallmentNumber.getSql());
                sbTotal.append(qb_InstallmentNumber.getSql());
            }
        }
        

        QueryBuilder qb_InstallmentAmountArray = new QueryBuilder("installmentAmount",installmentsQuery.getInstallmentAmount_array());
        if(qb_InstallmentAmountArray.getSql().length()>0){
            sb.append( qb_InstallmentAmountArray.getSql());
            sbTotal.append(qb_InstallmentAmountArray.getSql());
        }
        if(installmentsQuery.getInstallmentAmount()!=null){
            QueryBuilder qb_InstallmentAmount = new QueryBuilder("installmentAmount",installmentsQuery.getInstallmentAmount(),installmentsQuery.getInstallmentAmount_mode());
            if(qb_InstallmentAmount.getSql().length()>0){
                sb.append( qb_InstallmentAmount.getSql());
                sbTotal.append(qb_InstallmentAmount.getSql());
            }
        }
        

        QueryBuilder qb_LatePaymentFeeArray = new QueryBuilder("latePaymentFee",installmentsQuery.getLatePaymentFee_array());
        if(qb_LatePaymentFeeArray.getSql().length()>0){
            sb.append( qb_LatePaymentFeeArray.getSql());
            sbTotal.append(qb_LatePaymentFeeArray.getSql());
        }
        if(installmentsQuery.getLatePaymentFee()!=null){
            QueryBuilder qb_LatePaymentFee = new QueryBuilder("latePaymentFee",installmentsQuery.getLatePaymentFee(),installmentsQuery.getLatePaymentFee_mode());
            if(qb_LatePaymentFee.getSql().length()>0){
                sb.append( qb_LatePaymentFee.getSql());
                sbTotal.append(qb_LatePaymentFee.getSql());
            }
        }
        

        QueryBuilder qb_PartnerIdArray = new QueryBuilder("partnerId",installmentsQuery.getPartnerId_array());
        if(qb_PartnerIdArray.getSql().length()>0){
            sb.append( qb_PartnerIdArray.getSql());
            sbTotal.append(qb_PartnerIdArray.getSql());
        }
        if(installmentsQuery.getPartnerId()!=null){
            QueryBuilder qb_PartnerId = new QueryBuilder("partnerId",installmentsQuery.getPartnerId(),installmentsQuery.getPartnerId_mode());
            if(qb_PartnerId.getSql().length()>0){
                sb.append( qb_PartnerId.getSql());
                sbTotal.append(qb_PartnerId.getSql());
            }
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",installmentsQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(installmentsQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",installmentsQuery.getStatus(),installmentsQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

        QueryBuilder qb_CustomerIdArray = new QueryBuilder("customerId",installmentsQuery.getCustomerId_array());
        if(qb_CustomerIdArray.getSql().length()>0){
            sb.append( qb_CustomerIdArray.getSql());
            sbTotal.append(qb_CustomerIdArray.getSql());
        }
        QueryBuilder qb_CustomerId = new QueryBuilder("customerId",installmentsQuery.getCustomerId(),installmentsQuery.getCustomerId_mode());
        if(qb_CustomerId.getSql().length()>0){
            sb.append( qb_CustomerId.getSql());
            sbTotal.append(qb_CustomerId.getSql());
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(installmentsQuery.getKeywordColumns(),installmentsQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(installmentsQuery.getSortBy()!=null && !installmentsQuery.getSortBy().isEmpty()){
            if(installmentsQuery.getSortDirection()!=null &&  installmentsQuery.getSortDirection().toString()!=""){
                String ascDesc = installmentsQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+installmentsQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+installmentsQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Installments.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Installments.class);
        // Fill Parameters values 

        
        if(installmentsQuery.getId_array()!=null){
            if(installmentsQuery.getId_array().length==2){
                query.setParameter("id1",installmentsQuery.getId_array()[0]);
                queryTotal.setParameter("id1",installmentsQuery.getId_array()[0]);

                query.setParameter("id2",installmentsQuery.getId_array()[1]);
                queryTotal.setParameter("id2",installmentsQuery.getId_array()[1]);
            }
        }
        
        if(installmentsQuery.getId()!=null){
            query.setParameter("id",installmentsQuery.getId());
            queryTotal.setParameter("id",installmentsQuery.getId());
        }
        

        if(installmentsQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(installmentsQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(installmentsQuery.getTransactionID_array()!=null){
            if(installmentsQuery.getTransactionID_array().length==2){
                query.setParameter("transactionID1",installmentsQuery.getTransactionID_array()[0]);
                queryTotal.setParameter("transactionID1",installmentsQuery.getTransactionID_array()[0]);

                query.setParameter("transactionID2",installmentsQuery.getTransactionID_array()[1]);
                queryTotal.setParameter("transactionID2",installmentsQuery.getTransactionID_array()[1]);
            }
        }
        
        if(installmentsQuery.getTransactionID()!=null){
            query.setParameter("transactionID",installmentsQuery.getTransactionID());
            queryTotal.setParameter("transactionID",installmentsQuery.getTransactionID());
        }
        

        if(installmentsQuery.getInstallmentNumber_array()!=null){
            if(installmentsQuery.getInstallmentNumber_array().length==2){
                query.setParameter("installmentNumber1",installmentsQuery.getInstallmentNumber_array()[0]);
                queryTotal.setParameter("installmentNumber1",installmentsQuery.getInstallmentNumber_array()[0]);

                query.setParameter("installmentNumber2",installmentsQuery.getInstallmentNumber_array()[1]);
                queryTotal.setParameter("installmentNumber2",installmentsQuery.getInstallmentNumber_array()[1]);
            }
        }
        
        if(installmentsQuery.getInstallmentNumber()!=null){
            query.setParameter("installmentNumber",installmentsQuery.getInstallmentNumber());
            queryTotal.setParameter("installmentNumber",installmentsQuery.getInstallmentNumber());
        }
        

        if(installmentsQuery.getInstallmentAmount_array()!=null){
            if(installmentsQuery.getInstallmentAmount_array().length==2){
                query.setParameter("installmentAmount1",installmentsQuery.getInstallmentAmount_array()[0]);
                queryTotal.setParameter("installmentAmount1",installmentsQuery.getInstallmentAmount_array()[0]);

                query.setParameter("installmentAmount2",installmentsQuery.getInstallmentAmount_array()[1]);
                queryTotal.setParameter("installmentAmount2",installmentsQuery.getInstallmentAmount_array()[1]);
            }
        }
        
        if(installmentsQuery.getInstallmentAmount()!=null){
            query.setParameter("installmentAmount",installmentsQuery.getInstallmentAmount());
            queryTotal.setParameter("installmentAmount",installmentsQuery.getInstallmentAmount());
        }
        

        if(installmentsQuery.getLatePaymentFee_array()!=null){
            if(installmentsQuery.getLatePaymentFee_array().length==2){
                query.setParameter("latePaymentFee1",installmentsQuery.getLatePaymentFee_array()[0]);
                queryTotal.setParameter("latePaymentFee1",installmentsQuery.getLatePaymentFee_array()[0]);

                query.setParameter("latePaymentFee2",installmentsQuery.getLatePaymentFee_array()[1]);
                queryTotal.setParameter("latePaymentFee2",installmentsQuery.getLatePaymentFee_array()[1]);
            }
        }
        
        if(installmentsQuery.getLatePaymentFee()!=null){
            query.setParameter("latePaymentFee",installmentsQuery.getLatePaymentFee());
            queryTotal.setParameter("latePaymentFee",installmentsQuery.getLatePaymentFee());
        }
        

        if(installmentsQuery.getPartnerId_array()!=null){
            if(installmentsQuery.getPartnerId_array().length==2){
                query.setParameter("partnerId1",installmentsQuery.getPartnerId_array()[0]);
                queryTotal.setParameter("partnerId1",installmentsQuery.getPartnerId_array()[0]);

                query.setParameter("partnerId2",installmentsQuery.getPartnerId_array()[1]);
                queryTotal.setParameter("partnerId2",installmentsQuery.getPartnerId_array()[1]);
            }
        }
        
        if(installmentsQuery.getPartnerId()!=null){
            query.setParameter("partnerId",installmentsQuery.getPartnerId());
            queryTotal.setParameter("partnerId",installmentsQuery.getPartnerId());
        }
        

        if(installmentsQuery.getStatus_array()!=null){
            if(installmentsQuery.getStatus_array().length==2){
                query.setParameter("status1",installmentsQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",installmentsQuery.getStatus_array()[0]);

                query.setParameter("status2",installmentsQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",installmentsQuery.getStatus_array()[1]);
            }
        }
        
        if(installmentsQuery.getStatus()!=null){
            query.setParameter("status",installmentsQuery.getStatus());
            queryTotal.setParameter("status",installmentsQuery.getStatus());
        }
        

        if(installmentsQuery.getCustomerId()!=null){
            query.setParameter("customerId",qb_CustomerId.getValue());
            queryTotal.setParameter("customerId",qb_CustomerId.getValue());
        }
        

          if(installmentsQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  