package com.allwin.graphql.dto;

import com.allwin.graphql.model.SellerState;
import lombok.Data;

import java.util.UUID;

@Data
public class ProducerSellerState {

    private UUID producerId;

    private String producerName;

    private SellerState sellerState;

    private UUID sellerId;

    public ProducerSellerState(UUID producerId, String producerName, SellerState sellerState, UUID sellerId) {
        this.producerId = producerId;
        this.producerName = producerName;
        this.sellerState = sellerState;
        this.sellerId = sellerId;
    }
}
