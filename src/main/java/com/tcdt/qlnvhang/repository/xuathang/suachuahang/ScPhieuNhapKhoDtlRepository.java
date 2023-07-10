package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScPhieuNhapKhoDtlRepository extends JpaRepository<ScPhieuNhapKhoDtl,Long> {
    List<ScPhieuNhapKhoDtl> findByHdrId(Long id);

}
