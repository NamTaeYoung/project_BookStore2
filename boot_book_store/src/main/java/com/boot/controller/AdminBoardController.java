package com.boot.controller;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.boot.dto.BoardAttachDTO;
import com.boot.dto.BoardDTO;
import com.boot.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminBoardController {
	private final BoardService boardService;


    /**
     * ✅ 관리자 게시글 목록
     */
    @GetMapping("/boardManagement")
    public String boardManagement(HttpSession session,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "tc") String type,
                                   @RequestParam(defaultValue = "") String keyword,
                                   Model model) {

//        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
//        if (isAdmin == null || !isAdmin) {
//            log.warn("🚫 접근 차단: 관리자 세션 없음");
//            return "redirect:/admin/login";
//        }

        List<BoardDTO> list;
        int total;

        if (keyword.isEmpty()) {
            list = boardService.getPage(page, size);
            total = boardService.getTotalCount();
        } else {
            list = boardService.getSearchPage(type, keyword, page, size);
            total = boardService.getSearchTotalCount(type, keyword);
        }

        int pageCount = (int) Math.ceil(total / (double) size);
        int pageGroupSize = 5;
        int startPage = ((page - 1) / pageGroupSize) * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, pageCount);

        model.addAttribute("boardList", list);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("total", total);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);


        return "admin/boardManagement";
    }

    /**
     * ✅ 게시글 상세보기 (조회수 증가 X)
     */
    @GetMapping("/board/detail")
    public String adminDetail(@RequestParam("boardNo") Long boardNo,
                              HttpSession session, Model model) {

//        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
//        if (isAdmin == null || !isAdmin) return "redirect:/admin/login";

        BoardDTO post = boardService.getById(boardNo, false);
        if (post == null) return "redirect:/boardManagement";

        List<BoardAttachDTO> attaches = boardService.getImages(boardNo);

        Date boardDate = post.getBoardDate() == null ? null :
                Date.from(post.getBoardDate().atZone(ZoneId.systemDefault()).toInstant());


        model.addAttribute("post", post);
        model.addAttribute("attaches", attaches);
        model.addAttribute("boardDate", boardDate);

        return "admin/board/detail";
    }


    /**
     * ✅ 게시글 수정 폼
     */
    @GetMapping("/board/edit/{boardNo}")
    public String editForm(@PathVariable Long boardNo,
                           HttpSession session, Model model) {

//        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
//        if (isAdmin == null || !isAdmin) return "redirect:/admin/login";

        BoardDTO post = boardService.find(boardNo);
        if (post == null) return "redirect:/boardManagement";

        model.addAttribute("post", post);
        model.addAttribute("attaches", boardService.getImages(boardNo));

        return "admin/board/edit";
    }

    /**
     * ✅ 게시글 수정 처리
     */
    @PostMapping("/board/edit.do")
    public String edit(BoardDTO dto,
                       @RequestParam(value = "images", required = false) List<MultipartFile> newImages,
                       @RequestParam(value = "deleteFiles", required = false) List<String> deleteFiles)
            throws IOException {

    	boardService.update(dto);

        if (deleteFiles != null) {
            List<Long> deleteIds = deleteFiles.stream().map(Long::valueOf).toList();
            boardService.deleteAttachments(deleteIds);
        }

        if (newImages != null && !newImages.isEmpty()) {
        	boardService.addAttachments(dto.getBoardNo(), newImages);
        }

        return "redirect:/admin/board/detail?boardNo=" + dto.getBoardNo();
    }

    /**
     * ✅ 공지 삭제
     */
    @GetMapping("/board/delete/{boardNo}")
    public String delete(@PathVariable("boardNo") Long boardNo,
                         HttpSession session) {

//        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
//        if (isAdmin == null || !isAdmin) return "redirect:/admin/login";

    	boardService.delete(boardNo);

        return "redirect:/boardManagement";
    }
}
