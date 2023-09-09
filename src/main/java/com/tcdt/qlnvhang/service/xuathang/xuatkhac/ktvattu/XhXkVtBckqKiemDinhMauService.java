package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMauRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMau;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class XhXkVtBckqKiemDinhMauService extends BaseServiceImpl {


    @Autowired
    private XhXkVtBckqKiemDinhMauRepository xhXkVtBckqKiemDinhMauRepository;

    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;


    @Autowired
    private XhXkVtPhieuXuatNhapKhoRepository xhXkVtPhieuXuatNhapKhoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtBckqKiemDinhMau> searchPage(CustomUserDetails currentUser, XhXkVtBckqKiemDinhMauRequest req) throws Exception {
        req.setDvql(ObjectUtils.isEmpty(req.getDvql()) ? currentUser.getDvql() : req.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtBckqKiemDinhMau> search = xhXkVtBckqKiemDinhMauRepository.search(req, pageable);
        List<XhXkVtBckqKiemDinhMau> listData = search.getContent();
        if (!listData.isEmpty()) {
            Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
            Map<String, String> mapVthh = getListDanhMucHangHoa();
            List<Long> idsBcKqKdMaus = listData.stream().map(XhXkVtBckqKiemDinhMau::getId).collect(Collectors.toList());
            List<XhXkVtPhieuXuatNhapKho> allByIdBcKqkdMauIn = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdBcKqkdMauIn(idsBcKqKdMaus);
            Map<Long, List<XhXkVtPhieuXuatNhapKho>> mapPxk = allByIdBcKqkdMauIn.stream()
                    .collect(groupingBy(XhXkVtPhieuXuatNhapKho::getIdBcKqkdMau));
            listData.forEach(s -> {
                s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
                s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
                List<XhXkVtPhieuXuatNhapKho> listByBcId = mapPxk.get(s.getId());
                if (!ObjectUtils.isEmpty(listByBcId)) {
                    listByBcId.forEach(item -> {
                        item.setMapDmucDvi(mapDmucDvi);
                        item.setMapVthh(mapVthh);
                    });
                    s.setXhXkVtPhieuXuatNhapKho(listByBcId);
                }
            });
        }
        return search;
    }

    @Transactional
    public XhXkVtBckqKiemDinhMau save(CustomUserDetails currentUser, XhXkVtBckqKiemDinhMauRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBckqKiemDinhMau> optional = xhXkVtBckqKiemDinhMauRepository.findBySoBaoCao(objReq.getSoBaoCao());
        if (optional.isPresent()) {
            throw new Exception("Số báo cáo đã tồn tại");
        }
        XhXkVtBckqKiemDinhMau data = new XhXkVtBckqKiemDinhMau();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        XhXkVtBckqKiemDinhMau created = xhXkVtBckqKiemDinhMauRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBckqKiemDinhMau.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        //save lại pxk -- update mẫu bị hủy hay ko và số bc,id bc
        objReq.getListDetailPxk().forEach(item -> {
            item.setSoBcKqkdMau(created.getSoBaoCao());
            item.setIdBcKqkdMau(created.getId());
        });
        xhXkVtPhieuXuatNhapKhoRepository.saveAll(objReq.getListDetailPxk());
        //lưu lại số báo cáo vào qd giao nv xh
        Long[] idsQdGiaoNvXh = Arrays.stream(objReq.getIdQdGiaoNvXh().split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .toArray(Long[]::new);
        List<XhXkVtQdGiaonvXhHdr> listQdGiaoNvXh = xhXkVtQdGiaonvXhRepository.findByIdIn(Arrays.asList(idsQdGiaoNvXh));
        if (!listQdGiaoNvXh.isEmpty()) {
            listQdGiaoNvXh.forEach(item -> {
                item.setSoBaoCaoKdm(created.getSoBaoCao());
                item.setIdBaoCaoKdm(created.getId());
            });
            xhXkVtQdGiaonvXhRepository.saveAll(listQdGiaoNvXh);
        }
        return created;
    }

    @Transactional()
    public XhXkVtBckqKiemDinhMau update(CustomUserDetails currentUser, XhXkVtBckqKiemDinhMauRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtBckqKiemDinhMau> optional = xhXkVtBckqKiemDinhMauRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoBaoCao().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBaoCao().split("/")[0])) {
            Optional<XhXkVtBckqKiemDinhMau> optionalBySoBb = xhXkVtBckqKiemDinhMauRepository.findBySoBaoCao(objReq.getSoBaoCao());
            if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
                if (!optionalBySoBb.isPresent()) throw new Exception("Số báo cáo đã tồn tại!");
            }
        }
        XhXkVtBckqKiemDinhMau dx = optional.get();
        BeanUtils.copyProperties(objReq, dx);
        XhXkVtBckqKiemDinhMau created = xhXkVtBckqKiemDinhMauRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBckqKiemDinhMau.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBckqKiemDinhMau.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtBckqKiemDinhMau detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtBckqKiemDinhMau> optional = xhXkVtBckqKiemDinhMauRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtBckqKiemDinhMau model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBckqKiemDinhMau.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtBckqKiemDinhMau> optional = xhXkVtBckqKiemDinhMauRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        if (optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId()) || optional.get().getTrangThai().equals(TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
            XhXkVtBckqKiemDinhMau data = optional.get();
            fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBckqKiemDinhMau.TABLE_NAME));
            //update mẫu bị hủy cho phiếu xuất kho
            Long[] idsQdGiaoNvXh = Arrays.stream(data.getIdQdGiaoNvXh().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .toArray(Long[]::new);

            List<XhXkVtPhieuXuatNhapKho> allByIdCanCuIn = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdCanCuIn(Arrays.asList(idsQdGiaoNvXh));
            if (!allByIdCanCuIn.isEmpty()) {
                allByIdCanCuIn.forEach(item -> {
                    item.setMauBiHuy(Boolean.FALSE);
                    item.setSoBcKqkdMau(null);
                    item.setIdBcKqkdMau(null);
                });
            }
            List<XhXkVtQdGiaonvXhHdr> listQdGiaoNvXh = xhXkVtQdGiaonvXhRepository.findByIdIn(Arrays.asList(idsQdGiaoNvXh));
            if (!listQdGiaoNvXh.isEmpty()) {
                listQdGiaoNvXh.forEach(item -> {
                    item.setSoBaoCaoKdm(null);
                    item.setIdBaoCaoKdm(null);
                });
            }
            xhXkVtBckqKiemDinhMauRepository.delete(data);
        } else {
            throw new Exception("Bản ghi đang ở trạng thái " + TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()) + " không thể xóa.");
        }
    }

    @Transient
    public XhXkVtBckqKiemDinhMau approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtBckqKiemDinhMau> optional = xhXkVtBckqKiemDinhMauRepository.findById(Long.valueOf(statusReq.getId()));
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
        XhXkVtBckqKiemDinhMau created = xhXkVtBckqKiemDinhMauRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, XhXkVtBckqKiemDinhMauRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkVtBckqKiemDinhMau> page = this.searchPage(currentUser, objReq);
        List<XhXkVtBckqKiemDinhMau> data = page.getContent();

        String title = "Danh sách báo cáo kết quả kiểm định";
        String[] rowsName = new String[]{"STT", "Năm báo cáo", "Số báo cáo", "Tên báo cáo", "Ngày báo cáo", "Số QĐ giao nhiệm vụ XH lấy mẫu", "Số QĐ xuất giảm VT của Tổng Cục",
                "Số QĐ giao NV xuất giảm VT của cục", "Trạng thái"};
        String fileName = "danh-sach-bao-cao-ket-qua-kiem-dinh.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkVtBckqKiemDinhMau dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoBaoCao();
            objs[3] = dx.getTenBaoCao();
            objs[4] = dx.getNgayBaoCao();
            objs[5] = dx.getSoQdGiaoNvXh();
            objs[6] = null;
            objs[7] = null;
            objs[8] = dx.getTenTrangThai();
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
            XhXkVtBckqKiemDinhMau detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}

