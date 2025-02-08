package hu.fourig.partner.repository;

import hu.fourig.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    @Query("""
            select p from Partner p
            left join Address a on p = a.partner
            where upper(p.name) like %:filter% or upper(p.email) like %:filter% or upper(p.phone) like %:filter%
            or upper(a.city) like %:filter% or upper(a.street) like %:filter%
        """)
    List<Partner> findByFilter(String filter);
}
