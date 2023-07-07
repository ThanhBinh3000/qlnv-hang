package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkPhieuNhapKhoDtlRepository extends JpaRepository<HhNkPhieuNhapKhoDtl, Long> {
    List<HhNkPhieuNhapKhoDtl> findByHdrId(Long id);
}
