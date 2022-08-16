package com.tcdt.qlnvhang.repository.banhang;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.BhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.table.HhHopDongDdiemNhapKho;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BhHopDongDdiemNhapKhoRepository extends BaseRepository<BhHopDongDdiemNhapKho,Long> {
    List<BhHopDongDdiemNhapKho> findAllByIdHdongHdr(Long idHdongHdr);

    List<BhHopDongDdiemNhapKho> findAllByIdHdongHdrIn(Collection<Long> idHdongHdrs);
}
