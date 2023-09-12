package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBbNhapDayKhoHdrRepository extends JpaRepository<HhNkBbNhapDayKhoHdr, Long> {

    @Query(value = "SELECT distinct c FROM HhNkBbNhapDayKhoHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "ORDER BY c.soBb desc , c.nam desc, c.id desc")
    Page<HhNkBbNhapDayKhoHdr> search(@Param("param") HhNkBbNhapDayKhoHdrReq req, Pageable pageable);

    Optional<HhNkBbNhapDayKhoHdr> findBySoBb(String soBb);


    List<HhNkBbNhapDayKhoHdr> findByIdIn(List<Long> ids);

    List<HhNkBbNhapDayKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT distinct c FROM HhNkBbNhapDayKhoHdr c " +
            "WHERE 1=1 " +
            "AND c.trangThai = '17' " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "ORDER BY c.soBb desc , c.nam desc, c.id desc")
    List<HhNkBbNhapDayKhoHdrDTO> searchList(@Param("param") HhNkBbNhapDayKhoHdrReq param);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrDTO(" +
            "bbndk.id,qdgnv.id,qdgnv.soQd,qdgnv.ngayQd,qdgnv.nam,qdgnv.tgianNkMnhat,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.ten,hdr.dvt,dtl.tongSlNhap," +
            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.soPhieuKiemTraCl,bbndkd.phieuKiemTraClId, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho," +
            "bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
            "bbndk.ngayLap, bbndk.trangThai, bbndk.trangThai) " +
            "FROM HhQdGiaoNvuNhapHangKhacHdr qdgnv " +
            "LEFT JOIN HhQdPdNhapKhacHdr hdr ON hdr.id = qdgnv.idQdPdNk " +
            "LEFT JOIN HhQdPdNhapKhacDtl dtl ON hdr.id = dtl.idHdr " +
            "LEFT JOIN HhNkBbNhapDayKhoHdr bbndk On bbndk.qdPdNkId = qdgnv.id " +
            "and ((dtl.maLoKho is not null and dtl.maNganKho = bbndk.maNganKho and dtl.maLoKho = bbndk.maLoKho) or (dtl.maLoKho is null and dtl.maNganKho = bbndk.maNganKho)) " +
            "LEFT JOIN HhNkBbNhapDayKhoDtl bbndkd On bbndkd.hdrId = bbndk.id  " +
            "LEFT JOIN HhNkPhieuNhapKhoHdr pnk On pnk.qdGiaoNvId = qdgnv.idQdPdNk " +
            "and ((dtl.maLoKho is not null and pnk.maLoKho = dtl.maLoKho and pnk.maNganKho = dtl.maNganKho) or (dtl.maLoKho is null and pnk.maNganKho = dtl.maNganKho)) " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = dtl.cloaiVthh " +
            "LEFT JOIN QlnvDmDonvi dmdvnhakho On dmdvnhakho.maDvi = dtl.maNhaKho " +
            "LEFT JOIN QlnvDmDonvi dmdvdiemkho On dmdvdiemkho.maDvi = dtl.maDiemKho " +
            "LEFT JOIN QlnvDmDonvi dmdvlokho On dmdvlokho.maDvi = dtl.maLoKho " +
            "LEFT JOIN QlnvDmDonvi dmdvngankho On dmdvngankho.maDvi = dtl.maNganKho " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND (:#{#param.loaiVthh} IS NULL OR dmvt.ma LIKE CONCAT('',LOWER(:#{#param.loaiVthh}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR dtl.maChiCuc LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.soQdPdNk} IS NULL OR LOWER(qdgnv.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdPdNk}),'%')) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbndk.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgayBdNhap}  IS NULL OR bbndk.ngayBdNhap >= :#{#param.tuNgayBdNhap})" +
            "AND (:#{#param.denNgayBdNhap}  IS NULL OR bbndk.ngayBdNhap <= :#{#param.denNgayBdNhap}) ) " +
            "AND ((:#{#param.tuNgayKtNhap}  IS NULL OR bbndk.ngayKtNhap >= :#{#param.tuNgayKtNhap})" +
            "AND (:#{#param.denNgayKtNhap}  IS NULL OR bbndk.ngayKtNhap <= :#{#param.denNgayKtNhap}) ) " +
            "GROUP BY bbndk.id,qdgnv.id,qdgnv.soQd,qdgnv.ngayQd,qdgnv.nam,qdgnv.tgianNkMnhat,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.ten,hdr.dvt, hdr.dvt ,dtl.tongSlNhap," +
            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.soPhieuKiemTraCl,bbndkd.phieuKiemTraClId, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho," +
            "bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
            "bbndk.ngayLap, bbndk.trangThai, bbndk.trangThai "+
            "ORDER BY qdgnv.soQd DESC")
    Page<HhNkBbNhapDayKhoHdrDTO> searchPage(@Param("param") HhNkBbNhapDayKhoHdrReq req, Pageable pageable);

}
