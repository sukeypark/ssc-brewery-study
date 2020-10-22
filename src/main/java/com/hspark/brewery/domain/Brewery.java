package com.hspark.brewery.domain;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Brewery extends BaseEntity {
    @Builder
    public Brewery(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String breweryName) {
        super(id, version, createdDate, lastModifiedDate);
        this.breweryName = breweryName;
    }

    private String breweryName;
}
