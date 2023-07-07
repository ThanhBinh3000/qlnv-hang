package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbLoKhoDTO;
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
    @Query(value = "SELECT distinct new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO(" +
            "bblm.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,bblm.trangThai, bblm.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho," +
            " khdcd.thuKhoId, khdcd.thuKho, khdcd.thuKhoNhanId, khdcd.thuKhoNhan, khdcd.donViTinh, khdcd.tenDonViTinh) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id and khdcd.maLoKho = bblm.maLoKho and khdcd.maNganKho = bblm.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (bblm.type IS NULL OR (:#{#param.type} IS NULL OR bblm.type = :#{#param.type})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.dViKiemNghiem} IS NULL OR bblm.dViKiemNghiem LIKE CONCAT(:#{#param.dViKiemNghiem},'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(bblm.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bblm.ngayLayMau >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bblm.ngayLayMau <= :#{#param.denNgay}) ) " +
            "ORDER BY bblm.soQdinhDcc desc, bblm.nam desc")
    Page<DcnbBienBanLayMauHdrDTO> searchPageChiCuc(@Param("param") SearchDcnbBienBanLayMau param, Pageable pageable);

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
    @Query(value = "SELECT distinct hdr FROM DcnbBienBanLayMauHdr hdr \n" +
            "LEFT JOIN DcnbQuyetDinhDcCHdr h ON h.id = hdr.qdccId  \n" +
            "LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = hdr.maDvi "+
            "WHERE dvi.parent.maDvi = ?1 AND hdr.trangThai = ?2 AND h.parentId  = ?3")
    List<DcnbBienBanLayMauHdrDTO> findByMaDviAndTrangThaiAndQdinhDcId(String dvql, String banHanh, Long qdDcCucId);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbLoKhoDTO(" +
            "dtl.maNhaKho, dtl.tenNhaKho, dtl.maDiemKho, dtl.tenDiemKho, dtl.maNganKho, dtl.tenNganKho, dtl.maLoKho, dtl.tenLoKho )"+
            "FROM DcnbQuyetDinhDcCHdr qdh " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdd on qdd.hdrId = qdh.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khh on  khh.id = qdd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl dtl on  dtl.hdrId = khh.id " +
            "WHERE 1 = 1 " +
            "AND (qdh.id = :#{#param.qdDcCucId}) " +
            "AND ((dtl.maLoKho IS NULL AND not exists (SELECT distinct hdr.id FROM DcnbBienBanLayMauHdr hdr where hdr.qdccId = :#{#param.qdDcCucId} AND hdr.maNganKho = dtl.maNganKho )) " +
            "OR (dtl.maLoKho IS NOT NULL AND not exists (SELECT distinct hdr.id FROM DcnbBienBanLayMauHdr hdr where hdr.qdccId = :#{#param.qdDcCucId} AND hdr.maLoKho = dtl.maLoKho )))"
    )
    List<DcnbLoKhoDTO> danhSachMaLokho(@Param("param") SearchDcnbBienBanLayMau objReq);
    @Query(value = "SELECT distinct new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO(" +
            "bblm.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,bblm.trangThai, bblm.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.thuKhoId, khdcd.thuKho, khdcd.thuKhoNhanId, khdcd.thuKhoNhan, khdcd.donViTinh, khdcd.tenDonViTinh) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = dtlh.qdCcId and khdcd.maLoKho = bblm.maLoKho and khdcd.maNganKho = bblm.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 "+
            "AND dtlh.type = :#{#param.typeDataLink} AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (bblm.type is null or (:#{#param.type} IS NULL OR bblm.type = :#{#param.type})) "+
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (:#{#param.dViKiemNghiem} IS NULL OR bblm.dViKiemNghiem LIKE CONCAT(:#{#param.dViKiemNghiem},'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(bblm.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bblm.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bblm.ngayLayMau >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bblm.ngayLayMau <= :#{#param.denNgay}) ) " +
            "ORDER BY bblm.soQdinhDcc desc, bblm.nam desc")
    Page<DcnbBienBanLayMauHdrDTO> searchPageCuc(@Param("param") SearchDcnbBienBanLayMau req, Pageable pageable);
}
