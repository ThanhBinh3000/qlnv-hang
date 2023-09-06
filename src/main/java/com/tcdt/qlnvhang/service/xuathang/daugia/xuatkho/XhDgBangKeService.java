package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBangKeReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhDgBangKeService extends BaseServiceImpl {


    @Autowired
    private XhDgBangKeHdrRepository xhDgBangKeHdrRepository;
    @Autowired
    private XhDgBangKeDtlRepository xhDgBangKeDtlRepository;

    @Autowired
    private XhDgPhieuXuatKhoRepository xhDgPhieuXuatKhoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhDgBangKeHdr> searchPage(CustomUserDetails currentUser, SearchXhDgBangKeReq req) throws Exception {
        req.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgBangKeHdr> search = xhDgBangKeHdrRepository.search(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            if (mapDmucDvi.containsKey((s.getMaDvi()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
                s.setTenDvi(objDonVi.get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(s.getMaDiemKho())) {
                s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(s.getMaNhaKho())) {
                s.setTenNhaKho(mapDmucDvi.get(s.getMaNhaKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(s.getMaNganKho())) {
                s.setTenNganKho(mapDmucDvi.get(s.getMaNganKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(s.getMaLoKho())) {
                s.setTenLoKho(mapDmucDvi.get(s.getMaLoKho()).get("tenDvi").toString());
            }
            if (mapVthh.get((s.getLoaiVthh())) != null) {
                s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
            }
            if (mapVthh.get((s.getCloaiVthh())) != null) {
                s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
            }
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhDgBangKeHdr save(CustomUserDetails currentUser, XhDgBangKeReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhDgBangKeHdr> optional = xhDgBangKeHdrRepository.findBySoBangKe(objReq.getSoBangKe());
        if (optional.isPresent()) {
            throw new Exception("số biên bản đã tồn tại");
        }
        XhDgBangKeHdr data = new XhDgBangKeHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setId(DataUtils.safeToLong(data.getSoBangKe().split("/")[0]));
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.setNguoiGduyetId(currentUser.getUser().getId());
        XhDgBangKeHdr created = xhDgBangKeHdrRepository.save(data);
        //dtl
        data.getBangKeDtl().forEach(s -> {
            s.setIdHdr(created.getId());
        });
        xhDgBangKeDtlRepository.saveAll(data.getBangKeDtl());
        return created;
    }

    @Transactional
    public XhDgBangKeHdr update(CustomUserDetails currentUser, XhDgBangKeReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhDgBangKeHdr> optional = xhDgBangKeHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhDgBangKeHdr> soDx = xhDgBangKeHdrRepository.findBySoBangKe(objReq.getSoBangKe());
        if (soDx.isPresent()) {
            if (!soDx.get().getId().equals(objReq.getId())) {
                throw new Exception("số biên bản đã tồn tại");
            }
        }
        XhDgBangKeHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        XhDgBangKeHdr created = xhDgBangKeHdrRepository.save(data);
        xhDgBangKeDtlRepository.deleteAllByIdHdr(created.getId());
        data.getBangKeDtl().forEach(s -> {
            s.setIdHdr(created.getId());
        });
        xhDgBangKeDtlRepository.saveAll(data.getBangKeDtl());
        return created;
    }


    public List<XhDgBangKeHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhDgBangKeHdr> allById = xhDgBangKeHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(allById)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        allById.forEach(data -> {
            if (mapDmucDvi.containsKey(data.getMaDvi())) {
                //lay ten chi cuc nen lui lai 1 cap
                String maChiCuc = data.getMaDvi().substring(0, 8);
                data.setTenDvi(mapDmucDvi.get(DataUtils.safeToString(maChiCuc)).get("tenDvi").toString());
                data.setDiaChiDvi(mapDmucDvi.get(data.getMaDvi()).get("diaChi").toString());
            }
            if (mapDmucDvi.containsKey(data.getMaDiemKho())) {
                data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(data.getMaNhaKho())) {
                data.setTenNhaKho(mapDmucDvi.get(data.getMaNhaKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(data.getMaNganKho())) {
                data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(data.getMaLoKho())) {
                data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()).get("tenDvi").toString());
            }
            if (data.getNguoiPduyetId() != null) {
                data.setNguoiPduyet(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
            }
            if (data.getNguoiGduyetId() != null) {
                data.setNguoiGduyet(ObjectUtils.isEmpty(data.getNguoiGduyetId()) ? null : userInfoRepository.findById(data.getNguoiGduyetId()).get().getFullName());
            }
            data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

            List<XhDgBangKeDtl> allDtl = xhDgBangKeDtlRepository.findAllByIdHdr(data.getId());
            data.setBangKeDtl(allDtl);
        });
        return allById;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (Objects.isNull(idSearchReq.getId())) {
            throw new Exception("Bad request");
        }

        Optional<XhDgBangKeHdr> optional = xhDgBangKeHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }

        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(optional.get().getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }
        List<XhDgBangKeDtl> bangKeDtl = xhDgBangKeDtlRepository.findAllByIdHdr(optional.get().getId());
        if (bangKeDtl != null && bangKeDtl.size() > 0) {
            xhDgBangKeDtlRepository.deleteAll(bangKeDtl);
        }
        xhDgBangKeHdrRepository.delete(optional.get());

    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {

    }

    @Transient
    public XhDgBangKeHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhDgBangKeHdr> optional = xhDgBangKeHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                optional.get().setNgayGduyet(LocalDate.now());
                break;
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
        optional.get().setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            Optional<XhDgPhieuXuatKho> xhDgPhieuXuatKho = xhDgPhieuXuatKhoRepository.findById(optional.get().getIdPhieuXuatKho());
            if (xhDgPhieuXuatKho.isPresent()) {
                xhDgPhieuXuatKho.get().setSoBangKeCh(optional.get().getSoBangKe());
                xhDgPhieuXuatKho.get().setIdBangKeCh(optional.get().getId());
                xhDgPhieuXuatKhoRepository.save(xhDgPhieuXuatKho.get());
            }
        }
        XhDgBangKeHdr created = xhDgBangKeHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchXhDgBangKeReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhDgBangKeHdr> page = this.searchPage(currentUser, objReq);
        List<XhDgBangKeHdr> data = page.getContent();

        String title = "Danh sách phiếu xuất kho ";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho", "Lô kho", "Số bảng kê", "Số phiếu xuất kho", "ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bang-ke-can-hang.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhDgBangKeHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdGiaoNvXh();
            objs[2] = dx.getNam();
            objs[3] = dx.getNgayQdGiaoNvXh();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho() + '-' + dx.getTenNganKho();
            objs[6] = dx.getSoBangKe();
            objs[7] = dx.getSoPhieuXuatKho();
            objs[8] = dx.getNgayXuat();
            objs[9] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        try {
            FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bandaugia/Bảng kê cân hàng bán đấu giá.docx");
            List<XhDgBangKeHdr> listDetail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            XhDgBangKeHdr detail = listDetail.get(0);
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}