package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface XhBbTinhkBttHdrRepository extends JpaRepository<XhBbTinhkBttHdr, Long> {

    @Query("SELECT DISTINCT TK FROM XhBbTinhkBttHdr TK " +
            "LEFT JOIN XhQdNvXhBttHdr QD ON QD.id = TK.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR TK.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR TK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdNv} IS NULL OR TK.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR TK.soBbTinhKho = :#{#param.soBbTinhKho}) " +
            "AND (:#{#param.ngayLapBienBanTu} IS NULL OR TK.ngayLapBienBan >= :#{#param.ngayLapBienBanTu}) " +
            "AND (:#{#param.ngayLapBienBanDen} IS NULL OR TK.ngayLapBienBan <= :#{#param.ngayLapBienBanDen}) " +
            "AND (:#{#param.ngayBatDauXuatTu} IS NULL OR TK.ngayBatDauXuat >= :#{#param.ngayBatDauXuatTu}) " +
            "AND (:#{#param.ngayBatDauXuatDen} IS NULL OR TK.ngayBatDauXuat <= :#{#param.ngayBatDauXuatDen}) " +
            "AND (:#{#param.ngayKetThucXuatTu} IS NULL OR TK.ngayKetThucXuat >= :#{#param.ngayKetThucXuatTu}) " +
            "AND (:#{#param.ngayKetThucXuatDen} IS NULL OR TK.ngayKetThucXuat <= :#{#param.ngayKetThucXuatDen}) " +
            "AND (:#{#param.tgianGiaoNhanTu} IS NULL OR TK.tgianGiaoNhan >= :#{#param.tgianGiaoNhanTu}) " +
            "AND (:#{#param.tgianGiaoNhanDen} IS NULL OR TK.tgianGiaoNhan <= :#{#param.tgianGiaoNhanDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR TK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY TK.namKh DESC, TK.ngaySua DESC, TK.ngayTao DESC, TK.id DESC")
    Page<XhBbTinhkBttHdr> searchPage(@Param("param") XhBbTinhkBttHdrReq param, Pageable pageable);

    boolean existsBySoBbTinhKho(String soBbTinhKho);

    boolean existsBySoBbTinhKhoAndIdNot(String soBbTinhKho, Long id);

    List<XhBbTinhkBttHdr> findByIdIn(List<Long> idList);

    List<XhBbTinhkBttHdr> findAllByIdIn(List<Long> listId);
}