package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgNlqRepository extends JpaRepository<XhTcTtinBdgNlq, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTcTtinBdgNlq> findAllByIdHdr(Long idHdr);
}