package com.tcdt.qlnvhang.service.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongDdiemNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.docx4j.wml.P;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhHopDongServiceImpl extends BaseServiceImpl {
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;

    @Autowired
    private XhHopDongDtlRepository xhHopDongDtlRepository;

    @Autowired
    private XhHopDongDdiemNhapKhoRepository xhHopDongDdiemNhapKhoRepository;

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;

    public Page<XhHopDongHdr> searchPage(CustomUserDetails currentUser, XhHopDongHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.DAKY);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhHopDongHdr> searchResultPage = xhHopDongHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiXh(data.getTrangThaiXh());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhHopDongHdr create(CustomUserDetails currentUser, XhHopDongHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(request.getSoHopDong()) && DataUtils.isNullObject(request.getIdHopDong()) && xhHopDongHdrRepository.existsBySoHopDong(request.getSoHopDong())) {
            throw new Exception("Số hợp đồng " + request.getSoHopDong() + " đã tồn tại");
        }
        if (!StringUtils.isEmpty(request.getSoPhuLuc()) && !DataUtils.isNullObject(request.getIdHopDong()) && xhHopDongHdrRepository.existsBySoPhuLuc(request.getSoPhuLuc())) {
            throw new Exception("Số phụ lục " + request.getSoPhuLuc() + " đã tồn tại");
        }
        if (!DataUtils.isNullObject(request.getIdQdKq())) {
            xhKqBdgHdrRepository.findById(request.getIdQdKq()).map(checkKetQua -> {
                checkKetQua.setTrangThaiHd(Contains.DANG_THUC_HIEN);
                return xhKqBdgHdrRepository.save(checkKetQua);
            }).orElseThrow(() -> new Exception("Số quyết định phê duyệt kết quả bán đấu giá không tồn tại"));
        }
        XhHopDongHdr newData = new XhHopDongHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DU_THAO);
        newData.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        List<String> unitTsanCodesList = request.getListMaDviTsan();
        if (!unitTsanCodesList.isEmpty()) {
            newData.setMaDviTsan(String.join(",", unitTsanCodesList));
        }
        XhHopDongHdr createdRecord = xhHopDongHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhHopDongHdr update(CustomUserDetails currentUser, XhHopDongHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhHopDongHdr existingData = xhHopDongHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (DataUtils.isNullObject(request.getIdHopDong()) && !StringUtils.isEmpty(request.getSoHopDong())) {
            boolean soHongDongExists = xhHopDongHdrRepository.existsBySoHopDongAndIdNot(request.getSoHopDong(), request.getId());
            if (soHongDongExists) {
                throw new Exception("Số hợp đồng " + request.getSoHopDong() + " đã tồn tại");
            }
        }
        if (!StringUtils.isEmpty(request.getSoPhuLuc())) {
            boolean soPhuLucExists = xhHopDongHdrRepository.existsBySoPhuLucAndIdNot(request.getSoPhuLuc(), request.getId());
            if (soPhuLucExists) {
                throw new Exception("Số phụ lục " + request.getSoPhuLuc() + " đã tồn tại");
            }
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiXh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        List<String> unitTsanCodesList = request.getListMaDviTsan();
        if (!unitTsanCodesList.isEmpty()) {
            existingData.setMaDviTsan(String.join(",", unitTsanCodesList));
        }
        XhHopDongHdr updatedData = xhHopDongHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhHopDongHdrReq request, Long headerId, Boolean isCheckRequired) {
        Boolean isType = request.getIdHopDong() != null;
        xhHopDongDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhHopDongDtlReq detailRequest : request.getChildren()) {
            XhHopDongDtl detail = new XhHopDongDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            detail.setTyPe(isType);
            xhHopDongDtlRepository.save(detail);
            if (request.getIdHopDong() == null) {
                xhHopDongDdiemNhapKhoRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
                for (XhDdiemNhapKhoReq deliveryPointReq : detailRequest.getChildren()) {
                    XhHopDongDdiemNhapKho deliveryPoint = new XhHopDongDdiemNhapKho();
                    BeanUtils.copyProperties(deliveryPointReq, deliveryPoint, "id");
                    deliveryPoint.setId(null);
                    deliveryPoint.setIdDtl(detail.getId());
                    xhHopDongDdiemNhapKhoRepository.save(deliveryPoint);
                }
            }
        }
    }

    public List<XhHopDongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongHdr> resultList = xhHopDongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhHopDongHdr item : resultList) {
            List<XhHopDongDtl> detailList = xhHopDongDtlRepository.findAllByIdHdr(item.getId());
            for (XhHopDongDtl detailItem : detailList) {
                List<XhHopDongDdiemNhapKho> deliveryPointList = xhHopDongDdiemNhapKhoRepository.findAllByIdDtl(detailItem.getId());
                deliveryPointList.forEach(deliveryPointItem -> {
                    deliveryPointItem.setMapDmucDvi(mapDmucDvi);
                });
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setChildren(deliveryPointList);
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            item.setTrangThaiXh(item.getTrangThaiXh());
            item.setChildren(detailList);
            if (!DataUtils.isNullObject(item.getMaDviTsan())) {
                item.setListMaDviTsan(Arrays.asList(item.getMaDviTsan().split(",")));
            }
            List<XhHopDongHdr> childList = xhHopDongHdrRepository.findAllByIdHopDong(item.getId());
            if (childList != null && !childList.isEmpty()) {
                item.setPhuLuc(childList);
            }
        }
        return resultList;
    }

    public XhHopDongHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhHopDongHdr proposalData = xhHopDongHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));

        if (!proposalData.getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp.");
        }
        List<XhHopDongDtl> proposalDetailsList = xhHopDongDtlRepository.findAllByIdHdr(proposalData.getId());
        for (XhHopDongDtl proposalDetail : proposalDetailsList) {
            xhHopDongDdiemNhapKhoRepository.deleteAllByIdDtl(proposalDetail.getId());
        }
        xhHopDongDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhHopDongHdrRepository.delete(proposalData);
    }

    public XhHopDongHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhHopDongHdr proposal = xhHopDongHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (statusCombination) {
            case Contains.DAKY + Contains.DUTHAO:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        XhHopDongHdr updatedData = xhHopDongHdrRepository.save(proposal);
        xhKqBdgHdrRepository.findById(updatedData.getIdQdKq()).ifPresent(result -> {
            Integer approvedContractCount = xhHopDongHdrRepository.countSlHopDongDaKy(
                    updatedData.getIdQdKq(),
                    updatedData.getLoaiVthh(),
                    updatedData.getMaDvi());
            result.setSlHopDongDaKy(approvedContractCount);
            xhKqBdgHdrRepository.save(result);
        });
        return updatedData;
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null || requestParams == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bandaugia/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhHopDongHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}