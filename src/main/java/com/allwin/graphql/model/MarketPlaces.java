package com.allwin.graphql.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "marketplaces")
@Data
public class MarketPlaces {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "description")
    private String description;
}
