package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlqRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPloRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaNtgReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaPloReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
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
import java.time.ZoneId;
import java.util.*;

@Service
public class XhTcTtinBdgHdrServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;
    @Autowired
    private XhTcTtinBdgDtlRepository xhTcTtinBdgDtlRepository;
    @Autowired
    private XhTcTtinBdgPloRepository xhTcTtinBdgPloRepository;
    @Autowired
    private XhTcTtinBdgNlqRepository xhTcTtinBdgNlqRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

    public Page<XhTcTtinBdgHdr> searchPage(CustomUserDetails currentUser, ThongTinDauGiaReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setDvql(currentUser.getDvql().substring(0, 4));
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhTcTtinBdgHdr> searchResultPage = xhTcTtinBdgHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                List<XhTcTtinBdgDtl> listDtl = xhTcTtinBdgDtlRepository.findAllByIdHdr(data.getId());
                for (XhTcTtinBdgDtl dtl : listDtl) {
                    List<XhTcTtinBdgPlo> ploList = xhTcTtinBdgPloRepository.findAllByIdDtl(dtl.getId());
                    dtl.setChildren(ploList != null && !ploList.isEmpty() ? ploList : Collections.emptyList());
                }
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhTcTtinBdgHdr create(CustomUserDetails currentUser, ThongTinDauGiaReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(request.getIdQdPdDtl())) {
            xhQdPdKhBdgDtlRepository.findById(request.getIdQdPdDtl()).map(checkDecision -> {
                checkDecision.setTrangThai(Contains.DANG_THUC_HIEN);
                return xhQdPdKhBdgDtlRepository.save(checkDecision);
            }).orElseThrow(() -> new Exception("Không tìm thấy Quyết định phê duyệt kế hoạch bán đấu giá."));
        }
        XhTcTtinBdgHdr newData = new XhTcTtinBdgHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DANG_THUC_HIEN);
        newData.setId(Long.valueOf(request.getMaThongBao().split("/")[0]));
        if (request.getTgianDauGiaTu() != null) {
            newData.setTgianDauGiaTu(request.getTgianDauGiaTu()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        if (request.getTgianDauGiaDen() != null) {
            newData.setTgianDauGiaDen(request.getTgianDauGiaDen()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        List<XhTcTtinBdgHdr> byDecisionId = xhTcTtinBdgHdrRepository.findByIdQdPdDtlOrderByLanDauGia(newData.getIdQdPdDtl());
        newData.setLanDauGia(byDecisionId.size() + 1);
        XhTcTtinBdgHdr createdRecord = xhTcTtinBdgHdrRepository.save(newData);
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhTcTtinBdgHdr update(CustomUserDetails currentUser, ThongTinDauGiaReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhTcTtinBdgHdr existingData = xhTcTtinBdgHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(request, existingData, "id", "maDvi");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        if (request.getTgianDauGiaTu() != null) {
            existingData.setTgianDauGiaTu(request.getTgianDauGiaTu()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        if (request.getTgianDauGiaDen() != null) {
            existingData.setTgianDauGiaDen(request.getTgianDauGiaDen()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        XhTcTtinBdgHdr updatedData = xhTcTtinBdgHdrRepository.save(existingData);
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(ThongTinDauGiaReq request, Long idHdr, Boolean isCheckRequired) {
        xhTcTtinBdgDtlRepository.deleteAllByIdHdr(isCheckRequired ? idHdr : null);
        for (ThongTinDauGiaDtlReq detailRequest : request.getChildren()) {
            XhTcTtinBdgDtl detail = new XhTcTtinBdgDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(idHdr);
            xhTcTtinBdgDtlRepository.save(detail);
            xhTcTtinBdgPloRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (ThongTinDauGiaPloReq phanLoRequest : detailRequest.getChildren()) {
                XhTcTtinBdgPlo phanLoDetail = new XhTcTtinBdgPlo();
                BeanUtils.copyProperties(phanLoRequest, phanLoDetail, "id");
                phanLoDetail.setIdDtl(detail.getId());
                phanLoDetail.setId(null);
                if (request.getKetQua().equals(0) && isCheckRequired) {
                    phanLoDetail.setSoLanTraGia(null);
                    phanLoDetail.setDonGiaTraGia(null);
                    phanLoDetail.setToChucCaNhan(null);
                }
                xhTcTtinBdgPloRepository.save(phanLoDetail);
            }
        }
        xhTcTtinBdgNlqRepository.deleteAllByIdHdr(isCheckRequired ? idHdr : null);
        if (request.getKetQua().equals(1)) {
            for (ThongTinDauGiaNtgReq nlqRequest : request.getListNguoiTgia()) {
                XhTcTtinBdgNlq nlqDetail = new XhTcTtinBdgNlq();
                BeanUtils.copyProperties(nlqRequest, nlqDetail, "id");
                nlqDetail.setId(null);
                nlqDetail.setIdHdr(idHdr);
                xhTcTtinBdgNlqRepository.save(nlqDetail);
            }
        }
    }

    public List<XhTcTtinBdgHdr> detail(List<Long> ids) throws Exception {
        List<XhTcTtinBdgHdr> resultList = xhTcTtinBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        for (XhTcTtinBdgHdr item : resultList) {
            List<XhTcTtinBdgDtl> detailList = xhTcTtinBdgDtlRepository.findAllByIdHdr(item.getId());
            for (XhTcTtinBdgDtl detailItem : detailList) {
                List<XhTcTtinBdgPlo> subDetailList = xhTcTtinBdgPloRepository.findAllByIdDtl(detailItem.getId());
                subDetailList.forEach(subDetailItem -> {
                    subDetailItem.setMapDmucDvi(mapDmucDvi);
                    this.calculate(subDetailItem, detailItem, item, subDetailList, detailList);
                });
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setChildren(subDetailList);
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setChildren(detailList);
            item.setListNguoiTgia(xhTcTtinBdgNlqRepository.findAllByIdHdr(item.getId()));
        }
        return resultList;
    }

    private void calculate(XhTcTtinBdgPlo subDetailItem, XhTcTtinBdgDtl detailItem, XhTcTtinBdgHdr item, List<XhTcTtinBdgPlo> subDetailList, List<XhTcTtinBdgDtl> detailList) {
        Optional<BigDecimal> donGiaOptional = Optional.ofNullable(subDetailItem.getDonGiaTraGia());
        Optional<BigDecimal> soLuongOptional = Optional.ofNullable(subDetailItem.getSoLuongDeXuat());
        BigDecimal thanhTien = donGiaOptional.flatMap(donGia -> soLuongOptional.map(soLuong -> donGia.multiply(soLuong))).orElse(null);
        subDetailItem.setThanhTien(thanhTien);
        BigDecimal sumThanhTien = subDetailList.stream()
                .map(XhTcTtinBdgPlo::getThanhTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumDonGiaDeXuat = subDetailList.stream()
                .map(XhTcTtinBdgPlo::getDonGiaDeXuat)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        detailItem.setThanhTien(sumThanhTien);
        detailItem.setDonGiaDeXuat(sumDonGiaDeXuat);
        BigDecimal sumTongTien = detailList.stream()
                .map(XhTcTtinBdgDtl::getThanhTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setTongTien(sumTongTien);
    }

    public XhTcTtinBdgHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhTcTtinBdgHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhTcTtinBdgHdr proposalData = xhTcTtinBdgHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!proposalData.getTrangThai().equals(Contains.DANG_THUC_HIEN)) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp.");
        }
        List<XhTcTtinBdgDtl> proposalDetailsList = xhTcTtinBdgDtlRepository.findAllByIdHdr(proposalData.getId());
        for (XhTcTtinBdgDtl proposalDetail : proposalDetailsList) {
            xhTcTtinBdgPloRepository.deleteAllByIdDtl(proposalDetail.getId());
        }
        xhTcTtinBdgDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhTcTtinBdgNlqRepository.deleteAllByIdHdr(proposalData.getId());
        xhTcTtinBdgHdrRepository.delete(proposalData);
    }

    @Transactional
    public XhTcTtinBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhTcTtinBdgHdr proposal = xhTcTtinBdgHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        switch (statusCombination) {
            case Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setTrangThai(statusReq.getTrangThai());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        XhTcTtinBdgHdr updatedData = xhTcTtinBdgHdrRepository.save(proposal);
        if (statusReq.getTrangThai().equals(Contains.DA_HOAN_THANH)) {
            updateDauGiaDetails(updatedData);
        }
        return updatedData;
    }

    private void updateDauGiaDetails(XhTcTtinBdgHdr proposal) {
        xhQdPdKhBdgDtlRepository.findById(proposal.getIdQdPdDtl()).ifPresent(decision -> {
            int quantityUnitsApproved = decision.getSlDviTsan();
            BigDecimal successfulUnits = xhQdPdKhBdgDtlRepository.countSlDviTsanThanhCong(
                    proposal.getIdQdPdDtl(),
                    proposal.getLoaiVthh(),
                    proposal.getMaDvi());
            BigDecimal unsuccessfulUnits = new BigDecimal(quantityUnitsApproved).subtract(successfulUnits);
            decision.setSoDviTsanThanhCong(successfulUnits);
            decision.setSoDviTsanKhongThanh(unsuccessfulUnits);
            decision.setKetQuaDauGia(decision.getSoDviTsanThanhCong() + "/" + quantityUnitsApproved);
            xhQdPdKhBdgDtlRepository.save(decision);
        });
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null || requestParams == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bandaugia/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhTcTtinBdgHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            reportDetail.setTenDvi(reportDetail.getTenDvi().toUpperCase());
            reportDetail.setTenCloaiVthh(reportDetail.getTenCloaiVthh().toUpperCase());
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}