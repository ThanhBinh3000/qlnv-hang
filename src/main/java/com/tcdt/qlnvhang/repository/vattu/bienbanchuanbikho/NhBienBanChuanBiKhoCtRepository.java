package com.tcdt.qlnvhang.repository.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.entities.vattu.bienbanguihang.NhBienBanGuiHangCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhBienBanChuanBiKhoCtRepository extends BaseRepository<NhBienBanChuanBiKhoCt, Long> {
    List<NhBienBanChuanBiKhoCt> findByBbChuanBiKhoIdIn(Collection<Long> bbCbkIds);

    @Transactional
    @Modifying
    void deleteByBbChuanBiKhoIdIn(Collection<Long> bbCbkIds);
}
