package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface XhQdPdKhBdgService extends BaseService<XhQdPdKhBdg, XhQdPdKhBdgReq,Long> {

      public XhQdPdKhBdgDtl detailDtl(Long ids) throws Exception ;

}