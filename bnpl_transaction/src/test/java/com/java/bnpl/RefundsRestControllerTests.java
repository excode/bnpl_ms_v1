
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
import com.java.bnpl.refunds.Refunds;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RefundsRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Refunds oneRefunds= new Refunds();   
    
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
    public void testAddRefunds() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneRefunds.setTransactionID("magna");
    oneRefunds.setRefundAmount(1.35);
    oneRefunds.setReason("Incididunt reprehenderit aute fugiat do aliqua laborum culpa mollit dolor.");
    oneRefunds.setRefundDate(LocalDateTime.now().plusDays(1));
    oneRefunds.setPartnerId(1);
    oneRefunds.setStatus(1);
    oneRefunds.setCustomerId(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", oneRefunds.getTransactionID()!=null?oneRefunds.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "refundAmount", oneRefunds.getRefundAmount()>0?oneRefunds.getRefundAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "reason", oneRefunds.getReason()!=null?oneRefunds.getReason():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "refundDate", oneRefunds.getRefundDate()!=null?oneRefunds.getRefundDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneRefunds.getPartnerId()>0?oneRefunds.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneRefunds.getStatus()>0?oneRefunds.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "customerId", oneRefunds.getCustomerId()>0?oneRefunds.getCustomerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/refunds")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.transactionID").value(Matchers.containsString(oneRefunds.getTransactionID())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetRefundss() throws Exception {

        if(oneRefunds.getTransactionID()==null){
            GetOneRefunds();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/refunds")
                    .param("refundAmount", Double.toString(oneRefunds.getRefundAmount()))
        .param("refundAmount_array",Double.toString(oneRefunds.getRefundAmount()-1))
        .param("refundAmount_array",Double.toString(oneRefunds.getRefundAmount()+1))
        .param("reason", sortenString(oneRefunds.getReason()))
        .param("reason_array","dummy1")
        .param("reason_array",oneRefunds.getReason())
        .param("reason_array","dummy2")
        .param("refundDateStr", oneRefunds.getRefundDate().toString())
        .param("refundDate_array",oneRefunds.getRefundDate().minusDays(2).toString())
        .param("refundDate_array",oneRefunds.getRefundDate().plusDays(2).toString())
        .param("partnerId", Integer.toString(oneRefunds.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneRefunds.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneRefunds.getPartnerId()+1))
        .param("status", Integer.toString(oneRefunds.getStatus()))
        .param("status_array",Integer.toString(oneRefunds.getStatus()-1))
        .param("status_array",Integer.toString(oneRefunds.getStatus()+1))
        .param("customerId", Integer.toString(oneRefunds.getCustomerId()))
        .param("customerId_array",Integer.toString(oneRefunds.getCustomerId()-1))
        .param("customerId_array",Integer.toString(oneRefunds.getCustomerId()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneRefunds.getTransactionID()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateRefundsByID() throws Exception {
        if(oneRefunds.getTransactionID()==null){
            GetOneRefunds();
        }
        Refunds updateRefunds = new Refunds();
            updateRefunds.setTransactionID("Lorem");
    updateRefunds.setRefundAmount(1.35);
    updateRefunds.setReason("Est officia anim exercitation excepteur ex minim minim commodo mollit est id.");
    updateRefunds.setRefundDate(LocalDateTime.now().plusDays(1));
    updateRefunds.setPartnerId(1);
    updateRefunds.setStatus(1);
    updateRefunds.setCustomerId(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", updateRefunds.getTransactionID()!=null?updateRefunds.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "refundAmount", updateRefunds.getRefundAmount()>0?updateRefunds.getRefundAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "reason", updateRefunds.getReason()!=null?updateRefunds.getReason():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "refundDate", updateRefunds.getRefundDate()!=null?updateRefunds.getRefundDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateRefunds.getPartnerId()>0?updateRefunds.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateRefunds.getStatus()>0?updateRefunds.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "customerId", updateRefunds.getCustomerId()>0?updateRefunds.getCustomerId():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/refunds/"+oneRefunds.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionID").value(Matchers.containsString(updateRefunds.getTransactionID())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetRefundsByID() throws Exception {
        if(oneRefunds.getTransactionID()==null){
        GetOneRefunds();
        }
       
        String path = "/refunds/"+oneRefunds.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionID").value(Matchers.containsString(oneRefunds.getTransactionID())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteRefundsByID() throws Exception {
        if(oneRefunds.getTransactionID()==null){
            GetOneRefunds();
        }
        String path = "/refunds/"+oneRefunds.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneRefunds() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/refunds")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneRefunds_ = objectMapper.readValue(response,PagingData.class);
        if(oneRefunds_.getDocs().size()>0){
        int i =oneRefunds_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneRefunds_.getDocs();
        Refunds obj =convertToRefunds( one.get(i-1)); // last one
        this.oneRefunds = obj;
        }
     }
    private static Refunds convertToRefunds(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Refunds object
        
        // Retrieve other properties as needed
        Refunds objCls = new Refunds();
        
          String transactionID = (String) entry.get("transactionID");
          objCls.setTransactionID(transactionID);
              

          String refundAmount = (String) entry.get("refundAmount");
          double refundAmount_ = Double.parseDouble(refundAmount);
          objCls.setRefundAmount(refundAmount_);
                  

          String reason = (String) entry.get("reason");
          objCls.setReason(reason);
              

          String refundDate = (String) entry.get("refundDate");
          LocalDateTime refundDate_ = LocalDateTime.parse(refundDate);
          objCls.setRefundDate(refundDate_);
                      

          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      

          String status = (String) entry.get("status");
          Integer status_ = Integer.parseInt(status);
          objCls.setStatus(status_);
                      

          String customerId = (String) entry.get("customerId");
          Integer customerId_ = Integer.parseInt(customerId);
          objCls.setCustomerId(customerId_);
                      
        
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
    