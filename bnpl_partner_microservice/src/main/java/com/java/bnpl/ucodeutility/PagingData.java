package com.java.bnpl.ucodeutility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingData<T> {
        Long count;
        T docs;
        Long page;
        int limit;
        
}
