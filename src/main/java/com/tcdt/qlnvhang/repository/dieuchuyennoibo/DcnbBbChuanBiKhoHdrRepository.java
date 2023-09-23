package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbChuanBiKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBbChuanBiKhoHdrRepository extends JpaRepository<DcnbBbChuanBiKhoHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbBangKeCanHangHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(c.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.soQdinhDcc desc , c.nam desc, c.id desc")
    Page<DcnbBbChuanBiKhoHdr> search(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbBbChuanBiKhoHdr> findBySoBban(String soBban);


    List<DcnbBbChuanBiKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbBbChuanBiKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO(" +
            "bbcb.id, qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc, bbcb.soBban, bbcb.ngayLap, pnk.id, pnk.soPhieuNhapKho, bbktnk.id," +
            "bbktnk.soBb,bbktnk.ngayKetThucNhap,bbcb.bbGiaoNhanId,bbcb.soBbGiaoNhan,khdcd.maNhaKhoNhan, khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan, khdcd.tenDiemKhoNhan, khdcd.maLoKhoNhan, " +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.soLuongDc,bbcb.trangThai,khdcd.donViTinh,khdcd.soLuongDc,khdcd.id,khdcd.duToanKphi) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBbChuanBiKhoHdr bbcb On bbcb.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbcb.idPhieuNhapKho " +
            "and ((khdcd.maLoKhoNhan is not null and khdcd.maLoKhoNhan = pnk.maLoKho and khdcd.maNganKhoNhan = pnk.maNganKho ) or (khdcd.maLoKhoNhan is null and khdcd.maNganKhoNhan = pnk.maNganKho ))" +
            "LEFT JOIN DcnbBBKetThucNKHdr bbktnk On bbktnk.qdinhDccId = qdc.id  " +
            "and ((khdcd.maLoKhoNhan is not null and khdcd.maLoKhoNhan = bbktnk.maLoKho and khdcd.maNganKhoNhan = bbktnk.maNganKho ) or (khdcd.maLoKhoNhan is null and khdcd.maNganKhoNhan = bbktnk.maNganKho ))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND (:#{#param.soBban} IS NULL OR LOWER(bbcb.soBban) LIKE CONCAT('%',LOWER(:#{#param.soBban}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbcb.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgayLapBb}  IS NULL OR bbcb.ngayLap >= :#{#param.tuNgayLapBb})" +
            "AND (:#{#param.denNgayLapBb}  IS NULL OR bbcb.ngayLap <= :#{#param.denNgayLapBb}) ) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR pnk.ngayLap <= :#{#param.denNgayKtnk})) " +
            "GROUP BY bbcb.id, qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc, bbcb.soBban, bbcb.ngayLap, pnk.id, pnk.soPhieuNhapKho, bbktnk.id," +
            "bbktnk.soBb,bbktnk.ngayKetThucNhap,bbcb.bbGiaoNhanId,bbcb.soBbGiaoNhan,khdcd.maNhaKhoNhan, khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan, khdcd.tenDiemKhoNhan, khdcd.maLoKhoNhan, " +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.soLuongDc,bbcb.trangThai,khdcd.donViTinh,khdcd.soLuongDc,khdcd.id,khdcd.duToanKphi "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBbChuanBiKhoHdrDTO> searchPage(@Param("param") DcnbBbChuanBiKhoHdrReq req, Pageable pageable);

    @Query(value = "SELECT bbcb " +
            "FROM DcnbBbChuanBiKhoHdr bbcb " +
            "WHERE 1 =1 " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(bbcb.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%'))) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(bbcb.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND (:#{#param.qdDcCucId} IS NULL OR LOWER(bbcb.qdDcCucId) LIKE CONCAT('%',LOWER(:#{#param.qdDcCucId}),'%')) " +
            "AND (:#{#param.soBban} IS NULL OR LOWER(bbcb.soBban) LIKE CONCAT('%',LOWER(:#{#param.soBban}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR bbcb.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiDc} IS NULL OR bbcb.loaiDc = :#{#param.loaiDc}) " +
            "AND (:#{#param.maNganKho} IS NULL OR bbcb.maNganKho = :#{#param.maNganKho}) " +
            "AND (:#{#param.maLoKho} IS NULL OR bbcb.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbcb.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgayLapBb}  IS NULL OR bbcb.ngayLap >= :#{#param.tuNgayLapBb})" +
            "AND (:#{#param.denNgayLapBb}  IS NULL OR bbcb.ngayLap <= :#{#param.denNgayLapBb}) ) " +
            "ORDER BY bbcb.soQdDcCuc DESC")
    List<DcnbBbChuanBiKhoHdr> searchList(@Param("param") DcnbBbChuanBiKhoHdrReq req);

    List<DcnbBbChuanBiKhoHdr> findByMaDviAndQdDcCucIdAndMaNganKho(String maDvi, Long qdDcCucId, String maNganKho);

    List<DcnbBbChuanBiKhoHdr> findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKho(String maDvi, Long qdDcCucId, String maNganKho, String maLoKho);
}
