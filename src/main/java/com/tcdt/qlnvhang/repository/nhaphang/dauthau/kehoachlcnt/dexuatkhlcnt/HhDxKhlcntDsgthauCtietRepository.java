package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtiet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxKhlcntDsgthauCtietRepository extends JpaRepository<HhDxKhlcntDsgthauCtiet, Long> {

    List<HhDxKhlcntDsgthauCtiet> findByIdGoiThau(Long idGoiThau);

    void deleteAllByIdGoiThau(Long idGoiThau);

    @Transactional
    void deleteAllByIdGoiThauIn(List<Long> idGoiThauList);


}
