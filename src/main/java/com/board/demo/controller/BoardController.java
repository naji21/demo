package com.board.demo.controller;

import com.board.demo.dto.ReplyDto;
import com.board.demo.service.BoardService;
import com.board.demo.service.CategoryService;
import com.board.demo.service.MemberLikeBoardService;
import com.board.demo.service.ReplyService;
import com.board.demo.util.Conversion;
import com.board.demo.util.CurrentArticle;
import com.board.demo.util.ErrorPage;
import com.board.demo.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.board.demo.util.Constants.*;
import static java.util.Objects.isNull;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
    private final String DEFAULT_CATEGORY = "전체보기";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_LIST_SIZE = "10";
    private final String ON = "ON";
    private final int MAIN_PAGE= 1;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MemberLikeBoardService memberLikeBoardService;

    @GetMapping
    public ModelAndView showBoard(
            @RequestParam(defaultValue = DEFAULT_CATEGORY, required = false) String category,
            @RequestParam(defaultValue = DEFAULT_PAGE, required = false) Integer page,
            @RequestParam(defaultValue = DEFAULT_LIST_SIZE, required = false) Integer size) {
        ModelAndView mav = new ModelAndView();
        Page<Board> boardlistPage = boardService.getList(category, page - 1, size);
        List<Board> boards = boardlistPage.getContent();

        //boardService.convertArticleFormat(boards);
        List<Category> categories = categoryService.getList();
        int totalPage = boardlistPage.getTotalPages();
        int startPage = Conversion.calcStartPage(page);

        if ( category.equals(DEFAULT_CATEGORY) &&
                page == MAIN_PAGE ) {
            List<Board> notices = boardService.getNotices();
            List<Board> topLikes = boardService.getListByLikeTop5();
            //boardService.convertArticleFormat(notices);
            mav.addObject("notices", notices);
            mav.addObject("topLikes", topLikes);
        }

        mav.setViewName("board");
        mav.addObject("boards", boards);
        mav.addObject("categories", categories);
        mav.addObject("selectCategory", category);
        mav.addObject("selectSize", size);
        mav.addObject("curPage", page);
        mav.addObject("totalPage", totalPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", totalPage > (startPage + 9) ? (startPage + 9) : totalPage );
        return mav;
    }

    @GetMapping("/write")
    public ModelAndView showWriteForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if (isNull(loginMember)) {
            mav.setViewName("redirect:/board");
            return mav;
        }
        mav.setViewName("write_form");
        mav.addObject("categories", categoryService.getList());
        return mav;
    }

    @PostMapping("/write")
    public void write(@RequestParam String title,
                      @RequestParam String content,
                      @RequestParam int category,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if (isNull(loginMember)) {
            res.put(RESULT, INVALID_APPROACH);
        }
        else if (boardService.write(loginMember.getMemberId(), title, content, category)) {
            res.put(RESULT, SUCCESS);
        } else {
            res.put(RESULT, FAIL);
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @GetMapping("/{idx}")
    public ModelAndView showArticle(@PathVariable("idx") int boardId,
                                    HttpServletRequest request) {
        if (!boardService.addViews(boardId)) {
            return ErrorPage.show();
        }
        Member member = (Member)request.getSession().getAttribute("loginMember");
        ModelAndView mav = new ModelAndView();
        if (!isNull(member)) {
            boolean isLike = memberLikeBoardService.isLike(boardId, member.getMemberId());
            mav.addObject("isLike", isLike);
        }

        Board article = boardService.getPostByIdForViewArticle(boardId);
        CurrentArticle currentArticle = boardService.getPrevAndNextArticle(boardId);
        //List<Replylist> replies = replyService.getRepliesByBoardId(boardId);
       
        //Map<Long, List<Reply>> replyMap = article.getReplyList().stream().sorted(Comparator.comparingLong(Reply::getParent)).collect(Collectors.toMap(Reply::getParent, Function.identity()));
        List<Reply> replies = article.getReplyList().stream().filter(s -> s.getReplyId() == s.getParent()).sorted(Comparator.comparingLong(Reply::getReplyId)).collect(Collectors.toList());
        
        Map<Long, List<Reply>> replyMap = article.getReplyList().stream().sorted(Comparator.comparingLong(Reply::getParent)).filter(s -> s.getReplyId() != s.getParent()).collect(Collectors.groupingBy(Reply::getParent));
        
        List<ReplyDto> replyList = new ArrayList<>();
        
        for(Reply reply : replies) {
        	List<Reply> subReplies = replyMap.get(reply.getParent());
        	log.debug("level :: " + (1) + ":::" + reply.getReplyId());
        	System.out.println("level :: " + (1) + ":::" + reply.getParent() + ":::" + reply.getReplyId());
        	
        	ReplyDto replyDto = new ReplyDto(reply);
        	replyDto.setLevel(1);
        	replyList.add(replyDto);
        	levelData(replyMap, 1, subReplies, replyList);
        }
        
        //article.setReplyList(replyList);
        
        mav.setViewName("view_article");
        mav.addObject("article", article);
        mav.addObject("replyList", replyList);
        
        mav.addObject("current", currentArticle);
        return mav;
    }
    
    private void levelData(Map<Long, List<Reply>> map, int level, List<Reply> list, List<ReplyDto> resultList) {
    	if( list != null ) {
    		for(Reply reply : list) {
    			ReplyDto replyDto = new ReplyDto(reply);
            	replyDto.setLevel(level+1);
            	
    			resultList.add(replyDto);
    			
    			log.debug("level :: " + (level+1) + ":::" + reply.getReplyId());
    			System.out.println("level :: " + (level+1) + ":::" + reply.getParent() + ":::" + reply.getReplyId());
    			
    			levelData(map, level+1, map.get(reply.getReplyId()), resultList);
    		}
    	}
    }

    @PostMapping("/write/reply")
    public ModelAndView writeReply(@RequestParam int boardId,
                           @RequestParam int parent,
                           @RequestParam String content,
                           HttpServletRequest request) {
        Member member = (Member)request.getSession().getAttribute("loginMember");
        boolean result = replyService.writeReply(boardId, parent, content, member);

        if (!result) {
            return ErrorPage.show();
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/board/"+boardId);
        return mav;
    }

    @PostMapping("/delete/reply")
    public ModelAndView deleteReply(@RequestParam int replyId,
                                    @RequestParam int parent,
                                    @RequestParam int boardId,
                                    HttpServletRequest request) {
        Member member = (Member)request.getSession().getAttribute("loginMember");
        boolean result = replyService.deleteReply(replyId, parent, member);

        if (!result) {
            return ErrorPage.show();
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/board/"+boardId);
        return mav;
    }

    @GetMapping("/modify/{idx}")
    public ModelAndView showModifyForm(@PathVariable("idx") long boardId,
                                       HttpServletRequest request) {
        Board article = boardService.getPostById(boardId);
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if ( isNull(article) ||
             isNull(loginMember) ||
            (loginMember.getMemberId() != article.getMember().getMemberId()) ) {
            return ErrorPage.show();
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("modify_form");
        mav.addObject("article", article);
        mav.addObject("categories", categoryService.getList());
        return mav;
    }

    @PostMapping("/modify")
    public void modify(@RequestParam long boardId,
                       @RequestParam String title,
                       @RequestParam String content,
                       @RequestParam int category,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Board article = boardService.getBoardById(boardId);
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");
        JSONObject res = new JSONObject();

        if ( isNull(article) ||
             isNull(loginMember) ||
            (loginMember.getMemberId() != article.getMember().getMemberId()) ) {
            res.put(RESULT, INVALID_APPROACH);
        }
        else if (boardService.modify(article, title, content, category)) {
            res.put(RESULT, SUCCESS);
        }
        else {
            res.put(RESULT, FAIL);
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @PostMapping("/like")
    public void likeOnOff(@RequestParam long boardId,
                          @RequestParam String flag,
                          HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if (isNull(loginMember)) {
            res.put(RESULT, INVALID_APPROACH);
        }
        else if (ON.equals(flag)) { // like on
            if (memberLikeBoardService.like(loginMember.getMemberId(), boardId)) {
                res.put(RESULT, SUCCESS);
            } else {
                res.put(RESULT, FAIL);
            }
        }
        else {  // like off
            memberLikeBoardService.dislike(loginMember.getMemberId(), boardId);
            res.put(RESULT, SUCCESS);
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @PostMapping("/delete")
    public ModelAndView deleteArticle(@RequestParam long boardId,
                                      HttpServletRequest request) {
        Board article = boardService.getBoardById(boardId);
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        if ( isNull(article) ||
             isNull(loginMember) ||
            (loginMember.getMemberId() != article.getMember().getMemberId() &&
             loginMember.getMemberId() != ADMIN_ID )) {
            return ErrorPage.show();
        }

        boardService.deleteArticle(boardId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/board");
        return mav;
    }
}
