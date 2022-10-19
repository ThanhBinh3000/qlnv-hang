package com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QlpktclhPhieuKtChatLuongRepository extends BaseRepository<QlpktclhPhieuKtChatLuong, Long> {

    @Query(
        value = "SELECT * FROM NH_PHIEU_KT_CHAT_LUONG PKTCL " +
                "WHERE (:soPhieu IS NULL OR PKTCL.SO_PHIEU = TO_NUMBER(:soPhieu)) " +
//                "AND (:soQd IS NULL OR LOWER(PKTCL.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%')))"+
//                "AND (:ketLuan IS NULL OR PKTCL.KET_LUAN=:ketLuan)"+
                "AND (:ngayLPhieu IS NULL OR TO_CHAR(PKTCL.NGAY_TAO,'yyyy-MM-dd') = :ngayLPhieu) " +
                "AND (:nguoiGiaoHang IS NULL OR LOWER(PKTCL.NGUOI_GIAO_HANG) " +
                "LIKE LOWER(CONCAT(CONCAT('%', :nguoiGiaoHang), '%')))",
            nativeQuery = true)
    Page<QlpktclhPhieuKtChatLuong> selectPage( String soPhieu, String ngayLPhieu, String nguoiGiaoHang, Pageable pageable);

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<QlpktclhPhieuKtChatLuong> findFirstBySoPhieu(String soPhieu);

    List<QlpktclhPhieuKtChatLuong> findByIdIn(Collection<Long> ids);

    List<QlpktclhPhieuKtChatLuong> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    QlpktclhPhieuKtChatLuong findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

}
