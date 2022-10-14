package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface HhCtietTtinCgiaRepository extends JpaRepository<HhChiTietTTinChaoGia,Long> {
    List<HhChiTietTTinChaoGia> findAllByIdSoQdPduyetCgia(Long idSoQdPduyetCgia);

    List<HhChiTietTTinChaoGia> findAllByIdSoQdPduyetCgiaIn(List<Long> ids);

    @Transactional
    @Modifying
    @Query(value = "UPDATE HH_CTIET_TTIN_CHAO_GIA SET LUA_CHON_PDUYET =:luaChonPduyet where ID=:id",nativeQuery = true)
    void updateLcPd(Long id,Boolean luaChonPduyet);


}
