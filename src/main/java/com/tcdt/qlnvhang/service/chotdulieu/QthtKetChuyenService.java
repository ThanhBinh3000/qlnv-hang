package com.tcdt.qlnvhang.service.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtKetChuyenReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenDtl;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenHdr;
import org.springframework.data.domain.Page;

import java.util.List;


public interface QthtKetChuyenService extends BaseService<QthtKetChuyenHdr, QthtKetChuyenReq, Long> {

    List<QthtKetChuyenHdr> createList (List<QthtKetChuyenReq> req) throws Exception;


    Page<QthtKetChuyenDtl> searchPageDtl(QthtKetChuyenReq req) throws Exception;

    List<String> searchDviKc(QthtKetChuyenReq req) throws Exception;
}
