package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HhNkPhieuNhapKhoHdrRepository extends JpaRepository<HhNkPhieuNhapKhoHdr, Long> {
//    Page<HhNkPhieuNhapKhoHdrDTO> searchPageChiCuc(HhNkPhieuNhapKhoHdrReq req, Pageable pageable);
//
//    Page<HhNkPhieuNhapKhoHdrDTO> searchPage(HhNkPhieuNhapKhoHdrReq req, Pageable pageable);
}
