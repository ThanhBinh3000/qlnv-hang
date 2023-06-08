package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrReq;
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

    @Query("SELECT CL FROM XhPhieuKtraCluongBttHdr CL " +
            "LEFT JOIN XhBbLayMauBttHdr LM on LM.id = CL.idBienBan WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR CL.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(CL.soQdNv) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdNv}),'%' ) ) )" +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(CL.soPhieu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soPhieu}),'%' ) ) )" +
            "AND (:#{#param.ngayKnghiemTu} IS NULL OR CL.ngayKnghiem >= :#{#param.ngayKnghiemTu}) " +
            "AND (:#{#param.ngayKnghiemDen} IS NULL OR CL.ngayKnghiem <= :#{#param.ngayKnghiemDen}) " +
            "AND (:#{#param.soBienBan} IS NULL OR LOWER(CL.soBienBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBienBan}),'%' ) ) )" +
            "AND (:#{#param.soBbXuatDoc} IS NULL OR LOWER(CL.soBbXuatDoc) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBbXuatDoc}),'%' ) ) )" +
            "AND (:#{#param.maChiCuc} IS NULL OR LM.maDvi LIKE CONCAT(:#{#param.maChiCuc},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR CL.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(CL.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR CL.trangThai = :#{#param.trangThai}) "
    )
    Page<XhPhieuKtraCluongBttHdr> searchPage(@Param("param") XhPhieuKtraCluongBttHdrReq param, Pageable pageable);

    Optional<XhPhieuKtraCluongBttHdr> findBySoPhieu(String soPhieu);

    List<XhPhieuKtraCluongBttHdr> findByIdIn(List<Long> idList);


}
