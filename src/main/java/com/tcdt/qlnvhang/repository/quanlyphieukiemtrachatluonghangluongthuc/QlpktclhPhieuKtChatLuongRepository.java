package com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface QlpktclhPhieuKtChatLuongRepository extends CrudRepository<QlpktclhPhieuKtChatLuong, Long>, QlpktclhPhieuKtChatLuongRepositoryCustom {

    @Query(
        value = "SELECT * FROM QLPKTCLH_PHIEU_KT_CHAT_LUONG PKTCL " +
                "WHERE (:soPhieu IS NULL OR PKTCL.SO_PHIEU = TO_NUMBER(:soPhieu)) " +
                "AND (:ngayLPhieu IS NULL OR TO_CHAR(PKTCL.NGAY_TAO,'yyyy-MM-dd') = :ngayLPhieu) " +
                "AND (:nguoiGiaoHang IS NULL OR LOWER(PKTCL.NGUOI_GIAO_HANG) " +
                "LIKE LOWER(CONCAT(CONCAT('%', :nguoiGiaoHang), '%')))",
            nativeQuery = true)
    Page<QlpktclhPhieuKtChatLuong> select(Long soPhieu, String ngayLPhieu, String nguoiGiaoHang, Pageable pageable);
}
