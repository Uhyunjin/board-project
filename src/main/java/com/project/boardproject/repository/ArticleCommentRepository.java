package com.project.boardproject.repository;

import com.project.boardproject.domain.Article;
import com.project.boardproject.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
