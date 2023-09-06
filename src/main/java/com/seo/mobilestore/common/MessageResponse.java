package com.seo.mobilestore.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class MessageResponse {
    private int httpCode;
    private String message;
    private String path;
}
