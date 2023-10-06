package com.allwin.graphql.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@Data
public class Sellers {
    @Id
    @Column(name = "id", nullable = false)
    @Type(type="uuid-char")
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", foreignKey = @ForeignKey(name = "fk_producer_id"))
    private Producers producerId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_info_id", foreignKey = @ForeignKey(name = "fk_seller_info_id"))
    private SellerInfos sellerInfoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SellerState state;

}
