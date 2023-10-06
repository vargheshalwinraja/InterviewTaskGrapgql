package com.allwin.graphql.resolver;

import com.allwin.graphql.dto.PageInput;
import com.allwin.graphql.dto.SellerData;
import com.allwin.graphql.dto.SellerFilter;
import com.allwin.graphql.dto.SellerPageableResponse;
import com.allwin.graphql.model.MarketPlaces;
import com.allwin.graphql.model.SellerInfos;
import com.allwin.graphql.model.SellerSortBy;
import com.allwin.graphql.repository.SellerRepositoryCustom;
import com.allwin.graphql.repository.SellersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration
class QueryTest
{
    @Mock
    private SellersRepository sellersRepository;

    @Mock
    private SellerRepositoryCustom sellerRepositoryCustom;

    @InjectMocks
    private Query query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        query = new Query(sellersRepository, sellerRepositoryCustom);
    }

    // Test the sellers query method
    @Test
    void testSellersQuery() {
        // Mock input parameters
        SellerFilter filter = new SellerFilter();
        PageInput pageInput = new PageInput(1, 10);
        SellerSortBy sortBy = SellerSortBy.NAME_ASC;

        // Mock the behavior of sellerRepositoryCustom
        Page<SellerInfos> mockSellersPage = new PageImpl<>(Collections.emptyList());
        when(sellerRepositoryCustom.findByFilterProvided(any(SellerFilter.class), any(Pageable.class)))
                .thenReturn(mockSellersPage);

        // Execute the GraphQL query
        SellerPageableResponse result = query.sellers(filter, pageInput, sortBy);

        // Assertions
        assertEquals(0, result.getData().size()); // Replace with your expected result and assertions
    }

    // Test the mapToSellerData method
    @Test
    void testMapToSellerData() {
        // Create a list of mock SellerInfos
        List<SellerInfos> mockSellerInfosList = new ArrayList<>();
        SellerInfos seller1 = new SellerInfos();
        seller1.setName("Seller 1");
        seller1.setExternalId("External 1");
        seller1.setMarketplaceId(new MarketPlaces());
        mockSellerInfosList.add(seller1);

        // Mock the behavior of sellersRepository
        when(sellersRepository.findBySellerInfoId_Id(any())).thenReturn(Collections.emptyList());

        // Execute the mapToSellerData method
        List<SellerData> result = query.mapToSellerData(mockSellerInfosList);

        // Assertions
        assertEquals(1, result.size()); // Replace with your expected result and assertions
    }

    // Test the getSort method
    @Test
    void testGetSort() {
        // Test sorting by NAME_ASC
        Sort sortByNameAsc = query.getSort(SellerSortBy.NAME_ASC);
        assertEquals(Sort.Direction.ASC, sortByNameAsc.getOrderFor("sellerInfoId.name").getDirection());

        // Test sorting by NAME_DESC
        Sort sortByNameDesc = query.getSort(SellerSortBy.NAME_DESC);
        assertEquals(Sort.Direction.DESC, sortByNameDesc.getOrderFor("sellerInfoId.name").getDirection());

        // Test sorting by MARKETPLACE_ID_ASC
        Sort sortByMarketplaceIdAsc = query.getSort(SellerSortBy.MARKETPLACE_ID_ASC);
        assertEquals(Sort.Direction.ASC, sortByMarketplaceIdAsc.getOrderFor("sellerInfoId.marketplaceId.id").getDirection());

        // Test sorting by MARKETPLACE_ID_DESC
        Sort sortByMarketplaceIdDesc = query.getSort(SellerSortBy.MARKETPLACE_ID_DESC);
        assertEquals(Sort.Direction.DESC, sortByMarketplaceIdDesc.getOrderFor("sellerInfoId.marketplaceId.id").getDirection());

        // Test sorting by SELLER_INFO_EXTERNAL_ID_ASC
        Sort sortByExternalIdAsc = query.getSort(SellerSortBy.SELLER_INFO_EXTERNAL_ID_ASC);
        assertEquals(Sort.Direction.ASC, sortByExternalIdAsc.getOrderFor("sellerInfoId.externalId").getDirection());

        // Test sorting by SELLER_INFO_EXTERNAL_ID_DESC
        Sort sortByExternalIdDesc = query.getSort(SellerSortBy.SELLER_INFO_EXTERNAL_ID_DESC);
        assertEquals(Sort.Direction.DESC, sortByExternalIdDesc.getOrderFor("sellerInfoId.externalId").getDirection());
    }
}
