package com.java.bnpl.apiactivity;
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
@RequestMapping(path="apiactivity")
public class APIActivityController {
    @Autowired

    private final APIActivityService apiactivityService;
    public APIActivityController(APIActivityService apiactivityService){
        this.apiactivityService = apiactivityService;
        
    }

   @GetMapping
   public PagingData<List<APIActivity>> getAPIActivitys(@Validated(IDataSearch.class) APIActivityQuery apiactivityQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return apiactivityService.getAPIActivitys(apiactivityQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{apiactivityId}")
   public APIActivity getAPIActivity(@PathVariable("apiactivityId") Long id){
        try{
            return apiactivityService.getAPIActivity(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<APIActivity> getAPIActivityAll(@Validated(IDataSearch.class) APIActivityQuery apiactivityQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return apiactivityService.getAPIActivityAll(apiactivityQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<APIActivity> getAPIActivitySuggestions(@Validated(IDataSearch.class) APIActivityQuery apiactivityQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return apiactivityService.getAPIActivitySuggestions(apiactivityQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public APIActivity addNewAPIActivity(@RequestBody @Validated(IDataAdd.class) APIActivity apiactivity, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return apiactivityService.addNewAPIActivity(apiactivity);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }

   
   

   @DeleteMapping(path="{apiactivityId}")
   public void deleteAPIActivity(@PathVariable("apiactivityId") Long id){
         try {
            apiactivityService.deleteAPIActivity(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{apiactivityId}")
   public void updateAPIActivity(@PathVariable("apiactivityId") Long id,@RequestBody APIActivity apiactivity, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        apiactivityService.updateAPIActivity(id,apiactivity);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
