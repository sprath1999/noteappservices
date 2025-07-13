package in.noteapp.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.noteapp.spring.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}
