package com.tcdt.qlnvhang.repository.banhang;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.BhHopDongDtl;
import com.tcdt.qlnvhang.table.BhHopDongHdr;

import javax.transaction.Transactional;
import java.util.List;

public interface BhHopDongDtlRepository  extends BaseRepository<BhHopDongDtl,Long> {
    @Transactional
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);
}
