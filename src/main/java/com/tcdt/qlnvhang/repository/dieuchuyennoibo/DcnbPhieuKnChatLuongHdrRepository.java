package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKnChatLuong;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuKnChatLuongHdrRepository extends JpaRepository<DcnbPhieuKnChatLuongHdr, Long> {
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO(" +
            "pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho, khdcd.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.thuKho,khdcd.maNganKho,khdcd.tenNganKho, qdc.ngayHieuLuc,khdcd.donViTinh," +
            " khdcd.thuKhoId, khdcd.thuKhoNhanId, khdcd.thuKhoNhan,pkncl.bbNhapDayKhoId,pkncl.soNhapDayKho,pkncl.ngayNhapDayKho,pkncl.ngayLapBbTinhKho,khdcd.id) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.keHoachDcDtlId = khdcd.id AND (pkncl.type IS NULL OR (:#{#param.type} IS NULL OR pkncl.type = :#{#param.type})) " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On pkncl.bbLayMauId = bblm.id " +
            "LEFT JOIN DcnbBbNhapDayKhoHdr bbndk On bbndk.qdDcCucId = qdc.id and bbndk.trangThai = '17'" +
            "and ((khdcd.maLoKho is not null and bbndk.maLoKho = khdcd.maLoKho and bbndk.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and bbndk.maNganKho = khdcd.maNganKho))" +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtkh On bbtkh.qDinhDccId = qdc.id and bbtkh.maLoKho = khdcd.maLoKho and bbtkh.maNganKho = khdcd.maNganKho  " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (pkncl.type IS NULL OR (:#{#param.type} IS NULL OR pkncl.type = :#{#param.type})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pkncl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pkncl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(bblm.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soBbXuatDocKho} IS NULL OR LOWER(bbtkh.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbXuatDocKho}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pkncl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pkncl.ngayKiem <= :#{#param.denNgay}) ) " +
            "GROUP BY pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho, khdcd.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.thuKho,khdcd.maNganKho,khdcd.tenNganKho, qdc.ngayHieuLuc,khdcd.donViTinh," +
            " khdcd.thuKhoId, khdcd.thuKhoNhanId, khdcd.thuKhoNhan,pkncl.bbNhapDayKhoId,pkncl.soNhapDayKho,pkncl.ngayNhapDayKho,pkncl.ngayLapBbTinhKho,khdcd.id "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbPhieuKnChatLuongHdrDTO> searchPageXuat(@Param("param") SearchPhieuKnChatLuong req, Pageable pageable);

    Optional<DcnbPhieuKnChatLuongHdr> findFirstBySoPhieu(String soPhieu);

    List<DcnbPhieuKnChatLuongHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuKnChatLuongHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO(" +
            "pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,bblm.thoiHanDieuChuyen,bblm.maDiemKho,bblm.tenDiemKho,bblm.maLoKho," +
            "bblm.tenLoKho, bblm.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,bblm.loaiVthh,bblm.tenLoaiVthh,bblm.cloaiVthh,bblm.tenCloaiVthh,bblm.maNhaKho," +
            "bblm.tenNhaKho,bblm.tenThuKho,bblm.maNganKho,bblm.tenNganKho, qdc.ngayHieuLuc,bblm.donViTinh, bblm.thuKho,pkncl.bbNhapDayKhoId,pkncl.soNhapDayKho,pkncl.ngayNhapDayKho,pkncl.ngayLapBbTinhKho,pkncl.keHoachDcDtlId) " +
            "FROM DcnbPhieuKnChatLuongHdr pkncl " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On pkncl.bbLayMauId = bblm.id " +
            "LEFT JOIN DcnbQuyetDinhDcCHdr qdc On qdc.id = bblm.qdccId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = bblm.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (:#{#param.type} IS NULL OR pkncl.type = :#{#param.type}) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR bblm.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pkncl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR bblm.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pkncl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(bblm.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.maNganKho} IS NULL OR pkncl.maNganKho = :#{#param.maNganKho}) " +
            "AND (:#{#param.maLoKho} IS NULL OR pkncl.maLoKho = :#{#param.maLoKho}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pkncl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pkncl.ngayKiem <= :#{#param.denNgay}) ) " +
            "GROUP BY pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,bblm.thoiHanDieuChuyen,bblm.maDiemKho,bblm.tenDiemKho,bblm.maLoKho," +
            "bblm.tenLoKho, bblm.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,bblm.loaiVthh,bblm.tenLoaiVthh,bblm.cloaiVthh,bblm.tenCloaiVthh,bblm.maNhaKho," +
            "bblm.tenNhaKho,bblm.tenThuKho,bblm.maNganKho,bblm.tenNganKho, qdc.ngayHieuLuc,bblm.donViTinh, bblm.thuKho,pkncl.bbNhapDayKhoId,pkncl.soNhapDayKho,pkncl.ngayNhapDayKho,pkncl.ngayLapBbTinhKho,pkncl.keHoachDcDtlId "+
            "ORDER BY qdc.soQdinh DESC")
    List<DcnbPhieuKnChatLuongHdrDTO> searchList(@Param("param") SearchPhieuKnChatLuong objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO(" +
            "pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan, khdcd.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan," +
            "khdcd.thuKho,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, qdc.ngayHieuLuc,khdcd.donViTinh," +
            " khdcd.thuKhoId, khdcd.thuKhoNhanId, khdcd.thuKhoNhan,pkncl.bbNhapDayKhoId,pkncl.soNhapDayKho,pkncl.ngayNhapDayKho,pkncl.ngayLapBbTinhKho,khdcd.id) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.keHoachDcDtlId = khdcd.id AND (pkncl.type IS NULL OR (:#{#param.type} IS NULL OR pkncl.type = :#{#param.type})) " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On pkncl.bbLayMauId = bblm.id " +
            "LEFT JOIN DcnbBbNhapDayKhoHdr bbndk On bbndk.qdDcCucId = qdc.id and bbndk.trangThai = '17'" +
            "and ((khdcd.maLoKhoNhan is not null and bbndk.maLoKho = khdcd.maLoKhoNhan and bbndk.maNganKho = khdcd.maNganKhoNhan ) or (khdcd.maLoKhoNhan is null and bbndk.maNganKho = khdcd.maNganKhoNhan))" +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtkh On bbtkh.qDinhDccId = qdc.id and bbtkh.maLoKho = khdcd.maLoKho and bbtkh.maNganKho = khdcd.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (pkncl.type IS NULL OR (:#{#param.type} IS NULL OR pkncl.type = :#{#param.type})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pkncl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pkncl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(bblm.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.soBbXuatDocKho} IS NULL OR LOWER(bbtkh.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbXuatDocKho}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pkncl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pkncl.ngayKiem <= :#{#param.denNgay}) ) " +
            "GROUP BY pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan, khdcd.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan," +
            "khdcd.thuKho,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, qdc.ngayHieuLuc,khdcd.donViTinh," +
            " khdcd.thuKhoId, khdcd.thuKhoNhanId, khdcd.thuKhoNhan,pkncl.bbNhapDayKhoId,pkncl.soNhapDayKho,pkncl.ngayNhapDayKho,pkncl.ngayLapBbTinhKho,khdcd.id "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbPhieuKnChatLuongHdrDTO> searchPageNhan(@Param("param") SearchPhieuKnChatLuong req, Pageable pageable);

    List<DcnbPhieuKnChatLuongHdr> findByMaDviAndSoQdinhDcAndMaNganKhoAndMaLoKho(String maDvi, String soQdDcCuc, String maNganKho, String maLoKho);

    @Query(value ="SELECT distinct hdr FROM DcnbPhieuKnChatLuongHdr hdr " +
            "WHERE hdr.maDvi = ?1 AND hdr.soQdinhDc = ?2 AND hdr.maNganKho = ?3 and hdr.trangThai != '04' ")
    List<DcnbPhieuKnChatLuongHdr> findByMaDviAndSoQdinhDcAndMaNganKho(String dvql, String soQdinhDc, String maNganKho);
    @Query(value ="SELECT distinct hdr FROM DcnbPhieuKnChatLuongHdr hdr " +
            "WHERE hdr.maDvi = ?1 AND hdr.soQdinhDc = ?2 AND hdr.maLoKho = ?3 and hdr.trangThai != '04' ")
    List<DcnbPhieuKnChatLuongHdr> findByMaDviAndSoQdinhDcAndMaLoKho(String dvql, String soQdinhDc, String maLoKho);
}
