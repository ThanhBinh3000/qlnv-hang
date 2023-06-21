package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ScTrinhThamDinhRepository extends JpaRepository<ScTrinhThamDinh, Long> {
    @Query(value = "SELECT t FROM ScTrinhThamDinh t WHERE 1 = 1" +
            "AND (:soQdSc IS NULL OR t.soQdSc LIKE CONCAT(:soQdSc,'%'))  " +
            "AND (:trangThai IS NULL OR t.trangThai = :trangThai)" +
            "AND ((:ngayDuyetTu  IS NULL OR to_date(t.ngayDuyet,'dd/MMM/yyyy') >= :ngayDuyetTu)" +
            "AND (:ngayDuyetDen  IS NULL OR to_date(t.ngayDuyet,'dd/MMM/yyyy') <= :ngayDuyetDen)) ")
    Page<ScTrinhThamDinh> searchPage(@Param("soQdSc") String soQdSc,
                                     @Param("trangThai") String trangThai,
                                     @Param("ngayDuyetTu")LocalDate ngayDuyetTu,
                                     @Param("ngayDuyetDen")LocalDate ngayDuyetDen,
                                     Pageable pageable);
}
