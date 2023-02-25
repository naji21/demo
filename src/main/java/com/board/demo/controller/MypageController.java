package com.board.demo.controller;

import com.board.demo.service.BoardService;
import com.board.demo.service.MemberService;
import com.board.demo.service.MypageService;
import com.board.demo.service.ReplyService;
import com.board.demo.util.Conversion;
import com.board.demo.util.ErrorPage;
import com.board.demo.util.FileIO;
import com.board.demo.vo.Board;
import com.board.demo.vo.Member;
import com.board.demo.vo.Reply;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.board.demo.util.Constants.*;
import static java.util.Objects.isNull;

@Slf4j
@Controller
@RequestMapping("/mypage")
public class MypageController {
    private final String DEFAULT_PAGE = "1";
    private final int DEFAULT_LIST_SIZE = 10;
    private final String DEFAULT_TYPE = "board";

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MypageService mypageService;

    @GetMapping("")
    public ModelAndView showMypage(@RequestParam(defaultValue = DEFAULT_TYPE, required = false) String type,
                                   @RequestParam(defaultValue = DEFAULT_PAGE, required = false) Integer page,
                                   @RequestParam(value = "id", required = false) Long memberId,
                                   HttpServletRequest request) {
//        log.info("mypage > id: "+memberId);
        ModelAndView mav = new ModelAndView();
        
        if (isNull(memberId)) {
            Member loginMember = (Member) request.getSession().getAttribute("loginMember");
            if (isNull(loginMember)) {
                mav.setViewName("redirect:/board");
                return mav;
            }
            memberId = loginMember.getMemberId();
        }

        Member mypage = mypageService.getMemberInfo(memberId);
        
        if (isNull(mypage)) {
            return ErrorPage.show();
        }
        
        int boardCount = boardService.getCountByMemberId(memberId);
        int replyCount = replyService.getCountByMemberId(memberId);
        int totalPages = 0;
        if (type.equals(DEFAULT_TYPE)) {    // 등록한 게시글 요청
        	Page<Board> boardPage = boardService.getListByMemberId(memberId, page - 1, DEFAULT_LIST_SIZE);
            List<Board> boardList = boardPage.getContent();
            totalPages = boardPage.getTotalPages();
            
            mav.addObject("boards", boardList);
            
        } else {  // 등록한 댓글 요청
        	Page<Reply> replyPage = replyService.getListByMemberId(memberId, page - 1, DEFAULT_LIST_SIZE);
        	
        	List<Reply> replyList = replyPage.getContent();
            totalPages = replyPage.getTotalPages();
            
            mav.addObject("replies", replyList);
        }

        int startPage = Conversion.calcStartPage(page);
        mav.addObject("mypage", mypage);
        
        mav.addObject("type", type);
        mav.addObject("curPage", page);
        mav.addObject("boardCount", boardCount);
        mav.addObject("replyCount", replyCount);
        
        mav.addObject("startPage", startPage);
        mav.addObject("totalPages", totalPages);
        mav.addObject("endPage", totalPages > (startPage + 9) ? (startPage + 9) : totalPages );
        
        mav.setViewName("my_page");
        return mav;
    }

    @GetMapping("/show-profile")
    public ModelAndView showProfileImage(@RequestParam(value = "id") Long memberId) {
        ModelAndView mav = new ModelAndView();
        Member mypage = mypageService.getMemberInfo(memberId);
        mav.addObject("mypage", mypage);
        mav.setViewName("show_profile");
        return mav;
    }

    @PostMapping("/set-profile")
    public void setProfileImage(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
//        log.info("setProfileImage entered.. ");
//        log.info(file.getOriginalFilename());
//        log.info(file.getName());
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");
        JSONObject res = new JSONObject();
        if (isNull(loginMember) || file.isEmpty()) {
            // exception
            res.put("result", INVALID_APPROACH);
        }
        else {
            String folder = loginMember.getMemberId() + "";
            String filename = Conversion.convertImageName(file.getOriginalFilename());
            if (FileIO.saveImage(folder, filename, file.getBytes())) {
                if (memberService.setProfilePhoto(loginMember.getMemberId(), filename)) {
                    res.put("result", SUCCESS);
                } else {
                    res.put("result", FAIL);
                }
            } else {
                res.put("result", FAIL);
            }
        }

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }

    @GetMapping("/set-default-profile")
    public void setDefaultProfileImage(@RequestParam int id,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");
        JSONObject res = new JSONObject();
        if (isNull(loginMember) || loginMember.getMemberId() != id) {
            res.put("result", INVALID_APPROACH);
        }
        else {
            if (memberService.setProfilePhoto(loginMember.getMemberId(), null)) {
                res.put("result", SUCCESS);
            } else {
                res.put("result", FAIL);
            }
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);
    }
}
