package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


@Repository
public interface HhQdPheduyetKhMttHdrRepository extends JpaRepository<HhQdPheduyetKhMttHdr, Long> {


    @Query(value = "select * from HH_QD_PHE_DUYET_KHMTT_HDR MTT " +
            " where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:soQd IS NULL OR LOWER(MTT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%' ) ) )" +
            " AND (:trichYeu IS NULL OR LOWER(MTT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%')))" +
            " AND (:ngayQdTu IS NULL OR MTT.NGAY_QD >=  TO_DATE(:ngayQdTu,'yyyy-MM-dd')) " +
            " AND (:ngayQdDen IS NULL OR MTT.NGAY_QD <= TO_DATE(:ngayQdDen,'yyyy-MM-dd'))" +
            " AND (:loaiVthh IS NULL OR LOWER(MTT.LOAI_VTHH) LIKE LOWER(CONCAT(:loaiVthh,'%' ) ) )" +
            " AND (:trangThai IS NULL OR MTT.TRANG_THAI = :trangThai)" +
            " AND (:lastest IS NULL OR MTT.LASTEST = TO_NUMBER(:lastest)) " +
            " AND (:maDvi IS NULL OR MTT.MA_DVI = :maDvi) "
            , nativeQuery = true)
    Page<HhQdPheduyetKhMttHdr> searchPage(Integer namKh, String soQd, String trichYeu, String ngayQdTu, String ngayQdDen, String loaiVthh, String trangThai, Integer lastest, String maDvi, Pageable pageable);

    List<HhQdPheduyetKhMttHdr> findBySoQd(String soQd);

    List<HhQdPheduyetKhMttHdr> findAllByIdIn(List<Long> listId);

    @Query(value = "select * from HH_QD_PHE_DUYET_KHMTT_HDR MTT " +
            " LEFT JOIN HH_CTIET_TTIN_CHAO_GIA DTL ON MTT.ID=DTL.ID_SO_QD_PDUYET_CGIA" +
            " LEFT JOIN HH_QD_PHE_DUYET_KHMTT_DX DX ON DX.ID_QD_HDR=MTT.ID" +
            " where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:ngayCgiaTu IS NULL OR MTT.NGAY_HLUC >=  TO_DATE(:ngayCgiaTu,'yyyy-MM-dd')) " +
            " AND (:ngayCgiadDen IS NULL OR MTT.NGAY_HLUC <= TO_DATE(:ngayCgiadDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR MTT.TRANG_THAI=:trangThai)" +
            " AND (:trangThaiTk IS NULL OR MTT.TRANG_THAI_TKHAI=:trangThaiTk )" +
            " AND (:maDvi IS NULL OR LOWER(DX.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  " +
            " AND (:ctyCgia IS NULL OR LOWER(DTL.CANHAN_TOCHUC) LIKE LOWER(CONCAT(CONCAT('%', :ctyCgia),'%'))) " +
            " AND (:pthucMuatt IS NULL OR MTT.PTHUC_MUATT =:pthucMuatt)"
            , nativeQuery = true)
    Page<HhQdPheduyetKhMttHdr> searchPageTkhai(Integer namKh, String ngayCgiaTu, String ngayCgiadDen, String trangThai, String trangThaiTk, String maDvi, String ctyCgia, String pthucMuatt, Pageable pageable);

    @Transactional()
    @Modifying
    @Query(value = "update HH_QD_PHE_DUYET_KHMTT_HDR PD set PD.SO_QD_PD_CG=:soQdPdCg  where PD.SO_QD_PDUYET=:soQdPd ", nativeQuery = true)
    void updateSoQdPdCg(String soQdPd, String soQdPdCg);

    @Query(value = " SELECT NVL(SUM(DSG.SO_LUONG),0) FROM HH_QD_PHE_DUYET_KHMTT_HDR HDR " +
            " INNER JOIN HH_QD_PHE_DUYET_KHMTT_DX DTL on HDR.ID = DTL.ID_QD_HDR " +
            " LEFT JOIN HH_QD_PHE_DUYET_KHMTT_SLDD DSG ON DSG.ID_QD_DTL = DTL.ID " +
            "WHERE HDR.NAM_KH = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND DSG.MA_DVI = :maDvi AND HDR.TRANG_THAI = :trangThai AND HDR.LASTEST = 1",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi, String trangThai);


    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_QD_PHE_DUYET_KHMTT_HDR SET TRANG_THAI_TKHAI =:trangThaiTkhai  WHERE ID =TO_NUMBER(:id) ", nativeQuery = true)
    void updateTrangThaiTkhai(Long id, String trangThaiTkhai);

}
