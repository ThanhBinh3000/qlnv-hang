package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface HhDxuatKhMttThopRepository extends JpaRepository<HhDxKhMttThopHdr ,Long> {

    @Query(value = "select * from HH_DX_KHMTT_THOP_HDR TH" +
            " LEFT JOIN HH_QD_PHE_DUYET_KHMTT_HDR QDPD ON TH.ID=QDPD.ID_TH_HDR"+
            " where (:namKh IS NULL OR TH.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:loaiVthh IS NULL OR TH.LOAI_VTHH = :loaiVthh) " +
            "AND (:cloaiVthh IS NULL OR TH.CLOAI_VTHH = :cloaiVthh) " +
            "AND (:noiDung IS NULL OR LOWER( TH.NOI_DUNG) LIKE LOWER(CONCAT(CONCAT('%',:noiDung),'%')))" +
            "AND (:ngayThopTu IS NULL OR TH.NGAY_THOP >=  TO_DATE(:ngayThopTu,'yyyy-MM-dd')) " +
            "AND (:ngayThopDen IS NULL OR TH.NGAY_THOP <= TO_DATE(:ngayThopDen,'yyyy-MM-dd'))" +
            "AND (:ngayKyQdTu IS NULL OR QDPD.NGAY_QD >=  TO_DATE(:ngayKyQdTu,'yyyy-MM-dd')) " +
            "AND (:ngayKyQdDen IS NULL OR QDPD.NGAY_QD <= TO_DATE(:ngayKyQdDen,'yyyy-MM-dd'))" +
            "AND (:trangThai IS NULL OR TH.TRANG_THAI = :trangThai) "
            ,nativeQuery = true)
    Page<HhDxKhMttThopHdr> searchPage(Integer namKh, String loaiVthh, String cloaiVthh, String noiDung, String ngayThopTu, String ngayThopDen, String ngayKyQdTu, String ngayKyQdDen,String trangThai, Pageable pageable);

    List<HhDxKhMttThopHdr> findAllByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_THOP_HDR SET TRANG_THAI =:trangThai WHERE ID = :idThHdr", nativeQuery = true)
    void updateTrangThai(Long idThHdr, String trangThai);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_THOP_HDR SET SO_QD_PDUYET =:SoQdPduyet WHERE ID = :idThHdr", nativeQuery = true)
    void updateSoQdPduyet(Long idThHdr, String SoQdPduyet);


}
