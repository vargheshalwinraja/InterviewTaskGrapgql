package com.allwin.graphql.resolver;

import com.allwin.graphql.dto.*;
import com.allwin.graphql.model.SellerInfos;
import com.allwin.graphql.model.SellerSortBy;
import com.allwin.graphql.model.Sellers;
import com.allwin.graphql.repository.SellerRepositoryCustom;
import com.allwin.graphql.repository.SellersRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class Query implements GraphQLQueryResolver {

    private final SellersRepository sellersRepository;

    private final SellerRepositoryCustom sellerRepositoryCustom;

    public Query(SellersRepository sellersRepository, SellerRepositoryCustom sellerRepositoryCustom) {
        this.sellersRepository = sellersRepository;
        this.sellerRepositoryCustom = sellerRepositoryCustom;
    }

    /**
     * GraphQL resolver for the 'sellers' query.
     *
     * @param filter    Filter criteria for sellers.
     * @param pageInput Page input configuration.
     * @param sortBy    Sorting criteria for sellers.
     * @return A pageable response containing seller data.
     */
    public SellerPageableResponse sellers(SellerFilter filter, PageInput pageInput, SellerSortBy sortBy) {
        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(pageInput.getPage() - 1, pageInput.getSize(), getSort(sortBy));

        // Fetch sellers data based on provided filter and pagination
        Page<SellerInfos> sellersPage = sellerRepositoryCustom.findByFilterProvided(filter, pageable);

        // Create the GraphQL response object
        SellerPageableResponse response = new SellerPageableResponse();
        response.setData(mapToSellerData(sellersPage.getContent()));
        response.setMeta(new PageMeta(sellersPage.getTotalElements()));

        return response;
    }

    /**
     * Maps a list of SellerInfos to SellerData objects.
     *
     * @param sellers List of SellerInfos.
     * @return List of SellerData objects.
     */
    public List<SellerData> mapToSellerData(List<SellerInfos> sellers) {
        List<SellerData> sellerDataList = new ArrayList<>();

        for (SellerInfos seller : sellers) {
            SellerData sellerData = new SellerData();
            List<ProducerSellerState> producerSellerStates = new ArrayList<>();

            List<Sellers> sellersDatas = sellersRepository.findBySellerInfoId_Id(seller.getId());

            for (Sellers sellersData : sellersDatas) {
                producerSellerStates.add(new ProducerSellerState(sellersData.getProducerId().getId(), sellersData.getProducerId().getName(), sellersData.getState(), sellersData.getSellerInfoId().getId()));
            }

            sellerData.setSellerName(seller.getName());
            sellerData.setExternalId(seller.getExternalId());
            sellerData.setMarketplaceId(seller.getMarketplaceId().getId());
            sellerData.setProducerSellerStates(producerSellerStates);

            sellerDataList.add(sellerData);
        }
        return sellerDataList;
    }

    /**
     * Gets the sorting criteria based on SellerSortBy enum.
     *
     * @param sortBy Sorting criteria.
     * @return Sort object for sorting sellers.
     */
    public Sort getSort(SellerSortBy sortBy) {
        switch (sortBy) {
            case NAME_ASC:
                return Sort.by("sellerInfoId.name").ascending();
            case NAME_DESC:
                return Sort.by("sellerInfoId.name").descending();
            case MARKETPLACE_ID_ASC:
                return Sort.by("sellerInfoId.marketplaceId.id").ascending();
            case MARKETPLACE_ID_DESC:
                return Sort.by("sellerInfoId.marketplaceId.id").descending();
            case SELLER_INFO_EXTERNAL_ID_ASC:
                return Sort.by("sellerInfoId.externalId").ascending();
            case SELLER_INFO_EXTERNAL_ID_DESC:
                return Sort.by("sellerInfoId.externalId").descending();
            default:
                return Sort.by("sellerInfoId.name").ascending();
        }
    }
}
