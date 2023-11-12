package com.java.bnpl.customer;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import com.java.bnpl.ucodeutility.PagingData;
import com.java.bnpl.exception.CustomeException;
import com.java.bnpl.interfaces.IDataAdd;
import com.java.bnpl.interfaces.IDataUpdate;
import com.java.bnpl.interfaces.IDataSearch;
import com.java.bnpl.message.ResponseMessage;
import com.java.bnpl.ucodeutility.QueryEnum;

import com.java.bnpl.service.FilesStorageService;

@RestController
@RequestMapping(path="customer")
public class CustomerController {
    @Autowired

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
        
    }

   @GetMapping
   public PagingData<List<Customer>> getCustomers(@Validated(IDataSearch.class) CustomerQuery customerQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return customerService.getCustomers(customerQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{customerId}")
   public Customer getCustomer(@PathVariable("customerId") Long id){
        try{
            return customerService.getCustomer(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<Customer> getCustomerAll(@Validated(IDataSearch.class) CustomerQuery customerQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return customerService.getCustomerAll(customerQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<Customer> getCustomerSuggestions(@Validated(IDataSearch.class) CustomerQuery customerQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return customerService.getCustomerSuggestions(customerQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public Customer addNewCustomer(@RequestBody @Validated(IDataAdd.class) Customer customer, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return customerService.addNewCustomer(customer);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{customerId}")
   public void deleteCustomer(@PathVariable("customerId") Long id){
         try {
            customerService.deleteCustomer(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{customerId}")
   public void updateCustomer(@PathVariable("customerId") Long id,@RequestBody Customer customer, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        customerService.updateCustomer(id,customer);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
