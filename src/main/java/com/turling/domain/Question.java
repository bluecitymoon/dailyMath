package com.turling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "points")
    private Double points;

    @Column(name = "description")
    private String description;

    @Column(name = "solution")
    private String solution;

    @Column(name = "solution_external_link")
    private String solutionExternalLink;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_by_user_id")
    private Long createByUserId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "level")
    private Double level;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionCategory questionCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudentGrade grade;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Set<QuestionOption> options = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPoints() {
        return this.points;
    }

    public Question points(Double points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getDescription() {
        return this.description;
    }

    public Question description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return this.solution;
    }

    public Question solution(String solution) {
        this.setSolution(solution);
        return this;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolutionExternalLink() {
        return this.solutionExternalLink;
    }

    public Question solutionExternalLink(String solutionExternalLink) {
        this.setSolutionExternalLink(solutionExternalLink);
        return this;
    }

    public void setSolutionExternalLink(String solutionExternalLink) {
        this.solutionExternalLink = solutionExternalLink;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Question createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public Question updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Question createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateByUserId() {
        return this.createByUserId;
    }

    public Question createByUserId(Long createByUserId) {
        this.setCreateByUserId(createByUserId);
        return this;
    }

    public void setCreateByUserId(Long createByUserId) {
        this.createByUserId = createByUserId;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Question answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getLevel() {
        return this.level;
    }

    public Question level(Double level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public QuestionCategory getQuestionCategory() {
        return this.questionCategory;
    }

    public void setQuestionCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }

    public Question questionCategory(QuestionCategory questionCategory) {
        this.setQuestionCategory(questionCategory);
        return this;
    }

    public QuestionType getType() {
        return this.type;
    }

    public void setType(QuestionType questionType) {
        this.type = questionType;
    }

    public Question type(QuestionType questionType) {
        this.setType(questionType);
        return this;
    }

    public StudentGrade getGrade() {
        return this.grade;
    }

    public void setGrade(StudentGrade studentGrade) {
        this.grade = studentGrade;
    }

    public Question grade(StudentGrade studentGrade) {
        this.setGrade(studentGrade);
        return this;
    }

    public Set<QuestionOption> getOptions() {
        return this.options;
    }

    public void setOptions(Set<QuestionOption> questionOptions) {
        if (this.options != null) {
            this.options.forEach(i -> i.setQuestion(null));
        }
        if (questionOptions != null) {
            questionOptions.forEach(i -> i.setQuestion(this));
        }
        this.options = questionOptions;
    }

    public Question options(Set<QuestionOption> questionOptions) {
        this.setOptions(questionOptions);
        return this;
    }

    public Question addOptions(QuestionOption questionOption) {
        this.options.add(questionOption);
        questionOption.setQuestion(this);
        return this;
    }

    public Question removeOptions(QuestionOption questionOption) {
        this.options.remove(questionOption);
        questionOption.setQuestion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", description='" + getDescription() + "'" +
            ", solution='" + getSolution() + "'" +
            ", solutionExternalLink='" + getSolutionExternalLink() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createByUserId=" + getCreateByUserId() +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
