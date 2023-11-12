package com.java.bnpl.customer;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class CustomerQuery  extends Customer{
	@Getter @Setter private QueryEnum name_mode ;
	@Getter @Setter private String[] name_array ;
	@Getter @Setter private QueryEnum address_mode ;
	@Getter @Setter private String[] address_array ;
	@Getter @Setter private QueryEnum email_mode ;
	@Getter @Setter private String[] email_array ;
	@Getter @Setter private QueryEnum phone_mode ;
	@Getter @Setter private String[] phone_array ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum city_mode ;
	@Getter @Setter private String[] city_array ;
	@Getter @Setter private QueryEnum state_mode ;
	@Getter @Setter private String[] state_array ;
	@Getter @Setter private QueryEnum postcode_mode ;
	@Getter @Setter private String[] postcode_array ;
	@Getter @Setter private QueryEnum status_mode ;
	@Getter @Setter private Number[] status_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}