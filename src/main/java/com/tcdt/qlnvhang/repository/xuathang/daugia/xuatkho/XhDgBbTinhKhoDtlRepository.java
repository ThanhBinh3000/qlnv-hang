package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhDgBbTinhKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBbTinhKhoDtlRepository extends JpaRepository<XhDgBbTinhKhoDtl,Long> {
  List<XhDgBbTinhKhoDtl> findByIdHdr(Long id);

  List<XhDgBbTinhKhoDtl> findAllByIdHdrIn(List<Long> listId);
}
