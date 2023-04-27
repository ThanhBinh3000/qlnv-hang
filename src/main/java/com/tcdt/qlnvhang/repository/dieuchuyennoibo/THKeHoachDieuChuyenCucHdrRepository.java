package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucHdr;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface THKeHoachDieuChuyenCucHdrRepository extends JpaRepository<THKeHoachDieuChuyenCucHdr, Long> {
    @Query(value = "SELECT distinct hdr FROM THKeHoachDieuChuyenCucHdr hdr WHERE 1=1 " +
            "AND (:#{#param.maDVi} IS NULL OR hdr.maDvi LIKE CONCAT(:#{#param.maDVi},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR hdr.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.maTongHop} IS NULL OR LOWER(hdr.maTongHop) LIKE CONCAT('%',LOWER(:#{#param.maTongHop}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayTongHop >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayTongHop <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(hdr.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "ORDER BY hdr.namKeHoach desc")
    Page<THKeHoachDieuChuyenCucHdr> search(@Param("param") TongHopKeHoachDieuChuyenSearch param, Pageable pageable);

    List<THKeHoachDieuChuyenCucHdr> findAllByIdIn(List<Long> id);

    List<THKeHoachDieuChuyenCucHdr> findByIdIn(List<Long> id);

    List<THKeHoachDieuChuyenCucHdr> findByMaTongHop(String maTongHop);

@Query(nativeQuery = true,value = "SELECT * FROM DCNB_TH_KE_HOACH_DCC_HDR h \n" +
        "LEFT JOIN DCNB_TH_KE_HOACH_DCC_NBC_DTL dtl ON dtl.HDR_ID = h.ID \n" +
        "LEFT JOIN DCNB_KE_HOACH_DC_DTL khdtl ON khdtl.ID = dtl.DCNB_KE_HOACH_DC_DTL_ID \n" +
        "WHERE h.MA_DVI = ?1 AND h.TRANG_THAI = ?2 AND h.LOAI_DC = ?3 \n" +
        "AND (?4 IS NULL OR khdtl.LOAI_VTHH = ?4) \n" +
        "AND (?5 IS NULL OR khdtl.CLOAI_VTHH = ?5)\n" +
        "AND ((TO_DATE(TO_CHAR(h.NGAY_TAO ,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?6,'YYYY-MM-DD HH24:MI:SS')))")
    List<THKeHoachDieuChuyenCucHdr> findByDonViAndTrangThaiTongCuc(String maDVi,String trangThai,String loaiDieuChuyen, String loaiHangHoa, String chungLoaiHangHoa, String thoiGianTongHop);


    @Query(nativeQuery = true,value = "SELECT h.ID FROM DCNB_TH_KE_HOACH_DCC_HDR h WHERE h.MA_DVI = ?1 AND h.LOAI_DC = ?2")
    List<THKeHoachDieuChuyenCucHdr> findByDonViAndLoaiDc(String maDVi, String loaiDieuChuyen);
    @Query(nativeQuery = true,value = "SELECT h.ID FROM DCNB_TH_KE_HOACH_DCC_HDR h WHERE h.MA_DVI = ?1 AND h.LOAI_DC = ?2")
    void insertDataDtl(Long id, Long dcnbKeHoachDcHdrId);
}
