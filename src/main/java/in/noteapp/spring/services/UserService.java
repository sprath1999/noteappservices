package in.noteapp.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import in.noteapp.spring.model.User;
import in.noteapp.spring.repository.UserRepository;
import in.noteapp.spring.request.LoginRequest;
import in.noteapp.spring.request.RegisterRequest;
import in.noteapp.spring.response.LoginResponse;
import in.noteapp.spring.security.JwtUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;


	public String register(RegisterRequest request) {
		if(userRepository.existsByEmail(request.getEmail())) {
			return "User Already exists with the given mail id !";
		}
		User user = new User(request.getUsername(),request.getEmail(), passwordEncoder.encode(request.getPassword()));
		userRepository.save(user);
		
		return "User Registered Successfully!";
	}
	
	public LoginResponse login(LoginRequest request) {
	    try {
	        System.out.println("Attempting login for email: " + request.getEmail());

	        Authentication auth = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

	        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
	        String token = jwtUtil.generateToken(user.getEmail());

	        return new LoginResponse(token);
	    } catch (AuthenticationException ex) {
	      
	        throw new RuntimeException("Invalid email or password");
	    } catch (Exception e) {
	      
	        throw new RuntimeException("Something went wrong: " + e.getMessage());
	    }
	}

}
