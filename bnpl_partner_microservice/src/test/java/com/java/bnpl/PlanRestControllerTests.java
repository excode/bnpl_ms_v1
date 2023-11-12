
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
import com.java.bnpl.plan.Plan;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlanRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Plan onePlan= new Plan();   
    
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
    public void testAddPlan() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            onePlan.setPlanName("velit");
    onePlan.setStatus(1);
    onePlan.setNoOfInstallment(1);
    onePlan.setCommision(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "planName", onePlan.getPlanName()!=null?onePlan.getPlanName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", onePlan.getStatus()>0?onePlan.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "noOfInstallment", onePlan.getNoOfInstallment()>0?onePlan.getNoOfInstallment():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", onePlan.getCommision()>0?onePlan.getCommision():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/plan")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.planName").value(Matchers.containsString(onePlan.getPlanName())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetPlans() throws Exception {

        if(onePlan.getPlanName()==null){
            GetOnePlan();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/plan")
                    .param("status", Integer.toString(onePlan.getStatus()))
        .param("status_array",Integer.toString(onePlan.getStatus()-1))
        .param("status_array",Integer.toString(onePlan.getStatus()+1))
        .param("noOfInstallment", Integer.toString(onePlan.getNoOfInstallment()))
        .param("noOfInstallment_array",Integer.toString(onePlan.getNoOfInstallment()-1))
        .param("noOfInstallment_array",Integer.toString(onePlan.getNoOfInstallment()+1))
        .param("commision", Double.toString(onePlan.getCommision()))
        .param("commision_array",Double.toString(onePlan.getCommision()-1))
        .param("commision_array",Double.toString(onePlan.getCommision()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(onePlan.getPlanName()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdatePlanByID() throws Exception {
        if(onePlan.getPlanName()==null){
            GetOnePlan();
        }
        Plan updatePlan = new Plan();
            updatePlan.setPlanName("doclf");
    updatePlan.setStatus(1);
    updatePlan.setNoOfInstallment(1);
    updatePlan.setCommision(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "planName", updatePlan.getPlanName()!=null?updatePlan.getPlanName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updatePlan.getStatus()>0?updatePlan.getStatus():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "noOfInstallment", updatePlan.getNoOfInstallment()>0?updatePlan.getNoOfInstallment():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", updatePlan.getCommision()>0?updatePlan.getCommision():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/plan/"+onePlan.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planName").value(Matchers.containsString(updatePlan.getPlanName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetPlanByID() throws Exception {
        if(onePlan.getPlanName()==null){
        GetOnePlan();
        }
       
        String path = "/plan/"+onePlan.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.planName").value(Matchers.containsString(onePlan.getPlanName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeletePlanByID() throws Exception {
        if(onePlan.getPlanName()==null){
            GetOnePlan();
        }
        String path = "/plan/"+onePlan.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOnePlan() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/plan")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> onePlan_ = objectMapper.readValue(response,PagingData.class);
        if(onePlan_.getDocs().size()>0){
        int i =onePlan_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= onePlan_.getDocs();
        Plan obj =convertToPlan( one.get(i-1)); // last one
        this.onePlan = obj;
        }
     }
    private static Plan convertToPlan(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Plan object
        
        // Retrieve other properties as needed
        Plan objCls = new Plan();
        
          String planName = (String) entry.get("planName");
          objCls.setPlanName(planName);
              

          String status = (String) entry.get("status");
          Integer status_ = Integer.parseInt(status);
          objCls.setStatus(status_);
                      

          String noOfInstallment = (String) entry.get("noOfInstallment");
          Integer noOfInstallment_ = Integer.parseInt(noOfInstallment);
          objCls.setNoOfInstallment(noOfInstallment_);
                      

          String commision = (String) entry.get("commision");
          double commision_ = Double.parseDouble(commision);
          objCls.setCommision(commision_);
                  
        
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
    