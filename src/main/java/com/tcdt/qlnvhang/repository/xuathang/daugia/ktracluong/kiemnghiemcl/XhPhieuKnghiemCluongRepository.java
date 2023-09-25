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

    @Query("SELECT DISTINCT QD FROM XhPhieuKnghiemCluong QD " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR QD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdNv} IS NULL OR QD.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soPhieuKiemNghiem} IS NULL OR QD.soPhieuKiemNghiem = :#{#param.soPhieuKiemNghiem}) " +
            "AND (:#{#param.ngayKiemNghiemMauTu} IS NULL OR QD.ngayKiemNghiemMau >= :#{#param.ngayKiemNghiemMauTu}) " +
            "AND (:#{#param.ngayKiemNghiemMauDen} IS NULL OR QD.ngayKiemNghiemMau <= :#{#param.ngayKiemNghiemMauDen}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR QD.soBbLayMau = :#{#param.soBbLayMau}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR QD.soBbTinhKho = :#{#param.soBbTinhKho}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhPhieuKnghiemCluong> searchPage(@Param("param") XhPhieuKnghiemCluongReq param, Pageable pageable);

    boolean existsBySoPhieuKiemNghiem(String soPhieuKiemNghiem);

    boolean existsBySoPhieuKiemNghiemAndIdNot(String soPhieuKiemNghiem, Long id);

    List<XhPhieuKnghiemCluong> findByIdIn(List<Long> idList);

    List<XhPhieuKnghiemCluong> findAllByIdIn(List<Long> listId);
}
