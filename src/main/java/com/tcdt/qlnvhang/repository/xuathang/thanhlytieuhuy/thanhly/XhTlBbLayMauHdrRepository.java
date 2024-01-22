package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlBbLayMauHdrRepository extends JpaRepository<XhTlBbLayMauHdr, Long> {

    @Query("SELECT c FROM XhTlBbLayMauHdr c WHERE 1=1"+
//            " AND (:#{#param.dvpl} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvpl},'%'))" +
//            " AND (:#{#param.maDeTai} IS NULL OR c.maDeTai LIKE CONCAT(:#{#param.maDeTai},'%'))" +
//            " AND (:#{#param.tenDeTai} IS NULL OR c.tenDeTai LIKE CONCAT(:#{#param.tenDeTai},'%'))" +
//            " AND (:#{#param.capDeTai} IS NULL OR c.capDeTai =:#{#param.capDeTai})" +
//            " AND ((:#{#param.thoiGianTu}  IS NULL OR (c.ngayKyTu >= :#{#param.thoiGianTu} AND c.ngayKyDen >= :#{#param.thoiGianTu}))" +
//            " AND (:#{#param.thoiGianDen}  IS NULL OR (c.ngayKyTu <= :#{#param.thoiGianDen} AND c.ngayKyDen <= :#{#param.thoiGianDen})))" +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhTlBbLayMauHdr> searchPage(@Param("param") XhTlBbLayMauReq param, Pageable pageable);

    @Query("SELECT c FROM XhTlBbLayMauHdr c " +
            " left join XhTlKtraClHdr ktra on ktra.idBbLayMau = c.id " +
            " WHERE ktra.id is null "+
            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<XhTlBbLayMauHdr> searchDsTaoKtraCl(@Param("param") XhTlBbLayMauReq param);


    @Query("SELECT c FROM XhTlBbLayMauHdr c " +
            " WHERE 1 = 1 "+
            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
            " AND (:#{#param.dviKnghiem} IS NULL OR c.dviKnghiem LIKE concat('%',CONCAT(:#{#param.dviKnghiem},'%')))" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
            " AND (:#{#param.soBienBan} IS NULL OR c.soBienBan LIKE CONCAT(:#{#param.soBienBan},'%'))" +
            "AND ((:#{#param.ngayTu} IS NULL OR c.ngayLayMau >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR c.ngayLayMau <= :#{#param.ngayDen})) " +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<XhTlBbLayMauHdr> findAllByIdQdXh(@Param("param") XhTlBbLayMauReq param);


}