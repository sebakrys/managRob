package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpStation;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface SpStationRepository extends JpaRepository<SpStation, Long> {
    SpStation findById(long id);
    SpStation findByNazwa(String nazwa);



}
