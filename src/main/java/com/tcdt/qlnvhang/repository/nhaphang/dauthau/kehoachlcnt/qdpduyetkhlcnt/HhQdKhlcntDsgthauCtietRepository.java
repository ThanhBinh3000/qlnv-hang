package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.List;

public interface HhQdKhlcntDsgthauCtietRepository extends BaseRepository<HhQdKhlcntDsgthauCtiet, Long> {

    List<HhQdKhlcntDsgthauCtiet> findByIdGoiThau(Long idGoiThau);

    void deleteAllByIdGoiThau(Long idGoiThau);

}
