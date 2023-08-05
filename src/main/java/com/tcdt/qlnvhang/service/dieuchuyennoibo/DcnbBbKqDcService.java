package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbKqDcDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcHdr;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DcnbBbKqDcService {
    Page<DcnbBcKqDcHdr> search(CustomUserDetails currentUser, DcnbBbKqDcSearch objReq);

    List<DcnbBcKqDcHdr> searchList(CustomUserDetails currentUser, DcnbBbKqDcSearch objReq);

    DcnbBcKqDcHdr create(DcnbBbKqDcDTO objReq) throws Exception;

    DcnbBcKqDcHdr update(DcnbBbKqDcDTO objReq) throws Exception;

    DcnbBcKqDcHdr detail(Long id) throws Exception;

    void approve(StatusReq objReq) throws Exception;

    void delete(Long id) throws Exception;

    void export(DcnbBbKqDcSearch objReq, HttpServletResponse response) throws Exception;

    void deleteMulti(List<Long> ids);

    List<DcnbBcKqDcDtl> thongTinNhapXuatHangChiCuc(DcnbBbKqDcSearch objReq) throws Exception;

    List<DcnbBcKqDcDtl> thongTinNhapXuatHangCuc(DcnbBbKqDcSearch objReq) throws Exception;

    void finish(StatusReq objReq) throws Exception;
}
