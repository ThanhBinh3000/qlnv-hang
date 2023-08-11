package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcC;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCHdr;
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
public interface DcnbQuyetDinhDcCHdrRepository extends JpaRepository<DcnbQuyetDinhDcCHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbQuyetDinhDcCHdr c " +
            " LEFT JOIN DcnbQuyetDinhDcCDtl dtl on c.id = dtl.hdrId " +
            " LEFT JOIN DcnbKeHoachDcHdr kh on dtl.keHoachDcHdrId = kh.id " +
            " WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(c.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinh}),'%')) " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND ((:#{#param.ngayDuyetTcTu}  IS NULL OR c.ngayDuyetTc >= :#{#param.ngayDuyetTcTu})" +
            "AND (:#{#param.ngayDuyetTcDen}  IS NULL OR c.ngayDuyetTc <= :#{#param.ngayDuyetTcDen}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND c.type IS NULL " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "AND (:#{#param.loaiQdinh} IS NULL OR c.loaiQdinh = :#{#param.loaiQdinh}) " +
            "AND ((:#{#param.ngayHieuLucTu}  IS NULL OR c.ngayHieuLuc >= :#{#param.ngayHieuLucTu})" +
            "AND (:#{#param.ngayHieuLucDen}  IS NULL OR c.ngayHieuLuc <= :#{#param.ngayHieuLucDen}) ) " +
            "AND ((:#{#param.ngayKyQdinhTu}  IS NULL OR c.ngayKyQdinh >= :#{#param.ngayKyQdinhTu})" +
            "AND (:#{#param.ngayKyQdinhDen}  IS NULL OR c.ngayKyQdinh <= :#{#param.ngayKyQdinhDen}) ) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbQuyetDinhDcCHdr> searchCuc(@Param("param") SearchDcnbQuyetDinhDcC param, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO(c.id,c.soQdinh, c.ngayKyQdinh) FROM DcnbQuyetDinhDcCHdr c " +
            " LEFT JOIN DcnbQuyetDinhDcCDtl dtl on c.id = dtl.hdrId " +
            " LEFT JOIN DcnbKeHoachDcHdr kh on dtl.keHoachDcHdrId = kh.id " +
            " WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(c.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinh}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'')) OR (:#{#param.maDvi} IS NULL OR kh.maDvi LIKE CONCAT(:#{#param.maDvi},''))) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (exists (SELECT distinct hdrv.id FROM DcnbKeHoachDcDtl dtlv JOIN DcnbKeHoachDcHdr hdrv ON hdrv.id = dtlv.hdrId JOIN QlnvDmVattu dmvt On dmvt.ma = dtlv.cloaiVthh  where hdrv.id = dtl.keHoachDcHdrId and (:#{#param.dsLoaiHang.isEmpty() } = true OR dmvt.loaiHang in :#{#param.dsLoaiHang})  ) ) " +
            "AND ((:#{#param.ngayDuyetTcTu}  IS NULL OR c.ngayDuyetTc >= :#{#param.ngayDuyetTcTu})" +
            "AND (:#{#param.ngayDuyetTcDen}  IS NULL OR c.ngayDuyetTc <= :#{#param.ngayDuyetTcDen}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.types.isEmpty() } = true OR c.type IS NULL) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "AND (:#{#param.loaiQdinh} IS NULL OR c.loaiQdinh = :#{#param.loaiQdinh}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<DcnbQuyetDinhDcCHdrDTO> searchListCuc(@Param("param") SearchDcnbQuyetDinhDcC param);

    @Query(value = "SELECT distinct c FROM DcnbQuyetDinhDcCHdr c " +
            " LEFT JOIN DcnbQuyetDinhDcCDtl dtl on c.id = dtl.hdrId " +
            " LEFT JOIN DcnbKeHoachDcHdr kh on dtl.keHoachDcHdrId = kh.id " +
            " WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(c.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinh}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND c.type IN :#{#param.types} " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "AND (:#{#param.loaiQdinh} IS NULL OR c.loaiQdinh = :#{#param.loaiQdinh}) " +
            "AND ((:#{#param.ngayDuyetTcTu}  IS NULL OR c.ngayDuyetTc >= :#{#param.ngayDuyetTcTu})" +
            "AND (:#{#param.ngayDuyetTcDen}  IS NULL OR c.ngayDuyetTc <= :#{#param.ngayDuyetTcDen}) ) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbQuyetDinhDcCHdr> searchChiCuc(@Param("param") SearchDcnbQuyetDinhDcC param, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO(c.id,c.soQdinh, c.ngayKyQdinh) FROM DcnbQuyetDinhDcCHdr c " +
            " LEFT JOIN DcnbQuyetDinhDcCDtl dtl on c.id = dtl.hdrId " +
            "LEFT JOIN DcnbKeHoachDcHdr kh on dtl.keHoachDcHdrId = kh.id " +
            " WHERE 1=1 " +
            "AND (:#{#param.soQdinh} IS NULL OR LOWER(c.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinh}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'')) OR (:#{#param.maDvi} IS NULL OR kh.maDvi LIKE CONCAT(:#{#param.maDvi},''))) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (exists (SELECT distinct hdrv.id FROM DcnbKeHoachDcDtl dtlv JOIN DcnbKeHoachDcHdr hdrv ON hdrv.id = dtlv.hdrId JOIN QlnvDmVattu dmvt On dmvt.ma = dtlv.cloaiVthh  where hdrv.id = dtl.keHoachDcHdrId and (:#{#param.dsLoaiHang.isEmpty() } = true OR dmvt.loaiHang in :#{#param.dsLoaiHang})  ) ) " +
            "AND ((:#{#param.ngayDuyetTcTu}  IS NULL OR c.ngayDuyetTc >= :#{#param.ngayDuyetTcTu})" +
            "AND (:#{#param.ngayDuyetTcDen}  IS NULL OR c.ngayDuyetTc <= :#{#param.ngayDuyetTcDen}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.types.isEmpty() } = true OR c.type IN :#{#param.types}) " +
            "AND (c.type IS NULL OR (:#{#param.type} IS NULL OR c.type = :#{#param.type}))" +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "AND (:#{#param.loaiQdinh} IS NULL OR c.loaiQdinh = :#{#param.loaiQdinh}) " +
            "GROUP BY c.id, c.soQdinh, c.ngayKyQdinh " +
            "ORDER BY c.id desc"
    )
    List<DcnbQuyetDinhDcCHdrDTO> searchListChiCuc(@Param("param") SearchDcnbQuyetDinhDcC param);

    void deleteAllByIdIn(List<Long> listId);

    List<DcnbQuyetDinhDcCHdr> findByIdIn(List<Long> ids);

    List<DcnbQuyetDinhDcCHdr> findAllByIdIn(List<Long> listId);
    @Query("SELECT c FROM DcnbQuyetDinhDcCHdr c WHERE c.type is null and c.soQdinh = ?1")
    Optional<DcnbQuyetDinhDcCHdr> findFirstBySoQdinhAndTypeIsNull(String soQdinh);

    @Query(value = "SELECT CAST(COUNT(dtl.SO_LUONG_DC) AS DECIMAL) FROM DCNB_KE_HOACH_DC_HDR hdr " +
            "JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = hdr.ID WHERE dtl.CLOAI_VTHH = ?1 AND dtl.MA_LO_KHO = ?2", nativeQuery = true)
    BigDecimal countTongKeHoachDeXuat(String cloaiVthh, String maLoKho);

    @Query("SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO(" +
            "hdr.id, hdr.soQdinh , hdr.ngayKyQdinh) " +
            "FROM DcnbQuyetDinhDcCHdr hdr\n" +
            "LEFT JOIN DcnbQuyetDinhDcCDtl dtl ON dtl.hdrId = hdr.id \n" +
            "LEFT JOIN DcnbKeHoachDcHdr h On h.id  = dtl.keHoachDcHdrId \n" +
            "LEFT JOIN DcnbKeHoachDcDtl d ON d.hdrId  = h.id " +
            "WHERE hdr.loaiDc  = ?1 AND hdr.trangThai = ?2 AND hdr.maDvi = ?3 AND (hdr.loaiQdinh is null or hdr.loaiQdinh = ?4) and hdr.type is null group by hdr.id,hdr.soQdinh,hdr.ngayKyQdinh")
    List<DcnbQuyetDinhDcCHdrDTO> findByLoaiDcAndTrangThai(String loaiDc, String trangThai,String maDonVi, String loaiQdinh);

    List<DcnbQuyetDinhDcCHdr> findBySoQdinh(String soQdinh);
}