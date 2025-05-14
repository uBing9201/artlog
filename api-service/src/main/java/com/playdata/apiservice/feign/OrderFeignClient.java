package com.playdata.apiservice.feign;

import com.playdata.apiservice.dto.common.OrderInfoResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "order-service")
public interface OrderFeignClient {
    @GetMapping("/order/findByAllFeign/{userKey}")
    ResponseEntity<List<OrderInfoResDto>> findByAllFeign(@PathVariable Long userKey);
}
