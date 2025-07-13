package in.noteapp.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import in.noteapp.spring.model.Category;
import in.noteapp.spring.model.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
