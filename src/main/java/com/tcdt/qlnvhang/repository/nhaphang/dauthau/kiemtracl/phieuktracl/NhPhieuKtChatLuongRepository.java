package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhPhieuKtChatLuongRepository extends BaseRepository<NhPhieuKtChatLuong, Long> {

    @Query(
        value = "SELECT * FROM NH_PHIEU_KT_CHAT_LUONG PKTCL " +
                "WHERE (:soPhieu IS NULL OR PKTCL.SO_PHIEU = TO_NUMBER(:soPhieu)) " +
//                "AND (:soQd IS NULL OR LOWER(PKTCL.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%')))"+
//                "AND (:ketLuan IS NULL OR PKTCL.KET_LUAN=:ketLuan)"+
                "AND (:ngayLPhieu IS NULL OR TO_CHAR(PKTCL.NGAY_TAO,'yyyy-MM-dd') = :ngayLPhieu) " +
                "AND (:nguoiGiaoHang IS NULL OR LOWER(PKTCL.NGUOI_GIAO_HANG) " +
                "LIKE LOWER(CONCAT(CONCAT('%', :nguoiGiaoHang), '%')))",
            nativeQuery = true)
    Page<NhPhieuKtChatLuong> selectPage(String soPhieu, String ngayLPhieu, String nguoiGiaoHang, Pageable pageable);

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhPhieuKtChatLuong> findFirstBySoPhieu(String soPhieu);

    List<NhPhieuKtChatLuong> findByIdIn(Collection<Long> ids);

    List<NhPhieuKtChatLuong> findAllByIdQdGiaoNvNhOrderById(Long idQdGiaoNvNh);

    List<NhPhieuKtChatLuong> findByIdDdiemGiaoNvNhOrderById(Long idDdiemGiaoNvNh);

    @Query(value = " SELECT NVL(SUM(KT.SO_LUONG_NHAP_KHO),0) as C FROM NH_PHIEU_KT_CHAT_LUONG KT " +
            " WHERE KT.ID_DDIEM_GIAO_NV_NH = :idDdiemNhap AND KT.TRANG_THAI = :trangThai",nativeQuery = true)
    BigDecimal soLuongNhapKho(Long idDdiemNhap,String trangThai);

}
