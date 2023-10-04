package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlQdGiaoNvDtlRepository extends JpaRepository<XhTlQdGiaoNvDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTlQdGiaoNvDtl> findAllByIdHdr(Long idHdr);

    List<XhTlQdGiaoNvDtl> findAllByIdHdrAndPhanLoai(Long idHdr,String phanLoai);
}