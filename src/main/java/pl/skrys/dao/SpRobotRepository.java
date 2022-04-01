package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpRobot;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SpRobotRepository extends JpaRepository<SpRobot, Long> {
    SpRobot findById(long id);
    SpRobot findByRobotNumber(String robotNumber);
    List<SpRobot> findByRobotNumberContainingIgnoreCase(String robotNumber);

}
