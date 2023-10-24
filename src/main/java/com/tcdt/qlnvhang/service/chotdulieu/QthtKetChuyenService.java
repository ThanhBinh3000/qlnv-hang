package com.tcdt.qlnvhang.service.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtKetChuyenReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenHdr;

import java.util.List;


public interface QthtKetChuyenService extends BaseService<QthtKetChuyenHdr, QthtKetChuyenReq, Long> {

    List<QthtKetChuyenHdr> createList (List<QthtKetChuyenReq> req) throws Exception;
}
