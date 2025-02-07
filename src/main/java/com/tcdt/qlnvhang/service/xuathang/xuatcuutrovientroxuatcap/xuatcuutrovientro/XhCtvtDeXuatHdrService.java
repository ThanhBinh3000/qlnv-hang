package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdPdHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatPaRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.xuatcuutrovientro.XhCtvtDeXuatHdrPreview;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtDeXuatHdrReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdrReq;
import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhCtvtDeXuatHdrDto;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatPa;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.util.Contains.CAP_CUC;

@Service
public class XhCtvtDeXuatHdrService extends BaseServiceImpl {


    @Autowired
    private XhCtvtDeXuatHdrRepository xhCtvtDeXuatHdrRepository;
    @Autowired
    private XhCtvtTongHopHdrRepository xhCtvtTongHopHdrRepository;
    @Autowired
    private XhCtvtQdPdHdrRepository xhCtvtQdPdHdrRepository;
    @Autowired
    private XhCtvtDeXuatPaRepository xhCtvtDeXuatPaRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhCtvtDeXuatHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtDeXuatHdrReq req) throws Exception {
//    req.setDvql(currentUser.getDvql());
        if (currentUser.getUser().getCapDvi().equals(CAP_CUC)) {

            req.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhCtvtDeXuatHdr> search = xhCtvtDeXuatHdrRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        //lay thong tin tong hop va quyet dinh
        List<Long> listIdTh = search.getContent().stream().map(s -> s.getIdThop()).collect(Collectors.toList());
        List<XhCtvtTongHopHdr> dataIdTh = xhCtvtTongHopHdrRepository.findByIdIn(listIdTh);
        Map<Long, String> trangThaiTh = dataIdTh.stream().collect(Collectors.toMap(XhCtvtTongHopHdr::getId, XhCtvtTongHopHdr::getTrangThai));

        List<Long> listIdQd = search.getContent().stream().map(s -> s.getIdQdPd()).collect(Collectors.toList());
        List<XhCtvtQuyetDinhPdHdr> dataIdQd = xhCtvtQdPdHdrRepository.findByIdIn(listIdQd);
        Map<Long, String> trangThaiQd = dataIdQd.stream().collect(Collectors.toMap(XhCtvtQuyetDinhPdHdr::getId, XhCtvtQuyetDinhPdHdr::getTrangThai));

        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            if (DataUtils.isNullObject(s.getIdThop())) {
                s.setTenTrangThaiTh("Chưa tổng hợp");
            } else {
                s.setTenTrangThaiTh(TrangThaiAllEnum.getLabelById(trangThaiTh.get(s.getIdThop())));
            }
            if (!DataUtils.isNullObject(s.getIdQdPd())) {
                s.setTenTrangThaiQd(TrangThaiAllEnum.getLabelById(trangThaiQd.get(s.getIdQdPd())));
            }
        });
        return search;
    }

    @Transactional
    public XhCtvtDeXuatHdr save(CustomUserDetails currentUser, XhCtvtDeXuatHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (DataUtils.safeToString(objReq.getSoDx()).split("/").length != 1) {
            Optional<XhCtvtDeXuatHdr> optional = xhCtvtDeXuatHdrRepository.findFirstBySoDx(objReq.getSoDx());
            if (optional.isPresent()) {
                throw new Exception("số đề xuất đã tồn tại");
            }
        }
        double tongSoLuongDeXuat = objReq.getDeXuatPhuongAn().stream().map(item -> item.getSoLuong() != null ? item.getSoLuong() : new BigDecimal(0l)).mapToDouble(BigDecimal::doubleValue).sum();
        double tongNhuCapXuat = objReq.getDeXuatPhuongAn().stream().map(item -> item.getSoLuongNhuCauXuat() != null ? item.getSoLuongNhuCauXuat() : new BigDecimal(0l)).mapToDouble(BigDecimal::doubleValue).sum();
        double tongSoLuongXuatCap = objReq.getDeXuatPhuongAn().stream().map(item -> item.getSoLuongChuyenCapThoc() != null ? item.getSoLuongChuyenCapThoc() : new BigDecimal(0l)).mapToDouble(BigDecimal::doubleValue).sum();
        XhCtvtDeXuatHdr data = new XhCtvtDeXuatHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setTongSoLuongDeXuat(new BigDecimal(tongSoLuongDeXuat));
        if(tongNhuCapXuat > tongSoLuongDeXuat){
            if(tongSoLuongXuatCap == 0){
                tongSoLuongXuatCap = tongNhuCapXuat- tongSoLuongDeXuat;
            }
        }
        data.setTongSoLuongXuatCap(new BigDecimal(tongSoLuongXuatCap));
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
        XhCtvtDeXuatHdr created = xhCtvtDeXuatHdrRepository.save(data);
        xhCtvtDeXuatHdrRepository.save(created);
        return created;
    }

    @Transactional
    public XhCtvtDeXuatHdr update(CustomUserDetails currentUser, XhCtvtDeXuatHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhCtvtDeXuatHdr> optional = xhCtvtDeXuatHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (DataUtils.safeToString(objReq.getSoDx()).split("/").length != 1) {
            Optional<XhCtvtDeXuatHdr> soDx = xhCtvtDeXuatHdrRepository.findFirstBySoDx(objReq.getSoDx());
            if (soDx.isPresent()) {
                if (!soDx.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số đề xuất đã tồn tại");
                }
            }
        }
        double tongSoLuongDeXuat = objReq.getDeXuatPhuongAn().stream().map(XhCtvtDeXuatPa::getSoLuong).mapToDouble(BigDecimal::doubleValue).sum();
        double tongSoLuongXuatCap = objReq.getDeXuatPhuongAn().stream().map(XhCtvtDeXuatPa::getSoLuongChuyenCapThoc).mapToDouble(BigDecimal::doubleValue).sum();
        XhCtvtDeXuatHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data, "maDvi");
        data.setTongSoLuongDeXuat(new BigDecimal(tongSoLuongDeXuat));
        data.setTongSoLuongXuatCap(new BigDecimal(tongSoLuongXuatCap));
        XhCtvtDeXuatHdr created = xhCtvtDeXuatHdrRepository.save(data);
        return created;
    }


    public List<XhCtvtDeXuatHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhCtvtDeXuatHdr> optional = xhCtvtDeXuatHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhCtvtDeXuatHdr> allById = xhCtvtDeXuatHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.getDeXuatPhuongAn().forEach(s -> {
                s.setMapDmucDvi(mapDmucDvi);
                s.setMapVthh(mapVthh);
            });
        });

        return allById;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhCtvtDeXuatHdr> optional = xhCtvtDeXuatHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhCtvtDeXuatHdr data = optional.get();
        List<XhCtvtDeXuatPa> list = xhCtvtDeXuatPaRepository.findByXhCtvtDeXuatHdrId(data.getId());
        xhCtvtDeXuatPaRepository.deleteAll(list);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtDeXuatHdr.TABLE_NAME + "_CAN_CU"));
        xhCtvtDeXuatHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhCtvtDeXuatHdr> list = xhCtvtDeXuatHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(XhCtvtDeXuatHdr::getId).collect(Collectors.toList());
        List<XhCtvtDeXuatPa> listPhuongAn = xhCtvtDeXuatPaRepository.findAllByXhCtvtDeXuatHdrIdIn(listId);
        xhCtvtDeXuatPaRepository.deleteAll(listPhuongAn);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtDeXuatHdr.TABLE_NAME + "_CAN_CU"));
        xhCtvtDeXuatHdrRepository.deleteAll(list);
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        String capDvi = (currentUser.getUser().getCapDvi());
        if (Contains.CAP_TONG_CUC.equals(capDvi)) {
            this.approveTongCuc(currentUser, statusReq);
        } else {
            this.approveCuc(currentUser, statusReq);
        }
    }

    public XhCtvtDeXuatHdr approveTongCuc(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhCtvtDeXuatHdr> optional = xhCtvtDeXuatHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.DA_TAO_CBV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.DA_TAO_CBV + Contains.DUTHAO:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhCtvtDeXuatHdr created = xhCtvtDeXuatHdrRepository.save(optional.get());
        return created;
    }

    public XhCtvtDeXuatHdr approveCuc(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhCtvtDeXuatHdr> optional = xhCtvtDeXuatHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                optional.get().setNgayGduyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhCtvtDeXuatHdr created = xhCtvtDeXuatHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchXhCtvtDeXuatHdrReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhCtvtDeXuatHdr> page = this.searchPage(currentUser, objReq);
        List<XhCtvtDeXuatHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Loại hình nhập xuất", "Số công văn/đề xuất", "Đơn vị đề xuất", "Ngày đề xuất", "Ngày duyệt đề xuất", "Loại hàng hóa", "Tổng SL xuất CT,VT (kg)", "Trích yếu", "Trang thái đề xuât", "Trạng thái/Mã tổng hợp",};
        String fileName = "danh-sach-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhCtvtDeXuatHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getLoaiNhapXuat();
            objs[3] = dx.getSoDx();
            objs[4] = dx.getTenDvi();
            objs[5] = dx.getNgayDx();
            objs[6] = dx.getNgayPduyet();
            objs[7] = dx.getTenLoaiVthh();
            objs[8] = dx.getTongSoLuong();
            objs[9] = dx.getTrichYeu();
            objs[10] = dx.getTenTrangThai();
            objs[11] = dx.getMaTongHop();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(XhCtvtDeXuatHdrReq objReq) throws Exception {
        var xhCtvtDeXuatHdr = xhCtvtDeXuatHdrRepository.findById(objReq.getId());
        var fileTemplate = "";
        var checkTypeVT = false;
        if (!xhCtvtDeXuatHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        if (StringUtils.isEmpty(xhCtvtDeXuatHdr.get().getTenVthh())) throw new Exception("Không tồn tại loại vật tư hàng hoá");
        if (!StringUtils.isEmpty(xhCtvtDeXuatHdr.get().getTenVthh())) {
            if (xhCtvtDeXuatHdr.get().getTenVthh().equals("Vật tư thiết bị")) {
                fileTemplate = "xuatcuutrovientro/" + "Đề xuất PA Cứu trợ-Vụ tạo-Vật tư-Cấp Tổng cục.docx";
                checkTypeVT = true;
            } else {
                fileTemplate = "xuatcuutrovientro/" + "Đề xuất PA Cứu trợ-Vụ tạo-Lương thực-Cấp Tổng cục.docx";
            }
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        xhCtvtDeXuatHdr.get().getDeXuatPhuongAn().forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            });
        FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
        var xhCtvtDeXuatHdrPreview = setDataToPreview(xhCtvtDeXuatHdr, checkTypeVT);
        return docxToPdfConverter.convertDocxToPdf(inputStream, xhCtvtDeXuatHdrPreview);
    }

    private XhCtvtDeXuatHdrPreview setDataToPreview(Optional<XhCtvtDeXuatHdr> xhCtvtDeXuatHdr, Boolean checkTypeVT) throws Exception {
        var xhCtvtDeXuatHdrPreview = new XhCtvtDeXuatHdrPreview();
        xhCtvtDeXuatHdrPreview.setChungLoaiHangHoa(xhCtvtDeXuatHdr.get().getCloaiVthh());
        xhCtvtDeXuatHdrPreview.setNamKeHoach(xhCtvtDeXuatHdr.get().getNam());
        xhCtvtDeXuatHdrPreview.setTongSoLuongXuatCap(String.valueOf(xhCtvtDeXuatHdr.get().getTongSoLuongDeXuat()));
        if (checkTypeVT) {
            xhCtvtDeXuatHdrPreview.setXhCtvtDeXuatHdrDto(convertXhCtvtDeXuatHdrToDtoVT(xhCtvtDeXuatHdr));
        } else {
            xhCtvtDeXuatHdrPreview.setXhCtvtDeXuatHdrDto(convertXhCtvtDeXuatHdrToDtoLT(xhCtvtDeXuatHdr));
        }
        return xhCtvtDeXuatHdrPreview;
    }
    private List<XhCtvtDeXuatHdrDto> convertXhCtvtDeXuatHdrToDtoVT(Optional<XhCtvtDeXuatHdr> xhCtvtDeXuatHdr) {
        List<XhCtvtDeXuatHdrDto> xhCtvtDeXuatHdrDtos = new ArrayList<>();
        Map<String, List<XhCtvtDeXuatPa>> xhCtvtDeXuatPas = xhCtvtDeXuatHdr.get().getDeXuatPhuongAn()
                .stream().collect(Collectors.groupingBy(XhCtvtDeXuatPa::getNoiDung));
        int stt = 1;
        for (var key : xhCtvtDeXuatPas.entrySet()) {
            var xhCtvtDeXuatHdrDtoLvOne = new XhCtvtDeXuatHdrDto();
            xhCtvtDeXuatHdrDtoLvOne.setStt(String.valueOf(stt++));
            xhCtvtDeXuatHdrDtoLvOne.setDonViNhanCuuTro(key.getKey());
            xhCtvtDeXuatHdrDtoLvOne.setMucDichXuat(xhCtvtDeXuatHdr.get().getMucDichXuat());
            xhCtvtDeXuatHdrDtoLvOne.setNgayKetThuc(Contains.convertDateToString(xhCtvtDeXuatHdr.get().getNgayKetThuc()));
            xhCtvtDeXuatHdrDtos.add(xhCtvtDeXuatHdrDtoLvOne);
            convertXhCtvtDeXuatHdrToDtoLvTwo(xhCtvtDeXuatHdrDtos, key);
        }
        return xhCtvtDeXuatHdrDtos;
    }

    private List<XhCtvtDeXuatHdrDto> convertXhCtvtDeXuatHdrToDtoLT(Optional<XhCtvtDeXuatHdr> xhCtvtDeXuatHdr) {
        List<XhCtvtDeXuatHdrDto> xhCtvtDeXuatHdrDtos = new ArrayList<>();
        var xhCtvtDeXuatPas = xhCtvtDeXuatHdr.get().getDeXuatPhuongAn()
                .stream().collect(Collectors.groupingBy(XhCtvtDeXuatPa::getNoiDung));
        int stt = 1;
        for (var key : xhCtvtDeXuatPas.entrySet()) {
            var xhCtvtDeXuatHdrDto = new XhCtvtDeXuatHdrDto();
            xhCtvtDeXuatHdrDto.setStt(String.valueOf(stt++));
            xhCtvtDeXuatHdrDto.setDonViNhanCuuTro(key.getKey());
            xhCtvtDeXuatHdrDto.setMucDichXuat(xhCtvtDeXuatHdr.get().getMucDichXuat());
            xhCtvtDeXuatHdrDto.setNgayKetThuc(Contains.convertDateToString(xhCtvtDeXuatHdr.get().getNgayKetThuc()));
            xhCtvtDeXuatHdrDtos.add(xhCtvtDeXuatHdrDto);
                for (var valueLt : key.getValue()) {
                    var xhCtvtDeXuatPaDto = new  XhCtvtDeXuatHdrDto();
                    xhCtvtDeXuatPaDto.setTenDvi(valueLt.getTenDvi());
                    xhCtvtDeXuatPaDto.setSoLuong(String.valueOf(valueLt.getSoLuong()));
                    xhCtvtDeXuatHdrDtos.add(xhCtvtDeXuatPaDto);
                }
            }
        return xhCtvtDeXuatHdrDtos;
    }

    private void convertXhCtvtDeXuatHdrToDtoLvTwo(List<XhCtvtDeXuatHdrDto> xhCtvtDeXuatHdrDtos, Map.Entry<String, List<XhCtvtDeXuatPa>> key) {
        var xhCtvtDeXuatPaVt = key.getValue()
                .stream().collect(Collectors.groupingBy(XhCtvtDeXuatPa::getLoaiVthh));
        for (var res : xhCtvtDeXuatPaVt.entrySet()) {
            var xhCtvtDeXuatPaDtoLvTwo = new  XhCtvtDeXuatHdrDto();
            xhCtvtDeXuatPaDtoLvTwo.setLoaiVthh(res.getValue().stream().findAny().get().getTenLoaiVthh());
            xhCtvtDeXuatPaDtoLvTwo.setSoLuong(String.valueOf(res.getValue()
                    .stream().map(XhCtvtDeXuatPa::getSoLuong).reduce(BigDecimal.ZERO, BigDecimal::add)));
            xhCtvtDeXuatPaDtoLvTwo.setDonViTinh(res.getValue().stream().findAny().get().getDonViTinh());
            xhCtvtDeXuatHdrDtos.add(xhCtvtDeXuatPaDtoLvTwo);
            convertXhCtvtDeXuatHdrToDtoLvThree(xhCtvtDeXuatHdrDtos, res);
        }
    }

    private void convertXhCtvtDeXuatHdrToDtoLvThree(List<XhCtvtDeXuatHdrDto> xhCtvtDeXuatHdrDtos, Map.Entry<String, List<XhCtvtDeXuatPa>> res) {
        for (var value : res.getValue()) {
            XhCtvtDeXuatHdrDto xhCtvtDeXuatPaDtoLvThree = new XhCtvtDeXuatHdrDto();
            xhCtvtDeXuatPaDtoLvThree.setCloaiVthh(value.getTenCloaiVthh());
            xhCtvtDeXuatPaDtoLvThree.setTenDvi(value.getTenDvi());
            xhCtvtDeXuatPaDtoLvThree.setSoLuong(String.valueOf(value.getSoLuong()));
            xhCtvtDeXuatPaDtoLvThree.setDonViTinh(value.getDonViTinh());
            xhCtvtDeXuatHdrDtos.add(xhCtvtDeXuatPaDtoLvThree);
        }
    }
}
