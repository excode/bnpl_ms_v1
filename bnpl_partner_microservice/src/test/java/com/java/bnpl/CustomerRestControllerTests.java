
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
import com.java.bnpl.customer.Customer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Customer oneCustomer= new Customer();   
    
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
    public void testAddCustomer() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneCustomer.setName("etclf");
    oneCustomer.setAddress("Elit dolor labore ut.");
    oneCustomer.setEmail("proident@ucode.ai");
    oneCustomer.setPhone("601776437685");
    oneCustomer.setCity("adipisicing");
    oneCustomer.setState("officia");
    oneCustomer.setPostcode("excepteur");
    oneCustomer.setStatus(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "name", oneCustomer.getName()!=null?oneCustomer.getName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", oneCustomer.getAddress()!=null?oneCustomer.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", oneCustomer.getEmail()!=null?oneCustomer.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", oneCustomer.getPhone()!=null?oneCustomer.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", oneCustomer.getCity()!=null?oneCustomer.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", oneCustomer.getState()!=null?oneCustomer.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "postcode", oneCustomer.getPostcode()!=null?oneCustomer.getPostcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneCustomer.getStatus()>0?oneCustomer.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(oneCustomer.getName())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetCustomers() throws Exception {

        if(oneCustomer.getName()==null){
            GetOneCustomer();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/customer")
                    .param("address", sortenString(oneCustomer.getAddress()))
        .param("address_array","dummy1")
        .param("address_array",oneCustomer.getAddress())
        .param("address_array","dummy2")
        .param("email", sortenString(oneCustomer.getEmail()))
        .param("email_array","dummy1")
        .param("email_array",oneCustomer.getEmail())
        .param("email_array","dummy2")
        .param("phone", sortenString(oneCustomer.getPhone()))
        .param("phone_array","dummy1")
        .param("phone_array",oneCustomer.getPhone())
        .param("phone_array","dummy2")
        .param("city", sortenString(oneCustomer.getCity()))
        .param("city_array","dummy1")
        .param("city_array",oneCustomer.getCity())
        .param("city_array","dummy2")
        .param("status", Double.toString(oneCustomer.getStatus()))
        .param("status_array",Double.toString(oneCustomer.getStatus()-1))
        .param("status_array",Double.toString(oneCustomer.getStatus()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneCustomer.getName()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    @Test
    public void testAddCustomerByInvalidEmail() throws Exception {
        if(this.oneCustomer.getName()==null){
            GetOneCustomer();
        }
        Customer reqCustomer = new Customer();
            reqCustomer.setName("aliqua");
    reqCustomer.setAddress("Ullamco proident enim quis officia quis quis in est aliqua anim veniam et nisi aliquip.");
    reqCustomer.setEmail("ad@ucode.ai");
    reqCustomer.setPhone("601751463009");
    reqCustomer.setCity("veniam");
    reqCustomer.setState("incididunt");
    reqCustomer.setPostcode("eiusmod");
    reqCustomer.setStatus(1.35);
        reqCustomer.setEmail(reqCustomer.getEmail().replace("@","")); // set  invalid email by replacing @ 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "name", reqCustomer.getName()!=null?reqCustomer.getName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", reqCustomer.getAddress()!=null?reqCustomer.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqCustomer.getEmail()!=null?reqCustomer.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", reqCustomer.getPhone()!=null?reqCustomer.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", reqCustomer.getCity()!=null?reqCustomer.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", reqCustomer.getState()!=null?reqCustomer.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "postcode", reqCustomer.getPostcode()!=null?reqCustomer.getPostcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", reqCustomer.getStatus()>0?reqCustomer.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("email should be valid email address")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    @Test
    public void testAddCustomerByInvalidPhone() throws Exception {
        if(this.oneCustomer.getName()==null){
            GetOneCustomer();
        }
        Customer reqCustomer = new Customer();
            reqCustomer.setName("aliqua");
    reqCustomer.setAddress("Ullamco proident enim quis officia quis quis in est aliqua anim veniam et nisi aliquip.");
    reqCustomer.setEmail("ad@ucode.ai");
    reqCustomer.setPhone("601751463009");
    reqCustomer.setCity("veniam");
    reqCustomer.setState("incididunt");
    reqCustomer.setPostcode("eiusmod");
    reqCustomer.setStatus(1.35);
        reqCustomer.setPhone(reqCustomer.getPhone()+"a"); // set  invalid phone/mobile  value by adding a char
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "name", reqCustomer.getName()!=null?reqCustomer.getName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", reqCustomer.getAddress()!=null?reqCustomer.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqCustomer.getEmail()!=null?reqCustomer.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", reqCustomer.getPhone()!=null?reqCustomer.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", reqCustomer.getCity()!=null?reqCustomer.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", reqCustomer.getState()!=null?reqCustomer.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "postcode", reqCustomer.getPostcode()!=null?reqCustomer.getPostcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", reqCustomer.getStatus()>0?reqCustomer.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("phone should be valid")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    @Test  // 
    public void testUpdateCustomerByID() throws Exception {
        if(oneCustomer.getName()==null){
            GetOneCustomer();
        }
        Customer updateCustomer = new Customer();
            updateCustomer.setName("velit");
    updateCustomer.setAddress("Culpa est pariatur enim pariatur et occaecat amet pariatur nisi.");
    updateCustomer.setEmail("irure@ucode.ai");
    updateCustomer.setPhone("601744933774");
    updateCustomer.setCity("labore");
    updateCustomer.setState("quiclf");
    updateCustomer.setPostcode("laborum");
    updateCustomer.setStatus(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "name", updateCustomer.getName()!=null?updateCustomer.getName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", updateCustomer.getAddress()!=null?updateCustomer.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", updateCustomer.getEmail()!=null?updateCustomer.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", updateCustomer.getPhone()!=null?updateCustomer.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", updateCustomer.getCity()!=null?updateCustomer.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", updateCustomer.getState()!=null?updateCustomer.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "postcode", updateCustomer.getPostcode()!=null?updateCustomer.getPostcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateCustomer.getStatus()>0?updateCustomer.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/customer/"+oneCustomer.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(updateCustomer.getName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetCustomerByID() throws Exception {
        if(oneCustomer.getName()==null){
        GetOneCustomer();
        }
       
        String path = "/customer/"+oneCustomer.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.containsString(oneCustomer.getName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteCustomerByID() throws Exception {
        if(oneCustomer.getName()==null){
            GetOneCustomer();
        }
        String path = "/customer/"+oneCustomer.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneCustomer() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/customer")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneCustomer_ = objectMapper.readValue(response,PagingData.class);
        if(oneCustomer_.getDocs().size()>0){
        int i =oneCustomer_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneCustomer_.getDocs();
        Customer obj =convertToCustomer( one.get(i-1)); // last one
        this.oneCustomer = obj;
        }
     }
    private static Customer convertToCustomer(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Customer object
        
        // Retrieve other properties as needed
        Customer objCls = new Customer();
        
          String name = (String) entry.get("name");
          objCls.setName(name);
              

          String address = (String) entry.get("address");
          objCls.setAddress(address);
              

          String email = (String) entry.get("email");
          objCls.setEmail(email);
              

          String phone = (String) entry.get("phone");
          objCls.setPhone(phone);
              

          String city = (String) entry.get("city");
          objCls.setCity(city);
              

          String state = (String) entry.get("state");
          objCls.setState(state);
              

          String postcode = (String) entry.get("postcode");
          objCls.setPostcode(postcode);
              

          String status = (String) entry.get("status");
          double status_ = Double.parseDouble(status);
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
    