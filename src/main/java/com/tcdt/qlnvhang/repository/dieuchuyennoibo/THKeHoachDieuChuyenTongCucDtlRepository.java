package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenTongCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface THKeHoachDieuChuyenTongCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenTongCucDtl, Long> {
    List<THKeHoachDieuChuyenTongCucDtl> findByHdrId(Long id);

    List<THKeHoachDieuChuyenTongCucDtl> findAllByIdIn(List<Long> listId);
}
