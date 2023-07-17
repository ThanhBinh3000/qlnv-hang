package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScBangKeNhapVtDtlRepository extends JpaRepository<ScBangKeNhapVtDtl, Long> {

    List<ScBangKeNhapVtDtl> findByIdHdr(Long id);

    List<ScBangKeNhapVtDtl> findByIdHdrIn(List<Long> listId);

}
