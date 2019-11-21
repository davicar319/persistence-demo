package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Officer {
    private Integer id;
    private Rank rank;
    private String first;
    private String last;
}
