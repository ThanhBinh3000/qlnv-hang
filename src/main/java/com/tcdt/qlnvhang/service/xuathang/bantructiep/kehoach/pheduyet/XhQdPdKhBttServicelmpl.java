package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaInfoReq;
import com.tcdt.qlnvhang.request.getGiaDuocDuyet;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrReq;
import com.tcdt.qlnvhang.response.chotdulieu.QthtChotGiaInfoRes;
import com.tcdt.qlnvhang.service.chotdulieu.QthtChotGiaNhapXuatService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhQdPdKhBttServicelmpl extends BaseServiceImpl {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;
    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;
    @Autowired
    private XhTcTtinBttServiceImpl xhTcTtinBttServiceImpl;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private QthtChotGiaNhapXuatService qthtChotGiaNhapXuatService;

    public Page<XhQdPdKhBttHdr> searchPage(CustomUserDetails currentUser, XhQdPdKhBttHdrReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setMaCuc(currentUser.getDvql());
            request.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhQdPdKhBttHdr> searchResultPage = xhQdPdKhBttHdrRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
                if (data.getTrangThai().equals(Contains.BAN_HANH) && data.getType().equals("QDDC")) {
                    List<XhQdPdKhBttDtl> detailList = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
                    QthtChotGiaInfoReq objReq = new QthtChotGiaInfoReq();
                    objReq.setLoaiGia("LG04");
                    objReq.setNam(data.getNamKh());
                    objReq.setLoaiVthh(data.getLoaiVthh());
                    objReq.setCloaiVthh(data.getCloaiVthh());
                    objReq.setMaCucs(detailList.stream().map(XhQdPdKhBttDtl::getMaDvi).collect(Collectors.toList()));
                    objReq.setIdQuyetDinhCanDieuChinh(data.getId());
                    objReq.setType("XUAT_TRUC_TIEP");
                    QthtChotGiaInfoRes qthtChotGiaInfoRes = qthtChotGiaNhapXuatService.thongTinChotDieuChinhGia(objReq);
                    data.setQthtChotGiaInfoRes(qthtChotGiaInfoRes);
                }

            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }

    @Transactional
    public XhQdPdKhBttHdr create(CustomUserDetails currentUser, XhQdPdKhBttHdrReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if ("QDKH".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdPd()) && xhQdPdKhBttHdrRepository.existsBySoQdPd(request.getSoQdPd())) {
            throw new Exception("Số quyết định " + request.getSoQdPd() + " đã tồn tại");
        }
        if ("QDDC".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdDc()) && xhQdPdKhBttHdrRepository.existsBySoQdDc(request.getSoQdDc())) {
            throw new Exception("Số quyết định điều chỉnh " + request.getSoQdDc() + " đã tồn tại");
        }
        XhQdPdKhBttHdr newData = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setLastest(false);
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        if ("QDKH".equals(newData.getType())) {
            newData.setTrangThai(Contains.DU_THAO);
            newData.setLanDieuChinh(Integer.valueOf(0));
        }
        if ("QDDC".equals(newData.getType())) {
            newData.setTrangThai(Contains.DA_LAP);
            long occurrenceCount = xhQdPdKhBttHdrRepository.countBySoQdPdAndType(newData.getSoQdPd(), newData.getType());
            newData.setLanDieuChinh(Integer.valueOf((int) (occurrenceCount + 1)));
            int uniqueMaDviTsanCount = request.getChildren().stream()
                    .flatMap(item -> item.getChildren().stream())
                    .flatMap(child -> child.getChildren().stream())
                    .map(XhQdPdKhBttDviDtlReq::getMaDviTsan)
                    .collect(Collectors.toSet())
                    .size();
            newData.setSlDviTsan(uniqueMaDviTsanCount);
        }
        XhQdPdKhBttHdr createdRecord = xhQdPdKhBttHdrRepository.save(newData);
        if (!DataUtils.isNullOrEmpty(request.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(request.getCanCuPhapLy(), createdRecord.getId(), XhQdPdKhBttHdr.TABLE_NAME);
            createdRecord.setCanCuPhapLy(canCuPhapLy);
        }
        if (!DataUtils.isNullOrEmpty(request.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKem(), createdRecord.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
            createdRecord.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(request.getFileDinhKemDc())) {
            List<FileDinhKem> fileDinhKemDc = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKemDc(), createdRecord.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_DIEU_CHINH");
            createdRecord.setFileDinhKemDc(fileDinhKemDc);
        }
        if ("QDKH".equals(newData.getType()) && "TH".equals(newData.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(createdRecord.getIdThHdr()).ifPresent(summary -> {
                summary.setIdSoQdPd(createdRecord.getId());
                summary.setSoQdPd(createdRecord.getSoQdPd());
                summary.setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBttRepository.save(summary);
            });
        }
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhQdPdKhBttHdr update(CustomUserDetails currentUser, XhQdPdKhBttHdrReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        if ("QDKH".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdPd()) && xhQdPdKhBttHdrRepository.existsBySoQdPdAndIdNot(request.getSoQdPd(), request.getId())) {
            throw new Exception("Số quyết định " + request.getSoQdPd() + " đã tồn tại");
        }
        if ("QDDC".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdDc()) && xhQdPdKhBttHdrRepository.existsBySoQdDcAndIdNot(request.getSoQdDc(), request.getId())) {
            throw new Exception("Số quyết định điều chỉnh " + request.getSoQdDc() + " đã tồn tại");
        }
        XhQdPdKhBttHdr existingData = xhQdPdKhBttHdrRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "lastest", "lanDieuChinh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhQdPdKhBttHdr updatedData = xhQdPdKhBttHdrRepository.save(existingData);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.delete(updatedData.getId(), tableNames);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(request.getCanCuPhapLy(), updatedData.getId(), XhQdPdKhBttHdr.TABLE_NAME);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKem(), updatedData.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        List<FileDinhKem> fileDinhKemDc = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKemDc(), updatedData.getId(), XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        updatedData.setCanCuPhapLy(canCuPhapLy);
        updatedData.setFileDinhKem(fileDinhKem);
        updatedData.setFileDinhKemDc(fileDinhKemDc);
        if ("QDKH".equals(updatedData.getType()) && "TH".equals(updatedData.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(updatedData.getIdThHdr()).ifPresent(summary -> {
                summary.setIdSoQdPd(updatedData.getId());
                summary.setSoQdPd(updatedData.getSoQdPd());
                summary.setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBttRepository.save(summary);
            });
        }
        if ("QDDC".equals(updatedData.getType())) {
            int uniqueMaDviTsanCount = request.getChildren().stream()
                    .flatMap(item -> item.getChildren().stream())
                    .flatMap(child -> child.getChildren().stream())
                    .map(XhQdPdKhBttDviDtlReq::getMaDviTsan)
                    .collect(Collectors.toSet())
                    .size();
            updatedData.setSlDviTsan(uniqueMaDviTsanCount);
        }
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhQdPdKhBttHdrReq request, Long headerId, Boolean isCheckRequired) {
        xhQdPdKhBttDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhQdPdKhBttDtlReq detailRequest : request.getChildren()) {
            XhQdPdKhBttDtl detail = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            detail.setNamKh(request.getNamKh());
            detail.setSoQdDc(request.getSoQdDc());
            detail.setSoQdPd(request.getSoQdPd());
            detail.setTrangThai(Contains.CHUA_THUC_HIEN);
            detail.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
            detail.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
            if ("QDDC".equals(request.getType())) {
                detail.setPthucBanTrucTiep(null);
                detail.setNgayNhanCgia(null);
                detail.setIdQdKq(null);
                detail.setSoQdKq(null);
                detail.setDiaDiemChaoGia(null);
                detail.setGhiChuChaoGia(null);
                detail.setSlHdDaKy(null);
                detail.setSlHdChuaKy(null);
                detail.setIdQdNv(null);
                detail.setSoQdNv(null);
            }
            xhQdPdKhBttDtlRepository.save(detail);
            xhQdPdKhBttDviRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (XhQdPdKhBttDviReq donViReq : detailRequest.getChildren()) {
                XhQdPdKhBttDvi donVi = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(donViReq, donVi, "id");
                donVi.setIdDtl(detail.getId());
                donVi.setIsKetQua(false);
                xhQdPdKhBttDviRepository.save(donVi);
                xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(isCheckRequired ? donViReq.getId() : null);
                for (XhQdPdKhBttDviDtlReq detailDonVIRequest : donViReq.getChildren()) {
                    XhQdPdKhBttDviDtl detailDonVi = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(detailDonVIRequest, detailDonVi, "id");
                    detailDonVi.setId(null);
                    detailDonVi.setIdDvi(donVi.getId());
                    xhQdPdKhBttDviDtlRepository.save(detailDonVi);
                }
            }
        }
    }

    public BigDecimal getGiaDuocDuyet(getGiaDuocDuyet request) {
        if (request == null) {
            return BigDecimal.ZERO;
        }
        Long longNamKh = request.getNam() != null ? request.getNam().longValue() : null;
        if (!Contains.LOAI_VTHH_VATTU.equals(request.getTypeLoaiVthh()) && request.getMaDvi() != null) {
            return xhQdPdKhBttDviDtlRepository.getGiaDuocDuyetLuongThuc(
                    request.getCloaiVthh(),
                    request.getLoaiVthh(),
                    longNamKh,
                    request.getMaDvi());
        }
        return xhQdPdKhBttDviDtlRepository.getGiaDuocDuyetVatTu(
                request.getCloaiVthh(),
                request.getLoaiVthh(),
                longNamKh);
    }

    public List<XhQdPdKhBttHdr> detail(List<Long> ids) throws Exception {
        // chốt điều chỉnh giá
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttHdr> resultList = xhQdPdKhBttHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucThanhToan = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhQdPdKhBttHdr item : resultList) {
            List<XhQdPdKhBttDtl> detailList = xhQdPdKhBttDtlRepository.findAllByIdHdr(item.getId());
            for (XhQdPdKhBttDtl detailItem : detailList) {
                List<XhQdPdKhBttDvi> subCategoryList = xhQdPdKhBttDviRepository.findAllByIdDtl(detailItem.getId());
                for (XhQdPdKhBttDvi subCategoryItem : subCategoryList) {
                    List<XhQdPdKhBttDviDtl> subCategoryDetailList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(subCategoryItem.getId());
                    subCategoryDetailList.forEach(subCategoryDetailItem -> {
                        subCategoryDetailItem.setMapDmucDvi(mapDmucDvi);
                        subCategoryDetailItem.setMapDmucVthh(mapDmucVthh);
                        this.calculateGiaDuocDuyet(subCategoryDetailItem, subCategoryItem, detailItem, item, subCategoryDetailList, subCategoryList, detailList);
                    });
                    subCategoryItem.setMapDmucDvi(mapDmucDvi);
                    subCategoryItem.setChildren(subCategoryDetailList);
                }
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setMapDmucVthh(mapDmucVthh);
                detailItem.setMapDmucThanhToan(mapDmucThanhToan);
                item.setDonViTinh(detailItem.getDonViTinh());
                detailItem.setChildren(subCategoryList.stream().filter(type -> !type.getIsKetQua()).collect(Collectors.toList()));
            }
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(item.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME));
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(item.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH"));
            List<FileDinhKem> fileDinhKemDc = fileDinhKemService.search(item.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH"));
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucLoaiXuat(mapDmucLoaiXuat);
            item.setMapDmucKieuXuat(mapDmucKieuXuat);
            item.setTrangThai(item.getTrangThai());
            item.setCanCuPhapLy(canCuPhapLy);
            item.setFileDinhKem(fileDinhKem);
            item.setFileDinhKemDc(fileDinhKemDc);
            item.setChildren(detailList);
            if (item.getTrangThai().equals(Contains.BAN_HANH)) {
                QthtChotGiaInfoReq objReq = new QthtChotGiaInfoReq();
                objReq.setLoaiGia("LG04");
                objReq.setNam(item.getNamKh());
                objReq.setLoaiVthh(item.getLoaiVthh());
                objReq.setCloaiVthh(item.getCloaiVthh());
                objReq.setMaCucs(item.getChildren().stream().map(XhQdPdKhBttDtl::getMaDvi).collect(Collectors.toList()));
                objReq.setIdQuyetDinhCanDieuChinh(item.getId());
                objReq.setType("XUAT_TRUC_TIEP");
                QthtChotGiaInfoRes qthtChotGiaInfoRes = qthtChotGiaNhapXuatService.thongTinChotDieuChinhGia(objReq);
                item.setQthtChotGiaInfoRes(qthtChotGiaInfoRes);
            }
        }
        return resultList;
    }

    private BigDecimal calculateGiaDuocDuyet(XhQdPdKhBttDviDtl subCategoryDetailItem, XhQdPdKhBttDvi subCategoryItem, XhQdPdKhBttDtl detailItem, XhQdPdKhBttHdr item, List<XhQdPdKhBttDviDtl> subCategoryDetailList, List<XhQdPdKhBttDvi> subCategoryList, List<XhQdPdKhBttDtl> detailList) {
        BigDecimal giaDuocDuyet = BigDecimal.ZERO;
        if (subCategoryDetailItem.getDonGiaDuocDuyet() == null || subCategoryDetailItem.getDonGiaDuocDuyet().compareTo(BigDecimal.ZERO) == 0 && "QDKH".equals(item.getType())) {
            Long longNamKh = item.getNamKh() != null ? item.getNamKh().longValue() : null;
            if (subCategoryDetailItem.getLoaiVthh() != null && longNamKh != null && subCategoryDetailItem.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU)) {
                giaDuocDuyet = xhQdPdKhBttDviDtlRepository.getGiaDuocDuyetVatTu(
                        subCategoryDetailItem.getCloaiVthh(),
                        subCategoryDetailItem.getLoaiVthh(),
                        longNamKh);
            } else if (subCategoryDetailItem.getCloaiVthh() != null && subCategoryDetailItem.getLoaiVthh() != null && longNamKh != null && subCategoryItem.getMaDvi() != null) {
                giaDuocDuyet = xhQdPdKhBttDviDtlRepository.getGiaDuocDuyetLuongThuc(
                        subCategoryDetailItem.getCloaiVthh(),
                        subCategoryDetailItem.getLoaiVthh(),
                        longNamKh,
                        subCategoryItem.getMaDvi());
            }
            subCategoryDetailItem.setDonGiaDuocDuyet(giaDuocDuyet);
            Optional<BigDecimal> optionalGia = Optional.ofNullable(giaDuocDuyet);
            subCategoryDetailItem.setThanhTienDuocDuyet(subCategoryDetailItem.getSoLuongDeXuat().multiply(optionalGia.orElse(BigDecimal.ZERO)));

            if (giaDuocDuyet != null && giaDuocDuyet.compareTo(BigDecimal.ZERO) != 0 && "QDKH".equals(item.getType())) {
                BigDecimal sumTienDuocDuyet = subCategoryDetailList.stream()
                        .map(XhQdPdKhBttDviDtl::getThanhTienDuocDuyet)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                subCategoryItem.setTienDuocDuyet(sumTienDuocDuyet);
                BigDecimal sumThanhTienDuocDuyet = subCategoryList.stream()
                        .map(XhQdPdKhBttDvi::getTienDuocDuyet)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                detailItem.setThanhTienDuocDuyet(sumThanhTienDuocDuyet);
            }
        }
        BigDecimal sumTongSoTienCuc = detailList.stream()
                .map(XhQdPdKhBttDtl::getTongSoLuong)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setTongSoLuongCuc(sumTongSoTienCuc);
        BigDecimal sumTongTienCuc = detailList.stream()
                .map(XhQdPdKhBttDtl::getThanhTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        item.setTongTienCuc(sumTongTienCuc);
        return giaDuocDuyet;
    }

    public XhQdPdKhBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttHdr proposalData = xhQdPdKhBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatusList = Arrays.asList(Contains.DU_THAO, Contains.DA_LAP, Contains.TUCHOI_LDV, Contains.TUCHOI_LDTC);
        if (!allowedStatusList.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        List<XhQdPdKhBttDtl> proposalDetailsList = xhQdPdKhBttDtlRepository.findAllByIdHdr(proposalData.getId());
        proposalDetailsList.forEach(proposalDetai -> {
            List<XhQdPdKhBttDvi> proposalDonViList = xhQdPdKhBttDviRepository.findAllByIdDtl(proposalDetai.getId());
            proposalDonViList.forEach(proposalDonVi -> xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(proposalDonVi.getId()));
            xhQdPdKhBttDviRepository.deleteAllByIdDtl(proposalDetai.getId());
        });
        xhQdPdKhBttDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhQdPdKhBttHdrRepository.delete(proposalData);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.delete(proposalData.getId(), tableNames);
        if ("QDKH".equals(proposalData.getType()) && "TH".equals(proposalData.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(proposalData.getIdThHdr()).ifPresent(summary -> {
                summary.setIdSoQdPd(null);
                summary.setSoQdPd(null);
                summary.setTrangThai(Contains.CHUATAO_QD);
                xhThopDxKhBttRepository.save(summary);
            });
        }
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhQdPdKhBttHdr> proposalList = xhQdPdKhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Không tìm thấy bản ghi để xóa");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DUTHAO) ||
                        proposal.getTrangThai().equals(Contains.DA_LAP) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_CBV) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDTC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhQdPdKhBttHdr::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDtl> proposalDetailsList = xhQdPdKhBttDtlRepository.findByIdHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhQdPdKhBttDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDvi> proposalDonViList = xhQdPdKhBttDviRepository.findByIdDtlIn(detailIds);
        List<Long> proposalPhanLoIds = proposalDonViList.stream().map(XhQdPdKhBttDvi::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDviDtl> proposalDonViDetailsList = xhQdPdKhBttDviDtlRepository.findByIdDviIn(proposalPhanLoIds);
        xhQdPdKhBttDviDtlRepository.deleteAll(proposalDonViDetailsList);
        xhQdPdKhBttDviRepository.deleteAll(proposalDonViList);
        xhQdPdKhBttDtlRepository.deleteAll(proposalDetailsList);
        xhQdPdKhBttHdrRepository.deleteAll(proposalList);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), tableNames);
        List<String> listType = proposalList.stream().map(XhQdPdKhBttHdr::getType).collect(Collectors.toList());
        if (listType.contains("QDKH")) {
            List<Long> childIds = proposalList.stream().map(XhQdPdKhBttHdr::getIdThHdr).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(childIds)) {
                List<XhThopDxKhBttHdr> summaryList = xhThopDxKhBttRepository.findByIdIn(childIds);
                summaryList.forEach(summary -> {
                    summary.setIdSoQdPd(null);
                    summary.setSoQdPd(null);
                    summary.setTrangThai(Contains.CHUATAO_QD);
                });
                xhThopDxKhBttRepository.saveAll(summaryList);
            }
        }
    }

    public XhQdPdKhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttHdr proposal = xhQdPdKhBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        if ("QDKH".equals(proposal.getType())) {
            this.handleQDKHApproval(currentUser, statusReq, proposal, statusCombination);
        } else if ("QDDC".equals(proposal.getType())) {
            this.handleQDDCApproval(currentUser, statusReq, proposal, statusCombination);
        } else {
            throw new Exception("Loại dữ liệu không được hỗ trợ");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        return xhQdPdKhBttHdrRepository.save(proposal);
    }

    private void handleQDKHApproval(CustomUserDetails currentUser, StatusReq statusReq, XhQdPdKhBttHdr proposal, String statusCombination) throws Exception {
        switch (statusCombination) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            proposal.setLastest(true);
            this.handleBanHanhSpecifics(proposal);
        }
    }

    private void handleBanHanhSpecifics(XhQdPdKhBttHdr proposal) throws Exception {
        if ("TH".equals(proposal.getPhanLoai())) {
            XhThopDxKhBttHdr summary = xhThopDxKhBttRepository.findById(proposal.getIdThHdr())
                    .orElseThrow(() -> new Exception("Tổng hợp đề xuất kế hoạch không tồn tại"));
            if (summary.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Tổng hợp đề xuất kế hoạch đã được quyết định");
            }
            summary.setTrangThai(Contains.DABANHANH_QD);
            List<XhDxKhBanTrucTiepHdr> childList = xhDxKhBanTrucTiepHdrRepository.findAllByIdThop(summary.getId());
            childList.forEach(child -> {
                child.setIdSoQdPd(proposal.getId());
                child.setSoQdPd(proposal.getSoQdPd());
                child.setNgayKyQd(proposal.getNgayKyQd());
                child.setTrangThaiTh(Contains.DABANHANH_QD);
            });
            xhDxKhBanTrucTiepHdrRepository.saveAll(childList);
            xhThopDxKhBttRepository.save(summary);
        }
        if ("TTr".equals(proposal.getPhanLoai())) {
            XhDxKhBanTrucTiepHdr child = xhDxKhBanTrucTiepHdrRepository.findById(proposal.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Đề xuất kế hoạch không tồn tại"));
            if (child.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Đề xuất kế hoạch này đã được quyết định");
            }
            child.setIdSoQdPd(proposal.getId());
            child.setSoQdPd(proposal.getSoQdPd());
            child.setNgayKyQd(proposal.getNgayKyQd());
            child.setTrangThaiTh(Contains.DABANHANH_QD);
            xhDxKhBanTrucTiepHdrRepository.save(child);
        }
    }

    private void handleQDDCApproval(CustomUserDetails currentUser, StatusReq statusReq, XhQdPdKhBttHdr proposal, String statusCombination) throws Exception {
        switch (statusCombination) {
            case Contains.CHODUYET_LDV + Contains.DA_LAP:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
                proposal.setNguoiGuiDuyetId(currentUser.getUser().getId());
                proposal.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.DADUYET_LDV:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                proposal.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.BAN_HANH + Contains.DADUYET_LDV:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            proposal.setLastest(true);
            xhQdPdKhBttHdrRepository.findById(proposal.getIdGoc()).ifPresent(proposalDc -> {
                proposalDc.setLastest(false);
                xhQdPdKhBttHdrRepository.save(proposalDc);
            });
        }
    }

    public void export(CustomUserDetails currentUser, XhQdPdKhBttHdrReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBttHdr> page = this.searchPage(currentUser, request);
        List<XhQdPdKhBttHdr> dataList = page.getContent();
        boolean isQdPdType = dataList.stream().anyMatch(item -> item.getType().equals("QDKH"));
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        if (isQdPdType) {
            this.exportQdPdType(response, dataList, isVattuType);
        } else {
            this.exportQdDcType(response, dataList, isVattuType);
        }
    }

    public void exportQdPdType(HttpServletResponse response, List<XhQdPdKhBttHdr> dataList, boolean isVattuType) throws Exception {
        String title = "Danh sách quyết định phê duyệt kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BTT", "Ngày ký QĐ", "Trích yếu", "Số công văn/tờ trình", "Mã tổng hợp"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Số ĐV tài sản";
            vattuRowsName[10] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 3);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Số ĐV tài sản";
            nonVattuRowsName[9] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ke-hoach-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getSoQdPd();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayKyQd());
            excelRow[4] = proposal.getTrichYeu();
            excelRow[5] = proposal.getSoTrHdr();
            excelRow[6] = proposal.getIdThHdr();
            if (isVattuType) {
                excelRow[7] = proposal.getTenLoaiVthh();
                excelRow[8] = proposal.getTenCloaiVthh();
                excelRow[9] = proposal.getSlDviTsan();
                excelRow[10] = proposal.getTenTrangThai();
            } else {
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getSlDviTsan();
                excelRow[9] = proposal.getTenTrangThai();
            }
            excelDataList.add(excelRow);
        }
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public void exportQdDcType(HttpServletResponse response, List<XhQdPdKhBttHdr> dataList, boolean isVattuType) throws Exception {
        String title = "Danh sách điều chỉnh quyết định phê duyệt kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Năm quyết định", "Số QĐ điều chỉnh KH bán trực tiếp", "Ngày ký QĐ điều chỉnh", "Số quyết định gốc", "Trích yếu"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            vattuRowsName[6] = "Loại hàng DTQG";
            vattuRowsName[7] = "Chủng loại hàng DTQG";
            vattuRowsName[8] = "Số ĐV tài sản";
            vattuRowsName[9] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 3);
            nonVattuRowsName[6] = "Chủng loại hàng DTQG";
            nonVattuRowsName[7] = "Số ĐV tài sản";
            nonVattuRowsName[8] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-dieu-chinh-quyet-dinh-phe-duyet-ke-hoach-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBttHdr proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNamKh();
            excelRow[2] = proposal.getSoQdDc();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayKyDc());
            excelRow[4] = proposal.getSoQdPd();
            excelRow[5] = proposal.getTrichYeu();
            if (isVattuType) {
                excelRow[6] = proposal.getTenLoaiVthh();
                excelRow[7] = proposal.getTenCloaiVthh();
                excelRow[8] = proposal.getSlDviTsan();
                excelRow[9] = proposal.getTenTrangThai();
            } else {
                excelRow[6] = proposal.getTenCloaiVthh();
                excelRow[7] = proposal.getSlDviTsan();
                excelRow[8] = proposal.getTenTrangThai();
            }
            excelDataList.add(excelRow);
        }
        ExportExcel exportExcel = new ExportExcel(title, fileName, rowsName, excelDataList, response);
        exportExcel.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null || requestParams == null) {
            throw new Exception("Bad request.");
        }
        boolean userLevel = Contains.CAP_TONG_CUC.equals(currentUser.getUser().getCapDvi());
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bantructiep/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhQdPdKhBttHdr reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            if (userLevel) {
                return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
            } else {
                XhQdPdKhBttDtl proposalDetail = null;
                if (reportDetail.getId() != null) {
                    List<XhQdPdKhBttDtl> filteredChildren = reportDetail.getChildren()
                            .stream()
                            .filter(type -> type.getMaDvi().equals(currentUser.getDvql()))
                            .collect(Collectors.toList());
                    if (!filteredChildren.isEmpty()) {
                        proposalDetail = xhTcTtinBttServiceImpl.detail(filteredChildren.get(0).getId());
                    }
                }
                if (proposalDetail != null) {
                    return docxToPdfConverter.convertDocxToPdf(templateInputStream, proposalDetail);
                }
            }
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }

//    private void cloneProject(Long idClone) throws Exception {
//        XhQdPdKhBttHdr hdr = this.detail(idClone);
//        XhQdPdKhBttHdr hdrClone = new XhQdPdKhBttHdr();
//        BeanUtils.copyProperties(hdr, hdrClone);
//        hdrClone.setId(null);
//        hdrClone.setLastest(true);
//        hdrClone.setIdGoc(hdr.getId());
//        xhQdPdKhBttHdrRepository.save(hdrClone);
//        for (XhQdPdKhBttDtl dtl : hdr.getChildren()) {
//            XhQdPdKhBttDtl dtlClone = new XhQdPdKhBttDtl();
//            BeanUtils.copyProperties(dtl, dtlClone);
//            dtlClone.setId(null);
//            dtlClone.setLastest(true);
//            dtlClone.setIdHdr(hdrClone.getId());
//            xhQdPdKhBttDtlRepository.save(dtlClone);
//            for (XhQdPdKhBttDvi dvi : dtlClone.getChildren()) {
//                XhQdPdKhBttDvi dviClone = new XhQdPdKhBttDvi();
//                BeanUtils.copyProperties(dvi, dviClone);
//                dviClone.setId(null);
//                dviClone.setIdDtl(dtlClone.getId());
//                xhQdPdKhBttDviRepository.save(dviClone);
//                for (XhQdPdKhBttDviDtl dviDtl : dvi.getChildren()) {
//                    XhQdPdKhBttDviDtl dviDtlClone = new XhQdPdKhBttDviDtl();
//                    BeanUtils.copyProperties(dviDtl, dviDtlClone);
//                    dviDtlClone.setId(null);
//                    dviDtlClone.setIdDvi(dviClone.getId());
//                    xhQdPdKhBttDviDtlRepository.save(dviDtlClone);
//                }
//            }
//        }
//    }
}