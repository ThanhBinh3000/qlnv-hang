package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbLayMauBttDtlRepository extends JpaRepository<XhBbLayMauBttDtl , Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhBbLayMauBttDtl> findAllByIdHdr(Long idHdr);

    List<XhBbLayMauBttDtl> findByIdHdrIn(List<Long> listId);
}