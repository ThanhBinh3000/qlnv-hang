package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.request.object.DsChiCucPreview;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntThopPreview;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class XhBbLayMauServiceImpl extends BaseServiceImpl implements XhBbLayMauService {

	@Autowired
	private  XhBbLayMauRepository xhBbLayMauRepository;

	@Autowired
	private  XhBbLayMauCtRepository xhBbLayMauCtRepository;

	@Autowired
    FileDinhKemService fileDinhKemService;

	@Autowired
	UserInfoRepository userInfoRepository;

	@Override
	public Page<XhBbLayMau> searchPage(XhBbLayMauRequest req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
				req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<XhBbLayMau> data = xhBbLayMauRepository.searchPage(
				req,
				pageable
		);
		Map<String, String> hashMapVthh = getListDanhMucHangHoa();
		Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
		data.getContent().forEach(f->{
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
			f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDvi.get(f.getMaDiemKho()));
			f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDvi.get(f.getMaNhaKho()));
			f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDvi.get(f.getMaNganKho()));
			f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDvi.get(f.getMaLoKho()));
			f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
		});
		return data;
	}

	@Override
	public XhBbLayMau create(XhBbLayMauRequest req) throws Exception {

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

//		if (!StringUtils.isEmpty(req.getSoBienBan())){
//			Optional<XhBbLayMau> qOptional = xhBbLayMauRepository.findBySoBienBan(req.getSoBienBan());
//			if (qOptional.isPresent()){
//				throw new Exception("Số biên bản " + req.getSoBienBan() + " đã tồn tại ");
//			}
//		}

		XhBbLayMau data = new XhBbLayMau();
		BeanUtils.copyProperties(req,data,"id");

		data.setNguoiTaoId(userInfo.getId());
		data.setNgayTao(new Date());
		data.setTrangThai(Contains.DUTHAO);
		data.setMaDvi(userInfo.getDvql());
		data.setId(Long.parseLong(data.getSoBienBan().split("/")[0]));
		data.setIdKtv(userInfo.getId());
		XhBbLayMau	created = xhBbLayMauRepository.save(data);

		if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
			List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbLayMau.TABLE_NAME);
			created.setFileDinhKems(fileDinhKems);
		}

		if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
			List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhBbLayMau.TABLE_NAME + "_CAN_CU");
			created.setCanCuPhapLy(canCuPhapLy);
		}

		if (!DataUtils.isNullOrEmpty(req.getFileNiemPhong())) {
			List<FileDinhKem> fileNienPhong = fileDinhKemService.saveListFileDinhKem(req.getFileNiemPhong(), created.getId(), XhBbLayMau.TABLE_NAME + "_NIEM_PHONG");
			created.setFileNiemPhong(fileNienPhong);
		}

		saveDetail(req, data.getId());
		return created;
	}

	void saveDetail(XhBbLayMauRequest req,Long idHdr){
		xhBbLayMauCtRepository.deleteAllByIdHdr(idHdr);
		for (XhBbLayMauCtRequest ctReq :req.getChildren()) {
			XhBbLayMauCt ct = new XhBbLayMauCt();
			BeanUtils.copyProperties(ctReq, ct,"id");
			ct.setIdHdr(idHdr);
			xhBbLayMauCtRepository.save(ct);
		}
	}

	@Override
	public XhBbLayMau update(XhBbLayMauRequest req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		if (StringUtils.isEmpty(req.getId())){
			throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
		}


		Optional<XhBbLayMau> qOptional = xhBbLayMauRepository.findById(req.getId());
		if(!qOptional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần sửa");
		}
		XhBbLayMau  dataDB = qOptional.get();
		BeanUtils.copyProperties(req, dataDB, "id");
		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSuaId(userInfo.getId());
		XhBbLayMau created = xhBbLayMauRepository.save(dataDB);

		fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhBbLayMau.TABLE_NAME));
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbLayMau.TABLE_NAME);
		created.setFileDinhKems(fileDinhKems);

		fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhBbLayMau.TABLE_NAME+ "_CAN_CU"));
		List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhBbLayMau.TABLE_NAME + "_CAN_CU");
		created.setCanCuPhapLy(canCuPhapLy);

		fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhBbLayMau.TABLE_NAME+ "_NIEM_PHONG"));
		List<FileDinhKem> fileNiemPhong = fileDinhKemService.saveListFileDinhKem(req.getFileNiemPhong(), created.getId(), XhBbLayMau.TABLE_NAME + "_NIEM_PHONG");
		created.setFileNiemPhong(fileNiemPhong);


		this.saveDetail(req, dataDB.getId());
		return created;
	}

	@Override
	public XhBbLayMau detail(Long id) throws Exception {
		if (StringUtils.isEmpty(id))
			throw new Exception("Không tồn tại bản ghi");


		Optional<XhBbLayMau> qOptional = xhBbLayMauRepository.findById(id);
		if(!qOptional.isPresent()){
			throw new UnsupportedOperationException("Bản ghi không tồn tại");
		}

		Map<String, String> hashMapVthh = getListDanhMucHangHoa();
		Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

		XhBbLayMau data = qOptional.get();

		List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbLayMau.TABLE_NAME));
		data.setFileDinhKems(fileDinhKem);

		List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbLayMau.TABLE_NAME + "_CAN_CU"));
		data.setCanCuPhapLy(canCuPhapLy);

		List<FileDinhKem> fileNiemPhong = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbLayMau.TABLE_NAME + "_NIEM_PHONG"));
		data.setFileNiemPhong(fileNiemPhong);


		data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
		data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
		data.setMaDviCha(data.getMaDvi().substring(0,data.getMaDvi().length()-2));
		data.setTenDviCha(hashMapDvi.get(data.getMaDviCha()));
		data.setTenDiemKho(hashMapDvi.get(data.getMaDiemKho()));
		data.setTenNhaKho(hashMapDvi.get(data.getMaNhaKho()));
		data.setTenNganKho(hashMapDvi.get(data.getMaNganKho()));
		data.setTenLoKho(hashMapDvi.get(data.getMaLoKho()));
		data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
		if(!Objects.isNull(data.getIdKtv())){
			data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
		}
		if(!Objects.isNull(data.getNguoiPduyetId())){
			data.setTenNguoiPduyet(userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
		}
		Map<String,String> hashMapPpLayMau = getListDanhMucChung("PP_LAY_MAU");
		data.setTenPpLayMau(hashMapPpLayMau.getOrDefault(data.getPpLayMau(),null));

		data.setChildren(xhBbLayMauCtRepository.findAllByIdHdr(id));
		return data;
	}

	@Override
	public XhBbLayMau approve(XhBbLayMauRequest req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();


		if(Objects.isNull(req.getId())){
			throw new Exception("Bad reqeust");
		}

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
			throw new Exception("Bad Request");
		}

		Optional<XhBbLayMau> byId = xhBbLayMauRepository.findById(req.getId());
		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		XhBbLayMau data = byId.get();
		String status = req.getTrangThai() + data.getTrangThai();
		switch (status) {
			case Contains.CHODUYET_LDCC + Contains.DUTHAO:
			case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
				data.setNguoiGuiDuyetId(userInfo.getId());
				data.setNgayGuiDuyet(new Date());
				break;
			case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
				data.setNguoiPduyetId(userInfo.getId());
				data.setNgayPduyet(new Date());
				data.setLyDoTuChoi(req.getLyDoTuChoi());
				break;
			case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
				data.setNguoiPduyetId(userInfo.getId());
				data.setNgayPduyet(new Date());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		data.setTrangThai(req.getTrangThai());
		xhBbLayMauRepository.save(data);
		return data;
	}

	@Override
	public void delete(Long id) throws Exception {
		if(Objects.isNull(id)){
			throw new Exception("Bad request");
		}

		Optional<XhBbLayMau> optional = xhBbLayMauRepository.findById(id);
		if(!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		xhBbLayMauRepository.delete(optional.get());
		xhBbLayMauCtRepository.deleteAllByIdHdr(optional.get().getId());
		fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbLayMau.TABLE_NAME));
		fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbLayMau.TABLE_NAME + "_CAN_CU"));
		fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbLayMau.TABLE_NAME + "_NIEM_PHONG"));

	}

	@Override
	public void deleteMulti(List<Long> listMulti) throws Exception {
		if (StringUtils.isEmpty(listMulti)) {
			throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
		}

		List<XhBbLayMau> list = xhBbLayMauRepository.findByIdIn(listMulti);
		if (list.isEmpty()){
			throw new Exception("Không tìm thấy dữ liệu cần xóa");
		}

		for (XhBbLayMau xhBbLayMau : list){
			this.delete(xhBbLayMau.getId());
		}
	}

	@Override
	public void export(XhBbLayMauRequest req, HttpServletResponse response) throws Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		req.setPaggingReq(paggingReq);
		Page<XhBbLayMau> page = this.searchPage(req);
		List<XhBbLayMau> data = page.getContent();
		String title="Danh sách biên bản lấy mẫu/bàn giao mẫu ";
		String[] rowsName = new String[]{"STT","Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điển kho", "Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho", "Ngày xuất dốc kho", "sô BB hao dôi", "Trạng thái"};
		String filename="danh-sach-biển-ban-lay-mau-ban-giao-mau.xlsx";
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs=null;
		for (int i = 0; i < data.size(); i++) {
			XhBbLayMau hdr = data.get(i);
			objs=new Object[rowsName.length];
			objs[0]=i;
			objs[1]=hdr.getSoQd();
			objs[2]=hdr.getNam();
			objs[3]=hdr.getNgayQd();
			objs[4]=hdr.getTenDiemKho();
			objs[5]=hdr.getTenLoKho();
			objs[6]=hdr.getSoBienBan();
			objs[7]=hdr.getNgayLayMau();
			objs[8]=hdr.getSoBbTinhKho();
			objs[9]=hdr.getNgayXuatDocKho();
			objs[10]=hdr.getSoBbHaoDoi();
			objs[11]=hdr.getTenTrangThai();
			dataList.add(objs);
		}
		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public ReportTemplateResponse preview(XhBbLayMauRequest objReq) throws Exception {
		XhBbLayMau optional = detail(objReq.getId());
		ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
	}
}
