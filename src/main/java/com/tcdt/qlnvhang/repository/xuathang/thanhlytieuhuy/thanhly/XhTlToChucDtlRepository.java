package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlToChuc;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlToChucDtlRepository extends JpaRepository<XhTlToChucDtl, Long> {

    List<XhTlToChucDtl> findByIdIn(List<Long> ids);

    List<XhTlToChucDtl> findAllByIdHdr(Long idHdr);

    void deleteAllByIdHdr(Long idHdr);
}
