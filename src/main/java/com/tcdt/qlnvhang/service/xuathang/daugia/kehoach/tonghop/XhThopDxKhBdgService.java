package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.tonghop.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBdgService extends BaseServiceImpl {

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;
    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    @Autowired
    private XhDxKhBanDauGiaServiceImpl xhDxKhBanDauGiaServiceImpl;

    public Page<XhThopDxKhBdg> searchPage(CustomUserDetails currentUser, SearchXhThopDxKhBdg req) throws Exception {
        req.setDvql(currentUser.getDvql());
        if (!DataUtils.isNullObject(req.getNgayThopTu())) {
            req.setNgayThopTu(req.getNgayThopTu().toLocalDate().atTime(LocalTime.MAX));
        }
        if (!DataUtils.isNullObject(req.getNgayThopDen())) {
            req.setNgayThopDen(req.getNgayThopDen().toLocalDate().atTime(LocalTime.MIN));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhThopDxKhBdg> search = xhThopDxKhBdgRepository.searchPage(req, pageable);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(data -> {
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    public XhThopDxKhBdg sumarryData(CustomUserDetails currentUser, XhThopChiTieuReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        List<XhDxKhBanDauGia> dxuatList = xhDxKhBanDauGiaRepository.listTongHop(req);
        if (dxuatList.isEmpty()) throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        XhThopDxKhBdg thopHdr = new XhThopDxKhBdg();
        thopHdr.setId(getNextSequence("XH_THOP_DX_KH_BDG_SEQ"));
        List<XhThopDxKhBdgDtl> thopDtls = new ArrayList<>();
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        for (XhDxKhBanDauGia dxuat : dxuatList) {
            XhThopDxKhBdgDtl thopDtl = new XhThopDxKhBdgDtl();
            BeanUtils.copyProperties(dxuat, thopDtl, "id");
            thopDtl.setMaDvi(dxuat.getMaDvi());
            if (mapDmucDvi.containsKey((thopDtl.getMaDvi()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(thopDtl.getMaDvi());
                thopDtl.setTenDvi(objDonVi.get("tenDvi").toString());
            }
            thopDtl.setIdDxHdr(dxuat.getId());
            thopDtl.setSoDxuat(dxuat.getSoDxuat());
            thopDtl.setNgayPduyet(dxuat.getNgayPduyet());
            thopDtl.setTrichYeu(dxuat.getTrichYeu());
            thopDtl.setSlDviTsan(dxuat.getSlDviTsan());
            thopDtl.setTongSoLuong(dxuat.getTongSoLuong());
            thopDtl.setTongTienKhoiDiemDx(dxuat.getTongTienKhoiDiemDx());
            thopDtl.setKhoanTienDatTruoc(dxuat.getKhoanTienDatTruoc());
            thopDtl.setTongTienDatTruocDx(dxuat.getTongTienDatTruocDx());
            thopDtl.setTrangThai(dxuat.getTrangThai());
            thopDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(thopDtl.getTrangThai()));
            thopDtls.add(thopDtl);
        }
        thopHdr.setChildren(thopDtls);
        return thopHdr;
    }

    @Transactional
    public XhThopDxKhBdg create(CustomUserDetails currentUser, XhThopDxKhBdgReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(req.getIdTh())) {
            Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(req.getIdTh());
            if (optional.isPresent()) throw new Exception("Mã tổng hợp đã tồn tại");
        }
        XhThopDxKhBdg data = new XhThopDxKhBdg();
        BeanUtils.copyProperties(req, data);
        data.setId(req.getIdTh());
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.CHUATAO_QD);
        XhThopDxKhBdg created = xhThopDxKhBdgRepository.save(data);
        if (data.getId() > 0 && data.getChildren().size() > 0) {
            List<String> soDxuatList = data.getChildren().stream().map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanDauGiaRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP, data.getId());
        }
        return created;
    }

    @Transactional
    public XhThopDxKhBdg update(CustomUserDetails currentUser, XhThopDxKhBdgReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        XhThopDxKhBdg data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhThopDxKhBdg updated = xhThopDxKhBdgRepository.save(data);
        return updated;
    }

    public List<XhThopDxKhBdg> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhThopDxKhBdg> list = xhThopDxKhBdgRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhThopDxKhBdg> allById = xhThopDxKhBdgRepository.findAllById(ids);
        allById.forEach(data -> {
            data.getChildren().forEach(dtl -> {
                if (mapDmucDvi.containsKey((dtl.getMaDvi()))) {
                    Map<String, Object> objDonVi = mapDmucDvi.get(dtl.getMaDvi());
                    dtl.setTenDvi(objDonVi.get("tenDvi").toString());
                }
                dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            });
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhThopDxKhBdg data = optional.get();
        if (!data.getTrangThai().equals(Contains.CHUATAO_QD)) {
            throw new Exception("Chỉ thực hiện xóa tổng hợp ở trạng thái chưa tạo quyết định");
        }
        List<XhDxKhBanDauGia> list = xhDxKhBanDauGiaRepository.findAllByIdThop(data.getId());
        list.forEach(dataDx -> {
            dataDx.setIdThop(null);
            dataDx.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanDauGiaRepository.saveAll(list);
        xhThopDxKhBdgRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhThopDxKhBdg> list = xhThopDxKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhThopDxKhBdg xhThopDxKhBdg : list) {
            if (!xhThopDxKhBdg.getTrangThai().equals(Contains.CHUATAO_QD)) {
                throw new Exception("Chỉ thực hiện xóa tổng hợp ở trạng thái chưa tạo quyết định");
            }
        }
        List<Long> listIdThop = list.stream().map(XhThopDxKhBdg::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGia> listDx = xhDxKhBanDauGiaRepository.findByIdThopIn(listIdThop);
        listDx.forEach(dataDx -> {
            dataDx.setIdThop(null);
            dataDx.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanDauGiaRepository.saveAll(listDx);
        xhThopDxKhBdgRepository.deleteAll(list);
    }

    public void export(CustomUserDetails currentUser, SearchXhThopDxKhBdg req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhThopDxKhBdg> page = this.searchPage(currentUser, req);
        List<XhThopDxKhBdg> data = page.getContent();
        String title = "Danh sách tổng hợp kế hoạch bán đấu giá";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch", "Số QĐ phê duyêt KH BDG ", "Loại hàng hóa", "Trạng thái"};
        String fileName = "Tong-hop-de-xuat-ke-hoach-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBdg dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getId();
            objs[2] = dx.getNgayThop();
            objs[3] = dx.getNoiDungThop();
            objs[4] = dx.getNamKh();
            objs[5] = dx.getSoQdPd();
            objs[6] = dx.getTenLoaiVthh();
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
//      FileInputStream inputStream = new FileInputStream("src/main/resources/Tổng hợp kế hoạch bán đấu giá.docx");
            List<XhThopDxKhBdg> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            XhThopDxKhBdg xhThopDxKhBdg = detail.get(0);
            List<Long> listIdChild = xhThopDxKhBdg.getChildren().stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanDauGia> tableData = xhDxKhBanDauGiaServiceImpl.detail(listIdChild);
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("nam", xhThopDxKhBdg.getNamKh());
            hashMap.put("tenCloaiVthh", xhThopDxKhBdg.getTenCloaiVthh().toUpperCase());
            hashMap.put("table", tableData);
            String s = objectMapper.writeValueAsString(hashMap);
            return docxToPdfConverter.convertDocxToPdf(inputStream, hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
