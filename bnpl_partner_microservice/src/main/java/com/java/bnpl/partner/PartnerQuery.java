package com.java.bnpl.partner;
import com.java.bnpl.ucodeutility.QueryEnum;

import lombok.Getter;
import lombok.Setter;
public class PartnerQuery  extends Partner{
	@Getter @Setter private QueryEnum contactName_mode ;
	@Getter @Setter private String[] contactName_array ;
	@Getter @Setter private QueryEnum email_mode ;
	@Getter @Setter private String[] email_array ;
	@Getter @Setter private QueryEnum phone_mode ;
	@Getter @Setter private String[] phone_array ;
	@Getter @Setter private QueryEnum address_mode ;
	@Getter @Setter private String[] address_array ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum bussinessName_mode ;
	@Getter @Setter private String[] bussinessName_array ;
	@Getter @Setter private QueryEnum logo_mode ;
	@Getter @Setter private String[] logo_array ;
	@Getter @Setter private QueryEnum facebook_mode ;
	@Getter @Setter private String[] facebook_array ;
	@Getter @Setter private QueryEnum instagram_mode ;
	@Getter @Setter private String[] instagram_array ;
	@Getter @Setter private QueryEnum twitter_mode ;
	@Getter @Setter private String[] twitter_array ;
	@Getter @Setter private QueryEnum whatsapp_mode ;
	@Getter @Setter private String[] whatsapp_array ;
	@Getter @Setter private QueryEnum youtube_mode ;
	@Getter @Setter private String[] youtube_array ;
	@Getter @Setter private QueryEnum agreement_mode ;
	@Getter @Setter private String[] agreement_array ;
	@Getter @Setter private QueryEnum city_mode ;
	@Getter @Setter private String[] city_array ;
	@Getter @Setter private QueryEnum state_mode ;
	@Getter @Setter private String[] state_array ;
	@Getter @Setter private QueryEnum zipcode_mode ;
	@Getter @Setter private Number[] zipcode_array ;
	@Getter @Setter private QueryEnum commision_mode ;
	@Getter @Setter private Number[] commision_array ;
	@Getter @Setter private QueryEnum transactionlimit_mode ;
	@Getter @Setter private Number[] transactionlimit_array ;
	@Getter @Setter private QueryEnum password_mode ;
	@Getter @Setter private String[] password_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
	@Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
	
}