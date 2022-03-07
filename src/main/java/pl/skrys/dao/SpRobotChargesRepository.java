package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpRobotCharges;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SpRobotChargesRepository extends JpaRepository<SpRobotCharges, Long> {
    SpRobotCharges findByRobot(SpRobot robot);
    SpRobotCharges findById(long id);

    SpRobotCharges findFirstByRobotIdOrderByDataDesc(long robotID);
    List<SpRobotCharges> findTop12ByRobotIdAndAcceptedOrderByDataDesc(long robotID, boolean accepted);

    @Query( value = "select count(fc) from SpRobotCharges fc JOIN fc.robot f where f.id = :robotId and fc.accepted=true")
    long getNumberOfAcceptedRobotCharges(@Param("robotId") long robotId);



    /*
    @Query( value = "select fc from SpRobotCharges fc JOIN FETCH fc.robot f where f.id = :robotId order by fc.data desc")
    List<SpRobotCharges> getRobotChargerOrderByData(@Param("robotId") long robotId);
*/
}
