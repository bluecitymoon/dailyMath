package com.turling.service;

import static com.turling.security.SecurityUtils.AUTHORITIES_CLAIM;
import static com.turling.security.SecurityUtils.JWT_ALGORITHM;
import static com.turling.security.SecurityUtils.USER_ID_CLAIM;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turling.config.WechatProperties;
import com.turling.domain.Student;
import com.turling.repository.StudentRepository;
import com.turling.security.AuthoritiesConstants;
import com.turling.service.dto.StudentDTO;
import com.turling.service.dto.WechatLoginRequest;
import com.turling.service.dto.WechatLoginResponse;
import com.turling.service.mapper.StudentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service for handling WeChat login.
 */
@Service
@Transactional
public class WechatService {

    private static final Logger LOG = LoggerFactory.getLogger(WechatService.class);
    private static final String WECHAT_AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private final WechatProperties wechatProperties;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JwtEncoder jwtEncoder;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    public WechatService(
        WechatProperties wechatProperties,
        StudentRepository studentRepository,
        StudentMapper studentMapper,
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        JwtEncoder jwtEncoder
    ) {
        this.wechatProperties = wechatProperties;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Handle WeChat login
     *
     * @param request the WeChat login request
     * @return WechatLoginResponse containing student info and token
     */
    public WechatLoginResponse login(WechatLoginRequest request) {
        LOG.debug("Processing WeChat login with code: {}", request.getCode());

        // Step 1: Exchange code for session_key and openid
        String wechatUserId = getWechatUserId(request.getCode());
        LOG.debug("Retrieved WeChat openid: {}", wechatUserId);

        // Step 2: Check if student exists
        Optional<Student> existingStudent = studentRepository.findByWechatUserId(wechatUserId);

        Student student;
        boolean isNewUser = false;

        if (existingStudent.isPresent()) {
            // Existing user - update WeChat info
            student = existingStudent.get();
            LOG.debug("Found existing student with id: {}", student.getId());
            updateWechatInfo(student, request);
            student.setUpdateDate(Instant.now());
        } else {
            // New user - create student record
            student = new Student();
            student.setWechatUserId(wechatUserId);
            updateWechatInfo(student, request);
            student.setRegisterDate(Instant.now());
            student.setUpdateDate(Instant.now());
            isNewUser = true;
            LOG.debug("Creating new student for WeChat user: {}", wechatUserId);
        }

        student = studentRepository.save(student);
        StudentDTO studentDTO = studentMapper.toDto(student);

        // Step 3: Generate JWT token
        String token = generateToken(student.getId());

        return new WechatLoginResponse(token, studentDTO, isNewUser);
    }

    /**
     * Get WeChat user ID (openid) from WeChat API
     */
    private String getWechatUserId(String code) {
        try {
            String url = String.format(
                "%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WECHAT_AUTH_URL,
                wechatProperties.getAppId(),
                wechatProperties.getAppSecret(),
                code
            );

            LOG.debug("Calling WeChat API: {}", url.replaceAll(wechatProperties.getAppSecret(), "***"));

            String response = restTemplate.getForObject(url, String.class);
            LOG.debug("WeChat API response: {}", response);

            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.has("errcode")) {
                int errcode = jsonNode.get("errcode").asInt();
                String errmsg = jsonNode.has("errmsg") ? jsonNode.get("errmsg").asText() : "Unknown error";
                LOG.error("WeChat API error: {} - {}", errcode, errmsg);
                throw new RuntimeException("WeChat login failed: " + errmsg);
            }

            String openid = jsonNode.get("openid").asText();
            return openid;
        } catch (Exception e) {
            LOG.error("Error calling WeChat API", e);
            throw new RuntimeException("Failed to get WeChat user info", e);
        }
    }

    /**
     * Update student with WeChat user info
     */
    private void updateWechatInfo(Student student, WechatLoginRequest request) {
        if (request.getUserInfo() != null) {
            WechatLoginRequest.WechatUserInfo userInfo = request.getUserInfo();
            student.setWechatNickname(userInfo.getNickName());
            student.setWechatAvatar(userInfo.getAvatarUrl());
            student.setWechatSignature(userInfo.getSignature());
        }
    }

    /**
     * Generate JWT authentication token for student
     */
    private String generateToken(Long studentId) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);

        // For WeChat login, we give ROLE_USER authority by default
        String authorities = AuthoritiesConstants.Student;

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject("wechat-" + studentId) // Use student ID as subject with wechat prefix
            .claim(AUTHORITIES_CLAIM, authorities)
            .claim(USER_ID_CLAIM, studentId)
            .build();
        // @formatter:on

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
