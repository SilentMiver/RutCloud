package com.example.rutcloud.services;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.nio.charset.Charset;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {
    Jedis jedis = new Jedis("localhost", 6379);

    @Override
    public void addShortUrlToRedis(String url) {
        try {
            String id = Hashing.murmur3_32().hashString(url, Charset.defaultCharset()).toString();
            jedis.set(id, url);
            System.out.println("Запись добавлена");
        } finally {
            jedis.close();
        }
    }

    @Override
    public String getByShortUrl(String shortUrl) {
            String value = jedis.get(shortUrl);
            System.out.println("Значение по ключу " + shortUrl + ": " + value);
            jedis.close();

            return value;


    }
}
