package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtietVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtietVt1;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxKhlcntDsgthauCtietVt1Repository extends JpaRepository<HhDxKhlcntDsgthauCtietVt1, Long> {

    List<HhDxKhlcntDsgthauCtietVt1> findByIdGoiThauCtietVt(Long idGoiThauCtiet);

    void deleteAllByIdGoiThauCtietVt(Long idGoiThau);


}
