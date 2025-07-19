package in.noteapp.spring.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.noteapp.spring.model.Category;
import in.noteapp.spring.model.User;
import in.noteapp.spring.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public Category createCategory(String name, User user) {
		Category category = new Category(name,user);
		return categoryRepository.save(category);
	}
	
	 public List<Category> getCategoriesByUser(User user) {
	        return categoryRepository.findByUser(user);
	    }
	 public void deleteCategory(Long id) {
		    categoryRepository.deleteById(id);
		}

}
