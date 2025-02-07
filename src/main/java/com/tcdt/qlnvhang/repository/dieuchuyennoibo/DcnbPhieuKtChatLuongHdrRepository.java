package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKtChatLuong;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrLsDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuKtChatLuongHdrRepository extends JpaRepository<DcnbPhieuKtChatLuongHdr, Long> {

    Optional<DcnbPhieuKtChatLuongHdr> findFirstBySoPhieu(String soPhieu);

    List<DcnbPhieuKtChatLuongHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuKtChatLuongHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrLsDTO(pktcl.id, pktcl.soPhieu) " +
            "FROM DcnbPhieuKtChatLuongHdr pktcl " +
            "WHERE 1 =1 " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(pktcl.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR pktcl.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pktcl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maNganKho} IS NULL OR pktcl.maNganKho = :#{#param.maNganKho}) " +
            "AND (:#{#param.maNganKhoXuat} IS NULL OR pktcl.maNganKhoXuat = :#{#param.maNganKhoXuat}) " +
            "AND (:#{#param.maLoKho} IS NULL OR pktcl.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.maLoKhoXuat} IS NULL OR pktcl.maLoKhoXuat = :#{#param.maLoKhoXuat}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pktcl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(pktcl.soQdinhDc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pktcl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pktcl.ngayKiem <= :#{#param.denNgay}) ) " +
            "ORDER BY pktcl.soQdinhDc desc, pktcl.nam desc")
    List<DcnbPhieuKtChatLuongHdrLsDTO> searchList(@Param("param") SearchPhieuKtChatLuong req);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrDTO(" +
            "pktcl.id,pktcl.bbNtLdId,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,pktcl.soBBNtLd,khdcd.thoiGianDkDc,qdc.nam,pktcl.nhanXetKetLuan,khdcd.maNhaKho," +
            "khdcd.tenNhaKho,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.soLuongDc,khdcd.donViTinh," +
            "khdcd.thayDoiThuKho,pktcl.soPhieu,pktcl.ngayGiamDinh,pktcl.nhanXetKetLuan,pnk.soPhieuNhapKho, pnk.ngayLap, " +
            "khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan," +
            "pktcl.trangThai,pktcl.trangThai,khdcd.tichLuongKd,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.qdDcCucId = qdc.id and pnk.idPhieuKtraCluong = pktcl.id and pnk.trangThai = '17' " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND (qdc.type = 'NDC') " +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pktcl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pktcl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(pktcl.soQdinhDc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pktcl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pktcl.ngayKiem <= :#{#param.denNgay}) ) " +
            "GROUP BY pktcl.id,pktcl.bbNtLdId,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,pktcl.soBBNtLd,khdcd.thoiGianDkDc,qdc.nam,pktcl.nhanXetKetLuan,khdcd.maNhaKho," +
            "khdcd.tenNhaKho,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.soLuongDc,khdcd.donViTinh," +
            "khdcd.thayDoiThuKho,pktcl.soPhieu,pktcl.ngayGiamDinh,pktcl.nhanXetKetLuan,pnk.soPhieuNhapKho, pnk.ngayLap, " +
            "khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan," +
            "pktcl.trangThai,pktcl.trangThai,khdcd.tichLuongKd,khdcd.id "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbPhieuKtChatLuongHdrDTO> searchPage(@Param("param")SearchPhieuKtChatLuong req, Pageable pageable);
}
