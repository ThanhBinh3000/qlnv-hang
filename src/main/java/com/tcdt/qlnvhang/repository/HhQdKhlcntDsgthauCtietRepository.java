package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdKhlcntDsgthauCtiet;

import java.util.List;

public interface HhQdKhlcntDsgthauCtietRepository extends BaseRepository<HhQdKhlcntDsgthauCtiet, Long> {

    List<HhQdKhlcntDsgthauCtiet> findByIdGoiThau(Long idGoiThau);

    void deleteAllByIdGoiThau(Long idGoiThau);

}
