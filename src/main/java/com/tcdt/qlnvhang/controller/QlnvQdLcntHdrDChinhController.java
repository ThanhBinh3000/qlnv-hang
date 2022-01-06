package com.tcdt.qlnvhang.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.tcdt.qlnvhang.repository.QlnvQdLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntDtlCtietReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDchinhSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/qd-lcnt/dieu-chinh")
@Api(tags = "Quyết định phê duyệt KHLCNT")
public class QlnvQdLcntHdrDChinhController extends BaseController {
	@Autowired
	private QlnvQdLcntHdrRepository QlnvQdLcntHdrRepository;
	
	/**
	 * DIEU CHINH- search
	 * @param objReq
	 * @return
	 */
	@ApiOperation(value = "Tra cứu Quyết định điều chỉnh phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdLcntHdrDchinhSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvQdLcntHdr> qhKho = QlnvQdLcntHdrRepository.selectParams(objReq.getSoQdinh(),objReq.getSoQdGiaoCtkh(),objReq.getTuNgayQd(),objReq.getDenNgayQd(),objReq.getMaHanghoa(),objReq.getLoaiHanghoa(),
					objReq.getSoQdinhGoc(),objReq.getLoaiDieuChinh(), pageable);

			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}
	/**
	 * DIEU CHINH - TAO MOI
	 * @param request
	 * @param objReq
	 * @return
	 */
	@ApiOperation(value = "Tạo Quyết định điều chỉnh qđ phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> createDchinh(HttpServletRequest request,@Valid @RequestBody IdSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Tạo thất bại, không tìm thấy dữ liệu");
			Optional<QlnvQdLcntHdr> QlnvQdLcntHdr = QlnvQdLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!QlnvQdLcntHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần tạo");
			QlnvQdLcntHdr dataDTB = QlnvQdLcntHdr.get();
			List<QlnvQdLcntDtl> khoachList = dataDTB.getDetailList();
			QlnvQdLcntHdrReq hdrReq = new QlnvQdLcntHdrReq();
			List<QlnvQdLcntDtlReq> dtlReqList = new ArrayList<QlnvQdLcntDtlReq>();
			for (QlnvQdLcntDtl qlnvKhoachLcntHdr : khoachList) {
				

				
				QlnvQdLcntDtlReq dtlReq = new QlnvQdLcntDtlReq();
				List<QlnvQdLcntDtlCtietReq> detail = new ArrayList<QlnvQdLcntDtlCtietReq>();
				for (QlnvQdLcntDtlCtiet dtl : qlnvKhoachLcntHdr.getDetailList()) {
					QlnvQdLcntDtlCtietReq ctietReq = new QlnvQdLcntDtlCtietReq();
					ctietReq.setDiaChi(dtl.getDiaChi());
					ctietReq.setDonGia(dtl.getDonGia());
					ctietReq.setDviTinh(dtl.getDviTinh());
					ctietReq.setMaDvi(dtl.getMaDvi());
					ctietReq.setSoDxuat(dtl.getSoDxuat());
					ctietReq.setSoDuyet(dtl.getSoDuyet());
					ctietReq.setThuesuat(dtl.getThuesuat());
					ctietReq.setThanhtien(dtl.getThanhtien());
					ctietReq.setTongtien(dtl.getTongtien());
					detail.add(ctietReq);
				}
				dtlReq.setDetail(detail);
				dtlReq.setMaDvi(qlnvKhoachLcntHdr.getMaDvi());
				dtlReq.setSoDxuat(qlnvKhoachLcntHdr.getSoDxuat());
				dtlReqList.add(dtlReq);
				
			}
			hdrReq.setDetail(dtlReqList);
			hdrReq.setLoaiHanghoa(dataDTB.getLoaiHanghoa());
			hdrReq.setNguonvon(dataDTB.getNguonvon());
			hdrReq.setLoaiQd(Contains.QUYET_DINH_DC);
			hdrReq.setSoQdGiaoCtkh(dataDTB.getSoQdGiaoCtkh());
			hdrReq.setMaHanghoa(dataDTB.getMaHanghoa());
			hdrReq.setSoQdinhGoc(dataDTB.getSoQdinh());
			hdrReq.setTuNgayThien(dataDTB.getTuNgayThien());
			hdrReq.setDenNgayThien(dataDTB.getDenNgayThien());
			
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
			resp.setData(hdrReq);
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}