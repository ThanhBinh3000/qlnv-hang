package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XhXkDsHangDtqgDtlRepository extends JpaRepository<XhXkDsHangDtqgDtl, Long> {

    void deleteAllByIdIn(List<Long> listId);

    void deleteAllByIdHdr(Long idHdr);

    List<XhXkDsHangDtqgDtl> findAllByIdHdr(Long idHdr);

    List<XhXkDsHangDtqgDtl> findAllByIdHdrIn(List<Long> listId);

}
