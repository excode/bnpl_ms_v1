
package com.java.bnpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.hamcrest.Matchers;
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
import com.java.bnpl.ucodeutility.PagingData;
import com.java.bnpl.users.Users;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Users oneUsers= new Users();   
    
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
    public void testAddUsers() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneUsers.setUserType(1);
    oneUsers.setLastname("ipsum");
    oneUsers.setFirstname("inclf");
    oneUsers.setPassword("ucode1234");
    oneUsers.setEmail("consequat@ucode.ai2");
    oneUsers.setMobile("Lorem6");
    oneUsers.setRole("mollit");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", oneUsers.getUserType()>0?oneUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", oneUsers.getLastname()!=null?oneUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", oneUsers.getFirstname()!=null?oneUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", oneUsers.getPassword()!=null?oneUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", oneUsers.getEmail()!=null?oneUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", oneUsers.getMobile()!=null?oneUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", oneUsers.getRole()!=null?oneUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(Matchers.containsString(oneUsers.getLastname())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetUserss() throws Exception {

        if(oneUsers.getLastname()==null){
            GetOneUsers();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                    .param("userType", Integer.toString(oneUsers.getUserType()))
        .param("userType_array",Integer.toString(oneUsers.getUserType()-1))
        .param("userType_array",Integer.toString(oneUsers.getUserType()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneUsers.getLastname()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void testAddUsersByDuplicateEmail() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users dupemailUsers = new Users();
            dupemailUsers.setUserType(1);
    dupemailUsers.setLastname("inclf");
    dupemailUsers.setFirstname("estclf");
    dupemailUsers.setPassword("ucode1234");
    dupemailUsers.setEmail("exercitation@ucode.ai8");
    dupemailUsers.setMobile("mollit3");
    dupemailUsers.setRole("velit");
        dupemailUsers.setEmail(oneUsers.getEmail()); // set a  duplicate value
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", dupemailUsers.getUserType()>0?dupemailUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", dupemailUsers.getLastname()!=null?dupemailUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", dupemailUsers.getFirstname()!=null?dupemailUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", dupemailUsers.getPassword()!=null?dupemailUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", dupemailUsers.getEmail()!=null?dupemailUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", dupemailUsers.getMobile()!=null?dupemailUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", dupemailUsers.getRole()!=null?dupemailUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("email_NO")))
        .andDo(MockMvcResultHandlers.print());
    }
        

    @Test
    public void testAddUsersByDuplicateMobile() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users dupmobileUsers = new Users();
            dupmobileUsers.setUserType(1);
    dupmobileUsers.setLastname("fugiat");
    dupmobileUsers.setFirstname("nostrud");
    dupmobileUsers.setPassword("ucode1234");
    dupmobileUsers.setEmail("nulla@ucode.ai0");
    dupmobileUsers.setMobile("minim8");
    dupmobileUsers.setRole("ipsum");
        dupmobileUsers.setMobile(oneUsers.getMobile()); // set a  duplicate value
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", dupmobileUsers.getUserType()>0?dupmobileUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", dupmobileUsers.getLastname()!=null?dupmobileUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", dupmobileUsers.getFirstname()!=null?dupmobileUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", dupmobileUsers.getPassword()!=null?dupmobileUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", dupmobileUsers.getEmail()!=null?dupmobileUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", dupmobileUsers.getMobile()!=null?dupmobileUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", dupmobileUsers.getRole()!=null?dupmobileUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("mobile_NO")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    
    @Test
    public void testAddUsersByMinLastname_1() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users reqUsers = new Users();
            reqUsers.setUserType(1);
    reqUsers.setLastname("aliquip");
    reqUsers.setFirstname("consequat");
    reqUsers.setPassword("ucode1234");
    reqUsers.setEmail("aliquip@ucode.ai9");
    reqUsers.setMobile("utclf7");
    reqUsers.setRole("sunt");
        reqUsers.setLastname(""); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", reqUsers.getUserType()>0?reqUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", reqUsers.getLastname()!=null?reqUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", reqUsers.getFirstname()!=null?reqUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqUsers.getPassword()!=null?reqUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqUsers.getEmail()!=null?reqUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", reqUsers.getMobile()!=null?reqUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", reqUsers.getRole()!=null?reqUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("lastname length error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        

    @Test
    public void testAddUsersByMaxLastname_50() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users reqUsers = new Users();
            reqUsers.setUserType(1);
    reqUsers.setLastname("aliquip");
    reqUsers.setFirstname("consequat");
    reqUsers.setPassword("ucode1234");
    reqUsers.setEmail("aliquip@ucode.ai9");
    reqUsers.setMobile("utclf7");
    reqUsers.setRole("sunt");
        reqUsers.setLastname("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", reqUsers.getUserType()>0?reqUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", reqUsers.getLastname()!=null?reqUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", reqUsers.getFirstname()!=null?reqUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqUsers.getPassword()!=null?reqUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqUsers.getEmail()!=null?reqUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", reqUsers.getMobile()!=null?reqUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", reqUsers.getRole()!=null?reqUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("lastname length error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        

    @Test
    public void testAddUsersByMinPassword_6() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users reqUsers = new Users();
            reqUsers.setUserType(1);
    reqUsers.setLastname("aliquip");
    reqUsers.setFirstname("consequat");
    reqUsers.setPassword("ucode1234");
    reqUsers.setEmail("aliquip@ucode.ai9");
    reqUsers.setMobile("utclf7");
    reqUsers.setRole("sunt");
        reqUsers.setPassword("aaaaa"); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", reqUsers.getUserType()>0?reqUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", reqUsers.getLastname()!=null?reqUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", reqUsers.getFirstname()!=null?reqUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqUsers.getPassword()!=null?reqUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqUsers.getEmail()!=null?reqUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", reqUsers.getMobile()!=null?reqUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", reqUsers.getRole()!=null?reqUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("password length error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        

    @Test
    public void testAddUsersByMaxPassword_20() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users reqUsers = new Users();
            reqUsers.setUserType(1);
    reqUsers.setLastname("aliquip");
    reqUsers.setFirstname("consequat");
    reqUsers.setPassword("ucode1234");
    reqUsers.setEmail("aliquip@ucode.ai9");
    reqUsers.setMobile("utclf7");
    reqUsers.setRole("sunt");
        reqUsers.setPassword("bbbbbbbbbbbbbbbbbbbbb"); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", reqUsers.getUserType()>0?reqUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", reqUsers.getLastname()!=null?reqUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", reqUsers.getFirstname()!=null?reqUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqUsers.getPassword()!=null?reqUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqUsers.getEmail()!=null?reqUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", reqUsers.getMobile()!=null?reqUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", reqUsers.getRole()!=null?reqUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("password length error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    @Test
    public void testAddUsersByInvalidEmail() throws Exception {
        if(this.oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users reqUsers = new Users();
            reqUsers.setUserType(1);
    reqUsers.setLastname("aliquip");
    reqUsers.setFirstname("consequat");
    reqUsers.setPassword("ucode1234");
    reqUsers.setEmail("aliquip@ucode.ai9");
    reqUsers.setMobile("utclf7");
    reqUsers.setRole("sunt");
        reqUsers.setEmail(reqUsers.getEmail().replace("@","")); // set  invalid email by replacing @ 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", reqUsers.getUserType()>0?reqUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", reqUsers.getLastname()!=null?reqUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", reqUsers.getFirstname()!=null?reqUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqUsers.getPassword()!=null?reqUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqUsers.getEmail()!=null?reqUsers.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "mobile", reqUsers.getMobile()!=null?reqUsers.getMobile():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", reqUsers.getRole()!=null?reqUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("email should be valid email address")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    
    @Test  // 
    public void testUpdateUsersByID() throws Exception {
        if(oneUsers.getLastname()==null){
            GetOneUsers();
        }
        Users updateUsers = new Users();
            updateUsers.setUserType(1);
    updateUsers.setLastname("adclf");
    updateUsers.setFirstname("sunt");
    updateUsers.setPassword("ucode1234");
    updateUsers.setRole("consequat");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "userType", updateUsers.getUserType()>0?updateUsers.getUserType():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "lastname", updateUsers.getLastname()!=null?updateUsers.getLastname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "firstname", updateUsers.getFirstname()!=null?updateUsers.getFirstname():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", updateUsers.getPassword()!=null?updateUsers.getPassword():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "role", updateUsers.getRole()!=null?updateUsers.getRole():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/users/"+oneUsers.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(Matchers.containsString(updateUsers.getLastname())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetUsersByID() throws Exception {
        if(oneUsers.getLastname()==null){
        GetOneUsers();
        }
       
        String path = "/users/"+oneUsers.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(Matchers.containsString(oneUsers.getLastname())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteUsersByID() throws Exception {
        if(oneUsers.getLastname()==null){
            GetOneUsers();
        }
        String path = "/users/"+oneUsers.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneUsers() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneUsers_ = objectMapper.readValue(response,PagingData.class);
        if(oneUsers_.getDocs().size()>0){
        int i =oneUsers_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneUsers_.getDocs();
        Users obj =convertToUsers( one.get(i-1)); // last one
        this.oneUsers = obj;
        }
     }
    private static Users convertToUsers(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Users object
        
        // Retrieve other properties as needed
        Users objCls = new Users();
        
          String userType = (String) entry.get("userType");
          Integer userType_ = Integer.parseInt(userType);
          objCls.setUserType(userType_);
                      

          String lastname = (String) entry.get("lastname");
          objCls.setLastname(lastname);
              

          String firstname = (String) entry.get("firstname");
          objCls.setFirstname(firstname);
              

          String password = (String) entry.get("password");
          objCls.setPassword(password);
              

          String email = (String) entry.get("email");
          objCls.setEmail(email);
              

          String mobile = (String) entry.get("mobile");
          objCls.setMobile(mobile);
              

          String role = (String) entry.get("role");
          objCls.setRole(role);
              
        
    String id = String.valueOf(entry.get("id"));
    //String id_= Long.valueOf(id);
    objCls.setId(id);
            
        return objCls;
    }
    String  arrayToString(String[] array){
        String format = String.join(", ", Collections.nCopies(array.length, "\"%s\""));
        String formattedString = String.format(format, (Object[]) array);
        String formatted=String.format("[%s]",  formattedString);
       return  formatted ;
    }

}
    