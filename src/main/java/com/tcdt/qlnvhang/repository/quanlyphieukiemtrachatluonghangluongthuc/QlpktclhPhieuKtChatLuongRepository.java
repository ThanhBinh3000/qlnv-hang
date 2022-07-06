package com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Repository
public interface QlpktclhPhieuKtChatLuongRepository extends BaseRepository<QlpktclhPhieuKtChatLuong, Long>, QlpktclhPhieuKtChatLuongRepositoryCustom {

    @Query(
        value = "SELECT * FROM NH_PHIEU_KT_CHAT_LUONG PKTCL " +
                "WHERE (:soPhieu IS NULL OR PKTCL.SO_PHIEU = TO_NUMBER(:soPhieu)) " +
                "AND (:ngayLPhieu IS NULL OR TO_CHAR(PKTCL.NGAY_TAO,'yyyy-MM-dd') = :ngayLPhieu) " +
                "AND (:nguoiGiaoHang IS NULL OR LOWER(PKTCL.NGUOI_GIAO_HANG) " +
                "LIKE LOWER(CONCAT(CONCAT('%', :nguoiGiaoHang), '%')))",
            nativeQuery = true)
    Page<QlpktclhPhieuKtChatLuong> select(String soPhieu, String ngayLPhieu, String nguoiGiaoHang, Pageable pageable);

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<QlpktclhPhieuKtChatLuong> findFirstBySoPhieu(String soPhieu);
}
