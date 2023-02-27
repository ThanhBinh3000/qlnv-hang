package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhHopDongBttDtlRepository extends JpaRepository<XhHopDongBttDtl,Long> {

    @Transactional
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    List<XhHopDongBttDtl> findAllByIdHdr(Long idHdr);

     @Transactional
   Optional<XhHopDongBttDtl>  findByIdHdDtl(Long idHdDtl);
    @Transactional
    List<XhHopDongBttDtl>  findAllByIdHdDtl(Long idHdDtl);



}
