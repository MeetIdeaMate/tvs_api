package com.techlambdas.delearmanagementapp.config;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.techlambdas.delearmanagementapp.exception.TokenExpiredException;
import com.techlambdas.delearmanagementapp.response.AppResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils)
    {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtils.extractUsername(jwt));
            if (jwt != null && jwtUtils.validateJwtToken(jwt,userDetails)) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (ExpiredJwtException e) {
            HttpServletResponse servletResponse=handleException(response,e);
            return;
        }

        catch (MalformedJwtException e) {
            HttpServletResponse servletResponse=handleException(response,e);
            return;
        }
        catch (Exception e) {
            logger.error("Cannot set user authentication: {}");
        }

        filterChain.doFilter(request, response);
    }

    private HttpServletResponse handleException(HttpServletResponse response, Exception e) throws IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("message", "Invalid Token OR Expired Token:"+e.getMessage());
        errorDetails.put("result", null);
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        String jsonResponse = new Gson().toJson(responseEntity.getBody());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"result\":null,\"statusCode\":401,\"error\":" + new Gson().toJson(new JsonParser().parse(jsonResponse)) + "}");
        response.getWriter().flush();
        return response;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
