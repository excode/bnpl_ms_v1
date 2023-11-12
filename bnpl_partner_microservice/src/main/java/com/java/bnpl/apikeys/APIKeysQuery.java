package com.java.bnpl.apikeys;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class APIKeysQuery  extends APIKeys{
	@Getter @Setter private QueryEnum keyValue_mode ;
	@Getter @Setter private String[] keyValue_array ;
	@Getter @Setter private QueryEnum permissions_mode ;
	@Getter @Setter private String[] permissions_array ;
	@Getter @Setter private QueryEnum issueDate_mode ;
	@Getter @Setter private QueryEnum expiryDate_mode ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum partnerId_mode ;
	@Getter @Setter private Number[] partnerId_array ;
	@Getter @Setter private QueryEnum status_mode ;
	@Getter @Setter private Number[] status_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}