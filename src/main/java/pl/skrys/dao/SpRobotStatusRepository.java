package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpRobotStatus;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SpRobotStatusRepository extends JpaRepository<SpRobotStatus, Long> {
    SpRobotStatus findByRobot(SpRobot robot);
    SpRobotStatus findById(long id);

    SpRobotStatus findFirstByRobotIdOrderByDataDesc(long robotID);
    List<SpRobotStatus> findTop12ByRobotIdAndAcceptedOrderByDataDesc(long robotID, boolean accepted);

    @Query( value = "select count(fc) from SpRobotStatus fc JOIN fc.robot f where f.id = :robotId and fc.accepted=true")
    long getNumberOfAcceptedRobotStatus(@Param("robotId") long robotId);



    /*
    @Query( value = "select fc from SpRobotStatus fc JOIN FETCH fc.robot f where f.id = :robotId order by fc.data desc")
    List<SpRobotStatus> getRobotChargerOrderByData(@Param("robotId") long robotId);
*/
}
