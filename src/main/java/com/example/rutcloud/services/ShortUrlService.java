package com.example.rutcloud.services;

public interface ShortUrlService {
    void addShortUrlToRedis(String url);
    String getByShortUrl(String shortUrl);
    void deleteByShortUrl(String shortUrl);
    void deleteByUrl(String url);

}
