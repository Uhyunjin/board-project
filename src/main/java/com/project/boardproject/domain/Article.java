package com.project.boardproject.domain;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}) //검색해줄것들, 근데 사이즈가 너무 크면 안됌
@EntityListeners(AuditingEntityListener.class) //이 어노테이션이 있어야 Auditing 기능이 작동
@Entity
public class Article extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 도메인 관련 데이터

    @Setter @Column(nullable = false)
    private String title; // 제목
    @Setter @Column(nullable = false, length = 10000)
    private String content; //내용
    @Setter
    private String hashtag; // 해시태그

    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    // 양방향 바인딩, 실무에서는 편집할때 불편함이 많아서 사용하지 않는 경우가 많다
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    //메타데이터를 필드를 추가하지 않고 따로 상속으로 받는 방법으로 처리

    protected Article() {} //private은 에러가 난다 hibernate문서 확인하기(my batis 대신 사용)

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    } // 메타데이터->자동 아이디->set불가능 도메인과 관련된 정보만 오픈하고 생성

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    } // of 함수는 객체로서 최소조건을 만족한 채 만들어지고
      // entity일 경우 db저장, dto일 경우 네트워크 전송이 바로 가능해야한다

        
    // 리스트에 넣거나, 중복요소 제거, 정렬 시 필요한 동등성 검사(equals & hashcode)
    // 모두 다 검사하지 않고 id만 검사해도(unique) 충분
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
            // pattern variable
            
        return id!= null && id.equals(article.id);
            // db에 영속화 전의 아티클은 id를 부여받지 않은 상태로, 다른것으로 간주
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
