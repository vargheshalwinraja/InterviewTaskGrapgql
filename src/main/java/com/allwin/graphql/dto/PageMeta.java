package com.allwin.graphql.dto;

import lombok.Data;

@Data
public class PageMeta {

    private Long totalRecord;

    public PageMeta(Long totalRecord) {
        this.totalRecord = totalRecord;
    }
}
