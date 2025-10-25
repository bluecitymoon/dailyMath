package com.turling.service.dto;

import java.io.Serializable;

/**
 * DTO for wechat login request.
 */
public class WechatLoginRequest implements Serializable {

    private String code;
    private String encryptedData;
    private String iv;
    private String signature;
    private WechatUserInfo userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public WechatUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(WechatUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class WechatUserInfo implements Serializable {

        private String nickName;
        private String avatarUrl;
        private String signature;
        private Integer gender;
        private String city;
        private String province;
        private String country;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    @Override
    public String toString() {
        return (
            "WechatLoginRequest{" +
            "code='" +
            code +
            "'" +
            ", encryptedData='" +
            encryptedData +
            "'" +
            ", iv='" +
            iv +
            "'" +
            ", signature='" +
            signature +
            "'" +
            ", userInfo=" +
            userInfo +
            "}"
        );
    }
}
