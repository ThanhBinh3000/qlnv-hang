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

import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapxuat;
import com.tcdt.qlnvhang.repository.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.secification.HhQdGiaoNvuNhapxuatSpecification;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;
import com.tcdt.qlnvhang.table.HhDviThuchienQdinh;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhQdGiaoNvuNhapxuatServiceImpl extends BaseServiceImpl implements HhQdGiaoNvuNhapxuatService {
	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public HhQdGiaoNvuNhapxuatHdr create(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {

		Optional<HhQdGiaoNvuNhapxuatHdr> qOpQdinh = hhQdGiaoNvuNhapxuatRepository.findBySoQd(objReq.getSoQd());
		if (qOpQdinh.isPresent())
			throw new Exception("Hợp quyết định " + objReq.getSoHd() + " đã tồn tại");

		HhQdGiaoNvuNhapxuatHdr dataMap = ObjectMapperUtils.map(objReq, HhQdGiaoNvuNhapxuatHdr.class);

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setMaDvi(getDvql(req));

		// add thong tin chi tiet
		List<HhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetail(), HhQdGiaoNvuNhapxuatDtl.class);
		dataMap.setChildren(dtls);

		// add thong tin don vi thuc hien quyet dinh
		List<HhDviThuchienQdinh> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDviThuchienQdinh.class);
		dataMap.setChildren1(dtls1);

		// File dinh kem cua goi thau
		List<FileDKemJoinQdNhapxuat> dtls2 = new ArrayList<FileDKemJoinQdNhapxuat>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdNhapxuat.class);
			dtls2.forEach(f -> {
				f.setDataType(HhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataMap.setChildren2(dtls2);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		return hhQdGiaoNvuNhapxuatRepository.save(dataMap);
	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr update(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (!qOptional.get().getSoQd().equals(objReq.getSoQd())) {
			Optional<HhQdGiaoNvuNhapxuatHdr> qOpHdong = hhQdGiaoNvuNhapxuatRepository.findBySoQd(objReq.getSoQd());
			if (qOpHdong.isPresent())
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
		}

		HhQdGiaoNvuNhapxuatHdr dataDB = qOptional.get();
		HhQdGiaoNvuNhapxuatHdr dataMap = ObjectMapperUtils.map(objReq, HhQdGiaoNvuNhapxuatHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());

		// add thong tin chi tiet
		List<HhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetail(), HhQdGiaoNvuNhapxuatDtl.class);
		dataDB.setChildren(dtls);

		// add thong tin don vi thuc hien quyet dinh
		List<HhDviThuchienQdinh> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDviThuchienQdinh.class);
		dataDB.setChildren1(dtls1);

		// File dinh kem cua goi thau
		List<FileDKemJoinQdNhapxuat> dtls2 = new ArrayList<FileDKemJoinQdNhapxuat>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdNhapxuat.class);
			dtls2.forEach(f -> {
				f.setDataType(HhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataDB.setChildren2(dtls2);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);

		return hhQdGiaoNvuNhapxuatRepository.save(dataDB);
	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		UnitScaler.reverseFormatList(qOptional.get().getChildren(), Contains.DVT_TAN);

		return qOptional.get();
	}

	@Override
	public Page<HhQdGiaoNvuNhapxuatHdr> colection(HhQdNhapxuatSearchReq objReq, HttpServletRequest req)
			throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhQdGiaoNvuNhapxuatHdr> dataPage = hhQdGiaoNvuNhapxuatRepository
				.findAll(HhQdGiaoNvuNhapxuatSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapDmucDvi();
		for (HhQdGiaoNvuNhapxuatHdr hdr : dataPage.getContent()) {
			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
		}
		return dataPage;
	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(Long.valueOf(stReq.getId()));
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
		return hhQdGiaoNvuNhapxuatRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

		hhQdGiaoNvuNhapxuatRepository.delete(optional.get());

	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
