package com.java.bnpl.partner;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.java.bnpl.exception.CustomeException;
import com.java.bnpl.ucodeutility.PagingData;
import com.java.bnpl.ucodeutility.QueryBuilder;
import com.java.bnpl.ucodeutility.QueryEnum;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private EntityManager entityManager;

    
    public PartnerService(PartnerRepository partnerRepository,EntityManager entityManager){
        this.partnerRepository = partnerRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Partner>> getPartners(PartnerQuery partnerQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildPartnerQuery(partnerQuery);
        
       int size = partnerQuery.getLimit()>0 ?partnerQuery.getLimit():20;
       int page = partnerQuery.getPage()>0 ?partnerQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Partner> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Partner getPartner(Long id){
        PartnerQuery partnerQuery=new PartnerQuery();
        partnerQuery.setId(id);
        partnerQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         partnerQuery.setCreatedBy(username);
         */
        List<Query> query=buildPartnerQuery(partnerQuery);
        List<Partner> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Partner> getPartnerSuggestions(PartnerQuery partnerQuery){
       
        String[] cols={"contactname","email","phone","address","bussinessname","facebook","instagram","twitter","whatsapp","youtube","city","state"};
        partnerQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildPartnerQuery(partnerQuery);
        List<Partner> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Partner> getPartnerAll(PartnerQuery partnerQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildPartnerQuery(partnerQuery);
        List<Partner> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Partner addNewPartner(Partner partner) {
       
         Optional<Partner> partnerByEmail = partnerRepository.findPartnerByEmail(partner.getEmail());
          if(partnerByEmail.isPresent()){
              throw new CustomeException("email_NO_AVAILABLE",null);
          }
        
         Optional<Partner> partnerByPhone = partnerRepository.findPartnerByPhone(partner.getPhone());
          if(partnerByPhone.isPresent()){
              throw new CustomeException("phone_NO_AVAILABLE",null);
          }
        
        return partnerRepository.save(partner);
    }



    public void deletePartner(Long id) {
        PartnerQuery partnerQuery=new PartnerQuery();
        partnerQuery.setId(id);
        partnerQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         partnerQuery.setCreateby_mode(QueryEnum.equals);
         partnerQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildPartnerQuery(partnerQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        partnerRepository.deleteById(id);
    }

     @Transactional
     public Partner updatePartner(Long id, Partner partner) {
        Partner check = partnerRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(partner.getContactName()!=null && partner.getContactName().length()>0 && !Objects.equals(check.getContactName(),partner.getContactName())){
        
        check.setContactName(partner.getContactName());
    }
                             
    if(partner.getEmail()!=null && partner.getEmail().length()>0 && !Objects.equals(check.getEmail(),partner.getEmail())){
        
    Optional<Partner> checkPartner = partnerRepository.findPartnerByEmail(partner.getEmail());
        if(checkPartner.isPresent()){
            throw new IllegalStateException("Email_NOT_AVAILABLE");
        }
    
        check.setEmail(partner.getEmail());
    }
                             
    if(partner.getPhone()!=null && partner.getPhone().length()>0 && !Objects.equals(check.getPhone(),partner.getPhone())){
        
    Optional<Partner> checkPartner = partnerRepository.findPartnerByPhone(partner.getPhone());
        if(checkPartner.isPresent()){
            throw new IllegalStateException("Phone_NOT_AVAILABLE");
        }
    
        check.setPhone(partner.getPhone());
    }
                             
    if(partner.getAddress()!=null && partner.getAddress().length()>0 && !Objects.equals(check.getAddress(),partner.getAddress())){
        
        check.setAddress(partner.getAddress());
    }
                             
    if(partner.getCreateBy()!=null && partner.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),partner.getCreateBy())){
        
        check.setCreateBy(partner.getCreateBy());
    }

        if(partner.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),partner.getCreateAt())){
            check.setCreateAt(partner.getCreateAt());
        }
                             
    if(partner.getUpdateBy()!=null && partner.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),partner.getUpdateBy())){
        
        check.setUpdateBy(partner.getUpdateBy());
    }

        if(partner.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),partner.getUpdateAt())){
            check.setUpdateAt(partner.getUpdateAt());
        }
                             
    if(partner.getBussinessName()!=null && partner.getBussinessName().length()>0 && !Objects.equals(check.getBussinessName(),partner.getBussinessName())){
        
        check.setBussinessName(partner.getBussinessName());
    }
                             
    if(partner.getLogo()!=null && partner.getLogo().length()>0 && !Objects.equals(check.getLogo(),partner.getLogo())){
        
        check.setLogo(partner.getLogo());
    }
                             
    if(partner.getFacebook()!=null && partner.getFacebook().length()>0 && !Objects.equals(check.getFacebook(),partner.getFacebook())){
        
        check.setFacebook(partner.getFacebook());
    }
                             
    if(partner.getInstagram()!=null && partner.getInstagram().length()>0 && !Objects.equals(check.getInstagram(),partner.getInstagram())){
        
        check.setInstagram(partner.getInstagram());
    }
                             
    if(partner.getTwitter()!=null && partner.getTwitter().length()>0 && !Objects.equals(check.getTwitter(),partner.getTwitter())){
        
        check.setTwitter(partner.getTwitter());
    }
                             
    if(partner.getWhatsapp()!=null && partner.getWhatsapp().length()>0 && !Objects.equals(check.getWhatsapp(),partner.getWhatsapp())){
        
        check.setWhatsapp(partner.getWhatsapp());
    }
                             
    if(partner.getYoutube()!=null && partner.getYoutube().length()>0 && !Objects.equals(check.getYoutube(),partner.getYoutube())){
        
        check.setYoutube(partner.getYoutube());
    }
                             
    if(partner.getAgreement()!=null && partner.getAgreement().length()>0 && !Objects.equals(check.getAgreement(),partner.getAgreement())){
        
        check.setAgreement(partner.getAgreement());
    }
                             
    if(partner.getCity()!=null && partner.getCity().length()>0 && !Objects.equals(check.getCity(),partner.getCity())){
        
        check.setCity(partner.getCity());
    }
                             
    if(partner.getState()!=null && partner.getState().length()>0 && !Objects.equals(check.getState(),partner.getState())){
        
        check.setState(partner.getState());
    }
  

        if(partner.getZipcode()!=null   && !Objects.equals(check.getZipcode(),partner.getZipcode())){
            check.setZipcode(partner.getZipcode());
        }

        if(partner.getCommision()!=null  && !Objects.equals(check.getCommision(),partner.getCommision())){
            check.setCommision(partner.getCommision());
        }

        
                             
    if(partner.getPassword()!=null && partner.getPassword().length()>0 && !Objects.equals(check.getPassword(),partner.getPassword())){
        
        check.setPassword(partner.getPassword());
    }
     return partnerRepository.save(check);

    }

   
    private List<Query> buildPartnerQuery(PartnerQuery partnerQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Partner s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Partner s where 1=1 ");
        
        QueryBuilder qb_ContactNameArray = new QueryBuilder("contactName",partnerQuery.getContactName_array());
        if(qb_ContactNameArray.getSql().length()>0){
            sb.append( qb_ContactNameArray.getSql());
            sbTotal.append(qb_ContactNameArray.getSql());
        }
        QueryBuilder qb_ContactName = new QueryBuilder("contactName",partnerQuery.getContactName(),partnerQuery.getContactName_mode());
        if(qb_ContactName.getSql().length()>0){
            sb.append( qb_ContactName.getSql());
            sbTotal.append(qb_ContactName.getSql());
        }
        

        QueryBuilder qb_EmailArray = new QueryBuilder("email",partnerQuery.getEmail_array());
        if(qb_EmailArray.getSql().length()>0){
            sb.append( qb_EmailArray.getSql());
            sbTotal.append(qb_EmailArray.getSql());
        }
        QueryBuilder qb_Email = new QueryBuilder("email",partnerQuery.getEmail(),partnerQuery.getEmail_mode());
        if(qb_Email.getSql().length()>0){
            sb.append( qb_Email.getSql());
            sbTotal.append(qb_Email.getSql());
        }
        

        QueryBuilder qb_PhoneArray = new QueryBuilder("phone",partnerQuery.getPhone_array());
        if(qb_PhoneArray.getSql().length()>0){
            sb.append( qb_PhoneArray.getSql());
            sbTotal.append(qb_PhoneArray.getSql());
        }
        QueryBuilder qb_Phone = new QueryBuilder("phone",partnerQuery.getPhone(),partnerQuery.getPhone_mode());
        if(qb_Phone.getSql().length()>0){
            sb.append( qb_Phone.getSql());
            sbTotal.append(qb_Phone.getSql());
        }
        

        QueryBuilder qb_AddressArray = new QueryBuilder("address",partnerQuery.getAddress_array());
        if(qb_AddressArray.getSql().length()>0){
            sb.append( qb_AddressArray.getSql());
            sbTotal.append(qb_AddressArray.getSql());
        }
        QueryBuilder qb_Address = new QueryBuilder("address",partnerQuery.getAddress(),partnerQuery.getAddress_mode());
        if(qb_Address.getSql().length()>0){
            sb.append( qb_Address.getSql());
            sbTotal.append(qb_Address.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",partnerQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        if(partnerQuery.getId()!=null){
            QueryBuilder qb_Id = new QueryBuilder("id",partnerQuery.getId(),partnerQuery.getId_mode());
            if(qb_Id.getSql().length()>0){
                sb.append( qb_Id.getSql());
                sbTotal.append(qb_Id.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",partnerQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",partnerQuery.getCreateBy(),partnerQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",partnerQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",partnerQuery.getUpdateBy(),partnerQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_BussinessNameArray = new QueryBuilder("bussinessName",partnerQuery.getBussinessName_array());
        if(qb_BussinessNameArray.getSql().length()>0){
            sb.append( qb_BussinessNameArray.getSql());
            sbTotal.append(qb_BussinessNameArray.getSql());
        }
        QueryBuilder qb_BussinessName = new QueryBuilder("bussinessName",partnerQuery.getBussinessName(),partnerQuery.getBussinessName_mode());
        if(qb_BussinessName.getSql().length()>0){
            sb.append( qb_BussinessName.getSql());
            sbTotal.append(qb_BussinessName.getSql());
        }
        

        QueryBuilder qb_FacebookArray = new QueryBuilder("facebook",partnerQuery.getFacebook_array());
        if(qb_FacebookArray.getSql().length()>0){
            sb.append( qb_FacebookArray.getSql());
            sbTotal.append(qb_FacebookArray.getSql());
        }
        QueryBuilder qb_Facebook = new QueryBuilder("facebook",partnerQuery.getFacebook(),partnerQuery.getFacebook_mode());
        if(qb_Facebook.getSql().length()>0){
            sb.append( qb_Facebook.getSql());
            sbTotal.append(qb_Facebook.getSql());
        }
        

        QueryBuilder qb_InstagramArray = new QueryBuilder("instagram",partnerQuery.getInstagram_array());
        if(qb_InstagramArray.getSql().length()>0){
            sb.append( qb_InstagramArray.getSql());
            sbTotal.append(qb_InstagramArray.getSql());
        }
        QueryBuilder qb_Instagram = new QueryBuilder("instagram",partnerQuery.getInstagram(),partnerQuery.getInstagram_mode());
        if(qb_Instagram.getSql().length()>0){
            sb.append( qb_Instagram.getSql());
            sbTotal.append(qb_Instagram.getSql());
        }
        

        QueryBuilder qb_TwitterArray = new QueryBuilder("twitter",partnerQuery.getTwitter_array());
        if(qb_TwitterArray.getSql().length()>0){
            sb.append( qb_TwitterArray.getSql());
            sbTotal.append(qb_TwitterArray.getSql());
        }
        QueryBuilder qb_Twitter = new QueryBuilder("twitter",partnerQuery.getTwitter(),partnerQuery.getTwitter_mode());
        if(qb_Twitter.getSql().length()>0){
            sb.append( qb_Twitter.getSql());
            sbTotal.append(qb_Twitter.getSql());
        }
        

        QueryBuilder qb_WhatsappArray = new QueryBuilder("whatsapp",partnerQuery.getWhatsapp_array());
        if(qb_WhatsappArray.getSql().length()>0){
            sb.append( qb_WhatsappArray.getSql());
            sbTotal.append(qb_WhatsappArray.getSql());
        }
        QueryBuilder qb_Whatsapp = new QueryBuilder("whatsapp",partnerQuery.getWhatsapp(),partnerQuery.getWhatsapp_mode());
        if(qb_Whatsapp.getSql().length()>0){
            sb.append( qb_Whatsapp.getSql());
            sbTotal.append(qb_Whatsapp.getSql());
        }
        

        QueryBuilder qb_YoutubeArray = new QueryBuilder("youtube",partnerQuery.getYoutube_array());
        if(qb_YoutubeArray.getSql().length()>0){
            sb.append( qb_YoutubeArray.getSql());
            sbTotal.append(qb_YoutubeArray.getSql());
        }
        QueryBuilder qb_Youtube = new QueryBuilder("youtube",partnerQuery.getYoutube(),partnerQuery.getYoutube_mode());
        if(qb_Youtube.getSql().length()>0){
            sb.append( qb_Youtube.getSql());
            sbTotal.append(qb_Youtube.getSql());
        }
        

        QueryBuilder qb_CityArray = new QueryBuilder("city",partnerQuery.getCity_array());
        if(qb_CityArray.getSql().length()>0){
            sb.append( qb_CityArray.getSql());
            sbTotal.append(qb_CityArray.getSql());
        }
        QueryBuilder qb_City = new QueryBuilder("city",partnerQuery.getCity(),partnerQuery.getCity_mode());
        if(qb_City.getSql().length()>0){
            sb.append( qb_City.getSql());
            sbTotal.append(qb_City.getSql());
        }
        

        QueryBuilder qb_StateArray = new QueryBuilder("state",partnerQuery.getState_array());
        if(qb_StateArray.getSql().length()>0){
            sb.append( qb_StateArray.getSql());
            sbTotal.append(qb_StateArray.getSql());
        }
        QueryBuilder qb_State = new QueryBuilder("state",partnerQuery.getState(),partnerQuery.getState_mode());
        if(qb_State.getSql().length()>0){
            sb.append( qb_State.getSql());
            sbTotal.append(qb_State.getSql());
        }
        

        QueryBuilder qb_ZipcodeArray = new QueryBuilder("zipcode",partnerQuery.getZipcode_array());
        if(qb_ZipcodeArray.getSql().length()>0){
            sb.append( qb_ZipcodeArray.getSql());
            sbTotal.append(qb_ZipcodeArray.getSql());
        }
        if(partnerQuery.getZipcode()!=null){
            QueryBuilder qb_Zipcode = new QueryBuilder("zipcode",partnerQuery.getZipcode(),partnerQuery.getZipcode_mode());
            if(qb_Zipcode.getSql().length()>0){
                sb.append( qb_Zipcode.getSql());
                sbTotal.append(qb_Zipcode.getSql());
            }
        }
        

        QueryBuilder qb_CommisionArray = new QueryBuilder("commision",partnerQuery.getCommision_array());
        if(qb_CommisionArray.getSql().length()>0){
            sb.append( qb_CommisionArray.getSql());
            sbTotal.append(qb_CommisionArray.getSql());
        }
        if(partnerQuery.getCommision()!=null){
            QueryBuilder qb_Commision = new QueryBuilder("commision",partnerQuery.getCommision(),partnerQuery.getCommision_mode());
            if(qb_Commision.getSql().length()>0){
                sb.append( qb_Commision.getSql());
                sbTotal.append(qb_Commision.getSql());
            }
        }
        

        
        

      QueryBuilder qb_Keyword = new QueryBuilder(partnerQuery.getKeywordColumns(),partnerQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(partnerQuery.getSortBy()!=null && !partnerQuery.getSortBy().isEmpty()){
            if(partnerQuery.getSortDirection()!=null &&  partnerQuery.getSortDirection().toString()!=""){
                String ascDesc = partnerQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+partnerQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+partnerQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Partner.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Partner.class);
        // Fill Parameters values 

        
        if(partnerQuery.getContactName()!=null){
            query.setParameter("contactName",qb_ContactName.getValue());
            queryTotal.setParameter("contactName",qb_ContactName.getValue());
        }
        

        if(partnerQuery.getEmail()!=null){
            query.setParameter("email",qb_Email.getValue());
            queryTotal.setParameter("email",qb_Email.getValue());
        }
        

        if(partnerQuery.getPhone()!=null){
            query.setParameter("phone",qb_Phone.getValue());
            queryTotal.setParameter("phone",qb_Phone.getValue());
        }
        

        if(partnerQuery.getAddress()!=null){
            query.setParameter("address",qb_Address.getValue());
            queryTotal.setParameter("address",qb_Address.getValue());
        }
        

        if(partnerQuery.getId_array()!=null){
            if(partnerQuery.getId_array().length==2){
                query.setParameter("id1",partnerQuery.getId_array()[0]);
                queryTotal.setParameter("id1",partnerQuery.getId_array()[0]);

                query.setParameter("id2",partnerQuery.getId_array()[1]);
                queryTotal.setParameter("id2",partnerQuery.getId_array()[1]);
            }
        }
        
        if(partnerQuery.getId()!=null){
            query.setParameter("id",partnerQuery.getId());
            queryTotal.setParameter("id",partnerQuery.getId());
        }
        

        if(partnerQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(partnerQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(partnerQuery.getBussinessName()!=null){
            query.setParameter("bussinessName",qb_BussinessName.getValue());
            queryTotal.setParameter("bussinessName",qb_BussinessName.getValue());
        }
        

        if(partnerQuery.getFacebook()!=null){
            query.setParameter("facebook",qb_Facebook.getValue());
            queryTotal.setParameter("facebook",qb_Facebook.getValue());
        }
        

        if(partnerQuery.getInstagram()!=null){
            query.setParameter("instagram",qb_Instagram.getValue());
            queryTotal.setParameter("instagram",qb_Instagram.getValue());
        }
        

        if(partnerQuery.getTwitter()!=null){
            query.setParameter("twitter",qb_Twitter.getValue());
            queryTotal.setParameter("twitter",qb_Twitter.getValue());
        }
        

        if(partnerQuery.getWhatsapp()!=null){
            query.setParameter("whatsapp",qb_Whatsapp.getValue());
            queryTotal.setParameter("whatsapp",qb_Whatsapp.getValue());
        }
        

        if(partnerQuery.getYoutube()!=null){
            query.setParameter("youtube",qb_Youtube.getValue());
            queryTotal.setParameter("youtube",qb_Youtube.getValue());
        }
        

        if(partnerQuery.getCity()!=null){
            query.setParameter("city",qb_City.getValue());
            queryTotal.setParameter("city",qb_City.getValue());
        }
        

        if(partnerQuery.getState()!=null){
            query.setParameter("state",qb_State.getValue());
            queryTotal.setParameter("state",qb_State.getValue());
        }
        

        if(partnerQuery.getZipcode_array()!=null){
            if(partnerQuery.getZipcode_array().length==2){
                query.setParameter("zipcode1",partnerQuery.getZipcode_array()[0]);
                queryTotal.setParameter("zipcode1",partnerQuery.getZipcode_array()[0]);

                query.setParameter("zipcode2",partnerQuery.getZipcode_array()[1]);
                queryTotal.setParameter("zipcode2",partnerQuery.getZipcode_array()[1]);
            }
        }
        
        if(partnerQuery.getZipcode()!=null){
            query.setParameter("zipcode",partnerQuery.getZipcode());
            queryTotal.setParameter("zipcode",partnerQuery.getZipcode());
        }
        

        if(partnerQuery.getCommision_array()!=null){
            if(partnerQuery.getCommision_array().length==2){
                query.setParameter("commision1",partnerQuery.getCommision_array()[0]);
                queryTotal.setParameter("commision1",partnerQuery.getCommision_array()[0]);

                query.setParameter("commision2",partnerQuery.getCommision_array()[1]);
                queryTotal.setParameter("commision2",partnerQuery.getCommision_array()[1]);
            }
        }
        
        if(partnerQuery.getCommision()!=null){
            query.setParameter("commision",partnerQuery.getCommision());
            queryTotal.setParameter("commision",partnerQuery.getCommision());
        }
        

        
        
        
        

          if(partnerQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  