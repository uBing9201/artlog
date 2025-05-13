package com.playdata.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContentDto{

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
}