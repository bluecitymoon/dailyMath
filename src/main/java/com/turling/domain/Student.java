package com.turling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private Instant birthday;

    @Column(name = "register_date")
    private Instant registerDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "latest_contract_end_date")
    private Instant latestContractEndDate;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "parents_name")
    private String parentsName;

    @Column(name = "wechat_user_id", unique = true)
    private String wechatUserId;

    @Column(name = "wechat_nickname")
    private String wechatNickname;

    @Column(name = "wechat_avatar")
    private String wechatAvatar;

    @Column(name = "wechat_signature")
    private String wechatSignature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "distinct" }, allowSetters = true)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "distinct" }, allowSetters = true)
    private Community community;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public Student gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Instant getBirthday() {
        return this.birthday;
    }

    public Student birthday(Instant birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public Instant getRegisterDate() {
        return this.registerDate;
    }

    public Student registerDate(Instant registerDate) {
        this.setRegisterDate(registerDate);
        return this;
    }

    public void setRegisterDate(Instant registerDate) {
        this.registerDate = registerDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public Student updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public Instant getLatestContractEndDate() {
        return this.latestContractEndDate;
    }

    public Student latestContractEndDate(Instant latestContractEndDate) {
        this.setLatestContractEndDate(latestContractEndDate);
        return this;
    }

    public void setLatestContractEndDate(Instant latestContractEndDate) {
        this.latestContractEndDate = latestContractEndDate;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public Student contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getParentsName() {
        return this.parentsName;
    }

    public Student parentsName(String parentsName) {
        this.setParentsName(parentsName);
        return this;
    }

    public void setParentsName(String parentsName) {
        this.parentsName = parentsName;
    }

    public String getWechatUserId() {
        return this.wechatUserId;
    }

    public Student wechatUserId(String wechatUserId) {
        this.setWechatUserId(wechatUserId);
        return this;
    }

    public void setWechatUserId(String wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    public String getWechatNickname() {
        return this.wechatNickname;
    }

    public Student wechatNickname(String wechatNickname) {
        this.setWechatNickname(wechatNickname);
        return this;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname;
    }

    public String getWechatAvatar() {
        return this.wechatAvatar;
    }

    public Student wechatAvatar(String wechatAvatar) {
        this.setWechatAvatar(wechatAvatar);
        return this;
    }

    public void setWechatAvatar(String wechatAvatar) {
        this.wechatAvatar = wechatAvatar;
    }

    public String getWechatSignature() {
        return this.wechatSignature;
    }

    public Student wechatSignature(String wechatSignature) {
        this.setWechatSignature(wechatSignature);
        return this;
    }

    public void setWechatSignature(String wechatSignature) {
        this.wechatSignature = wechatSignature;
    }

    public School getSchool() {
        return this.school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Student school(School school) {
        this.setSchool(school);
        return this;
    }

    public Community getCommunity() {
        return this.community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Student community(Community community) {
        this.setCommunity(community);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return getId() != null && getId().equals(((Student) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
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
            "}";
    }
}
