
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
import com.java.bnpl.transaction.Transaction;
import com.java.bnpl.ucodeutility.PagingData;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Transaction oneTransaction= new Transaction();   
    
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
    public void testAddTransaction() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneTransaction.setCustomerId(1);
    oneTransaction.setTransactionDate(LocalDateTime.now().plusDays(1));
    oneTransaction.setTransactionAmount(1.35);
    oneTransaction.setStatus(1);
    oneTransaction.setPaymentMethod("consectetur");
    oneTransaction.setNextPaymentDate(LocalDateTime.now().plusDays(1));
    oneTransaction.setPartnerId(1);
    oneTransaction.setPlan(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "customerId", oneTransaction.getCustomerId()>0?oneTransaction.getCustomerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionDate", oneTransaction.getTransactionDate()!=null?oneTransaction.getTransactionDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionAmount", oneTransaction.getTransactionAmount()>0?oneTransaction.getTransactionAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneTransaction.getStatus()>0?oneTransaction.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "paymentMethod", oneTransaction.getPaymentMethod()!=null?oneTransaction.getPaymentMethod():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "nextPaymentDate", oneTransaction.getNextPaymentDate()!=null?oneTransaction.getNextPaymentDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneTransaction.getPartnerId()>0?oneTransaction.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "plan", oneTransaction.getPlan()>0?oneTransaction.getPlan():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
       // .andExpect(MockMvcResultMatchers.jsonPath("$.transactionDate").value(Matchers.containsString(oneTransaction.getTransactionDate())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetTransactions() throws Exception {

        if(oneTransaction.getTransactionDate()==null){
            GetOneTransaction();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction")
                    .param("customerId", Integer.toString(oneTransaction.getCustomerId()))
        .param("customerId_array",Integer.toString(oneTransaction.getCustomerId()-1))
        .param("customerId_array",Integer.toString(oneTransaction.getCustomerId()+1))
        .param("transactionDateStr", oneTransaction.getTransactionDate().toString())
        .param("transactionDate_array",oneTransaction.getTransactionDate().minusDays(2).toString())
        .param("transactionDate_array",oneTransaction.getTransactionDate().plusDays(2).toString())
        .param("transactionAmount", Double.toString(oneTransaction.getTransactionAmount()))
        .param("transactionAmount_array",Double.toString(oneTransaction.getTransactionAmount()-1))
        .param("transactionAmount_array",Double.toString(oneTransaction.getTransactionAmount()+1))
        .param("status", Integer.toString(oneTransaction.getStatus()))
        .param("status_array",Integer.toString(oneTransaction.getStatus()-1))
        .param("status_array",Integer.toString(oneTransaction.getStatus()+1))
        .param("paymentMethod", sortenString(oneTransaction.getPaymentMethod()))
        .param("paymentMethod_array","dummy1")
        .param("paymentMethod_array",oneTransaction.getPaymentMethod())
        .param("paymentMethod_array","dummy2")
        .param("nextPaymentDateStr", oneTransaction.getNextPaymentDate().toString())
        .param("nextPaymentDate_array",oneTransaction.getNextPaymentDate().minusDays(2).toString())
        .param("nextPaymentDate_array",oneTransaction.getNextPaymentDate().plusDays(2).toString())
        .param("partnerId", Integer.toString(oneTransaction.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneTransaction.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneTransaction.getPartnerId()+1))
        .param("plan", Integer.toString(oneTransaction.getPlan()))
        .param("plan_array",Integer.toString(oneTransaction.getPlan()-1))
        .param("plan_array",Integer.toString(oneTransaction.getPlan()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
          // .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneTransaction.getTransactionDate()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateTransactionByID() throws Exception {
        if(oneTransaction.getTransactionDate()==null){
            GetOneTransaction();
        }
        Transaction updateTransaction = new Transaction();
            updateTransaction.setCustomerId(1);
    updateTransaction.setTransactionDate(LocalDateTime.now().plusDays(1));
    updateTransaction.setTransactionAmount(1.35);
    updateTransaction.setStatus(1);
    updateTransaction.setPaymentMethod("euclf");
    updateTransaction.setNextPaymentDate(LocalDateTime.now().plusDays(1));
    updateTransaction.setPartnerId(1);
    updateTransaction.setPlan(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "customerId", updateTransaction.getCustomerId()>0?updateTransaction.getCustomerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionDate", updateTransaction.getTransactionDate()!=null?updateTransaction.getTransactionDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "transactionAmount", updateTransaction.getTransactionAmount()>0?updateTransaction.getTransactionAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateTransaction.getStatus()>0?updateTransaction.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "paymentMethod", updateTransaction.getPaymentMethod()!=null?updateTransaction.getPaymentMethod():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "nextPaymentDate", updateTransaction.getNextPaymentDate()!=null?updateTransaction.getNextPaymentDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateTransaction.getPartnerId()>0?updateTransaction.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "plan", updateTransaction.getPlan()>0?updateTransaction.getPlan():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/transaction/"+oneTransaction.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.transactionDate").value(Matchers.containsString(updateTransaction.getTransactionDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetTransactionByID() throws Exception {
        if(oneTransaction.getTransactionDate()==null){
        GetOneTransaction();
        }
       
        String path = "/transaction/"+oneTransaction.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
               // .andExpect(MockMvcResultMatchers.jsonPath("$.transactionDate").value(Matchers.containsString(oneTransaction.getTransactionDate())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteTransactionByID() throws Exception {
        if(oneTransaction.getTransactionDate()==null){
            GetOneTransaction();
        }
        String path = "/transaction/"+oneTransaction.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneTransaction() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/transaction")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneTransaction_ = objectMapper.readValue(response,PagingData.class);
        if(oneTransaction_.getDocs().size()>0){
        int i =oneTransaction_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneTransaction_.getDocs();
        Transaction obj =convertToTransaction( one.get(i-1)); // last one
        this.oneTransaction = obj;
        }
     }
    private static Transaction convertToTransaction(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Transaction object
        
        // Retrieve other properties as needed
        Transaction objCls = new Transaction();
        
          String customerId = (String) entry.get("customerId");
          Integer customerId_ = Integer.parseInt(customerId);
          objCls.setCustomerId(customerId_);
                      

          String transactionDate = (String) entry.get("transactionDate");
          LocalDateTime transactionDate_ = LocalDateTime.parse(transactionDate);
          objCls.setTransactionDate(transactionDate_);
                      

          String transactionAmount = (String) entry.get("transactionAmount");
          double transactionAmount_ = Double.parseDouble(transactionAmount);
          objCls.setTransactionAmount(transactionAmount_);
                  

          String status = (String) entry.get("status");
          Integer status_ = Integer.parseInt(status);
          objCls.setStatus(status_);
                      

          String paymentMethod = (String) entry.get("paymentMethod");
          objCls.setPaymentMethod(paymentMethod);
              

          String nextPaymentDate = (String) entry.get("nextPaymentDate");
          LocalDateTime nextPaymentDate_ = LocalDateTime.parse(nextPaymentDate);
          objCls.setNextPaymentDate(nextPaymentDate_);
                      

          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      

          String plan = (String) entry.get("plan");
          Integer plan_ = Integer.parseInt(plan);
          objCls.setPlan(plan_);
                      
        
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
    