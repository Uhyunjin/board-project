package com.project.boardproject;

import com.project.boardproject.config.JpaConfig;
import com.project.boardproject.domain.Article;
import com.project.boardproject.repository.ArticleCommentRepository;
import com.project.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {

        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        //Given

        //When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles).isNotNull();
        // isEqualTo가 hasSize랑 너무 다르다.. 갯수 어떻게 세는지 모르겠음 ㅠㅠ
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        //Given
        long previousCount = articleRepository.count();
        //When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

        //Then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);
    }
    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        //기존의 데이터를 수정했을때의 변화를 관찰
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        System.out.println(article);
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        //When
        Article savedArticle = articleRepository.saveAndFlush(article);

        //Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
        System.out.println(savedArticle);
    }
    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        //기존의 데이터를 수정했을때의 변화를 관찰
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();

        int deletedCommentsSize = article.getArticleComments().size();
        System.out.println("deletedCommentsSize = " + deletedCommentsSize);

        //When
        articleRepository.delete(article);

        //Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount -1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }
}