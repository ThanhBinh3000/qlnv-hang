package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattuRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattuRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattu;
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

@Service
public class XhXkVtQdXuatGiamVattuService extends BaseServiceImpl {
    @Autowired
    private XhXkVtQdXuatGiamVattuRepository xhXkVtQdXuatGiamVattuRepository;
    @Autowired
    private XhXkVtPhieuXuatNhapKhoRepository xhXkVtPhieuXuatNhapKhoRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtQdXuatGiamVattu> searchPage(CustomUserDetails currentUser, XhXkVtQdXuatGiamVattuRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (ObjectUtils.isEmpty(req.getDvql())) {
            req.setDvql(currentUser.getDvql());
        }
        Page<XhXkVtQdXuatGiamVattu> search = xhXkVtQdXuatGiamVattuRepository.searchPage(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(s -> {
            s.setTenLoai(Contains.getLoaiHinhXuat(s.getLoai()));
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
            s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
            s.setTenDviNhan(mapDmucDvi.get(s.getMaDviNhan()));
        });
        return search;
    }

    @Transactional
    public XhXkVtQdXuatGiamVattu save(CustomUserDetails currentUser, XhXkVtQdXuatGiamVattuRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(objReq.getSoQuyetDinh())) {
            Optional<XhXkVtQdXuatGiamVattu> optional = xhXkVtQdXuatGiamVattuRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
            if (optional.isPresent()) {
                throw new Exception("Số quyết định đã tồn tại");
            }
        }
        if (objReq.getXhXkVtPhieuXuatNhapKho().isEmpty()) {
            throw new Exception("Danh sách hàng hóa xuất giảm trống, không thể tạo quyết định.");
        }
        XhXkVtQdXuatGiamVattu data = new XhXkVtQdXuatGiamVattu();
        BeanUtils.copyProperties(objReq, data);
        XhXkVtQdXuatGiamVattu created = xhXkVtQdXuatGiamVattuRepository.save(data);
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtQdXuatGiamVattu.TABLE_NAME);
        //cập nhật số qd vào phiếu xuất kho có trạng thái bị huy
        List<XhXkVtPhieuXuatNhapKho> xhXkVtPhieuXuatNhapKho = objReq.getXhXkVtPhieuXuatNhapKho();
        if (!xhXkVtPhieuXuatNhapKho.isEmpty()) {
            xhXkVtPhieuXuatNhapKho.forEach(item -> {
                item.setSoQdXuatGiamVt(created.getSoCanCu());
                item.setIdQdXuatGiamVt(created.getId());
            });
            xhXkVtPhieuXuatNhapKhoRepository.saveAll(xhXkVtPhieuXuatNhapKho);
        }
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtQdXuatGiamVattu update(CustomUserDetails currentUser, XhXkVtQdXuatGiamVattuRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtQdXuatGiamVattu> optional = xhXkVtQdXuatGiamVattuRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoQuyetDinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoQuyetDinh().split("/")[0])) {
            Optional<XhXkVtQdXuatGiamVattu> optionalBySoTt = xhXkVtQdXuatGiamVattuRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
            if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
                if (!optionalBySoTt.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
            }
        }
        if (objReq.getXhXkVtPhieuXuatNhapKho().isEmpty()) {
            throw new Exception("Danh sách hàng hóa xuất giảm trống, không thể tạo quyết định.");
        }
        XhXkVtQdXuatGiamVattu dx = optional.get();
        BeanUtils.copyProperties(objReq, dx);
        XhXkVtQdXuatGiamVattu created = xhXkVtQdXuatGiamVattuRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtQdXuatGiamVattu.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtQdXuatGiamVattu.TABLE_NAME);
        return detail(created.getId());
    }

    @Transactional()
    public XhXkVtQdXuatGiamVattu detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtQdXuatGiamVattu> optional = xhXkVtQdXuatGiamVattuRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtQdXuatGiamVattu model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtQdXuatGiamVattu.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        List<XhXkVtPhieuXuatNhapKho> allByIdQdXuatGiamVt = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdQdXuatGiamVt(id);
        if (!allByIdQdXuatGiamVt.isEmpty()) {
            allByIdQdXuatGiamVt.stream().forEach(item -> {
                item.setMapVthh(mapVthh);
                item.setMapDmucDvi(mapDmucDvi);
            });
        }
        model.setXhXkVtPhieuXuatNhapKho(allByIdQdXuatGiamVt);
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenDviNhan(mapDmucDvi.get(model.getMaDviNhan()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }


    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtQdXuatGiamVattu> optional = xhXkVtQdXuatGiamVattuRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkVtQdXuatGiamVattu data = optional.get();
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtQdXuatGiamVattu.TABLE_NAME));
        //Clear id,so qd o phieu xuat kho
        List<XhXkVtPhieuXuatNhapKho> allByIdQdXuatGiamVt = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdQdXuatGiamVt(data.getId());
        if (!allByIdQdXuatGiamVt.isEmpty()) {
            allByIdQdXuatGiamVt.forEach(item -> {
                item.setSoQdXuatGiamVt(null);
                item.setIdQdXuatGiamVt(null);
            });
            xhXkVtPhieuXuatNhapKhoRepository.saveAll(allByIdQdXuatGiamVt);
        }
        xhXkVtQdXuatGiamVattuRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkVtQdXuatGiamVattu> list = xhXkVtQdXuatGiamVattuRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtQdXuatGiamVattu.TABLE_NAME));
        xhXkVtQdXuatGiamVattuRepository.deleteAll(list);
    }


    public XhXkVtQdXuatGiamVattu pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
        Optional<XhXkVtQdXuatGiamVattu> dx = xhXkVtQdXuatGiamVattuRepository.findById(req.getId());
        if (!dx.isPresent()) {
            throw new Exception("Không tồn tại bản ghi");
        }
        XhXkVtQdXuatGiamVattu xhXkVtQdXuatGiamVattu = dx.get();
        String status = xhXkVtQdXuatGiamVattu.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.DU_THAO + Contains.BAN_HANH:
                xhXkVtQdXuatGiamVattu.setNguoiDuyetId(currentUser.getUser().getId());
                xhXkVtQdXuatGiamVattu.setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công.");
        }
        xhXkVtQdXuatGiamVattu.setTrangThai(req.getTrangThai());
        XhXkVtQdXuatGiamVattu model = xhXkVtQdXuatGiamVattuRepository.save(xhXkVtQdXuatGiamVattu);
        return detail(model.getId());
    }


    public void export(CustomUserDetails currentUser, XhXkVtQdXuatGiamVattuRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        List<XhXkVtQdXuatGiamVattu> data = this.searchPage(currentUser, objReq).getContent();
        String title, fileName = "";
        String[] rowsName;
        Object[] objs;
        List<Object[]> dataList = new ArrayList<>();
        title = "Danh sách quyết định xuất giảm vật tư";
        fileName = "ds-ke-qd-xuat-giam-vattu.xlsx";
        rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số báo cáo KQKĐ mẫu", "Thời hạn xuất giảm VT", "Trích yếu", "Đơn vị nhận quyết định", "Số quyết định giao NVXH của Cục", "Trạng thái"};
        for (int i = 0; i < data.size(); i++) {
            XhXkVtQdXuatGiamVattu dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getSoQuyetDinh();
            objs[3] = dx.getNgayKy();
            objs[4] = dx.getTenLoai();
            objs[5] = dx.getSoCanCu();
            objs[6] = dx.getSoCanCu();
            objs[7] = dx.getThoiHanXuatGiam();
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
            XhXkVtQdXuatGiamVattu detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
