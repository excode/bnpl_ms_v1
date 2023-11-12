
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
import com.java.bnpl.partner.Partner;
import com.java.bnpl.ucodeutility.PagingData;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PartnerRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Partner onePartner= new Partner();   
    
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
    public void testAddPartner() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            onePartner.setContactName("elit");
    onePartner.setEmail("veniam@ucode.ai7");
    onePartner.setPhone("601742169225");
    onePartner.setAddress("Eu consequat amet dolor aliquip pariatur sunt duis sint.");
    onePartner.setBussinessName("estclf");
    onePartner.setLogo("");
    onePartner.setFacebook("https://excode.net");
    onePartner.setInstagram("https://excode.net");
    onePartner.setTwitter("https://excode.net");
    onePartner.setWhatsapp("https://excode.net");
    onePartner.setYoutube("https://ucode.ai");
    onePartner.setAgreement("");
    onePartner.setCity("officia");
    onePartner.setState("excepteur");
    onePartner.setZipcode(1);
    onePartner.setCommision(1.35);
    onePartner.setTransactionLimit(1.35);
    onePartner.setPassword("ucode1234");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", onePartner.getContactName()!=null?onePartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", onePartner.getEmail()!=null?onePartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", onePartner.getPhone()!=null?onePartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", onePartner.getAddress()!=null?onePartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", onePartner.getBussinessName()!=null?onePartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", onePartner.getLogo()!=null?onePartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", onePartner.getFacebook()!=null?onePartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", onePartner.getInstagram()!=null?onePartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", onePartner.getTwitter()!=null?onePartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", onePartner.getWhatsapp()!=null?onePartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", onePartner.getYoutube()!=null?onePartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", onePartner.getAgreement()!=null?onePartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", onePartner.getCity()!=null?onePartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", onePartner.getState()!=null?onePartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", onePartner.getZipcode()>0?onePartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", onePartner.getCommision()>0?onePartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", onePartner.getTransactionLimit()>0?onePartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", onePartner.getPassword()!=null?onePartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.contactName").value(Matchers.containsString(onePartner.getContactName())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetPartners() throws Exception {

        if(onePartner.getContactName()==null){
            GetOnePartner();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/partner")
                    .param("address", sortenString(onePartner.getAddress()))
        .param("address_array","dummy1")
        .param("address_array",onePartner.getAddress())
        .param("address_array","dummy2")
        .param("facebook", sortenString(onePartner.getFacebook()))
        .param("facebook_array","dummy1")
        .param("facebook_array",onePartner.getFacebook())
        .param("facebook_array","dummy2")
        .param("instagram", sortenString(onePartner.getInstagram()))
        .param("instagram_array","dummy1")
        .param("instagram_array",onePartner.getInstagram())
        .param("instagram_array","dummy2")
        .param("twitter", sortenString(onePartner.getTwitter()))
        .param("twitter_array","dummy1")
        .param("twitter_array",onePartner.getTwitter())
        .param("twitter_array","dummy2")
        .param("whatsapp", sortenString(onePartner.getWhatsapp()))
        .param("whatsapp_array","dummy1")
        .param("whatsapp_array",onePartner.getWhatsapp())
        .param("whatsapp_array","dummy2")
        .param("youtube", sortenString(onePartner.getYoutube()))
        .param("youtube_array","dummy1")
        .param("youtube_array",onePartner.getYoutube())
        .param("youtube_array","dummy2")
        .param("zipcode", Integer.toString(onePartner.getZipcode()))
        .param("zipcode_array",Integer.toString(onePartner.getZipcode()-1))
        .param("zipcode_array",Integer.toString(onePartner.getZipcode()+1))
        .param("commision", Double.toString(onePartner.getCommision()))
        .param("commision_array",Double.toString(onePartner.getCommision()-1))
        .param("commision_array",Double.toString(onePartner.getCommision()+1))
        .param("limit", Double.toString(onePartner.getTransactionLimit()))
        .param("limit_array",Double.toString(onePartner.getTransactionLimit()-1))
        .param("limit_array",Double.toString(onePartner.getTransactionLimit()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(onePartner.getContactName()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void testAddPartnerByDuplicateEmail() throws Exception {
        if(this.onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner dupemailPartner = new Partner();
            dupemailPartner.setContactName("reprehenderit");
    dupemailPartner.setEmail("officia@ucode.ai7");
    dupemailPartner.setPhone("601771984752");
    dupemailPartner.setAddress("Ullamco ex nisi dolore deserunt tempor commodo in nostrud ipsum velit sint.");
    dupemailPartner.setBussinessName("utclf");
    dupemailPartner.setLogo("");
    dupemailPartner.setFacebook("https://ucode.ai");
    dupemailPartner.setInstagram("https://excode.net");
    dupemailPartner.setTwitter("https://excode.net");
    dupemailPartner.setWhatsapp("https://mypaaa.com");
    dupemailPartner.setYoutube("https://mypaaa.com");
    dupemailPartner.setAgreement("");
    dupemailPartner.setCity("laborum");
    dupemailPartner.setState("euclf");
    dupemailPartner.setZipcode(1);
    dupemailPartner.setCommision(1.35);
    dupemailPartner.setTransactionLimit(1.35);
    dupemailPartner.setPassword("ucode1234");
        dupemailPartner.setEmail(onePartner.getEmail()); // set a  duplicate value
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", dupemailPartner.getContactName()!=null?dupemailPartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", dupemailPartner.getEmail()!=null?dupemailPartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", dupemailPartner.getPhone()!=null?dupemailPartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", dupemailPartner.getAddress()!=null?dupemailPartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", dupemailPartner.getBussinessName()!=null?dupemailPartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", dupemailPartner.getLogo()!=null?dupemailPartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", dupemailPartner.getFacebook()!=null?dupemailPartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", dupemailPartner.getInstagram()!=null?dupemailPartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", dupemailPartner.getTwitter()!=null?dupemailPartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", dupemailPartner.getWhatsapp()!=null?dupemailPartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", dupemailPartner.getYoutube()!=null?dupemailPartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", dupemailPartner.getAgreement()!=null?dupemailPartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", dupemailPartner.getCity()!=null?dupemailPartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", dupemailPartner.getState()!=null?dupemailPartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", dupemailPartner.getZipcode()>0?dupemailPartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", dupemailPartner.getCommision()>0?dupemailPartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", dupemailPartner.getTransactionLimit()>0?dupemailPartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", dupemailPartner.getPassword()!=null?dupemailPartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("email_NO")))
        .andDo(MockMvcResultHandlers.print());
    }
        

    @Test
    public void testAddPartnerByDuplicatePhone() throws Exception {
        if(this.onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner dupphonePartner = new Partner();
            dupphonePartner.setContactName("euclf");
    dupphonePartner.setEmail("magna@ucode.ai9");
    dupphonePartner.setPhone("601757060610");
    dupphonePartner.setAddress("Ad Lorem qui irure laboris ipsum.");
    dupphonePartner.setBussinessName("utclf");
    dupphonePartner.setLogo("");
    dupphonePartner.setFacebook("https://excode.net");
    dupphonePartner.setInstagram("https://ucode.ai");
    dupphonePartner.setTwitter("https://ucode.ai");
    dupphonePartner.setWhatsapp("https://excode.net");
    dupphonePartner.setYoutube("https://excode.net");
    dupphonePartner.setAgreement("");
    dupphonePartner.setCity("excepteur");
    dupphonePartner.setState("irure");
    dupphonePartner.setZipcode(1);
    dupphonePartner.setCommision(1.35);
    dupphonePartner.setTransactionLimit(1.35);
    dupphonePartner.setPassword("ucode1234");
        dupphonePartner.setPhone(onePartner.getPhone()); // set a  duplicate value
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", dupphonePartner.getContactName()!=null?dupphonePartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", dupphonePartner.getEmail()!=null?dupphonePartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", dupphonePartner.getPhone()!=null?dupphonePartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", dupphonePartner.getAddress()!=null?dupphonePartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", dupphonePartner.getBussinessName()!=null?dupphonePartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", dupphonePartner.getLogo()!=null?dupphonePartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", dupphonePartner.getFacebook()!=null?dupphonePartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", dupphonePartner.getInstagram()!=null?dupphonePartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", dupphonePartner.getTwitter()!=null?dupphonePartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", dupphonePartner.getWhatsapp()!=null?dupphonePartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", dupphonePartner.getYoutube()!=null?dupphonePartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", dupphonePartner.getAgreement()!=null?dupphonePartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", dupphonePartner.getCity()!=null?dupphonePartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", dupphonePartner.getState()!=null?dupphonePartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", dupphonePartner.getZipcode()>0?dupphonePartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", dupphonePartner.getCommision()>0?dupphonePartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", dupphonePartner.getTransactionLimit()>0?dupphonePartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", dupphonePartner.getPassword()!=null?dupphonePartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("phone_NO")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    
    @Test
    public void testAddPartnerByMinPassword_6() throws Exception {
        if(this.onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner reqPartner = new Partner();
            reqPartner.setContactName("dolor");
    reqPartner.setEmail("consectetur@ucode.ai8");
    reqPartner.setPhone("601778954038");
    reqPartner.setAddress("Dolor deserunt veniam adipisicing eu Lorem.");
    reqPartner.setBussinessName("cillum");
    reqPartner.setLogo("");
    reqPartner.setFacebook("https://ucode.ai");
    reqPartner.setInstagram("https://mypaaa.com");
    reqPartner.setTwitter("https://ucode.ai");
    reqPartner.setWhatsapp("https://mypaaa.com");
    reqPartner.setYoutube("https://excode.net");
    reqPartner.setAgreement("");
    reqPartner.setCity("adipisicing");
    reqPartner.setState("doclf");
    reqPartner.setZipcode(1);
    reqPartner.setCommision(1.35);
    reqPartner.setTransactionLimit(1.35);
    reqPartner.setPassword("ucode1234");
        reqPartner.setPassword("aaaaa"); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", reqPartner.getContactName()!=null?reqPartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqPartner.getEmail()!=null?reqPartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", reqPartner.getPhone()!=null?reqPartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", reqPartner.getAddress()!=null?reqPartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", reqPartner.getBussinessName()!=null?reqPartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", reqPartner.getLogo()!=null?reqPartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", reqPartner.getFacebook()!=null?reqPartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", reqPartner.getInstagram()!=null?reqPartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", reqPartner.getTwitter()!=null?reqPartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", reqPartner.getWhatsapp()!=null?reqPartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", reqPartner.getYoutube()!=null?reqPartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", reqPartner.getAgreement()!=null?reqPartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", reqPartner.getCity()!=null?reqPartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", reqPartner.getState()!=null?reqPartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", reqPartner.getZipcode()>0?reqPartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", reqPartner.getCommision()>0?reqPartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", reqPartner.getTransactionLimit()>0?reqPartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqPartner.getPassword()!=null?reqPartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("password length error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        

    @Test
    public void testAddPartnerByMaxPassword_100() throws Exception {
        if(this.onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner reqPartner = new Partner();
            reqPartner.setContactName("dolor");
    reqPartner.setEmail("consectetur@ucode.ai8");
    reqPartner.setPhone("601778954038");
    reqPartner.setAddress("Dolor deserunt veniam adipisicing eu Lorem.");
    reqPartner.setBussinessName("cillum");
    reqPartner.setLogo("");
    reqPartner.setFacebook("https://ucode.ai");
    reqPartner.setInstagram("https://mypaaa.com");
    reqPartner.setTwitter("https://ucode.ai");
    reqPartner.setWhatsapp("https://mypaaa.com");
    reqPartner.setYoutube("https://excode.net");
    reqPartner.setAgreement("");
    reqPartner.setCity("adipisicing");
    reqPartner.setState("doclf");
    reqPartner.setZipcode(1);
    reqPartner.setCommision(1.35);
    reqPartner.setTransactionLimit(1.35);
    reqPartner.setPassword("ucode1234");
        reqPartner.setPassword("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", reqPartner.getContactName()!=null?reqPartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqPartner.getEmail()!=null?reqPartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", reqPartner.getPhone()!=null?reqPartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", reqPartner.getAddress()!=null?reqPartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", reqPartner.getBussinessName()!=null?reqPartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", reqPartner.getLogo()!=null?reqPartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", reqPartner.getFacebook()!=null?reqPartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", reqPartner.getInstagram()!=null?reqPartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", reqPartner.getTwitter()!=null?reqPartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", reqPartner.getWhatsapp()!=null?reqPartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", reqPartner.getYoutube()!=null?reqPartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", reqPartner.getAgreement()!=null?reqPartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", reqPartner.getCity()!=null?reqPartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", reqPartner.getState()!=null?reqPartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", reqPartner.getZipcode()>0?reqPartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", reqPartner.getCommision()>0?reqPartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", reqPartner.getTransactionLimit()>0?reqPartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqPartner.getPassword()!=null?reqPartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("password length error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    @Test
    public void testAddPartnerByInvalidEmail() throws Exception {
        if(this.onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner reqPartner = new Partner();
            reqPartner.setContactName("dolor");
    reqPartner.setEmail("consectetur@ucode.ai8");
    reqPartner.setPhone("601778954038");
    reqPartner.setAddress("Dolor deserunt veniam adipisicing eu Lorem.");
    reqPartner.setBussinessName("cillum");
    reqPartner.setLogo("");
    reqPartner.setFacebook("https://ucode.ai");
    reqPartner.setInstagram("https://mypaaa.com");
    reqPartner.setTwitter("https://ucode.ai");
    reqPartner.setWhatsapp("https://mypaaa.com");
    reqPartner.setYoutube("https://excode.net");
    reqPartner.setAgreement("");
    reqPartner.setCity("adipisicing");
    reqPartner.setState("doclf");
    reqPartner.setZipcode(1);
    reqPartner.setCommision(1.35);
    reqPartner.setTransactionLimit(1.35);
    reqPartner.setPassword("ucode1234");
        reqPartner.setEmail(reqPartner.getEmail().replace("@","")); // set  invalid email by replacing @ 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", reqPartner.getContactName()!=null?reqPartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqPartner.getEmail()!=null?reqPartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", reqPartner.getPhone()!=null?reqPartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", reqPartner.getAddress()!=null?reqPartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", reqPartner.getBussinessName()!=null?reqPartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", reqPartner.getLogo()!=null?reqPartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", reqPartner.getFacebook()!=null?reqPartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", reqPartner.getInstagram()!=null?reqPartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", reqPartner.getTwitter()!=null?reqPartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", reqPartner.getWhatsapp()!=null?reqPartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", reqPartner.getYoutube()!=null?reqPartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", reqPartner.getAgreement()!=null?reqPartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", reqPartner.getCity()!=null?reqPartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", reqPartner.getState()!=null?reqPartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", reqPartner.getZipcode()>0?reqPartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", reqPartner.getCommision()>0?reqPartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", reqPartner.getTransactionLimit()>0?reqPartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqPartner.getPassword()!=null?reqPartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("email should be valid email address")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    @Test
    public void testAddPartnerByInvalidPhone() throws Exception {
        if(this.onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner reqPartner = new Partner();
            reqPartner.setContactName("dolor");
    reqPartner.setEmail("consectetur@ucode.ai8");
    reqPartner.setPhone("601778954038");
    reqPartner.setAddress("Dolor deserunt veniam adipisicing eu Lorem.");
    reqPartner.setBussinessName("cillum");
    reqPartner.setLogo("");
    reqPartner.setFacebook("https://ucode.ai");
    reqPartner.setInstagram("https://mypaaa.com");
    reqPartner.setTwitter("https://ucode.ai");
    reqPartner.setWhatsapp("https://mypaaa.com");
    reqPartner.setYoutube("https://excode.net");
    reqPartner.setAgreement("");
    reqPartner.setCity("adipisicing");
    reqPartner.setState("doclf");
    reqPartner.setZipcode(1);
    reqPartner.setCommision(1.35);
    reqPartner.setTransactionLimit(1.35);
    reqPartner.setPassword("ucode1234");
        reqPartner.setPhone(reqPartner.getPhone()+"a"); // set  invalid phone/mobile  value by adding a char
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", reqPartner.getContactName()!=null?reqPartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "email", reqPartner.getEmail()!=null?reqPartner.getEmail():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "phone", reqPartner.getPhone()!=null?reqPartner.getPhone():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", reqPartner.getAddress()!=null?reqPartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", reqPartner.getBussinessName()!=null?reqPartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", reqPartner.getLogo()!=null?reqPartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", reqPartner.getFacebook()!=null?reqPartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", reqPartner.getInstagram()!=null?reqPartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", reqPartner.getTwitter()!=null?reqPartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", reqPartner.getWhatsapp()!=null?reqPartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", reqPartner.getYoutube()!=null?reqPartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", reqPartner.getAgreement()!=null?reqPartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", reqPartner.getCity()!=null?reqPartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", reqPartner.getState()!=null?reqPartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", reqPartner.getZipcode()>0?reqPartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", reqPartner.getCommision()>0?reqPartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", reqPartner.getTransactionLimit()>0?reqPartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", reqPartner.getPassword()!=null?reqPartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/partner")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("phone should be valid")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    @Test  // 
    public void testUpdatePartnerByID() throws Exception {
        if(onePartner.getContactName()==null){
            GetOnePartner();
        }
        Partner updatePartner = new Partner();
            updatePartner.setContactName("excepteur");
    updatePartner.setAddress("Cillum in laborum occaecat eu aliquip commodo.");
    updatePartner.setBussinessName("proident");
    updatePartner.setLogo("");
    updatePartner.setFacebook("https://excode.net");
    updatePartner.setInstagram("https://excode.net");
    updatePartner.setTwitter("https://excode.net");
    updatePartner.setWhatsapp("https://ucode.ai");
    updatePartner.setYoutube("https://mypaaa.com");
    updatePartner.setAgreement("");
    updatePartner.setCity("dolore");
    updatePartner.setState("nostrud");
    updatePartner.setZipcode(1);
    updatePartner.setCommision(1.35);
    updatePartner.setTransactionLimit(1.35);
    updatePartner.setPassword("ucode1234");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "contactName", updatePartner.getContactName()!=null?updatePartner.getContactName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "address", updatePartner.getAddress()!=null?updatePartner.getAddress():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "bussinessName", updatePartner.getBussinessName()!=null?updatePartner.getBussinessName():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "logo", updatePartner.getLogo()!=null?updatePartner.getLogo():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "facebook", updatePartner.getFacebook()!=null?updatePartner.getFacebook():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instagram", updatePartner.getInstagram()!=null?updatePartner.getInstagram():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "twitter", updatePartner.getTwitter()!=null?updatePartner.getTwitter():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "whatsapp", updatePartner.getWhatsapp()!=null?updatePartner.getWhatsapp():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "youtube", updatePartner.getYoutube()!=null?updatePartner.getYoutube():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "agreement", updatePartner.getAgreement()!=null?updatePartner.getAgreement():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "city", updatePartner.getCity()!=null?updatePartner.getCity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "state", updatePartner.getState()!=null?updatePartner.getState():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "zipcode", updatePartner.getZipcode()>0?updatePartner.getZipcode():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "commision", updatePartner.getCommision()>0?updatePartner.getCommision():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "limit", updatePartner.getTransactionLimit()>0?updatePartner.getTransactionLimit():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "password", updatePartner.getPassword()!=null?updatePartner.getPassword():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/partner/"+onePartner.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactName").value(Matchers.containsString(updatePartner.getContactName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetPartnerByID() throws Exception {
        if(onePartner.getContactName()==null){
        GetOnePartner();
        }
       
        String path = "/partner/"+onePartner.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactName").value(Matchers.containsString(onePartner.getContactName())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeletePartnerByID() throws Exception {
        if(onePartner.getContactName()==null){
            GetOnePartner();
        }
        String path = "/partner/"+onePartner.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOnePartner() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/partner")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> onePartner_ = objectMapper.readValue(response,PagingData.class);
        if(onePartner_.getDocs().size()>0){
        int i =onePartner_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= onePartner_.getDocs();
        Partner obj =convertToPartner( one.get(i-1)); // last one
        this.onePartner = obj;
        }
     }
    private static Partner convertToPartner(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Partner object
        
        // Retrieve other properties as needed
        Partner objCls = new Partner();
        
          String contactName = (String) entry.get("contactName");
          objCls.setContactName(contactName);
              

          String email = (String) entry.get("email");
          objCls.setEmail(email);
              

          String phone = (String) entry.get("phone");
          objCls.setPhone(phone);
              

          String address = (String) entry.get("address");
          objCls.setAddress(address);
              

          String bussinessName = (String) entry.get("bussinessName");
          objCls.setBussinessName(bussinessName);
              

          String logo = (String) entry.get("logo");
          objCls.setLogo(logo);
              

          String facebook = (String) entry.get("facebook");
          objCls.setFacebook(facebook);
              

          String instagram = (String) entry.get("instagram");
          objCls.setInstagram(instagram);
              

          String twitter = (String) entry.get("twitter");
          objCls.setTwitter(twitter);
              

          String whatsapp = (String) entry.get("whatsapp");
          objCls.setWhatsapp(whatsapp);
              

          String youtube = (String) entry.get("youtube");
          objCls.setYoutube(youtube);
              

          String agreement = (String) entry.get("agreement");
          objCls.setAgreement(agreement);
              

          String city = (String) entry.get("city");
          objCls.setCity(city);
              

          String state = (String) entry.get("state");
          objCls.setState(state);
              

          String zipcode = (String) entry.get("zipcode");
          Integer zipcode_ = Integer.parseInt(zipcode);
          objCls.setZipcode(zipcode_);
                      

          String commision = (String) entry.get("commision");
          double commision_ = Double.parseDouble(commision);
          objCls.setCommision(commision_);
                  

          String limit = (String) entry.get("limit");
          double limit_ = Double.parseDouble(limit);
          objCls.setTransactionLimit(limit_);
                  

          String password = (String) entry.get("password");
          objCls.setPassword(password);
              
        
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
    