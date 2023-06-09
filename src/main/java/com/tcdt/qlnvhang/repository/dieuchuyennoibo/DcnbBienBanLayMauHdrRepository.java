package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO;
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
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO(" +
            "bblm.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,bblm.trangThai, bblm.trangThai) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29'"+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.DViKiemNghiem} IS NULL OR bblm.dViKiemNghiem LIKE CONCAT(:#{#param.DViKiemNghiem},'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(bblm.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bblm.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bblm.ngayLayMau >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bblm.ngayLayMau <= :#{#param.denNgay}) ) " +
            "ORDER BY bblm.soQdinhDcc desc, bblm.nam desc")
    Page<DcnbBienBanLayMauHdrDTO> searchPage(@Param("param") SearchDcnbBienBanLayMau param, Pageable pageable);

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
}
