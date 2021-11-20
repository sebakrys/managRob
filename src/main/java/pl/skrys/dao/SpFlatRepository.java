package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpFlatCharges;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface SpFlatRepository extends JpaRepository<SpFlat, Long> {
    SpFlat findById(long id);


}
