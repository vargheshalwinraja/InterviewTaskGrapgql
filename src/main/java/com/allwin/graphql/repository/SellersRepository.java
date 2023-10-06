package com.allwin.graphql.repository;

import com.allwin.graphql.model.Sellers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SellersRepository extends JpaRepository<Sellers, UUID> {

    List<Sellers> findBySellerInfoId_Id(UUID id);
}