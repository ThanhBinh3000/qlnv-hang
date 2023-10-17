package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbHdoiBttHdrRepository extends JpaRepository<XhBbHdoiBttHdr, Long> {

    @Query("SELECT DISTINCT HD FROM XhBbHdoiBttHdr HD " +
            "LEFT JOIN XhQdNvXhBttHdr QD ON QD.id = HD.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR HD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdNv} IS NULL OR HD.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR HD.soBbHaoDoi = :#{#param.soBbHaoDoi}) " +
            "AND (:#{#param.ngayLapBienBanTu} IS NULL OR HD.ngayLapBienBan >= :#{#param.ngayLapBienBanTu}) " +
            "AND (:#{#param.ngayLapBienBanDen} IS NULL OR HD.ngayLapBienBan <= :#{#param.ngayLapBienBanDen}) " +
            "AND (:#{#param.ngayBatDauXuatTu} IS NULL OR HD.ngayBatDauXuat >= :#{#param.ngayBatDauXuatTu}) " +
            "AND (:#{#param.ngayBatDauXuatDen} IS NULL OR HD.ngayBatDauXuat <= :#{#param.ngayBatDauXuatDen}) " +
            "AND (:#{#param.ngayKetThucXuatTu} IS NULL OR HD.ngayKetThucXuat >= :#{#param.ngayKetThucXuatTu}) " +
            "AND (:#{#param.ngayKetThucXuatDen} IS NULL OR HD.ngayKetThucXuat <= :#{#param.ngayKetThucXuatDen}) " +
            "AND (:#{#param.tgianGiaoNhanTu} IS NULL OR HD.tgianGiaoNhan >= :#{#param.tgianGiaoNhanTu}) " +
            "AND (:#{#param.tgianGiaoNhanDen} IS NULL OR HD.tgianGiaoNhan <= :#{#param.tgianGiaoNhanDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR HD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY HD.namKh DESC, HD.ngaySua DESC, HD.ngayTao DESC, HD.id DESC")
    Page<XhBbHdoiBttHdr> searchPage(@Param("param") XhBbHdoiBttHdrReq param, Pageable pageable);

    boolean existsBySoBbHaoDoi(String soBbHaoDoi);

    boolean existsBySoBbHaoDoiAndIdNot(String soBbHaoDoi, Long id);

    List<XhBbHdoiBttHdr> findByIdIn(List<Long> idList);

    List<XhBbHdoiBttHdr> findAllByIdIn(List<Long> listId);
}