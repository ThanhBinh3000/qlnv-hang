package com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhHopDongHdrRepository extends BaseRepository<XhHopDongHdr,Long> {
    Optional<XhHopDongHdr> findBySoHd(String soHd);

    Optional<XhHopDongHdr> findFirstBySoHd(String soHd);


    @Query("SELECT c FROM XhHopDongHdr c where 1 = 1" +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdKq} IS NULL OR c.soQdKq = :#{#param.soQdKq}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.typeQdGnv} IS NULL OR LOWER(c.typeQdGnv) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.typeQdGnv}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    Page<XhHopDongHdr> selectPage(@Param("param") XhHopDongHdrReq param, Pageable pageable);

//    @Query(
//            value = "SELECT * " +
//                    "FROM XH_HOP_DONG_HDR  HDR" +
//                    " WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH = :loaiVthh) " +
//                    "  AND (:soHd IS NULL OR HDR.SO_HD = :soHd) " +
//                    "  AND (:tenHd IS NULL OR HDR.TEN_HD = :tenHd) " +
//                    "  AND (:nhaCcap IS NULL OR HDR.SO_HD = :nhaCcap) " +
//                    "  AND (:tuNgayKy IS NULL OR HDR.NGAY_KY >= TO_DATE(:tuNgayKy, 'yyyy-MM-dd')) " +
//                    "  AND (:denNgayKy IS NULL OR HDR.NGAY_KY <= TO_DATE(:denNgayKy, 'yyyy-MM-dd')) " +
//                    "  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
//            nativeQuery = true)
//    Page<XhHopDongHdr> select(String loaiVthh, String soHd, String tenHd, String nhaCcap, String tuNgayKy, String denNgayKy, String trangThai, Pageable pageable);

    List<XhHopDongHdr> findByIdIn(Collection<Long> ids);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    List<XhHopDongHdr> findAllBySoQdKq(String soQdKq);


    @Transactional
    List<XhHopDongHdr> findAllByIdHd(Long idHd);

    @Query(value =  "SELECT COUNT(*) AS count FROM XH_HOP_DONG_HDR WHERE TRANG_THAI = '30' AND SO_QD_KQ =:soQdKq",
            nativeQuery = true)
    Long countSlHopDongDaKy(String soQdKq);

}
