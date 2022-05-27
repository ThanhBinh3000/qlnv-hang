package com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhKetQuaKiemTra;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface QlpktclhKetQuaKiemTraRepository extends BaseRepository<QlpktclhKetQuaKiemTra, Long> {
    @Transactional
    @Modifying
    void deleteByPhieuKtChatLuongId(Long phieuKtChatLuongId);

    List<QlpktclhKetQuaKiemTra> findAllByPhieuKtChatLuongId(Long phieuKtChatLuongId);
}
