package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface THKeHoachDieuChuyenCucHdrRepository extends JpaRepository<THKeHoachDieuChuyenCucHdr, Long> {
    @Query(value = "SELECT distinct hdr FROM THKeHoachDieuChuyenCucHdr hdr WHERE 1=1 " +
            "AND (:#{#param.maDVi} IS NULL OR hdr.maDvi LIKE CONCAT(:#{#param.maDVi},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR hdr.namKeHoach = :#{#param.namKeHoach}) " +
            "AND LOWER(hdr.id) LIKE CONCAT('%',LOWER(:#{#param.id}),'%') " +
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayTongHop >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayTongHop <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(hdr.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "ORDER BY hdr.namKeHoach desc")
    Page<THKeHoachDieuChuyenCucHdr> search(@Param("param") TongHopKeHoachDieuChuyenSearch param, Pageable pageable);

    List<THKeHoachDieuChuyenCucHdr> findAllByIdIn(List<Long> id);

    List<THKeHoachDieuChuyenCucHdr> findByIdIn(List<Long> id);

    List<THKeHoachDieuChuyenCucHdr> findByMaTongHop(String maTongHop);

    @Query(value = "SELECT distinct h FROM THKeHoachDieuChuyenCucHdr h \n" +
            "LEFT JOIN THKeHoachDieuChuyenNoiBoCucDtl dtl ON dtl.hdrId = h.id \n" +
            "WHERE h.maDvi = ?1 AND h.trangThai = ?2 AND h.loaiDieuChuyen = ?3 \n" +
            "AND h.ngaytao <= ?4")
    List<THKeHoachDieuChuyenCucHdr> findByDonViAndTrangThaiTongCuc(String maDVi, String trangThai, String loaiDieuChuyen, LocalDate thoiGianTongHop);

    @Query(nativeQuery = true, value = "SELECT  h.* FROM DCNB_TH_KE_HOACH_DCC_HDR h  " +
            "            LEFT JOIN DCNB_TH_KE_HOACH_DCC_KC_DTL dtl ON dtl.hdr_Id = h.id " +
            "            LEFT JOIN DCNB_KE_HOACH_DC_HDR khhdr ON to_char(khhdr.id) = dtl.dcnb_Ke_Hoach_Dc_Hdr_Id" +
            "            LEFT JOIN DCNB_KE_HOACH_DC_DTL khdtl ON khdtl.hdr_Id = khhdr.id " +
            "            WHERE h.ma_Dvi = :maDVi AND h.trang_Thai = :trangThai AND h.loai_dc = :loaiDieuChuyen " +
            "            AND (:loaiHangHoa IS NULL OR khdtl.loai_Vthh = :loaiHangHoa)  " +
            "            AND (:chungLoaiHangHoa IS NULL OR khdtl.cloai_Vthh = :chungLoaiHangHoa)" +
            "            AND h.ngay_tao <= :thoiGianTongHop")
    List<THKeHoachDieuChuyenCucHdr> findByDonViAndTrangThaiTongCucKhacCuc(String maDVi, String trangThai, String loaiDieuChuyen, String loaiHangHoa, String chungLoaiHangHoa, LocalDate thoiGianTongHop);

    @Query(nativeQuery = true, value = "SELECT h.ID FROM DCNB_TH_KE_HOACH_DCC_HDR h WHERE h.MA_DVI = ?1 AND h.LOAI_DC = ?2")
    List<THKeHoachDieuChuyenCucHdr> findByDonViAndLoaiDc(String maDVi, String loaiDieuChuyen);

    @Query(nativeQuery = true, value = "SELECT h.ID FROM DCNB_TH_KE_HOACH_DCC_HDR h WHERE h.MA_DVI = ?1 AND h.LOAI_DC = ?2")
    void insertDataDtl(Long id, Long dcnbKeHoachDcHdrId);

    @Query(value = "SELECT distinct hdr FROM THKeHoachDieuChuyenCucHdr hdr WHERE 1=1 " +
            "AND (:#{#param.loaiDieuChuyen} IS NULL OR hdr.loaiDieuChuyen = :#{#param.loaiDieuChuyen}) " +
            "AND (:#{#param.namKeHoach} IS NULL OR hdr.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.soDeXuat} IS NULL OR LOWER(hdr.soDeXuat) LIKE CONCAT('%',LOWER(:#{#param.soDeXuat}),'%')) ")
    List<THKeHoachDieuChuyenCucHdr> filterSoDeXuat(@Param("param") TongHopKeHoachDieuChuyenSearch req);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE DCNB_TH_KE_HOACH_DCC_HDR SET ID_THOP_TC = to_number(:idTh) WHERE ID IN :danhSachKeHoach", nativeQuery = true)
    void updateIdTongHop(Long id, List<Long> danhSachKeHoach);
}
