package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpFlat;
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

    //@Query( "select u from SpUserApp u JOIN FETCH u.userRole r, u.buildings b where r.role = 'ROLE_MANAGER' and b.id= :buildingId" )
    @Query( "select u from SpUserApp u JOIN FETCH u.buildings b where b.id = :buildingId" )
    List<SpUserApp> findByBuildingId(@Param("buildingId") long buildingId);

    @Query( "select u from SpUserApp u JOIN FETCH u.flat b where b.id = :flatId" )
    List<SpUserApp> findByByFlatId(@Param("flatId") long flatId);

    @Query( "select u.buildings from SpUserApp u  where u.id = :userId" )
    List<SpBuilding> findBuildingsByUserId(@Param("userId") long userId);

    @Query( "select u.flat from SpUserApp u  where u.id = :userId" )
    List<SpFlat> findFlatsByUserId(@Param("userId") long userId);

}
