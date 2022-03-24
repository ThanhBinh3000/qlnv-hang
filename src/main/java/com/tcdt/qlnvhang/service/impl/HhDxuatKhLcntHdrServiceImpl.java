package com.tcdt.qlnvhang.service.impl;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntCcxdg;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntDsgtDtlRepository;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntCcxdgDtlReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.service.HhDxuatKhLcntHdrService;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntCcxdgDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntGaoDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.table.QlnvDanhMuc;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.MoneyConvert;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.UnitScaler;

@Service
public class HhDxuatKhLcntHdrServiceImpl extends BaseServiceImpl implements HhDxuatKhLcntHdrService {

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@Autowired
	private HhDxuatKhLcntDsgtDtlRepository hhDxuatKhLcntDtlRepository;

	Long shgtNext = new Long(0);

	@Override
	public HhDxuatKhLcntHdr create(HhDxuatKhLcntHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
		if (qOptional.isPresent())
			throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setChildren(fileDinhKemList);

		// Add thong tin chung
		List<HhDxuatKhLcntGaoDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDxuatKhLcntGaoDtl.class);
		dataMap.setChildren1(dtls1);

		// Add danh sach goi thau
		List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(objReq.getDetail2(), HhDxuatKhLcntDsgtDtl.class);
		UnitScaler.reverseFormatList(dtls2, Contains.DVT_TAN);
		String prefix = "-" + Contains.SHGT + "/" + dataMap.getMaDvi();
		// TODO: xem lai cach sinh so, viet tam de lay so
		// Lay danh muc dung chung
		String shgtStr = "0";
		QlnvDanhMuc qlnvDanhMuc = danhMucRepository.findByMa(Contains.SHGT);
		if (qlnvDanhMuc != null)
			shgtStr = qlnvDanhMuc.getGiaTri();
		Long shgt = Long.parseLong(shgtStr);
		dtls2.forEach(h -> {
			h.setShgt(String.format("%07d", shgt) + prefix);
			shgtNext = Long.sum(shgt, 1);
		});

		danhMucRepository.updateVal(Contains.SHGT, shgtNext);

		dataMap.setChildren2(dtls2);
		// Add danh sach can cu xac dinh gia
		if (objReq.getDetail3() != null) {
			List<FileDKemJoinDxKhLcntCcxdg> detailChild;
			List<HhDxuatKhLcntCcxdgDtlReq> dtlReqList = objReq.getDetail3();
			for (HhDxuatKhLcntCcxdgDtlReq dtlReq : dtlReqList) {
				HhDxuatKhLcntCcxdgDtl detail = ObjectMapperUtils.map(dtlReq, HhDxuatKhLcntCcxdgDtl.class);
				detailChild = new ArrayList<FileDKemJoinDxKhLcntCcxdg>();
				if (dtlReq.getFileDinhKems() != null) {
					detailChild = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(), FileDKemJoinDxKhLcntCcxdg.class);
					detailChild.forEach(f -> {
						f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
						f.setCreateDate(new Date());
					});
				}
				detail.setChildren(detailChild);
				dataMap.addChild3(detail);
			}
		}

		return hhDxuatKhLcntHdrRepository.save(dataMap);

	}

	@Override
	public HhDxuatKhLcntHdr update(HhDxuatKhLcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKems() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDxuatKhLcntHdr dataDTB = qOptional.get();
		HhDxuatKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhLcntHdr.class);

		updateObjectToObject(dataDTB, dataMap);

		dataDTB.setNgaySua(getDateTimeNow());
		dataDTB.setNguoiSua(getUser().getUsername());
		dataDTB.setChildren(fileDinhKemList);

		// Add thong tin chung
		List<HhDxuatKhLcntGaoDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDxuatKhLcntGaoDtl.class);
		dataDTB.setChildren1(dtls1);
		// Add danh sach goi thau
		List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(objReq.getDetail2(), HhDxuatKhLcntDsgtDtl.class);
		UnitScaler.reverseFormatList(dtls2, Contains.DVT_TAN);
		dataDTB.setChildren2(dtls2);
		// Add danh sach can cu xac dinh gia
		if (objReq.getDetail3() != null) {
			List<FileDKemJoinDxKhLcntCcxdg> detailChild;
			List<HhDxuatKhLcntCcxdgDtlReq> dtlReqList = objReq.getDetail3();
			for (HhDxuatKhLcntCcxdgDtlReq dtlReq : dtlReqList) {
				HhDxuatKhLcntCcxdgDtl detail = ObjectMapperUtils.map(dtlReq, HhDxuatKhLcntCcxdgDtl.class);
				detailChild = new ArrayList<FileDKemJoinDxKhLcntCcxdg>();
				if (dtlReq.getFileDinhKems() != null) {
					detailChild = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(), FileDKemJoinDxKhLcntCcxdg.class);
					detailChild.forEach(f -> {
						f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
						f.setCreateDate(new Date());
					});
				}
				detail.setChildren(detailChild);
				dataDTB.addChild3(detail);
			}
		}

		return hhDxuatKhLcntHdrRepository.save(dataDTB);

	}

	@Override
	public HhDxuatKhLcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
		List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren2(),
				HhDxuatKhLcntDsgtDtl.class);
		UnitScaler.formatList(dtls2, Contains.DVT_TAN);
		qOptional.get().setChildren2(dtls2);

		return qOptional.get();
	}

	@Override
	public Page<HhDxuatKhLcntHdr> colection(HhDxuatKhLcntSearchReq objReq, HttpServletRequest req) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit);

		Page<HhDxuatKhLcntHdr> qhKho = hhDxuatKhLcntHdrRepository
				.findAll(HhDxuatKhLcntSpecification.buildSearchQuery(objReq), pageable);

		for (HhDxuatKhLcntHdr hdr : qhKho.getContent()) {
			hdr.setTenDvi(getDviByMa(hdr.getMaDvi(), req).getTenDvi());
		}

		return qhKho;
	}

	@Override
	public HhDxuatKhLcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(stReq.getId()));
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
		return hhDxuatKhLcntHdrRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(idSearchReq.getId());

		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI))
			throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp");

		hhDxuatKhLcntHdrRepository.delete(optional.get());
	}

	@Override
	public void exportToExcel(IdSearchReq searchReq, HttpServletResponse response) throws Exception {
		// Tao form excel
		String title = "Danh sách gói thầu";
		String[] rowsName = new String[] { "STT", "Gói thầu", "Số lượng (tấn)", "Địa điểm nhập kho",
				"Đơn giá (đồng/kg)", "Thành tiền (đồng)", "Bằng chữ" };
		List<HhDxuatKhLcntDsgtDtl> dsgtDtls = hhDxuatKhLcntDtlRepository.findByIdHdr(searchReq.getId());

		if (dsgtDtls.isEmpty())
			throw new UnsupportedOperationException("Không tìm thấy dữ liệu");

		String filename = "Dexuat_Danhsachgoithau_" + dsgtDtls.get(0).getParent().getSoDxuat() + ".xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < dsgtDtls.size(); i++) {
			HhDxuatKhLcntDsgtDtl dsgtDtl = dsgtDtls.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dsgtDtl.getGoiThau();
			objs[2] = dsgtDtl.getSoLuong().multiply(Contains.getDVTinh(Contains.DVT_KG))
					.divide(Contains.getDVTinh(Contains.DVT_TAN)).setScale(0, RoundingMode.HALF_UP);
			objs[3] = dsgtDtl.getDiaDiemNhap();
			objs[4] = dsgtDtl.getDonGia();
			objs[5] = dsgtDtl.getThanhTien();
			objs[6] = MoneyConvert.doctienBangChu(dsgtDtl.getThanhTien().toString(), "");
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

}
