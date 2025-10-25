package com.turling.web.rest;

import com.turling.service.WechatService;
import com.turling.service.dto.WechatLoginRequest;
import com.turling.service.dto.WechatLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for WeChat operations.
 */
@RestController
@RequestMapping("/api/wechat")
public class WechatController {

    private static final Logger LOG = LoggerFactory.getLogger(WechatController.class);

    private final WechatService wechatService;

    public WechatController(WechatService wechatService) {
        this.wechatService = wechatService;
    }

    /**
     * {@code POST /wechat/login} : WeChat login
     *
     * @param request the WeChat login request containing code and user info
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the login response,
     * and JWT token in Authorization header
     */
    @PostMapping("/login")
    public ResponseEntity<WechatLoginResponse> login(@RequestBody WechatLoginRequest request) {
        LOG.debug("REST request for WeChat login with code: {}", request.getCode());

        WechatLoginResponse response = wechatService.login(request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(response.getToken());

        return ResponseEntity.ok().headers(httpHeaders).body(response);
    }
}
