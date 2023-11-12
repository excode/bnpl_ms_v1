package com.java.bnpl.partner;
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
@RequestMapping(path="partner")
public class PartnerController {
    @Autowired
   FilesStorageService storageService;
    private final PartnerService partnerService;
    public PartnerController(PartnerService partnerService){
        this.partnerService = partnerService;
        
    }

   @GetMapping
   public PagingData<List<Partner>> getPartners(@Validated(IDataSearch.class) PartnerQuery partnerQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return partnerService.getPartners(partnerQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{partnerId}")
   public Partner getPartner(@PathVariable("partnerId") Long id){
        try{
            return partnerService.getPartner(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<Partner> getPartnerAll(@Validated(IDataSearch.class) PartnerQuery partnerQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return partnerService.getPartnerAll(partnerQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<Partner> getPartnerSuggestions(@Validated(IDataSearch.class) PartnerQuery partnerQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return partnerService.getPartnerSuggestions(partnerQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public Partner addNewPartner(@RequestBody @Validated(IDataAdd.class) Partner partner, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return partnerService.addNewPartner(partner);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
        @PostMapping(path="/reg")
        public Partner addNewPartner(@RequestBody @Validated(IDataAdd.class) Partner partner){
           try{
             return partnerService.addNewPartner(partner);
           }catch(Exception e){
             throw new CustomeException(e.getMessage(),null);
           }
          
        }
    
   
    @PostMapping(path="/upload/{columnName}/{partnerId}")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile file,@PathVariable("columnName") String columnName,@PathVariable("partnerId") Long id ) {
        try{
            UUID uuid = UUID.randomUUID();
            String savedfileName="";
            String leading= uuid.toString();
            storageService.save(file,leading);
            Partner partner = new Partner(id,leading+file.getOriginalFilename(),columnName);
            Partner partner_upload = partnerService.updatePartner(id,partner);
            
        if(columnName.toLowerCase().equals("logo")){
            savedfileName= partner_upload.getLogo();
        }
        
        else if(columnName.toLowerCase().equals("agreement")){
            savedfileName= partner_upload.getAgreement();
        }
            
            return savedfileName;
        }catch(Exception e){
            throw new CustomeException(e.getMessage(),null);
        }
    }
        

   @DeleteMapping(path="{partnerId}")
   public void deletePartner(@PathVariable("partnerId") Long id){
         try {
            partnerService.deletePartner(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{partnerId}")
   public void updatePartner(@PathVariable("partnerId") Long id,@RequestBody Partner partner, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        partnerService.updatePartner(id,partner);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
