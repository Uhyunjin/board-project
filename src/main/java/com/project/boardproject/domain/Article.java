package com.project.boardproject.domain;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hasgtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}) //검색해줄것들, 근데 사이즈가 너무 크면 안됌
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 도메인 관련 데이터

    @Setter @Column(nullable = false, length = 10000)
    private String title; // 제목
    @Setter @Column(nullable = false)
    private String content; //내용
    @Setter
    private String hashtag; // 해시태그

    //메타데이터
    @CreatedDate @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy @Column(nullable = false, length = 100)
    private String createdBy; // 생성자
    //어노테이션에 의해 auditing 할때마다 jap config에서 bean으로 등록된 unknown으로 입력된다

    @LastModifiedDate @Column(nullable = false)
    private LocalDateTime modifiedAt; //수정일시

    @LastModifiedBy @Column(nullable = false, length = 100)
    private String modifiedBy; //수정자

    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id!= null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
