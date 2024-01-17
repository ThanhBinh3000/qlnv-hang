package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.table.chotdulieu.KhPagTtChung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhPagTtChungRepository extends JpaRepository<KhPagTtChung, Long> {
    List<KhPagTtChung> findByPhuongAnGiaIdInAndQdBtcIdIsNullAndQdTcdtnnIdIsNull(List<Long> ids);


    @Query(value = "select * from KH_PAG_TT_CHUNG where pag_id = :id and  ma_chi_cuc = :maChiCuc and qd_btc_id is null and qd_tcdtnn_id is null", nativeQuery = true)
    Optional<KhPagTtChung> findByQdBtcAndMaChiCuc(Long id, String maChiCuc);


    @Query(value = "select * from KH_PAG_TT_CHUNG where pag_id = :id and (:loaiVthh is null or loai_vthh = :loaiVthh) and (:cloaiVthh is null or cloai_vthh = :cloaiVthh) and  qd_btc_id is null and qd_tcdtnn_id is null ", nativeQuery = true)
    Optional<KhPagTtChung> findByQdBtcCtiet(Long id, String loaiVthh, String cloaiVthh);



    List<KhPagTtChung> findAllByQdBtcIdIn(List<Long> ids);

    List<KhPagTtChung> findAllByPhuongAnGiaId(Long id);

    List<KhPagTtChung> findAllByQdBtcId(Long id);


    List<KhPagTtChung> findByQdTcdtnnId(Long ids);
    List<KhPagTtChung> findByQdTcdtnnIdIn(List<Long> ids);

    void deleteAllByQdBtcId(Long id);

    void deleteAllByQdBtcIdIn(List<Long> ids);

    void deleteAllByQdTcdtnnId(Long id);

    @Query(value = "select * from (SELECT DTL.*,(ROW_NUMBER() OVER (PARTITION BY DTL.LOAI_VTHH, DTL.CLOAI_VTHH, HDR.LOAI_GIA, HDR.NAM_KE_HOACH, HDR.MA_DVI  ORDER BY HDR.NGAY_DUYET desc)) row_tt" +
            "    From KH_PAG_TT_CHUNG DTL, KH_PAG_QD_BTC HDR" +
            " where DTL.QD_BTC_ID = HDR.ID and  DTL.LOAI_VTHH = :loaiVthh" +
            " and (:cloaiVthh is null or DTL.CLOAI_VTHH = :cloaiVthh ) " +
            " AND (HDR.NAM_KE_HOACH = :namKeHoach)" +
            " AND (HDR.LOAI_GIA = :loaiGia)" +
            " AND (HDR.TRANG_THAI = :trangThai))" +
            "where row_tt = 1",
            nativeQuery = true)
    Optional<KhPagTtChung> getDtlByQdBtc(String trangThai, String loaiGia, Long namKeHoach, String loaiVthh, String cloaiVthh);


}
