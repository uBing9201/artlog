package com.playdata.apiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.TimeZone;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiScheduleService {
    private final ApiCacheService apiCacheService;
    private final ApiDataService apiDataService;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void checkApiSchedule(){
        try {
            apiCacheService.deleteOrderCache();
            apiDataService.getApiData(1);
            log.info("최신 정보를 불러오는 데 성공하였습니다.");
        } catch (IOException e) {
            log.info("최신 정보를 불러오는 데 실패하였습니다.");
            return;
        }
    }
}
