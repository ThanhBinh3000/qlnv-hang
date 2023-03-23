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

    @Query("SELECT c FROM XhPhieuKtraCluongBttHdr c " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR c.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    Page<XhPhieuKtraCluongBttHdr> searchPage(@Param("param") XhPhieuKtraCluongBttHdrReq param, Pageable pageable);

    Optional<XhPhieuKtraCluongBttHdr> findBySoPhieu(String soPhieu);

    List<XhPhieuKtraCluongBttHdr> findAllByIdQd(Long idQd);

    List<XhPhieuKtraCluongBttHdr> findAllByIdDdiemXh(Long idDdiemXh);
}
