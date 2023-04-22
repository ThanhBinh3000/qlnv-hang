package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenTongCucHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface THKeHoachDieuChuyenTongCucHdrRepository extends JpaRepository<THKeHoachDieuChuyenTongCucHdr,Long> {
    Optional<THKeHoachDieuChuyenTongCucHdr> findByMaTongHop(String maTongHop);

//    Page<THKeHoachDieuChuyenTongCucHdr> search(TongHopKeHoachDieuChuyenSearch req, Pageable pageable);

    List<THKeHoachDieuChuyenTongCucHdr> findByIdIn(List<Long> ids);
}
