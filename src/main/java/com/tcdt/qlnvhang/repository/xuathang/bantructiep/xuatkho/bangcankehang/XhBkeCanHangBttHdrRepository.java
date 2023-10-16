package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bangcankehang;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBangKeReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBkeCanHangBttHdrRepository extends JpaRepository<XhBkeCanHangBttHdr, Long> {

    @Query("SELECT DISTINCT BK FROM XhBkeCanHangBttHdr BK " +
            "LEFT JOIN XhQdNvXhBttHdr QD ON QD.id = BK.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR BK.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR BK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBangKeHang} IS NULL OR BK.soBangKeHang = :#{#param.soBangKeHang}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR BK.soPhieuXuatKho = :#{#param.soPhieuXuatKho}) " +
            "AND (:#{#param.soQdNv} IS NULL OR BK.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR BK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.ngayLapBangKeTu} IS NULL OR BK.ngayLapBangKe >= :#{#param.ngayLapBangKeTu}) " +
            "AND (:#{#param.ngayLapBangKeDen} IS NULL OR BK.ngayLapBangKe <= :#{#param.ngayLapBangKeDen}) " +
            "AND (:#{#param.tgianGiaoNhanTu} IS NULL OR BK.tgianGiaoNhan >= :#{#param.tgianGiaoNhanTu}) " +
            "AND (:#{#param.tgianGiaoNhanDen} IS NULL OR BK.tgianGiaoNhan <= :#{#param.tgianGiaoNhanDen}) " +
            "AND (:#{#param.ngayXuatKhoTu} IS NULL OR BK.ngayXuatKho >= :#{#param.ngayXuatKhoTu}) " +
            "AND (:#{#param.ngayXuatKhoDen} IS NULL OR BK.ngayXuatKho <= :#{#param.ngayXuatKhoDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR BK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY BK.namKh DESC, BK.ngaySua DESC, BK.ngayTao DESC, BK.id DESC")
    Page<XhBkeCanHangBttHdr> searchPage(@Param("param") XhBkeCanHangBttHdrReq param, Pageable pageable);

    boolean existsBySoBangKeHang(String soBangKeHang);

    boolean existsBySoBangKeHangAndIdNot(String soBangKeHang, Long id);

    List<XhBkeCanHangBttHdr> findByIdIn(List<Long> idList);

    List<XhBkeCanHangBttHdr> findAllByIdIn(List<Long> listId);
}