package com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhHopDongDdiemNhapKhoRepository extends BaseRepository<XhHopDongDdiemNhapKho, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhHopDongDdiemNhapKho> findAllByIdDtl(Long idDtl);
}
