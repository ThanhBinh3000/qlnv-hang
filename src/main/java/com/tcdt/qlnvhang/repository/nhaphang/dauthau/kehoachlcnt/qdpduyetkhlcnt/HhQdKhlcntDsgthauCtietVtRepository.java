package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietVt;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.List;

public interface HhQdKhlcntDsgthauCtietVtRepository extends BaseRepository<HhQdKhlcntDsgthauCtietVt, Long> {

    List<HhQdKhlcntDsgthauCtietVt> findByIdGoiThauCtiet(Long idGoiThau);

    void deleteAllByIdGoiThauCtiet(Long idGoiThau);

}
