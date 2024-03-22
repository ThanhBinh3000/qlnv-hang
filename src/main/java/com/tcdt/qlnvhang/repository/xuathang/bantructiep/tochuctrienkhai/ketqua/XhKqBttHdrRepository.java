package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhKqBttHdrRepository extends JpaRepository<XhKqBttHdr, Long> {

    @Query("SELECT DISTINCT QD FROM XhKqBttHdr QD " +
            "LEFT JOIN XhHopDongBttHdr HĐ ON QD.id = HĐ.idQdKq " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdKq} IS NULL OR LOWER(QD.soQdKq) LIKE LOWER(CONCAT('%', :#{#param.soQdKq}, '%'))) " +
            "AND (:#{#param.soHopDong} IS NULL OR LOWER(HĐ.soHopDong) LIKE LOWER(CONCAT('%', :#{#param.soHopDong}, '%'))) " +
            "AND (:#{#param.tenHopDong} IS NULL OR LOWER(HĐ.tenHopDong) LIKE CONCAT(:#{#param.tenHopDong},'%')) " +
            "AND (:#{#param.tenBenMua} IS NULL OR LOWER(HĐ.tenBenMua) LIKE CONCAT(:#{#param.tenBenMua},'%')) " +
            "AND (:#{#param.ngayKyHopDongTu} IS NULL OR HĐ.ngayKyHopDong >= :#{#param.ngayKyHopDongTu}) " +
            "AND (:#{#param.ngayKyHopDongDen} IS NULL OR HĐ.ngayKyHopDong <= :#{#param.ngayKyHopDongDen}) " +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR EXISTS (SELECT 1 FROM XhQdPdKhBttDtl DTL WHERE DTL.idQdKq = QD.id AND DTL.ngayNhanCgia >= :#{#param.ngayCgiaTu})) " +
            "AND (:#{#param.ngayCgiaDen} IS NULL OR EXISTS (SELECT 1 FROM XhQdPdKhBttDtl DTL WHERE DTL.idQdKq = QD.id AND DTL.ngayNhanCgia <= :#{#param.ngayCgiaDen})) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY QD.namKh DESC, QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhKqBttHdr> searchPage(@Param("param") XhKqBttHdrReq param, Pageable pageable);

    boolean existsBySoQdKq(String soQdKq);

    boolean existsBySoQdKqAndIdNot(String soQdKq, Long id);

    List<XhKqBttHdr> findByIdIn(List<Long> idQdList);

    List<XhKqBttHdr> findAllByIdIn(List<Long> listId);
}