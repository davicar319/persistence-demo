package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Officer {
    private Integer id;
    private Rank rank;
    private String first;
    private String last;

    public Officer(@NonNull Rank rank, @NonNull String first, @NonNull String last) {
        this.rank = rank;
        this.first = first;
        this.last = last;
    }
}
