package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import org.springframework.stereotype.Service;

@Service
public interface XhQdPdKhBdgService extends BaseService<XhQdPdKhBdg, XhQdPdKhBdgReq,Long> {

      public XhQdPdKhBdgDtl detailDtl(Long ids) throws Exception ;

}