package com.java.bnpl.accesspermission;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.java.bnpl.ucodeutility.QueryEnum;
import com.java.bnpl.ucodeutility.SortEnum;
public class AccessPermissionQuery  extends AccessPermission{
	@Getter @Setter private QueryEnum endPointname_mode ;
	@Getter @Setter private String[] endPointname_array ;
	@Getter @Setter private QueryEnum add_mode ;
	@Getter @Setter private QueryEnum edit_mode ;
	@Getter @Setter private QueryEnum read_mode ;
	@Getter @Setter private QueryEnum delete_mode ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private Number[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum username_mode ;
	@Getter @Setter private String[] username_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}