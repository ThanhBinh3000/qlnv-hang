package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.response.DieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO;
import com.tcdt.qlnvhang.response.DieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBienBanLayMauHdrRepository extends JpaRepository<DcnbBienBanLayMauHdr, Long> {

    @Query(value = "SELECT distinct hdr FROM DcnbBienBanLayMauHdr hdr " +
            " LEFT JOIN DcnbBienBanLayMauDtl d On d.hdrId = hdr.id " +
            " LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = hdr.maDvi WHERE 1=1 " +
            "AND ((:#{#param.maDvi} IS NULL OR hdr.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.DViKiemNghiem} IS NULL OR hdr.dViKiemNghiem LIKE CONCAT(:#{#param.DViKiemNghiem},'%')) " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(hdr.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(hdr.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayLayMau >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayLayMau <= :#{#param.denNgay}) ) " +
            "ORDER BY hdr.soQdinhDcc desc, hdr.nam desc")
    Page<DcnbBienBanLayMauHdr> search(@Param("param") SearchDcnbBienBanLayMau param, Pageable pageable);

    @Query(value = "SELECT distinct hdr FROM DcnbBienBanLayMauHdr hdr " +
            " LEFT JOIN DcnbBienBanLayMauDtl d On d.hdrId = hdr.id " +
            " LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = hdr.maDvi WHERE 1=1 " +
            "AND ((:#{#param.maDvi} IS NULL OR hdr.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.DViKiemNghiem} IS NULL OR hdr.dViKiemNghiem LIKE CONCAT(:#{#param.DViKiemNghiem},'%')) " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(hdr.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(hdr.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayLayMau >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayLayMau <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR hdr.trangThai = :#{#param.trangThai}) " +
            "ORDER BY hdr.soQdinhDcc desc, hdr.nam desc")
    List<DcnbBienBanLayMauHdr> searchList(@Param("param") SearchDcnbBienBanLayMau param);

    Optional<DcnbBienBanLayMauHdr> findFirstBySoBbLayMau (String soBbLayMau);

    List<DcnbBienBanLayMauHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanLayMauHdr> findAllByIdIn(List<Long> idList);
    @Query("SELECT new com.tcdt.qlnvhang.response.DieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO(" +
            "hdr.qDinhDccId, hdr.soQdinhDcc , hdr.ngayLayMau) " +
            "FROM DcnbBienBanLayMauHdr hdr\n" +
            "LEFT JOIN DcnbKeHoachNhapXuat kh ON kh.idHdr = hdr.id " +
            "WHERE hdr.loaiDc  = ?1 AND hdr.trangThai = ?2 AND hdr.maDvi = ?3 AND kh.type = ?4 group by hdr.id,hdr.soQdinhDcc , hdr.ngayLayMau")
    List<DcnbBienBanLayMauHdrDTO> findByLoaiDcAndTrangThai(String loaiDc, String trangThai, String maDviCha, String type);

    @Query("SELECT new com.tcdt.qlnvhang.response.DieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO(" +
            "hdr.maLoKho) " +
            "FROM DcnbBienBanLayMauHdr hdr\n" +
            "LEFT JOIN DcnbKeHoachNhapXuat kh ON kh.idHdr = hdr.id " +
            "WHERE hdr.loaiDc  = ?1 AND hdr.qDinhDccId = ?2 AND hdr.trangThai = ?3  AND kh.type = ?4 group by hdr.maLoKho")
    List<DcnbBienBanLayMauHdrDTO> findByLoaiDcAndQDinhDccIdAndTrangThai(String loaiDc,Long qDinhDccId, String trangThai, String type);
}
