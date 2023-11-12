
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
import com.java.bnpl.accesspermission.AccessPermission;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.java.bnpl.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccessPermissionRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private AccessPermission oneAccessPermission= new AccessPermission();   
    
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
    public void testAddAccessPermission() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneAccessPermission.setEndPointname("dolore");
    oneAccessPermission.setAdd(false);
    oneAccessPermission.setEdit(false);
    oneAccessPermission.setRead(false);
    oneAccessPermission.setDelete(false);
    oneAccessPermission.setUsername("fugiat");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "endPointname", oneAccessPermission.getEndPointname()!=null?oneAccessPermission.getEndPointname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "add", oneAccessPermission.getAdd()));
      jsonData.add(String.format("\"%s\":\"%s\"", "edit", oneAccessPermission.getEdit()));
      jsonData.add(String.format("\"%s\":\"%s\"", "read", oneAccessPermission.getRead()));
      jsonData.add(String.format("\"%s\":\"%s\"", "delete", oneAccessPermission.getDelete()));
      jsonData.add(String.format("\"%s\":\"%s\"", "username", oneAccessPermission.getUsername()!=null?oneAccessPermission.getUsername():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/accesspermission")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.endPointname").value(Matchers.containsString(oneAccessPermission.getEndPointname())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetAccessPermissions() throws Exception {

        if(oneAccessPermission.getEndPointname()==null){
            GetOneAccessPermission();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/accesspermission")
                .param("add",Boolean.toString( oneAccessPermission.getAdd()))
    .param("edit",Boolean.toString( oneAccessPermission.getEdit()))
    .param("read",Boolean.toString( oneAccessPermission.getRead()))
    .param("delete",Boolean.toString( oneAccessPermission.getDelete()))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneAccessPermission.getEndPointname()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    
    
    
    @Test  // 
    public void testUpdateAccessPermissionByID() throws Exception {
        if(oneAccessPermission.getEndPointname()==null){
            GetOneAccessPermission();
        }
        AccessPermission updateAccessPermission = new AccessPermission();
            updateAccessPermission.setEndPointname("inclf");
    updateAccessPermission.setAdd(false);
    updateAccessPermission.setEdit(false);
    updateAccessPermission.setRead(false);
    updateAccessPermission.setDelete(false);
    updateAccessPermission.setUsername("enim");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "endPointname", updateAccessPermission.getEndPointname()!=null?updateAccessPermission.getEndPointname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "add", updateAccessPermission.getAdd()));
      jsonData.add(String.format("\"%s\":\"%s\"", "edit", updateAccessPermission.getEdit()));
      jsonData.add(String.format("\"%s\":\"%s\"", "read", updateAccessPermission.getRead()));
      jsonData.add(String.format("\"%s\":\"%s\"", "delete", updateAccessPermission.getDelete()));
      jsonData.add(String.format("\"%s\":\"%s\"", "username", updateAccessPermission.getUsername()!=null?updateAccessPermission.getUsername():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/accesspermission/"+oneAccessPermission.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.endPointname").value(Matchers.containsString(updateAccessPermission.getEndPointname())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetAccessPermissionByID() throws Exception {
        if(oneAccessPermission.getEndPointname()==null){
        GetOneAccessPermission();
        }
       
        String path = "/accesspermission/"+oneAccessPermission.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.endPointname").value(Matchers.containsString(oneAccessPermission.getEndPointname())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteAccessPermissionByID() throws Exception {
        if(oneAccessPermission.getEndPointname()==null){
            GetOneAccessPermission();
        }
        String path = "/accesspermission/"+oneAccessPermission.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneAccessPermission() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/accesspermission")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneAccessPermission_ = objectMapper.readValue(response,PagingData.class);
        if(oneAccessPermission_.getDocs().size()>0){
        int i =oneAccessPermission_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneAccessPermission_.getDocs();
        AccessPermission obj =convertToAccessPermission( one.get(i-1)); // last one
        this.oneAccessPermission = obj;
        }
     }
    private static AccessPermission convertToAccessPermission(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a AccessPermission object
        
        // Retrieve other properties as needed
        AccessPermission objCls = new AccessPermission();
        
          String endPointname = (String) entry.get("endPointname");
          objCls.setEndPointname(endPointname);
              

          String add = (String) entry.get("add");
          boolean add_ = Boolean.parseBoolean(add);
          objCls.setAdd(add_);
                      

          String edit = (String) entry.get("edit");
          boolean edit_ = Boolean.parseBoolean(edit);
          objCls.setEdit(edit_);
                      

          String read = (String) entry.get("read");
          boolean read_ = Boolean.parseBoolean(read);
          objCls.setRead(read_);
                      

          String delete = (String) entry.get("delete");
          boolean delete_ = Boolean.parseBoolean(delete);
          objCls.setDelete(delete_);
                      

          String username = (String) entry.get("username");
          objCls.setUsername(username);
              
        
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
    