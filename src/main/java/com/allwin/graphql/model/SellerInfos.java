package com.allwin.graphql.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "seller_infos")
@Data
public class SellerInfos {
    @Id
    @Column(name = "id", nullable = false)
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "name", length = 2048)
    private String name;

    @Column(name = "url", length = 2048)
    private String url;

    @Column(name = "country")
    private String country;

    @Column(name = "external_id")
    private String externalId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "marketplace_id", foreignKey = @ForeignKey(name = "fk_marketplace_id"))
    private MarketPlaces marketplaceId;

}
