package com.java.bnpl.customer;
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
public class CustomerService {

    private final CustomerRepository customerRepository;
    private EntityManager entityManager;

    
    public CustomerService(CustomerRepository customerRepository,EntityManager entityManager){
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Customer>> getCustomers(CustomerQuery customerQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildCustomerQuery(customerQuery);
        
       int size = customerQuery.getLimit()>0 ?customerQuery.getLimit():20;
       int page = customerQuery.getPage()>0 ?customerQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Customer> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Customer getCustomer(Long id){
        CustomerQuery customerQuery=new CustomerQuery();
        customerQuery.setId(id);
        customerQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         customerQuery.setCreatedBy(username);
         */
        List<Query> query=buildCustomerQuery(customerQuery);
        List<Customer> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Customer> getCustomerSuggestions(CustomerQuery customerQuery){
       
        String[] cols={"name","address","email","phone","city","state","postcode"};
        customerQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildCustomerQuery(customerQuery);
        List<Customer> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Customer> getCustomerAll(CustomerQuery customerQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildCustomerQuery(customerQuery);
        List<Customer> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Customer addNewCustomer(Customer customer) {
       
        return customerRepository.save(customer);
    }



    public void deleteCustomer(Long id) {
        CustomerQuery customerQuery=new CustomerQuery();
        customerQuery.setId(id);
        customerQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         customerQuery.setCreateby_mode(QueryEnum.equals);
         customerQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildCustomerQuery(customerQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        customerRepository.deleteById(id);
    }

     @Transactional
     public Customer updateCustomer(Long id, Customer customer) {
        Customer check = customerRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(customer.getName()!=null && customer.getName().length()>0 && !Objects.equals(check.getName(),customer.getName())){
        
        check.setName(customer.getName());
    }
                             
    if(customer.getAddress()!=null && customer.getAddress().length()>0 && !Objects.equals(check.getAddress(),customer.getAddress())){
        
        check.setAddress(customer.getAddress());
    }
                             
    if(customer.getEmail()!=null && customer.getEmail().length()>0 && !Objects.equals(check.getEmail(),customer.getEmail())){
        
        check.setEmail(customer.getEmail());
    }
                             
    if(customer.getPhone()!=null && customer.getPhone().length()>0 && !Objects.equals(check.getPhone(),customer.getPhone())){
        
        check.setPhone(customer.getPhone());
    }
                             
    if(customer.getCreateBy()!=null && customer.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),customer.getCreateBy())){
        
        check.setCreateBy(customer.getCreateBy());
    }

        if(customer.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),customer.getCreateAt())){
            check.setCreateAt(customer.getCreateAt());
        }
                             
    if(customer.getUpdateBy()!=null && customer.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),customer.getUpdateBy())){
        
        check.setUpdateBy(customer.getUpdateBy());
    }

        if(customer.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),customer.getUpdateAt())){
            check.setUpdateAt(customer.getUpdateAt());
        }
                             
    if(customer.getCity()!=null && customer.getCity().length()>0 && !Objects.equals(check.getCity(),customer.getCity())){
        
        check.setCity(customer.getCity());
    }
                             
    if(customer.getState()!=null && customer.getState().length()>0 && !Objects.equals(check.getState(),customer.getState())){
        
        check.setState(customer.getState());
    }
                             
    if(customer.getPostcode()!=null && customer.getPostcode().length()>0 && !Objects.equals(check.getPostcode(),customer.getPostcode())){
        
        check.setPostcode(customer.getPostcode());
    }

        if(customer.getStatus()!=null  && !Objects.equals(check.getStatus(),customer.getStatus())){
            check.setStatus(customer.getStatus());
        }
     return customerRepository.save(check);

    }

   
    private List<Query> buildCustomerQuery(CustomerQuery customerQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Customer s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Customer s where 1=1 ");
        
        QueryBuilder qb_NameArray = new QueryBuilder("name",customerQuery.getName_array());
        if(qb_NameArray.getSql().length()>0){
            sb.append( qb_NameArray.getSql());
            sbTotal.append(qb_NameArray.getSql());
        }
        QueryBuilder qb_Name = new QueryBuilder("name",customerQuery.getName(),customerQuery.getName_mode());
        if(qb_Name.getSql().length()>0){
            sb.append( qb_Name.getSql());
            sbTotal.append(qb_Name.getSql());
        }
        

        QueryBuilder qb_AddressArray = new QueryBuilder("address",customerQuery.getAddress_array());
        if(qb_AddressArray.getSql().length()>0){
            sb.append( qb_AddressArray.getSql());
            sbTotal.append(qb_AddressArray.getSql());
        }
        QueryBuilder qb_Address = new QueryBuilder("address",customerQuery.getAddress(),customerQuery.getAddress_mode());
        if(qb_Address.getSql().length()>0){
            sb.append( qb_Address.getSql());
            sbTotal.append(qb_Address.getSql());
        }
        

        QueryBuilder qb_EmailArray = new QueryBuilder("email",customerQuery.getEmail_array());
        if(qb_EmailArray.getSql().length()>0){
            sb.append( qb_EmailArray.getSql());
            sbTotal.append(qb_EmailArray.getSql());
        }
        QueryBuilder qb_Email = new QueryBuilder("email",customerQuery.getEmail(),customerQuery.getEmail_mode());
        if(qb_Email.getSql().length()>0){
            sb.append( qb_Email.getSql());
            sbTotal.append(qb_Email.getSql());
        }
        

        QueryBuilder qb_PhoneArray = new QueryBuilder("phone",customerQuery.getPhone_array());
        if(qb_PhoneArray.getSql().length()>0){
            sb.append( qb_PhoneArray.getSql());
            sbTotal.append(qb_PhoneArray.getSql());
        }
        QueryBuilder qb_Phone = new QueryBuilder("phone",customerQuery.getPhone(),customerQuery.getPhone_mode());
        if(qb_Phone.getSql().length()>0){
            sb.append( qb_Phone.getSql());
            sbTotal.append(qb_Phone.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",customerQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(customerQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",customerQuery.getId(),customerQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",customerQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",customerQuery.getCreateBy(),customerQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",customerQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",customerQuery.getUpdateBy(),customerQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_CityArray = new QueryBuilder("city",customerQuery.getCity_array());
        if(qb_CityArray.getSql().length()>0){
            sb.append( qb_CityArray.getSql());
            sbTotal.append(qb_CityArray.getSql());
        }
        QueryBuilder qb_City = new QueryBuilder("city",customerQuery.getCity(),customerQuery.getCity_mode());
        if(qb_City.getSql().length()>0){
            sb.append( qb_City.getSql());
            sbTotal.append(qb_City.getSql());
        }
        

        QueryBuilder qb_StateArray = new QueryBuilder("state",customerQuery.getState_array());
        if(qb_StateArray.getSql().length()>0){
            sb.append( qb_StateArray.getSql());
            sbTotal.append(qb_StateArray.getSql());
        }
        QueryBuilder qb_State = new QueryBuilder("state",customerQuery.getState(),customerQuery.getState_mode());
        if(qb_State.getSql().length()>0){
            sb.append( qb_State.getSql());
            sbTotal.append(qb_State.getSql());
        }
        

        QueryBuilder qb_PostcodeArray = new QueryBuilder("postcode",customerQuery.getPostcode_array());
        if(qb_PostcodeArray.getSql().length()>0){
            sb.append( qb_PostcodeArray.getSql());
            sbTotal.append(qb_PostcodeArray.getSql());
        }
        QueryBuilder qb_Postcode = new QueryBuilder("postcode",customerQuery.getPostcode(),customerQuery.getPostcode_mode());
        if(qb_Postcode.getSql().length()>0){
            sb.append( qb_Postcode.getSql());
            sbTotal.append(qb_Postcode.getSql());
        }
        

        QueryBuilder qb_StatusArray = new QueryBuilder("status",customerQuery.getStatus_array());
        if(qb_StatusArray.getSql().length()>0){
            sb.append( qb_StatusArray.getSql());
            sbTotal.append(qb_StatusArray.getSql());
        }
        if(customerQuery.getStatus()!=null){
            QueryBuilder qb_Status = new QueryBuilder("status",customerQuery.getStatus(),customerQuery.getStatus_mode());
            if(qb_Status.getSql().length()>0){
                sb.append( qb_Status.getSql());
                sbTotal.append(qb_Status.getSql());
            }
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(customerQuery.getKeywordColumns(),customerQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(customerQuery.getSortBy()!=null && !customerQuery.getSortBy().isEmpty()){
            if(customerQuery.getSortDirection()!=null &&  customerQuery.getSortDirection().toString()!=""){
                String ascDesc = customerQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+customerQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+customerQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Customer.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Customer.class);
        // Fill Parameters values 

        
        if(customerQuery.getName()!=null){
            query.setParameter("name",qb_Name.getValue());
            queryTotal.setParameter("name",qb_Name.getValue());
        }
        

        if(customerQuery.getAddress()!=null){
            query.setParameter("address",qb_Address.getValue());
            queryTotal.setParameter("address",qb_Address.getValue());
        }
        

        if(customerQuery.getEmail()!=null){
            query.setParameter("email",qb_Email.getValue());
            queryTotal.setParameter("email",qb_Email.getValue());
        }
        

        if(customerQuery.getPhone()!=null){
            query.setParameter("phone",qb_Phone.getValue());
            queryTotal.setParameter("phone",qb_Phone.getValue());
        }
        

        if(customerQuery.getId_array()!=null){
            if(customerQuery.getId_array().length==2){
                query.setParameter("id1",customerQuery.getId_array()[0]);
                queryTotal.setParameter("id1",customerQuery.getId_array()[0]);

                query.setParameter("id2",customerQuery.getId_array()[1]);
                queryTotal.setParameter("id2",customerQuery.getId_array()[1]);
            }
        }
        
        if(customerQuery.getId()!=null){
            query.setParameter("id",customerQuery.getId());
            queryTotal.setParameter("id",customerQuery.getId());
        }
        

        if(customerQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(customerQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(customerQuery.getCity()!=null){
            query.setParameter("city",qb_City.getValue());
            queryTotal.setParameter("city",qb_City.getValue());
        }
        

        if(customerQuery.getState()!=null){
            query.setParameter("state",qb_State.getValue());
            queryTotal.setParameter("state",qb_State.getValue());
        }
        

        if(customerQuery.getPostcode()!=null){
            query.setParameter("postcode",qb_Postcode.getValue());
            queryTotal.setParameter("postcode",qb_Postcode.getValue());
        }
        

        if(customerQuery.getStatus_array()!=null){
            if(customerQuery.getStatus_array().length==2){
                query.setParameter("status1",customerQuery.getStatus_array()[0]);
                queryTotal.setParameter("status1",customerQuery.getStatus_array()[0]);

                query.setParameter("status2",customerQuery.getStatus_array()[1]);
                queryTotal.setParameter("status2",customerQuery.getStatus_array()[1]);
            }
        }
        
        if(customerQuery.getStatus()!=null){
            query.setParameter("status",customerQuery.getStatus());
            queryTotal.setParameter("status",customerQuery.getStatus());
        }
        

          if(customerQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  