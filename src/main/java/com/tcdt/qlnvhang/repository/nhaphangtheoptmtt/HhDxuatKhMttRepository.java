package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface HhDxuatKhMttRepository extends JpaRepository<HhDxuatKhMttHdr, Long> {

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_HDR SET TRANG_THAI_TH=:trangThaiTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh);



    @Query(value = "select * from HH_DX_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soDxuat IS NULL OR LOWER(MTT.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%',:soDxuat),'%' ) ) )" +
            "AND (:ngayTaoTu IS NULL OR MTT.NGAY_TAO >=  TO_DATE(:ngayTaoTu,'yyyy-MM-dd')) " +
            "AND (:ngayTaoDen IS NULL OR MTT.NGAY_TAO <= TO_DATE(:ngayTaoDen,'yyyy-MM-dd'))" +
            "AND (:ngayDuyetTu IS NULL OR MTT.NGAY_PDUYET >=  TO_DATE(:ngayDuyetTu,'yyyy-MM-dd')) " +
            "AND (:ngayDuyetDen IS NULL OR MTT.NGAY_PDUYET <= TO_DATE(:ngayDuyetDen,'yyyy-MM-dd'))" +
            "AND (:trichYeu IS NULL OR LOWER(MTT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%' ) ) )" +
            "AND (:noiDungTh IS NULL OR LOWER(MTT.NOI_DUNG_TH) LIKE LOWER(CONCAT(CONCAT('%',:noiDungTh),'%' ) ) )" +
            "AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            "AND (:trangThai IS NULL OR MTT.TRANG_THAI = :trangThai)" +
            "AND (:trangThaiTh IS NULL OR MTT.TRANG_THAI_TH = :trangThaiTh) " +
            "AND (:maDvi IS NULL OR LOWER(MTT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhDxuatKhMttHdr> searchPage(Integer namKh, String soDxuat, String ngayTaoTu, String ngayTaoDen,String ngayDuyetTu, String ngayDuyetDen, String trichYeu,String noiDungTh, String loaiVthh, String trangThai, String trangThaiTh, String maDvi, Pageable pageable);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);
    Optional<HhDxuatKhMttHdr> findBySoDxuat(String soDxuat);
    List<HhDxuatKhMttHdr> findBySoDxuatIn (List<String> list);

    List <HhDxuatKhMttHdr>findAllByIdIn(List<Long> listId);

    @Query(value = "select * from HH_DX_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            " AND (:cloaiVthh IS NULL OR MTT.CLOAI_VTHH = :cloaiVthh) " +
            " AND MTT.TRANG_THAI = '"+ Contains.DADUYET_LDC+"'" +
            " AND MTT.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+"'" +
            " AND MTT.MA_THOP is null "+
//            " AND MTT.SO_QD_PDUYET is null "+
            " AND (:maDvi IS NULL OR LOWER(MTT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    List<HhDxuatKhMttHdr> listTongHop(Integer namKh, String loaiVthh, String cloaiVthh, String maDvi);

    List<HhDxuatKhMttHdr> findByIdIn(List<Long> id);
    HhDxuatKhMttHdr findAllById(Long id);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_HDR SET SO_QD_PDUYET =:soQdPd WHERE ID = :id", nativeQuery = true)
    void updateSoQdPduyet(Long id, String soQdPd);

    @Query(value = " SELECT NVL(SUM(SLDD.SO_LUONG),0) FROM HH_QD_PHE_DUYET_KHMTT_HDR HDR " +
            " INNER JOIN HH_QD_PHE_DUYET_KHMTT_DX DX on HDR.ID = DX.ID_QD_HDR " +
            " LEFT JOIN HH_QD_PHE_DUYET_KHMTT_SLDD SLDD ON SLDD.ID_QD_DTL = DX.ID " +
            "WHERE HDR.NAM_KH = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND SLDD.MA_DVI = :maDvi AND HDR.TRANG_THAI = :trangThai",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi, String trangThai);


    HhDxuatKhMttHdr findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(String loaiVthh,String cloaiVthh,Integer namKh,String maDvi,String trangThai);
}
