package com.playdata.apiservice.controller;

import com.playdata.apiservice.dto.api.ContentDto;
import com.playdata.apiservice.dto.api.ContentResDto;
import com.playdata.apiservice.dto.api.ContentUserResDto;
import com.playdata.apiservice.dto.common.CommonResDto;
import com.playdata.apiservice.exception.PublicApiException;
import com.playdata.apiservice.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final ApiService apiService;

    @GetMapping("/get/{number}")
    public ResponseEntity<?> get(@PathVariable Long number) throws IOException, PublicApiException {
        List<ContentResDto> resDto = apiService.getData(number, 1L);
        log.info(resDto.toString());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "데이터 불러오기에 성공하였습니다.", resDto));
    }

    @GetMapping("/select")
    public ResponseEntity<?> select(@RequestParam Long numOfRows, @RequestParam Long pageNo) throws IOException, PublicApiException {
        List<ContentResDto> resDto = apiService.getData(numOfRows, pageNo);
        log.info(resDto.toString());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "데이터 불러오기에 성공하였습니다.", resDto));
    }

    @GetMapping("/selectByUserKey/{userKey}")
    public ResponseEntity<?> selectByUserKey(@PathVariable Long userKey) throws IOException, PublicApiException {
        List<ContentUserResDto> resDto = apiService.getDataByUserKey(userKey);
        log.info(resDto.toString());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "데이터 불러오기에 성공하였습니다.", resDto));
    }
    //test
}

