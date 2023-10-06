package com.tcdt.qlnvhang.service.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.kiemtrachatluong.XhPhieuKnclRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchPhieuKnclReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.XhPhieuKnclReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhPhieuKnclService extends BaseServiceImpl {

    @Autowired
    private XhPhieuKnclRepository xhPhieuKnclRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhPhieuKnclHdr> searchPage(CustomUserDetails currentUser, SearchPhieuKnclReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0, 6));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhPhieuKnclHdr> search = xhPhieuKnclRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        Map<String, Map<String, Object>> mapVthh = getListDanhMucHangHoaObject();
        search.getContent().forEach(s -> {
            s.setMapVthh(mapVthh);
            s.setMapDmucDvi(mapDmucDvi);
        });
        return search;
    }

    @Transactional
    public XhPhieuKnclHdr create(CustomUserDetails currentUser, XhPhieuKnclReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
            Optional<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findBySoBbQd(objReq.getSoBbQd());
            if (optional.isPresent()) {
                throw new Exception("Số quyết định đã tồn tại");
            }
        }
        XhPhieuKnclHdr data = new XhPhieuKnclHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        XhPhieuKnclHdr created = xhPhieuKnclRepository.save(data);
        return created;
    }

    @Transactional
    public XhPhieuKnclHdr update(CustomUserDetails currentUser, XhPhieuKnclReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
            Optional<XhPhieuKnclHdr> soQd = xhPhieuKnclRepository.findBySoBbQd(objReq.getSoBbQd());
            if (soQd.isPresent()) {
                if (!soQd.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số quyết định đã tồn tại");
                }
            }
        }

        XhPhieuKnclHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data, "id", "maDvi");
        XhPhieuKnclHdr updated = xhPhieuKnclRepository.save(data);
        return updated;
    }


    public List<XhPhieuKnclHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, Map<String, Object>> mapVthh = getListDanhMucHangHoaObject();
        List<XhPhieuKnclHdr> allById = xhPhieuKnclRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
        });
        return allById;
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhPhieuKnclHdr> list = xhPhieuKnclRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        xhPhieuKnclRepository.deleteAll(list);

    }


    public XhPhieuKnclHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        } else {
            XhPhieuKnclHdr data = optional.get();
            String status = data.getTrangThai() + statusReq.getTrangThai();
            switch (status) {
                case Contains.DU_THAO + Contains.CHO_DUYET_TP:
                case Contains.TU_CHOI_TP + Contains.CHO_DUYET_TP:
                case Contains.TUCHOI_LDCC + Contains.CHO_DUYET_TP:
                case Contains.DU_THAO + Contains.CHODUYET_LDCC:
                    data.setNguoiGduyetId(currentUser.getUser().getId());
                    data.setNgayGduyet(LocalDate.now());
                    break;
                case Contains.CHO_DUYET_TP + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetTpId(currentUser.getUser().getId());
                    data.setNgayPduyetTp(LocalDate.now());
                    break;
                case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
                    data.setNguoiPduyetTpId(currentUser.getUser().getId());
                    data.setNgayPduyetTp(LocalDate.now());
                    data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                    break;
                case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                    break;
                case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    break;
                case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                    break;
                case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(statusReq.getTrangThai());
            XhPhieuKnclHdr created = xhPhieuKnclRepository.save(data);
            return created;
        }
    }


    public void export(CustomUserDetails currentUser, SearchPhieuKnclReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhPhieuKnclHdr> page = this.searchPage(currentUser, objReq);
        List<XhPhieuKnclHdr> data = page.getContent();

        String title = "Danh sách quyết định thanh lý hàng DTQG ";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký", "Hồ sơ đề nghị thanh lý", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhPhieuKnclHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoBbQd();
      /*objs[2] = qd.getTrichYeu();
      objs[3] = qd.getNgayKy();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();*/
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
            List<XhPhieuKnclHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }

}
