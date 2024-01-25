package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaInfoReq;
import com.tcdt.qlnvhang.request.getGiaDuocDuyet;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import com.tcdt.qlnvhang.response.chotdulieu.QthtChotGiaInfoRes;
import com.tcdt.qlnvhang.service.chotdulieu.QthtChotGiaNhapXuatService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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
public class XhQdPdKhBdgServiceImpl extends BaseServiceImpl {
    @Autowired
    private XhQdPdKhBdgRepository xhQdPdKhBdgRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;
    @Autowired
    private XhQdPdKhBdgPlRepository xhQdPdKhBdgPlRepository;
    @Autowired
    private XhQdPdKhBdgPlDtlRepository xhQdPdKhBdgPlDtlRepository;
    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;
    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private QthtChotGiaNhapXuatService qthtChotGiaNhapXuatService;

    public Page<XhQdPdKhBdg> searchPage(CustomUserDetails currentUser, XhQdPdKhBdgReq request) throws Exception {
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            request.setDvql(currentUser.getDvql());
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setMaCuc(currentUser.getDvql());
            request.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhQdPdKhBdg> searchResultPage = xhQdPdKhBdgRepository.searchPage(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
                if (data.getTrangThai().equals(Contains.BAN_HANH) && data.getType().equals("QDDC")) {
                    List<XhQdPdKhBdgDtl> detailList = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
                    QthtChotGiaInfoReq objReq = new QthtChotGiaInfoReq();
                    objReq.setLoaiGia("LG04");
                    objReq.setNam(data.getNam());
                    objReq.setLoaiVthh(data.getLoaiVthh());
                    objReq.setCloaiVthh(data.getCloaiVthh());
                    objReq.setMaCucs(detailList.stream().map(XhQdPdKhBdgDtl::getMaDvi).collect(Collectors.toList()));
                    objReq.setIdQuyetDinhCanDieuChinh(data.getId());
                    objReq.setType("XUAT_DAU_GIA");
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
    public XhQdPdKhBdg create(CustomUserDetails currentUser, XhQdPdKhBdgReq request) throws Exception {
        if (currentUser == null || request == null) {
            throw new Exception("Bad request.");
        }
        if ("QDKH".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdPd()) && xhQdPdKhBdgRepository.existsBySoQdPd(request.getSoQdPd())) {
            throw new Exception("Số quyết định " + request.getSoQdPd() + " đã tồn tại");
        }
        if ("QDDC".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdDc()) && xhQdPdKhBdgRepository.existsBySoQdDc(request.getSoQdDc())) {
            throw new Exception("Số quyết định điều chỉnh " + request.getSoQdDc() + " đã tồn tại");
        }
        XhQdPdKhBdg newData = new XhQdPdKhBdg();
        BeanUtils.copyProperties(request, newData);
        newData.setMaDvi(currentUser.getDvql());
        newData.setLastest(false);
        newData.setNgayTao(LocalDate.now());
        newData.setNguoiTaoId(currentUser.getUser().getId());
        if ("QDKH".equals(newData.getType())) {
            newData.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
            newData.setLanDieuChinh(Integer.valueOf(0));
        }
        if ("QDDC".equals(newData.getType())) {
            newData.setTrangThai(Contains.DA_LAP);
            long occurrenceCount = xhQdPdKhBdgRepository.countBySoQdPdAndType(newData.getSoQdPd(), newData.getType());
            newData.setLanDieuChinh(Integer.valueOf((int) (occurrenceCount + 1)));
        }
        XhQdPdKhBdg createdRecord = xhQdPdKhBdgRepository.save(newData);
        if (!DataUtils.isNullOrEmpty(request.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(request.getCanCuPhapLy(), createdRecord.getId(), XhQdPdKhBdg.TABLE_NAME);
            createdRecord.setCanCuPhapLy(canCuPhapLy);
        }
        if (!DataUtils.isNullOrEmpty(request.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKem(), createdRecord.getId(), XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
            createdRecord.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(request.getFileDinhKemDc())) {
            List<FileDinhKem> fileDinhKemDc = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKemDc(), createdRecord.getId(), XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
            createdRecord.setFileDinhKemDc(fileDinhKemDc);
        }
        if ("QDKH".equals(newData.getType()) && "TH".equals(newData.getPhanLoai())) {
            xhThopDxKhBdgRepository.findById(createdRecord.getIdThHdr()).ifPresent(summary -> {
                summary.setIdQdPd(createdRecord.getId());
                summary.setSoQdPd(createdRecord.getSoQdPd());
                summary.setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBdgRepository.save(summary);
            });
        }
        this.saveDetail(request, createdRecord.getId(), false);
        return createdRecord;
    }

    @Transactional
    public XhQdPdKhBdg update(CustomUserDetails currentUser, XhQdPdKhBdgReq request) throws Exception {
        if (currentUser == null || request == null || request.getId() == null) {
            throw new Exception("Bad request.");
        }
        if ("QDKH".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdPd()) && xhQdPdKhBdgRepository.existsBySoQdPdAndIdNot(request.getSoQdPd(), request.getId())) {
            throw new Exception("Số quyết định " + request.getSoQdPd() + " đã tồn tại");
        }
        if ("QDDC".equals(request.getType()) && !StringUtils.isEmpty(request.getSoQdDc()) && xhQdPdKhBdgRepository.existsBySoQdDcAndIdNot(request.getSoQdDc(), request.getId())) {
            throw new Exception("Số quyết định điều chỉnh " + request.getSoQdDc() + " đã tồn tại");
        }
        XhQdPdKhBdg existingData = xhQdPdKhBdgRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(request, existingData, "id", "maDvi", "lastest", "lanDieuChinh");
        existingData.setNgaySua(LocalDate.now());
        existingData.setNguoiSuaId(currentUser.getUser().getId());
        XhQdPdKhBdg updatedData = xhQdPdKhBdgRepository.save(existingData);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBdg.TABLE_NAME);
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.delete(updatedData.getId(), tableNames);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(request.getCanCuPhapLy(), updatedData.getId(), XhQdPdKhBdg.TABLE_NAME);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKem(), updatedData.getId(), XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        List<FileDinhKem> fileDinhKemDc = fileDinhKemService.saveListFileDinhKem(request.getFileDinhKemDc(), updatedData.getId(), XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        updatedData.setCanCuPhapLy(canCuPhapLy);
        updatedData.setFileDinhKem(fileDinhKem);
        updatedData.setFileDinhKemDc(fileDinhKemDc);
        if ("QDKH".equals(updatedData.getType()) && "TH".equals(updatedData.getPhanLoai())) {
            xhThopDxKhBdgRepository.findById(updatedData.getIdThHdr()).ifPresent(summary -> {
                summary.setIdQdPd(updatedData.getId());
                summary.setSoQdPd(updatedData.getSoQdPd());
                summary.setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBdgRepository.save(summary);
            });
        }
        this.saveDetail(request, updatedData.getId(), true);
        return updatedData;
    }

    void saveDetail(XhQdPdKhBdgReq request, Long headerId, Boolean isCheckRequired) {
        xhQdPdKhBdgDtlRepository.deleteAllByIdHdr(isCheckRequired ? headerId : null);
        for (XhQdPdKhBdgDtlReq detailRequest : request.getChildren()) {
            XhQdPdKhBdgDtl detail = new XhQdPdKhBdgDtl();
            BeanUtils.copyProperties(detailRequest, detail, "id");
            detail.setIdHdr(headerId);
            detail.setNam(request.getNam());
            detail.setSoQdDc(request.getSoQdDc());
            detail.setSoQdPd(request.getSoQdPd());
            detail.setTrangThai(Contains.CHUA_THUC_HIEN);
            xhQdPdKhBdgDtlRepository.save(detail);
            xhQdPdKhBdgPlRepository.deleteAllByIdDtl(isCheckRequired ? detailRequest.getId() : null);
            for (XhQdPdKhBdgPlReq phanLoReq : detailRequest.getChildren()) {
                XhQdPdKhBdgPl phanLo = new XhQdPdKhBdgPl();
                BeanUtils.copyProperties(phanLoReq, phanLo, "id");
                phanLo.setIdDtl(detail.getId());
                xhQdPdKhBdgPlRepository.save(phanLo);
                xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(isCheckRequired ? phanLoReq.getId() : null);
                for (XhQdPdKhBdgPlDtlReq detailPhanLoReq : phanLoReq.getChildren()) {
                    XhQdPdKhBdgPlDtl detailPhanLo = new XhQdPdKhBdgPlDtl();
                    BeanUtils.copyProperties(detailPhanLoReq, detailPhanLo, "id");
                    detailPhanLo.setId(null);
                    detailPhanLo.setIdPhanLo(phanLo.getId());
                    xhQdPdKhBdgPlDtlRepository.save(detailPhanLo);
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
            return xhQdPdKhBdgPlDtlRepository.getGiaDuocDuyetLuongThuc(
                    request.getCloaiVthh(),
                    request.getLoaiVthh(),
                    longNamKh,
                    request.getMaDvi());
        }
        return xhQdPdKhBdgPlDtlRepository.getGiaDuocDuyetVatTu(
                request.getCloaiVthh(),
                request.getLoaiVthh(),
                longNamKh);
    }

    public List<XhQdPdKhBdg> detail(List<Long> ids) throws Exception {
        // Chốt điều chỉnh giá
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBdg> resultList = xhQdPdKhBdgRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucThanhToan = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhQdPdKhBdg item : resultList) {
            List<XhQdPdKhBdgDtl> detailList = xhQdPdKhBdgDtlRepository.findAllByIdHdr(item.getId());
            for (XhQdPdKhBdgDtl detailItem : detailList) {
                List<XhQdPdKhBdgPl> subCategoryList = xhQdPdKhBdgPlRepository.findAllByIdDtl(detailItem.getId());
                for (XhQdPdKhBdgPl subCategoryItem : subCategoryList) {
                    List<XhQdPdKhBdgPlDtl> subCategoryDetailList = xhQdPdKhBdgPlDtlRepository.findAllByIdPhanLo(subCategoryItem.getId());
                    subCategoryDetailList.forEach(subCategoryDetailItem -> {
                        subCategoryDetailItem.setMapDmucDvi(mapDmucDvi);
                        subCategoryDetailItem.setMapDmucVthh(mapDmucVthh);
                        this.calculateGiaDuocDuyet(subCategoryDetailItem, subCategoryItem, detailItem, item, subCategoryDetailList, subCategoryList);
                    });
                    subCategoryItem.setMapDmucDvi(mapDmucDvi);
                    subCategoryItem.setChildren(subCategoryDetailList);
                }
                detailItem.setMapDmucDvi(mapDmucDvi);
                detailItem.setMapDmucVthh(mapDmucVthh);
                detailItem.setMapDmucThanhToan(mapDmucThanhToan);
                detailItem.setTrangThai(detailItem.getTrangThai());
                detailItem.setChildren(subCategoryList);
            }
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(item.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME));
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(item.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH"));
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
                objReq.setNam(item.getNam());
                objReq.setLoaiVthh(item.getLoaiVthh());
                objReq.setCloaiVthh(item.getCloaiVthh());
                objReq.setMaCucs(item.getChildren().stream().map(XhQdPdKhBdgDtl::getMaDvi).collect(Collectors.toList()));
                objReq.setIdQuyetDinhCanDieuChinh(item.getId());
                objReq.setType("XUAT_DAU_GIA");
                QthtChotGiaInfoRes qthtChotGiaInfoRes = qthtChotGiaNhapXuatService.thongTinChotDieuChinhGia(objReq);
                item.setQthtChotGiaInfoRes(qthtChotGiaInfoRes);
            }
        }
        return resultList;
    }

    private BigDecimal calculateGiaDuocDuyet(XhQdPdKhBdgPlDtl subCategoryDetailItem, XhQdPdKhBdgPl subCategoryItem, XhQdPdKhBdgDtl subDetailItem, XhQdPdKhBdg item, List<XhQdPdKhBdgPlDtl> subCategoryDetailList, List<XhQdPdKhBdgPl> subCategoryList) {
        BigDecimal giaDuocDuyet = BigDecimal.ZERO;
        if (subCategoryDetailItem.getDonGiaDuocDuyet() == null || subCategoryDetailItem.getDonGiaDuocDuyet().compareTo(BigDecimal.ZERO) == 0 && "QDKH".equals(item.getType())) {
            Long longNamKh = subDetailItem.getNam() != null ? subDetailItem.getNam().longValue() : null;
            if (subCategoryDetailItem.getLoaiVthh() != null && longNamKh != null && subCategoryDetailItem.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU)) {
                giaDuocDuyet = xhQdPdKhBdgPlDtlRepository.getGiaDuocDuyetVatTu(
                        subCategoryDetailItem.getCloaiVthh(),
                        subCategoryDetailItem.getLoaiVthh(),
                        longNamKh);
            } else if (subCategoryDetailItem.getCloaiVthh() != null && subCategoryDetailItem.getLoaiVthh() != null && longNamKh != null && subCategoryItem.getMaDvi() != null) {
                giaDuocDuyet = xhQdPdKhBdgPlDtlRepository.getGiaDuocDuyetLuongThuc(
                        subCategoryDetailItem.getCloaiVthh(),
                        subCategoryDetailItem.getLoaiVthh(),
                        longNamKh,
                        subCategoryItem.getMaDvi());
            }
            subCategoryDetailItem.setDonGiaDuocDuyet(giaDuocDuyet);
            Optional<BigDecimal> optionalGia = Optional.ofNullable(giaDuocDuyet);
            BigDecimal thanhTienDuocDuyet = subCategoryDetailItem.getSoLuongDeXuat().multiply(optionalGia.orElse(BigDecimal.ZERO));
            BigDecimal tienDatTruocDuocDuyet = thanhTienDuocDuyet.multiply(subDetailItem.getKhoanTienDatTruoc().divide(BigDecimal.valueOf(100)));
            subCategoryDetailItem.setThanhTienDuocDuyet(thanhTienDuocDuyet);
            subCategoryDetailItem.setTienDatTruocDuocDuyet(tienDatTruocDuocDuyet);
            if (giaDuocDuyet != null && giaDuocDuyet.compareTo(BigDecimal.ZERO) != 0 && "QDKH".equals(item.getType())) {
                BigDecimal sumSoTienDuocDuyet = subCategoryDetailList.stream()
                        .map(XhQdPdKhBdgPlDtl::getThanhTienDuocDuyet)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal sumSoTienDtruocDduyet = subCategoryDetailList.stream()
                        .map(XhQdPdKhBdgPlDtl::getTienDatTruocDuocDuyet)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                subCategoryItem.setSoTienDuocDuyet(sumSoTienDuocDuyet);
                subCategoryItem.setSoTienDtruocDduyet(sumSoTienDtruocDduyet);
                BigDecimal sumTongTienDuocDuyet = subCategoryList.stream()
                        .map(XhQdPdKhBdgPl::getSoTienDuocDuyet)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal sumTongKtienDtruocDduyet = subCategoryList.stream()
                        .map(XhQdPdKhBdgPl::getSoTienDtruocDduyet)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                subDetailItem.setTongTienDuocDuyet(sumTongTienDuocDuyet);
                subDetailItem.setTongKtienDtruocDduyet(sumTongKtienDtruocDduyet);
            }
        }
        return giaDuocDuyet;
    }

    public XhQdPdKhBdg detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBdg> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBdg proposalData = xhQdPdKhBdgRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatusList = Arrays.asList(Contains.DANG_NHAP_DU_LIEU, Contains.DA_LAP, Contains.TUCHOI_LDV, Contains.TUCHOI_LDTC);
        if (!allowedStatusList.contains(proposalData.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        List<XhQdPdKhBdgDtl> proposalDetailsList = xhQdPdKhBdgDtlRepository.findAllByIdHdr(proposalData.getId());
        proposalDetailsList.forEach(proposalDetai -> {
            List<XhQdPdKhBdgPl> proposalPhanLoList = xhQdPdKhBdgPlRepository.findAllByIdDtl(proposalDetai.getId());
            proposalPhanLoList.forEach(proposalPhanLo -> xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(proposalPhanLo.getId()));
            xhQdPdKhBdgPlRepository.deleteAllByIdDtl(proposalDetai.getId());
        });
        xhQdPdKhBdgDtlRepository.deleteAllByIdHdr(proposalData.getId());
        xhQdPdKhBdgRepository.delete(proposalData);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBdg.TABLE_NAME);
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.delete(proposalData.getId(), tableNames);
        if ("QDKH".equals(proposalData.getType()) && "TH".equals(proposalData.getPhanLoai())) {
            xhThopDxKhBdgRepository.findById(proposalData.getIdThHdr()).ifPresent(summary -> {
                summary.setIdQdPd(null);
                summary.setSoQdPd(null);
                summary.setTrangThai(Contains.CHUATAO_QD);
                xhThopDxKhBdgRepository.save(summary);
            });
        }
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq == null || idSearchReq.getIdList() == null) {
            throw new Exception("Bad request.");
        }
        List<XhQdPdKhBdg> proposalList = xhQdPdKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        if (proposalList.isEmpty()) {
            throw new Exception("Không tìm thấy bản ghi để xóa");
        }
        boolean isValidToDelete = proposalList.stream().allMatch(proposal ->
                proposal.getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU) ||
                        proposal.getTrangThai().equals(Contains.DA_LAP) ||
                        proposal.getTrangThai().equals(Contains.TU_CHOI_CBV) ||
                        proposal.getTrangThai().equals(Contains.TUCHOI_LDTC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> proposalIds = proposalList.stream().map(XhQdPdKhBdg::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgDtl> proposalDetailsList = xhQdPdKhBdgDtlRepository.findByIdHdrIn(proposalIds);
        List<Long> detailIds = proposalDetailsList.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> proposalPhanLoList = xhQdPdKhBdgPlRepository.findByIdDtlIn(detailIds);
        List<Long> proposalPhanLoIds = proposalPhanLoList.stream().map(XhQdPdKhBdgPl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPlDtl> proposalPhanLoDetailsList = xhQdPdKhBdgPlDtlRepository.findByIdPhanLoIn(proposalPhanLoIds);
        xhQdPdKhBdgPlDtlRepository.deleteAll(proposalPhanLoDetailsList);
        xhQdPdKhBdgPlRepository.deleteAll(proposalPhanLoList);
        xhQdPdKhBdgDtlRepository.deleteAll(proposalDetailsList);
        xhQdPdKhBdgRepository.deleteAll(proposalList);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBdg.TABLE_NAME);
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), tableNames);
        List<String> listType = proposalList.stream().map(XhQdPdKhBdg::getType).collect(Collectors.toList());
        if (listType.contains("QDKH")) {
            List<Long> childIds = proposalList.stream().map(XhQdPdKhBdg::getIdThHdr).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(childIds)) {
                List<XhThopDxKhBdg> summaryList = xhThopDxKhBdgRepository.findByIdIn(childIds);
                summaryList.forEach(summary -> {
                    summary.setIdQdPd(null);
                    summary.setSoQdPd(null);
                    summary.setTrangThai(Contains.CHUATAO_QD);
                });
                xhThopDxKhBdgRepository.saveAll(summaryList);
            }
        }
    }

    public XhQdPdKhBdg approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBdg proposal = xhQdPdKhBdgRepository.findById(Long.valueOf(statusReq.getId()))
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
        return xhQdPdKhBdgRepository.save(proposal);
    }

    private void handleQDKHApproval(CustomUserDetails currentUser, StatusReq statusReq, XhQdPdKhBdg proposal, String statusCombination) throws Exception {
        switch (statusCombination) {
            case Contains.BAN_HANH + Contains.DANG_NHAP_DU_LIEU:
                proposal.setNguoiPduyetId(currentUser.getUser().getId());
                proposal.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            proposal.setLastest(true);
            this.handleBanHanhStatus(proposal);
        }
    }

    private void handleBanHanhStatus(XhQdPdKhBdg proposal) throws Exception {
        if ("TH".equals(proposal.getPhanLoai())) {
            XhThopDxKhBdg summary = xhThopDxKhBdgRepository.findById(proposal.getIdThHdr())
                    .orElseThrow(() -> new Exception("Tổng hợp đề xuất kế hoạch không tồn tại"));
            if (summary.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Tổng hợp đề xuất kế hoạch đã được quyết định");
            }
            summary.setTrangThai(Contains.DABANHANH_QD);
            List<XhDxKhBanDauGia> childList = xhDxKhBanDauGiaRepository.findAllByIdThop(summary.getId());
            childList.forEach(child -> {
                child.setIdSoQdPd(proposal.getId());
                child.setSoQdPd(proposal.getSoQdPd());
                child.setNgayKyQd(proposal.getNgayKyQd());
                child.setTrangThaiTh(Contains.DABANHANH_QD);
            });
            xhDxKhBanDauGiaRepository.saveAll(childList);
            xhThopDxKhBdgRepository.save(summary);
        }
        if ("TTr".equals(proposal.getPhanLoai())) {
            XhDxKhBanDauGia child = xhDxKhBanDauGiaRepository.findById(proposal.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Đề xuất kế hoạch không tồn tại"));
            if (child.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Đề xuất kế hoạch này đã được quyết định");
            }
            child.setIdSoQdPd(proposal.getId());
            child.setSoQdPd(proposal.getSoQdPd());
            child.setNgayKyQd(proposal.getNgayKyQd());
            child.setTrangThaiTh(Contains.DABANHANH_QD);
            xhDxKhBanDauGiaRepository.save(child);
        }
    }

    private void handleQDDCApproval(CustomUserDetails currentUser, StatusReq statusReq, XhQdPdKhBdg proposal, String statusCombination) throws Exception {
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
            xhQdPdKhBdgRepository.findById(proposal.getIdGoc()).ifPresent(proposalDc -> {
                proposalDc.setLastest(false);
                xhQdPdKhBdgRepository.save(proposalDc);
            });
        }
    }

    public void export(CustomUserDetails currentUser, XhQdPdKhBdgReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBdg> page = this.searchPage(currentUser, request);
        List<XhQdPdKhBdg> dataList = page.getContent();
        boolean isQdPdType = dataList.stream().anyMatch(item -> item.getType().equals("QDKH"));
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        if (isQdPdType) {
            this.exportQdPdType(response, dataList, isVattuType);
        } else {
            this.exportQdDcType(response, dataList, isVattuType);
        }
    }

    public void exportQdPdType(HttpServletResponse response, List<XhQdPdKhBdg> dataList, boolean isVattuType) throws Exception {
        String title = "Danh sách quyết định phê duyệt kế hoạch bán đấu giá hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BĐG", "Ngày ký QĐ", "Trích yếu", "Số công văn/tờ trình", "Mã tổng hợp"};
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
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ke-hoach-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBdg proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNam();
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

    public void exportQdDcType(HttpServletResponse response, List<XhQdPdKhBdg> dataList, boolean isVattuType) throws Exception {
        String title = "Danh sách điều chỉnh quyết định phê duyệt kế hoạch bán đấu giá hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Năm KH", "Số QĐ điều chỉnh KH BĐG", "Ngày ký QĐ điều chỉnh", "Số QĐ cần điều chỉnh", "Số QĐ công văn/tờ trình", "Trích yếu"};
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
        String fileName = "danh-sach-dieu-chinh-quyet-dinh-phe-duyet-ke-hoach-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBdg proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNam();
            excelRow[2] = proposal.getSoQdDc();
            excelRow[3] = LocalDateTimeUtils.localDateToString(proposal.getNgayKyDc());
            excelRow[4] = proposal.getSoQdPd();
            excelRow[5] = proposal.getSoCongVan();
            excelRow[6] = proposal.getTrichYeu();
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

    public Page<XhQdPdKhBdgDtl> searchPageDtl(CustomUserDetails currentUser, XhQdPdKhBdgDtlReq request) throws Exception {
        request.setLastest(Integer.valueOf(1));
        request.setTrangThaiHdr(Contains.BAN_HANH);
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            request.setDvql(currentUser.getDvql());
        }
        Pageable pageable = PageRequest.of(request.getPaggingReq().getPage(), request.getPaggingReq().getLimit());
        Page<XhQdPdKhBdgDtl> searchResultPage = xhQdPdKhBdgDtlRepository.searchDtl(request, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        searchResultPage.getContent().forEach(data -> {
            try {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setTrangThai(data.getTrangThai());
                data.setXhQdPdKhBdg(xhQdPdKhBdgRepository.findById(data.getIdHdr()).orElse(null));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        return searchResultPage;
    }


    public List<XhQdPdKhBdgDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBdgDtl> resultList = xhQdPdKhBdgDtlRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(resultList)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucLoaiXuat = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucKieuXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucThanhToan = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhQdPdKhBdgDtl item : resultList) {
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setMapDmucDvi(mapDmucDvi);
            item.setMapDmucVthh(mapDmucVthh);
            item.setMapDmucThanhToan(mapDmucThanhToan);
            item.setTrangThai(item.getTrangThai());
            List<XhQdPdKhBdgPl> detailList = xhQdPdKhBdgPlRepository.findAllByIdDtl(item.getId());
            for (XhQdPdKhBdgPl detailItem : detailList) {
                detailItem.setMapDmucDvi(mapDmucDvi);
                List<XhQdPdKhBdgPlDtl> subCategoryList = xhQdPdKhBdgPlDtlRepository.findAllByIdPhanLo(detailItem.getId());
                subCategoryList.forEach(dataPhanLoDtl -> {
                    dataPhanLoDtl.setMapDmucDvi(mapDmucDvi);
                    dataPhanLoDtl.setMapDmucVthh(mapDmucVthh);
                });
                detailItem.setChildren(subCategoryList);
            }
            List<XhTcTtinBdgHdr> biddingInfoList = xhTcTtinBdgHdrRepository.findAllBySoQdPd(item.getSoQdPd());
            for (XhTcTtinBdgHdr biddingInfo : biddingInfoList) {
                biddingInfo.setMapDmucDvi(mapDmucDvi);
                biddingInfo.setMapDmucVthh(mapDmucVthh);
                biddingInfo.setTrangThai(biddingInfo.getTrangThai());
            }
            item.setListTtinDg(biddingInfoList);
            item.setChildren(detailList);
            xhQdPdKhBdgRepository.findById(item.getIdHdr()).ifPresent(data -> {
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapDmucVthh(mapDmucVthh);
                data.setMapDmucLoaiXuat(mapDmucLoaiXuat);
                data.setMapDmucKieuXuat(mapDmucKieuXuat);
                data.setTrangThai(data.getTrangThai());
                item.setXhQdPdKhBdg(data);
            });
        }
        return resultList;
    }

    public XhQdPdKhBdgDtl approveDtl(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBdgDtl proposal = xhQdPdKhBdgDtlRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String statusCombination = statusReq.getTrangThai() + proposal.getTrangThai();
        if (!statusCombination.equals(Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN)) {
            throw new Exception("Phê duyệt không thành công");
        }
        proposal.setTrangThai(statusReq.getTrangThai());
        return xhQdPdKhBdgDtlRepository.save(proposal);
    }

    public void exportDtl(CustomUserDetails currentUser, XhQdPdKhBdgDtlReq request, HttpServletResponse response) throws Exception {
        request.getPaggingReq().setPage(0);
        request.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBdgDtl> page = this.searchPageDtl(currentUser, request);
        List<XhQdPdKhBdgDtl> dataList = page.getContent();
        String title = "Danh sách quản lý thông tin bán đấu giá hàng DTQG";
        String[] rowsName;
        boolean isVattuType = dataList.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KHBĐG", "Số QĐ ĐC KHBĐG", "Số công văn/tờ trình", "Số QĐ PD KQBĐG", "Ngày QĐ PD KQBĐG", "Tổng số ĐV tài sản", "Số ĐV tài sản đấu giá thành công", "Số ĐV tài sản đấu giá không thành"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            vattuRowsName[10] = "Loại hàng DTQG";
            vattuRowsName[11] = "Chủng loại hàng DTQG";
            vattuRowsName[12] = "Trạng thái thực hiện";
            vattuRowsName[13] = "Kết quả đấu giá";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 3);
            nonVattuRowsName[10] = "Chủng loại hàng DTQG";
            nonVattuRowsName[11] = "Trạng thái thực hiện";
            nonVattuRowsName[12] = "Kết quả đấu giá";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-quan-ly-thong-tin-ban-dau-gia-hang-DTQG.xlsx";
        List<Object[]> excelDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            XhQdPdKhBdgDtl proposal = dataList.get(i);
            Object[] excelRow = new Object[rowsName.length];
            excelRow[0] = i;
            excelRow[1] = proposal.getNam();
            excelRow[2] = proposal.getSoQdPd();
            excelRow[3] = proposal.getSoQdDc();
            excelRow[4] = proposal.getSoDxuat();
            excelRow[5] = proposal.getSoQdKq();
            excelRow[6] = LocalDateTimeUtils.localDateToString(proposal.getNgayKyQdKq());
            excelRow[7] = proposal.getSlDviTsan();
            excelRow[8] = proposal.getSoDviTsanThanhCong();
            excelRow[9] = proposal.getSoDviTsanKhongThanh();
            if (isVattuType) {
                excelRow[10] = proposal.getTenLoaiVthh();
                excelRow[11] = proposal.getTenCloaiVthh();
                excelRow[12] = proposal.getTenTrangThai();
                excelRow[13] = proposal.getKetQuaDauGia();
            } else {
                excelRow[10] = proposal.getTenCloaiVthh();
                excelRow[11] = proposal.getTenTrangThai();
                excelRow[12] = proposal.getKetQuaDauGia();
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
        try {
            String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
            String templatePath = "bandaugia/" + templateName;
            FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
            XhQdPdKhBdg reportDetail = this.detail(DataUtils.safeToLong(requestParams.get("id")));
            reportDetail.setTenCloaiVthh(reportDetail.getTenCloaiVthh().toUpperCase());
            for (XhQdPdKhBdgDtl report : reportDetail.getChildren()) {
                report.setTenDvi(report.getTenDvi().toUpperCase());
            }
            return docxToPdfConverter.convertDocxToPdf(templateInputStream, reportDetail);
        } catch (IOException | XDocReportException exception) {
            exception.printStackTrace();
        }
        return null;
    }

//    private void cloneProject(Long idClone) throws Exception {
//        XhQdPdKhBdg hdr = this.detail(idClone);
//        XhQdPdKhBdg hdrClone = new XhQdPdKhBdg();
//        BeanUtils.copyProperties(hdr, hdrClone);
//        hdrClone.setId(null);
//        hdrClone.setLastest(true);
//        hdrClone.setLanDieuChinh(Integer.valueOf(1));
//        hdrClone.setIdGoc(hdr.getId());
//        xhQdPdKhBdgRepository.save(hdrClone);
//        for (XhQdPdKhBdgDtl dx : hdr.getChildren()) {
//            XhQdPdKhBdgDtl dxClone = new XhQdPdKhBdgDtl();
//            BeanUtils.copyProperties(dx, dxClone);
//            dxClone.setId(null);
//            dxClone.setIdHdr(hdrClone.getId());
//            xhQdPdKhBdgDtlRepository.save(dxClone);
//            for (XhQdPdKhBdgPl pl : dxClone.getChildren()) {
//                XhQdPdKhBdgPl plClone = new XhQdPdKhBdgPl();
//                BeanUtils.copyProperties(pl, plClone);
//                plClone.setId(null);
//                plClone.setIdDtl(dxClone.getId());
//                xhQdPdKhBdgPlRepository.save(plClone);
//                for (XhQdPdKhBdgPlDtl plDtl : pl.getChildren()) {
//                    XhQdPdKhBdgPlDtl plDtlClone = new XhQdPdKhBdgPlDtl();
//                    BeanUtils.copyProperties(plDtl, plDtlClone);
//                    plDtlClone.setId(null);
//                    plDtlClone.setIdPhanLo(plClone.getId());
//                    xhQdPdKhBdgPlDtlRepository.save(plDtlClone);
//                }
//            }
//        }
//    }
}