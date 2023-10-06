package com.allwin.graphql.dto;

import lombok.Data;

import java.util.List;

@Data
public class SellerPageableResponse {
    private PageMeta meta;

    private List<SellerData> data;

}
