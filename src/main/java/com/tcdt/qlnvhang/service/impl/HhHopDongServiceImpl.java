package com.tcdt.qlnvhang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.HhQdPduyetKqlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.object.HhHopDongDtlReq;
import com.tcdt.qlnvhang.request.object.HhHopDongHdrReq;
import com.tcdt.qlnvhang.request.search.HhHopDongSearchReq;
import com.tcdt.qlnvhang.secification.HhHopDongSpecification;
import com.tcdt.qlnvhang.service.HhHopDongService;
import com.tcdt.qlnvhang.table.HhDdiemNhapKho;
import com.tcdt.qlnvhang.table.HhDviLquan;
import com.tcdt.qlnvhang.table.HhHopDongDtl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhHopDongServiceImpl extends BaseServiceImpl implements HhHopDongService {
	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public HhHopDongHdr create(HhHopDongHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
		if (qOpHdong.isPresent())
			throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");

		Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
		if (!checkSoQd.isPresent())
			throw new Exception(
					"Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");

		HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setMaDvi(getDvql(req));

		// add thong tin detail
		List<HhHopDongDtlReq> dtlReqList = objReq.getDetail();
		List<HhHopDongDtl> details = new ArrayList<>();
		if (dtlReqList != null) {
			List<HhDdiemNhapKho> detailChild;
			for (HhHopDongDtlReq dtlReq : dtlReqList) {
				List<HhDdiemNhapKhoReq> cTietReq = dtlReq.getDetail();
				HhHopDongDtl detail = ObjectMapperUtils.map(dtlReq, HhHopDongDtl.class);
				detail.setType(Contains.HOP_DONG);
				detailChild = new ArrayList<HhDdiemNhapKho>();
				if (cTietReq != null)
					detailChild = ObjectMapperUtils.mapAll(cTietReq, HhDdiemNhapKho.class);
				detailChild.forEach(f -> {
					f.setType(Contains.HOP_DONG);
				});

				detail.setChildren(detailChild);
				details.add(detail);
			}
			dataMap.setChildren(details);
		}

		// add thong tin don vi lien quan
		List<HhDviLquan> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDviLquan.class);
		dataMap.setChildren1(dtls1);

		// File dinh kem cua goi thau
		List<FileDKemJoinHopDong> dtls2 = new ArrayList<FileDKemJoinHopDong>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDong.class);
			dtls2.forEach(f -> {
				f.setDataType(HhHopDongHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataMap.setChildren2(dtls2);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		return hhHopDongRepository.save(dataMap);
	}

	@Override
	public HhHopDongHdr update(HhHopDongHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (!qOptional.get().getSoHd().equals(objReq.getSoHd())) {
			Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
			if (qOpHdong.isPresent())
				throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");
		}

		if (!qOptional.get().getCanCu().equals(objReq.getCanCu())) {
			Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
			if (!checkSoQd.isPresent())
				throw new Exception(
						"Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");
		}

		HhHopDongHdr dataDB = qOptional.get();
		HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());

		// add thong tin detail
		List<HhHopDongDtlReq> dtlReqList = objReq.getDetail();
		List<HhHopDongDtl> details = new ArrayList<>();
		if (dtlReqList != null) {
			List<HhDdiemNhapKho> detailChild;
			for (HhHopDongDtlReq dtlReq : dtlReqList) {
				List<HhDdiemNhapKhoReq> cTietReq = dtlReq.getDetail();
				HhHopDongDtl detail = ObjectMapperUtils.map(dtlReq, HhHopDongDtl.class);
				detail.setType(Contains.HOP_DONG);
				detailChild = new ArrayList<HhDdiemNhapKho>();
				if (cTietReq != null)
					detailChild = ObjectMapperUtils.mapAll(cTietReq, HhDdiemNhapKho.class);
				detail.setChildren(detailChild);
				details.add(detail);
			}
			dataDB.setChildren(details);
		}

		// add thong tin don vi lien quan
		List<HhDviLquan> dtls1 = ObjectMapperUtils.mapAll(dtlReqList, HhDviLquan.class);
		dataDB.setChildren1(dtls1);

		// File dinh kem cua goi thau
		List<FileDKemJoinHopDong> dtls2 = new ArrayList<FileDKemJoinHopDong>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDong.class);
			dtls2.forEach(f -> {
				f.setDataType(HhHopDongHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataDB.setChildren2(dtls2);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);

		return hhHopDongRepository.save(dataDB);
	}

	@Override
	public HhHopDongHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhHopDongDtl.class);
		for (HhHopDongDtl dtl : dtls2) {
			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
		}

		return qOptional.get();
	}

	@Override
	public HhHopDongHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
		if (StringUtils.isEmpty(strSearchReq.getStr()))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findBySoHd(strSearchReq.getStr());

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhHopDongDtl.class);
		for (HhHopDongDtl dtl : dtls2) {
			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
		}

		return qOptional.get();
	}

	@Override
	public Page<HhHopDongHdr> colection(HhHopDongSearchReq objReq, HttpServletRequest req) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhHopDongHdr> dataPage = hhHopDongRepository.findAll(HhHopDongSpecification.buildSearchQuery(objReq),
				pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapTenDvi();
		for (HhHopDongHdr hdr : dataPage.getContent()) {
			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
		}
		return dataPage;
	}

	@Override
	public HhHopDongHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhHopDongHdr> optional = hhHopDongRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
		case Contains.CHO_DUYET + Contains.MOI_TAO:
			optional.get().setNguoiGuiDuyet(getUser().getUsername());
			optional.get().setNgayGuiDuyet(getDateTimeNow());
			break;
		case Contains.TU_CHOI + Contains.CHO_DUYET:
			optional.get().setNguoiPduyet(getUser().getUsername());
			optional.get().setNgayPduyet(getDateTimeNow());
			optional.get().setLdoTuchoi(stReq.getLyDo());
			break;
		case Contains.DUYET + Contains.CHO_DUYET:
			optional.get().setNguoiPduyet(getUser().getUsername());
			optional.get().setNgayPduyet(getDateTimeNow());
			break;
		default:
			throw new Exception("Phê duyệt không thành công");
		}

		optional.get().setTrangThai(stReq.getTrangThai());

		// TODO: Cap nhat lai tinh trang hien thoi cua kho sau khi phe duyet quyet dinh
		// giao nhiem vu nhap hang
		return hhHopDongRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhHopDongHdr> optional = hhHopDongRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

		hhHopDongRepository.delete(optional.get());

	}

}
