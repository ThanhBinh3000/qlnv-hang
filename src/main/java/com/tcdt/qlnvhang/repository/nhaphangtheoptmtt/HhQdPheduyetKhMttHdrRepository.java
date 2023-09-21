package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrSearchReq;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface HhQdPheduyetKhMttHdrRepository extends JpaRepository<HhQdPheduyetKhMttHdr, Long> {

    @Query("SELECT DX from HhQdPheduyetKhMttHdr DX LEFT JOIN HhQdPheduyetKhMttDx DTL ON DTL.idQdHdr = DX.id" +
            " WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQd} IS NULL OR LOWER(DX.soQd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQd}),'%' ) ) )" +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.ngayQdTu} IS NULL OR DX.ngayQd >= :#{#param.ngayQdTu}) " +
            "AND (:#{#param.ngayQdDen} IS NULL OR DX.ngayQd <= :#{#param.ngayQdDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(DX.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) "
            + "AND (:#{#param.maDvi} IS NULL OR DTL.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) "
    )
    Page<HhQdPheduyetKhMttHdr> searchPage(@Param("param") HhQdPheduyetKhMttHdrSearchReq param, Pageable pageable);

    List<HhQdPheduyetKhMttHdr> findBySoQd(String soQd);

    List<HhQdPheduyetKhMttHdr> findAllByIdIn(List<Long> listId);

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

    Optional<HhQdPheduyetKhMttHdr> findByIdThHdrAndLastest(Long idThHdr, Boolean lastest);
    Optional<HhQdPheduyetKhMttHdr> findBySoQdAndLastest(String soQd, Boolean lastest);
    Optional<HhQdPheduyetKhMttHdr> findByIdQdGnvu(Long idQdGnvu);

}
