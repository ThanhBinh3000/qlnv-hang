package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhPhieuKtraCluongBttHdrRepository extends JpaRepository<XhPhieuKtraCluongBttHdr ,Long> {

    @Query("SELECT DISTINCT KN FROM XhPhieuKtraCluongBttHdr KN " +
            "WHERE (:#{#param.dvql} IS NULL OR KN.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR KN.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdNv} IS NULL OR KN.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soPhieuKiemNghiem} IS NULL OR KN.soPhieuKiemNghiem = :#{#param.soPhieuKiemNghiem}) " +
            "AND (:#{#param.ngayKiemNghiemMauTu} IS NULL OR KN.ngayKiemNghiemMau >= :#{#param.ngayKiemNghiemMauTu}) " +
            "AND (:#{#param.ngayKiemNghiemMauDen} IS NULL OR KN.ngayKiemNghiemMau <= :#{#param.ngayKiemNghiemMauDen}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR KN.soBbLayMau = :#{#param.soBbLayMau}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR KN.soBbTinhKho = :#{#param.soBbTinhKho}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR KN.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR KN.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCon} IS NULL OR KN.maDviCon LIKE CONCAT(:#{#param.maDviCon}, '%')) " +
            "ORDER BY KN.namKh DESC, KN.ngaySua DESC, KN.ngayTao DESC, KN.id DESC")
    Page<XhPhieuKtraCluongBttHdr> searchPage(@Param("param") XhPhieuKtraCluongBttHdrReq param, Pageable pageable);

    boolean existsBySoPhieuKiemNghiem(String soPhieuKiemNghiem);

    boolean existsBySoPhieuKiemNghiemAndIdNot(String soPhieuKiemNghiem, Long id);

    List<XhPhieuKtraCluongBttHdr> findByIdIn(List<Long> idList);

    List<XhPhieuKtraCluongBttHdr> findAllByIdIn(List<Long> listId);
}