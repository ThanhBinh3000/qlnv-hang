package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface XhKqBdgHdrRepository extends JpaRepository<XhKqBdgHdr, Long> {

    @Query("SELECT KQ FROM XhKqBdgHdr KQ WHERE " +
            "(:#{#param.dvql} IS NULL OR KQ.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR KQ.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdKq} IS NULL OR LOWER(KQ.soQdKq) LIKE LOWER(CONCAT('%',:#{#param.soQdKq},'%'))) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(KQ.trichYeu) LIKE LOWER(CONCAT('%',:#{#param.trichYeu},'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR KQ.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR LOWER(KQ.cloaiVthh) LIKE CONCAT(:#{#param.cloaiVthh},'%')) " +
            "AND (:#{#param.ngayKyTu} IS NULL OR KQ.ngayKy >= :#{#param.ngayKyTu}) " +
            "AND (:#{#param.ngayKyDen} IS NULL OR KQ.ngayKy <= :#{#param.ngayKyDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR KQ.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiHd} IS NULL OR KQ.trangThaiHd = :#{#param.trangThaiHd}) " +
            "ORDER BY KQ.nam DESC, KQ.ngaySua DESC, KQ.ngayTao DESC, KQ.id DESC")
    Page<XhKqBdgHdr> searchPage(@Param("param") XhKqBdgHdrReq param, Pageable pageable);

    boolean existsBySoQdKq(String soQdKq);

    Optional<XhKqBdgHdr> findByMaThongBao(String maThongBao);

    boolean existsBySoQdKqAndIdNot(String soQdKq, Long id);

    List<XhKqBdgHdr> findByIdIn(List<Long> idQdList);

    List<XhKqBdgHdr> findAllByIdIn(List<Long> listId);

    Optional<XhKqBdgHdr> findFirstBySoQdKq(String soQdKq);
}