package com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.BhHopDongDdiemNhapKho;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BhHopDongDdiemNhapKhoRepository extends BaseRepository<BhHopDongDdiemNhapKho,Long> {
    List<BhHopDongDdiemNhapKho> findAllByIdHdongHdr(Long idHdongHdr);

    List<BhHopDongDdiemNhapKho> findAllByIdHdongHdrIn(Collection<Long> idHdongHdrs);
}
