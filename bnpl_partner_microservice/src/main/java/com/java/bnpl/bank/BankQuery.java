package com.java.bnpl.bank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class BankQuery  extends Bank{
	@Getter @Setter private QueryEnum name_mode ;
	@Getter @Setter private String[] name_array ;
	@Getter @Setter private QueryEnum accountName_mode ;
	@Getter @Setter private String[] accountName_array ;
	@Getter @Setter private QueryEnum swift_mode ;
	@Getter @Setter private String[] swift_array ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum accountNumber_mode ;
	@Getter @Setter private Number[] accountNumber_array ;
	@Getter @Setter private QueryEnum city_mode ;
	@Getter @Setter private String[] city_array ;
	@Getter @Setter private QueryEnum country_mode ;
	@Getter @Setter private String[] country_array ;
	@Getter @Setter private QueryEnum address_mode ;
	@Getter @Setter private String[] address_array ;
	@Getter @Setter private QueryEnum postcode_mode ;
	@Getter @Setter private Number[] postcode_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}