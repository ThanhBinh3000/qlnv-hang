package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBbTinhKhoDtlRepository extends JpaRepository<XhDgBbTinhKhoDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhDgBbTinhKhoDtl> findAllByIdHdr(Long idHdr);

    List<XhDgBbTinhKhoDtl> findByIdHdrIn(List<Long> listId);
}