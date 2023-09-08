package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBbTinhKho;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
import java.util.stream.Collectors;

@Service
public class XhDgBbTinhKhoService extends BaseServiceImpl {


    @Autowired
    private XhDgBbTinhKhoHdrRepository xhDgBbTinhKhoHdrRepository;

    @Autowired
    private XhDgBbTinhKhoDtlRepository xhDgBbTinhKhoDtlRepository;

    @Autowired
    private XhPhieuKnghiemCluongRepository xhPhieuKnghiemCluongRepository;

    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhDgBbTinhKhoHdr> searchPage(CustomUserDetails currentUser, SearchXhDgBbTinhKho req) throws Exception {
        req.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgBbTinhKhoHdr> search = xhDgBbTinhKhoHdrRepository.search(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(f -> {
            if (mapDmucDvi.containsKey((f.getMaDvi()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(f.getMaDvi());
                f.setTenDvi(objDonVi.get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(f.getMaDiemKho())) {
                f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(f.getMaNhaKho())) {
                f.setTenNhaKho(mapDmucDvi.get(f.getMaNhaKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(f.getMaNganKho())) {
                f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(f.getMaLoKho())) {
                f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()).get("tenDvi").toString());
            }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setListPhieuXuatKho(xhDgBbTinhKhoDtlRepository.findAllByIdHdr(f.getId()));
        });
        return search;
    }

    @Transactional
    public XhDgBbTinhKhoHdr save(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhDgBbTinhKhoHdr> optional = xhDgBbTinhKhoHdrRepository.findBySoBbTinhKho(objReq.getSoBbTinhKho());
        if (optional.isPresent()) {
            throw new Exception("số biên bản đã tồn tại");
        }
        XhDgBbTinhKhoHdr data = new XhDgBbTinhKhoHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setIdThuKho(currentUser.getUser().getId());
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        XhDgBbTinhKhoHdr created = xhDgBbTinhKhoHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhDgBbTinhKhoHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(created.getId(), objReq);
        return created;
    }

    @Transactional()
    void saveCtiet(Long idHdr, XhDgBbTinhKhoHdrReq objReq) {
        for (XhDgBbTinhKhoDtl listPhieuXuatKhoReq : objReq.getListPhieuXuatKho()) {
            XhDgBbTinhKhoDtl listPhieuXuatKho = new XhDgBbTinhKhoDtl();
            BeanUtils.copyProperties(listPhieuXuatKhoReq, listPhieuXuatKho);
            listPhieuXuatKho.setIdHdr(idHdr);
            xhDgBbTinhKhoDtlRepository.save(listPhieuXuatKho);
        }
    }

    @Transactional
    public XhDgBbTinhKhoHdr update(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhDgBbTinhKhoHdr> optional = xhDgBbTinhKhoHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhDgBbTinhKhoHdr> soDx = xhDgBbTinhKhoHdrRepository.findBySoBbTinhKho(objReq.getSoBbTinhKho());
        if (soDx.isPresent()) {
            if (!soDx.get().getId().equals(objReq.getId())) {
                throw new Exception("số biên bản đã tồn tại");
            }
        }
        XhDgBbTinhKhoHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data, "listPhieuXuatKho", "idThuKho");
        XhDgBbTinhKhoHdr created = xhDgBbTinhKhoHdrRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhDgBbTinhKhoHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhDgBbTinhKhoHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(created.getId(), objReq);
        return created;
    }


    public List<XhDgBbTinhKhoHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<XhDgBbTinhKhoHdr> optional = xhDgBbTinhKhoHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhDgBbTinhKhoHdr> allById = xhDgBbTinhKhoHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            if (mapDmucDvi.containsKey(data.getMaDvi())) {
                data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
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
            if (data.getLoaiVthh() != null) {
                data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : mapVthh.get(data.getLoaiVthh()));
            }
            if (data.getCloaiVthh() != null) {
                data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : mapVthh.get(data.getCloaiVthh()));
            }
            if (data.getIdThuKho() != null) {
                data.setTenThuKho(ObjectUtils.isEmpty(data.getIdThuKho()) ? null : userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
            }
            if (data.getIdKtvBaoQuan() != null) {
                data.setTenKtvBaoQuan(ObjectUtils.isEmpty(data.getIdKtvBaoQuan()) ? null : userInfoRepository.findById(data.getIdKtvBaoQuan()).get().getFullName());
            }
            if (data.getIdKeToan() != null) {
                data.setTenKeToan(ObjectUtils.isEmpty(data.getIdKeToan()) ? null : userInfoRepository.findById(data.getIdKeToan()).get().getFullName());
            }
            if (data.getNguoiPduyetId() != null) {
                data.setTenLanhDaoChiCuc(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
            }
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDgBbTinhKhoHdr.TABLE_NAME));
            data.setFileDinhKems(fileDinhKems);

            List<XhDgBbTinhKhoDtl> list = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(data.getId());
            data.setListPhieuXuatKho(list);
        });

        return allById;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhDgBbTinhKhoHdr> optional = xhDgBbTinhKhoHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhDgBbTinhKhoHdr data = optional.get();
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhDgBbTinhKhoHdr.TABLE_NAME));
        List<XhDgBbTinhKhoDtl> list = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(data.getId());
        xhDgBbTinhKhoDtlRepository.deleteAll(list);
        xhDgBbTinhKhoHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhDgBbTinhKhoHdr> list = xhDgBbTinhKhoHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(XhDgBbTinhKhoHdr::getId).collect(Collectors.toList());
        List<XhDgBbTinhKhoDtl> listKetQua = xhDgBbTinhKhoDtlRepository.findAllByIdHdrIn(listId);
        xhDgBbTinhKhoDtlRepository.deleteAll(listKetQua);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhDgBbTinhKhoHdr.TABLE_NAME));
        xhDgBbTinhKhoHdrRepository.deleteAll(list);
    }

    @Transient
    public XhDgBbTinhKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhDgBbTinhKhoHdr> optional = xhDgBbTinhKhoHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
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
        optional.get().setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            List<XhDgBbTinhKhoDtl> listDtl = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(optional.get().getId());
            listDtl.forEach(f -> {
                Optional<XhPhieuKnghiemCluong> xhPhieuKnghiemCluong = xhPhieuKnghiemCluongRepository.findById(f.getIdPhieuKnCl());
                if (xhPhieuKnghiemCluong.isPresent()) {
                    Optional<XhBbLayMau> xhBbLayMau = xhBbLayMauRepository.findById(xhPhieuKnghiemCluong.get().getIdBbLayMau());
                    xhBbLayMau.get().setIdBbTinhKho(optional.get().getId());
                    xhBbLayMau.get().setSoBbTinhKho(optional.get().getSoBbTinhKho());
                    xhBbLayMauRepository.save(xhBbLayMau.get());
                }
            });
        }
        XhDgBbTinhKhoHdr created = xhDgBbTinhKhoHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchXhDgBbTinhKho objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhDgBbTinhKhoHdr> page = this.searchPage(currentUser, objReq);
        List<XhDgBbTinhKhoHdr> data = page.getContent();

        String title = "Danh sách biên bản tịnh kho ";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Số BB tịnh kho",
                "Ngày bắt đầu", "Ngày kết thúc xuất", "Điểm kho", "Lô kho", "Số phiếu XK", "Ngày xuất kho", "Số bảng kê", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-tinh-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhDgBbTinhKhoHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdGiaoNvXh();
            objs[2] = dx.getNam();
            objs[3] = dx.getNgayQdGiaoNvXh();
            objs[4] = dx.getSoBbTinhKho();
            objs[5] = dx.getNgayBatDauXuat();
            objs[6] = dx.getNgayKetThucXuat();
            objs[7] = dx.getTenDiemKho();
            objs[8] = dx.getTenLoKho();
            Object[] finalObjs = objs;
            dx.getListPhieuXuatKho().forEach(s -> {
                finalObjs[9] = s.getSoPhieuXuatKho();
                finalObjs[10] = s.getNgayXuatKho();
                finalObjs[11] = s.getSoBkCanHang();
            });
            objs[12] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        try {
            FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bandaugia/Biên bản tịnh kho bán đấu giá.docx");
            List<XhDgBbTinhKhoHdr> listDetail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            XhDgBbTinhKhoHdr detail = listDetail.get(0);
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}