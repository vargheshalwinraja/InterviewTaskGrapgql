package com.allwin.graphql.repository;

import com.allwin.graphql.dto.SellerFilter;
import com.allwin.graphql.model.SellerInfos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepositoryCustom {

    Page<SellerInfos> findByFilterProvided(SellerFilter filter, Pageable pageable);
}
