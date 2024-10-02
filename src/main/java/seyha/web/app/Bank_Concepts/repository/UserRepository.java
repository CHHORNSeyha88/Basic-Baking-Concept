package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seyha.web.app.Bank_Concepts.entity.User;

public interface UserRepository extends JpaRepository<User,String> {
    User findByUsernameIgnoreCase(String username);

}
