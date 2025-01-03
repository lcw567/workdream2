package com.cs.workdream.business.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cs.workdream.business.model.vo.Applicants;
import com.cs.workdream.business.model.vo.Business;
import com.cs.workdream.business.model.vo.BusinessBookmark;
import com.cs.workdream.business.model.vo.Position;
import com.cs.workdream.business.model.vo.Recruitment;
import com.cs.workdream.business.service.BusinessProfileService;
import com.cs.workdream.business.service.BusinessService;
import com.cs.workdream.member.model.vo.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/business")
public class BusinessController {
	@Autowired
	private BusinessService businessService;
    private BusinessProfileService businessProfileService;
	
	@Autowired
	public BusinessController(BusinessService businessService, BusinessProfileService businessProfileService) {
		this.businessService = businessService;
		this.businessProfileService = businessProfileService;
	}
	
	
	/*=====================================================================================================*/
	
	
	/* 기업 마이페이지 (기업 홈) 관련 */
	// 기업홈 페이지로 이동
	@RequestMapping("/businessMypage")
    public ModelAndView businessMypage(HttpSession session, ModelAndView mv) throws Exception {
        // 세션에서 로그인한 사용자 정보 가져오기
        Member currentUser = (Member)session.getAttribute("loginUser");
        
        if (currentUser != null) {
        	// 기업 정보 가져오기
        	int businessNo = currentUser.getBusinessNo();
            Business business = businessProfileService.viewBusinessProfile(businessNo);
            
            // 지원자 현황 정보 가져오기
            Map<String, Integer> dashboard = businessService.selectApplicantsDashboard(businessNo, 0);
            
            mv.setViewName("business/businessMypage");
            mv.addObject("business", business);
            mv.addObject("dashboard", dashboard);
        } else {
            // 로그인하지 않은 경우 처리
        	mv.setViewName("common/errorPage");
			mv.addObject("errorMsg", "로그인이 필요한 서비스입니다.");
			mv.addObject("returnPage", "/login?ut=B");
        }
        
        return mv;
    }
	
	
	/*=====================================================================================================*/
	
	
	/* 채용공고 관련 */
	// 채용공고관리 페이지로 이동
	@RequestMapping("/recruitmentManager")
    public ModelAndView recruitmentManager(ModelAndView mv, HttpSession session) {
		Member currentUser = (Member) session.getAttribute("loginUser");
		
		if(currentUser != null) {
			// 현재 공고 현황 조회 > 전달
			mv.setViewName("business/recruitmentManager");
			mv.addObject("statusMap", selectRecruitmentStatus(session));
		} else {
			mv.setViewName("common/errorPage");
			mv.addObject("errorMsg", "로그인이 필요한 서비스입니다.");
			mv.addObject("returnPage", "/login?ut=B");
		}
		
        return mv;
    }

	// 현재 공고 현황 조회
	public Map<String, Integer> selectRecruitmentStatus(HttpSession session) {
		Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
		
		Map<String, Integer> statusMap = businessService.selectRecruitmentStatus(businessNo);
		
		return statusMap;
	}
	
	// 진행중인 공고 목록 조회
	@GetMapping("/progressRecruitment.biz")
	@ResponseBody
	public List<Recruitment> selectListProgressRecruitment(HttpSession session) {
		Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	List<Recruitment> progressList = businessService.selectListProgressRecruitment(businessNo);
		
		return progressList;
	}
	
	// 대기중인 공고 목록 조회
	@RequestMapping("/standByRecruitment.biz")
	@ResponseBody
	public List<Recruitment> selectListStandByRecruitment(HttpSession session) {
		Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	List<Recruitment> standByList = businessService.selectListStandByRecruitment(businessNo);
		
		return standByList;
	}
	
	// 임시저장한 공고 목록 조회
	@RequestMapping("/temporaryRecruitment.biz")
	@ResponseBody
	public List<Recruitment> selectListTemporaryRecruitment(HttpSession session) {
		Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	List<Recruitment> temporaryList = businessService.selectListTemporaryRecruitment(businessNo);
		
		return temporaryList;
	}
	
	// 마감된 공고 목록 조회
	@RequestMapping("/endRecruitment.biz")
	@ResponseBody
	public List<Recruitment> selectListEndyRecruitment(HttpSession session) {
		Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	List<Recruitment> endList = businessService.selectListEndRecruitment(businessNo);
		
		return endList;
	}
	
	// 채용공고 작성 페이지로 이동
    @GetMapping("/recruitmentRegister")
    public ModelAndView recruitmentRegister(@RequestParam(value="no", defaultValue="0") int recruitmentNo, @RequestParam(value="step", defaultValue="1") int step, ModelAndView mv, HttpSession session) throws JsonProcessingException {
    	// 공고 정보 불러오기
    	Member currentUser = (Member)session.getAttribute("loginUser");
    	Recruitment jobPosting = (Recruitment)session.getAttribute("jobPosting");
    	
    	if(currentUser != null) {
    		int businessNo = currentUser.getBusinessNo();
        	
        	switch(step) {
        	case 1:
        		mv.setViewName("business/recruitmentRegister1");
        		break;
        	case 2:
        		mv.setViewName("business/recruitmentRegister2");
        		break;
        	default:
        		// 값을 제대로 받지 못했을 경우에도 첫 번째 작성 페이지로 이동
        		mv.setViewName("business/recruitmentRegister1");
        	}
        	
        	if(jobPosting == null) {
        		// 편집 중인 공고가 없을 경우 > 기존 공고 or 기본 설정 불러온 후 반환
        		jobPosting = businessService.selectJobPosting(businessNo, recruitmentNo);
        	}
        	
        	// 객체를 JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String jobPostingJson = objectMapper.writeValueAsString(jobPosting);
        	
    		mv.addObject("jobPosting", jobPosting);
    		mv.addObject("jobPostingJson", jobPostingJson);
    	} else {
    		mv.setViewName("common/errorPage");
			mv.addObject("errorMsg", "로그인이 필요한 서비스입니다.");
			mv.addObject("returnPage", "/login?ut=B");
    	}
    	
    	return mv;
    }
    
    // 페이지 이동 시 편집 정보 저장
    @PostMapping("/updateJobPosting.biz")
    @ResponseBody
    public int updateJobPosting(@RequestBody Recruitment updatedJobPosting, HttpSession session) {
    	if (updatedJobPosting != null) {
            // updatedJobPosting이 null이 아닐 경우 > 기존 세션에 덮어쓰기
            session.setAttribute("jobPosting", updatedJobPosting);
            return 1;
        } else {
        	// updatedJobPosting이 null일 경우
            return 0;
        }
    }
    
    // 페이지 이동 시 편집 정보 저장된 세션 삭제
    @RequestMapping("/deleteJobPosting.biz")
    @ResponseBody
    public int deleteJobPosting(HttpSession session) {
    	session.removeAttribute("jobPosting");
    	return 1;
    }
    
    // 공고 수정
    public int updateRecruitment(@RequestParam("no") int recruitmentNo) {
    	// 여긴 구상중 - 채용공고 작성 페이지가 완료 되어야 수정 가능할듯.
    	return 0;
    }
    
    // 공고 삭제
    @GetMapping("/deleteRecruitment.biz")
    @ResponseBody
    public int deleteRecruitment(@RequestParam("no") int recruitmentNo) {
    	return businessService.deleteRecruitment(recruitmentNo);
    }
	
	
	/*=====================================================================================================*/
	
	
	/* 지원자 현황&목록 관련 */
	// 지원자 현황 페이지로 이동
    @RequestMapping("/applicantsStatus")
	public ModelAndView applicantsStatus(@RequestParam(value="no", defaultValue="0") int recruitmentNo, ModelAndView mv, HttpSession session) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
		
		if(currentUser != null) {
			// 지원자 현황 조회 -> 전달
			Map<String, Integer> dashboard = selectApplicantsDashboard(recruitmentNo, session);
			List<Position> positionList = selectPositionList(recruitmentNo, session);
			positionList = selectPositionDashboard(positionList);
			
			mv.setViewName("business/applicantsStatus");
			mv.addObject("dashboard", dashboard);
			mv.addObject("positionList", positionList);
		} else {
			mv.setViewName("common/errorPage");
			mv.addObject("errorMsg", "로그인이 필요한 서비스입니다.");
			mv.addObject("returnPage", "/login?ut=B");
		}
		
        return mv;
	}
    
    // 지원자 현황 조회
    public Map<String, Integer> selectApplicantsDashboard(int recruitmentNo, HttpSession session) {
    	Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	Map<String, Integer> dashboard = businessService.selectApplicantsDashboard(businessNo, recruitmentNo);
    	
    	return dashboard;
    }
    
    // 포지션 목록 조회
    public List<Position> selectPositionList(int recruitmentNo, HttpSession session) {
    	Member currentUser = (Member)session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	List<Position> positionList = businessService.selectPositionList(businessNo, recruitmentNo);
    	
    	return positionList;
    }
    
    // 포지션 현황 조회
    public List<Position> selectPositionDashboard(List<Position> positionList) {
    	// 기존 객체에 현황 정보만 추가해서 반환
    	return businessService.selectPositionDashboard(positionList);
    }
	
	// 지원자 목록 페이지로 이동
    @GetMapping("/applicantsList")
    public ModelAndView applicantsList(@RequestParam("no") int recruitmentNo, @RequestParam("position") int positionNo, ModelAndView mv, HttpSession session) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
    	
    	if(currentUser != null) {
    		// 포지션 정보 및 현황 조회
			Position positionDetail = businessService.selectPositionDetail(recruitmentNo, positionNo);
			
			mv.setViewName("business/applicantsList");
			mv.addObject("positionDetail", positionDetail);
		} else {
			mv.setViewName("common/errorPage");
			mv.addObject("errorMsg", "로그인이 필요한 서비스입니다.");
			mv.addObject("returnPage", "/login?ut=B");
		}
		
        return mv;
    }
    
    // 지원자 목록 조회
    @RequestMapping("/loadApplicantsList.biz")
    @ResponseBody
    public List<Applicants> selectApplicantsList(@RequestParam("no") int recruitmentNo, @RequestParam("position") int positionNo, @RequestParam(value="stagy", defaultValue="0") int stagyNo) {
    	return businessService.selectApplicantsList(recruitmentNo, positionNo, stagyNo);
    }
    
    
    /*=====================================================================================================*/
    
    
    /* 구직자 즐겨찾기 관련 */
    // 구직자 즐겨찾기 페이지로 이동
    @RequestMapping("/bookmark")
    public String bookmarkList(HttpSession session, Model model) {
    	// loadBookmarkList 값을 받아서 사용
    	loadBookmarkList(session, model);
    	
        return "business/businessBookmarkList";
    }
    
    // 즐겨찾기 목록 조회
    public Model loadBookmarkList(HttpSession session, Model model) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	List<BusinessBookmark> bookmarkList = businessService.loadBookmarkList(businessNo);
    	model.addAttribute("bookmarkList", bookmarkList);
    	
    	return model;
    }
	
    // 즐겨찾기에서 삭제
    @RequestMapping("/deleteBookmark.biz")
    @ResponseBody
    public int deleteBookmarkList(@RequestParam("no") int resumeNo, HttpSession session) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	return businessService.deleteBookmarkList(businessNo, resumeNo);
    }
    
    // 즐겨찾기 그룹 분류 수정
    @RequestMapping("/updateBookmark.biz")
    @ResponseBody
    public int updateBookmarkFolder(@RequestBody BusinessBookmark bookmark, @RequestParam("type") String type, @RequestParam(value = "folder", required = false) int folder) {
    	return businessService.updateBookmarkFolder(bookmark, type, folder);
    }
    
    // 즐겨찾기 그룹 추가
    @RequestMapping("/insertFolder.biz")
    @ResponseBody
    public int insertFolder(@RequestParam("folderName") String folderName, HttpSession session) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	return businessService.insertFolder(businessNo, folderName);
    }
    
    // 즐겨찾기 그룹 편집
    @RequestMapping("/updateFolder.biz")
    @ResponseBody
    public int updateFolder(@RequestParam("folder") int folder, @RequestParam(value = "order") int order, @RequestParam(value = "folderName") String folderName, HttpSession session) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	return businessService.updateFolder(businessNo, folder, order, folderName);
    }
    
    // 즐겨찾기 그룹 삭제
    @RequestMapping("/deleteFolder.biz")
    @ResponseBody
    public int deleteFolder(@RequestParam("folder") int folder, HttpSession session) {
    	Member currentUser = (Member) session.getAttribute("loginUser");
    	int businessNo = currentUser.getBusinessNo();
    	
    	return businessService.deleteFolder(businessNo, folder);
    }
	
    
	/*=====================================================================================================*/
	

	@RequestMapping("/announcementDetailView")
    public String announcementDetailView() {
        return "business/announcementDetailView";

	}
	@RequestMapping("/preview")
    public String preview() {
        return "business/preview";

    }
	
	@RequestMapping("/positionAndCareer")
    public String positionCareer() {
        return "business/positionAndCareer";
    }
	
	
	/*=====================================================================================================*/
	
	
	/* 인재풀 검색 관련 */
	// 인재풀 검색 페이지로 이동
	@RequestMapping("/serachTalent")
	public String serachTalent(HttpSession session) {
		session.setAttribute("alertMsg", "오픈 준비중입니다!");
		return "business/businessMypage";
	}
	
}
