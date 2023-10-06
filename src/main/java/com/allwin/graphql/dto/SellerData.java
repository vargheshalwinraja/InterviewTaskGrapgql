package com.allwin.graphql.dto;

import lombok.Data;

import java.util.List;

@Data
public class SellerData {

    private String sellerName;

    private List<ProducerSellerState> producerSellerStates;

    private String externalId;

    private String marketplaceId;
}
