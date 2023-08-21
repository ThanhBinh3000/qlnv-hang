package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuXuatKhoDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScPhieuXuatKhoHdrRepository extends JpaRepository<ScPhieuXuatKhoHdr, Long> {

    @Query(value = "SELECT qd FROM ScPhieuXuatKhoHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) ")
    Page<ScPhieuXuatKhoHdr> searchPage(@Param("param") ScPhieuXuatKhoReq req, Pageable pageable);


    @Query(value = "SELECT qd FROM ScPhieuXuatKhoHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
            "AND (:#{#param.maDviSr} IS NULL OR qd.maDvi = :#{#param.maDviSr}) " +
            "AND (:#{#param.idScDanhSachHdr} IS NULL OR qd.idScDanhSachHdr = :#{#param.idScDanhSachHdr}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR qd.soPhieuXuatKho LIKE CONCAT(:#{#param.soPhieuXuatKho},'%'))" +
            "AND ((:#{#param.ngayTu} IS NULL OR qd.ngayXuatKho >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR qd.ngayXuatKho <= :#{#param.ngayDen})) " +
            " ORDER BY qd.ngaySua desc , qd.ngayTao desc, qd.id desc "
    )
    List<ScPhieuXuatKhoHdr> searchList(@Param("param") ScPhieuXuatKhoReq req);

    @Query(value = "SELECT qd FROM ScPhieuXuatKhoHdr qd " +
            " LEFT JOIN ScBangKeXuatVatTuHdr bk on qd.id = bk.idPhieuXuatKho " +
            " WHERE 1 = 1 " +
            " AND bk.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR qd.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.idQdXh} IS NULL OR qd.idQdXh = :#{#param.idQdXh}) " )
    List<ScPhieuXuatKhoHdr> searchListTaoBangKe(@Param("param") ScPhieuXuatKhoReq req);

    @Query(value = "SELECT xk FROM ScPhieuXuatKhoHdr xk " +
            " LEFT JOIN ScKiemTraChatLuongHdr kt on xk.id = kt.idPhieuXuatKho " +
            " WHERE 1 = 1 " +
            " AND kt.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR xk.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.idQdXh} IS NULL OR xk.idQdXh = :#{#param.idQdXh}) " )
    List<ScPhieuXuatKhoHdr> searchListTaoKiemTraCl(@Param("param") ScPhieuXuatKhoReq req);

    @Query(value = "SELECT xk FROM ScPhieuXuatKhoHdr xk " +
            " LEFT JOIN ScKiemTraChatLuongHdr kt on xk.id = kt.idPhieuXuatKho " +
            " WHERE 1 = 1 " +
            " AND kt.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR xk.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.idQdXh} IS NULL OR xk.idQdXh = :#{#param.idQdXh}) " )
    List<ScPhieuXuatKhoHdr> searchListTaoBaoCao(@Param("param") ScPhieuXuatKhoReq req);

    List<ScPhieuXuatKhoHdr> findAllByIdQdXhAndIdScDanhSachHdr(Long idQdXh,Long idScDanhSachHdr);
}
