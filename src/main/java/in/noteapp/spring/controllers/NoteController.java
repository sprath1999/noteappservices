package in.noteapp.spring.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.noteapp.spring.model.Note;
import in.noteapp.spring.request.NoteRequest;
import in.noteapp.spring.security.JwtUtil;
import in.noteapp.spring.services.NoteService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

	 @Autowired
	    private NoteService noteService;

	    @PostMapping("/create")
	    public ResponseEntity<Note> createNote(@ModelAttribute NoteRequest noteRequest) {
	        Note savedNote = noteService.createNote(noteRequest);
	        return ResponseEntity.ok(savedNote);
	    }
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
	        Optional<Note> note = noteService.getNoteById(id);
	        if (note.isPresent()) {
	            return ResponseEntity.ok(note.get());
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
	        boolean deleted = noteService.deleteNoteById(id);
	        if (deleted) {
	            return ResponseEntity.ok().body("Note deleted successfully");
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    @PutMapping("/update/{id}")
	    public ResponseEntity<Note> updateNote(@PathVariable Long id, @ModelAttribute NoteRequest request) {
	        Note updatedNote = noteService.updateNote(id, request);
	        return ResponseEntity.ok(updatedNote);
	    }
	    @GetMapping("/search")
	    public ResponseEntity<List<Note>> searchNotes(
	            @RequestParam(required = false) String title,
	            @RequestParam Long categoryId) {

	        List<Note> notes = noteService.searchNotesByTitleAndCategory(title, categoryId);
	        return ResponseEntity.ok(notes);
	    }

}
