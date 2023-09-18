package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKhoRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnHdr;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkVtBhPhieuXuatNhapKhoService extends BaseServiceImpl {


    @Autowired
    private XhXkVtBhPhieuXuatNhapKhoRepository xhXkVtBhPhieuXuatNhapKhoRepository;

    @Autowired
    private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtBhPhieuXuatNhapKho> searchPage(CustomUserDetails currentUser, XhXkVtBhPhieuXuatNhapKhoRequest req) throws Exception {
        req.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtBhPhieuXuatNhapKho> search = xhXkVtBhPhieuXuatNhapKhoRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            s.setTenLoai(Contains.getLoaiHinhXuat(s.getLoai()));
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkVtBhPhieuXuatNhapKho save(CustomUserDetails currentUser, XhXkVtBhPhieuXuatNhapKhoRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBhPhieuXuatNhapKho> optional = xhXkVtBhPhieuXuatNhapKhoRepository.findBySoPhieu(objReq.getSoPhieu());
        if (optional.isPresent()) {
            throw new Exception("số số phiếu đã tồn tại");
        }
        XhXkVtBhPhieuXuatNhapKho data = new XhXkVtBhPhieuXuatNhapKho();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        XhXkVtBhPhieuXuatNhapKho created = xhXkVtBhPhieuXuatNhapKhoRepository.save(data);
        this.updatePhieuNk(created, false);
        // cập nhật trạng thái đang thực hiện cho QD giao nv nhập hàng
        if (!DataUtils.isNullObject(data.getLoaiPhieu())) {
            Optional<XhXkVtBhQdGiaonvXnHdr> qdGiaoNvXh = xhXkVtBhQdGiaonvXnRepository.findById(created.getIdCanCu());
            if (qdGiaoNvXh.isPresent()) {
                qdGiaoNvXh.get().setTrangThaiXh(TrangThaiAllEnum.DANG_THUC_HIEN.getId());
            }
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhPhieuXuatNhapKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }

    @Transactional
    public XhXkVtBhPhieuXuatNhapKho update(CustomUserDetails currentUser, XhXkVtBhPhieuXuatNhapKhoRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBhPhieuXuatNhapKho> optional = xhXkVtBhPhieuXuatNhapKhoRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhXkVtBhPhieuXuatNhapKho> soPhieuXuatKho = xhXkVtBhPhieuXuatNhapKhoRepository.findBySoPhieu(objReq.getSoPhieu());
        if (soPhieuXuatKho.isPresent()) {
            if (!soPhieuXuatKho.get().getId().equals(objReq.getId())) {
                throw new Exception("số số phiếu đã tồn tại");
            }
        }
        XhXkVtBhPhieuXuatNhapKho data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        XhXkVtBhPhieuXuatNhapKho created = xhXkVtBhPhieuXuatNhapKhoRepository.save(data);
        this.updatePhieuNk(created, false);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkVtBhPhieuXuatNhapKho.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhPhieuXuatNhapKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }

    public XhXkVtBhPhieuXuatNhapKho detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtBhPhieuXuatNhapKho> optional = xhXkVtBhPhieuXuatNhapKhoRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtBhPhieuXuatNhapKho model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhPhieuXuatNhapKho.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setMapDmucDvi(mapDmucDvi);
        model.setMapVthh(mapVthh);
        model.setTenLoai(Contains.getLoaiHinhXuat(model.getLoai()));
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtBhPhieuXuatNhapKho> optional = xhXkVtBhPhieuXuatNhapKhoRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkVtBhPhieuXuatNhapKho data = optional.get();
        this.updatePhieuNk(data, true);
        //Update trạng thái chưa thực hiện xuất hàng cho qd giao nv xuất hàng
        if (!DataUtils.isNullObject(data.getLoaiPhieu()) ) {
            Optional<XhXkVtBhQdGiaonvXnHdr> qdGiaoNv = xhXkVtBhQdGiaonvXnRepository.findById(data.getIdCanCu());
            if (qdGiaoNv.isPresent()) {
                qdGiaoNv.get().setTrangThaiXh(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
            }
        }
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkVtBhPhieuXuatNhapKho.TABLE_NAME));
        xhXkVtBhPhieuXuatNhapKhoRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkVtBhPhieuXuatNhapKho> list = xhXkVtBhPhieuXuatNhapKhoRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> idsQdGiaoNv = list.stream().map(XhXkVtBhPhieuXuatNhapKho::getIdCanCu).collect(Collectors.toList());
        List<XhXkVtBhQdGiaonvXnHdr> listQdGiaoNv = xhXkVtBhQdGiaonvXnRepository.findByIdIn(idsQdGiaoNv);
        if (!listQdGiaoNv.isEmpty()) {
            listQdGiaoNv.forEach(item -> {
                item.setTrangThaiXh(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
            });
            xhXkVtBhQdGiaonvXnRepository.saveAll(listQdGiaoNv);
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkVtBhPhieuXuatNhapKho.TABLE_NAME));
        xhXkVtBhPhieuXuatNhapKhoRepository.deleteAll(list);
    }

    @Transactional
    public XhXkVtBhPhieuXuatNhapKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtBhPhieuXuatNhapKho> optional = xhXkVtBhPhieuXuatNhapKhoRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhXkVtBhPhieuXuatNhapKho model = xhXkVtBhPhieuXuatNhapKhoRepository.save(optional.get());
        return model;
    }

    public void export(CustomUserDetails currentUser, XhXkVtBhPhieuXuatNhapKhoRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkVtBhPhieuXuatNhapKho> page = this.searchPage(currentUser, objReq);
        List<XhXkVtBhPhieuXuatNhapKho> data = page.getContent();

        String title = "Danh sách phiếu xuất kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho",
                "Lô kho", "Số phiếu xuất kho", "Ngày xuất kho", "Số phiếu KNCL", "Trạng thái"};
        String fileName = "danh-sach-phieu-xuat-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkVtBhPhieuXuatNhapKho dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoCanCu();
            objs[2] = dx.getNamKeHoach();
            objs[3] = dx.getThoiGianGiaoHang();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoPhieu();
            objs[7] = dx.getNgayXuatNhap();
            objs[8] = dx.getSoPhieuKdcl();
            objs[9] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void updatePhieuNk(XhXkVtBhPhieuXuatNhapKho phieuXuatNhapKho, boolean xoa) {
        if (!DataUtils.isNullObject(phieuXuatNhapKho.getIdCanCu())) {
            Optional<XhXkVtBhQdGiaonvXnHdr> phieuXuatKho = xhXkVtBhQdGiaonvXnRepository.findById(phieuXuatNhapKho.getIdCanCu());
            if (phieuXuatKho.isPresent()) {
                XhXkVtBhQdGiaonvXnHdr item = phieuXuatKho.get();
                if (xoa) {
                    item.setIdPhieuNk(null);
                    item.setSoPhieuNk(null);
                } else {
                    item.setIdPhieuNk(phieuXuatNhapKho.getId());
                    item.setSoPhieuNk(phieuXuatNhapKho.getSoPhieu());
                }
                xhXkVtBhQdGiaonvXnRepository.save(item);
            }
        }
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "xuatkhac/" + fileName;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhXkVtBhPhieuXuatNhapKho detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
