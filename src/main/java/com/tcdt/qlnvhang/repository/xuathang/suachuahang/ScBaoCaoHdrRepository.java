package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBaoCaoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBaoCaoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScBaoCaoHdrRepository extends JpaRepository<ScBaoCaoHdr, Long> {

    @Query(value = "SELECT q FROM ScBaoCaoHdr q WHERE 1 = 1" +
            "AND (:#{#param.trangThai} IS NULL OR q.trangThai = :#{#param.trangThai})")
    Page<ScBaoCaoHdr> searchPage(@Param("param") ScBaoCaoReq req, Pageable pageable);

}
