
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
import com.java.bnpl.settlement.Settlement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SettlementRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Settlement oneSettlement= new Settlement();   
    
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
    public void testAddSettlement() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneSettlement.setAccount("commodo");
    oneSettlement.setDuedate(LocalDateTime.now().plusDays(1));
    oneSettlement.setStatus(1);
    oneSettlement.setPartnerId(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "account", oneSettlement.getAccount()!=null?oneSettlement.getAccount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "duedate", oneSettlement.getDuedate()!=null?oneSettlement.getDuedate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneSettlement.getStatus()>0?oneSettlement.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneSettlement.getPartnerId()>0?oneSettlement.getPartnerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/settlement")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.account").value(Matchers.containsString(oneSettlement.getAccount())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetSettlements() throws Exception {

        if(oneSettlement.getAccount()==null){
            GetOneSettlement();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/settlement")
                    .param("duedateStr", oneSettlement.getDuedate().toString())
        .param("duedate_array",oneSettlement.getDuedate().minusDays(2).toString())
        .param("duedate_array",oneSettlement.getDuedate().plusDays(2).toString())
        .param("status", Integer.toString(oneSettlement.getStatus()))
        .param("status_array",Integer.toString(oneSettlement.getStatus()-1))
        .param("status_array",Integer.toString(oneSettlement.getStatus()+1))
        .param("partnerId", Integer.toString(oneSettlement.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneSettlement.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneSettlement.getPartnerId()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneSettlement.getAccount()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateSettlementByID() throws Exception {
        if(oneSettlement.getAccount()==null){
            GetOneSettlement();
        }
        Settlement updateSettlement = new Settlement();
            updateSettlement.setAccount("occaecat");
    updateSettlement.setDuedate(LocalDateTime.now().plusDays(1));
    updateSettlement.setStatus(1);
    updateSettlement.setPartnerId(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "account", updateSettlement.getAccount()!=null?updateSettlement.getAccount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "duedate", updateSettlement.getDuedate()!=null?updateSettlement.getDuedate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateSettlement.getStatus()>0?updateSettlement.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateSettlement.getPartnerId()>0?updateSettlement.getPartnerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/settlement/"+oneSettlement.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value(Matchers.containsString(updateSettlement.getAccount())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetSettlementByID() throws Exception {
        if(oneSettlement.getAccount()==null){
        GetOneSettlement();
        }
       
        String path = "/settlement/"+oneSettlement.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value(Matchers.containsString(oneSettlement.getAccount())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteSettlementByID() throws Exception {
        if(oneSettlement.getAccount()==null){
            GetOneSettlement();
        }
        String path = "/settlement/"+oneSettlement.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneSettlement() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/settlement")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneSettlement_ = objectMapper.readValue(response,PagingData.class);
        if(oneSettlement_.getDocs().size()>0){
        int i =oneSettlement_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneSettlement_.getDocs();
        Settlement obj =convertToSettlement( one.get(i-1)); // last one
        this.oneSettlement = obj;
        }
     }
    private static Settlement convertToSettlement(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Settlement object
        
        // Retrieve other properties as needed
        Settlement objCls = new Settlement();
        
          String account = (String) entry.get("account");
          objCls.setAccount(account);
              

          String duedate = (String) entry.get("duedate");
          LocalDateTime duedate_ = LocalDateTime.parse(duedate);
          objCls.setDuedate(duedate_);
                      

          String status = (String) entry.get("status");
          Integer status_ = Integer.parseInt(status);
          objCls.setStatus(status_);
                      

          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      
        
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
    