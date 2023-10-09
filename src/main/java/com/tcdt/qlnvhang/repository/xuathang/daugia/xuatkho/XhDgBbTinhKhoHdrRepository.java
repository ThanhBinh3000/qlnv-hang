package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBbTinhKhoHdrRepository extends JpaRepository<XhDgBbTinhKhoHdr, Long> {

    @Query("SELECT DISTINCT TK FROM XhDgBbTinhKhoHdr TK " +
            "LEFT JOIN XhQdGiaoNvXh QD ON QD.id = TK.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR TK.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR TK.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdNv} IS NULL OR TK.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR TK.soBbTinhKho = :#{#param.soBbTinhKho}) " +
            "AND (:#{#param.ngayLapBienBanTu} IS NULL OR TK.ngayLapBienBan >= :#{#param.ngayLapBienBanTu}) " +
            "AND (:#{#param.ngayLapBienBanDen} IS NULL OR TK.ngayLapBienBan <= :#{#param.ngayLapBienBanDen}) " +
            "AND (:#{#param.ngayBatDauXuatTu} IS NULL OR TK.ngayBatDauXuat >= :#{#param.ngayBatDauXuatTu}) " +
            "AND (:#{#param.ngayBatDauXuatDen} IS NULL OR TK.ngayBatDauXuat <= :#{#param.ngayBatDauXuatDen}) " +
            "AND (:#{#param.ngayKetThucXuatTu} IS NULL OR TK.ngayKetThucXuat >= :#{#param.ngayKetThucXuatTu}) " +
            "AND (:#{#param.ngayKetThucXuatDen} IS NULL OR TK.ngayKetThucXuat <= :#{#param.ngayKetThucXuatDen}) " +
            "AND (:#{#param.thoiGianGiaoNhanTu} IS NULL OR TK.thoiGianGiaoNhan >= :#{#param.thoiGianGiaoNhanTu}) " +
            "AND (:#{#param.thoiGianGiaoNhanDen} IS NULL OR TK.thoiGianGiaoNhan <= :#{#param.thoiGianGiaoNhanDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR TK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY TK.nam DESC, TK.ngaySua DESC, TK.ngayTao DESC, TK.id DESC")
    Page<XhDgBbTinhKhoHdr> searchPage(@Param("param") XhDgBbTinhKhoHdrReq param, Pageable pageable);

    boolean existsBySoBbTinhKho(String soBbTinhKho);

    boolean existsBySoBbTinhKhoAndIdNot(String soBbTinhKho, Long id);

    List<XhDgBbTinhKhoHdr> findByIdIn(List<Long> idList);

    List<XhDgBbTinhKhoHdr> findAllByIdIn(List<Long> listId);
}