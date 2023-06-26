package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTrinhVaThamDinhReq;
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
            "AND (:#{#param.soQdSc} IS NULL OR t.soQdSc LIKE CONCAT(:#{#param.soQdSc},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR t.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.ngayDuyetTu}  IS NULL OR t.ngayDuyet >= :#{#param.ngayDuyetTu})" +
            "AND (:#{#param.ngayDuyetDen}  IS NULL OR t.ngayDuyet <= :#{#param.ngayDuyetDen})) ")
    Page<ScTrinhThamDinh> searchPage(@Param("param")ScTrinhVaThamDinhReq req,
                                     Pageable pageable);
}
