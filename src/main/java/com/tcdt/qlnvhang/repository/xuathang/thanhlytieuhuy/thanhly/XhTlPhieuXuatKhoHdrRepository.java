package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBienBanTkHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlPhieuXuatKhoHdrRepository extends JpaRepository<XhTlPhieuXuatKhoHdr, Long> {

    @Query(value = "SELECT qd FROM XhTlPhieuXuatKhoHdr qd WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) ")
    Page<XhTlPhieuXuatKhoHdr> searchPage(@Param("param") XhTlPhieuXuatKhoReq req, Pageable pageable);

    @Query(value = "SELECT c FROM XhTlPhieuXuatKhoHdr c " +
            " LEFT JOIN ScBangKeNhapVtHdr bk on c.id = bk.idPhieuNhapKho " +
            " WHERE 1 = 1 " +
            " AND bk.id is null " +
            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " )
    List<XhTlPhieuXuatKhoHdr> searchListTaoBangKe(@Param("param") XhTlPhieuXuatKhoReq req);


//    @Query(value = "SELECT qd FROM XhTlPhieuXuatKhoHdr qd WHERE 1 = 1 " +
//            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
//            "AND (:#{#param.maDviSr} IS NULL OR qd.maDvi = :#{#param.maDviSr}) " +
//            "AND (:#{#param.idScDanhSachHdr} IS NULL OR qd.idScDanhSachHdr = :#{#param.idScDanhSachHdr}) " +
//            "AND (:#{#param.soPhieuXuatKho} IS NULL OR qd.soPhieuXuatKho LIKE CONCAT(:#{#param.soPhieuXuatKho},'%'))" +
//            "AND ((:#{#param.ngayTu} IS NULL OR qd.ngayXuatKho >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR qd.ngayXuatKho <= :#{#param.ngayDen})) " +
//            " ORDER BY qd.ngaySua desc , qd.ngayTao desc, qd.id desc "
//    )
//    List<XhTlPhieuXuatKhoHdr> searchList(@Param("param") XhTlPhieuXuatKhoReq req);

//    @Query(value = "SELECT qd FROM XhTlPhieuXuatKhoHdr qd " +
//            " LEFT JOIN ScBangKeXuatVatTuHdr bk on qd.id = bk.idPhieuXuatKho " +
//            " WHERE 1 = 1 " +
//            " AND bk.id is null " +
//            " AND (:#{#param.trangThai} IS NULL OR qd.trangThai = :#{#param.trangThai}) " +
//            " AND (:#{#param.idQdXh} IS NULL OR qd.idQdXh = :#{#param.idQdXh}) " )
//    List<XhTlPhieuXuatKhoHdr> searchListTaoBangKe(@Param("param") XhTlPhieuXuatKhoReq req);
//
//    @Query(value = "SELECT xk FROM XhTlPhieuXuatKhoHdr xk " +
//            " LEFT JOIN ScKiemTraChatLuongHdr kt on xk.id = kt.idPhieuXuatKho " +
//            " WHERE 1 = 1 " +
//            " AND kt.id is null " +
//            " AND (:#{#param.trangThai} IS NULL OR xk.trangThai = :#{#param.trangThai}) " +
//            " AND (:#{#param.idQdXh} IS NULL OR xk.idQdXh = :#{#param.idQdXh}) " )
//    List<XhTlPhieuXuatKhoHdr> searchListTaoKiemTraCl(@Param("param") XhTlPhieuXuatKhoReq req);
//
//    @Query(value = "SELECT xk FROM XhTlPhieuXuatKhoHdr xk " +
//            " LEFT JOIN ScKiemTraChatLuongHdr kt on xk.id = kt.idPhieuXuatKho " +
//            " WHERE 1 = 1 " +
//            " AND kt.id is null " +
//            " AND (:#{#param.trangThai} IS NULL OR xk.trangThai = :#{#param.trangThai}) " +
//            " AND (:#{#param.idQdXh} IS NULL OR xk.idQdXh = :#{#param.idQdXh}) " )
//    List<XhTlPhieuXuatKhoHdr> searchListTaoBaoCao(@Param("param") XhTlPhieuXuatKhoReq req);

}