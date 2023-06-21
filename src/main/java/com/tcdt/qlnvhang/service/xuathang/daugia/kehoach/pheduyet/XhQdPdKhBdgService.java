package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface XhQdPdKhBdgService extends BaseService<XhQdPdKhBdg, XhQdPdKhBdgReq,Long> {

      public XhQdPdKhBdgDtl detailDtl(Long ids) throws Exception ;

      Page<XhQdPdKhBdgDtl> searchDtlPage (XhQdPdKhBdgDtlReq req) throws Exception;

      void exportDtl(XhQdPdKhBdgDtlReq req, HttpServletResponse response) throws Exception;

}