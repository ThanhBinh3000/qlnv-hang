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
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
        if (created.getCapTh() == 2) {
            List<Long> listIdDsHdr = created.getTongHopDtl().stream().map(XhXkTongHopDtl::getIdDsHdr).collect(Collectors.toList());
            List<XhXkDanhSachHdr> listDsHdr = xhXkDanhSachRepository.findByIdIn(listIdDsHdr);
            listDsHdr.forEach(s -> {
                s.setIdTongHop(id);
                s.setMaTongHop(ma);
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
            List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHop(data.getId());
            items.forEach(item -> {
                item.setIdTongHop(null);
                item.setMaTongHop(null);
                item.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
                xhXkDanhSachRepository.save(item);
            });
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
                List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHop(data.getId());
                items.forEach(item -> {
                    item.setIdTongHop(null);
                    item.setMaTongHop(null);
                    xhXkDanhSachRepository.save(item);
                });
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

        String title = "Danh sách vật tư thiết bị có thời hạn lưu kho lớn hơn 12 tháng";
        String[] rowsName = new String[]{"STT", "Năm KH", "Mã danh sách", "Chi cục DTNN", "Loại hàng hóa", "Chủng loại",
                "Điểm kho", "Ngăn/lô kho", "Ngày nhập kho", "SL hết hạn 12 tháng", "SL tồn", "DVT", "Ngày đề xuất", "Trạng thái"};
        String fileName = "danh-sach-vat-tu-thiet-bi-co-thoi-han-luu-kho-lon-hon-12-thang.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkTongHopHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            for (XhXkTongHopDtl dtl : qd.getTongHopDtl()) {
                objs[0] = i;
                objs[1] = qd.getNam();
                objs[2] = qd.getMaDanhSach();
                objs[3] = dtl.getTenChiCuc();
                objs[4] = dtl.getTenLoaiVthh();
                objs[5] = dtl.getTenCloaiVthh();
                objs[6] = dtl.getTenDiemKho();
                objs[7] = dtl.getTenLoKho();
                objs[8] = dtl.getNgayNhapKho();
                objs[9] = dtl.getSlHetHan();
                objs[10] = dtl.getSlTonKho();
                objs[11] = dtl.getDonViTinh();
                objs[12] = dtl.getNgayDeXuat();
                objs[13] = qd.getTenTrangThai();
                dataList.add(objs);
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

