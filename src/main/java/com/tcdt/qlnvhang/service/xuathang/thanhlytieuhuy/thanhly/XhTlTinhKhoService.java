package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlXuatKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhTlTinhKhoService extends BaseServiceImpl {

    @Autowired
    private XhTlTinhKhoRepository xhTlTinhKhoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhTlTinhKhoHdr> searchPage(CustomUserDetails currentUser, XhTlTinhKhoHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0, 6));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
            req.setTrangThai(Contains.DADUYET_LDCC);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlTinhKhoHdr> search = xhTlTinhKhoRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhTlTinhKhoHdr create(CustomUserDetails currentUser, XhTlTinhKhoHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        XhTlTinhKhoHdr data = new XhTlTinhKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        XhTlTinhKhoHdr created = xhTlTinhKhoRepository.save(data);
        return created;
    }

    @Transactional
    public XhTlTinhKhoHdr update(CustomUserDetails currentUser, XhTlTinhKhoHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhTlTinhKhoHdr> optional = xhTlTinhKhoRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        XhTlTinhKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhTlTinhKhoHdr updated = xhTlTinhKhoRepository.save(data);
        return updated;
    }

    public List<XhTlTinhKhoHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlTinhKhoHdr> list = xhTlTinhKhoRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<XhTlTinhKhoHdr> allById = xhTlTinhKhoRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            data.setTenKtvBaoQuan(ObjectUtils.isEmpty(data.getIdKtvBaoQuan()) ? null : userInfoRepository.findById(data.getIdKtvBaoQuan()).get().getFullName());
            data.setTenKeToan(ObjectUtils.isEmpty(data.getIdKeToan()) ? null : userInfoRepository.findById(data.getIdKeToan()).get().getFullName());
            data.setTenLanhDaoChiCuc(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlTinhKhoHdr> optional = xhTlTinhKhoRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Banr ghi không tồn tại");
        xhTlTinhKhoRepository.delete(optional.get());
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlTinhKhoHdr> list = xhTlTinhKhoRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        xhTlTinhKhoRepository.deleteAll(list);
    }

    public XhTlTinhKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlTinhKhoHdr> optional = xhTlTinhKhoRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhTlTinhKhoHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                optional.get().setNgayGduyet(LocalDate.now());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
                optional.get().setIdKtvBaoQuan(currentUser.getUser().getId());
                optional.get().setNgayPduyetKtvBq(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
                optional.get().setIdKeToan(currentUser.getUser().getId());
                optional.get().setNgayPduyetKt(LocalDate.now());
                break;
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhTlTinhKhoHdr created = xhTlTinhKhoRepository.save(data);
        return created;
    }
    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "xuatthanhly/" + fileName;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            List<XhTlTinhKhoHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}