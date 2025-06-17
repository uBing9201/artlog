package com.playdata.apiservice.service;

import com.playdata.apiservice.dto.api.ContentUserResDto;
import com.playdata.apiservice.dto.common.OrderInfoResDto;
import com.playdata.apiservice.exception.PublicApiException;
import com.playdata.apiservice.feign.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final ApiCacheService apiCacheService;
    private final OrderFeignClient orderFeignClient;

    public List<ContentUserResDto> getDataByUserKeyPaging(Long userKey, Long numOfRows, Long pageNo) throws IOException, PublicApiException, IllegalArgumentException {
        List<OrderInfoResDto> orderList = orderFeignClient.findByAllFeign(userKey).getBody();
        List<OrderInfoResDto> orderListCache = apiCacheService.getOrderList(userKey);

        if(orderList == null) {
            throw new PublicApiException("주문 내역을 찾을 수 없습니다.");
        }

        if(orderList.size() != orderListCache.size()){
            apiCacheService.deleteUserCache(userKey);
        }

        List<ContentUserResDto> dataByUserKey = apiCacheService.getDataByUserKey(userKey);
        if(dataByUserKey == null || dataByUserKey.isEmpty()) {
            return new ArrayList<>();
        }
        log.info(Long.toString((pageNo - 1) * numOfRows));

        List<ContentUserResDto> list = dataByUserKey.stream()
                .skip((pageNo-1) * numOfRows)
                .limit(numOfRows)
                .toList();

        log.info(list.toString());
        return list;
    }

}
