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

    protected Article() {} //private은 에러가 난다 hibernate문서 확인하기(my batis 대신 사용)

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    } // 메타데이터->자동 아이디->set불가능 도메인과 관련된 정보만 오픈하고 생성

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    } // of함수는 객체로서 최소조건을 만족한 채 만들어지고
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
