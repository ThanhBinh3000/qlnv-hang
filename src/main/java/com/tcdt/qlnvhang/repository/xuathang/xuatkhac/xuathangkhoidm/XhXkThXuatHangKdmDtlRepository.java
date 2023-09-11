package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkThXuatHangKdmDtlRepository extends JpaRepository<XhXkThXuatHangKdmDtl, Long> {

    void deleteAllByIdIn(List<Long> listId);

    List<XhXkThXuatHangKdmDtl> findByIdIn(List<Long> ids);

}
