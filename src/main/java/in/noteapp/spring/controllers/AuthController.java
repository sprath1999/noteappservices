package in.noteapp.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.noteapp.spring.request.LoginRequest;
import in.noteapp.spring.request.RegisterRequest;
import in.noteapp.spring.response.LoginResponse;
import in.noteapp.spring.services.UserService;

@RestController
@RequestMapping
public class AuthController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request){
		String response= userService.register(request);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
