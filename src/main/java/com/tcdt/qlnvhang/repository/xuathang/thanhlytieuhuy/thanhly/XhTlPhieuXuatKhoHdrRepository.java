package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBienBanTkHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClHdr;
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
            " LEFT JOIN XhTlBangKeHdr bk on c.id = bk.idPhieuXuatKho " +
            " WHERE 1 = 1 " +
            " AND bk.id is null " +
            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " )
    List<XhTlPhieuXuatKhoHdr> searchListTaoBangKe(@Param("param") XhTlPhieuXuatKhoReq req);

    @Query(value = "SELECT c FROM XhTlPhieuXuatKhoHdr c " +
            " WHERE 1 = 1 " +
            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
            " AND (:#{#param.idDsHdr} IS NULL OR c.idDsHdr =:#{#param.idDsHdr})" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " )
    List<XhTlPhieuXuatKhoHdr> findAllByIdQdXhAndIdDsHdr(@Param("param") XhTlPhieuXuatKhoReq req);


    List<XhTlPhieuXuatKhoHdr> findAllByIdDsHdr(Long idDsHdr);


//    @Query("SELECT c FROM XhTlKtraClHdr c " +
//            " WHERE 1 = 1 "+
//            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
//            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
//            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
//            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
//            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
//            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
//    )
//    List<XhTlKtraClHdr> findAllByIdQdXh(@Param("param") XhTlKtraClReq param);
}