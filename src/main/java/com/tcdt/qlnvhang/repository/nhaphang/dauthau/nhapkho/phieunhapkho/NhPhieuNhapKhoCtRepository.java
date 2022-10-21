package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhPhieuNhapKhoCtRepository extends BaseRepository<NhPhieuNhapKhoCt, Long> {
//    List<NhPhieuNhapKhoCt> findAllByPhieuNkId(Long phieuNhapKhoId);
//
//    @Transactional
//    @Modifying
//    void deleteByPhieuNkIdIn(Collection<Long> phieuNhapKhoIds);
//
//    NhPhieuNhapKhoCt findFirstByPhieuNkIdAndMaVatTu(Long phieuNhapKhoId, String maVatTu);

    void deleteAllByIdPhieuNkHdr(Long idPhieuNKHdr);

    List<NhPhieuNhapKhoCt> findAllByIdPhieuNkHdr(Long idPhieuNkHdr);

}
