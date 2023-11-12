package com.java.bnpl.paymentlinks;
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
@RequestMapping(path="paymentlinks")
public class PaymentLinksController {
    @Autowired

    private final PaymentLinksService paymentlinksService;
    public PaymentLinksController(PaymentLinksService paymentlinksService){
        this.paymentlinksService = paymentlinksService;
        
    }

   @GetMapping
   public PagingData<List<PaymentLinks>> getPaymentLinkss(@Validated(IDataSearch.class) PaymentLinksQuery paymentlinksQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return paymentlinksService.getPaymentLinkss(paymentlinksQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{paymentlinksId}")
   public PaymentLinks getPaymentLinks(@PathVariable("paymentlinksId") Long id){
        try{
            return paymentlinksService.getPaymentLinks(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<PaymentLinks> getPaymentLinksAll(@Validated(IDataSearch.class) PaymentLinksQuery paymentlinksQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return paymentlinksService.getPaymentLinksAll(paymentlinksQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<PaymentLinks> getPaymentLinksSuggestions(@Validated(IDataSearch.class) PaymentLinksQuery paymentlinksQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return paymentlinksService.getPaymentLinksSuggestions(paymentlinksQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public PaymentLinks addNewPaymentLinks(@RequestBody @Validated(IDataAdd.class) PaymentLinks paymentlinks, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return paymentlinksService.addNewPaymentLinks(paymentlinks);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{paymentlinksId}")
   public void deletePaymentLinks(@PathVariable("paymentlinksId") Long id){
         try {
            paymentlinksService.deletePaymentLinks(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{paymentlinksId}")
   public void updatePaymentLinks(@PathVariable("paymentlinksId") Long id,@RequestBody PaymentLinks paymentlinks, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        paymentlinksService.updatePaymentLinks(id,paymentlinks);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
