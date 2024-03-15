package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdr;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkTongHopVttbService extends BaseServiceImpl {


    @Autowired
    private XhXkTongHopRepository xhXkTongHopRepository;
    @Autowired
    private XhXkDanhSachRepository xhXkDanhSachRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkTongHopHdr> searchPage(CustomUserDetails currentUser, XhXkTongHopRequest req) throws Exception {
        req.setDvql(currentUser.getDvql());
        if (!DataUtils.isNullObject(req.getNgayTaoTu())) {
            req.setNgayTaoTu(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MAX));
        }
        if (!DataUtils.isNullObject(req.getNgayTaoDen())) {
            req.setNgayTaoDen(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MIN));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkTongHopHdr> search = xhXkTongHopRepository.searchPage(req, pageable);
        //set label
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.getTongHopDtl().forEach(s1 -> {
                s1.setMapDmucDvi(mapDmucDvi);
                s1.setMapVthh(mapVthh);
                s1.setNamNhap(xhXkTongHopRepository.getNamNhap(s1.getMaDiaDiem(), s1.getId()));
            });
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
            s.setTenDvi(mapDmucDvi.containsKey(s.getMaDvi()) ? mapDmucDvi.get(s.getMaDvi()) : null);
            String maDvql = DataUtils.isNullOrEmpty(s.getMaDvi()) ? s.getMaDvi() : s.getMaDvi().substring(0, s.getMaDvi().length() - 2);
            s.setMaDvql(maDvql);
            s.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
        });
        return search;
    }

    @Transactional
    public XhXkTongHopHdr save(CustomUserDetails currentUser, XhXkTongHopRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(objReq.getMaDanhSach())) {
            Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
            if (optional.isPresent()) {
                throw new Exception("Mã danh sách tổng hợp đã tồn tại");
            }
        }
        XhXkTongHopHdr data = new XhXkTongHopHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.getTongHopDtl().forEach(s -> s.setTongHopHdr(data));
        XhXkTongHopHdr created = xhXkTongHopRepository.save(data);
        created.setMaDanhSach(created.getMaDanhSach() + "_" + created.getId());
        created = xhXkTongHopRepository.save(created);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBbLayMauHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        Long id = created.getId();
        String ma = created.getMaDanhSach();
        //set ma tong hop cho danh sach
        List<Long> listIdDsHdr = created.getTongHopDtl().stream().map(XhXkTongHopDtl::getIdDsHdr).collect(Collectors.toList());
        if (created.getCapTh() == 2) {
            List<XhXkDanhSachHdr> listDsHdr = xhXkDanhSachRepository.findByIdIn(listIdDsHdr);
            listDsHdr.forEach(s -> {
                s.setIdTongHop(id);
                s.setMaTongHop(ma);
                s.setNgayTongHop(LocalDateTime.now());
                s.setTrangThai(TrangThaiAllEnum.DA_CHOT.getId());
            });
            xhXkDanhSachRepository.saveAll(listDsHdr);
        } else if (created.getCapTh() == 1) {
            List<XhXkDanhSachHdr> listDsHdr = xhXkDanhSachRepository.findAllByIdTongHopIn(listIdDsHdr);
            listDsHdr.forEach(s -> {
                s.setIdTongHopTc(id);
                s.setMaTongHopTc(ma);
                s.setNgayTongHopTc(LocalDateTime.now());
                s.setTrangThai(TrangThaiAllEnum.DA_CHOT.getId());
            });
            xhXkDanhSachRepository.saveAll(listDsHdr);
        }
        return detail(Arrays.asList(created.getId())).get(0);

    }

    public List<XhXkTongHopHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhXkTongHopHdr> optional = xhXkTongHopRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        List<XhXkTongHopHdr> allById = xhXkTongHopRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();

        allById.forEach(data -> {
            data.getTongHopDtl().forEach(s -> {
                s.setMapDmucDvi(mapDmucDvi);
                s.setMapVthh(mapVthh);
                s.setNamNhap(xhXkDanhSachRepository.getNamNhap(s.getMaDiaDiem(), s.getId()));
            });
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkVtBbLayMauHdr.TABLE_NAME));
            data.setFileDinhKems(fileDinhKem);
            data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
            data.setTenDvi(mapDmucDvi.containsKey(data.getMaDvi()) ? mapDmucDvi.get(data.getMaDvi()) : null);
            String maDvql = DataUtils.isNullOrEmpty(data.getMaDvi()) ? data.getMaDvi() : data.getMaDvi().substring(0, data.getMaDvi().length() - 2);
            data.setMaDvql(maDvql);
            data.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkTongHopHdr data = optional.get();
        if (!DataUtils.isNullObject(data.getId())) {
            if (data.getCapTh() == 2) {
                List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHop(data.getId());
                items.forEach(item -> {
                    item.setIdTongHop(null);
                    item.setMaTongHop(null);
                    item.setNgayTongHop(null);
                    item.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
                    xhXkDanhSachRepository.save(item);
                });
            } else if (data.getCapTh() == 1) {
                List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHopTc(data.getId());
                items.forEach(item -> {
                    item.setIdTongHopTc(null);
                    item.setMaTongHopTc(null);
                    item.setNgayTongHopTc(null);
                    xhXkDanhSachRepository.save(item);
                });
            }
        }
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBbLayMauHdr.TABLE_NAME));
        xhXkTongHopRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkTongHopHdr> list = xhXkTongHopRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        list.forEach(data -> {
            if (!DataUtils.isNullObject(data.getId())) {
                if (data.getCapTh() == 2) {
                    List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHop(data.getId());
                    items.forEach(item -> {
                        item.setIdTongHop(null);
                        item.setMaTongHop(null);
                        item.setNgayTongHop(null);
                        item.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
                        xhXkDanhSachRepository.save(item);
                    });
                } else if (data.getCapTh() == 1) {
                    List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHopTc(data.getId());
                    items.forEach(item -> {
                        item.setIdTongHopTc(null);
                        item.setMaTongHopTc(null);
                        item.setNgayTongHopTc(null);
                        xhXkDanhSachRepository.save(item);
                    });
                }
            }
        });
        xhXkTongHopRepository.deleteAll(list);
    }


    public XhXkTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.GUI_DUYET.getId())) {
            optional.get().setNguoiGduyetId(currentUser.getUser().getId());
            optional.get().setNgayGduyet(LocalDate.now());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhXkTongHopHdr created = xhXkTongHopRepository.save(optional.get());
        return created;
    }


    public void export(CustomUserDetails currentUser, XhXkTongHopRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        List<XhXkTongHopHdr> data = this.searchPage(currentUser, objReq).getContent();
        String title, fileName = "";
        String[] rowsName;
        Object[] objs;
        List<Object[]> dataList = new ArrayList<>();
        if (objReq.getLoai().equals("VT12")) {
            title = "Tổng hợp danh sách vật tư thiết bị có thời hạn lưu kho lớn hơn 12 tháng";
            rowsName = new String[]{"STT", "Chi cục DTNN", "Loại hàng DTQG", "Chủng loại DTQG", "Điểm kho", "Ngăn/lô kho",
                "Ngày nhập kho", "SL hết hạn (12 tháng) đề xất xuất bán", "SL tồn", "DVT", "Ngày đề xuất"};
            fileName = "tong-hop-danh-sach-vat-tu-thiet-bi-co-thoi-han-luu-kho-lon-hon-12-thang.xlsx";
        } else if (objReq.getLoai().equals("VT6")) {
            title = "Tổng hợp danh sách vật tư thiết bị có thời hạn lưu kho lớn hơn 6 tháng";
            rowsName = new String[]{"STT", "Chi cục DTNN", "Loại hàng DTQG", "Chủng loại DTQG", "Điểm kho", "Ngăn/lô kho",
                "Ngày nhập kho", "SL hết hạn (6 tháng) đề xất xuất bán", "SL tồn", "DVT", "Ngày đề xuất"};
            fileName = "tong-hop-danh-sach-vat-tu-thiet-bi-co-thoi-han-luu-kho-lon-hon-6-thang.xlsx";
        } else if (objReq.getLoai().equals("VTBH_BKK")) {
            title = "Tổng hợp danh sách vật tư thiết bị hòng hóc, giảm chất lượng do nguyên nhân bất khả kháng";
            rowsName = new String[]{"STT", "Chi cục DTNN", "Loại hàng DTQG", "Chủng loại DTQG", "Điểm kho", "Ngăn/lô kho",
                "Ngày nhập kho", "SL tồn", "SL cần xuất hàng", "DVT", "Ngày đề xuất", "lý do cần xuất hàng"};
            fileName = "tong-hop-danh-sach-vat-tu-thiet-bi-hong-hoc-giam-chat-luong-do-nguyen-nhan-bat-kha-khang.xlsx";
        } else {
            title = "Tổng hợp danh sách hàng DTQG thuộc diện xuất khỏi danh mục";
            rowsName = new String[]{"STT", "Số QĐ xuất hàng khỏi danh mục", "Chi cục DTNN", "Loại hàng DTQG", "Loại hình xuất",
            "Chủng loại DTQG", "Điểm kho", "Ngăn/lô kho", "SL tồn", "DVT"};
            fileName = "tong-hop-danh-sach-dtqg-thuoc-dien-xuat-khoi-danh-muc.xlsx";
        }
        for (int i = 0; i < data.size(); i++) {
            XhXkTongHopHdr qd = data.get(i);
            if (qd.getLoai().equals("VT12") || qd.getLoai().equals("VT6")) {
                for (XhXkTongHopDtl dtl : qd.getTongHopDtl()) {
                    objs = new Object[rowsName.length];
                    objs[0] = i;
                    objs[1] = dtl.getTenChiCuc();
                    objs[2] = dtl.getTenLoaiVthh();
                    objs[3] = dtl.getTenCloaiVthh();
                    objs[4] = dtl.getTenDiemKho();
                    objs[5] = dtl.getTenNganKho() + (dtl.getTenLoKho() != null ? ' ' + dtl.getTenLoKho() : "");
                    objs[6] = dtl.getNgayNhapKho().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    objs[7] = dtl.getSlTonKho();
                    objs[8] = dtl.getSlHetHan();
                    objs[9] = dtl.getDonViTinh();
                    objs[10] = dtl.getNgayDeXuat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    dataList.add(objs);
                }
            } else if (qd.getLoai().equals("VTBH_BKK")) {
                for (XhXkTongHopDtl dtl : qd.getTongHopDtl()) {
                    objs = new Object[rowsName.length];
                    objs[0] = i;
                    objs[1] = dtl.getTenChiCuc();
                    objs[2] = dtl.getTenLoaiVthh();
                    objs[3] = dtl.getTenCloaiVthh();
                    objs[4] = dtl.getTenDiemKho();
                    objs[5] = dtl.getTenNganKho() + (dtl.getTenLoKho() != null ? ' ' + dtl.getTenLoKho() : "");
                    objs[6] = dtl.getNgayNhapKho().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    objs[7] = dtl.getSlHetHan();
                    objs[8] = dtl.getSlTonKho();
                    objs[9] = dtl.getDonViTinh();
                    objs[10] = dtl.getNgayDeXuat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    objs[11] = dtl.getLyDo();
                    dataList.add(objs);
                }
            } else {
                for (XhXkTongHopDtl dtl : qd.getTongHopDtl()) {
                    objs = new Object[rowsName.length];
                    objs[0] = i;
                    objs[1] = dtl.getTenChiCuc();
                    objs[2] = dtl.getTenLoaiVthh();
                    objs[3] = dtl.getTenCloaiVthh();
                    objs[4] = dtl.getTenDiemKho();
                    objs[5] = dtl.getTenNganKho() + (dtl.getTenLoKho() != null ? ' ' + dtl.getTenLoKho() : "");
                    objs[6] = dtl.getNgayNhapKho().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    objs[7] = dtl.getSlHetHan();
                    objs[8] = dtl.getSlTonKho();
                    objs[9] = dtl.getDonViTinh();
                    objs[10] = dtl.getNgayDeXuat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    dataList.add(objs);
                }
            }
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "xuatcuutrovientro/" + fileName;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            List<XhXkTongHopHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}

