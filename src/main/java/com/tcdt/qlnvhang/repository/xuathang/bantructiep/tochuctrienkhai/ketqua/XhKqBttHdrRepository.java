package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XhKqBttHdrRepository  extends JpaRepository<XhKqBttHdr, Long> {

    @Query("SELECT DISTINCT KQ FROM XhKqBttHdr KQ " +
            " LEFT JOIN XhKqBttDtl DTL on KQ.id = DTL.idHdr " +
            " LEFT JOIN XhQdPdKhBttDtl QDDTL on KQ.id = QDDTL.idQdKq " +
            " LEFT JOIN XhHopDongBttHdr HD on KQ.id = HD.idQdKq" + " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR KQ.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.namKh} IS NULL OR KQ.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(KQ.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR QDDTL.ngayNhanCgia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiaDen} IS NULL OR QDDTL.ngayNhanCgia <= :#{#param.ngayCgiaDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR KQ.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soHd} IS NULL OR LOWER(HD.soHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHd}),'%' ) ) )" +
            "AND (:#{#param.tenHd} IS NULL OR LOWER(HD.tenHd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenHd}),'%'))) " +
            "AND (:#{#param.tenDviMua} IS NULL OR LOWER(HD.tenDviMua) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenDviMua}),'%'))) " +
            "AND (:#{#param.ngayPduyetTu} IS NULL OR HD.ngayPduyet >= :#{#param.ngayPduyetTu}) " +
            "AND (:#{#param.ngayPduyetDen} IS NULL OR HD.ngayPduyet <= :#{#param.ngayPduyetDen}) " +
            "AND (:#{#param.maChiCuc} IS NULL OR DTL.maDvi = :#{#param.maChiCuc}) "
    )
    Page<XhKqBttHdr> searchPage(@Param("param") XhKqBttHdrReq param, Pageable pageable);

    Optional<XhKqBttHdr> findBySoQdKq(String soQdKq);


}
