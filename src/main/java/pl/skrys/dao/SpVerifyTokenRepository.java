package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpVerifyToken;
import pl.skrys.app.UserRole;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface SpVerifyTokenRepository extends JpaRepository<SpVerifyToken, Long> {
    SpVerifyToken findByVerifyToken(String verifyToken);

    SpVerifyToken findById(long id);
}
