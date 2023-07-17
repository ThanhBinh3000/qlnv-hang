package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuNhapKhoDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScPhieuNhapKhoHdrRepository extends JpaRepository<ScPhieuNhapKhoHdr, Long> {

    Optional<ScPhieuNhapKhoHdr> findBySoPhieuNhapKho(String soPhieuNhapKho);

    List<ScPhieuNhapKhoHdr> findAllByIdScDanhSachHdrAndIdQdNh(Long idScDanhSachHdr,Long idQdNh);

}
