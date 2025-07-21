package in.noteapp.spring.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.noteapp.spring.model.Category;
import in.noteapp.spring.model.Note;
import in.noteapp.spring.model.User;
import in.noteapp.spring.repository.CategoryRepository;
import in.noteapp.spring.repository.NoteRepository;
import in.noteapp.spring.repository.UserRepository;
import in.noteapp.spring.request.NoteRequest;
import in.noteapp.spring.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteService {
	@Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
//    private final String uploadDir = "uploads/";
    private final String uploadDir = System.getProperty("user.home") + "/NoteUploads/";


    public Note createNote(NoteRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String filePath = null;
        MultipartFile file = request.getDocument();

        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                File dest = new File(uploadPath, fileName);
                file.transferTo(dest);

                filePath = fileName;
            } catch (IOException e) {
                throw new RuntimeException("File upload failed", e);
            }
        }

        Note note = new Note(
                request.getTitle(),
                request.getContent(),
                LocalDateTime.now(),
                filePath,
                category
        );

        return noteRepository.save(note);
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
    public boolean deleteNoteById(Long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            noteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    public Note updateNote(Long id, NoteRequest request) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        existingNote.setTitle(request.getTitle());
        existingNote.setContent(request.getContent());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingNote.setCategory(category);

        MultipartFile file = request.getDocument();
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs(); // ✅ Ensure the folder exists
                }

                File dest = new File(uploadPath, fileName);
                file.transferTo(dest);

                existingNote.setDocumentPath(fileName);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed", e);
            }
        }

        return noteRepository.save(existingNote);
    }
    
    public List<Note> searchNotesByTitleAndCategory(String title, Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }

        if (title != null && !title.trim().isEmpty()) {
            return noteRepository.findByTitleContainingIgnoreCaseAndCategoryId(title, categoryId);
        } else {
            return noteRepository.findByCategoryId(categoryId);
        }
    }
    
}
