
package com.java.bnpl;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.java.bnpl.auth.AuthenticationResponse;
import com.java.bnpl.apikeys.APIKeys;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIKeysRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private APIKeys oneAPIKeys= new APIKeys();   
    
    private void loginAndGetToken() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@ucode.ai\", \"password\": \"123456\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        this.token = extractTokenFromResponse(response);
    }
    private String extractTokenFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AuthenticationResponse json = objectMapper.readValue(response, AuthenticationResponse.class);
            return json.getAccessToken();
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Test
    public void testAddAPIKeys() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneAPIKeys.setKeyValue("pariatur");
    oneAPIKeys.setPermissions("nulla");
    oneAPIKeys.setIssueDate(LocalDateTime.now().plusDays(1));
    oneAPIKeys.setExpiryDate(LocalDateTime.now().plusDays(1));
    oneAPIKeys.setPartnerId(1);
    oneAPIKeys.setStatus(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "keyValue", oneAPIKeys.getKeyValue()!=null?oneAPIKeys.getKeyValue():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "permissions", oneAPIKeys.getPermissions()!=null?oneAPIKeys.getPermissions():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "issueDate", oneAPIKeys.getIssueDate()!=null?oneAPIKeys.getIssueDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "expiryDate", oneAPIKeys.getExpiryDate()!=null?oneAPIKeys.getExpiryDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneAPIKeys.getPartnerId()>0?oneAPIKeys.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneAPIKeys.getStatus()>0?oneAPIKeys.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/apikeys")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.keyValue").value(Matchers.containsString(oneAPIKeys.getKeyValue())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetAPIKeyss() throws Exception {

        if(oneAPIKeys.getKeyValue()==null){
            GetOneAPIKeys();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/apikeys")
                    .param("issueDateStr", oneAPIKeys.getIssueDate().toString())
        .param("issueDate_array",oneAPIKeys.getIssueDate().minusDays(2).toString())
        .param("issueDate_array",oneAPIKeys.getIssueDate().plusDays(2).toString())
        .param("expiryDateStr", oneAPIKeys.getExpiryDate().toString())
        .param("expiryDate_array",oneAPIKeys.getExpiryDate().minusDays(2).toString())
        .param("expiryDate_array",oneAPIKeys.getExpiryDate().plusDays(2).toString())
        .param("partnerId", Integer.toString(oneAPIKeys.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneAPIKeys.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneAPIKeys.getPartnerId()+1))
        .param("status", Integer.toString(oneAPIKeys.getStatus()))
        .param("status_array",Integer.toString(oneAPIKeys.getStatus()-1))
        .param("status_array",Integer.toString(oneAPIKeys.getStatus()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneAPIKeys.getKeyValue()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateAPIKeysByID() throws Exception {
        if(oneAPIKeys.getKeyValue()==null){
            GetOneAPIKeys();
        }
        APIKeys updateAPIKeys = new APIKeys();
            updateAPIKeys.setKeyValue("adclf");
    updateAPIKeys.setPermissions("sint");
    updateAPIKeys.setIssueDate(LocalDateTime.now().plusDays(1));
    updateAPIKeys.setExpiryDate(LocalDateTime.now().plusDays(1));
    updateAPIKeys.setPartnerId(1);
    updateAPIKeys.setStatus(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "keyValue", updateAPIKeys.getKeyValue()!=null?updateAPIKeys.getKeyValue():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "permissions", updateAPIKeys.getPermissions()!=null?updateAPIKeys.getPermissions():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "issueDate", updateAPIKeys.getIssueDate()!=null?updateAPIKeys.getIssueDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "expiryDate", updateAPIKeys.getExpiryDate()!=null?updateAPIKeys.getExpiryDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateAPIKeys.getPartnerId()>0?updateAPIKeys.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateAPIKeys.getStatus()>0?updateAPIKeys.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/apikeys/"+oneAPIKeys.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.keyValue").value(Matchers.containsString(updateAPIKeys.getKeyValue())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetAPIKeysByID() throws Exception {
        if(oneAPIKeys.getKeyValue()==null){
        GetOneAPIKeys();
        }
       
        String path = "/apikeys/"+oneAPIKeys.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.keyValue").value(Matchers.containsString(oneAPIKeys.getKeyValue())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteAPIKeysByID() throws Exception {
        if(oneAPIKeys.getKeyValue()==null){
            GetOneAPIKeys();
        }
        String path = "/apikeys/"+oneAPIKeys.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneAPIKeys() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/apikeys")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneAPIKeys_ = objectMapper.readValue(response,PagingData.class);
        if(oneAPIKeys_.getDocs().size()>0){
        int i =oneAPIKeys_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneAPIKeys_.getDocs();
        APIKeys obj =convertToAPIKeys( one.get(i-1)); // last one
        this.oneAPIKeys = obj;
        }
     }
    private static APIKeys convertToAPIKeys(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a APIKeys object
        
        // Retrieve other properties as needed
        APIKeys objCls = new APIKeys();
        
          String keyValue = (String) entry.get("keyValue");
          objCls.setKeyValue(keyValue);
              

          String permissions = (String) entry.get("permissions");
          objCls.setPermissions(permissions);
              

          String issueDate = (String) entry.get("issueDate");
          LocalDateTime issueDate_ = LocalDateTime.parse(issueDate);
          objCls.setIssueDate(issueDate_);
                      

          String expiryDate = (String) entry.get("expiryDate");
          LocalDateTime expiryDate_ = LocalDateTime.parse(expiryDate);
          objCls.setExpiryDate(expiryDate_);
                      

          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      

          String status = (String) entry.get("status");
          Integer status_ = Integer.parseInt(status);
          objCls.setStatus(status_);
                      
        
    String id = String.valueOf(entry.get("id"));
    Long id_= Long.valueOf(id);
    objCls.setId(id_);
            
        return objCls;
    }
    String  arrayToString(String[] array){
        String format = String.join(", ", Collections.nCopies(array.length, "\"%s\""));
        String formattedString = String.format(format, (Object[]) array);
        String formatted=String.format("[%s]",  formattedString);
       return  formatted ;
    }

}
    