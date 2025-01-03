package com.cs.workdream.board.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cs.workdream.board.model.vo.Board;
import com.cs.workdream.board.model.vo.Reply;
import com.cs.workdream.board.service.BoardService;
import com.cs.workdream.member.model.vo.Member;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // �뚣끇占썬끇�빍占쎈뼒 占쎌냳 占쎈읂占쎌뵠筌욑옙 占쎈ご占쎈뻻  skld;sad
    @GetMapping("/communityHome")
    public String showCommunityHome() {
        return "board/communityHome"; // communityHome.jsp sdsd
    }

    // 野껊슣�뻻疫뀐옙 占쎌삂占쎄쉐 占쎈읂占쎌뵠筌욑옙 占쎈ご占쎈뻻�뀏�뀒�뀓�뀕�뀓�뀒���뀓�꽩�뀒�꽴
    @GetMapping("/communityPost")
    public String showCommunityPost() {
        return "board/communityPost"; // communityPost.jsp
    }

    // 野껊슣�뻻疫뀐옙 癰귣떯由� 占쎈읂占쎌뵠筌욑옙 占쎈ご占썬뀋�꽩�꽮�꽩�뀋�꽮�뀋�뻻
    @GetMapping("/communityView")
    public String showCommunityView(@RequestParam("postId") int postId, Model model, HttpSession session) {
        // 野껊슣�뻻疫뀐옙 鈺곌퀬�돳 (筌욊낮龜 燁삳똾�믤�⑥쥓�봺 占쎈７占쎈맙)
        Board post = boardService.getPostWithJobCategories(postId);
        if(post != null && "Y".equals(post.getStatus())) { // getStatus() 筌롫뗄苑뚳옙諭� 鈺곕똻�삺 占쎌넇占쎌뵥
            // 鈺곌퀬�돳占쎈땾 筌앹빓占�
            boardService.increaseViewCount(postId);

            // 筌뤴뫀�쑞占쎈퓠 野껊슣�뻻疫뀐옙 �빊遺쏙옙
            model.addAttribute("post", post);

            // 占쎌겱占쎌삺 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 (嚥≪뮄�젃占쎌뵥 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙)
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser != null) {
                model.addAttribute("currentUser", currentUser);

                // 占쎄텢占쎌뒠占쎌쁽揶쏉옙 占쎌뵠 野껊슣�뻻疫뀐옙占쎈퓠 �⑤벀而㏆옙六쏙옙�뮉筌욑옙 占쎈연�겫占� 占쎈솇占쎈뼊 嚥≪뮇彛� �빊遺쏙옙 揶쏉옙占쎈뮟
                boolean userLikedPost = false; // 占쎈뼄占쎌젫 �⑤벀而� 占쎈연�겫占� 占쎈솇占쎈뼊 嚥≪뮇彛� �뤃�뗭겱 占쎈툡占쎌뒄
                model.addAttribute("userLikedPost", userLikedPost);
            } else {
                model.addAttribute("userLikedPost", false);
            }

            return "board/communityView"; // communityView.jsp
        } else {
            model.addAttribute("errorMsg", "野껊슣�뻻疫뀐옙占쎌뱽 筌≪뼚�뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
            return "common/errorPage"; // 占쎈퓠占쎌쑎 占쎈읂占쎌뵠筌욑옙
        }
    }
    
    // 野껊슣�뻻疫뀐옙 占쎈땾占쎌젟 占쎈읂占쎌뵠筌욑옙 占쎈ご占쎈뻻
    @GetMapping("/editPost")
    public String showEditPost(@RequestParam("postId") int postId, Model model, HttpSession session) {
        // 野껊슣�뻻疫뀐옙 鈺곌퀬�돳
        Board post = boardService.getPostWithJobCategories(postId);
        if(post != null && "Y".equals(post.getStatus())) {
            // 占쎌겱占쎌삺 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귙뀒�뀏�뀒�뀑�뀏占�
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser != null && post.getUserNo() == currentUser.getUserNo()) {
                model.addAttribute("post", post);
                return "board/editPost"; // editPost.jsp
            } else {
                model.addAttribute("errorMsg", "野껊슣�뻻疫뀐옙占쎌뱽 占쎈땾占쎌젟占쎈막 亦낅슦釉놂옙�뵠 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
                return "common/errorPage";
            }
        } else {
            model.addAttribute("errorMsg", "野껊슣�뻻疫뀐옙占쎌뱽 筌≪뼚�뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
            return "common/errorPage"; // 占쎈퓠占쎌쑎 占쎈읂占쎌뵠筌욑옙
        }
    }

    // �뚣끇占썬끇�빍占쎈뼒 野껊슣�뻻占쎈솇 筌뤴뫖以� 占쎈읂占쎌뵠筌욑옙 占쎈ご占쎈뻻
    @GetMapping("/communityList")
    public String showCommunityList(@RequestParam(value="category", defaultValue="占쎌읈筌ｋ떯占�") String category, Model model) {
        model.addAttribute("category", category);
        return "board/communityList"; // communityList.jsp
    }

    // 占쎌뿫占쎈뻻 嚥≪뮄�젃占쎌뵥 占쎈퓦占쎈굡占쎈７占쎌뵥占쎈뱜 (揶쏆뮆而삼옙�뒠)
    @GetMapping("/testLogin")
    public String testLogin(HttpSession session) {
        Member mockUser = new Member();
        mockUser.setUserNo(1); // 占쎈뼄占쎌젫 占쎄텢占쎌뒠占쎌쁽 甕곕뜇�깈嚥∽옙 癰귨옙野껓옙
        mockUser.setUserId("testuser123"); // userId 占쎄퐬占쎌젟
        // 占쎈툡占쎌뒄占쎈립 占쎈뼄�몴占� 占쎈툡占쎈굡占쎈즲 占쎄퐬占쎌젟
        session.setAttribute("loginUser", mockUser);
        return "redirect:/resume/enrollresume"; // 嚥≪뮄�젃占쎌뵥 占쎌뜎 �뵳�됰뼄占쎌뵠占쎌젂占쎈뱜占쎈막 占쎈읂占쎌뵠筌욑옙
    }
    
    // 筌�袁⑹뒠�⑤벀�э쭗�뫖以� 筌띾벏釉�
    @GetMapping("/listOfJobOpening")
    public String showJobOpeningList() {
        return "board/listOfJobOpening"; // listOfJobOpening.jsp
    }
    
    // -------------------
    // REST API �꽴占쏙옙�졃 筌롫뗄苑뚳옙諭�
    // -------------------

    // 野껊슣�뻻疫뀐옙 占쎈땾 鈺곌퀬�돳 (REST API)
    @GetMapping("/api/postCount")
    @ResponseBody
    public Map<String, Object> getPostCount() {
        int count = boardService.countPosts("占쎌읈筌ｋ떯占�");
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        return response;
    }

    // 占쎌뵥疫뀐옙 野껊슣�뻻疫뀐옙 鈺곌퀬�돳 (REST API)
    @GetMapping("/api/popularPosts")
    @ResponseBody
    public Map<String, Object> getPopularPosts() {
        List<Board> posts = boardService.getPopularPosts();
        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        return response;
    }

    // 燁삳똾�믤�⑥쥓�봺 獄쏉옙 占쎈툡占쎄숲占쎈퓠 占쎈뎡�몴占� 野껊슣�뻻疫뀐옙 鈺곌퀬�돳 (REST API)
    @GetMapping("/api/posts")
    @ResponseBody
    public Map<String, Object> getPosts(
            @RequestParam(value="category", defaultValue="占쎌읈筌ｋ떯占�") String category,
            @RequestParam(value="filter", defaultValue="筌ㅼ뮇�뻿占쎈떄") String filter,
            @RequestParam(value="jobFilter", required=false) String jobFilter,
            @RequestParam(value="offset", defaultValue="0") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {

        List<Board> posts = boardService.getFilteredPosts(category, filter, jobFilter, offset, limit);
        int totalCount = boardService.countFilteredPosts(category, filter, jobFilter);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("totalCount", totalCount);
        return response;
    }

    // 占쎄퉱嚥≪뮇�뒲 野껊슣�뻻疫뀐옙 占쎄문占쎄쉐 (REST API)
    @PostMapping("/api/posts")
    @ResponseBody
    public Map<String, Object> createPost(
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value="image", required=false) MultipartFile image,
            @RequestParam(value="jobs", required=false) List<String> jobs,
            @RequestParam(value="hashtags", required=false) List<String> hashtags,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // 占쎌겱占쎌삺 嚥≪뮄�젃占쎌뵥占쎈립 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 揶쏉옙占쎌죬占쎌궎疫뀐옙
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }
            int userNo = currentUser.getUserNo();
            String author = currentUser.getUserId(); // 占쎈툡占쎌뒄占쎈퓠 占쎈뎡占쎌뵬 癰귨옙野껓옙

            // 占쎌뵠沃섎챷占� 占쎈씜嚥≪뮆諭� 筌ｌ꼶�봺
            String imagePath = null;
            if(image != null && !image.isEmpty()) {
                // �⑥쥙�젟占쎈쭆 占쎈씜嚥≪뮆諭� 占쎈탵占쎌젂占쎈꽅�뵳占� 占쎄퐬占쎌젟
                String uploadDir = "C:/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // 占쎈탵占쎌젂占쎈꽅�뵳占� 占쎄문占쎄쉐
                }
                String originalFilename = image.getOriginalFilename();
                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
                File dest = new File(dir, uniqueFilename);
                image.transferTo(dest);
                imagePath = uniqueFilename; // 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬筌뤿굝彛� 占쏙옙占쎌삢

                // 占쎈씜嚥≪뮆諭� 野껋럥以� 嚥≪뮄�젃 �빊�뮆�젾
                System.out.println("Image uploaded to: " + dest.getAbsolutePath());
            }

            // Board 揶쏆빘猿� 占쎄문占쎄쉐
            Board board = new Board();
            board.setCategory(category);
            board.setTitle(title);
            board.setContent(content);
            board.setImagePath(imagePath);
            board.setAuthor(author);
            board.setUserNo(userNo);
            board.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            board.setViewCount(0);
            board.setLikeCount(0);
            board.setHashtags(hashtags);
            board.setJobCategories(jobs);
            board.setStatus("Y"); // 野껊슣�뻻疫뀐옙 占쎄맒占쎄묶 占쎄퐬占쎌젟

            // 野껊슣�뻻疫뀐옙 占쎄땜占쎌뿯
            int result = boardService.createPost(board);
            if(result > 0) {
                response.put("status", "success");
                response.put("postId", board.getPostingNo());
            } else {
                response.put("status", "fail");
                response.put("message", "野껊슣�뻻疫뀐옙 占쎈쾻嚥≪빘肉� 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
        } catch(IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎌뵠沃섎챷占� 占쎈씜嚥≪뮆諭� 餓ο옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄: " + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄: " + e.getMessage());
        }

        return response;
    }

    // 野껊슣�뻻疫뀐옙 占쎄텣占쎌젫 API (REST API)
    @DeleteMapping("/api/posts/{postingNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePost(
            @PathVariable("postingNo") int postingNo,
            HttpSession session) { // 占쎄쉭占쎈�∽옙�뱽 占쎈꽰占쎈퉸 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 揶쏉옙占쎌죬占쎌궎疫뀐옙

        Map<String, Object> response = new HashMap<>();

        // 占쎌겱占쎌삺 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 揶쏉옙占쎌죬占쎌궎疫뀐옙 (占쎄쉭占쎈�∽옙�뱽 占쎈꽰占쎈퉸)
        Member currentUser = (Member) session.getAttribute("loginUser");
        if(currentUser == null) {
            response.put("status", "fail");
            response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        int currentUserNo = currentUser.getUserNo();

        // 野껊슣�뻻疫뀐옙 占쎌젟癰귨옙 鈺곌퀬�돳
        Board post = boardService.getPost(postingNo);
        if (post == null || !"Y".equals(post.getStatus())) {
            response.put("status", "fail");
            response.put("message", "野껊슣�뻻疫뀐옙占쎌뱽 筌≪뼚�뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // 占쎌삂占쎄쉐占쎌쁽 占쎌넇占쎌뵥
        if (post.getUserNo() != currentUserNo) {
            response.put("status", "fail");
            response.put("message", "癰귣챷�뵥占쎌벥 野껊슣�뻻疫뀐옙筌랃옙 占쎄텣占쎌젫占쎈막 占쎈땾 占쎌뿳占쎈뮸占쎈빍占쎈뼄.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // 野껊슣�뻻疫뀐옙 占쎄텣占쎌젫
        int result = boardService.deletePost(postingNo);
        if (result > 0) {
            response.put("status", "success");
            response.put("message", "野껊슣�뻻疫뀐옙占쎌뵠 占쎄텣占쎌젫占쎈┷占쎈�占쎈뮸占쎈빍占쎈뼄.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "fail");
            response.put("message", "野껊슣�뻻疫뀐옙 占쎄텣占쎌젫占쎈퓠 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 野껊슣�뻻疫뀐옙 鈺곌퀬�돳 API (REST API)
    @GetMapping("/api/posts/{postingNo}")
    @ResponseBody
    public ResponseEntity<Board> getPost(@PathVariable("postingNo") int postingNo) {
        Board post = boardService.getPostWithJobCategories(postingNo);
        if (post == null || !"Y".equals(post.getStatus())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // 鈺곌퀬�돳占쎈땾 筌앹빓占�
        boardService.increaseViewCount(postingNo);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // 野껊슣�뻻疫뀐옙 占쎈땾占쎌젟 API (REST API)
    @PostMapping("/api/posts/{postingNo}/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePost(
            @PathVariable("postingNo") int postingNo,
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value="image", required=false) MultipartFile image,
            @RequestParam(value="jobs", required=false) List<String> jobs,
            @RequestParam(value="hashtags", required=false) List<String> hashtags,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 占쎌겱占쎌삺 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 揶쏉옙占쎌죬占쎌궎疫뀐옙 (嚥≪뮄�젃占쎌뵥 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙)
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            int userNo = currentUser.getUserNo();

            // 野껊슣�뻻疫뀐옙 占쎌젟癰귨옙 鈺곌퀬�돳
            Board existingPost = boardService.getPostWithJobCategories(postingNo);
            if(existingPost == null || !"Y".equals(existingPost.getStatus())) {
                response.put("status", "fail");
                response.put("message", "野껊슣�뻻疫뀐옙占쎌뱽 筌≪뼚�뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // 占쎌삂占쎄쉐占쎌쁽 占쎌넇占쎌뵥
            if(existingPost.getUserNo() != userNo) {
                response.put("status", "fail");
                response.put("message", "癰귣챷�뵥占쎌벥 野껊슣�뻻疫뀐옙筌랃옙 占쎈땾占쎌젟占쎈막 占쎈땾 占쎌뿳占쎈뮸占쎈빍占쎈뼄.");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            // 占쎌뵠沃섎챷占� 占쎈씜嚥≪뮆諭� 筌ｌ꼶�봺 (疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎌�筌욑옙 占쎌굢占쎈뮉 占쎄퉱嚥≪뮇�뒲 占쎌뵠沃섎챷占썸에占� 占쏙옙筌ｏ옙)
            String imagePath = existingPost.getImagePath(); // 疫꿸퀣�� 占쎌뵠沃섎챷占� 野껋럥以� 占쎌�筌욑옙
            if(image != null && !image.isEmpty()) {
                // �⑥쥙�젟占쎈쭆 占쎈씜嚥≪뮆諭� 占쎈탵占쎌젂占쎈꽅�뵳占� 占쎄퐬占쎌젟
                String uploadDir = "C:/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // 占쎈탵占쎌젂占쎈꽅�뵳占� 占쎄문占쎄쉐
                }
                String originalFilename = image.getOriginalFilename();
                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
                File dest = new File(dir, uniqueFilename);
                image.transferTo(dest);
                imagePath = uniqueFilename; // 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬筌뤿굝彛� 占쏙옙占쎌삢

                // 疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬 占쎄텣占쎌젫 (占쎄퐨占쎄문 占쎄텢占쎈퉮)
                if(existingPost.getImagePath() != null) {
                    File oldImage = new File("C:/uploads/" + existingPost.getImagePath());
                    if(oldImage.exists()) {
                        oldImage.delete();
                        System.out.println("Deleted old image: " + oldImage.getAbsolutePath());
                    }
                }

                // 占쎈씜嚥≪뮆諭� 野껋럥以� 嚥≪뮄�젃 �빊�뮆�젾
                System.out.println("Image updated to: " + dest.getAbsolutePath());
            }

            // Board 揶쏆빘猿� 占쎄문占쎄쉐 獄쏉옙 占쎈씜占쎈쑓占쎌뵠占쎈뱜占쎈막 占쎈툡占쎈굡 占쎄퐬占쎌젟
            Board board = new Board();
            board.setPostingNo(postingNo);
            board.setCategory(category);
            board.setTitle(title);
            board.setContent(content);
            board.setImagePath(imagePath);
            board.setUserNo(userNo);
            board.setJobCategories(jobs);
            board.setHashtags(hashtags);
            board.setStatus("Y"); // 占쎄맒占쎄묶 占쎌�筌욑옙

            // 野껊슣�뻻疫뀐옙 占쎈씜占쎈쑓占쎌뵠占쎈뱜
            int result = boardService.updatePost(board);
            if(result > 0) {
                response.put("status", "success");
                response.put("message", "野껊슣�뻻疫뀐옙占쎌뵠 占쎈땾占쎌젟占쎈┷占쎈�占쎈뮸占쎈빍占쎈뼄.");
                response.put("postId", postingNo);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "fail");
                response.put("message", "野껊슣�뻻疫뀐옙 占쎈땾占쎌젟占쎈퓠 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch(IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎌뵠沃섎챷占� 占쎈씜嚥≪뮆諭� 餓ο옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/api/posts/{postingNo}/like")
    @ResponseBody
    public Map<String, Object> likePost(@PathVariable("postingNo") int postingNo, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 嚥≪뮄�젃占쎌뵥 占쎈연�겫占� 占쎌넇占쎌뵥
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }

            // �⑤벀而� 嚥≪뮇彛�
            boolean success = boardService.likePost(postingNo, currentUser.getUserNo());

            if(success) {
                response.put("status", "success");
                response.put("message", "�⑤벀而㏆옙六쏙옙�뮸占쎈빍占쎈뼄.");
            } else {
                response.put("status", "fail");
                response.put("message", "占쎌뵠沃섓옙 �⑤벀而㏆옙六썲쳞怨뺢돌, �⑤벀而㏆옙肉� 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
        }
        return response;
    }

    /* 野껊슣�뻻疫뀐옙 �⑤벀而� �뿆�뫁�꺖 API */
    @PostMapping("/api/posts/{postingNo}/unlike")
    @ResponseBody
    public Map<String, Object> unlikePost(@PathVariable("postingNo") int postingNo, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 嚥≪뮄�젃占쎌뵥 占쎈연�겫占� 占쎌넇占쎌뵥
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }

            // �⑤벀而� �뿆�뫁�꺖 嚥≪뮇彛�
            boolean success = boardService.unlikePost(postingNo, currentUser.getUserNo());

            if(success) {
                response.put("status", "success");
                response.put("message", "�⑤벀而㏆옙�뱽 �뿆�뫁�꺖占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            } else {
                response.put("status", "fail");
                response.put("message", "�⑤벀而㏆옙�뱽 占쎈릭筌욑옙 占쎈륫占쎈릭椰꾧퀡援�, �뿆�뫁�꺖占쎈퓠 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
        }
        return response;
    }

    /* 占쎈솊疫뀐옙 �⑤벀而� API */
    @PostMapping("/api/replies/{replyNo}/like")
    @ResponseBody
    public Map<String, Object> likeReply(@PathVariable("replyNo") int replyNo, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 嚥≪뮄�젃占쎌뵥 占쎈연�겫占� 占쎌넇占쎌뵥
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }

            // �⑤벀而� 嚥≪뮇彛�
            boolean success = boardService.likeReply(replyNo, currentUser.getUserNo());

            if(success) {
                response.put("status", "success");
                response.put("message", "�⑤벀而㏆옙六쏙옙�뮸占쎈빍占쎈뼄.");
            } else {
                response.put("status", "fail");
                response.put("message", "占쎌뵠沃섓옙 �⑤벀而㏆옙六썲쳞怨뺢돌, �⑤벀而㏆옙肉� 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
        }
        return response;
    }

    /* 占쎈솊疫뀐옙 �⑤벀而� �뿆�뫁�꺖 API */
    @PostMapping("/api/replies/{replyNo}/unlike")
    @ResponseBody
    public Map<String, Object> unlikeReply(@PathVariable("replyNo") int replyNo, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 嚥≪뮄�젃占쎌뵥 占쎈연�겫占� 占쎌넇占쎌뵥
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }

            // �⑤벀而� �뿆�뫁�꺖 嚥≪뮇彛�
            boolean success = boardService.unlikeReply(replyNo, currentUser.getUserNo());

            if(success) {
                response.put("status", "success");
                response.put("message", "�⑤벀而㏆옙�뱽 �뿆�뫁�꺖占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            } else {
                response.put("status", "fail");
                response.put("message", "�⑤벀而㏆옙�뱽 占쎈릭筌욑옙 占쎈륫占쎈릭椰꾧퀡援�, �뿆�뫁�꺖占쎈퓠 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
        }
        return response;
    }

    
    // 占쎈뱟占쎌젟 野껊슣�뻻疫뀐옙占쎌벥 占쎈솊疫뀐옙 鈺곌퀬�돳 (REST API)
    @GetMapping("/api/replies")
    @ResponseBody
    public Map<String, Object> getReplies(@RequestParam("postId") int postId) {
        List<Reply> replies = boardService.getReplies(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("replies", replies);
        return response;
    }

    // 占쎈솊疫뀐옙 �빊遺쏙옙 (REST API)
    @PostMapping("/api/replies")
    @ResponseBody
    public Map<String, Object> addReply(@RequestBody Reply reply, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 占쎄쉭占쎈�∽옙肉됵옙苑� 占쎌겱占쎌삺 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 揶쏉옙占쎌죬占쎌궎疫뀐옙
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }
            int userNo = currentUser.getUserNo();
            reply.setUserNo(userNo);
            // author 占쎈툡占쎈굡占쎈뮉 MyBatis selectReplies占쎈퓠占쎄퐣 JOIN占쎌뱽 占쎈꽰占쎈퉸 揶쏉옙占쎌죬占쎌궎疫뀐옙 占쎈르�눧紐꾨퓠 占쎄퐣甕곌쑴肉됵옙苑� 占쎄퐬占쎌젟占쎈막 占쎈툡占쎌뒄 占쎈씨占쎌벉

            int result = boardService.addReply(reply);
            if(result > 0) {
                response.put("status", "success");
            } else {
                response.put("status", "fail");
                response.put("message", "占쎈솊疫뀐옙 占쎈쾻嚥≪빘肉� 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
            return response;
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            return response;
        }
    }   
    
    // 占쎈솊疫뀐옙 占쎄텣占쎌젫 (REST API)
    @DeleteMapping("/api/replies/{replyNo}")
    @ResponseBody
    public Map<String, Object> deleteReply(@PathVariable("replyNo") int replyNo, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 占쎄쉭占쎈�∽옙肉됵옙苑� 占쎌겱占쎌삺 占쎄텢占쎌뒠占쎌쁽 占쎌젟癰귨옙 揶쏉옙占쎌죬占쎌궎疫뀐옙
            Member currentUser = (Member) session.getAttribute("loginUser");
            if(currentUser == null) {
                response.put("status", "fail");
                response.put("message", "嚥≪뮄�젃占쎌뵥占쎌뵠 占쎈툡占쎌뒄占쎈�占쎈빍占쎈뼄.");
                return response;
            }

            // 占쎈솊疫뀐옙 占쎈꺖占쎌�占쎌쁽 占쎌넇占쎌뵥
            Reply existingReply = boardService.getReplyById(replyNo);
            if(existingReply == null || existingReply.getUserNo() != currentUser.getUserNo()) {
                response.put("status", "fail");
                response.put("message", "亦낅슦釉놂옙�뵠 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
                return response;
            }

            int result = boardService.deleteReply(replyNo);
            if(result > 0) {
                response.put("status", "success");
            } else {
                response.put("status", "fail");
                response.put("message", "占쎈솊疫뀐옙 占쎄텣占쎌젫占쎈퓠 占쎈뼄占쎈솭占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "占쎄퐣甕곤옙 占쎌궎�몴�꼵占� 獄쏆뮇源�占쎈뻥占쎈뮸占쎈빍占쎈뼄.");
        }
        return response;
    }

    // 占쎌뵠沃섎챷占� 占쎄퐣�뜮�늿�뱽 占쎌맄占쎈립 �뚢뫂�뱜嚥▲끇�쑎 筌롫뗄苑뚳옙諭� �빊遺쏙옙
    @GetMapping("/uploads/{filename}")
    @ResponseBody
    public ResponseEntity<byte[]> serveImage(@PathVariable("filename") String filename) {
        try {
            // �⑥쥙�젟占쎈쭆 占쎈씜嚥≪뮆諭� 占쎈탵占쎌젂占쎈꽅�뵳占� 占쎄퐬占쎌젟
            String uploadDir = "C:/uploads/";
            File imageFile = new File(uploadDir, filename);
            if (!imageFile.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String mimeType = Files.probeContentType(imageFile.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch(IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 占쎌젫筌뤴뫗�몵嚥∽옙 野껊슣�뻻疫뀐옙 野껓옙占쎄퉳 (REST API)
     * GET /board/api/searchPosts?title=野껓옙占쎄퉳占쎈선&offset=0&limit=10
     */
    @GetMapping("/api/searchPosts")
    @ResponseBody
    public Map<String, Object> searchPosts(
            @RequestParam("title") String title,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        List<Board> posts = boardService.searchPostsByTitle(title, offset, limit);
        int totalCount = boardService.countSearchPostsByTitle(title);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("totalCount", totalCount);
        return response;
    }

}
