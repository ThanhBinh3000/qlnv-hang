package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.table.chotdulieu.KhPagTongHopCTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhLtPagTongHopCTietRepository extends JpaRepository<KhPagTongHopCTiet, Long> {
    List<KhPagTongHopCTiet> findByIdIn(List<Long> ids);

    List<KhPagTongHopCTiet> findAllByPagThIdAndQdTcdtnnIdIsNull(Long id);

    List<KhPagTongHopCTiet> findByPagThIdInAndQdTcdtnnIdIsNull(List<Long> ids);

    List<KhPagTongHopCTiet> findAllByQdTcdtnnId(Long idQd);

    List<KhPagTongHopCTiet> findAllByQdTcdtnnIdIn(List<Long> idsQd);

    void deleteAllByPagThId(Long id);

    void deleteAllByQdTcdtnnId(Long id);
}
