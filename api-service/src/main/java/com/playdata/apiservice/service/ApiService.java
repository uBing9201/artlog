package com.playdata.apiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.apiservice.dto.api.ApiResponse;
import com.playdata.apiservice.dto.api.ContentDto;
import com.playdata.apiservice.dto.api.ContentResDto;
import com.playdata.apiservice.dto.api.ContentUserResDto;
import com.playdata.apiservice.dto.common.OrderInfoResDto;
import com.playdata.apiservice.exception.PublicApiException;
import com.playdata.apiservice.feign.OrderFeignClient;
import com.playdata.apiservice.feign.ReviewFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final ApiCacheService apiCacheService;

    public List<ContentUserResDto> getDataByUserKeyPaging(Long userKey, Long numOfRows, Long pageNo) throws IOException, PublicApiException, IllegalArgumentException {
        List<ContentUserResDto> dataByUserKey = apiCacheService.getDataByUserKey(userKey);
        log.info(dataByUserKey.toString());

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
