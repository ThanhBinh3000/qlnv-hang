package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgPloRepository extends JpaRepository<XhTcTtinBdgPlo, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhTcTtinBdgPlo> findAllByIdDtl(Long idDtl);
}