
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
import com.java.bnpl.bank.Bank;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BankRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Bank oneBank= new Bank();   
    
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
    public void testAddBank() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneBank.setName("laborum");
    oneBank.setAccountName("irure");
    oneBank.setSwift("doclf");
    oneBank.setAccountNumber(1.35);
    oneBank.setCity("fugiat");
    oneBank.setCountry("utclf");
    oneBank.setAddress("In non fugiat aliqua eiusmod irure velit.");
    oneBank.setPostcode(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "name", oneBank.getName()!=null?oneBank.getName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "accountName", oneBank.getAccountName()!=null?oneBank.getAccountName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "swift", oneBank.getSwift()!=null?oneBank.getSwift():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "accountNumber", oneBank.getAccountNumber()>0?oneBank.getAccountNumber():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", oneBank.getCity()!=null?oneBank.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "country", oneBank.getCountry()!=null?oneBank.getCountry():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", oneBank.getAddress()!=null?oneBank.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "postcode", oneBank.getPostcode()>0?oneBank.getPostcode():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/bank")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(oneBank.getName())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetBanks() throws Exception {

        if(oneBank.getName()==null){
            GetOneBank();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/bank")
                    .param("accountNumber", Double.toString(oneBank.getAccountNumber()))
        .param("accountNumber_array",Double.toString(oneBank.getAccountNumber()-1))
        .param("accountNumber_array",Double.toString(oneBank.getAccountNumber()+1))
        .param("address", sortenString(oneBank.getAddress()))
        .param("address_array","dummy1")
        .param("address_array",oneBank.getAddress())
        .param("address_array","dummy2")
        .param("postcode", Double.toString(oneBank.getPostcode()))
        .param("postcode_array",Double.toString(oneBank.getPostcode()-1))
        .param("postcode_array",Double.toString(oneBank.getPostcode()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneBank.getName()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateBankByID() throws Exception {
        if(oneBank.getName()==null){
            GetOneBank();
        }
        Bank updateBank = new Bank();
            updateBank.setName("labore");
    updateBank.setAccountName("sint");
    updateBank.setSwift("proident");
    updateBank.setAccountNumber(1.35);
    updateBank.setCity("exercitation");
    updateBank.setCountry("mollit");
    updateBank.setAddress("Nostrud tempor quis labore anim labore ut dolore pariatur aute in quis excepteur.");
    updateBank.setPostcode(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "name", updateBank.getName()!=null?updateBank.getName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "accountName", updateBank.getAccountName()!=null?updateBank.getAccountName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "swift", updateBank.getSwift()!=null?updateBank.getSwift():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "accountNumber", updateBank.getAccountNumber()>0?updateBank.getAccountNumber():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", updateBank.getCity()!=null?updateBank.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "country", updateBank.getCountry()!=null?updateBank.getCountry():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", updateBank.getAddress()!=null?updateBank.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "postcode", updateBank.getPostcode()>0?updateBank.getPostcode():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/bank/"+oneBank.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(updateBank.getName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetBankByID() throws Exception {
        if(oneBank.getName()==null){
        GetOneBank();
        }
       
        String path = "/bank/"+oneBank.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(oneBank.getName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteBankByID() throws Exception {
        if(oneBank.getName()==null){
            GetOneBank();
        }
        String path = "/bank/"+oneBank.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneBank() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bank")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneBank_ = objectMapper.readValue(response,PagingData.class);
        if(oneBank_.getDocs().size()>0){
        int i =oneBank_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneBank_.getDocs();
        Bank obj =convertToBank( one.get(i-1)); // last one
        this.oneBank = obj;
        }
     }
    private static Bank convertToBank(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Bank object
        
        // Retrieve other properties as needed
        Bank objCls = new Bank();
        
          String name = (String) entry.get("name");
          objCls.setName(name);
              

          String accountName = (String) entry.get("accountName");
          objCls.setAccountName(accountName);
              

          String swift = (String) entry.get("swift");
          objCls.setSwift(swift);
              

          String accountNumber = (String) entry.get("accountNumber");
          double accountNumber_ = Double.parseDouble(accountNumber);
          objCls.setAccountNumber(accountNumber_);
                  

          String city = (String) entry.get("city");
          objCls.setCity(city);
              

          String country = (String) entry.get("country");
          objCls.setCountry(country);
              

          String address = (String) entry.get("address");
          objCls.setAddress(address);
              

          String postcode = (String) entry.get("postcode");
          double postcode_ = Double.parseDouble(postcode);
          objCls.setPostcode(postcode_);
                  
        
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
    