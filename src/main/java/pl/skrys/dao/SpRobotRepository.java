package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpRobot;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface SpRobotRepository extends JpaRepository<SpRobot, Long> {
    SpRobot findById(long id);


}
