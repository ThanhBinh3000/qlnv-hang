package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.time.LocalDate;
import java.util.*;

@Service
public class XhHopDongBttServiceImpI extends BaseServiceImpl {

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private XhHopDongBttDtlRepository xhHopDongBttDtlRepository;
    @Autowired
    private XhHopDongBttDviRepository xhHopDongBttDviRepository;
    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;

    public Page<XhHopDongBttHdr> searchPage(CustomUserDetails currentUser, XhHopDongBttHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setTrangThai(Contains.DAKY);
        } else {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhHopDongBttHdr> searchResultPage = xhHopDongBttHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                String maCuc = data.getMaDvi().length() >= 6 ? data.getMaDvi().substring(0, 6) : "";
                String maChiCuc = data.getMaDvi().length() >= 8 ? data.getMaDvi().substring(0, 8) : "";
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                if (!maCuc.isEmpty()) {
                    List<XhHopDongBttDtl> listDtl = xhHopDongBttDtlRepository.findAllByIdHdr(data.getId());
                    for (XhHopDongBttDtl item : listDtl) {
                        item.setMapDmucDvi(mapDmucDvi);
                        List<XhHopDongBttDvi> listDiaDiemKho = xhHopDongBttDviRepository.findAllByIdDtl(item.getId());
                        listDiaDiemKho.forEach(child -> {
                            child.setMapDmucDvi(mapDmucDvi);
                        });
                        item.setChildren(listDiaDiemKho != null && !listDiaDiemKho.isEmpty() ? listDiaDiemKho : Collections.emptyList());
                    }
                    data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
                }
                if (!maChiCuc.isEmpty()) {
                    List<XhHopDongBttDvi> listDiaDiemKho = xhHopDongBttDviRepository.findAllByIdHdr(data.getId());
                    listDiaDiemKho.forEach(child -> {
                        child.setMapDmucDvi(mapDmucDvi);
                    });
                    data.setXhHopDongBttDviList(listDiaDiemKho != null && !listDiaDiemKho.isEmpty() ? listDiaDiemKho : Collections.emptyList());
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhHopDongBttHdr create(CustomUserDetails currentUser, XhHopDongBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        String capDvi = currentUser.getUser().getCapDvi();
        boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
        if (!StringUtils.isEmpty(request.getSoHopDong()) && DataUtils.isNullObject(request.getIdHd()) && xhHopDongBttHdrRepository.existsBySoHopDong(request.getSoHopDong())) {
            throw new Exception("Số hợp đồng " + request.getSoHopDong() + " đã tồn tại");
        }
        if (!StringUtils.isEmpty(request.getSoPhuLuc()) && !DataUtils.isNullObject(request.getIdHd()) && xhHopDongBttHdrRepository.existsBySoPhuLuc(request.getSoPhuLuc())) {
            throw new Exception("Số phụ lục " + request.getSoPhuLuc() + " đã tồn tại");
        }
        this.updateRelatedEntityStatus(request, isCapCuc);
        XhHopDongBttHdr newData = new XhHopDongBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getUser().getDvql());
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        newData.setTrangThai(Contains.DU_THAO);
        newData.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        List<String> unitTsanCodesList = request.getListMaDviTsan();
        if (!unitTsanCodesList.isEmpty()) {
            newData.setMaDviTsan(String.join(",", unitTsanCodesList));
        }
        XhHopDongBttHdr createdRecord = xhHopDongBttHdrRepository.save(newData);
        this.updateRelatedEntityHdChuaKy(createdRecord, isCapCuc);
        if (DataUtils.isNullObject(request.getIdHd())) {
            this.saveDetailHopDong(request, createdRecord.getId(), isCapCuc, false);
        } else {
            this.saveDetailPhuLuc(request, createdRecord.getId(), false);
        }
        return createdRecord;
    }


    private void updateRelatedEntityStatus(XhHopDongBttHdrReq request, Boolean isCheckRequired) throws Exception {
        if (isCheckRequired && !DataUtils.isNullObject(request.getIdQdKq())) {
            xhKqBttHdrRepository.findById(request.getIdQdKq()).map(checkData -> {
                checkData.setTrangThaiHd(Contains.DANG_THUC_HIEN);
                return xhKqBttHdrRepository.save(checkData);
            }).orElseThrow(() -> new Exception("Số quyết định phê duyệt kết quả bán trực tiếp không tồn tại"));
        } else if (!isCheckRequired && !DataUtils.isNullObject(request.getIdChaoGia())) {
            xhQdPdKhBttDtlRepository.findById(request.getIdChaoGia()).map(checkData -> {
                checkData.setTrangThaiHd(Contains.DANG_THUC_HIEN);
                return xhQdPdKhBttDtlRepository.save(checkData);
            }).orElseThrow(() -> new Exception("Tổ chức triển khai hế hoạch bán trực tiếp không tồn tại"));
        }
    }

    private void updateRelatedEntityHdChuaKy(XhHopDongBttHdr createdRecord, Boolean isCheckRequired) {
        if (isCheckRequired && !DataUtils.isNullObject(createdRecord.getIdQdKq())) {
            xhKqBttHdrRepository.findById(createdRecord.getIdQdKq()).ifPresent(relatedEntity -> {
                Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyCuc(createdRecord.getIdQdKq());
                relatedEntity.setSlHdChuaKy(slHdChuaKy);
                xhKqBttHdrRepository.save(relatedEntity);
            });
        } else if (!isCheckRequired && !DataUtils.isNullObject(createdRecord.getIdChaoGia())) {
            xhQdPdKhBttDtlRepository.findById(createdRecord.getIdChaoGia()).ifPresent(relatedEntity -> {
                Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyChiCuc(createdRecord.getIdChaoGia());
                relatedEntity.setSlHdChuaKy(slHdChuaKy);
                xhQdPdKhBttDtlRepository.save(relatedEntity);
            });
        }
    }

    @Transactional
    public XhHopDongBttHdr update(CustomUserDetails currentUser, XhHopDongBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        String capDvi = currentUser.getUser().getCapDvi();
        boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
        XhHopDongBttHdr existingData = xhHopDongBttHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (DataUtils.isNullObject(request.getIdHd()) && !StringUtils.isEmpty(request.getSoHopDong())) {
            boolean soHdExists = xhHopDongBttHdrRepository.existsBySoHopDongAndIdNot(request.getSoHopDong(), request.getId());
            if (soHdExists) {
                throw new Exception("Số hợp đồng đã tồn tại");
            }
        } else if (!StringUtils.isEmpty(request.getSoPhuLuc())) {
            boolean soPhuLucExists = xhHopDongBttHdrRepository.existsBySoPhuLucAndIdNot(request.getSoPhuLuc(), request.getId());
            if (soPhuLucExists) {
                throw new Exception("Số phụ lục đã tồn tại");
            }
        }
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "trangThaiXh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        List<String> unitTsanCodesList = request.getListMaDviTsan();
        if (!unitTsanCodesList.isEmpty()) {
            existingData.setMaDviTsan(String.join(",", unitTsanCodesList));
        }
        XhHopDongBttHdr updatedData = xhHopDongBttHdrRepository.save(existingData);
        if (DataUtils.isNullObject(request.getIdHd())) {
            this.saveDetailHopDong(request, updatedData.getId(), isCapCuc, true);
        } else {
            this.saveDetailPhuLuc(request, updatedData.getId(), true);
        }
        return updatedData;
    }

    private void saveDetailHopDong(XhHopDongBttHdrReq request, Long headerId, Boolean isCapCuc, Boolean isCheckRequired) {
        //Thêm Hợp đồng detail ở cấp Cục
        if (isCapCuc) {
            xhHopDongBttDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
            for (XhHopDongBttDtlReq detailRequest : request.getChildren()) {
                XhHopDongBttDtl detail = new XhHopDongBttDtl();
                BeanUtils.copyProperties(detailRequest, detail, "id");
                detail.setIdHdr(headerId);
                XhHopDongBttDtl createdRecord = xhHopDongBttDtlRepository.save(detail);
                //Hiển thị thông tin phụ lục để thêm vào detail phụ lục
                List<XhHopDongBttDtl> phuLucDtl = xhHopDongBttDtlRepository.findAllByIdHdDtl(detailRequest.getId());
                if (!DataUtils.isNullOrEmpty(phuLucDtl)) {
                    phuLucDtl.forEach(item -> item.setIdHdDtl(createdRecord.getId()));
                    xhHopDongBttDtlRepository.saveAll(phuLucDtl);
                }
                xhHopDongBttDviRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
                for (XhHopDongBttDviReq deliveryPointReq : detailRequest.getChildren()) {
                    XhHopDongBttDvi deliveryPoint = new XhHopDongBttDvi();
                    BeanUtils.copyProperties(deliveryPointReq, deliveryPoint, "id");
                    deliveryPoint.setId(null);
                    deliveryPoint.setIdDtl(detail.getId());
                    xhHopDongBttDviRepository.save(deliveryPoint);
                }
            }
        } else {
            //Thêm Hợp đồng detail ở cấp Chi cục
            xhHopDongBttDviRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
            for (XhHopDongBttDviReq detailRequest : request.getXhHopDongBttDviList()) {
                XhHopDongBttDvi detail = new XhHopDongBttDvi();
                BeanUtils.copyProperties(detailRequest, detail, "id");
                detail.setId(null);
                detail.setIdHdr(headerId);
                xhHopDongBttDviRepository.save(detail);
            }
        }
    }

    //Thêm thông tin phụ lục detail
    private void saveDetailPhuLuc(XhHopDongBttHdrReq req, Long idHdr, Boolean isCheckRequired) {
        xhHopDongBttDtlRepository.deleteAllByIdHdr(isCheckRequired ? idHdr : null);
        for (XhHopDongBttDtlReq phuLucReq : req.getPhuLucDtl()) {
            XhHopDongBttDtl phuLuc = new XhHopDongBttDtl();
            BeanUtils.copyProperties(phuLucReq, phuLuc, "id");
            phuLuc.setIdHdr(idHdr);
            xhHopDongBttDtlRepository.save(phuLuc);
        }
    }

    public List<XhHopDongBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongBttHdr> resultList = xhHopDongBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");

        for (XhHopDongBttHdr item : resultList) {
            UserInfo userInfo = SecurityContextService.getUser();
            String capDvi = userInfo.getCapDvi();
            boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
            if (isCapCuc) {
                List<XhHopDongBttDtl> detailList = xhHopDongBttDtlRepository.findAllByIdHdr(item.getId());
                for (XhHopDongBttDtl detailItem : detailList) {
                    List<XhHopDongBttDvi> deliveryPointList = xhHopDongBttDviRepository.findAllByIdDtl(detailItem.getId());
                    deliveryPointList.forEach(deliveryPointItem -> deliveryPointItem.setMapDmucDvi(mapDmucDvi));
                    if (!DataUtils.isNullObject(item.getIdHd())) {
                        xhHopDongBttDtlRepository.findById(detailItem.getIdHdDtl()).ifPresent(Appendix -> {
                            detailItem.setMapDmucDvi(mapDmucDvi);
                            detailItem.setDiaChiHd(Appendix.getDiaChi());
                        });
                    }
                    detailItem.setMapDmucDvi(mapDmucDvi);
                    detailItem.setChildren(deliveryPointList);
                }
                this.detailPhuLuc(item, mapDmucDvi);
                item.setChildren(detailList);
            } else {
                List<XhHopDongBttDvi> detailList = xhHopDongBttDviRepository.findAllByIdHdr(item.getId());
                detailList.forEach(detailItem -> detailItem.setMapDmucDvi(mapDmucDvi));
                item.setXhHopDongBttDviList(detailList);
            }
            if (!DataUtils.isNullObject(item.getMaDviTsan())) {
                item.setListMaDviTsan(Arrays.asList(item.getMaDviTsan().split(",")));
            }
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
        }
        return resultList;
    }


    private void detailPhuLuc(XhHopDongBttHdr item, Map<String, String> mapDmucDvi) {
        List<XhHopDongBttHdr> resultList = xhHopDongBttHdrRepository.findAllByIdHd(item.getId());
        for (XhHopDongBttHdr result : resultList) {
            List<XhHopDongBttDtl> detailList = xhHopDongBttDtlRepository.findAllByIdHdr(result.getId());
            detailList.forEach(detailItem -> {
                detailItem.setMapDmucDvi(mapDmucDvi);
            });
            result.setTrangThai(result.getTrangThai());
            result.setPhuLucDtl(detailList);
        }
        item.setPhuLuc(resultList);
    }

    public XhHopDongBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhHopDongBttHdr proposalData = xhHopDongBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO);
        if (!allowedStatus.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ được xóa với hợp đồng ở trạng thái là dự thảo");
        }
        boolean isCapCuc = Contains.CAP_CUC.equals(SecurityContextService.getUser().getCapDvi());
        if (isCapCuc) {
            List<XhHopDongBttDtl> proposalDetailsList = xhHopDongBttDtlRepository.findAllByIdHdr(proposalData.getId());
            for (XhHopDongBttDtl proposalDetail : proposalDetailsList) {
                xhHopDongBttDviRepository.deleteAllByIdDtl(proposalDetail.getId());
            }
            xhHopDongBttDtlRepository.deleteAllByIdHdr(proposalData.getId());
        } else {
            xhHopDongBttDviRepository.deleteAllByIdHdr(proposalData.getId());
        }
        xhHopDongBttHdrRepository.delete(proposalData);
        this.updateRelatedEntityHdChuaKy(proposalData, isCapCuc);
    }

    @Transactional
    public XhHopDongBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhHopDongBttHdr proposal = xhHopDongBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhHopDongBttHdr updatedData = xhHopDongBttHdrRepository.save(proposal);
        boolean isCapCuc = Contains.CAP_CUC.equals(SecurityContextService.getUser().getCapDvi());
        this.updateRelatedEntityHdDaKy(updatedData, isCapCuc);
        this.updateRelatedEntityHdChuaKy(updatedData, isCapCuc);
        if (!isCapCuc && updatedData.getIdQdNv() != null && updatedData.getSoHopDong() != null) {
            xhQdNvXhBttHdrRepository.findById(updatedData.getIdQdNv()).ifPresent(tasks -> {
                String soHopDong = tasks.getSoHopDong() != null ? (", " + updatedData.getSoHopDong()) : updatedData.getSoHopDong();
                xhQdNvXhBttHdrRepository.updateSoHopDong(soHopDong, updatedData.getIdQdNv());
            });
        }
        return updatedData;
    }

    private void updateRelatedEntityHdDaKy(XhHopDongBttHdr createdRecord, Boolean isCheckRequired) {
        if (isCheckRequired && !DataUtils.isNullObject(createdRecord.getIdQdKq())) {
            xhKqBttHdrRepository.findById(createdRecord.getIdQdKq()).ifPresent(relatedEntity -> {
                Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyCuc(createdRecord.getIdQdKq());
                relatedEntity.setSlHdDaKy(slHdongDaKy);
                xhKqBttHdrRepository.save(relatedEntity);
            });
        } else if (!isCheckRequired && !DataUtils.isNullObject(createdRecord.getIdChaoGia())) {
            xhQdPdKhBttDtlRepository.findById(createdRecord.getIdChaoGia()).ifPresent(relatedEntity -> {
                Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyChiCuc(createdRecord.getIdChaoGia());
                relatedEntity.setSlHdDaKy(slHdongDaKy);
                xhQdPdKhBttDtlRepository.save(relatedEntity);
            });
        }
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null || requestParams == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bantructiep/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhHopDongBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}