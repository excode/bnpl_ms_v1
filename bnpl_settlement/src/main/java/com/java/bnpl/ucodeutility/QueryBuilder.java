package com.java.bnpl.ucodeutility;
import java.time.LocalDate;
import java.util.Collections;

public class QueryBuilder {
   private String sql ;
   private String leftSide;
   private String rightSide;
   private String Value;

   
public QueryBuilder(
   String colName, 
   String value,  
   QueryEnum queryEnum) {
    this.Value = value;
    this.rightSide="";
    this.leftSide="";
       
        if(value!=null && !value.isEmpty()){
        
            if(queryEnum!=null && queryEnum.toString()!=""){ 
                if(queryEnum.equals(QueryEnum.startsWith)){
    
                    this.sql=String.format("and LOWER(s.%s) LIKE   LOWER(:%s)", colName, colName);
                    this.leftSide="";
                    this.rightSide = "%";
                    
                }else if(queryEnum.equals(QueryEnum.endsWith)){
                    this.sql=String.format("and LOWER(s.%s) LIKE  LOWER(:%s)", colName, colName);
                    this.leftSide="%";
                    this.rightSide = "";
                }else if(queryEnum.equals(QueryEnum.contains)){
                    this.sql=String.format("and LOWER(s.%s) LIKE  LOWER(:%s)", colName, colName);
                    this.leftSide="%";
                    this.rightSide = "%";
                }else if(queryEnum.equals(QueryEnum.equals)){
                    this.sql=String.format("and LOWER(s.%s) =  LOWER(:%s)", colName, colName);
                    this.leftSide="";
                    this.rightSide = "";
                }else if(queryEnum.equals(QueryEnum.notContains)){
                    this.sql=String.format("and LOWER(s.%s) NOT LIKE  LOWER(:%s)", colName, colName);
                    this.leftSide="%";
                    this.rightSide = "%";
                }   
            }else{
                this.sql=String.format("and LOWER(s.%s) LIKE  LOWER(:%s)", colName, colName);
                this.leftSide="%";
                this.rightSide = "%";
            }
        }
    }
  public QueryBuilder(
   String colName, 
   LocalDate value, 
   QueryEnum queryEnum) {

    this.rightSide="";
    this.leftSide="";
     
        if(value!=null){
            this.leftSide="";
            this.rightSide = "";
            if(queryEnum!=null && queryEnum.toString()!=""){ 
                if(queryEnum.equals(QueryEnum.equals)){
                    this.sql=String.format("and s.%s =  :%s", colName, colName);
                }else if(queryEnum.equals(QueryEnum.gt)){
                    this.sql=String.format("and s.%s >  :%s", colName, colName);
                   
                }else if(queryEnum.equals(QueryEnum.gte)){
                    this.sql=String.format("and s.%s >=  :%s", colName, colName);
                    
                }else if(queryEnum.equals(QueryEnum.lt)){
                    this.sql=String.format("and s.%s <  :%s", colName, colName);
                   
                }else if(queryEnum.equals(QueryEnum.lte)){
                    this.sql=String.format("and s.%s <=  :%s", colName, colName);
                    
                }else if(queryEnum.equals(QueryEnum.notEquals) ||queryEnum.equals(QueryEnum.ne)){
                    this.sql=String.format("and s.%s !=  :%s", colName, colName);
                   
                }   
            }else{
                this.sql=String.format("and s.%s =  :%s", colName, colName);
                
            }
        }
    
    }
    public QueryBuilder(
   String colName, 
   Number value, 
   QueryEnum queryEnum) {

    this.rightSide="";
    this.leftSide="";
     
        if(value!=null){
            this.leftSide="";
            this.rightSide = "";
            if(queryEnum!=null && queryEnum.toString()!=""){ 
                if(queryEnum.equals(QueryEnum.equals)){
                    this.sql=String.format("and s.%s =  :%s", colName, colName);
                }else if(queryEnum.equals(QueryEnum.gt)){
                    this.sql=String.format("and s.%s >  :%s", colName, colName);
                   
                }else if(queryEnum.equals(QueryEnum.gte)){
                    this.sql=String.format("and s.%s >=  :%s", colName, colName);
                    
                }else if(queryEnum.equals(QueryEnum.lt)){
                    this.sql=String.format("and s.%s <  :%s", colName, colName);
                   
                }else if(queryEnum.equals(QueryEnum.lte)){
                    this.sql=String.format("and s.%s <=  :%s", colName, colName);
                    
                }else if(queryEnum.equals(QueryEnum.notEquals) ||queryEnum.equals(QueryEnum.ne)){
                    this.sql=String.format("and s.%s !=  :%s", colName, colName);
                   
                }   
            }else{
                this.sql=String.format("and s.%s =  :%s", colName, colName);    
            }
        }
    
    }
public QueryBuilder(
   String colName, 
   String[] value) {
    
    this.rightSide="";
    this.leftSide="";
   
        if(value!=null && value.length>0){
          
            String format = String.join(", ", Collections.nCopies(value.length, "'%s'"));
            String formattedString = String.format(format, (Object[]) value);
            this.sql=String.format("and s.%s IN (%s)", colName, formattedString);
        }       
  }
  public QueryBuilder(
   String colName, 
   LocalDate[] value) {

    this.rightSide="";
    this.leftSide="";
     
        if(value!=null && value.length >0){
         
            
                if( value.length==2){
                    this.sql=String.format("and s.%s BETWEEN   :%s AND :%s  ", colName, colName+"1", colName+"2");
                } 
                /*
                else{
            
           
                String format = String.join(", ", Collections.nCopies(value.length, "'%s'"));
                String formattedString = String.format(format, (Object[]) value);
                this.sql=String.format("and s.%s IN (%s)", colName, formattedString);
                
                }
                 */
        }
   
  }
  public QueryBuilder(
   String colName, 
   Number[] value) {

    this.rightSide="";
    this.leftSide="";
   
        if(value!=null && value.length >0){
         
           
            if(value.length==2){
                    this.sql=String.format("and s.%s BETWEEN   :%s AND :%s  ", colName, colName+"1", colName+"2");
                
            }else{
                String format = String.join(", ", Collections.nCopies(value.length, "%s"));
                String formattedString = String.format(format, (Object[]) value);
                this.sql=String.format("and s.%s IN (%s)", colName, formattedString);
                
            }
        
        }
  }
  public QueryBuilder( // KEYWORD SEARCH ON MULTIPLE COLUMN
   String[] colName, 
   String value) {

    this.rightSide="%";
    this.leftSide="%";
     
        if(value!=null && value.length() >0){
        this.Value= value;
        String format = String.join(" OR ", Collections.nCopies(colName.length, "LOWER(s.%s) LIKE :search"));
        String formattedString = String.format(format, (Object[]) colName);
        this.sql=String.format("and (%s)",formattedString);
        }
  }
  public QueryBuilder( // KEYWORD SEARCH ON MULTIPLE COLUMN
   String colName, 
   Boolean value) {

    this.rightSide="%";
    this.leftSide="%";

        if(value!=null){
      
            this.sql=String.format("and s.%s =  :%s", colName, colName);
        }
  }
    public String getLeftSide() {
        return leftSide;
       }
    public String getRightSide() {
        return rightSide;
    }

    public String getSql() {
        if(sql==null){
        return "";
        }else{
            return sql.isEmpty()||sql.isBlank()?"":" "+sql;
        }
    }
    public String getValue() {
        if(sql==null){
            return "";
        }else{
            if(Value!=null){
                String lowerCaseVal = Value.toLowerCase();
            return leftSide+lowerCaseVal+rightSide;
            }else{
                return "";
            }
        }
        
    }
   
 


   



 
  

    
    
 
}
