package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
public class ArticleApiController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/api/articles")
    public List<Article> index(){
        return articleRepository.findAll();
    }

    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id){
        return articleRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto){
        Article article = dto.toEntity();
        return articleRepository.save(article);
    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id,@RequestBody ArticleForm dto){
        //DTO -> 엔티티 변환
        Article article = dto.toEntity();
        log.info("id : {}, article: {}",id,article.toString());
        //타깃 DB에서 조회하기
        Article target = articleRepository.findById(id).orElse(null);
        //잘못된 정보 처리
        if(target == null || article.getId() != id){
            log.info("잘못된 요청! id : {}, article: {}",id,article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //업데이트(수정) 및 정상응답하기
        target.patch(article);
        Article updated = articleRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){
        Article target = articleRepository.findById(id).orElse(null);
        if (target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        articleRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
