package com.turling.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A QuestionBaseGroup.
 */
@Entity
@Table(name = "question_base_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionBaseGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "question_ids")
    private String questionIds;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionBaseGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public QuestionBaseGroup title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionIds() {
        return this.questionIds;
    }

    public QuestionBaseGroup questionIds(String questionIds) {
        this.setQuestionIds(questionIds);
        return this;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionBaseGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((QuestionBaseGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionBaseGroup{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", questionIds='" + getQuestionIds() + "'" +
            "}";
    }
}
