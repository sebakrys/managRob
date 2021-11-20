package pl.skrys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpFlatCharges;
import pl.skrys.app.SpUserApp;
import pl.skrys.app.SpVerifyToken;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SpFlatChargesRepository extends JpaRepository<SpFlatCharges, Long> {
    SpFlatCharges findByFlat(SpFlat flat);
    SpFlatCharges findById(long id);

    SpFlatCharges findFirstByFlatIdOrderByDataDesc(long flatID);
    List<SpFlatCharges> findTop12ByFlatIdAndAcceptedOrderByDataDesc(long flatID, boolean accepted);

    @Query( value = "select count(fc) from SpFlatCharges fc JOIN fc.flat f where f.id = :flatId and fc.accepted=true")
    long getNumberOfAcceptedFlatCharges(@Param("flatId") long flatId);



    /*
    @Query( value = "select fc from SpFlatCharges fc JOIN FETCH fc.flat f where f.id = :flatId order by fc.data desc")
    List<SpFlatCharges> getFlatChargerOrderByData(@Param("flatId") long flatId);
*/
}
