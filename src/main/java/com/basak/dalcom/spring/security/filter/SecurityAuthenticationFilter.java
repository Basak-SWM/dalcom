package com.basak.dalcom.spring.security.filter;

import com.basak.dalcom.spring.security.service.DalcomUserDetails;
import com.basak.dalcom.spring.security.service.UserDetailsServiceImpl;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        // Set session httpOnly, sameSite, secure
        session.setAttribute("secure", true);
        session.setAttribute("sameSite", "None");

        ResponseCookie cookie = ResponseCookie.from("YUBIN", "STUPID")
            .path("/")
            .sameSite("None")
            .httpOnly(false)
            .secure(false)
            .maxAge(60 * 60 * 24 * 30)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());

        if (!session.isNew()) {
            String userId = (String) session.getAttribute("userId");
            if (userId != null) {
                DalcomUserDetails authentication = userDetailsService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(authentication, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
