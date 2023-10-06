package com.allwin.graphql.dto;

import lombok.Data;

@Data
public class PageInput {
    private int page;
    private int size;

    public PageInput(int page, int size) {
        this.page = page;
        this.size = size;
    }

}

