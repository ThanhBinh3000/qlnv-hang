package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuNhapXuatRepository extends JpaRepository<PhieuNhapXuatHistory, Long> {
    @Query(value ="SELECT * from PHIEU_NHAP_XUAT_HISTORY where (:ngayTaoTu IS NULL OR ngay_duyet >= TO_DATE(:ngayTaoTu,'yyyy-MM-dd'))"
            +" AND (:ngayTaoDen IS NULL OR  ngay_duyet <= TO_DATE(:ngayTaoDen,'yyyy-MM-dd'))",
            nativeQuery = true)
    List<PhieuNhapXuatHistory> selectPage(String ngayTaoTu, String ngayTaoDen);
}
