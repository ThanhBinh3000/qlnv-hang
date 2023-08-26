package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgDtlRepository extends JpaRepository<XhTcTtinBdgDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTcTtinBdgDtl> findAllByIdHdr(Long idHdr);
}