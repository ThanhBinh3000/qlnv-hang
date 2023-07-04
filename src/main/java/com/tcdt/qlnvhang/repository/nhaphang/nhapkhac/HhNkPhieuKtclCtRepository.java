package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtclCt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkPhieuKtclCtRepository extends JpaRepository<HhNkPhieuKtclCt, Long> {
    List<HhNkPhieuKtclCt> findAllByPhieuKtChatLuongId(Long hdrId);
    void deleteAllByPhieuKtChatLuongId(long idHdr);
}
