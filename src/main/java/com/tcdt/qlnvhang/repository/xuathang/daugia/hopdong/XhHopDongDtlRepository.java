package com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDtl;
import com.tcdt.qlnvhang.repository.BaseRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface XhHopDongDtlRepository extends BaseRepository<XhHopDongDtl,Long> {
    @Transactional
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);

    List<XhHopDongDtl> findAllByIdHdrIn(Collection<Long> idHdr);

    @Transactional
    List<XhHopDongDtl> findAllByIdHdr(Long idHdr);
}
