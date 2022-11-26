package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhBienBanChuanBiKhoRepository extends BaseRepository<NhBienBanChuanBiKho, Long> {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    List<NhBienBanChuanBiKho> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh,String maDvi);

    Optional<NhBienBanChuanBiKho> findFirstBySoBienBan(String soBienBan);

    NhBienBanChuanBiKho findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

}
