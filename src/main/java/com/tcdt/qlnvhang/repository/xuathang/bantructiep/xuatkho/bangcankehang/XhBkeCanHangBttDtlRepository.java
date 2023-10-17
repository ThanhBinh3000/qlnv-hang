package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bangcankehang;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBkeCanHangBttDtlRepository extends JpaRepository<XhBkeCanHangBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhBkeCanHangBttDtl> findAllByIdHdr(Long idHdr);

    List<XhBkeCanHangBttDtl> findByIdHdrIn(List<Long> listId);
}