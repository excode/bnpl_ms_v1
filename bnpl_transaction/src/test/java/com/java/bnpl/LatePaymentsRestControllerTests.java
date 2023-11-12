
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
import com.java.bnpl.latepayments.LatePayments;
import com.java.bnpl.ucodeutility.PagingData;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LatePaymentsRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private LatePayments oneLatePayments= new LatePayments();   
    
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
    public void testAddLatePayments() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneLatePayments.setCustomerID(1);
    oneLatePayments.setTransactionID(1);
    oneLatePayments.setLatePaymentAmount(1.35);
    oneLatePayments.setLatePaymentDate(LocalDateTime.now().plusDays(1));
    oneLatePayments.setPartnerId(1);
    oneLatePayments.setStatus(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "customerID", oneLatePayments.getCustomerID()>0?oneLatePayments.getCustomerID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", oneLatePayments.getTransactionID()>0?oneLatePayments.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentAmount", oneLatePayments.getLatePaymentAmount()>0?oneLatePayments.getLatePaymentAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentDate", oneLatePayments.getLatePaymentDate()!=null?oneLatePayments.getLatePaymentDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneLatePayments.getPartnerId()>0?oneLatePayments.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneLatePayments.getStatus()>0?oneLatePayments.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/latepayments")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
       // .andExpect(MockMvcResultMatchers.jsonPath("$.latePaymentDate").value(Matchers.containsString(oneLatePayments.getLatePaymentDate())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetLatePaymentss() throws Exception {

        if(oneLatePayments.getLatePaymentDate()==null){
            GetOneLatePayments();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/latepayments")
                    .param("customerID", Integer.toString(oneLatePayments.getCustomerID()))
        .param("customerID_array",Integer.toString(oneLatePayments.getCustomerID()-1))
        .param("customerID_array",Integer.toString(oneLatePayments.getCustomerID()+1))
        .param("transactionID", Integer.toString(oneLatePayments.getTransactionID()))
        .param("transactionID_array",Integer.toString(oneLatePayments.getTransactionID()-1))
        .param("transactionID_array",Integer.toString(oneLatePayments.getTransactionID()+1))
        .param("latePaymentAmount", Double.toString(oneLatePayments.getLatePaymentAmount()))
        .param("latePaymentAmount_array",Double.toString(oneLatePayments.getLatePaymentAmount()-1))
        .param("latePaymentAmount_array",Double.toString(oneLatePayments.getLatePaymentAmount()+1))
        .param("latePaymentDateStr", oneLatePayments.getLatePaymentDate().toString())
        .param("latePaymentDate_array",oneLatePayments.getLatePaymentDate().minusDays(2).toString())
        .param("latePaymentDate_array",oneLatePayments.getLatePaymentDate().plusDays(2).toString())
        .param("partnerId", Integer.toString(oneLatePayments.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneLatePayments.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneLatePayments.getPartnerId()+1))
        .param("status", Integer.toString(oneLatePayments.getStatus()))
        .param("status_array",Integer.toString(oneLatePayments.getStatus()-1))
        .param("status_array",Integer.toString(oneLatePayments.getStatus()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            //.andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneLatePayments.getLatePaymentDate()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateLatePaymentsByID() throws Exception {
        if(oneLatePayments.getLatePaymentDate()==null){
            GetOneLatePayments();
        }
        LatePayments updateLatePayments = new LatePayments();
            updateLatePayments.setCustomerID(1);
    updateLatePayments.setTransactionID(1);
    updateLatePayments.setLatePaymentAmount(1.35);
    updateLatePayments.setLatePaymentDate(LocalDateTime.now().plusDays(1));
    updateLatePayments.setPartnerId(1);
    updateLatePayments.setStatus(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "customerID", updateLatePayments.getCustomerID()>0?updateLatePayments.getCustomerID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", updateLatePayments.getTransactionID()>0?updateLatePayments.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentAmount", updateLatePayments.getLatePaymentAmount()>0?updateLatePayments.getLatePaymentAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentDate", updateLatePayments.getLatePaymentDate()!=null?updateLatePayments.getLatePaymentDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateLatePayments.getPartnerId()>0?updateLatePayments.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateLatePayments.getStatus()>0?updateLatePayments.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/latepayments/"+oneLatePayments.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
               // .andExpect(MockMvcResultMatchers.jsonPath("$.latePaymentDate").value(Matchers.containsString(updateLatePayments.getLatePaymentDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetLatePaymentsByID() throws Exception {
        if(oneLatePayments.getLatePaymentDate()==null){
        GetOneLatePayments();
        }
       
        String path = "/latepayments/"+oneLatePayments.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
               // .andExpect(MockMvcResultMatchers.jsonPath("$.latePaymentDate").value(Matchers.containsString(oneLatePayments.getLatePaymentDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteLatePaymentsByID() throws Exception {
        if(oneLatePayments.getLatePaymentDate()==null){
            GetOneLatePayments();
        }
        String path = "/latepayments/"+oneLatePayments.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneLatePayments() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/latepayments")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneLatePayments_ = objectMapper.readValue(response,PagingData.class);
        if(oneLatePayments_.getDocs().size()>0){
        int i =oneLatePayments_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneLatePayments_.getDocs();
        LatePayments obj =convertToLatePayments( one.get(i-1)); // last one
        this.oneLatePayments = obj;
        }
     }
    private static LatePayments convertToLatePayments(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a LatePayments object
        
        // Retrieve other properties as needed
        LatePayments objCls = new LatePayments();
        
          String customerID = (String) entry.get("customerID");
          Integer customerID_ = Integer.parseInt(customerID);
          objCls.setCustomerID(customerID_);
                      

          String transactionID = (String) entry.get("transactionID");
          Integer transactionID_ = Integer.parseInt(transactionID);
          objCls.setTransactionID(transactionID_);
                      

          String latePaymentAmount = (String) entry.get("latePaymentAmount");
          double latePaymentAmount_ = Double.parseDouble(latePaymentAmount);
          objCls.setLatePaymentAmount(latePaymentAmount_);
                  

          String latePaymentDate = (String) entry.get("latePaymentDate");
          LocalDateTime latePaymentDate_ = LocalDateTime.parse(latePaymentDate);
          objCls.setLatePaymentDate(latePaymentDate_);
                      

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
    