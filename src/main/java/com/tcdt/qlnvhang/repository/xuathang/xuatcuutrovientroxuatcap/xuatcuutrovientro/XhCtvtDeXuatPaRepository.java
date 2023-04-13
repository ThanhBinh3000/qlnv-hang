package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatPa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtDeXuatPaRepository extends JpaRepository<XhCtvtDeXuatPa,Long> {
    List<XhCtvtDeXuatPa> findByXhCtvtDeXuatHdrId(Long idHdr);

    List<XhCtvtDeXuatPa> findAllByXhCtvtDeXuatHdrIdIn(List<Long> ids);
}
