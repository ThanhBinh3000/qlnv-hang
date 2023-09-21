package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTlHoSoDtlRepository extends JpaRepository<XhTlHoSoDtl, Long> {


    List<ScTrinhThamDinhDtl> findAllByIdHdr(Long idHdr);

    void deleteAllByIdHdr(Long idHdr);
}
