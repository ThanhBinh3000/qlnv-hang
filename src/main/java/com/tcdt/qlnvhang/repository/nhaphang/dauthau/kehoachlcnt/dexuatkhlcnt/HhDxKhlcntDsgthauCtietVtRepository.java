package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtietVt;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxKhlcntDsgthauCtietVtRepository extends JpaRepository<HhDxKhlcntDsgthauCtietVt, Long> {

    List<HhDxKhlcntDsgthauCtietVt> findByIdGoiThauCtiet(Long idGoiThauCtiet);

    void deleteAllByIdGoiThauCtiet(Long idGoiThau);

    @Transactional
    void deleteAllByIdGoiThauCtietIn(List<Long> idGoiThauList);


}
