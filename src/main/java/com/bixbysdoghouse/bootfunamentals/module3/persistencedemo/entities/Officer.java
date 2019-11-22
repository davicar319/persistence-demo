package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "officers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    @Column(name="first_name", nullable = false)
    private String first;
    @Column(name = "last_name", nullable = false)
    private String last;

    public Officer(@NonNull Rank rank, @NonNull String first, @NonNull String last) {
        this.rank = rank;
        this.first = first;
        this.last = last;
    }
}
