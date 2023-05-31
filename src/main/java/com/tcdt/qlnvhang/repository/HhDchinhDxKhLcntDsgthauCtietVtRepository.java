package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthauCtiet;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthauCtietVt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HhDchinhDxKhLcntDsgthauCtietVtRepository extends CrudRepository<HhDchinhDxKhLcntDsgthauCtietVt, Long> {

    List<HhDchinhDxKhLcntDsgthauCtietVt> findAllByIdGoiThauCtiet(Long idGoiThau);
    List<HhDchinhDxKhLcntDsgthauCtietVt> findAllByIdGoiThauCtietIn(List<Long> idGoiThau);

    void deleteAllByIdGoiThauCtiet(Long idDcDxDtl);

}
