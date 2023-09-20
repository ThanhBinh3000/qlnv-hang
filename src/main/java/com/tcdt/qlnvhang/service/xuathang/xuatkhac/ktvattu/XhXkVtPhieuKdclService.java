package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class XhXkVtPhieuKdclService extends BaseServiceImpl {


    @Autowired
    private XhXkVtPhieuKdclHdrRepository xhXkVtPhieuKdclHdrRepository;

    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;


    @Autowired
    private XhXkVtPhieuXuatNhapKhoRepository xhXkVtPhieuXuatNhapKhoRepository;

    @Autowired
    private XhXkVtBbLayMauHdrRepository xhXkVtBbLayMauHdrRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtPhieuKdclHdr> searchPage(CustomUserDetails currentUser, XhXkVtPhieuKdclRequest req) throws Exception {
        req.setDvql(ObjectUtils.isEmpty(req.getMaDvi()) ? currentUser.getDvql() : req.getMaDvi());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtPhieuKdclHdr> search = xhXkVtPhieuKdclHdrRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<Long> idsBbLayMau = search.getContent().stream().map(XhXkVtPhieuKdclHdr::getIdBbLayMau).collect(Collectors.toList());
        Map<Long, XhXkVtBbLayMauHdr> mapBbLayMau = xhXkVtBbLayMauHdrRepository.findAllByIdIn(idsBbLayMau).stream().collect(Collectors.toMap(XhXkVtBbLayMauHdr::getId, Function.identity()));
        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            s.setNgayXuatLayMau(mapBbLayMau.get(s.getIdBbLayMau()).getNgayXuatLayMau());
            if (s.getNguoiPduyetId() != null) {
                s.setTenThuKho(ObjectUtils.isEmpty(s.getNguoiPduyetId()) ? null : userInfoRepository.findById(s.getNguoiPduyetId()).get().getFullName());
            }
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkVtPhieuKdclHdr save(CustomUserDetails currentUser, XhXkVtPhieuKdclRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtPhieuKdclHdr> optional = xhXkVtPhieuKdclHdrRepository.findBySoPhieu(objReq.getSoPhieu());
        if (optional.isPresent()) {
            throw new Exception("Số phiếu đã tồn tại");
        }
        XhXkVtPhieuKdclHdr data = new XhXkVtPhieuKdclHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        data.getXhXkVtPhieuKdclDtl().forEach(s -> {
            s.setXhXkVtPhieuKdclHdr(data);
            s.setId(null);
        });
        XhXkVtPhieuKdclHdr created = xhXkVtPhieuKdclHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtPhieuKdclHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        //update phiếu kdcl vào phiếu xuất kho
        // tìm số bb lấy mẫu -> tìm phiếu xuất kho
        Optional<XhXkVtBbLayMauHdr> bbLayMauById = xhXkVtBbLayMauHdrRepository.findById(created.getIdBbLayMau());
        if (bbLayMauById.isPresent()) {
            Long idPxk = bbLayMauById.get().getIdPhieuXuatKho();
            Optional<XhXkVtPhieuXuatNhapKho> pxkById = xhXkVtPhieuXuatNhapKhoRepository.findById(idPxk);
            if (pxkById.isPresent()) {
                pxkById.get().setSoPhieuKncl(created.getSoPhieu());
                pxkById.get().setIdPhieuKncl(created.getId());
                xhXkVtPhieuXuatNhapKhoRepository.save(pxkById.get());
            }
        }
        return created;
    }

    @Transactional()
    public XhXkVtPhieuKdclHdr update(CustomUserDetails currentUser, XhXkVtPhieuKdclRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtPhieuKdclHdr> optional = xhXkVtPhieuKdclHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoPhieu().contains("/") && !ObjectUtils.isEmpty(objReq.getSoPhieu().split("/")[0])) {
            Optional<XhXkVtPhieuKdclHdr> optionalBySoPhieu = xhXkVtPhieuKdclHdrRepository.findBySoPhieu(objReq.getSoPhieu());
            if (optionalBySoPhieu.isPresent() && optionalBySoPhieu.get().getId() != objReq.getId()) {
                if (!optionalBySoPhieu.isPresent()) throw new Exception("Số phiếu đã tồn tại!");
            }
        }
        XhXkVtPhieuKdclHdr dx = optional.get();
        dx.getXhXkVtPhieuKdclDtl().forEach(e -> e.setXhXkVtPhieuKdclHdr(null));
        BeanUtils.copyProperties(objReq, dx);
        dx.getXhXkVtPhieuKdclDtl().forEach(e -> e.setXhXkVtPhieuKdclHdr(dx));
        dx.setXhXkVtPhieuKdclDtl(objReq.getXhXkVtPhieuKdclDtl());
        XhXkVtPhieuKdclHdr created = xhXkVtPhieuKdclHdrRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtPhieuKdclHdr.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtPhieuKdclHdr.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtPhieuKdclHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtPhieuKdclHdr> optional = xhXkVtPhieuKdclHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtPhieuKdclHdr model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtPhieuKdclHdr.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setMapDmucDvi(mapDmucDvi);
        model.setMapVthh(mapVthh);
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtPhieuKdclHdr> optional = xhXkVtPhieuKdclHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
            throw new Exception("Bản ghi có trạng thái khác dự thảo, không thể xóa.");
        }
        XhXkVtPhieuKdclHdr data = optional.get();
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtPhieuKdclHdr.TABLE_NAME));
        //Update pxk
        // tìm số bb lấy mẫu -> tìm phiếu xuất kho
        Optional<XhXkVtBbLayMauHdr> bbLayMauById = xhXkVtBbLayMauHdrRepository.findById(data.getIdBbLayMau());
        if (bbLayMauById.isPresent()) {
            Long idPxk = bbLayMauById.get().getIdPhieuXuatKho();
            Optional<XhXkVtPhieuXuatNhapKho> pxkById = xhXkVtPhieuXuatNhapKhoRepository.findById(idPxk);
            if (pxkById.isPresent()) {
                pxkById.get().setSoPhieuKncl(null);
                pxkById.get().setIdPhieuKncl(null);
                xhXkVtPhieuXuatNhapKhoRepository.save(pxkById.get());
            }
        }
        xhXkVtPhieuKdclHdrRepository.delete(data);
    }

    @Transient
    public XhXkVtPhieuKdclHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtPhieuKdclHdr> optional = xhXkVtPhieuKdclHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDC + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
            case Contains.CHODUYET_LDC + Contains.CHO_DUYET_TP:
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                optional.get().setNgayGduyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
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
        XhXkVtPhieuKdclHdr created = xhXkVtPhieuKdclHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, XhXkVtPhieuKdclRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkVtPhieuKdclHdr> page = this.searchPage(currentUser, objReq);
        List<XhXkVtPhieuKdclHdr> data = page.getContent();

        String title = "Danh sách phiếu kiểm định chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Ngày lấy mẫu", "Điểm Kho",
                "Lô kho", "Chủng loại hàng hóa", "Số phiếu KĐCL", "Ngày kiểm định", "Số BB LM/BGM", "Trạng thái"};
        String fileName = "danh-sach-phieu-kiem-dinh-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkVtPhieuKdclHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdGiaoNvXh();
            objs[2] = dx.getNam();
            objs[3] = dx.getNgayLayMau();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getTenCloaiVthh();
            objs[7] = dx.getSoPhieu();
            objs[8] = dx.getNgayLapPhieu();
            objs[9] = dx.getSoBbLayMau();
            objs[10] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }


}

