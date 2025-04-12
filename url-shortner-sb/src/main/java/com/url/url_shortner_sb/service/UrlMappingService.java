package com.url.url_shortner_sb.service;

import com.url.url_shortner_sb.dtos.ClickEventDTO;
import com.url.url_shortner_sb.dtos.UrlMappingDTO;
import com.url.url_shortner_sb.models.ClickEvent;
import com.url.url_shortner_sb.models.UrlMapping;
import com.url.url_shortner_sb.models.User;
import com.url.url_shortner_sb.repository.ClickEventRepository;
import com.url.url_shortner_sb.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    public UrlMappingService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    private UrlMappingDTO convertToDto(UrlMapping urlMapping){
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDTO;

    }

    private String generateShortUrl() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    public List<UrlMappingDTO> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end){
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping != null){
                return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end)
                        .stream()
                        .collect(Collectors
                                .groupingBy(click -> click.getClickDate(), Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> {
                            ClickEventDTO clickEventDTO = new ClickEventDTO();
                            clickEventDTO.setClickDate(entry.getKey());
                            clickEventDTO.setCount(entry.getValue());
                            return clickEventDTO;
                        })
                        .collect(Collectors.toList());

        }
        return null;
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays((1)).atStartOfDay());
        return clickEvents
                .stream()
                .collect(
                        Collectors
                                .groupingBy(click -> click
                                        .getClickDate()
                                        .toLocalDate(),
                                        Collectors
                                                .counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        return urlMapping;
    }
}
