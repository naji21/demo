package com.board.demo.service;

import com.board.demo.util.CurrentArticle;
import com.board.demo.vo.Board;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    Page<Board> getList(String category, int page, int size);

    boolean write(Long memberId, String title, String content, long category);

    Board getPostByIdForViewArticle(long boardId);

    Board getPostById(long boardId);

    boolean addViews(long boardId);

    CurrentArticle getPrevAndNextArticle(long boardId);

    Board getBoardById(long boardId);

    boolean modify(Board article, String title, String content, long category);

    void deleteArticle(long boardId);

    Page<Board> getListByMemberId(long memberId, int page, int size);
    
    List<Board> getListByLikeTop5();
    
    int getCountByMemberId(long memberId);
    
    List<Board> getNotices();

    public void convertArticleFormat(List<Board> articles);
}
