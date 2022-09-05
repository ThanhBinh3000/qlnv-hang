package com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdg;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReqExt;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface BhQdPheDuyetKqbdgService {
    @Transactional(rollbackOn = Exception.class)
    BhQdPheDuyetKqbdgRes create(BhQdPheDuyetKqbdgReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    BhQdPheDuyetKqbdgRes update(BhQdPheDuyetKqbdgReq req) throws Exception;

    BhQdPheDuyetKqbdgRes detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq stReq) throws Exception;

   //Page<BhQdPheDuyetKqbdgRes> search(BhQdPheDuyetKqbdgSearchReq req) throws Exception;

    Page<BhQdPheDuyetKqbdgRes> search(BhQdPheDuyetKqbdgSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(BhQdPheDuyetKqbdgSearchReq objReq, HttpServletResponse response) throws Exception;

    Page<BhQdPheDuyetKqbdg> listData(BhQdPheDuyetKqbdgSearchReqExt reqExt, Pageable pageable) throws Exception;
}
