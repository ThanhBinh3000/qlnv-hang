package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QthtChotGiaNhapXuatRepository extends JpaRepository<QthtChotGiaNhapXuat, Long> {


    @Query(value = "SELECT q FROM QthtChotGiaNhapXuat q WHERE 1 = 1" +
            " ORDER BY q.ngaySua desc , q.ngayTao desc, q.id desc "
    )
    Page<QthtChotGiaNhapXuat> searchPage(@Param("param") QthtChotGiaNhapXuatReq req, Pageable pageable);

}
