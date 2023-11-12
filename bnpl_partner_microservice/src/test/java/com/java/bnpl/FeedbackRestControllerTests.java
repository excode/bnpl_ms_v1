
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
import com.java.bnpl.feedback.Feedback;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Feedback oneFeedback= new Feedback();   
    
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
    public void testAddFeedback() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneFeedback.setPartnerId(1);
    oneFeedback.setRating(7.119999999999999);
    oneFeedback.setFeedbackText("Voluptate veniam cillum aliquip elit et.");
    oneFeedback.setSubmissionDate(LocalDateTime.now().plusDays(1));
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", oneFeedback.getPartnerId()>0?oneFeedback.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "rating", oneFeedback.getRating()>0?oneFeedback.getRating():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "feedbackText", oneFeedback.getFeedbackText()!=null?oneFeedback.getFeedbackText():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "submissionDate", oneFeedback.getSubmissionDate()!=null?oneFeedback.getSubmissionDate():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/feedback")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackText").value(Matchers.containsString(oneFeedback.getFeedbackText())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetFeedbacks() throws Exception {

        if(oneFeedback.getFeedbackText()==null){
            GetOneFeedback();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/feedback")
                    .param("partnerId", Integer.toString(oneFeedback.getPartnerId()))
        .param("partnerId_array",Integer.toString(oneFeedback.getPartnerId()-1))
        .param("partnerId_array",Integer.toString(oneFeedback.getPartnerId()+1))
        .param("rating", Double.toString(oneFeedback.getRating()))
        .param("rating_array",Double.toString(oneFeedback.getRating()-1))
        .param("rating_array",Double.toString(oneFeedback.getRating()+1))
        .param("feedbackText", sortenString(oneFeedback.getFeedbackText()))
        .param("feedbackText_array","dummy1")
        .param("feedbackText_array",oneFeedback.getFeedbackText())
        .param("feedbackText_array","dummy2")
        .param("submissionDateStr", oneFeedback.getSubmissionDate().toString())
        .param("submissionDate_array",oneFeedback.getSubmissionDate().minusDays(2).toString())
        .param("submissionDate_array",oneFeedback.getSubmissionDate().plusDays(2).toString())
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneFeedback.getFeedbackText()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    @Test
    public void testAddFeedbackByMaxRating_5() throws Exception {
        if(this.oneFeedback.getFeedbackText()==null){
            GetOneFeedback();
        }
        Feedback reqFeedback = new Feedback();
            reqFeedback.setPartnerId(1);
    reqFeedback.setRating(2.36);
    reqFeedback.setFeedbackText("Magna id in velit magna cillum sint fugiat dolore irure.");
    reqFeedback.setSubmissionDate(LocalDateTime.now().plusDays(1));
        reqFeedback.setRating(6.50); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", reqFeedback.getPartnerId()>0?reqFeedback.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "rating", reqFeedback.getRating()>0?reqFeedback.getRating():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "feedbackText", reqFeedback.getFeedbackText()!=null?reqFeedback.getFeedbackText():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "submissionDate", reqFeedback.getSubmissionDate()!=null?reqFeedback.getSubmissionDate():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/feedback")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("rating range error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    
    
    @Test  // 
    public void testUpdateFeedbackByID() throws Exception {
        if(oneFeedback.getFeedbackText()==null){
            GetOneFeedback();
        }
        Feedback updateFeedback = new Feedback();
            updateFeedback.setPartnerId(1);
    updateFeedback.setRating(4.05);
    updateFeedback.setFeedbackText("Aliquip est dolore ad non reprehenderit aliqua sunt ullamco nostrud velit aute voluptate incididunt.");
    updateFeedback.setSubmissionDate(LocalDateTime.now().plusDays(1));
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "partnerId", updateFeedback.getPartnerId()>0?updateFeedback.getPartnerId():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "rating", updateFeedback.getRating()>0?updateFeedback.getRating():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "feedbackText", updateFeedback.getFeedbackText()!=null?updateFeedback.getFeedbackText():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "submissionDate", updateFeedback.getSubmissionDate()!=null?updateFeedback.getSubmissionDate():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/feedback/"+oneFeedback.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackText").value(Matchers.containsString(updateFeedback.getFeedbackText())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetFeedbackByID() throws Exception {
        if(oneFeedback.getFeedbackText()==null){
        GetOneFeedback();
        }
       
        String path = "/feedback/"+oneFeedback.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackText").value(Matchers.containsString(oneFeedback.getFeedbackText())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteFeedbackByID() throws Exception {
        if(oneFeedback.getFeedbackText()==null){
            GetOneFeedback();
        }
        String path = "/feedback/"+oneFeedback.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneFeedback() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/feedback")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneFeedback_ = objectMapper.readValue(response,PagingData.class);
        if(oneFeedback_.getDocs().size()>0){
        int i =oneFeedback_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneFeedback_.getDocs();
        Feedback obj =convertToFeedback( one.get(i-1)); // last one
        this.oneFeedback = obj;
        }
     }
    private static Feedback convertToFeedback(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Feedback object
        
        // Retrieve other properties as needed
        Feedback objCls = new Feedback();
        
          String partnerId = (String) entry.get("partnerId");
          Integer partnerId_ = Integer.parseInt(partnerId);
          objCls.setPartnerId(partnerId_);
                      

          String rating = (String) entry.get("rating");
          double rating_ = Double.parseDouble(rating);
          objCls.setRating(rating_);
                  

          String feedbackText = (String) entry.get("feedbackText");
          objCls.setFeedbackText(feedbackText);
              

          String submissionDate = (String) entry.get("submissionDate");
          LocalDateTime submissionDate_ = LocalDateTime.parse(submissionDate);
          objCls.setSubmissionDate(submissionDate_);
                      
        
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
    