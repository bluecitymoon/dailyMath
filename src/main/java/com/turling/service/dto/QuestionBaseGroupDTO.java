package com.turling.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.turling.domain.QuestionBaseGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionBaseGroupDTO implements Serializable {

    private Long id;

    private String title;

    private String questionIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionBaseGroupDTO)) {
            return false;
        }

        QuestionBaseGroupDTO questionBaseGroupDTO = (QuestionBaseGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionBaseGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionBaseGroupDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", questionIds='" + getQuestionIds() + "'" +
            "}";
    }
}
