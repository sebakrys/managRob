package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpStation;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpUserApp;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SpUserRepository extends JpaRepository<SpUserApp, Long> {
    List<SpUserApp> findByLastName(String lastName);
    //@Query("select u from UserApp u where u.id = ?1")
    SpUserApp findById(long id);

    SpUserApp findByPesel(String pesel);
    List<SpUserApp> findByTelephone(String telephone);
    List<SpUserApp> findByEmail(String email);

    @Query( "select u from SpUserApp u JOIN FETCH u.userRole r where r.role = :roleName" )
    List<SpUserApp> findBySpecificRoles(@Param("roleName") String roleName);

    //@Query( "select u from SpUserApp u JOIN FETCH u.userRole r, u.stations b where r.role = 'ROLE_MANAGER' and b.id= :stationId" )
    @Query( "select u from SpUserApp u JOIN FETCH u.stations b where b.id = :stationId" )
    List<SpUserApp> findByStationId(@Param("stationId") long stationId);

    @Query( "select u from SpUserApp u JOIN FETCH u.robot b where b.id = :robotId" )
    List<SpUserApp> findByByRobotId(@Param("robotId") long robotId);

    @Query( "select u.stations from SpUserApp u  where u.id = :userId" )
    List<SpStation> findStationsByUserId(@Param("userId") long userId);

    @Query( "select u.robot from SpUserApp u  where u.id = :userId" )
    List<SpRobot> findRobotsByUserId(@Param("userId") long userId);

}
