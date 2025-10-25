package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.QuestionOption} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionOptionDTO implements Serializable {

    private Long id;

    private String name;

    private Integer type;

    private String imageUrl;

    private Boolean isAnswer;

    private QuestionDTO question;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Boolean isAnswer) {
        this.isAnswer = isAnswer;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionOptionDTO)) {
            return false;
        }

        QuestionOptionDTO questionOptionDTO = (QuestionOptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionOptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionOptionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type=" + getType() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", isAnswer='" + getIsAnswer() + "'" +
            ", question=" + getQuestion() +
            "}";
    }
}
