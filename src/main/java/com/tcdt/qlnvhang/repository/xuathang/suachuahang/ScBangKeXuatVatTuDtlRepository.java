package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScBangKeXuatVatTuDtlRepository extends JpaRepository<ScBangKeXuatVatTuDtl,Long> {
    List<ScBangKeXuatVatTuDtl> findByIdHdr(Long id);

    List<ScBangKeXuatVatTuDtl> findByIdHdrIn(List<Long> ids);

    void deleteAllByIdHdr(Long idHdr);
}
