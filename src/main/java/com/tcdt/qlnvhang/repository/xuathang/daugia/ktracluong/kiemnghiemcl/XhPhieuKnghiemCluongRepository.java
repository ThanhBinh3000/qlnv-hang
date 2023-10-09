package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhPhieuKnghiemCluongRepository extends BaseRepository<XhPhieuKnghiemCluong, Long> {

    @Query("SELECT DISTINCT KN FROM XhPhieuKnghiemCluong KN " +
            "WHERE (:#{#param.dvql} IS NULL OR KN.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR KN.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdNv} IS NULL OR KN.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soPhieuKiemNghiem} IS NULL OR KN.soPhieuKiemNghiem = :#{#param.soPhieuKiemNghiem}) " +
            "AND (:#{#param.ngayKiemNghiemMauTu} IS NULL OR KN.ngayKiemNghiemMau >= :#{#param.ngayKiemNghiemMauTu}) " +
            "AND (:#{#param.ngayKiemNghiemMauDen} IS NULL OR KN.ngayKiemNghiemMau <= :#{#param.ngayKiemNghiemMauDen}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR KN.soBbLayMau = :#{#param.soBbLayMau}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR KN.soBbTinhKho = :#{#param.soBbTinhKho}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR KN.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR KN.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCon} IS NULL OR KN.maDviCon LIKE CONCAT(:#{#param.maDviCon}, '%')) " +
            "ORDER BY KN.nam DESC, KN.ngaySua DESC, KN.ngayTao DESC, KN.id DESC")
    Page<XhPhieuKnghiemCluong> searchPage(@Param("param") XhPhieuKnghiemCluongReq param, Pageable pageable);

    boolean existsBySoPhieuKiemNghiem(String soPhieuKiemNghiem);

    boolean existsBySoPhieuKiemNghiemAndIdNot(String soPhieuKiemNghiem, Long id);

    List<XhPhieuKnghiemCluong> findByIdIn(List<Long> idList);

    List<XhPhieuKnghiemCluong> findAllByIdIn(List<Long> listId);
}
