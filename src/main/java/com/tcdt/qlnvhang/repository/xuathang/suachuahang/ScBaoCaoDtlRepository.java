package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBaoCaoDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScBaoCaoDtlRepository extends JpaRepository<ScBaoCaoDtl, Long> {

  List<ScBaoCaoDtl> findByIdIn(List<Long> ids);

  void deleteAllByIdHdr(Long idHdr);

  List<ScBaoCaoDtl> findAllByIdHdr(Long idHdr);

}
