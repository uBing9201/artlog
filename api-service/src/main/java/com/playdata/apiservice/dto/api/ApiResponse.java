package com.playdata.apiservice.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;

        @Data
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        @Data
        public static class Body {
            private Items items;
            private String numOfRows;
            private String pageNo;
            private String totalCount;

            @Data
            public static class Items {
                private List<ContentDto> item;
            }
        }
    }
}
