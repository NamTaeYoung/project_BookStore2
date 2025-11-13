package com.boot.controller;

import com.boot.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/member")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    // ⭐ 관리자 회원목록 페이지
    @GetMapping("/adminlist")
    public String adminList(Model model, HttpSession session) {

        // ✔ 로그인 체크
        String loginId = (String) session.getAttribute("loginId");
        String userRole = (String) session.getAttribute("userRole");

        if (loginId == null) {
            return "redirect:/login";
        }

//        // ✔ 관리자만 접근 가능
//        if (!"ADMIN".equals(userRole)) {
//            return "redirect:/main";
//        }

        List<Map<String, Object>> members = adminMemberService.getAllMembers();
        model.addAttribute("members", members);

        return "admin/memberList";
    }

    // ⭐ 상세 보기
    @GetMapping("/detail")
    public String detail(@RequestParam("user_id") String userId,
                         Model model,
                         HttpSession session) {

//        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
//            return "redirect:/main";
//        }

        Map<String, Object> member = adminMemberService.getMemberById(userId);
        model.addAttribute("member", member);
        return "admin/memberDetail";
    }

    // ⭐ 수정 처리
    @PostMapping("/edit")
    public String edit(@RequestParam Map<String, Object> param, HttpSession session) {

//        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
//            return "redirect:/main";
//        }

        adminMemberService.updateMember(param);
        return "redirect:/admin/member/adminlist";
    }

    // ⭐ 삭제 처리
    @GetMapping("/delete")
    public String delete(@RequestParam("user_id") String userId, HttpSession session) {
//
//        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
//            return "redirect:/main";
//        }

        adminMemberService.deleteMember(userId);
        return "redirect:/admin/member/adminlist";
    }
}
