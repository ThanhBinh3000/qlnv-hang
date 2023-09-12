package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBbGiaoNhanHdrRepository extends JpaRepository<DcnbBbGiaoNhanHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbBbGiaoNhanHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(c.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi})) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(c.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    Page<DcnbBbGiaoNhanHdr> search(@Param("param") DcnbBbGiaoNhanHdrReq req, Pageable pageable);

    Optional<DcnbBbGiaoNhanHdr> findFirstBySoBb(String soBb);


    List<DcnbBbGiaoNhanHdr> findByIdIn(List<Long> ids);

    List<DcnbBbGiaoNhanHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO(" +
            "bbgn.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.soLuongDc,khdcd.donViTinh, " +
            "hskt.soHskt, hskt.id, bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap, bblm.soBbLayMau,bblm.id, bbgn.trangThai, bbgn.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBbGiaoNhanHdr bbgn On bbgn.qdDcCucId = qdc.id " +
            "and ((khdcd.maLoKhoNhan is not null and  khdcd.maLoKhoNhan = bbgn.maLoKho and khdcd.maNganKhoNhan = bbgn.maNganKho ) or (khdcd.maLoKhoNhan is null and khdcd.maNganKhoNhan = bbgn.maNganKho ))" +
            "LEFT JOIN XhHoSoKyThuatHdr hskt On 1 = 1  " +
            "and ((khdcd.maLoKhoNhan is not null and  khdcd.maLoKhoNhan = SUBSTRING(hskt.maDiaDiem, 1, 17) and khdcd.maNganKhoNhan = SUBSTRING(hskt.maDiaDiem, 1, 15) ) or (khdcd.maLoKhoNhan is null and khdcd.maNganKhoNhan = SUBSTRING(hskt.maDiaDiem, 1, 15) ))" +
            "LEFT JOIN DcnbBBKetThucNKHdr bbktnk On bbktnk.qDinhDccId = qdc.id and bbgn.idBbKtNhapKho =  bbktnk.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id " +
            "and ((khdcd.maLoKhoNhan is not null and bblm.maLoKho = khdcd.maLoKhoNhan and bblm.maNganKho = khdcd.maNganKhoNhan ) or (khdcd.maLoKhoNhan is null and bblm.maNganKho = khdcd.maNganKhoNhan ))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbgn.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbgn.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbgn.ngayKtNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbgn.ngayKtNhap <= :#{#param.denNgayKtnk}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNhap})" +
            "AND (:#{#param.denNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNhap}) ) " +
            "GROUP BY bbgn.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.soLuongDc,khdcd.donViTinh," +
            "hskt.soHskt, hskt.id, bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap, bblm.soBbLayMau,bblm.id, bbgn.trangThai, bbgn.trangThai "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBbGiaoNhanHdrDTO> searchPage(@Param("param") DcnbBbGiaoNhanHdrReq req, Pageable pageable);

}
