package com.java.bnpl.installments;
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
@RequestMapping(path="installments")
public class InstallmentsController {
    @Autowired

    private final InstallmentsService installmentsService;
    public InstallmentsController(InstallmentsService installmentsService){
        this.installmentsService = installmentsService;
        
    }

   @GetMapping
   public PagingData<List<Installments>> getInstallmentss(@Validated(IDataSearch.class) InstallmentsQuery installmentsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return installmentsService.getInstallmentss(installmentsQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{installmentsId}")
   public Installments getInstallments(@PathVariable("installmentsId") Long id){
        try{
            return installmentsService.getInstallments(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<Installments> getInstallmentsAll(@Validated(IDataSearch.class) InstallmentsQuery installmentsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return installmentsService.getInstallmentsAll(installmentsQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<Installments> getInstallmentsSuggestions(@Validated(IDataSearch.class) InstallmentsQuery installmentsQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return installmentsService.getInstallmentsSuggestions(installmentsQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public Installments addNewInstallments(@RequestBody @Validated(IDataAdd.class) Installments installments, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return installmentsService.addNewInstallments(installments);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{installmentsId}")
   public void deleteInstallments(@PathVariable("installmentsId") Long id){
         try {
            installmentsService.deleteInstallments(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{installmentsId}")
   public void updateInstallments(@PathVariable("installmentsId") Long id,@RequestBody Installments installments, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        installmentsService.updateInstallments(id,installments);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
