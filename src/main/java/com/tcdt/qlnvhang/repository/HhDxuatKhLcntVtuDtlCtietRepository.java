package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntVtuDtlCtiet;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HhDxuatKhLcntVtuDtlCtietRepository extends BaseRepository<HhDxuatKhLcntVtuDtlCtiet, Long> {

    List<HhDxuatKhLcntVtuDtlCtiet> findByIdGoiThau(Long idGoiThau);
}
