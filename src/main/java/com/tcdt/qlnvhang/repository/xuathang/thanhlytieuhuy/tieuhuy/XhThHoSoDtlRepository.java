package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhThHoSoDtlRepository extends JpaRepository<XhThHoSoDtl, Long> {

  void deleteAllByIdHdr(Long idHdr);

  List<XhThHoSoDtl> findAllByIdHdr(Long idHdr);

}
