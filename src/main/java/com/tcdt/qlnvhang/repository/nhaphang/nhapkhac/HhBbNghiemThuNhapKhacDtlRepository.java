package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhBbNghiemThuNhapKhacDtlRepository extends JpaRepository<HhBbNghiemThuNhapKhacDtl, Long> {
    List<HhBbNghiemThuNhapKhacDtl> findAllByIdHdr (Long ids);
    void deleteAllByIdHdr (Long ids);
}
