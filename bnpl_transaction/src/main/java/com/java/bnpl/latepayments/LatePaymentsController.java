package com.java.bnpl.latepayments;
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
@RequestMapping(path="latepayments")
public class LatePaymentsController {
    @Autowired

    private final LatePaymentsService latepaymentsService;
    public LatePaymentsController(LatePaymentsService latepaymentsService){
        this.latepaymentsService = latepaymentsService;
        
    }

   @GetMapping
   public PagingData<List<LatePayments>> getLatePaymentss(@Validated(IDataSearch.class) LatePaymentsQuery latepaymentsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return latepaymentsService.getLatePaymentss(latepaymentsQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{latepaymentsId}")
   public LatePayments getLatePayments(@PathVariable("latepaymentsId") Long id){
        try{
            return latepaymentsService.getLatePayments(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<LatePayments> getLatePaymentsAll(@Validated(IDataSearch.class) LatePaymentsQuery latepaymentsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return latepaymentsService.getLatePaymentsAll(latepaymentsQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<LatePayments> getLatePaymentsSuggestions(@Validated(IDataSearch.class) LatePaymentsQuery latepaymentsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return latepaymentsService.getLatePaymentsSuggestions(latepaymentsQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public LatePayments addNewLatePayments(@RequestBody @Validated(IDataAdd.class) LatePayments latepayments, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return latepaymentsService.addNewLatePayments(latepayments);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{latepaymentsId}")
   public void deleteLatePayments(@PathVariable("latepaymentsId") Long id){
         try {
            latepaymentsService.deleteLatePayments(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{latepaymentsId}")
   public void updateLatePayments(@PathVariable("latepaymentsId") Long id,@RequestBody LatePayments latepayments, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        latepaymentsService.updateLatePayments(id,latepayments);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
