package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbTinhkBttDtlRepository extends JpaRepository<XhBbTinhkBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhBbTinhkBttDtl> findAllByIdHdr(Long idHdr);

    List<XhBbTinhkBttDtl> findByIdHdrIn(List<Long> listId);
}