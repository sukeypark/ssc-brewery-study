package com.hspark.brewery.domain;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;

public class Brewery extends BaseEntity {
    @Builder
    public Brewery(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String breweryName) {
        super(id, version, createdDate, lastModifiedDate);
        this.breweryName = breweryName;
    }

    private String breweryName;
}
