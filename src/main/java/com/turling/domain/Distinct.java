package com.turling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Distinct.
 */
@Entity
@Table(name = "jhi_distinct")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Distinct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pinyin")
    private String pinyin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "distinct")
    @JsonIgnoreProperties(value = { "distinct" }, allowSetters = true)
    private Set<Community> communities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Distinct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Distinct name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public Distinct pinyin(String pinyin) {
        this.setPinyin(pinyin);
        return this;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Set<Community> getCommunities() {
        return this.communities;
    }

    public void setCommunities(Set<Community> communities) {
        if (this.communities != null) {
            this.communities.forEach(i -> i.setDistinct(null));
        }
        if (communities != null) {
            communities.forEach(i -> i.setDistinct(this));
        }
        this.communities = communities;
    }

    public Distinct communities(Set<Community> communities) {
        this.setCommunities(communities);
        return this;
    }

    public Distinct addCommunities(Community community) {
        this.communities.add(community);
        community.setDistinct(this);
        return this;
    }

    public Distinct removeCommunities(Community community) {
        this.communities.remove(community);
        community.setDistinct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distinct)) {
            return false;
        }
        return getId() != null && getId().equals(((Distinct) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Distinct{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pinyin='" + getPinyin() + "'" +
            "}";
    }
}
