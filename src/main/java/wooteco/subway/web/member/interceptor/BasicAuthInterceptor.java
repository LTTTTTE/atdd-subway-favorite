package wooteco.subway.web.member.interceptor;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Component
public class BasicAuthInterceptor implements HandlerInterceptor {
	private AuthorizationExtractor authExtractor;
	private MemberService memberService;

	public BasicAuthInterceptor(AuthorizationExtractor authExtractor, MemberService memberService) {
		this.authExtractor = authExtractor;
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String basic = authExtractor.extract(request, "Basic");
		String decode = new String(Base64.getDecoder().decode(basic));
		String[] split = decode.split(":");

		String email = split[0].trim();
		String password = split[1].trim();

		Member member = memberService.findMemberByEmail(email);
		if (!member.checkPassword(password)) {
			throw new InvalidAuthenticationException("올바르지 않은 이메일과 비밀번호 입력");
		}

		request.setAttribute("loginMemberEmail", email);
		return true;
	}
}
