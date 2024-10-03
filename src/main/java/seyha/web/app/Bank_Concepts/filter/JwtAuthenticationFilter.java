package seyha.web.app.Bank_Concepts.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Performs the actual filtering of the request.
     *
     * @param request The incoming request.
     * @param response The response to be sent.
     * @param filterChain The filter chain to be executed after this filter.
     * @throws ServletException If an error occurs during the filtering process.
     * @throws IOException If an error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull  HttpServletResponse response,
            @NonNull  FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = jwtToken.startsWith("Bearer ") ? jwtToken.substring(7) : jwtToken;
        String subject = jwtService.extractSubject(jwtToken);
        User user = (User) userDetailsService.loadUserByUsername(subject);
        var context = SecurityContextHolder.getContext();
        if(user!=null && context.getAuthentication()==null) {
            var AuthenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            context.setAuthentication(AuthenticationToken);
        }

        filterChain.doFilter(request,response);


    }
}
