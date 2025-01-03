<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>회원가입 | 워크드림</title>
	<link rel="icon" href="${pageContext.request.contextPath}/img/logo_icon.png"/>
	
	<!-- 외부 css / js 연결 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>const contextPath = "${pageContext.request.contextPath}";</script>
    <script src="${pageContext.request.contextPath}/js/member.js" defer></script>
</head>
<body>
	<!-- 헤더 -->
	<header class="mini">
		<nav>
			<ul id="mini-menu-left">
				<li>
					<a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/img/logo_title.png" /></a>
				</li>
				<li>
					<h1>기업 회원가입</h1>
				</li>
			</ul>
			
			<ul id="mini-menu-right">
				<li>
					<a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/img/icon_home.png" /> 워크드림 홈</a>
				</li>
				<li>|</li>
				<li>
					<a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/img/icon_office.png" /> 기업 홈</a>
				</li>
			</ul>
		</nav>
	</header>
	
	<!-- 메인 -->
	<main id="registDetail-main">
		<h1>워크드림 통합 기업회원 가입</h1>
		
		<section>
			<form id="registDetail-form" class="Business" action="${pageContext.request.contextPath}/insert.me" method="post">
				<h2>기업회원</h2>
				<input type="hidden" name="userType" value="B" />
				
				<h6>사업자등록번호 (필수)</h6>
				<input type="text" id="registNumber" name="registrationNumber" maxlength="10" spellcheck="false" placeholder="사업자 등록번호 직접입력(10자리)" />
				<p><img src="${pageContext.request.contextPath}/img/icon_caution(sub).png" />사업자등록번호가 없으신가요? <a href="">사업자등록번호가 없을 때</a></p>
				<p id="number-check" class="NotChecked"></p>
				
				<h6>기업 인증</h6> 
				<div id="registDetail-form-file">
					<h3><strong>사업자등록증명원</strong>을 첨부해주세요</h3>
					<img src="${pageContext.request.contextPath}/img/sample.jpg">
					<label for="registProveImg">
						<input type="file" id="registProveImg" value="registProveImg" />
						<button type="button">파일 선택</button>
					</label>
					
					<label for="test">
						<input type="checkbox" id="test">
						<span></span>&ensp;다음에 인증
					</label>
				</div>
				<p><img src="${pageContext.request.contextPath}/img/icon_caution(sub).png" />사업자등록증명원이란?<a href="">사업자등록증명원 가이드라인</a></p>
				
				<h6>아이디 (필수)</h6>
				<input type="text" id="registId" name="userId" maxlength="20" spellcheck="false" placeholder="아이디 입력(4~20자리)" />
				<p><img src="${pageContext.request.contextPath}/img/icon_caution(sub).png" />영문 소문자, 숫자, 특수문자 '_'만 사용 가능</p>
				<p id="id-check" class="NotChecked"></p>
				
				<h6>비밀번호 (필수)</h6>
				<input type="password" id="registPwd" name="userPwd" maxlength="16" spellcheck="false" placeholder="비밀번호 입력(8~16자리)" />
				<p><img src="${pageContext.request.contextPath}/img/icon_caution(sub).png" />영문 대소문자, 숫자, 특수문자 조합</p>
				<p id="pwd-check" class="NotChecked"></p>
				
				<h6>비밀번호 확인 (필수)</h6>
				<input type="password" id="registRePwd" maxlength="16" spellcheck="false" placeholder="비밀번호 재입력" />
				<p id="rePwd-check" class="NotChecked"></p>
				
				<h6>이메일 (필수)</h6>
				<input type="email" id="registEmail" name="email" spellcheck="false" placeholder="email@workdream.co.kr" />
				<p><img src="${pageContext.request.contextPath}/img/icon_caution(sub).png" />기업서비스 및 서비스 관련 정보를 받을 때 필요합니다.</p>
				<p id="email-check" class="error"></p>
				
				<h6>휴대전화</h6>
				<input type="text" name="phone" id="registPhone" placeholder="'-'없이 숫자로만 입력" />
				
				
				<h6>약관</h6>
				<div id="registDetail-form-terms">
					<span class="bold">
						<label for="allCheckTerms">
							<input type="checkbox" id="allCheckTerms" />
							<span></span>&ensp;전체 동의
						</label>
					</span>
					<p>위치기반 서비스 이용약관(선택), 마케팅 정보 수신 동의(이메일, SMS/MMS)(선택) 동의를 포합합니다.</p>
					
					<hr />
					
					<span class="bold">
						<label for="terms-required1">
							<input type="checkbox" id="terms-required1" class="terms" required />
							<span></span>&ensp;(필수)기업회원 약관에 동의
						</label>
						<a href="#">&gt;</a>
					</span>
					<span class="bold">
						<label for="terms-required2">
							<input type="checkbox" id="terms-required2" class="terms" required />
							<span></span>&ensp;(필수)개인정보 수집 및 이용에 동의
						</label>
						<a href="#">&gt;</a>
					</span>
					<span class="bold">
						<label for="terms-required3">
							<input type="checkbox" id="terms-required3" class="terms" required />
							<span></span>&ensp;(필수)SMS 발송 서비스 약관에 동의
						</label>
						<a href="#">&gt;</a>
					</span>
					<span>
						<label for="terms-selection1">
							<input type="checkbox" id="terms-selection1" class="terms" />
							<span></span>&ensp;(선택)위치기반서비스 이용약관에 동의
						</label>
						<a href="#">&gt;</a>
					</span>
					<span>
						<label for="terms-selection2">
							<input type="checkbox" id="terms-selection2" class="terms" />
							<span></span>&ensp;(선택)마케팅 정보 수신 동의 - 이메일
						</label>
						<a href="#">&gt;</a>
					</span>
				</div>
				<p><img src="${pageContext.request.contextPath}/img/icon_caution(sub).png" />(필수)항목은 반드시 동의하셔야 회원가입이 가능합니다.</p>
				<p id="terms-check" class="NotChecked"></p>
				
				<h6>안내</h6>
				<div id="registDetail-form-info">
					<ul>
						<li>워크드림 통합회원으로 가입하시면 하나의 ID로 워크드림이 제공하는 모든 서비스를 이용하실 수 있습니다.</li>
					</ul>
				</div>
				
				<input type="button" id="btnRegist" value="회원가입" />
			</form>
		</section>
	</main>
</body>
</html>