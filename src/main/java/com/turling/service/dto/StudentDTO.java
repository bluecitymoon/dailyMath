package com.turling.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.Student} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentDTO implements Serializable {

    private Long id;

    private String name;

    private String gender;

    private Instant birthday;

    private Instant registerDate;

    private Instant updateDate;

    private Instant latestContractEndDate;

    private String contactNumber;

    private String parentsName;

    private String wechatUserId;

    private String wechatNickname;

    private String wechatAvatar;

    private String wechatSignature;

    private SchoolDTO school;

    private CommunityDTO community;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public Instant getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Instant registerDate) {
        this.registerDate = registerDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public Instant getLatestContractEndDate() {
        return latestContractEndDate;
    }

    public void setLatestContractEndDate(Instant latestContractEndDate) {
        this.latestContractEndDate = latestContractEndDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getParentsName() {
        return parentsName;
    }

    public void setParentsName(String parentsName) {
        this.parentsName = parentsName;
    }

    public String getWechatUserId() {
        return wechatUserId;
    }

    public void setWechatUserId(String wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    public String getWechatNickname() {
        return wechatNickname;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname;
    }

    public String getWechatAvatar() {
        return wechatAvatar;
    }

    public void setWechatAvatar(String wechatAvatar) {
        this.wechatAvatar = wechatAvatar;
    }

    public String getWechatSignature() {
        return wechatSignature;
    }

    public void setWechatSignature(String wechatSignature) {
        this.wechatSignature = wechatSignature;
    }

    public SchoolDTO getSchool() {
        return school;
    }

    public void setSchool(SchoolDTO school) {
        this.school = school;
    }

    public CommunityDTO getCommunity() {
        return community;
    }

    public void setCommunity(CommunityDTO community) {
        this.community = community;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentDTO)) {
            return false;
        }

        StudentDTO studentDTO = (StudentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", registerDate='" + getRegisterDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", latestContractEndDate='" + getLatestContractEndDate() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", parentsName='" + getParentsName() + "'" +
            ", wechatUserId='" + getWechatUserId() + "'" +
            ", wechatNickname='" + getWechatNickname() + "'" +
            ", wechatAvatar='" + getWechatAvatar() + "'" +
            ", wechatSignature='" + getWechatSignature() + "'" +
            ", school=" + getSchool() +
            ", community=" + getCommunity() +
            "}";
    }
}
