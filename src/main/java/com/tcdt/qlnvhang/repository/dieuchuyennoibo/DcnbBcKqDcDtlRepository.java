package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBcKqDcDtlRepository extends JpaRepository<DcnbBcKqDcDtl, Long> {
    List<DcnbBcKqDcDtl> findByHdrId(Long id);

    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh," +
            "khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho," +
            "khdcd.maChiCucNhan,khdcd.tenChiCucNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, khdcd.maLoKhoNhan,khdcd.tenLoKhoNhan," +
            "khdcd.donViTinh, khdcd.tonKho, khdcd.soLuongDc, pnkh.tongSoLuong,khdcd.duToanKphi,  pnkh.tongKinhPhi,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnkh On pnkh.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' and pnkh.trangThai = '17' " +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "GROUP BY khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh," +
            "khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho," +
            "khdcd.maChiCucNhan,khdcd.tenChiCucNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, khdcd.maLoKhoNhan,khdcd.tenLoKhoNhan," +
            "khdcd.donViTinh, " +
            "khdcd.tonKho, khdcd.soLuongDc, pnkh.tongSoLuong,khdcd.duToanKphi, pnkh.tongKinhPhi,khdcd.id ")
    List<DcnbBcKqDcDtl> thongTinNhapHangChiCuc(@Param("param") DcnbBbKqDcSearch objReq);

    List<DcnbBcKqDcDtl> findByHdrIdIn(List<Long> ids);
}
