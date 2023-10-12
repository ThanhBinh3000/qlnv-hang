package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface XhPhieuXkhoBttReposytory extends JpaRepository<XhPhieuXkhoBtt, Long> {

    @Query("SELECT DISTINCT XK FROM XhPhieuXkhoBtt XK " +
            "LEFT JOIN XhQdNvXhBttHdr QD ON QD.id = XK.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR XK.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR XK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR XK.soPhieuXuatKho = :#{#param.soPhieuXuatKho}) " +
            "AND (:#{#param.soQdNv} IS NULL OR XK.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR XK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.ngayLapPhieuTu} IS NULL OR XK.ngayLapPhieu >= :#{#param.ngayLapPhieuTu}) " +
            "AND (:#{#param.ngayLapPhieuDen} IS NULL OR XK.ngayLapPhieu <= :#{#param.ngayLapPhieuDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR XK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY XK.namKh DESC, XK.ngaySua DESC, XK.ngayTao DESC, XK.id DESC")
    Page<XhPhieuXkhoBtt> searchPage(@Param("param") XhPhieuXkhoBttReq param, Pageable pageable);

    boolean existsBySoPhieuXuatKho(String soPhieuXuatKho);

    boolean existsBySoPhieuXuatKhoAndIdNot(String soPhieuXuatKho, Long id);

    List<XhPhieuXkhoBtt> findByIdIn(List<Long> idList);

    List<XhPhieuXkhoBtt> findAllByIdIn(List<Long> listId);
}