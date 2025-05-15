package com.playdata.apiservice.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Data
@Slf4j
public class ContentDto {

    @JsonProperty("TITLE")
    private String title;

    @JsonProperty("CNTC_INSTT_NM")
    private String cntcInsttNm;

    @JsonProperty("COLLECTED_DATE")
    private String collectedDate;

    @JsonProperty("ISSUED_DATE")
    private String issuedDate;

    @JsonProperty("DESCRIPTION")
    private String description;

    @JsonProperty("IMAGE_OBJECT")
    private String imageObject;

    @JsonProperty("LOCAL_ID")
    private String localId;

    @JsonProperty("URL")
    private String url;

    @JsonProperty("VIEW_COUNT")
    private String viewCount;

    @JsonProperty("SUB_DESCRIPTION")
    private String subDescription;

    @JsonProperty("SPATIAL_COVERAGE")
    private String spatialCoverage;

    @JsonProperty("EVENT_SITE")
    private String eventSite;

    @JsonProperty("GENRE")
    private String genre;

    @JsonProperty("DURATION")
    private String duration;

    @JsonProperty("NUMBER_PAGES")
    private String numberPages;

    @JsonProperty("TABLE_OF_CONTENTS")
    private String tableOfContents;

    @JsonProperty("AUTHOR")
    private String author;

    @JsonProperty("CONTACT_POINT")
    private String contactPoint;

    @JsonProperty("ACTOR")
    private String actor;

    @JsonProperty("CONTRIBUTOR")
    private String contributor;

    @JsonProperty("AUDIENCE")
    private String audience;

    @JsonProperty("CHARGE")
    private String charge;

    @JsonProperty("PERIOD")
    private String period;

    @JsonProperty("EVENT_PERIOD")
    private String eventPeriod;


    public ContentResDto toResDto() {
        // 1. localId 검증
        if (isLocalIdValid()) return null;

        // 2. title 검증
        if (isTitleValid()) return null;

        // 3. eventSite 검증
        if (isEventSiteValid()) return null;

        // 4. url 검증
        if (isUrlValid()) return null;

        // 5. charge 검증
        if (isChargeValid()) return null;

        // 6. imageObject 검증
        if (isImageObjectValid()) return null;

        // 7. period 검증
        long periodL = 0L;
        String startDateStr, endDateStr;
        if (this.period == null || this.period.isEmpty()) {
            log.error("period가 유효하지 않습니다. : " + this.period);
            return null;
        } else {
            try {
                String[] dates;
                if (!this.period.contains("~")) {
                    dates = this.period.split(" ");
                } else {
                    dates = this.period.split("~");
                }
                // 1. 시작일과 종료일 분리
                startDateStr = dates[0].trim();
                endDateStr = dates[dates.length - 1].trim();

                // 2. 문자열을 LocalDate로 파싱
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                // 3. 일수 계산 (양 끝 포함하려면 +1)
                periodL = ChronoUnit.DAYS.between(startDate, endDate) + 1;

                log.info("startDate:" + startDateStr + " endDate:" + endDateStr);
            } catch (Exception e) {
                log.error("period 파싱에 실패하였습니다. : " + this.period);
                return null;
            }
        }

        return ContentResDto.builder()
                .contentId(this.localId)
                .contentTitle(this.title)
                .contentVenue(this.eventSite)
                .contentUrl(this.url)
                .contentCharge(Long.parseLong(this.charge))
                .contentThumbnail(this.imageObject)
                .contentPeriod(periodL)
                .startDate(startDateStr)
                .endDate(endDateStr)
                .build();
    }

    private boolean isImageObjectValid() {
        if (this.imageObject == null || this.imageObject.isEmpty() || !this.imageObject.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
            log.error("imageObject가 유효하지 않습니다.");
            return true;
        }
        return false;
    }

    private boolean isChargeValid() {
        if (this.charge == null || this.charge.isEmpty() || this.charge.equals("-") || this.charge.equals("무료") || this.charge.equals("미정")) {
            this.charge = "0";
        } else if (!this.charge.matches("[0-9]+")) {
            if (this.charge.contains("원")) {
                StringBuilder chargeSb = new StringBuilder();
                boolean flag = false;
                for (char c : this.charge.toCharArray()) {
                    if (c >= '0' && c <= '9') {
                        flag = true;
                        chargeSb.append(c);
                    } else if (c != ',' && flag) {
                        break;
                    }
                }
                this.charge = chargeSb.toString();
            } else {
                log.error("charge가 유효하지 않습니다. : " + this.charge);
                return true;
            }
        }
        return false;
    }

    private boolean isUrlValid() {
        if (this.url == null || this.url.isEmpty() || !this.url.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
            log.error("url이 유효하지 않습니다.");
            return true;
        }
        return false;
    }

    private boolean isEventSiteValid() {
        if (this.eventSite == null || this.eventSite.isEmpty() || this.eventSite.equals("해외")) {
            log.error("Event Site가 유효하지 않습니다. : " + this.eventSite);
            return true;
        }
        return false;
    }

    private boolean isTitleValid() {
        if (this.title == null || this.title.isEmpty() || this.title.equals("테스트") || this.title.equals("test")) {
            log.error("Title이 유효하지 않습니다. :" + this.title);
            return true;
        }
        return false;
    }

    private boolean isLocalIdValid() {
        // 1. localId 검증
        if (this.localId == null || this.localId.isEmpty()) {
            log.error("Local ID가 유효하지 않습니다. : " + this.localId);
            return true;
        }
        return false;
    }

    public boolean isValid() {
        return !isLocalIdValid() && !isTitleValid() && !isEventSiteValid() && !isUrlValid() && !isImageObjectValid() && !isChargeValid();
    }


}