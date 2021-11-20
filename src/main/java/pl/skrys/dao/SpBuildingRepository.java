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
public interface SpBuildingRepository extends JpaRepository<SpBuilding, Long> {
    SpBuilding findById(long id);
    SpBuilding findByNazwa(String nazwa);



}
