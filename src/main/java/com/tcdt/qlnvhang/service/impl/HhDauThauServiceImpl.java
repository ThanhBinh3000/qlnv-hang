package com.tcdt.qlnvhang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.tcdt.qlnvhang.request.search.HhDthauSearchReq;
import com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinGoiThau;
import com.tcdt.qlnvhang.entities.FileDKemJoinHsoKthuat;
import com.tcdt.qlnvhang.entities.FileDKemJoinTthaoHdong;
import com.tcdt.qlnvhang.repository.HhDthauRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDthauGthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauHsoKthuatReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauTthaoHdongReq;
import com.tcdt.qlnvhang.service.HhDauThauService;
import com.tcdt.qlnvhang.table.HhDthau;
import com.tcdt.qlnvhang.table.HhDthauGthau;
import com.tcdt.qlnvhang.table.HhDthauHsoKthuat;
import com.tcdt.qlnvhang.table.HhDthauHsoTchinh;
import com.tcdt.qlnvhang.table.HhDthauKquaLcnt;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import com.tcdt.qlnvhang.table.HhDthauTthaoHdong;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhDauThauServiceImpl extends BaseServiceImpl implements HhDauThauService {
	@Autowired
	private HhDthauRepository hhDthauRepository;

	@Override
	public HhDthau create(HhDthauReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		HhDthau dataMap = ObjectMapperUtils.map(objReq, HhDthau.class);

		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);

		// Them thong tin chi tiet thong tin dau thau
		if (objReq.getDetail() != null) {
			List<HhDthauNthauDuthau> detailChild = new ArrayList<HhDthauNthauDuthau>();
			List<HhDthauHsoKthuat> detailChild1 = new ArrayList<HhDthauHsoKthuat>();
			List<HhDthauHsoTchinh> detailChild2 = new ArrayList<HhDthauHsoTchinh>();
			List<HhDthauTthaoHdong> detailChild3 = new ArrayList<HhDthauTthaoHdong>();
			List<HhDthauKquaLcnt> detailChild4 = new ArrayList<HhDthauKquaLcnt>();
			List<FileDKemJoinGoiThau> detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

			// Thong tin goi thau
			List<HhDthauGthauReq> dtlReqList = objReq.getDetail();
			for (HhDthauGthauReq dtlReq : dtlReqList) {
				HhDthauGthau detail = ObjectMapperUtils.map(dtlReq, HhDthauGthau.class);
				detailChild = new ArrayList<HhDthauNthauDuthau>();
				detailChild1 = new ArrayList<HhDthauHsoKthuat>();
				detailChild2 = new ArrayList<HhDthauHsoTchinh>();
				detailChild3 = new ArrayList<HhDthauTthaoHdong>();
				detailChild4 = new ArrayList<HhDthauKquaLcnt>();
				detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

				// Thong tin danh sach cac nha thau du thau
//				if (dtlReq.getChildren() != null)
//					detailChild = ObjectMapperUtils.mapAll(dtlReq.getChildren(), HhDthauNthauDuthau.class);
//
//				// Thong tin danh sach ho so ky thuat
//				if (dtlReq.getDetail1() != null) {
//					for (HhDthauHsoKthuatReq hsKthuatReq : dtlReq.getDetail1()) {
//						HhDthauHsoKthuat hsKthuat = ObjectMapperUtils.map(hsKthuatReq, HhDthauHsoKthuat.class);
//						List<FileDKemJoinHsoKthuat> fileDkHsKts = ObjectMapperUtils
//								.mapAll(hsKthuatReq.getFileDinhKems(), FileDKemJoinHsoKthuat.class);
//						fileDkHsKts.forEach(f -> {
//							f.setDataType(HhDthauHsoKthuat.TABLE_NAME);
//							f.setCreateDate(new Date());
//						});
//						hsKthuat.setChildren(fileDkHsKts);
//						detailChild1.add(hsKthuat);
//					}
//				}
//
//				// Thong tin danh sach ho so tai chinh
//				if (dtlReq.getDetail2() != null)
//					detailChild2 = ObjectMapperUtils.mapAll(dtlReq.getDetail2(), HhDthauHsoTchinh.class);
//
//				// Thong tin thuong thao hop dong
//				if (dtlReq.getDetail3() != null) {
//					for (HhDthauTthaoHdongReq hsTtHdReq : dtlReq.getDetail3()) {
//						HhDthauTthaoHdong hsTtHd = ObjectMapperUtils.map(hsTtHdReq, HhDthauTthaoHdong.class);
//						List<FileDKemJoinTthaoHdong> fileDkTtHds = ObjectMapperUtils.mapAll(hsTtHdReq.getFileDinhKems(),
//								FileDKemJoinTthaoHdong.class);
//						fileDkTtHds.forEach(f -> {
//							f.setDataType(HhDthauTthaoHdong.TABLE_NAME);
//							f.setCreateDate(new Date());
//						});
//						hsTtHd.setChildren(fileDkTtHds);
//						detailChild3.add(hsTtHd);
//					}
//				}

				// Thong tin ket qua lua chon nha thau
//				if (dtlReq.getDetail4() != null) {
//					for (HhDthauKquaLcntReq kqLcntReq : dtlReq.getDetail4()) {
//						HhDthauKquaLcnt kqLcnt = ObjectMapperUtils.map(kqLcntReq, HhDthauKquaLcnt.class);
//						List<FileDKemJoinKquaLcnt> fileDkKqLcnts = ObjectMapperUtils
//								.mapAll(kqLcntReq.getFileDinhKems(), FileDKemJoinKquaLcnt.class);
//						fileDkKqLcnts.forEach(f -> {
//							f.setDataType(HhDthauKquaLcnt.TABLE_NAME);
//							f.setCreateDate(new Date());
//						});
//						kqLcnt.setChildren(fileDkKqLcnts);
//						detailChild4.add(kqLcnt);
//					}
//				}

//				// File dinh kem cua goi thau
//				if (dtlReq.getFileDinhKems() != null) {
//					detailChild5 = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(), FileDKemJoinGoiThau.class);
//					detailChild5.forEach(f -> {
//						f.setDataType(HhDthauGthau.TABLE_NAME);
//						f.setCreateDate(new Date());
//					});
//				}
//
//				detail.setChildren(detailChild);
//				detail.setChildren1(detailChild1);
//				detail.setChildren2(detailChild2);
//				detail.setChildren3(detailChild3);
//				detail.setChildren4(detailChild4);
//				detail.setChildren5(detailChild5);
//				dataMap.addChild(detail);
			}
		}

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		return hhDthauRepository.save(dataMap);
	}

	@Override
	public Page<ThongTinDauThauRes> selectPage(HhDthauSearchReq objReq) throws Exception {
		Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit(), Sort.by("id").ascending());
		Page<ThongTinDauThauRes> page = hhDthauRepository.cusTomQuerySearch(objReq.getNamKhoach(),objReq.getLoaiVthh(),objReq.getSoQd(),objReq.getMaDvi(),objReq.getTrichYeu(),pageable);
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		page.forEach(f -> {
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
			f.setTenVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
		});

		return page;
	}

	@Override
	public List<ThongTinDauThauRes> selectAll(HhDthauSearchReq objReq) throws Exception {
		List<ThongTinDauThauRes> list = hhDthauRepository.cusTomQuerySearch(objReq.getNamKhoach(),objReq.getLoaiVthh(),objReq.getCloaiVthh(),objReq.getSoQd(),objReq.getMaDvi(),objReq.getTrangThai());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> hashMapDviLquan = getListDanhMucDviLq("NT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		list.forEach(f -> {
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
			f.setTenVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
			f.setTenNhaThau(StringUtils.isEmpty(f.getIdNhaThau()) ? null : hashMapDviLquan.get(String.valueOf(Double.parseDouble(f.getIdNhaThau().toString()))));
			f.setTenLoaiHdong(hashMapLoaiHdong.get(f.getLoaiHdong()));
		});
		return list;
	}

	@Override
	public HhDthau update(HhDthauReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhDthau> qOptional = hhDthauRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		HhDthau dataDB = qOptional.get();
		HhDthau dataMap = ObjectMapperUtils.map(objReq, HhDthau.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());

		if (objReq.getDetail() != null) {
			List<HhDthauNthauDuthau> detailChild = new ArrayList<HhDthauNthauDuthau>();
			List<HhDthauHsoKthuat> detailChild1 = new ArrayList<HhDthauHsoKthuat>();
			List<HhDthauHsoTchinh> detailChild2 = new ArrayList<HhDthauHsoTchinh>();
			List<HhDthauTthaoHdong> detailChild3 = new ArrayList<HhDthauTthaoHdong>();
			List<HhDthauKquaLcnt> detailChild4 = new ArrayList<HhDthauKquaLcnt>();
			List<FileDKemJoinGoiThau> detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

			List<HhDthauGthauReq> dtlReqList = objReq.getDetail();
			for (HhDthauGthauReq dtlReq : dtlReqList) {
				HhDthauGthau detail = ObjectMapperUtils.map(dtlReq, HhDthauGthau.class);
				detailChild = new ArrayList<HhDthauNthauDuthau>();
				detailChild1 = new ArrayList<HhDthauHsoKthuat>();
				detailChild2 = new ArrayList<HhDthauHsoTchinh>();
				detailChild3 = new ArrayList<HhDthauTthaoHdong>();
				detailChild4 = new ArrayList<HhDthauKquaLcnt>();
				detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

//				if (dtlReq.getChildren() != null)
//					detailChild = ObjectMapperUtils.mapAll(dtlReq.getChildren(), HhDthauNthauDuthau.class);
//
//				if (dtlReq.getDetail1() != null) {
//					for (HhDthauHsoKthuatReq hsKthuatReq : dtlReq.getDetail1()) {
//						HhDthauHsoKthuat hsKthuat = ObjectMapperUtils.map(hsKthuatReq, HhDthauHsoKthuat.class);
//						List<FileDKemJoinHsoKthuat> fileDkHsKts = ObjectMapperUtils
//								.mapAll(hsKthuatReq.getFileDinhKems(), FileDKemJoinHsoKthuat.class);
//						fileDkHsKts.forEach(f -> {
//							f.setDataType(HhDthauHsoKthuat.TABLE_NAME);
//							f.setCreateDate(new Date());
//						});
//						hsKthuat.setChildren(fileDkHsKts);
//						detailChild1.add(hsKthuat);
//					}
//				}
//
//				if (dtlReq.getDetail2() != null)
//					detailChild2 = ObjectMapperUtils.mapAll(dtlReq.getDetail2(), HhDthauHsoTchinh.class);
//
//				if (dtlReq.getDetail3() != null) {
//					for (HhDthauTthaoHdongReq hsTtHdReq : dtlReq.getDetail3()) {
//						HhDthauTthaoHdong hsTtHd = ObjectMapperUtils.map(hsTtHdReq, HhDthauTthaoHdong.class);
//						List<FileDKemJoinTthaoHdong> fileDkTtHds = ObjectMapperUtils.mapAll(hsTtHdReq.getFileDinhKems(),
//								FileDKemJoinTthaoHdong.class);
//						fileDkTtHds.forEach(f -> {
//							f.setDataType(HhDthauTthaoHdong.TABLE_NAME);
//							f.setCreateDate(new Date());
//						});
//						hsTtHd.setChildren(fileDkTtHds);
//						detailChild3.add(hsTtHd);
//					}
//				}

//				if (dtlReq.getDetail4() != null) {
//					for (HhDthauKquaLcntReq kqLcntReq : dtlReq.getDetail4()) {
//						HhDthauKquaLcnt kqLcnt = ObjectMapperUtils.map(kqLcntReq, HhDthauKquaLcnt.class);
//						List<FileDKemJoinKquaLcnt> fileDkKqLcnts = ObjectMapperUtils
//								.mapAll(kqLcntReq.getFileDinhKems(), FileDKemJoinKquaLcnt.class);
//						fileDkKqLcnts.forEach(f -> {
//							f.setDataType(HhDthauKquaLcnt.TABLE_NAME);
//							f.setCreateDate(new Date());
//						});
//						kqLcnt.setChildren(fileDkKqLcnts);
//						detailChild4.add(kqLcnt);
//					}
//				}

//				if (dtlReq.getFileDinhKems() != null) {
//					detailChild5 = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(), FileDKemJoinGoiThau.class);
//					detailChild5.forEach(f -> {
//						f.setDataType(HhDthauGthau.TABLE_NAME);
//						f.setCreateDate(new Date());
//					});
//				}

//				detail.setChildren(detailChild);
//				detail.setChildren1(detailChild1);
//				detail.setChildren2(detailChild2);
//				detail.setChildren3(detailChild3);
//				detail.setChildren4(detailChild4);
//				detail.setChildren5(detailChild5);
//				dataDB.addChild(detail);
			}
		}

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);

		return hhDthauRepository.save(dataDB);
	}

	@Override
	public HhDthau detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDthau> qOptional = hhDthauRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
//		List<HhDthauGthau> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhDthauGthau.class);
//		for (HhDthauGthau dtl : dtls2) {
//			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
//		}

		return qOptional.get();
	}

//	@Override
//	public Page<HhDthau2> colection(HhDthauSearchReq objReq, HttpServletRequest req) throws Exception {
//		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
//		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
//		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
//
//		Page<HhDthau2> dataPage = hhDthau2Repository.findAll(HhDthau2Specification.buildSearchQuery(objReq), pageable);
//
//		Map<String, String> mapDmucDvi = getMapTenDvi();
//		for (HhDthau2 hdr : dataPage.getContent()) {
//			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
//		}
//		return dataPage;
//	}

	@Override
	public HhDthau approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhDthau> optional = hhDthauRepository.findById(Long.valueOf(stReq.getId()));
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
		return hhDthauRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhDthau> optional = hhDthauRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

		hhDthauRepository.delete(optional.get());

	}

}
