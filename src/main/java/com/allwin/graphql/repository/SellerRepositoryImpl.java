package com.allwin.graphql.repository;

import com.allwin.graphql.dto.SellerFilter;
import com.allwin.graphql.model.SellerInfos;
import com.allwin.graphql.model.Sellers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SellerRepositoryImpl implements SellerRepositoryCustom {

    private final EntityManager entityManager;

    public SellerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Helper method to count sellers based on filter criteria
    private long countSellers(SellerFilter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Sellers> root = query.from(Sellers.class);

        query.select(builder.count(root));
        query.where(getPredicate(filter, builder, root));

        return entityManager.createQuery(query).getSingleResult();
    }

    // Helper method to build the predicate for filtering
    private Predicate getPredicate(SellerFilter filter, CriteriaBuilder builder, Root<Sellers> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(filter.getSearchByName())) {
            // Add filtering by seller name
            predicates.add(builder.like(root.join("sellerInfoId").get("name"), "%" + filter.getSearchByName() + "%"));
        }

        if (filter.getProducerIds() != null && !filter.getProducerIds().isEmpty()) {
            // Add filtering by producer IDs
            predicates.add(root.join("producerId").get("id").in(filter.getProducerIds()));
        }

        if (filter.getMarketplaceIds() != null && !filter.getMarketplaceIds().isEmpty()) {
            // Add filtering by marketplace IDs
            predicates.add(root.join("sellerInfoId").join("marketplaceId").get("id").in(filter.getMarketplaceIds()));
        }

        // Add more filtering criteria as needed

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    // Override method to fetch sellers based on filter and pagination
    @Override
    public Page<SellerInfos> findByFilterProvided(SellerFilter filter, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SellerInfos> query = builder.createQuery(SellerInfos.class);
        Root<Sellers> root = query.from(Sellers.class);

        query.select(root.get("sellerInfoId")).distinct(true);
        query.where(getPredicate(filter, builder, root));

        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            String property = sortOrder.getProperty();
            if (property != null) {
                switch (property) {
                    case "sellerInfoId.name":
                        if (sortOrder.isAscending()) {
                            orders.add(builder.asc(root.join("sellerInfoId").get("name")));
                        } else {
                            orders.add(builder.desc(root.join("sellerInfoId").get("name")));
                        }
                        break;
                    case "sellerInfoId.externalId":
                        if (sortOrder.isAscending()) {
                            orders.add(builder.asc(root.join("sellerInfoId").get("externalId")));
                        } else {
                            orders.add(builder.desc(root.join("sellerInfoId").get("externalId")));
                        }
                        break;
                    case "sellerInfoId.marketplaceId.id":
                        if (sortOrder.isAscending()) {
                            orders.add(builder.asc(root.join("sellerInfoId").join("marketplaceId").get("id")));
                        } else {
                            orders.add(builder.desc(root.join("sellerInfoId").join("marketplaceId").get("id")));
                        }
                        break;
                    // Add more sorting options as needed
                }
            }
        }

        // Apply the sorting to the query
        query.orderBy(orders);

        // Execute the query and fetch the result
        List<SellerInfos> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Count total sellers based on the filter criteria
        long total = countSellers(filter);

        // Create a Page object with the result list, pagination, and total count
        return new PageImpl<>(resultList, pageable, total);
    }
}
