package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBBNTBQHdrRepository extends JpaRepository<DcnbBBNTBQHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbPhieuXuatKhoHdr c " +
            " LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi " +
            " WHERE 1=1 " +
            " AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            " ORDER BY c.soPhieuXuatKho desc , c.nam desc, c.id desc")
    Page<DcnbBBNTBQHdr> search(@Param("param") DcnbBBNTBQHdrReq req, Pageable pageable);

    //
    Optional<DcnbBBNTBQHdr> findBySoBban(String soBban);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO(" +
            "bblm.id, qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maDiemKhoNhan, khdcd.tenDiemKhoNhan, khdcd.maLoKhoNhan, khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, bblm.soBban," +
            "bblm.ngayLap,bblm.ngayKetThucNt , bblm.tongKinhPhiDaTh,bblm.tongKinhPhiDaTh ,bblm.trangThai, bblm.trangThai,khdcd.tenLoaiVthh,khdcd.tenCloaiVthh, khdcd.tichLuongKd, khdcd.tenDonViTinh, khdcd.loaiVthh, khdcd.cloaiVthh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBBNTBQHdr bblm On bblm.qdDcCucId = qdc.id " +
            "and ((khdcd.maLoKhoNhan is not null and khdcd.maLoKhoNhan =  bblm.maLoKho and  khdcd.maNganKhoNhan = bblm.maNganKho and khdcd.maLoKho =  bblm.maLoKhoXuat and  khdcd.maNganKho = bblm.maNganKhoXuat ) or (khdcd.maLoKhoNhan is null and khdcd.maNganKhoNhan = bblm.maNganKho and  khdcd.maNganKho = bblm.maNganKhoXuat ))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bblm.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND (:#{#param.soBban} IS NULL OR LOWER(bblm.soBban) LIKE CONCAT('%',LOWER(:#{#param.soBban}),'%')) " +
            "AND ((:#{#param.tuNgayLap}  IS NULL OR bblm.ngayLap >= :#{#param.tuNgayLap})" +
            "AND (:#{#param.denNgayLap}  IS NULL OR bblm.ngayLap <= :#{#param.denNgayLap}) ) " +
            "AND ((:#{#param.tuNgayKtnt}  IS NULL OR bblm.ngayKetThucNt >= :#{#param.tuNgayKtnt})" +
            "AND (:#{#param.denNgayKtnt}  IS NULL OR bblm.ngayKetThucNt <= :#{#param.denNgayKtnt}) ) " +
            "GROUP BY bblm.id, qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maDiemKhoNhan, khdcd.tenDiemKhoNhan, khdcd.maLoKhoNhan, khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, bblm.soBban," +
            "bblm.ngayLap,bblm.ngayKetThucNt , bblm.tongKinhPhiDaTh,bblm.tongKinhPhiDaTh ,bblm.trangThai, bblm.trangThai,khdcd.tenLoaiVthh,khdcd.tenCloaiVthh, khdcd.tichLuongKd, khdcd.tenDonViTinh, khdcd.loaiVthh, khdcd.cloaiVthh "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBBNTBQHdrDTO> searchPage(@Param("param") DcnbBBNTBQHdrReq req, Pageable pageable);

    List<DcnbBBNTBQHdr> findByQdDcCucIdAndMaNganKho(Long qdDcCucId, String maNganKho);

    List<DcnbBBNTBQHdr> findByQdDcCucIdAndMaNganKhoAndMaLoKho(Long qdDcCucId, String maNganKho, String maLoKho);
    @Query(value = "SELECT distinct c FROM DcnbBBNTBQHdr c " +
            " WHERE 1=1 " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.maLoKho} IS NULL OR LOWER(c.maLoKho) = :#{#param.maLoKho}) " +
            "AND (:#{#param.maNganKho} IS NULL OR LOWER(c.maNganKho) = :#{#param.maNganKho}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.nam desc, c.id desc")
    List<DcnbBBNTBQHdr> list(@Param("param") DcnbBBNTBQHdrReq req);
}
