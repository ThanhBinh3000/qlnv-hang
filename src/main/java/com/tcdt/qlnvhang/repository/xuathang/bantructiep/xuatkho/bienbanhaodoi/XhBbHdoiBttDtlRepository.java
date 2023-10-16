package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbHdoiBttDtlRepository extends JpaRepository<XhBbHdoiBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhBbHdoiBttDtl> findAllByIdHdr(Long idHdr);

    List<XhBbHdoiBttDtl> findByIdHdrIn(List<Long> listId);
}