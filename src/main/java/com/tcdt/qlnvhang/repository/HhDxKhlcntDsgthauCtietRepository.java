package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxKhlcntDsgthauCtiet;

import java.util.List;

public interface HhDxKhlcntDsgthauCtietRepository extends BaseRepository<HhDxKhlcntDsgthauCtiet, Long> {

    List<HhDxKhlcntDsgthauCtiet> findByIdGoiThau(Long idGoiThau);

    void deleteAllByIdGoiThau(Long idGoiThau);

}
