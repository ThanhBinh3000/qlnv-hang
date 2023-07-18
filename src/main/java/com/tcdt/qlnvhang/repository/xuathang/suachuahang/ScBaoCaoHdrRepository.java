package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBaoCaoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScBaoCaoHdrRepository extends JpaRepository<ScBaoCaoHdr, Long> {

}
