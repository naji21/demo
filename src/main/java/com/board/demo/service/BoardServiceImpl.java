package com.board.demo.service;

import com.board.demo.repository.BoardRepository;
import com.board.demo.repository.BoardRepositorySupport;
import com.board.demo.util.Conversion;
import com.board.demo.util.CurrentArticle;
import com.board.demo.vo.Board;
import com.board.demo.vo.Category;
import com.board.demo.vo.Member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final String ALL_POSTS = "전체보기";
    private final String NOTICE = "공지";
    private final int PREV_OR_NEXT = 0;
    private final int PREV_ARTICLE = 0;
    private final int NEXT_ARTICLE = 1;

    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private BoardRepositorySupport boardRepositorySupport;

    @Override
    public Page<Board> getList(String category, int page, int size) {
        Page<Board> boardlistPage;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "boardId");
        if (ALL_POSTS.equals(category)) {
            boardlistPage = boardRepository.findAllByCategoryNot(Category.builder().categoryId(Long.parseLong("0")).build(), pageRequest);
        } else {
            boardlistPage = boardRepository.findAllByCategory(Category.builder().categoryId(Long.parseLong(category)).build(), pageRequest);
        }

        return boardlistPage;
    }
    
    @Override
    public List<Board> getListByLikeTop5() {
        return boardRepositorySupport.getBoardListByLikeTop5();
    }

    @Override
    public boolean write(Long memberId, String title, String content, long category) {
        Board board = Board.builder()
                .member(Member.builder().memberId(memberId).build())
                .title(title)
                .content(content)
                .category(Category.builder().categoryId(category).build())
                .build();
        return !Objects.isNull(boardRepository.save(board));
    }

    @Override
    public boolean modify(Board article, String title, String content, long category) {
        article.setTitle(title);
        article.setContent(content);
        article.setCategory(Category.builder().categoryId(category).build());
        return !Objects.isNull(boardRepository.save(article));
    }

    @Override
    public void deleteArticle(long boardId) {
        try {
            boardRepository.deleteById(boardId);
        } catch (Exception e) {
            log.error("\n >> " + e.toString() + "\n >> There isn't a board no." + boardId);
        }
    }

    @Override
    public Page<Board> getListByMemberId(long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "boardId");
        return boardRepository.findAllByMember(Member.builder().memberId(memberId).build(), pageRequest);
    }
    
    @Override
    public int getCountByMemberId(long memberId) {
    	return boardRepository.countByMember(Member.builder().memberId(memberId).build());
    }

    @Override
    public List<Board> getNotices() {
        return boardRepository.findByCategory(Category.builder().categoryId((long)0).build());
    }

    @Override
    public void convertArticleFormat(List<Board> articles) {
        //articles.forEach(Conversion::convertDateFormatForArticleList);
        Conversion.convertTitleLength(articles);
    }

    @Override
    public Board getPostByIdForViewArticle(long boardId) {
        Board board = getPostById(boardId);
        //Conversion.convertContent(board);
        //Conversion.convertDateFormatForArticle(board);
        return board;
    }

    @Override
    public Board getPostById(long boardId) {
        //return boardlistRepository.findById(boardId).orElse(null);
    	return boardRepository.findById(boardId).orElse(null);
    }

    @Override
    public Board getBoardById(long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }

    @Override
    public boolean addViews(long boardId) {
        Optional<Board> opBoard = boardRepository.findById(boardId);
        if (!opBoard.isPresent()) {
            return false;
        }
        Board board = opBoard.get();
        long views = board.getViews() + 1;
        board.setViews(views);
        board = boardRepository.save(board);

        return board.getViews() == views;
    }

    @Override
    public CurrentArticle getPrevAndNextArticle(long boardId) {
        CurrentArticle currentArticle;
        List<Board> boards = boardRepository.findPrevAndNextBoardIdByBoardId(boardId);
        switch (boards.size()) {
            case 0:
                currentArticle = new CurrentArticle();
                break;
            case 1:
                long prevOrNext = boards.get(PREV_OR_NEXT).getBoardId();
                if (prevOrNext > boardId) {
                    currentArticle = CurrentArticle.builder()
                            .next(prevOrNext)
                            .build();
                    break;
                }
                currentArticle = CurrentArticle.builder()
                        .prev(prevOrNext)
                        .build();
                break;
            case 2:
                currentArticle = CurrentArticle.builder()
                        .prev(boards.get(PREV_ARTICLE).getBoardId())
                        .next(boards.get(NEXT_ARTICLE).getBoardId())
                        .build();
                break;
            default:
                currentArticle = null;
                break;
        }
        return currentArticle;
    }
}
