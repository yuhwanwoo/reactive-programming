package com.example.webflux.practice.webflux.common.domain;

import lombok.Data;

@Data
public class Image {
    private final String id;
    private final String name;
    private final String url;
}
