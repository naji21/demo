package com.board.demo.util;

import com.board.demo.vo.Reply;
import com.board.demo.vo.Article;
import com.board.demo.vo.Board;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Conversion {
    private static final int DATE_START = 0;
    private static final int DATE_END = 10;
    private static final int TIME_START = 11;
    private static final int TIME_END = 16;
    private static final int BEGIN = 0;
    private static final int MAX_TITLE_LENGTH = 30;
    private static final int MAX_CONTENT_LENGTH = 45;
    private static final int MAX_IMAGE_LENGHT = 25;

    public static void convertDateFormatForArticleList(Article article) {
        if (article.getDate().length() != 21)
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        if (article.getDate().substring(DATE_START, DATE_END).equals(today)) {
            article.setDate(article.getDate().substring(TIME_START, TIME_END));
        } else {
            article.setDate(article.getDate().substring(DATE_START, DATE_END));
        }
    }

    public static void convertTitleLength(List<Board> boards) {
        boards.forEach(board -> {
            if (board.getTitle().length() > MAX_TITLE_LENGTH) {
                StringBuilder title = new StringBuilder(board.getTitle().substring(BEGIN, MAX_TITLE_LENGTH));
                title.append( "...");
                board.setTitle(title.toString());
            }
        });
    }

    public static void convertContentLength(List<Reply> replies) {
        replies.forEach(reply -> {
            if (reply.getContent().length() > MAX_CONTENT_LENGTH) {
                StringBuilder content = new StringBuilder(reply.getContent().substring(BEGIN, MAX_CONTENT_LENGTH));
                content.append( "...");
                reply.setContent(content.toString());
            }
        });
    }

    public static int calcStartPage(int page) {
        return ((page - 1) / 10) * 10 + 1;
    }

    public static void convertContent(Article article) {
        String oldContent = article.getContent();
        article.setContent(oldContent.replace("\n", "<br>"));
    }

    public static void convertDateFormatForArticle(Article article) {
        article.setDate(article.getDate().substring(DATE_START, TIME_END));
    }

    public static String convertImageName(String originalFilename) {
        if (originalFilename.length() > MAX_TITLE_LENGTH) {
            StringBuilder sb = new StringBuilder();
            sb.append(originalFilename.substring(BEGIN, MAX_IMAGE_LENGHT));
            sb.append(".png");
            return sb.toString();
        }
        return originalFilename;
    }
}
