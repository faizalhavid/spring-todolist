package faizal.project.todo_list.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import faizal.project.todo_list.helpers.JWTHelper;
import faizal.project.todo_list.services.AccountDetailsService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter{
    private final AccountDetailsService accountDetailsService;
    private final ObjectMapper objectMapper;

    public JWTAuthFilter(AccountDetailsService accountDetailsService, ObjectMapper objectMapper) {
        this.accountDetailsService = accountDetailsService;
        this.objectMapper = objectMapper;
    }
    @Override

protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    try{
        String authHeader = request.getHeader("Authorization");
        String token = null, username = null;
        System.out.println("Auth Header: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = JWTHelper.extractUsername(token);
        }
        //      If the accessToken is null. It will pass the request to next filter in the chain.
        //      Any login and signup requests will not have jwt token in their header, therefore they will be passed to next filter chain.
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //        if any accessToken is present,then it will validate the token and then authenticate the request in security context
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = accountDetailsService.loadUserByUsername(username);
            if(JWTHelper.validateToken(token, userDetails)){
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            }
        }
        filterChain.doFilter(request, response);


} catch (AccessDeniedException e){
    ApiResponse<String> apiResponse = new ApiResponse<>("error", e.getMessage(), null);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write(toJson(apiResponse));
}

    }
    private String toJson(ApiResponse<String> apiResponse) {
        try{
            return objectMapper.writeValueAsString(apiResponse);
        } catch (IOException e){
            return "";
        }
    }
}
