package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBbHaoDoiDtlRepository extends JpaRepository<XhDgBbHaoDoiDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhDgBbHaoDoiDtl> findAllByIdHdr(Long idHdr);

    List<XhDgBbHaoDoiDtl> findByIdHdrIn(List<Long> listId);
}