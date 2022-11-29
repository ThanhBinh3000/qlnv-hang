package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnNghiemThuThanhLyDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhCnNghiemThuThanhLyDtlRepository extends JpaRepository<KhCnNghiemThuThanhLyDtl,Long> {
    List<KhCnNghiemThuThanhLyDtl> findAllByIdNghiemThu(Long ids);
    List<KhCnNghiemThuThanhLyDtl> findAllByIdNghiemThuIn(List<Long> ids);

}
