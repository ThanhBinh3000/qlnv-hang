package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcTc;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcTcHdr;
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
public interface DcnbQuyetDinhDcTcHdrRepository extends JpaRepository<DcnbQuyetDinhDcTcHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbQuyetDinhDcTcHdr c WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(c.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinh}),'%')) " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND ((:#{#param.ngayDuyetTcTu}  IS NULL OR c.ngayDuyetTc >= :#{#param.ngayDuyetTcTu})" +
            "AND (:#{#param.ngayDuyetTcDen}  IS NULL OR c.ngayDuyetTc <= :#{#param.ngayDuyetTcDen}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbQuyetDinhDcTcHdr> search(@Param("param") SearchDcnbQuyetDinhDcTc param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<DcnbQuyetDinhDcTcHdr> findByIdIn(List<Long> ids);

    List<DcnbQuyetDinhDcTcHdr> findAllByIdIn(List<Long> listId);

    Optional<DcnbQuyetDinhDcTcHdr> findFirstBySoQdinh(String soQdinh);

    @Query(value = "SELECT CAST(COUNT(dtl.SO_LUONG_DC) AS DECIMAL) FROM DCNB_KE_HOACH_DC_HDR hdr " +
            "JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = hdr.ID WHERE dtl.CLOAI_VTHH = ?1 AND dtl.MA_LO_KHO = ?2", nativeQuery = true)
    BigDecimal countTongKeHoachDeXuat(String cloaiVthh, String maLoKho);
    @Query(value = "SELECT distinct * FROM DCNB_QUYET_DINH_DC_TC_HDR hdr WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(hdr.SO_QDINH) LIKE CONCAT('%', CONCAT(LOWER(:#{#param.soQdinh}),'%'))) " +
            "AND EXISTS(SELECT DISTINCT * FROM DCNB_QUYET_DINH_DC_TC_DTL dtl JOIN DCNB_KE_HOACH_DC_HDR dchdr ON dtl.DCNB_KE_HOACH_DC_HDR_ID = dchdr.ID JOIN DCNB_KE_HOACH_DC_DTL dcdtl ON dcdtl.HDR_ID = dchdr.ID WHERE dtl.HDR_ID = hdr.ID AND (dchdr.MA_DVI = CONCAT(:#{#param.maDvi},'') OR dchdr.MA_DVI LIKE CONCAT(:#{#param.maDvi},'%'))) " +
            "AND (:#{#param.loaiDc} IS NULL OR hdr.LOAI_DC = :#{#param.loaiDc}) " +
            "ORDER BY hdr.NGAY_SUA desc , hdr.NGAY_TAO desc, hdr.id desc", nativeQuery = true
    )
    List<DcnbQuyetDinhDcTcHdr> findDanhSachQuyetDinhXuat(@Param("param")SearchDcnbQuyetDinhDcTc objReq);
    @Query(value = "SELECT distinct * FROM DCNB_QUYET_DINH_DC_TC_HDR hdr WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(hdr.SO_QDINH) LIKE CONCAT('%', CONCAT(LOWER(:#{#param.soQdinh}),'%'))) " +
            "AND EXISTS(SELECT DISTINCT * FROM DCNB_QUYET_DINH_DC_TC_DTL dtl JOIN DCNB_KE_HOACH_DC_HDR dchdr ON dtl.DCNB_KE_HOACH_DC_HDR_ID = dchdr.ID JOIN DCNB_KE_HOACH_DC_DTL dcdtl ON dcdtl.HDR_ID = dchdr.ID WHERE dtl.HDR_ID = hdr.ID AND (dcdtl.MA_CHI_CUC_NHAN = CONCAT(:#{#param.maDvi},'') OR dcdtl.MA_CHI_CUC_NHAN LIKE CONCAT(:#{#param.maDvi},'%'))) " +
            "AND (:#{#param.loaiDc} IS NULL OR hdr.LOAI_DC = :#{#param.loaiDc}) " +
            "ORDER BY hdr.NGAY_SUA desc , hdr.NGAY_TAO desc, hdr.id desc", nativeQuery = true
    )
    List<DcnbQuyetDinhDcTcHdr> findDanhSachQuyetDinhNhan(@Param("param")SearchDcnbQuyetDinhDcTc objReq);

    List<DcnbQuyetDinhDcTcHdr> findByIdThopIn(List<Long> ids);
}