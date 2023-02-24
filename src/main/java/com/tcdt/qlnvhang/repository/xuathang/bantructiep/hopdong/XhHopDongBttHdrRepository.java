package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhHopDongBttHdrRepository extends JpaRepository<XhHopDongBttHdr, Long> {

    @Query("SELECT DX from XhHopDongBttHdr DX WHERE 1 = 1 " +
            "AND (:#{#param.namHd} IS NULL OR DX.namHd = :#{#param.namHd}) " +
            "AND (:#{#param.soHd} IS NULL OR LOWER(DX.soHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHd}),'%' ) ) )" +
            "AND (:#{#param.tenHd} IS NULL OR LOWER(DX.tenHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenHd}),'%'))) " +
            "AND (:#{#param.tenDviMua} IS NULL OR LOWER(DX.tenDviMua) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenDviMua}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
    Page<XhHopDongBttHdr> searchPage(@Param("param") XhHopDongBttHdrReq param, Pageable pageable);

    Optional<XhHopDongBttHdr> findBySoHd(String soHd);

    @Transactional
    List<XhHopDongBttHdr> findAllByIdQdKq(Long idQdKq);

}
