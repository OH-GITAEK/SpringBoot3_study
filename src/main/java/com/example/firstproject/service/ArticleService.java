package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if(article.getId() != null) return null;
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        //DTO -> 엔티티 변환
        Article article = dto.toEntity();
        log.info("id : {}, article: {}",id,article.toString());
        //타깃 DB에서 조회하기
        Article target = articleRepository.findById(id).orElse(null);
        //잘못된 정보 처리
        if(target == null || article.getId() != id){
            log.info("잘못된 요청! id : {}, article: {}",id,article.toString());
            return null;
        }
        //업데이트(수정) 및 정상응답하기
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);
        if (target == null){
            return null;
        }
        articleRepository.delete(target);
        return target;
    }

    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        //dto 묶음을 엔티티 묶음으로 변환하기
        List<Article> articleList = dtos.stream().map(dto -> dto.toEntity()).collect(Collectors.toList());
        //엔티티 묶음을 DB에 저장하기
        articleList.stream().forEach(article -> articleRepository.save(article));
        //강제 예외 발생시키기
        articleRepository.findById(-1L).orElseThrow(()-> new IllegalArgumentException("결제 실패!"));
        //결과 값 반환하기
        return articleList;
    }
}
