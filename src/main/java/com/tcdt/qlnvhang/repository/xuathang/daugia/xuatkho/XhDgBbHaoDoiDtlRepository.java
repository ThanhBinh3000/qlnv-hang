package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhDgBbHaoDoiDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBbHaoDoiDtlRepository extends JpaRepository<XhDgBbHaoDoiDtl,Long> {
  List<XhDgBbHaoDoiDtl> findByIdHdr(Long id);

  List<XhDgBbHaoDoiDtl> findAllByIdHdrIn(List<Long> listId);
}
