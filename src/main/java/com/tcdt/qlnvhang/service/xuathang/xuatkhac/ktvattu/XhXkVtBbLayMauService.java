package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdr;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkVtBbLayMauService extends BaseServiceImpl {


    @Autowired
    private XhXkVtBbLayMauHdrRepository xhXkVtBbLayMauHdrRepository;

    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;


    @Autowired
    private XhXkVtPhieuXuatNhapKhoRepository xhXkVtPhieuXuatNhapKhoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtBbLayMauHdr> searchPage(CustomUserDetails currentUser, XhXkVtBbLayMauRequest req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0,6));
        } else {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtBbLayMauHdr> search = xhXkVtBbLayMauHdrRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            if (s.getNguoiPduyetId() != null) {
                s.setTenThuKho(ObjectUtils.isEmpty(s.getNguoiPduyetId()) ? null : userInfoRepository.findById(s.getNguoiPduyetId()).get().getFullName());
            }
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkVtBbLayMauHdr save(CustomUserDetails currentUser, XhXkVtBbLayMauRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBbLayMauHdr> optional = xhXkVtBbLayMauHdrRepository.findBySoBienBan(objReq.getSoBienBan());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }
        XhXkVtBbLayMauHdr data = new XhXkVtBbLayMauHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        data.getXhXkVtBbLayMauDtl().forEach(s -> {
            s.setXhXkVtBbLayMauHdr(data);
            s.setId(null);
        });
        XhXkVtBbLayMauHdr created = xhXkVtBbLayMauHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBbLayMauHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }

    @Transactional()
    public XhXkVtBbLayMauHdr update(CustomUserDetails currentUser, XhXkVtBbLayMauRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtBbLayMauHdr> optional = xhXkVtBbLayMauHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoBienBan().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBienBan().split("/")[0])) {
            Optional<XhXkVtBbLayMauHdr> optionalBySoBb = xhXkVtBbLayMauHdrRepository.findBySoBienBan(objReq.getSoBienBan());
            if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
                if (!optionalBySoBb.isPresent()) throw new Exception("Số biên bản đã tồn tại!");
            }
        }
        XhXkVtBbLayMauHdr dx = optional.get();
        dx.getXhXkVtBbLayMauDtl().forEach(e -> e.setXhXkVtBbLayMauHdr(null));
        BeanUtils.copyProperties(objReq, dx);
        dx.getXhXkVtBbLayMauDtl().forEach(e -> e.setXhXkVtBbLayMauHdr(dx));
        dx.setXhXkVtBbLayMauDtl(objReq.getXhXkVtBbLayMauDtl());
        XhXkVtBbLayMauHdr created = xhXkVtBbLayMauHdrRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBbLayMauHdr.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBbLayMauHdr.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtBbLayMauHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtBbLayMauHdr> optional = xhXkVtBbLayMauHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtBbLayMauHdr model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBbLayMauHdr.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setMapDmucDvi(mapDmucDvi);
        model.setMapVthh(mapVthh);
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtBbLayMauHdr> optional = xhXkVtBbLayMauHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
            throw new Exception("Bản ghi có trạng thái khác dự thảo, không thể xóa.");
        }
        XhXkVtBbLayMauHdr data = optional.get();
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBbLayMauHdr.TABLE_NAME));
        xhXkVtBbLayMauHdrRepository.delete(data);
    }

//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<XhXkVtQdGiaonvXhHdr> list = xhXkVtQdGiaonvXhRepository.findByIdIn(idSearchReq.getIdList());
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
//        xhXkVtQdGiaonvXhRepository.deleteAll(list);
//    }

    @Transient
    public XhXkVtBbLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtBbLayMauHdr> optional = xhXkVtBbLayMauHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDC + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                optional.get().setNgayGduyet(LocalDate.now());
                break;
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
        XhXkVtBbLayMauHdr created = xhXkVtBbLayMauHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, XhXkVtBbLayMauRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkVtBbLayMauHdr> page = this.searchPage(currentUser, objReq);
        List<XhXkVtBbLayMauHdr> data = page.getContent();

        String title = "Danh sách biên bản lấy mẫu bàn giao mẫu ";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Số BB LM/BGM", "Ngày lấy mẫu", "Điểm Kho",
                "Lô kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau-ban-giao-mau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkVtBbLayMauHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdGiaoNvXh();
            objs[2] = dx.getNam();
            objs[3] = dx.getSoBienBan();
            objs[4] = dx.getNgayLayMau();
            objs[5] = dx.getTenDiemKho();
            objs[6] = dx.getTenLoKho();
            objs[7] = dx.getTenTrangThai();
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
            XhXkVtBbLayMauHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}

