package com.example.webflux.practice.webflux.common.domain;

import lombok.Data;

@Data
public class Article {
    private final String id;
    private final String title;
    private final String content;
}
