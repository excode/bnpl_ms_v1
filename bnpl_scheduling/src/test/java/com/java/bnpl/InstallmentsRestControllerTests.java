
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
import com.java.bnpl.installments.Installments;
import com.java.bnpl.ucodeutility.PagingData;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InstallmentsRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Installments oneInstallments= new Installments();   
    
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
    public void testAddInstallments() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneInstallments.setTransactionID(1);
    oneInstallments.setInstallmentNumber(1);
    oneInstallments.setInstallmentAmount(1);
    oneInstallments.setDueDate(LocalDateTime.now().plusDays(1));
    oneInstallments.setLatePaymentFee(1.35);
    oneInstallments.setPartnerId(1);
    oneInstallments.setStatus(1);
    oneInstallments.setCustomerId("dolor");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", oneInstallments.getTransactionID()>0?oneInstallments.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "installmentNumber", oneInstallments.getInstallmentNumber()>0?oneInstallments.getInstallmentNumber():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "installmentAmount", oneInstallments.getInstallmentAmount()>0?oneInstallments.getInstallmentAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "dueDate", oneInstallments.getDueDate()!=null?oneInstallments.getDueDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentFee", oneInstallments.getLatePaymentFee()>0?oneInstallments.getLatePaymentFee():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneInstallments.getPartnerId()>0?oneInstallments.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneInstallments.getStatus()>0?oneInstallments.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "customerId", oneInstallments.getCustomerId()!=null?oneInstallments.getCustomerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/installments")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        //.andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(Matchers.containsString(oneInstallments.getDueDate())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetInstallmentss() throws Exception {

        if(oneInstallments.getDueDate()==null){
            GetOneInstallments();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/installments")
                    .param("transactionID", Integer.toString(oneInstallments.getTransactionID()))
        .param("transactionID_array",Integer.toString(oneInstallments.getTransactionID()-1))
        .param("transactionID_array",Integer.toString(oneInstallments.getTransactionID()+1))
        .param("installmentNumber", Integer.toString(oneInstallments.getInstallmentNumber()))
        .param("installmentNumber_array",Integer.toString(oneInstallments.getInstallmentNumber()-1))
        .param("installmentNumber_array",Integer.toString(oneInstallments.getInstallmentNumber()+1))
        .param("installmentAmount", Integer.toString(oneInstallments.getInstallmentAmount()))
        .param("installmentAmount_array",Integer.toString(oneInstallments.getInstallmentAmount()-1))
        .param("installmentAmount_array",Integer.toString(oneInstallments.getInstallmentAmount()+1))
        .param("dueDateStr", oneInstallments.getDueDate().toString())
        .param("dueDate_array",oneInstallments.getDueDate().minusDays(2).toString())
        .param("dueDate_array",oneInstallments.getDueDate().plusDays(2).toString())
        .param("latePaymentFee", Double.toString(oneInstallments.getLatePaymentFee()))
        .param("latePaymentFee_array",Double.toString(oneInstallments.getLatePaymentFee()-1))
        .param("latePaymentFee_array",Double.toString(oneInstallments.getLatePaymentFee()+1))
        .param("partnerId", Integer.toString(oneInstallments.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneInstallments.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneInstallments.getPartnerId()+1))
        .param("status", Integer.toString(oneInstallments.getStatus()))
        .param("status_array",Integer.toString(oneInstallments.getStatus()-1))
        .param("status_array",Integer.toString(oneInstallments.getStatus()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            //.andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneInstallments.getDueDate()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateInstallmentsByID() throws Exception {
        if(oneInstallments.getDueDate()==null){
            GetOneInstallments();
        }
        Installments updateInstallments = new Installments();
            updateInstallments.setTransactionID(1);
    updateInstallments.setInstallmentNumber(1);
    updateInstallments.setInstallmentAmount(1);
    updateInstallments.setDueDate(LocalDateTime.now().plusDays(1));
    updateInstallments.setLatePaymentFee(1.35);
    updateInstallments.setPartnerId(1);
    updateInstallments.setStatus(1);
    updateInstallments.setCustomerId("utclf");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", updateInstallments.getTransactionID()>0?updateInstallments.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "installmentNumber", updateInstallments.getInstallmentNumber()>0?updateInstallments.getInstallmentNumber():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "installmentAmount", updateInstallments.getInstallmentAmount()>0?updateInstallments.getInstallmentAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "dueDate", updateInstallments.getDueDate()!=null?updateInstallments.getDueDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "latePaymentFee", updateInstallments.getLatePaymentFee()>0?updateInstallments.getLatePaymentFee():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateInstallments.getPartnerId()>0?updateInstallments.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateInstallments.getStatus()>0?updateInstallments.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "customerId", updateInstallments.getCustomerId()!=null?updateInstallments.getCustomerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/installments/"+oneInstallments.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(Matchers.containsString(updateInstallments.getDueDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetInstallmentsByID() throws Exception {
        if(oneInstallments.getDueDate()==null){
        GetOneInstallments();
        }
       
        String path = "/installments/"+oneInstallments.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
               // .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(Matchers.containsString(oneInstallments.getDueDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteInstallmentsByID() throws Exception {
        if(oneInstallments.getDueDate()==null){
            GetOneInstallments();
        }
        String path = "/installments/"+oneInstallments.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneInstallments() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/installments")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneInstallments_ = objectMapper.readValue(response,PagingData.class);
        if(oneInstallments_.getDocs().size()>0){
        int i =oneInstallments_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneInstallments_.getDocs();
        Installments obj =convertToInstallments( one.get(i-1)); // last one
        this.oneInstallments = obj;
        }
     }
    private static Installments convertToInstallments(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Installments object
        
        // Retrieve other properties as needed
        Installments objCls = new Installments();
        
          String transactionID = (String) entry.get("transactionID");
          Integer transactionID_ = Integer.parseInt(transactionID);
          objCls.setTransactionID(transactionID_);
                      

          String installmentNumber = (String) entry.get("installmentNumber");
          Integer installmentNumber_ = Integer.parseInt(installmentNumber);
          objCls.setInstallmentNumber(installmentNumber_);
                      

          String installmentAmount = (String) entry.get("installmentAmount");
          Integer installmentAmount_ = Integer.parseInt(installmentAmount);
          objCls.setInstallmentAmount(installmentAmount_);
                      

          String dueDate = (String) entry.get("dueDate");
          LocalDateTime dueDate_ = LocalDateTime.parse(dueDate);
          objCls.setDueDate(dueDate_);
                      

          String latePaymentFee = (String) entry.get("latePaymentFee");
          double latePaymentFee_ = Double.parseDouble(latePaymentFee);
          objCls.setLatePaymentFee(latePaymentFee_);
                  

          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      

          String status = (String) entry.get("status");
          Integer status_ = Integer.parseInt(status);
          objCls.setStatus(status_);
                      

          String customerId = (String) entry.get("customerId");
          objCls.setCustomerId(customerId);
              
        
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
    