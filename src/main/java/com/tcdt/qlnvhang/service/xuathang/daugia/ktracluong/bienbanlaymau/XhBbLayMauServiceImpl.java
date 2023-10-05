package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class XhBbLayMauServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;
    @Autowired
    private XhBbLayMauCtRepository xhBbLayMauCtRepository;
    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhBbLayMau> searchPage(CustomUserDetails currentUser, XhBbLayMauRequest req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhBbLayMau> search = xhBbLayMauRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhBbLayMau create(CustomUserDetails currentUser, XhBbLayMauRequest req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBbLayMau()) && xhBbLayMauRepository.existsBySoBbLayMau(req.getSoBbLayMau())) {
            throw new Exception("Số biên bản lấy mẫu " + req.getSoBbLayMau() + " đã tồn tại");
        }
        XhBbLayMau data = new XhBbLayMau();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdKtvBaoQuan(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBbLayMau().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhBbLayMau created = xhBbLayMauRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhBbLayMauRequest req, Long idHdr) {
        xhBbLayMauCtRepository.deleteAllByIdHdr(idHdr);
        for (XhBbLayMauCtRequest dtlReq : req.getChildren()) {
            XhBbLayMauCt dtl = new XhBbLayMauCt();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setId(null);
            dtl.setIdHdr(idHdr);
            xhBbLayMauCtRepository.save(dtl);
        }
    }

    @Transactional
    public XhBbLayMau update(CustomUserDetails currentUser, XhBbLayMauRequest req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbLayMau data = xhBbLayMauRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhBbLayMauRepository.existsBySoBbLayMauAndIdNot(req.getSoBbLayMau(), req.getId())) {
            throw new Exception("Số biên bản lấy mẫu " + req.getSoBbLayMau() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhBbLayMau update = xhBbLayMauRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhBbLayMau> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbLayMau> list = xhBbLayMauRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhBbLayMau> allById = xhBbLayMauRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhBbLayMau data : allById) {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            if (data.getIdThuKho() != null) {
                userInfoRepository.findById(data.getIdThuKho()).ifPresent(userInfo -> {
                    data.setTenThuKho(userInfo.getFullName());
                });
            }
            if (data.getIdKtvBaoQuan() != null) {
                userInfoRepository.findById(data.getIdKtvBaoQuan()).ifPresent(userInfo -> {
                    data.setTenKtvBaoQuan(userInfo.getFullName());
                });
            }
            if (data.getIdLanhDaoChiCuc() != null) {
                userInfoRepository.findById(data.getIdLanhDaoChiCuc()).ifPresent(userInfo -> {
                    data.setTenLanhDaoChiCuc(userInfo.getFullName());
                });
            }
            List<XhBbLayMauCt> listDtl = xhBbLayMauCtRepository.findAllByIdHdr(data.getId());
            data.setChildren(listDtl);
        }
        return allById;
    }

    public XhBbLayMau detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbLayMau> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhBbLayMau data = xhBbLayMauRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu xuất kho ở trạng thái bản nháp hoặc từ chối");
        }
        xhBbLayMauCtRepository.deleteAllByIdHdr(data.getId());
        xhBbLayMauRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhBbLayMau> list = xhBbLayMauRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DUTHAO) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDCC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với phiếu xuất kho ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> idHdr = list.stream().map(XhBbLayMau::getId).collect(Collectors.toList());
        List<XhBbLayMauCt> listDtl = xhBbLayMauCtRepository.findByIdHdrIn(idHdr);
        xhBbLayMauCtRepository.deleteAll(listDtl);
        xhBbLayMauRepository.deleteAll(list);
    }

    public XhBbLayMau approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhBbLayMau data = xhBbLayMauRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setIdLanhDaoChiCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhBbLayMau created = xhBbLayMauRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhBbLayMauRequest req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhBbLayMau> page = this.searchPage(currentUser, req);
        List<XhBbLayMau> data = page.getContent();
        String title = "Danh sách biên bản lấy mẫu/bàn giao mẫu";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho", "Ngày xuất dốc kho",
                "Số BB hao dôi", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau/ban-giao-mau.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhBbLayMau hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNam();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenLoKho();
            objs[6] = hdr.getSoBbLayMau();
            objs[7] = hdr.getNgayLayMau();
            objs[8] = hdr.getSoBbTinhKho();
            objs[9] = hdr.getNgayXuatDocKho();
            objs[10] = hdr.getSoBbHaoDoi();
            objs[11] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templatePath = baseReportFolder + "/bandaugia/";
            XhBbLayMau detail = this.detail(DataUtils.safeToLong(body.get("id")));
            if (detail.getLoaiVthh().startsWith("02")) {
                templatePath += "Biên bản lấy mẫu bàn giao mẫu vật tư.docx";
            } else {
                templatePath += "Biên bản lấy mẫu bàn giao mẫu lương thực.docx";
            }
            Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
            xhQdGiaoNvXhRepository.findById(detail.getIdQdNv())
                    .ifPresent(xhQdGiaoNvXh -> detail.setMaDviCha(xhQdGiaoNvXh.getMaDvi()));
            if (mapDmucDvi.containsKey((detail.getMaDviCha()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(detail.getMaDviCha());
                detail.setTenDviCha(objDonVi.get("tenDvi").toString());
            }
            List<XhBbLayMauCt> listDtl = xhBbLayMauCtRepository.findAllByIdHdr(detail.getId());
            if (listDtl != null || !listDtl.isEmpty()) {
                List<XhBbLayMauCt> filteredPhuongPhapLayMau = listDtl.stream().filter(type -> "PPLM".equals(type.getType()) && type.getChecked()).collect(Collectors.toList());
                List<XhBbLayMauCt> filteredChiTieuKiemTra = listDtl.stream().filter(type -> "CTCL".equals(type.getType()) && type.getChecked()).collect(Collectors.toList());
                if (!filteredPhuongPhapLayMau.isEmpty()) {
                    detail.setPhuongPhapLayMau(filteredPhuongPhapLayMau.get(0).getTen());
                }
                if (!filteredChiTieuKiemTra.isEmpty()) {
                    detail.setChiTieuKiemTra(String.join(",", filteredChiTieuKiemTra.stream().map(XhBbLayMauCt::getTen).collect(Collectors.toList())));
                }
                detail.setChildren(listDtl.stream().filter(type -> "NLQ".equals(type.getType())).collect(Collectors.toList()));
            }
            FileInputStream inputStream = new FileInputStream(templatePath);
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
