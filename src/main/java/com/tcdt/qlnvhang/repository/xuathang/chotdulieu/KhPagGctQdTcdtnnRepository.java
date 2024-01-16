package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.table.chotdulieu.KhPagGctQdTcdtnn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhPagGctQdTcdtnnRepository extends JpaRepository<KhPagGctQdTcdtnn, Long> {

    @Query(value = "select * from KH_PAG_GCT_QD_TCDTNN where so_qd = :soQd and ((:loaiVthh like '02%' and loai_vthh like '02%') or (:loaiVthh not like '02%' and loai_vthh not like '02%') )   ", nativeQuery = true)
    Optional<KhPagGctQdTcdtnn> findBySoQd(String soQd, String loaiVthh);

    Optional<KhPagGctQdTcdtnn> findBySoToTrinh(String soToTrinh);

    Optional<KhPagGctQdTcdtnn> findById(Long aLong);

    List<KhPagGctQdTcdtnn> findAllByIdIn(List<Long> ids);

    void deleteAllByIdIn(List<Long> ids);

    @Query(value = "select * from (SELECT HDR.*,(ROW_NUMBER() OVER (PARTITION BY HDR.LOAI_VTHH, HDR.CLOAI_VTHH, HDR.LOAI_GIA, HDR.NAM_KE_HOACH  ORDER BY HDR.NGUOI_PDUYET desc)) row_tt" +
            "    From KH_PAG_GCT_QD_TCDTNN HDR, KH_PAG_TONG_HOP_CTIET DTL where  DTL.QD_TCDTNN_ID = HDR.ID" +
            " and (HDR.CLOAI_VTHH = :cloaiVthh) " +
            " and (HDR.LOAI_VTHH = :loaiVthh) " +
            " AND (HDR.NAM_KE_HOACH = :namKeHoach)" +
            " AND (HDR.LOAI_GIA = :loaiGia)" +
            " AND (DTL.MA_CHI_CUC LIKE concat(:maDvi,'%'))" +
            " AND (HDR.TRANG_THAI = :trangThai))" +
            "where row_tt =1",
            nativeQuery = true)
    List<KhPagGctQdTcdtnn> getGiaTcdtLastestLt(String trangThai, String maDvi, String loaiGia, Long namKeHoach, String loaiVthh, String cloaiVthh);

    @Query(value = "select * from (SELECT HDR.*,(ROW_NUMBER() OVER (PARTITION BY DTL.LOAI_VTHH, DTL.CLOAI_VTHH, HDR.LOAI_GIA, HDR.NAM_KE_HOACH ORDER BY HDR.NGUOI_PDUYET desc)) row_tt" +
            "    From KH_PAG_TT_CHUNG DTL, KH_PAG_GCT_QD_TCDTNN HDR" +
            " where DTL.QD_TCDTNN_ID = HDR.ID and  DTL.LOAI_VTHH = :loaiVthh" +
            " and (:cloaiVthh is null or DTL.CLOAI_VTHH = :cloaiVthh ) " +
            " AND (HDR.NAM_KE_HOACH = :namKeHoach)" +
            " AND (HDR.LOAI_GIA = :loaiGia)" +
            " AND (HDR.TRANG_THAI = :trangThai))" +
            "where row_tt =1",
            nativeQuery = true)
    List<KhPagGctQdTcdtnn> getGiaTcdtLastestVt(String trangThai, String loaiGia, Long namKeHoach, String loaiVthh, String cloaiVthh);


    @Query(value = "SELECT kt.*\n" +
            " FROM KH_PAG_GCT_QD_TCDTNN kt\n" +
            " JOIN KH_PAG_TONG_HOP_CTIET dtllt ON dtllt.QD_TCDTNN_ID = kt.id\n" +
            " WHERE kt.NGAY_PDUYET = (\n" +
            "    SELECT MAX(hdr.NGAY_PDUYET)\n" +
            "    FROM KH_PAG_GCT_QD_TCDTNN hdr\n" +
            "    JOIN KH_PAG_TONG_HOP_CTIET dt ON dt.QD_TCDTNN_ID = hdr.id\n" +
            "    WHERE hdr.TRANG_THAI = :trangThai AND hdr.LOAI_GIA = :loaiGia AND hdr.LOAI_VTHH = :loaiVthh AND hdr.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKeHoach AND hdr.NGAY_HIEU_LUC <= SYSDATE \n" +
            "      AND dt.MA_DVI IN (:maDvis)\n" +
            " )\n" +
            " AND kt.TRANG_THAI = :trangThai AND kt.LOAI_GIA = :loaiGia AND kt.LOAI_VTHH = :loaiVthh AND kt.CLOAI_VTHH = :cloaiVthh  AND kt.NAM_KE_HOACH = :namKeHoach AND kt.NGAY_HIEU_LUC <= SYSDATE \n" +
            " AND dtllt.MA_DVI IN (:maDvis)",
            nativeQuery = true)
    List<KhPagGctQdTcdtnn> getGiaTcdtLastestLt(String trangThai, String loaiGia, Integer namKeHoach, String loaiVthh, String cloaiVthh, List<String> maDvis );


    @Query(value = "SELECT kt.*\n" +
            " FROM KH_PAG_GCT_QD_TCDTNN kt\n" +
            " JOIN KH_PAG_TT_CHUNG dtlvt ON dtlvt.QD_TCDTNN_ID = kt.id\n" +
            " WHERE kt.NGAY_PDUYET = (\n" +
            "    SELECT MAX(hdr.NGAY_PDUYET)\n" +
            "    FROM KH_PAG_GCT_QD_TCDTNN hdr\n" +
            "    JOIN KH_PAG_TONG_HOP_CTIET dt ON dt.QD_TCDTNN_ID = hdr.id\n" +
            "    WHERE hdr.TRANG_THAI = :trangThai AND hdr.LOAI_GIA = :loaiGia AND hdr.LOAI_VTHH = :loaiVthh AND (:cloaiVthh IS NULL OR hdr.CLOAI_VTHH = :cloaiVthh) AND hdr.NAM_KE_HOACH = :namKeHoach AND hdr.NGAY_HIEU_LUC <= SYSDATE \n" +
            "      AND dt.MA_DVI IN (:maDvis)\n" +
            " )\n" +
            " AND kt.TRANG_THAI = :trangThai AND kt.LOAI_GIA = :loaiGia AND kt.LOAI_VTHH = :loaiVthh AND (:cloaiVthh IS NULL OR kt.CLOAI_VTHH = :cloaiVthh)  AND kt.NAM_KE_HOACH = :namKeHoach AND kt.NGAY_HIEU_LUC <= SYSDATE \n" +
            " AND dtlvt.MA_DVI IN (:maDvis)",
            nativeQuery = true)
    List<KhPagGctQdTcdtnn> getGiaTcdtLastestVt(String trangThai, String loaiGia, Integer namKeHoach, String loaiVthh, String cloaiVthh, List<String> maDvis );
}
