
package com.java.bnpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.bnpl.auth.AuthenticationResponse;
import com.java.bnpl.dues.Dues;
import com.java.bnpl.ucodeutility.PagingData;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DuesRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Dues oneDues= new Dues();   
    
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
    public void testAddDues() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneDues.setCustomerID(1);
    oneDues.setTransactionID(1.35);
    oneDues.setDueAmount(1.35);
    oneDues.setDueDate(LocalDateTime.now().plusDays(1));
    oneDues.setLatePaymentFeePolicy("euclf");
    oneDues.setPartnerId(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "customerID", oneDues.getCustomerID()>0?oneDues.getCustomerID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", oneDues.getTransactionID()>0?oneDues.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "dueAmount", oneDues.getDueAmount()>0?oneDues.getDueAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "dueDate", oneDues.getDueDate()!=null?oneDues.getDueDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentFeePolicy", oneDues.getLatePaymentFeePolicy()!=null?oneDues.getLatePaymentFeePolicy():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneDues.getPartnerId()>0?oneDues.getPartnerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/dues")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        //.andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(Matchers.containsString(oneDues.getDueDate())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetDuess() throws Exception {

        if(oneDues.getDueDate()==null){
            GetOneDues();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/dues")
                    .param("customerID", Integer.toString(oneDues.getCustomerID()))
        .param("customerID_array",Integer.toString(oneDues.getCustomerID()-1))
        .param("customerID_array",Integer.toString(oneDues.getCustomerID()+1))
        .param("transactionID", Double.toString(oneDues.getTransactionID()))
        .param("transactionID_array",Double.toString(oneDues.getTransactionID()-1))
        .param("transactionID_array",Double.toString(oneDues.getTransactionID()+1))
        .param("dueAmount", Double.toString(oneDues.getDueAmount()))
        .param("dueAmount_array",Double.toString(oneDues.getDueAmount()-1))
        .param("dueAmount_array",Double.toString(oneDues.getDueAmount()+1))
        .param("dueDateStr", oneDues.getDueDate().toString())
        .param("dueDate_array",oneDues.getDueDate().minusDays(2).toString())
        .param("dueDate_array",oneDues.getDueDate().plusDays(2).toString())
        .param("partnerId", Integer.toString(oneDues.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneDues.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneDues.getPartnerId()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
           // .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneDues.getDueDate()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateDuesByID() throws Exception {
        if(oneDues.getDueDate()==null){
            GetOneDues();
        }
        Dues updateDues = new Dues();
            updateDues.setCustomerID(1);
    updateDues.setTransactionID(1.35);
    updateDues.setDueAmount(1.35);
    updateDues.setDueDate(LocalDateTime.now().plusDays(1));
    updateDues.setLatePaymentFeePolicy("magna");
    updateDues.setPartnerId(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "customerID", updateDues.getCustomerID()>0?updateDues.getCustomerID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", updateDues.getTransactionID()>0?updateDues.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "dueAmount", updateDues.getDueAmount()>0?updateDues.getDueAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "dueDate", updateDues.getDueDate()!=null?updateDues.getDueDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentFeePolicy", updateDues.getLatePaymentFeePolicy()!=null?updateDues.getLatePaymentFeePolicy():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateDues.getPartnerId()>0?updateDues.getPartnerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/dues/"+oneDues.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
               // .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(Matchers.containsString(updateDues.getDueDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetDuesByID() throws Exception {
        if(oneDues.getDueDate()==null){
        GetOneDues();
        }
       
        String path = "/dues/"+oneDues.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(Matchers.containsString(oneDues.getDueDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteDuesByID() throws Exception {
        if(oneDues.getDueDate()==null){
            GetOneDues();
        }
        String path = "/dues/"+oneDues.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneDues() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/dues")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneDues_ = objectMapper.readValue(response,PagingData.class);
        if(oneDues_.getDocs().size()>0){
        int i =oneDues_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneDues_.getDocs();
        Dues obj =convertToDues( one.get(i-1)); // last one
        this.oneDues = obj;
        }
     }
    private static Dues convertToDues(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Dues object
        
        // Retrieve other properties as needed
        Dues objCls = new Dues();
        
          String customerID = (String) entry.get("customerID");
          Integer customerID_ = Integer.parseInt(customerID);
          objCls.setCustomerID(customerID_);
                      

          String transactionID = (String) entry.get("transactionID");
          double transactionID_ = Double.parseDouble(transactionID);
          objCls.setTransactionID(transactionID_);
                  

          String dueAmount = (String) entry.get("dueAmount");
          double dueAmount_ = Double.parseDouble(dueAmount);
          objCls.setDueAmount(dueAmount_);
                  

          String dueDate = (String) entry.get("dueDate");
          LocalDateTime dueDate_ = LocalDateTime.parse(dueDate);
          objCls.setDueDate(dueDate_);
                      

          String latePaymentFeePolicy = (String) entry.get("latePaymentFeePolicy");
          objCls.setLatePaymentFeePolicy(latePaymentFeePolicy);
              

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
    