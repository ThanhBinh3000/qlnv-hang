package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBbanNghiemThuDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhBbanNghiemThuDtlRepository extends JpaRepository<HhBbanNghiemThuDtl,Long> {
    List<HhBbanNghiemThuDtl> findAllByIdHdr(Long ids);
    List<HhBbanNghiemThuDtl> findAllByIdHdrIn(List<Long> ids);
}
