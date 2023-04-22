package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbKeHoachDc;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbKeHoachDcHdrRepository extends JpaRepository<DcnbKeHoachDcHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbKeHoachDcHdr c left join c.danhSachHangHoa h  WHERE 1=1 " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDviPq LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(c.soDxuat) LIKE CONCAT('%',LOWER(:#{#param.soDxuat}),'%')) " +
            "AND ((:#{#param.ngayLapKhTu}  IS NULL OR c.ngayLapKh >= :#{#param.ngayLapKhTu})" +
            "AND (:#{#param.ngayLapKhDen}  IS NULL OR c.ngayLapKh <= :#{#param.ngayLapKhDen}) ) " +
            "AND ((:#{#param.ngayDuyetLdccTu}  IS NULL OR c.ngayDuyetLdcc >= :#{#param.ngayDuyetLdccTu})" +
            "AND (:#{#param.ngayDuyetLdccDen}  IS NULL OR c.ngayDuyetLdcc <= :#{#param.ngayDuyetLdccDen}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR h.cloaiVthh = :#{#param.cloaiVthh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR h.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbKeHoachDcHdr> search(@Param("param") SearchDcnbKeHoachDc param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<DcnbKeHoachDcHdr> findByIdIn(List<Long> ids);

    List<DcnbKeHoachDcHdr> findAllByIdIn(List<Long> listId);

    Optional<DcnbKeHoachDcHdr> findFirstBySoDxuat(String soDxuat);

    @Query(value = "SELECT CAST(COUNT(dtl.SO_LUONG_DC) AS DECIMAL) FROM DCNB_KE_HOACH_DC_HDR hdr " +
            "JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = hdr.ID WHERE dtl.CLOAI_VTHH = ?1 AND dtl.MA_LO_KHO = ?2", nativeQuery = true)
    BigDecimal countTongKeHoachDeXuat(String cloaiVthh, String maLoKho);

    @Query(nativeQuery = true, value = "SELECT * FROM DCNB_KE_HOACH_DC_HDR hdr WHERE hdr.MA_DVI = ?1  AND hdr.TRANG_THAI = ?2 AND (TO_DATE(TO_CHAR(hdr.NGAY_TAO ,'YYYY-MM-DD'),'YYYY-MM-DD') <= TO_DATE(?3,'YYYY-MM-DD'))")
    List<DcnbKeHoachDcHdr> findByDonViAndTrangThaiCuc(String maDVi, String trangThai, String thoiGianTongHop);
}