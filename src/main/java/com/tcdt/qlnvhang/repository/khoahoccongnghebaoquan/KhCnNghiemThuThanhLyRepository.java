package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnNghiemThuThanhLy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhCnNghiemThuThanhLyRepository extends JpaRepository<KhCnNghiemThuThanhLy,Long> {
    List<KhCnNghiemThuThanhLy> findAllByIdHdr(Long ids);
    List<KhCnNghiemThuThanhLy> findAllByIdHdrIn(List<Long> ids);
}
