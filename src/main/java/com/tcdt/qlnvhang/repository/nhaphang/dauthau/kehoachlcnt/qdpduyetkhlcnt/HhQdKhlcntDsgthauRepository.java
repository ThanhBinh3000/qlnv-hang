package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface HhQdKhlcntDsgthauRepository extends BaseRepository<HhQdKhlcntDsgthau, Long> {

    List<HhQdKhlcntDsgthau> findByIdQdDtl(Long IdQdDtl);
    List<HhQdKhlcntDsgthau> findByIdQdHdr(Long IdQdHdr);
    List<HhQdKhlcntDsgthau> findByIdQdHdrOrderByGoiThauAsc(Long IdQdHdr);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_QD_KHLCNT_DSGTHAU SET TRANG_THAI =:trangThai , LY_DO_HUY =:lyDoHuy WHERE ID =TO_NUMBER(:idGt) ", nativeQuery = true)
    void updateGoiThau(Long idGt, String trangThai, String lyDoHuy);

    void deleteByIdQdDtl(Long idQdDtl);
    void deleteByIdQdHdr(Long idQdHdr);

    @Query(value = "SELECT NVL(SUM(DTL.THANH_TIEN),0) FROM HH_QD_KHLCNT_DSGTHAU DTL WHERE DTL.ID_QD_DTL = :idQdDtl ", nativeQuery = true)
    BigDecimal sumTotalPriceByIdQdDtl(Long idQdDtl);

}
