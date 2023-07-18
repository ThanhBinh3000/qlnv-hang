package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeNhapVtDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScBangKeNhapVtHdrRepository extends JpaRepository<ScBangKeNhapVtHdr, Long> {
    Optional<ScBangKeNhapVtHdr> findFirstBySoBangKe(String soBangKe);

    List<ScBangKeNhapVtHdr> findAllByIdIn(List<Long> id);

    List<ScBangKeNhapVtHdr> findAllByIdPhieuNhapKhoIn(List<Long> idPhieuNhapKho);

}
