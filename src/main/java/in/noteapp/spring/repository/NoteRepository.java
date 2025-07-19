package in.noteapp.spring.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import in.noteapp.spring.model.Note;
import in.noteapp.spring.model.Category;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCategory(Category category);
    List<Note> findByCategoryId(Long categoryId);
}
