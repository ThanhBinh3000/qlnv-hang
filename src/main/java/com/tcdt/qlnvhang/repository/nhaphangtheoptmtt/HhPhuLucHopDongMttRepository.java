package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhuLucHopDongMtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhPhuLucHopDongMttRepository extends JpaRepository<HhPhuLucHopDongMtt,Long> {
    Optional<HhPhuLucHopDongMtt> findAllByPhuLucSo(Integer plucSo);

    List<HhPhuLucHopDongMtt> findAllByIdIn(List<Long> ids);

    List<HhPhuLucHopDongMtt> findAllByIdHdHdr(Long ids);
    List<HhPhuLucHopDongMtt> findAllByIdHdHdrIn(List<Long> ids);
}
