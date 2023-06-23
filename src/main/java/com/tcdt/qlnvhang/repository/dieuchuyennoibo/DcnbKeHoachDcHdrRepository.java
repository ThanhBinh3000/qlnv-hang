package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbKeHoachDc;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
            "AND c.type in ('DC','NDC') " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbKeHoachDcHdr> search(@Param("param") SearchDcnbKeHoachDc param, Pageable pageable);

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
            "AND ( c.trangThai != '00') " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
            "AND c.type in ('DC','NDC') " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbKeHoachDcHdr> searchCuc(@Param("param") SearchDcnbKeHoachDc param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<DcnbKeHoachDcHdr> findByIdIn(List<Long> ids);
    List<DcnbKeHoachDcHdr> findByParentIdIn(List<Long> ids);

    List<DcnbKeHoachDcHdr> findAllByIdIn(List<Long> listId);

    Optional<DcnbKeHoachDcHdr> findFirstBySoDxuat(String soDxuat);

    @Query(value = "SELECT CAST(COUNT(dtl.SO_LUONG_DC) AS DECIMAL) FROM DCNB_KE_HOACH_DC_HDR hdr " +
            "JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = hdr.ID WHERE dtl.CLOAI_VTHH = ?1 AND dtl.MA_LO_KHO = ?2 AND hdr.id = ?3", nativeQuery = true)
    BigDecimal countTongKeHoachDeXuatLoKho(String cloaiVthh, String maLoKho, Long hdrId);

    @Query(value = "SELECT CAST(COUNT(dtl.SO_LUONG_DC) AS DECIMAL) FROM DCNB_KE_HOACH_DC_HDR hdr " +
            "JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = hdr.ID WHERE dtl.CLOAI_VTHH = ?1 AND dtl.MA_NGAN_KHO = ?2 AND hdr.id = ?3", nativeQuery = true)
    BigDecimal countTongKeHoachDeXuatNganKho(String cloaiVthh, String maNganKho, Long hdrId);

    @Query(value = "SELECT distinct hdr FROM DcnbKeHoachDcHdr hdr " +
            "LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = hdr.maDvi WHERE dvi.parent.maDvi = ?1  " +
            "AND hdr.trangThai = ?2 AND hdr.loaiDc = ?3 AND hdr.type = ?4 "+
            "AND hdr.ngayTao <= ?5 AND hdr.idThop is null ")
    List<DcnbKeHoachDcHdr> findByDonViAndTrangThaiCucCuc(String maDVi, String trangThai, String loaiDieuChuyen, String type, LocalDateTime thoiGianTongHop);

    @Query(value = "FROM DcnbKeHoachDcHdr hdr WHERE hdr.maDvi = ?1 " +
            "AND hdr.trangThai = ?2 AND hdr.loaiDc = ?3 AND hdr.type = ?4 "+
            "AND hdr.ngayTao <= ?5 AND (hdr.idThop is null )")
    List<DcnbKeHoachDcHdr> findByDonViAndTrangThaiCucChiCuc(String maDVi, String trangThai, String loaiDieuChuyen, String type, LocalDateTime thoiGianTongHop);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE  DCNB_KE_HOACH_DC_HDR SET  TRANG_THAI = ?3 WHERE PARENT_ID = (SELECT HDR_ID FROM DCNB_KE_HOACH_DC_DTL WHERE ID = to_number(?1)) AND TYPE= ?2")
    void updateTrangThaiNdc(Long dtlId, String type, String trangThai);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE DCNB_KE_HOACH_DC_HDR SET ID_THOP = to_number(:idTh),MA_THOP = to_char(:idTh) WHERE ID IN :danhSachKeHoach", nativeQuery = true)
    void updateIdTongHop(Long idTh,List<Long> danhSachKeHoach);

    List<DcnbKeHoachDcHdr> findByParentIdAndType(Long dcKeHoachDcHdrId,String type);
    @Transactional()
    @Modifying
    @Query(value = "UPDATE DCNB_KE_HOACH_DC_HDR SET TRANG_THAI = '67', XD_LAI_DIEM_NHAP = 1 WHERE ID = ?1 AND LOAI_DC = ?2", nativeQuery = true)
    void updateTrangThaiTuChoiAndType(Long id, String loaiDieuChuyen);
}