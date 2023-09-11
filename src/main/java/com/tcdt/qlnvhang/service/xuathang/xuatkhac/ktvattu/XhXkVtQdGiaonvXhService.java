package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRequest;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatCuc;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatHangDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkVtQdGiaonvXhService extends BaseServiceImpl {
    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtQdGiaonvXhHdr> searchPage(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (ObjectUtils.isEmpty(req.getDvql())) {
            req.setDvql(currentUser.getDvql());
        }
        Page<XhXkVtQdGiaonvXhHdr> search = xhXkVtQdGiaonvXhRepository.searchPage(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setTenLoai(Contains.getLoaiHinhXuat(s.getLoai()));
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
            s.setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(s.getTrangThaiXh()));
            s.getXhXkVtQdGiaonvXhDtl().forEach(item -> {
                item.setMapVthh(mapVthh);
                item.setMapDmucDvi(mapDmucDvi);
            });
        });
        return search;
    }

    @Transactional
    public XhXkVtQdGiaonvXhHdr save(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(objReq.getSoQuyetDinh())) {
            Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
            if (optional.isPresent()) {
                throw new Exception("Số quyết định đã tồn tại");
            }
        }
        XhXkVtQdGiaonvXhHdr data = new XhXkVtQdGiaonvXhHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        data.getXhXkVtQdGiaonvXhDtl().forEach(s -> {
            s.setXhXkVtQdGiaonvXhHdr(data);
            s.setId(null);
        });
        XhXkVtQdGiaonvXhHdr created = xhXkVtQdGiaonvXhRepository.save(data);
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtQdGiaonvXhHdr.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtQdGiaonvXhHdr update(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoQuyetDinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoQuyetDinh().split("/")[0])) {
            Optional<XhXkVtQdGiaonvXhHdr> optionalBySoTt = xhXkVtQdGiaonvXhRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
            if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
                if (!optionalBySoTt.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
            }
        }
        XhXkVtQdGiaonvXhHdr dx = optional.get();
        dx.getXhXkVtQdGiaonvXhDtl().forEach(e -> e.setXhXkVtQdGiaonvXhHdr(null));
        BeanUtils.copyProperties(objReq, dx);
        dx.getXhXkVtQdGiaonvXhDtl().forEach(e -> e.setXhXkVtQdGiaonvXhHdr(dx));
        dx.setXhXkVtQdGiaonvXhDtl(objReq.getXhXkVtQdGiaonvXhDtl());
        XhXkVtQdGiaonvXhHdr created = xhXkVtQdGiaonvXhRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtQdGiaonvXhHdr.TABLE_NAME);
        return detail(created.getId());
    }

    @Transactional()
    public XhXkVtQdGiaonvXhHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtQdGiaonvXhHdr model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkKhXuatHang.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.getXhXkVtQdGiaonvXhDtl().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
        });
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }


    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkVtQdGiaonvXhHdr data = optional.get();
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
        xhXkVtQdGiaonvXhRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkVtQdGiaonvXhHdr> list = xhXkVtQdGiaonvXhRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
        xhXkVtQdGiaonvXhRepository.deleteAll(list);
    }


    public XhXkVtQdGiaonvXhHdr pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
        Optional<XhXkVtQdGiaonvXhHdr> dx = xhXkVtQdGiaonvXhRepository.findById(req.getId());
        if (!dx.isPresent()) {
            throw new Exception("Không tồn tại bản ghi");
        }
        XhXkVtQdGiaonvXhHdr xhXkVtQdGiaonvXhHdr = dx.get();
        String status = xhXkVtQdGiaonvXhHdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.DU_THAO + Contains.CHO_DUYET_LDC:
            case Contains.TU_CHOI_LDC + Contains.CHO_DUYET_LDC:
                break;
            case Contains.CHO_DUYET_LDC + Contains.TU_CHOI_LDC:
                xhXkVtQdGiaonvXhHdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
                xhXkVtQdGiaonvXhHdr.setNguoiDuyetId(currentUser.getUser().getId());
                xhXkVtQdGiaonvXhHdr.setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công.");
        }
        xhXkVtQdGiaonvXhHdr.setTrangThai(req.getTrangThai());
        XhXkVtQdGiaonvXhHdr model = xhXkVtQdGiaonvXhRepository.save(xhXkVtQdGiaonvXhHdr);
        return detail(model.getId());
    }


    public void export(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        List<XhXkVtQdGiaonvXhHdr> data = this.searchPage(currentUser, objReq).getContent();
        String title, fileName = "";
        String[] rowsName;
        Object[] objs;
        List<Object[]> dataList = new ArrayList<>();
        title = "Danh sách kế hoạch VT, TB có thời hạn lưu kho lớn hơn 12 tháng của Cục DTNN KV";
        fileName = "ds-ke-hoach-vt-tb-co-thoi-han-luu-kho-lon-hon-12-thang-cua-cuc-dtnn-kv.xlsx";
        rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Loại hình nhập xuất", "Mã DS tổng hợp", "Số QĐ xuất giảm TC", "Thời gian xuất hàng", "Trích yếu", "Trạng thái"};
        for (int i = 0; i < data.size(); i++) {
            XhXkVtQdGiaonvXhHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getSoQuyetDinh();
            objs[3] = dx.getNgayKy();
            objs[4] = dx.getTenLoai();
            objs[5] = dx.getSoCanCu();
            objs[6] = dx.getSoCanCu();
            objs[7] = dx.getThoiHanXuatHang();
            objs[8] = dx.getTrichYeu();
            objs[9] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
            reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
            ReportTemplate model = findByTenFile(reportTemplateRequest);
            byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//            FileInputStream inputStream = new FileInputStream("src/main/resources/reports/xuatcuutrovientro/Phiếu kiểm nghiệm chất lượng.docx");
            XhXkVtQdGiaonvXhHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
