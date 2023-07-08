package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScTrinhThamDinhDtlRepository extends JpaRepository<ScTrinhThamDinhDtl, Long> {

    List<ScTrinhThamDinhDtl> findAllByIdHdr(Long idHdr);

    void deleteAllByIdHdr(Long idHdr);

}
