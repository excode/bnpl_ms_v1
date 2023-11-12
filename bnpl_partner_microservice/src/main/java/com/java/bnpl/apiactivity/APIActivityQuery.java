package com.java.bnpl.apiactivity;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class APIActivityQuery  extends APIActivity{
	@Getter @Setter private QueryEnum partnerId_mode ;
	@Getter @Setter private Number[] partnerId_array ;
	@Getter @Setter private QueryEnum aPIKeyID_mode ;
	@Getter @Setter private String[] aPIKeyID_array ;
	@Getter @Setter private QueryEnum aPICallName_mode ;
	@Getter @Setter private String[] aPICallName_array ;
	@Getter @Setter private QueryEnum aPICallResult_mode ;
	@Getter @Setter private String[] aPICallResult_array ;
	@Getter @Setter private QueryEnum errorDetails_mode ;
	@Getter @Setter private String[] errorDetails_array ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}