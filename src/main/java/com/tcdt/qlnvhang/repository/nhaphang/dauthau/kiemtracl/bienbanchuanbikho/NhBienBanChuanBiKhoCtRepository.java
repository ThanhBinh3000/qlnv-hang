package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhBienBanChuanBiKhoCtRepository extends BaseRepository<NhBienBanChuanBiKhoCt, Long> {
//    List<NhBienBanChuanBiKhoCt> findByBbChuanBiKhoIdIn(Collection<Long> bbCbkIds);
//
//    @Transactional
//    @Modifying
//    void deleteByBbChuanBiKhoIdIn(Collection<Long> bbCbkIds);

    List<NhBienBanChuanBiKhoCt> findAllByHdrId(Long idBbChuanBiKho);

    void deleteAllByHdrId(Long idBbChuanBiKho);
}
