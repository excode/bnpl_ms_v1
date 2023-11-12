
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
import com.java.bnpl.apiactivity.APIActivity;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIActivityRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private APIActivity oneAPIActivity= new APIActivity();   
    
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
    public void testAddAPIActivity() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneAPIActivity.setPartnerId(1);
    oneAPIActivity.setAPIKeyID("sint");
    oneAPIActivity.setAPICallName("laborum");
    oneAPIActivity.setAPICallResult("inclf");
    oneAPIActivity.setErrorDetails("ipsum");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneAPIActivity.getPartnerId()>0?oneAPIActivity.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "aPIKeyID", oneAPIActivity.getAPIKeyID()!=null?oneAPIActivity.getAPIKeyID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "aPICallName", oneAPIActivity.getAPICallName()!=null?oneAPIActivity.getAPICallName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "aPICallResult", oneAPIActivity.getAPICallResult()!=null?oneAPIActivity.getAPICallResult():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "errorDetails", oneAPIActivity.getErrorDetails()!=null?oneAPIActivity.getErrorDetails():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/apiactivity")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.aPIKeyID").value(Matchers.containsString(oneAPIActivity.getAPIKeyID())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetAPIActivitys() throws Exception {

        if(oneAPIActivity.getAPIKeyID()==null){
            GetOneAPIActivity();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/apiactivity")
                    .param("partnerId", Integer.toString(oneAPIActivity.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneAPIActivity.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneAPIActivity.getPartnerId()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneAPIActivity.getAPIKeyID()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateAPIActivityByID() throws Exception {
        if(oneAPIActivity.getAPIKeyID()==null){
            GetOneAPIActivity();
        }
        APIActivity updateAPIActivity = new APIActivity();
            updateAPIActivity.setPartnerId(1);
    updateAPIActivity.setAPIKeyID("quiclf");
    updateAPIActivity.setAPICallName("culpa");
    updateAPIActivity.setAPICallResult("aute");
    updateAPIActivity.setErrorDetails("sitclf");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateAPIActivity.getPartnerId()>0?updateAPIActivity.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "aPIKeyID", updateAPIActivity.getAPIKeyID()!=null?updateAPIActivity.getAPIKeyID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "aPICallName", updateAPIActivity.getAPICallName()!=null?updateAPIActivity.getAPICallName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "aPICallResult", updateAPIActivity.getAPICallResult()!=null?updateAPIActivity.getAPICallResult():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "errorDetails", updateAPIActivity.getErrorDetails()!=null?updateAPIActivity.getErrorDetails():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/apiactivity/"+oneAPIActivity.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aPIKeyID").value(Matchers.containsString(updateAPIActivity.getAPIKeyID())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetAPIActivityByID() throws Exception {
        if(oneAPIActivity.getAPIKeyID()==null){
        GetOneAPIActivity();
        }
       
        String path = "/apiactivity/"+oneAPIActivity.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aPIKeyID").value(Matchers.containsString(oneAPIActivity.getAPIKeyID())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteAPIActivityByID() throws Exception {
        if(oneAPIActivity.getAPIKeyID()==null){
            GetOneAPIActivity();
        }
        String path = "/apiactivity/"+oneAPIActivity.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneAPIActivity() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/apiactivity")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneAPIActivity_ = objectMapper.readValue(response,PagingData.class);
        if(oneAPIActivity_.getDocs().size()>0){
        int i =oneAPIActivity_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneAPIActivity_.getDocs();
        APIActivity obj =convertToAPIActivity( one.get(i-1)); // last one
        this.oneAPIActivity = obj;
        }
     }
    private static APIActivity convertToAPIActivity(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a APIActivity object
        
        // Retrieve other properties as needed
        APIActivity objCls = new APIActivity();
        
          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      

          String aPIKeyID = (String) entry.get("aPIKeyID");
          objCls.setAPIKeyID(aPIKeyID);
              

          String aPICallName = (String) entry.get("aPICallName");
          objCls.setAPICallName(aPICallName);
              

          String aPICallResult = (String) entry.get("aPICallResult");
          objCls.setAPICallResult(aPICallResult);
              

          String errorDetails = (String) entry.get("errorDetails");
          objCls.setErrorDetails(errorDetails);
              
        
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
    