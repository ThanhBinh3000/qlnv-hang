package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
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

    public Page<XhTcTtinBdgHdr> searchPage(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String capDvi = currentUser.getUser().getCapDvi();
        req.setDvql(capDvi.equals(Contains.CAP_CUC) ? dvql : capDvi.equals(Contains.CAP_TONG_CUC) ? dvql.substring(0, 4) : null);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTcTtinBdgHdr> search = xhTcTtinBdgHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhTcTtinBdgHdr create(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhTcTtinBdgHdr data = new XhTcTtinBdgHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DANG_THUC_HIEN);
        Long maThongBao = Long.valueOf(req.getMaThongBao().split("/")[0]);
        data.setId(maThongBao);
        Optional<XhQdPdKhBdgDtl> optional = xhQdPdKhBdgDtlRepository.findById(data.getIdQdPdDtl());
        XhQdPdKhBdgDtl xhQdPdKhBdgDtl = optional.orElseThrow(()
                -> new Exception("Không tìm thấy Quyết định phê duyệt kế hoạch bán đấu giá"));
        xhQdPdKhBdgDtl.setTrangThai(Contains.DANG_THUC_HIEN);
        xhQdPdKhBdgDtlRepository.save(xhQdPdKhBdgDtl);
        List<XhTcTtinBdgHdr> byIdQdPdDtl = xhTcTtinBdgHdrRepository.findByIdQdPdDtlOrderByLanDauGia(data.getIdQdPdDtl());
        data.setLanDauGia(byIdQdPdDtl.size() + 1);
        XhTcTtinBdgHdr created = xhTcTtinBdgHdrRepository.save(data);
        this.saveDetail(req, created.getId(), false);
        return created;
    }

    void saveDetail(ThongTinDauGiaReq req, Long idHdr, Boolean check) {
        if (check) {
            xhTcTtinBdgDtlRepository.deleteAllByIdHdr(idHdr);
        }
        for (ThongTinDauGiaDtlReq dtlReq : req.getChildren()) {
            XhTcTtinBdgDtl dtl = new XhTcTtinBdgDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhTcTtinBdgDtlRepository.save(dtl);
            if (check) {
                xhTcTtinBdgPloRepository.deleteAllByIdDtl(dtlReq.getId());
            }
            for (ThongTinDauGiaPloReq ploReq : dtlReq.getChildren()) {
                XhTcTtinBdgPlo ploDtl = new XhTcTtinBdgPlo();
                BeanUtils.copyProperties(ploReq, ploDtl, "id");
                ploDtl.setIdDtl(dtl.getId());
                ploDtl.setId(null);
                if (req.getKetQua().equals(0) && check) {
                    ploDtl.setSoLanTraGia(null);
                    ploDtl.setDonGiaTraGia(null);
                    ploDtl.setToChucCaNhan(null);
                }
                xhTcTtinBdgPloRepository.save(ploDtl);
            }
        }

        if (check) {
            xhTcTtinBdgNlqRepository.deleteAllByIdHdr(idHdr);
        }
        if (req.getKetQua().equals(1)) {
            for (ThongTinDauGiaNtgReq nlqReq : req.getListNguoiTgia()) {
                XhTcTtinBdgNlq nlq = new XhTcTtinBdgNlq();
                BeanUtils.copyProperties(nlqReq, nlq, "id");
                nlq.setId(null);
                nlq.setIdHdr(idHdr);
                xhTcTtinBdgNlqRepository.save(nlq);
            }
        }
    }

    @Transactional
    public XhTcTtinBdgHdr update(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhTcTtinBdgHdr data = xhTcTtinBdgHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhTcTtinBdgHdr updated = xhTcTtinBdgHdrRepository.save(data);
        this.saveDetail(req, updated.getId(), true);
        return updated;
    }

    public List<XhTcTtinBdgHdr> detail(List<Long> ids) throws Exception {
        List<XhTcTtinBdgHdr> list = xhTcTtinBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        for (XhTcTtinBdgHdr data : list) {
            List<XhTcTtinBdgDtl> listDtl = xhTcTtinBdgDtlRepository.findAllByIdHdr(data.getId());
            for (XhTcTtinBdgDtl dataDtl : listDtl) {
                List<XhTcTtinBdgPlo> listPhanLo = xhTcTtinBdgPloRepository.findAllByIdDtl(dataDtl.getId());
                for (XhTcTtinBdgPlo dataPhanLo : listPhanLo) {
                    dataPhanLo.setTenDiemKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaDiemKho(), null));
                    dataPhanLo.setTenNhaKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaNhaKho(), null));
                    dataPhanLo.setTenNganKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaNganKho(), null));
                    dataPhanLo.setTenLoKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaLoKho(), null));
                    Optional<BigDecimal> donGiaOptional = Optional.ofNullable(dataPhanLo.getDonGiaTraGia());
                    Optional<BigDecimal> soLuongOptional = Optional.ofNullable(dataPhanLo.getSoLuongDeXuat());
                    dataPhanLo.setThanhTien(donGiaOptional.flatMap(donGia -> soLuongOptional.map(soLuong -> donGia.multiply(soLuong))).orElse(null));
                }
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                BigDecimal sumThanhTien = listPhanLo.stream().map(XhTcTtinBdgPlo::getThanhTien).filter(Objects::nonNull).reduce((a, b) -> a.add(b)).orElse(null);
                BigDecimal sumDonGiaDeXuat = listPhanLo.stream().map(XhTcTtinBdgPlo::getDonGiaDeXuat).filter(Objects::nonNull).reduce((a, b) -> a.add(b)).orElse(null);
                dataDtl.setThanhTien(sumThanhTien);
                dataDtl.setDonGiaDeXuat(sumDonGiaDeXuat);
                dataDtl.setChildren(listPhanLo);
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
            BigDecimal sumTongTien = listDtl.stream().map(XhTcTtinBdgDtl::getThanhTien).filter(Objects::nonNull).reduce((a, b) -> a.add(b)).orElse(null);
            data.setTongTien(sumTongTien);
            data.setChildren(listDtl);
            data.setListNguoiTgia(xhTcTtinBdgNlqRepository.findAllByIdHdr(data.getId()));
        }
        return list;
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
        XhTcTtinBdgHdr data = xhTcTtinBdgHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!data.getTrangThai().equals(Contains.DANG_THUC_HIEN)) {
            throw new Exception("Chỉ thực hiện xóa bản ghi ở chưa cập nhập");
        }
        List<XhTcTtinBdgDtl> listDtl = xhTcTtinBdgDtlRepository.findAllByIdHdr(data.getId());
        for (XhTcTtinBdgDtl dtl : listDtl) {
            xhTcTtinBdgPloRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhTcTtinBdgDtlRepository.deleteAllByIdHdr(data.getId());
        xhTcTtinBdgNlqRepository.deleteAllByIdHdr(data.getId());
        xhTcTtinBdgHdrRepository.delete(data);
    }

    @Transactional
    public XhTcTtinBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTcTtinBdgHdr data = xhTcTtinBdgHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setTrangThai(statusReq.getTrangThai());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        XhTcTtinBdgHdr created = xhTcTtinBdgHdrRepository.save(data);
        if (statusReq.getTrangThai().equals(Contains.DA_HOAN_THANH)) {
            updateDauGiaDetails(data);
        }
        return created;
    }

    private void updateDauGiaDetails(XhTcTtinBdgHdr data) {
        Optional<XhQdPdKhBdgDtl> dauGia = xhQdPdKhBdgDtlRepository.findById(data.getIdQdPdDtl());
        dauGia.ifPresent(dauGiaDtl -> {
            int slDviTsan = dauGiaDtl.getSlDviTsan();
            BigDecimal soDviTsanThanhCong = xhQdPdKhBdgDtlRepository.countSlDviTsanThanhCong(data.getIdQdPdDtl(), data.getLoaiVthh(), data.getMaDvi());
            BigDecimal soDviTsanKhongThanh = new BigDecimal(slDviTsan).subtract(soDviTsanThanhCong);
            dauGiaDtl.setSoDviTsanThanhCong(soDviTsanThanhCong);
            dauGiaDtl.setSoDviTsanKhongThanh(soDviTsanKhongThanh);
            dauGiaDtl.setKetQuaDauGia(dauGiaDtl.getSoDviTsanThanhCong() + "/" + slDviTsan);
            xhQdPdKhBdgDtlRepository.save(dauGiaDtl);
        });
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bandaugia/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhTcTtinBdgHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            detail.setTenDvi(detail.getTenDvi().toUpperCase());
            detail.setTenCloaiVthh(detail.getTenCloaiVthh().toUpperCase());
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}