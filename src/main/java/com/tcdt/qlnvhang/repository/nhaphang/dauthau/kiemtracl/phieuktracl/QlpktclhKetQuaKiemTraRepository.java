package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuongCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface QlpktclhKetQuaKiemTraRepository extends BaseRepository<NhPhieuKtChatLuongCt, Long> {
    @Transactional
    @Modifying
    void deleteByPhieuKtChatLuongId(Long phieuKtChatLuongId);

    List<NhPhieuKtChatLuongCt> findAllByPhieuKtChatLuongId(Long phieuKtChatLuongId);

    @Transactional
    @Modifying
    void deleteByPhieuKtChatLuongIdIn(Collection<Long> phieuKtChatLuongIds);
}
