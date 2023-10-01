package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlToChuc;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucNlq;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlToChucNlqRepository extends JpaRepository<XhTlToChucNlq, Long> {


//    List<XhTlToChucHdr> findByIdQdTlDtlOrderByLanDauGia(Long idQdTlDtl);


    void deleteAllByIdHdr(Long idHdr);

    List<XhTlToChucNlq> findAllByIdHdr(Long idHdr);


}
