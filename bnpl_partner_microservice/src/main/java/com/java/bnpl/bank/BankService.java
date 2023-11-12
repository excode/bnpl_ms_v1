package com.java.bnpl.bank;
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
public class BankService {

    private final BankRepository bankRepository;
    private EntityManager entityManager;

    
    public BankService(BankRepository bankRepository,EntityManager entityManager){
        this.bankRepository = bankRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Bank>> getBanks(BankQuery bankQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildBankQuery(bankQuery);
        
       int size = bankQuery.getLimit()>0 ?bankQuery.getLimit():20;
       int page = bankQuery.getPage()>0 ?bankQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Bank> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Bank getBank(Long id){
        BankQuery bankQuery=new BankQuery();
        bankQuery.setId(id);
        bankQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         bankQuery.setCreatedBy(username);
         */
        List<Query> query=buildBankQuery(bankQuery);
        List<Bank> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Bank> getBankSuggestions(BankQuery bankQuery){
       
        String[] cols={"name","accountname","swift","city","country","address"};
        bankQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildBankQuery(bankQuery);
        List<Bank> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Bank> getBankAll(BankQuery bankQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildBankQuery(bankQuery);
        List<Bank> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Bank addNewBank(Bank bank) {
       
        return bankRepository.save(bank);
    }



    public void deleteBank(Long id) {
        BankQuery bankQuery=new BankQuery();
        bankQuery.setId(id);
        bankQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         bankQuery.setCreateby_mode(QueryEnum.equals);
         bankQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildBankQuery(bankQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        bankRepository.deleteById(id);
    }

     @Transactional
     public Bank updateBank(Long id, Bank bank) {
        Bank check = bankRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(bank.getName()!=null && bank.getName().length()>0 && !Objects.equals(check.getName(),bank.getName())){
        
        check.setName(bank.getName());
    }
                             
    if(bank.getAccountName()!=null && bank.getAccountName().length()>0 && !Objects.equals(check.getAccountName(),bank.getAccountName())){
        
        check.setAccountName(bank.getAccountName());
    }
                             
    if(bank.getSwift()!=null && bank.getSwift().length()>0 && !Objects.equals(check.getSwift(),bank.getSwift())){
        
        check.setSwift(bank.getSwift());
    }
                             
    if(bank.getCreateBy()!=null && bank.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),bank.getCreateBy())){
        
        check.setCreateBy(bank.getCreateBy());
    }

        if(bank.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),bank.getCreateAt())){
            check.setCreateAt(bank.getCreateAt());
        }
                             
    if(bank.getUpdateBy()!=null && bank.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),bank.getUpdateBy())){
        
        check.setUpdateBy(bank.getUpdateBy());
    }

        if(bank.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),bank.getUpdateAt())){
            check.setUpdateAt(bank.getUpdateAt());
        }

        if(bank.getAccountNumber()!=null  && !Objects.equals(check.getAccountNumber(),bank.getAccountNumber())){
            check.setAccountNumber(bank.getAccountNumber());
        }
                             
    if(bank.getCity()!=null && bank.getCity().length()>0 && !Objects.equals(check.getCity(),bank.getCity())){
        
        check.setCity(bank.getCity());
    }
                             
    if(bank.getCountry()!=null && bank.getCountry().length()>0 && !Objects.equals(check.getCountry(),bank.getCountry())){
        
        check.setCountry(bank.getCountry());
    }
                             
    if(bank.getAddress()!=null && bank.getAddress().length()>0 && !Objects.equals(check.getAddress(),bank.getAddress())){
        
        check.setAddress(bank.getAddress());
    }

        if(bank.getPostcode()!=null  && !Objects.equals(check.getPostcode(),bank.getPostcode())){
            check.setPostcode(bank.getPostcode());
        }
     return bankRepository.save(check);

    }

   
    private List<Query> buildBankQuery(BankQuery bankQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Bank s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Bank s where 1=1 ");
        
        QueryBuilder qb_NameArray = new QueryBuilder("name",bankQuery.getName_array());
        if(qb_NameArray.getSql().length()>0){
            sb.append( qb_NameArray.getSql());
            sbTotal.append(qb_NameArray.getSql());
        }
        QueryBuilder qb_Name = new QueryBuilder("name",bankQuery.getName(),bankQuery.getName_mode());
        if(qb_Name.getSql().length()>0){
            sb.append( qb_Name.getSql());
            sbTotal.append(qb_Name.getSql());
        }
        

        QueryBuilder qb_AccountNameArray = new QueryBuilder("accountName",bankQuery.getAccountName_array());
        if(qb_AccountNameArray.getSql().length()>0){
            sb.append( qb_AccountNameArray.getSql());
            sbTotal.append(qb_AccountNameArray.getSql());
        }
        QueryBuilder qb_AccountName = new QueryBuilder("accountName",bankQuery.getAccountName(),bankQuery.getAccountName_mode());
        if(qb_AccountName.getSql().length()>0){
            sb.append( qb_AccountName.getSql());
            sbTotal.append(qb_AccountName.getSql());
        }
        

        QueryBuilder qb_SwiftArray = new QueryBuilder("swift",bankQuery.getSwift_array());
        if(qb_SwiftArray.getSql().length()>0){
            sb.append( qb_SwiftArray.getSql());
            sbTotal.append(qb_SwiftArray.getSql());
        }
        QueryBuilder qb_Swift = new QueryBuilder("swift",bankQuery.getSwift(),bankQuery.getSwift_mode());
        if(qb_Swift.getSql().length()>0){
            sb.append( qb_Swift.getSql());
            sbTotal.append(qb_Swift.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",bankQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(bankQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",bankQuery.getId(),bankQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",bankQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",bankQuery.getCreateBy(),bankQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",bankQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",bankQuery.getUpdateBy(),bankQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_AccountNumberArray = new QueryBuilder("accountNumber",bankQuery.getAccountNumber_array());
        if(qb_AccountNumberArray.getSql().length()>0){
            sb.append( qb_AccountNumberArray.getSql());
            sbTotal.append(qb_AccountNumberArray.getSql());
        }
        if(bankQuery.getAccountNumber()!=null){
            QueryBuilder qb_AccountNumber = new QueryBuilder("accountNumber",bankQuery.getAccountNumber(),bankQuery.getAccountNumber_mode());
            if(qb_AccountNumber.getSql().length()>0){
                sb.append( qb_AccountNumber.getSql());
                sbTotal.append(qb_AccountNumber.getSql());
            }
        }
        

        QueryBuilder qb_CityArray = new QueryBuilder("city",bankQuery.getCity_array());
        if(qb_CityArray.getSql().length()>0){
            sb.append( qb_CityArray.getSql());
            sbTotal.append(qb_CityArray.getSql());
        }
        QueryBuilder qb_City = new QueryBuilder("city",bankQuery.getCity(),bankQuery.getCity_mode());
        if(qb_City.getSql().length()>0){
            sb.append( qb_City.getSql());
            sbTotal.append(qb_City.getSql());
        }
        

        QueryBuilder qb_CountryArray = new QueryBuilder("country",bankQuery.getCountry_array());
        if(qb_CountryArray.getSql().length()>0){
            sb.append( qb_CountryArray.getSql());
            sbTotal.append(qb_CountryArray.getSql());
        }
        QueryBuilder qb_Country = new QueryBuilder("country",bankQuery.getCountry(),bankQuery.getCountry_mode());
        if(qb_Country.getSql().length()>0){
            sb.append( qb_Country.getSql());
            sbTotal.append(qb_Country.getSql());
        }
        

        QueryBuilder qb_AddressArray = new QueryBuilder("address",bankQuery.getAddress_array());
        if(qb_AddressArray.getSql().length()>0){
            sb.append( qb_AddressArray.getSql());
            sbTotal.append(qb_AddressArray.getSql());
        }
        QueryBuilder qb_Address = new QueryBuilder("address",bankQuery.getAddress(),bankQuery.getAddress_mode());
        if(qb_Address.getSql().length()>0){
            sb.append( qb_Address.getSql());
            sbTotal.append(qb_Address.getSql());
        }
        

        QueryBuilder qb_PostcodeArray = new QueryBuilder("postcode",bankQuery.getPostcode_array());
        if(qb_PostcodeArray.getSql().length()>0){
            sb.append( qb_PostcodeArray.getSql());
            sbTotal.append(qb_PostcodeArray.getSql());
        }
        if(bankQuery.getPostcode()!=null){
            QueryBuilder qb_Postcode = new QueryBuilder("postcode",bankQuery.getPostcode(),bankQuery.getPostcode_mode());
            if(qb_Postcode.getSql().length()>0){
                sb.append( qb_Postcode.getSql());
                sbTotal.append(qb_Postcode.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(bankQuery.getKeywordColumns(),bankQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(bankQuery.getSortBy()!=null && !bankQuery.getSortBy().isEmpty()){
            if(bankQuery.getSortDirection()!=null &&  bankQuery.getSortDirection().toString()!=""){
                String ascDesc = bankQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+bankQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+bankQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Bank.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Bank.class);
        // Fill Parameters values 

        
        if(bankQuery.getName()!=null){
            query.setParameter("name",qb_Name.getValue());
            queryTotal.setParameter("name",qb_Name.getValue());
        }
        

        if(bankQuery.getAccountName()!=null){
            query.setParameter("accountName",qb_AccountName.getValue());
            queryTotal.setParameter("accountName",qb_AccountName.getValue());
        }
        

        if(bankQuery.getSwift()!=null){
            query.setParameter("swift",qb_Swift.getValue());
            queryTotal.setParameter("swift",qb_Swift.getValue());
        }
        

        if(bankQuery.getId_array()!=null){
            if(bankQuery.getId_array().length==2){
                query.setParameter("id1",bankQuery.getId_array()[0]);
                queryTotal.setParameter("id1",bankQuery.getId_array()[0]);

                query.setParameter("id2",bankQuery.getId_array()[1]);
                queryTotal.setParameter("id2",bankQuery.getId_array()[1]);
            }
        }
        
        if(bankQuery.getId()!=null){
            query.setParameter("id",bankQuery.getId());
            queryTotal.setParameter("id",bankQuery.getId());
        }
        

        if(bankQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(bankQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(bankQuery.getAccountNumber_array()!=null){
            if(bankQuery.getAccountNumber_array().length==2){
                query.setParameter("accountNumber1",bankQuery.getAccountNumber_array()[0]);
                queryTotal.setParameter("accountNumber1",bankQuery.getAccountNumber_array()[0]);

                query.setParameter("accountNumber2",bankQuery.getAccountNumber_array()[1]);
                queryTotal.setParameter("accountNumber2",bankQuery.getAccountNumber_array()[1]);
            }
        }
        
        if(bankQuery.getAccountNumber()!=null){
            query.setParameter("accountNumber",bankQuery.getAccountNumber());
            queryTotal.setParameter("accountNumber",bankQuery.getAccountNumber());
        }
        

        if(bankQuery.getCity()!=null){
            query.setParameter("city",qb_City.getValue());
            queryTotal.setParameter("city",qb_City.getValue());
        }
        

        if(bankQuery.getCountry()!=null){
            query.setParameter("country",qb_Country.getValue());
            queryTotal.setParameter("country",qb_Country.getValue());
        }
        

        if(bankQuery.getAddress()!=null){
            query.setParameter("address",qb_Address.getValue());
            queryTotal.setParameter("address",qb_Address.getValue());
        }
        

        if(bankQuery.getPostcode_array()!=null){
            if(bankQuery.getPostcode_array().length==2){
                query.setParameter("postcode1",bankQuery.getPostcode_array()[0]);
                queryTotal.setParameter("postcode1",bankQuery.getPostcode_array()[0]);

                query.setParameter("postcode2",bankQuery.getPostcode_array()[1]);
                queryTotal.setParameter("postcode2",bankQuery.getPostcode_array()[1]);
            }
        }
        
        if(bankQuery.getPostcode()!=null){
            query.setParameter("postcode",bankQuery.getPostcode());
            queryTotal.setParameter("postcode",bankQuery.getPostcode());
        }
        

          if(bankQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  