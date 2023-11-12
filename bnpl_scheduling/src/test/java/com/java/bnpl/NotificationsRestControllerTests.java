
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
import com.java.bnpl.notifications.Notifications;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationsRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Notifications oneNotifications= new Notifications();   
    
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
    public void testAddNotifications() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneNotifications.setSenderID("culpa");
    oneNotifications.setRecipientID("sunt");
    oneNotifications.setNotificationType("minim");
    oneNotifications.setNotificationChannel("nisi");
    oneNotifications.setNotificationContent("laborum");
    oneNotifications.setReadDate(LocalDateTime.now().plusDays(1));
    oneNotifications.setStatus(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "senderID", oneNotifications.getSenderID()!=null?oneNotifications.getSenderID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "recipientID", oneNotifications.getRecipientID()!=null?oneNotifications.getRecipientID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "notificationType", oneNotifications.getNotificationType()!=null?oneNotifications.getNotificationType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "notificationChannel", oneNotifications.getNotificationChannel()!=null?oneNotifications.getNotificationChannel():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "notificationContent", oneNotifications.getNotificationContent()!=null?oneNotifications.getNotificationContent():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "readDate", oneNotifications.getReadDate()!=null?oneNotifications.getReadDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", oneNotifications.getStatus()>0?oneNotifications.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.senderID").value(Matchers.containsString(oneNotifications.getSenderID())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetNotificationss() throws Exception {

        if(oneNotifications.getSenderID()==null){
            GetOneNotifications();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications")
                    .param("readDateStr", oneNotifications.getReadDate().toString())
        .param("readDate_array",oneNotifications.getReadDate().minusDays(2).toString())
        .param("readDate_array",oneNotifications.getReadDate().plusDays(2).toString())
        .param("status", Double.toString(oneNotifications.getStatus()))
        .param("status_array",Double.toString(oneNotifications.getStatus()-1))
        .param("status_array",Double.toString(oneNotifications.getStatus()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneNotifications.getSenderID()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateNotificationsByID() throws Exception {
        if(oneNotifications.getSenderID()==null){
            GetOneNotifications();
        }
        Notifications updateNotifications = new Notifications();
            updateNotifications.setSenderID("culpa");
    updateNotifications.setRecipientID("voluptate");
    updateNotifications.setNotificationType("duis");
    updateNotifications.setNotificationChannel("aute");
    updateNotifications.setNotificationContent("esse");
    updateNotifications.setReadDate(LocalDateTime.now().plusDays(1));
    updateNotifications.setStatus(1.35);
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "senderID", updateNotifications.getSenderID()!=null?updateNotifications.getSenderID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "recipientID", updateNotifications.getRecipientID()!=null?updateNotifications.getRecipientID():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "notificationType", updateNotifications.getNotificationType()!=null?updateNotifications.getNotificationType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "notificationChannel", updateNotifications.getNotificationChannel()!=null?updateNotifications.getNotificationChannel():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "notificationContent", updateNotifications.getNotificationContent()!=null?updateNotifications.getNotificationContent():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "readDate", updateNotifications.getReadDate()!=null?updateNotifications.getReadDate():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "status", updateNotifications.getStatus()>0?updateNotifications.getStatus():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/notifications/"+oneNotifications.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.senderID").value(Matchers.containsString(updateNotifications.getSenderID())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetNotificationsByID() throws Exception {
        if(oneNotifications.getSenderID()==null){
        GetOneNotifications();
        }
       
        String path = "/notifications/"+oneNotifications.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.senderID").value(Matchers.containsString(oneNotifications.getSenderID())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteNotificationsByID() throws Exception {
        if(oneNotifications.getSenderID()==null){
            GetOneNotifications();
        }
        String path = "/notifications/"+oneNotifications.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneNotifications() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/notifications")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneNotifications_ = objectMapper.readValue(response,PagingData.class);
        if(oneNotifications_.getDocs().size()>0){
        int i =oneNotifications_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneNotifications_.getDocs();
        Notifications obj =convertToNotifications( one.get(i-1)); // last one
        this.oneNotifications = obj;
        }
     }
    private static Notifications convertToNotifications(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Notifications object
        
        // Retrieve other properties as needed
        Notifications objCls = new Notifications();
        
          String senderID = (String) entry.get("senderID");
          objCls.setSenderID(senderID);
              

          String recipientID = (String) entry.get("recipientID");
          objCls.setRecipientID(recipientID);
              

          String notificationType = (String) entry.get("notificationType");
          objCls.setNotificationType(notificationType);
              

          String notificationChannel = (String) entry.get("notificationChannel");
          objCls.setNotificationChannel(notificationChannel);
              

          String notificationContent = (String) entry.get("notificationContent");
          objCls.setNotificationContent(notificationContent);
              

          String readDate = (String) entry.get("readDate");
          LocalDateTime readDate_ = LocalDateTime.parse(readDate);
          objCls.setReadDate(readDate_);
                      

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
    