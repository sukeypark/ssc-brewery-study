package com.hspark.brewery.web.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BeerDto extends BaseItem {

    @Builder
    public BeerDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, String beerName,
                   BeerStyleEnum beerStyle, String upc, Integer quantityOnHand, BigDecimal price) {
        super(id, version, createdDate, lastModifiedDate);
        this.beerName = beerName;
        this.beerStyle = beerStyle;
        this.upc = upc;
        this.quantityOnHand = quantityOnHand;
        this.price = price;
    }
    
    private String beerName;
    private BeerStyleEnum beerStyle;
    private String upc;
    private Integer quantityOnHand;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
}
