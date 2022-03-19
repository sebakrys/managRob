package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpStation;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SpStationRepository extends JpaRepository<SpStation, Long> {
    SpStation findById(long id);
    SpStation findByNazwa(String nazwa);
    List<SpStation> findAllByProjectId(long id);

    List<SpStation> findByNazwaContainingIgnoreCase(String nazwa);

/*
    @Query( "select s from SpStation s where s.nazwa like :searchNazwa" )
    List<SpStation> searchStationsBysearch(@Param("searchNazwa") String searchNazwa);*/


}
