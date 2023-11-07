package com.tcdt.qlnvhang.service.nhaphang.dauthau.tochuctrienkhai;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.tochuctrienkhai.QdPdHsmt;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.dauthauvattu.QdPdHsmtReq;
import com.tcdt.qlnvhang.request.search.QdPdHsmtSearchReq;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public interface QdPdHsmtService {
    Page<QdPdHsmt> timKiem(QdPdHsmtSearchReq req) throws Exception;

    QdPdHsmt create(QdPdHsmtReq objReq) throws Exception;

    QdPdHsmt update(QdPdHsmtReq objReq) throws Exception;

    QdPdHsmt detail(Long id) throws Exception;

    QdPdHsmt approve(StatusReq stReq) throws Exception;

    void exportList(QdPdHsmtSearchReq req, HttpServletResponse response) throws Exception;
    void delete(IdSearchReq idSearchReq) throws Exception;

    void deleteMulti(IdSearchReq idSearchReq) throws Exception;
}
