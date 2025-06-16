package com.playdata.apiservice.controller;

import com.playdata.apiservice.dto.api.ContentDto;
import com.playdata.apiservice.dto.api.ContentResDto;
import com.playdata.apiservice.dto.api.ContentUserResDto;
import com.playdata.apiservice.dto.common.CommonResDto;
import com.playdata.apiservice.exception.PublicApiException;
import com.playdata.apiservice.service.ApiCacheService;
import com.playdata.apiservice.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final ApiService apiService;
    private final ApiCacheService apiCacheService;

    @GetMapping("/select")
    public ResponseEntity<?> select(@RequestParam Long numOfRows, @RequestParam Long pageNo) throws IOException, PublicApiException {
        List<ContentResDto> resDto = apiCacheService.getData(numOfRows, pageNo);
        log.info(resDto.toString());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "데이터 불러오기에 성공하였습니다.", resDto));
    }

    @GetMapping("/selectByUserKey/{userKey}")
    public ResponseEntity<?> selectByUserKey(@PathVariable Long userKey) throws IOException, PublicApiException {
        List<ContentUserResDto> resDto = apiCacheService.getDataByUserKey(userKey);
        log.info(resDto.toString());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "데이터 불러오기에 성공하였습니다.", resDto));
    }

    @GetMapping("/selectByUserKeyPaging")
    public ResponseEntity<?> selectByUserKeyPaging(@RequestParam Long userKey, @RequestParam Long pageNo, @RequestParam Long numOfRows) throws IOException, PublicApiException {
        List<ContentUserResDto> resDto = null;
        try {
            resDto = apiService.getDataByUserKeyPaging(userKey, numOfRows, pageNo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "모든 데이터를 조회하였습니다.", new ArrayList<>()));
        }
        log.info(resDto.toString());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "데이터 불러오기에 성공하였습니다.", resDto));
    }
}

