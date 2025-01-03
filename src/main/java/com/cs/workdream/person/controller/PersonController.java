package com.cs.workdream.person.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cs.workdream.business.model.vo.Business;
import com.cs.workdream.member.model.vo.Member;
import com.cs.workdream.usertags.model.vo.UserTag;
import com.cs.workdream.usertags.service.UserTagsService;

@Controller
@RequestMapping("/person")
public class PersonController {
	@Autowired
    private UserTagsService userTagsService;
	
	
	/*=====================================================================================================*/
	
	
	/* 구직자 마이페이지 관련*/
	// 마이페이지로 이동
	@GetMapping("/applicantsMypage")
    public String applicantsMypage(Model model, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        Member currentUser = (Member) session.getAttribute("loginUser");
        System.out.println("Current User: " + currentUser); // 로그 추가
        
        if (currentUser != null) {
            // 현재 사용자의 관심태그 목록 가져오기
            List<UserTag> userTags = userTagsService.getUserTags(currentUser.getUserNo());
            System.out.println("Fetched userTags: " + userTags); // 로그 추가
            // 모델에 관심태그 추가
            model.addAttribute("userTags", userTags);
        } else {
            // 로그인하지 않은 사용자 처리 (빈 리스트 추가)
            System.out.println("No user is logged in."); // 로그 추가
            model.addAttribute("userTags", Collections.emptyList());
        }
        
        return "person/applicantsMypage"; // applicantsMypage.jsp
    }
	
	
	/*=====================================================================================================*/
	
	
	/* 지원현황 관련 */
	// 지원현황 페이지로 이동
	@GetMapping("/applicationManager")
	public ModelAndView applicationManager(ModelAndView mv, HttpSession session) {
		// 세션에서 로그인한 사용자 정보 가져오기
        Member currentUser = (Member) session.getAttribute("loginUser");
		
        if (currentUser != null) {
        	int personNo = currentUser.getPersonNo();
        	
        	// 이력서 > personNo으로 검색 > 반환
        	mv.setViewName("person/applicationManage");
        } else {
            // 로그인하지 않은 경우 처리
        	mv.setViewName("common/errorPage");
			mv.addObject("errorMsg", "로그인이 필요한 서비스입니다.");
			mv.addObject("returnPage", "/login?ut=P");
        }
        
        return mv;
	}
	
	
	/*=====================================================================================================*/
	
	
	@GetMapping("/clipping")
	public String clipping() {
		return "person/clipping";
	}
	
	@GetMapping("/clippingcompany")
	public String clippingcompany() {
		return "person/clippingcompany";
	}
	
}
