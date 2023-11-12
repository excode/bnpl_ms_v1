package com.java.bnpl.refunds;
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
@RequestMapping(path="refunds")
public class RefundsController {
    @Autowired

    private final RefundsService refundsService;
    public RefundsController(RefundsService refundsService){
        this.refundsService = refundsService;
        
    }

   @GetMapping
   public PagingData<List<Refunds>> getRefundss(@Validated(IDataSearch.class) RefundsQuery refundsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return refundsService.getRefundss(refundsQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{refundsId}")
   public Refunds getRefunds(@PathVariable("refundsId") Long id){
        try{
            return refundsService.getRefunds(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<Refunds> getRefundsAll(@Validated(IDataSearch.class) RefundsQuery refundsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return refundsService.getRefundsAll(refundsQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<Refunds> getRefundsSuggestions(@Validated(IDataSearch.class) RefundsQuery refundsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return refundsService.getRefundsSuggestions(refundsQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public Refunds addNewRefunds(@RequestBody @Validated(IDataAdd.class) Refunds refunds, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return refundsService.addNewRefunds(refunds);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{refundsId}")
   public void deleteRefunds(@PathVariable("refundsId") Long id){
         try {
            refundsService.deleteRefunds(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{refundsId}")
   public void updateRefunds(@PathVariable("refundsId") Long id,@RequestBody Refunds refunds, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        refundsService.updateRefunds(id,refunds);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
