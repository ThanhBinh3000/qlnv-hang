package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBbGiaoNhanHdrRepository extends JpaRepository<DcnbBbGiaoNhanHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbBangKeCanHangHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(c.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.soQdinhDcc desc , c.nam desc, c.id desc")
    Page<DcnbBbGiaoNhanHdr> search(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbBbGiaoNhanHdr> findFirstBySoBb(String soBb);


    List<DcnbBbGiaoNhanHdr> findByIdIn(List<Long> ids);

    List<DcnbBbGiaoNhanHdr> findAllByIdIn(List<Long> idList);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO(" +
            "bbgn.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "hskt.soHoSoKyThuat, hskt.id, bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap, bblm.soBbLayMau,bblm.id, bbgn.trangThai, bbgn.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBbGiaoNhanHdr bbgn On bbgn.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbHoSoKyThuatHdr hskt On hskt.idQdnh = qdc.id and bbgn.idHoSoKyThuat = hskt.id " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbktnk On bbktnk.qDinhDccId = qdc.id and bbgn.idBbKtNhapKho =  bbktnk.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi})) " +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbgn.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbgn.ngayKtNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbgn.ngayKtNhap <= :#{#param.denNgayKtnk}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNhap})" +
            "AND (:#{#param.denNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNhap}) ) " +
            "ORDER BY bbgn.soBb desc, bbgn.nam desc")
    Page<DcnbBbGiaoNhanHdrDTO> searchPageChiCuc(@Param("param") DcnbBbGiaoNhanHdrReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO(" +
            "bbgn.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "hskt.soHoSoKyThuat, hskt.id, bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap, bblm.soBbLayMau,bblm.id, bbgn.trangThai, bbgn.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBbGiaoNhanHdr bbgn On bbgn.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbHoSoKyThuatHdr hskt On bbgn.idHoSoKyThuat = hskt.id " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbktnk On bbgn.idBbKtNhapKho =  bbktnk.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbgn.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbgn.ngayKtNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbgn.ngayKtNhap <= :#{#param.denNgayKtnk}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNhap})" +
            "AND (:#{#param.denNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNhap}) ) " +
            "ORDER BY bbgn.soBb desc, bbgn.nam desc")
    Page<DcnbBbGiaoNhanHdrDTO> searchPageCuc(@Param("param") DcnbBbGiaoNhanHdrReq req, Pageable pageable);
}
