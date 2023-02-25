package com.board.demo.service;

import com.board.demo.repository.ReplyRepository;
import com.board.demo.vo.Board;
import com.board.demo.vo.Member;
import com.board.demo.vo.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.board.demo.util.Constants.ADMIN_ID;

@Service
public class ReplyServiceImpl implements ReplyService {
    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final int JUST_ONE_REPLY = 1;

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public boolean writeReply(long boardId, long parent, String content, Member member) {
        if (Objects.isNull(member)) {
            return false;
        }
        //SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        //String date = sdf.format(new Date());
        
        Reply reply = Reply.builder()
                .parent(parent)
                .board(Board.builder().boardId(boardId).build())
                //.writer(member.getMemberId())
                .member(member)
                .content(content)
                //.date(date)
                .build();

        reply = replyRepository.save(reply);
        if (parent == 0) {
            reply.setParent(reply.getReplyId());
            reply = replyRepository.save(reply);
        }
        return true;
    }

    @Override
    public boolean deleteReply(long replyId, long parent, Member member) {
        if (Objects.isNull(member)) {
            return false;
        }
        Optional<Reply> resReply = replyRepository.findByReplyIdAndParent(replyId, parent);

        if (!resReply.isPresent()) {
            return false;
        }
        if (resReply.get().getMember().getMemberId() != member.getMemberId() &&
                member.getMemberId() != ADMIN_ID ) {
            return false;
        }
        List<Reply> replies = replyRepository.findAllByParent(parent);
        if ( (replies.size() > JUST_ONE_REPLY) &&
                (parent == replyId) ) {
            Reply reply = resReply.get();
            reply.setContent("NULL");
            replyRepository.save(reply);
            return true;
        }
        replyRepository.deleteById(replyId);

        return !replyRepository.findById(replyId).isPresent();
    }

    @Override
    public Page<Reply> getListByMemberId(long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "replyId");
        return replyRepository.findAllByMember(Member.builder().memberId(memberId).build(), pageRequest);
    }
    
    @Override
    public int getCountByMemberId(long memberId) {
    	return replyRepository.countByMember(Member.builder().memberId(memberId).build());
    }
}
