package com.java.bnpl.transaction;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class TransactionQuery  extends Transaction{
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum customerId_mode ;
	@Getter @Setter private Number[] customerId_array ;
	@Getter @Setter private QueryEnum transactionDate_mode ;
	@Getter @Setter private QueryEnum transactionAmount_mode ;
	@Getter @Setter private Number[] transactionAmount_array ;
	@Getter @Setter private QueryEnum status_mode ;
	@Getter @Setter private Number[] status_array ;
	@Getter @Setter private QueryEnum paymentMethod_mode ;
	@Getter @Setter private String[] paymentMethod_array ;
	@Getter @Setter private QueryEnum nextPaymentDate_mode ;
	@Getter @Setter private QueryEnum partnerId_mode ;
	@Getter @Setter private Number[] partnerId_array ;
	@Getter @Setter private QueryEnum plan_mode ;
	@Getter @Setter private Number[] plan_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}