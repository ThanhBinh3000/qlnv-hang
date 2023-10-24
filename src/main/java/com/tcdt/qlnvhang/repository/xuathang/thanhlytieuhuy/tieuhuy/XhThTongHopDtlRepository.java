package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhThTongHopDtlRepository extends JpaRepository<XhThTongHopDtl, Long> {

  List<XhThTongHopDtl> findAllByIdTongHop(Long idTongHop);

  void deleteAllByIdTongHop(Long idTongHop);
}
