package com.playdata.apiservice.controller;

import com.playdata.apiservice.dto.ContentDto;
import com.playdata.apiservice.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final ApiService apiService;

    @GetMapping("/first")
    public String first() {
        List<ContentDto> first = apiService.first();
        log.error(first.toString());
        return first.toString();
    }
}
