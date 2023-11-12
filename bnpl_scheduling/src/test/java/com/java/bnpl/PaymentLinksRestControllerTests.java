
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
import com.java.bnpl.paymentlinks.PaymentLinks;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentLinksRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private PaymentLinks onePaymentLinks= new PaymentLinks();   
    
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
    public void testAddPaymentLinks() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            onePaymentLinks.setTransactionID(1);
    onePaymentLinks.setCustomerID(1);
    onePaymentLinks.setPaymentLinkURL("nisi");
    onePaymentLinks.setPartnerId(1.35);
    onePaymentLinks.setPaymentAmount(1.35);
    onePaymentLinks.setStatus(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", onePaymentLinks.getTransactionID()>0?onePaymentLinks.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "customerID", onePaymentLinks.getCustomerID()>0?onePaymentLinks.getCustomerID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "paymentLinkURL", onePaymentLinks.getPaymentLinkURL()!=null?onePaymentLinks.getPaymentLinkURL():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", onePaymentLinks.getPartnerId()>0?onePaymentLinks.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "paymentAmount", onePaymentLinks.getPaymentAmount()>0?onePaymentLinks.getPaymentAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", onePaymentLinks.getStatus()>0?onePaymentLinks.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/paymentlinks")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.paymentLinkURL").value(Matchers.containsString(onePaymentLinks.getPaymentLinkURL())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetPaymentLinkss() throws Exception {

        if(onePaymentLinks.getPaymentLinkURL()==null){
            GetOnePaymentLinks();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/paymentlinks")
                    .param("transactionID", Integer.toString(onePaymentLinks.getTransactionID()))
        .param("transactionID_array",Integer.toString(onePaymentLinks.getTransactionID()-1))
        .param("transactionID_array",Integer.toString(onePaymentLinks.getTransactionID()+1))
        .param("customerID", Integer.toString(onePaymentLinks.getCustomerID()))
        .param("customerID_array",Integer.toString(onePaymentLinks.getCustomerID()-1))
        .param("customerID_array",Integer.toString(onePaymentLinks.getCustomerID()+1))
        .param("partnerId", Double.toString(onePaymentLinks.getPartnerId()))
        .param("partnerId_array",Double.toString(onePaymentLinks.getPartnerId()-1))
        .param("partnerId_array",Double.toString(onePaymentLinks.getPartnerId()+1))
        .param("paymentAmount", Double.toString(onePaymentLinks.getPaymentAmount()))
        .param("paymentAmount_array",Double.toString(onePaymentLinks.getPaymentAmount()-1))
        .param("paymentAmount_array",Double.toString(onePaymentLinks.getPaymentAmount()+1))
        .param("status", Integer.toString(onePaymentLinks.getStatus()))
        .param("status_array",Integer.toString(onePaymentLinks.getStatus()-1))
        .param("status_array",Integer.toString(onePaymentLinks.getStatus()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(onePaymentLinks.getPaymentLinkURL()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdatePaymentLinksByID() throws Exception {
        if(onePaymentLinks.getPaymentLinkURL()==null){
            GetOnePaymentLinks();
        }
        PaymentLinks updatePaymentLinks = new PaymentLinks();
            updatePaymentLinks.setTransactionID(1);
    updatePaymentLinks.setCustomerID(1);
    updatePaymentLinks.setPaymentLinkURL("adipisicing");
    updatePaymentLinks.setPartnerId(1.35);
    updatePaymentLinks.setPaymentAmount(1.35);
    updatePaymentLinks.setStatus(1);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "transactionID", updatePaymentLinks.getTransactionID()>0?updatePaymentLinks.getTransactionID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "customerID", updatePaymentLinks.getCustomerID()>0?updatePaymentLinks.getCustomerID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "paymentLinkURL", updatePaymentLinks.getPaymentLinkURL()!=null?updatePaymentLinks.getPaymentLinkURL():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updatePaymentLinks.getPartnerId()>0?updatePaymentLinks.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "paymentAmount", updatePaymentLinks.getPaymentAmount()>0?updatePaymentLinks.getPaymentAmount():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updatePaymentLinks.getStatus()>0?updatePaymentLinks.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/paymentlinks/"+onePaymentLinks.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentLinkURL").value(Matchers.containsString(updatePaymentLinks.getPaymentLinkURL())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetPaymentLinksByID() throws Exception {
        if(onePaymentLinks.getPaymentLinkURL()==null){
        GetOnePaymentLinks();
        }
       
        String path = "/paymentlinks/"+onePaymentLinks.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentLinkURL").value(Matchers.containsString(onePaymentLinks.getPaymentLinkURL())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeletePaymentLinksByID() throws Exception {
        if(onePaymentLinks.getPaymentLinkURL()==null){
            GetOnePaymentLinks();
        }
        String path = "/paymentlinks/"+onePaymentLinks.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOnePaymentLinks() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/paymentlinks")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> onePaymentLinks_ = objectMapper.readValue(response,PagingData.class);
        if(onePaymentLinks_.getDocs().size()>0){
        int i =onePaymentLinks_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= onePaymentLinks_.getDocs();
        PaymentLinks obj =convertToPaymentLinks( one.get(i-1)); // last one
        this.onePaymentLinks = obj;
        }
     }
    private static PaymentLinks convertToPaymentLinks(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a PaymentLinks object
        
        // Retrieve other properties as needed
        PaymentLinks objCls = new PaymentLinks();
        
          String transactionID = (String) entry.get("transactionID");
          Integer transactionID_ = Integer.parseInt(transactionID);
          objCls.setTransactionID(transactionID_);
                      

          String customerID = (String) entry.get("customerID");
          Integer customerID_ = Integer.parseInt(customerID);
          objCls.setCustomerID(customerID_);
                      

          String paymentLinkURL = (String) entry.get("paymentLinkURL");
          objCls.setPaymentLinkURL(paymentLinkURL);
              

          String partnerId = (String) entry.get("partnerId");
          double partnerId_ = Double.parseDouble(partnerId);
          objCls.setPartnerId(partnerId_);
                  

          String paymentAmount = (String) entry.get("paymentAmount");
          double paymentAmount_ = Double.parseDouble(paymentAmount);
          objCls.setPaymentAmount(paymentAmount_);
                  

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
    