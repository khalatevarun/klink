package com.url.url_shortner_sb.controller;

import com.url.url_shortner_sb.dtos.UrlMappingDTO;
import com.url.url_shortner_sb.models.User;
import com.url.url_shortner_sb.service.UrlMappingService;
import com.url.url_shortner_sb.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
public class UrlMappingController {

    private UrlMappingService urlMappingService;
    private UserService userService;

    public UrlMappingController(UrlMappingService urlMappingService,
                               UserService userService) {
        this.urlMappingService = urlMappingService;
        this.userService = userService;
    }

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request,
                                                        Principal principal
                                                        ){
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO =  urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

}
