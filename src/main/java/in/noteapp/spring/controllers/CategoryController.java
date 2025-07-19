package in.noteapp.spring.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.noteapp.spring.model.Category;
import in.noteapp.spring.model.User;
import in.noteapp.spring.repository.CategoryRepository;
import in.noteapp.spring.repository.UserRepository;
import in.noteapp.spring.security.JwtUtil;
import in.noteapp.spring.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired 
    private JwtUtil jwtUtil;

  
  
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(
        @RequestParam String name,
        HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        token = token.substring(7);
        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        Category category = new Category(name, user);
        Category saved = categoryRepository.save(category);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getUserCategories(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        token = token.substring(7);
        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        List<Category> categories = categoryRepository.findByUser(user);

        return ResponseEntity.ok(categories);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
