package com.tcdt.qlnvhang.repository.banhang;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.BhHopDongDtl;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface BhHopDongDtlRepository  extends BaseRepository<BhHopDongDtl,Long> {
    @Transactional
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);

    List<BhHopDongDtl> findAllByIdHdrIn(Collection<Long> idHdr);

    @Transactional
    List findAllByIdHdr(Long idHdr);
}
