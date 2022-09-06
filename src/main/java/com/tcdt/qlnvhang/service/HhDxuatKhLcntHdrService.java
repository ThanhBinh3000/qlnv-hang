package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;

public interface HhDxuatKhLcntHdrService {

	@Transactional(rollbackOn = Exception.class)
	HhDxuatKhLcntHdr create(HhDxuatKhLcntHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxuatKhLcntHdr update(HhDxuatKhLcntHdrReq objReq) throws Exception;

	HhDxuatKhLcntHdr detail(Long id) throws Exception;

	Page<HhDxuatKhLcntHdr> colection(HhDxuatKhLcntSearchReq objReq, HttpServletRequest req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxuatKhLcntHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

	void deleteMulti(IdSearchReq idSearchReq) throws Exception;

	void exportToExcel(IdSearchReq searchReq, HttpServletResponse response) throws Exception;

//	void exportDsKhlcnt(HhDxuatKhLcntSearchReq searchReq, HttpServletResponse response) throws Exception;


//    Page<HhDxuatKhLcntHdr> timKiem (HttpServletRequest request, HhDxuatKhLcntSearchReq objReq) throws Exception;

	void exportDsKhlcnt(HhDxuatKhLcntSearchReq req, HttpServletResponse response) throws Exception;

	void exportList(HhDxuatKhLcntSearchReq objReq, HttpServletResponse response) throws Exception;

	Page<HhDxuatKhLcntHdr> timKiem(HhDxuatKhLcntSearchReq req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxuatKhLcntHdr createVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxuatKhLcntHdr updateVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception;

	HhDxuatKhLcntHdr detailVatTu(String ids) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxuatKhLcntHdr approveVatTu(StatusReq stReq) throws Exception;

}
