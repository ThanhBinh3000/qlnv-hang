package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbKqDcDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcHdr;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DcnbBbKqDcService {
    Page<DcnbBbKqDcHdr> search(CustomUserDetails currentUser, DcnbBbKqDcSearch objReq);

    List<DcnbBbKqDcHdr> searchList(CustomUserDetails currentUser, DcnbBbKqDcSearch objReq);

    DcnbBbKqDcHdr create(DcnbBbKqDcDTO objReq) throws Exception;

    DcnbBbKqDcHdr update(DcnbBbKqDcDTO objReq) throws Exception;

    DcnbBbKqDcHdr detail(Long id) throws Exception;

    void approve(StatusReq objReq) throws Exception;

    void delete(Long id) throws Exception;

    void export(DcnbBbKqDcSearch objReq, HttpServletResponse response) throws Exception;

    void deleteMulti(List<Long> ids);
}
