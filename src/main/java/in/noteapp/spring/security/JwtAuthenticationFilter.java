package in.noteapp.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.noteapp.spring.model.User;
import in.noteapp.spring.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override 
	
	public void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException,IOException {
		
		System.out.println(">> Incoming request path: " + request.getServletPath());

		  String path = request.getServletPath();
	      if (path.equals("/register") || path.equals("/login")) {
	    	  System.out.println(">> Skipping JWT filter for: " + path);

	          filterChain.doFilter(request, response);
	          return;
        }
	      
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
		
		
		 String token = authHeader.substring(7);

	        String userEmail = jwtUtil.extractUsername(token);

	        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	            User user = userRepository.findByEmail(userEmail).orElse(null);

	            if (user != null && jwtUtil.validateToken(token)) {

	                UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(user, null, null);

	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }
	        filterChain.doFilter(request, response);
		
	}
	
}
