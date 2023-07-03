package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.TokenLogIn;

@Repository
public interface TokenLogInReposirory extends JpaRepository<TokenLogIn,Integer> {
    TokenLogIn findByUsers_UserId(int userId);
    boolean existsByUsers_UserId(int userId);
}
