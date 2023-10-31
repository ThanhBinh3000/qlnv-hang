package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.SearchKhCnCtrinhNcReq;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnCongTrinhNghienCuu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhCnCongTrinhNghienCuuRepository extends JpaRepository<KhCnCongTrinhNghienCuu, Long> {

    @Query("SELECT c FROM KhCnCongTrinhNghienCuu c WHERE 1=1" +
            " AND (:#{#param.dvpl} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvpl},'%'))" +
            " AND (:#{#param.maDeTai} IS NULL OR c.maDeTai LIKE CONCAT(:#{#param.maDeTai},'%'))" +
            " AND (:#{#param.tenDeTai} IS NULL OR c.tenDeTai LIKE CONCAT('%',CONCAT(:#{#param.tenDeTai},'%')))" +
            " AND (:#{#param.capDeTai} IS NULL OR c.capDeTai =:#{#param.capDeTai})" +
            " AND ((:#{#param.thoiGianThTu}  IS NULL OR (c.ngayKyTu >= :#{#param.thoiGianThTu} AND c.ngayKyTu >= :#{#param.thoiGianThTu}))" +
            " AND (:#{#param.thoiGianThDen}  IS NULL OR (c.ngayKyTu <= :#{#param.thoiGianThDen} AND c.ngayKyTu <= :#{#param.thoiGianThDen})))" +
            " AND ((:#{#param.thoiGianHtTu}  IS NULL OR (c.ngayKyDen >= :#{#param.thoiGianHtTu} AND c.ngayKyDen >= :#{#param.thoiGianHtTu}))" +
            " AND (:#{#param.thoiGianHtDen}  IS NULL OR (c.ngayKyDen <= :#{#param.thoiGianHtDen} AND c.ngayKyDen <= :#{#param.thoiGianHtDen})))" +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<KhCnCongTrinhNghienCuu> searchPage(@Param("param") SearchKhCnCtrinhNcReq param, Pageable pageable);

    Optional<KhCnCongTrinhNghienCuu> findAllByMaDeTai(String maDetai);

    List<KhCnCongTrinhNghienCuu> findAllByIdIn(List<Long> ids);

}
