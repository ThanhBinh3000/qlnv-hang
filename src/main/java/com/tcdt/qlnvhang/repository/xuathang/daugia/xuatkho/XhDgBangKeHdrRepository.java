package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBangKeReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBangKeHdrRepository extends JpaRepository<XhDgBangKeHdr, Long> {


    @Query("SELECT DISTINCT BK FROM XhDgBangKeHdr BK " +
            "LEFT JOIN XhQdGiaoNvXh QD ON QD.id = BK.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR BK.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR BK.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKeHang} IS NULL OR BK.soBangKeHang = :#{#param.soBangKeHang}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR BK.soPhieuXuatKho = :#{#param.soPhieuXuatKho}) " +
            "AND (:#{#param.soQdNv} IS NULL OR BK.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR BK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.ngayLapBangKeTu} IS NULL OR BK.ngayLapBangKe >= :#{#param.ngayLapBangKeTu}) " +
            "AND (:#{#param.ngayLapBangKeDen} IS NULL OR BK.ngayLapBangKe <= :#{#param.ngayLapBangKeDen}) " +
            "AND (:#{#param.thoiGianGiaoNhanTu} IS NULL OR BK.thoiGianGiaoNhan >= :#{#param.thoiGianGiaoNhanTu}) " +
            "AND (:#{#param.thoiGianGiaoNhanDen} IS NULL OR BK.thoiGianGiaoNhan <= :#{#param.thoiGianGiaoNhanDen}) " +
            "AND (:#{#param.ngayXuatKhoTu} IS NULL OR BK.ngayXuatKho >= :#{#param.ngayXuatKhoTu}) " +
            "AND (:#{#param.ngayXuatKhoDen} IS NULL OR BK.ngayXuatKho <= :#{#param.ngayXuatKhoDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR BK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY BK.nam DESC, BK.ngaySua DESC, BK.ngayTao DESC, BK.id DESC")
    Page<XhDgBangKeHdr> searchPage(@Param("param") XhDgBangKeReq param, Pageable pageable);

    boolean existsBySoBangKeHang(String soBangKeHang);

    boolean existsBySoBangKeHangAndIdNot(String soBangKeHang, Long id);

    List<XhDgBangKeHdr> findByIdIn(List<Long> idList);

    List<XhDgBangKeHdr> findAllByIdIn(List<Long> listId);
}
