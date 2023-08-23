package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttDtlRepository extends JpaRepository<XhQdDchinhKhBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhQdDchinhKhBttDtl> findAllByIdHdr(Long idHdr);

    List<XhQdDchinhKhBttDtl> findByIdHdrIn(List<Long> listId);
}
