package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhServiceImpl;
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
public class XhDgPhieuXuatKhoService extends BaseServiceImpl {


    @Autowired
    private XhDgPhieuXuatKhoRepository xhDgPhieuXuatKhoRepository;

    @Autowired
    private XhQdGiaoNvXhServiceImpl xhQdGiaoNvXhService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhDgPhieuXuatKho> searchPage(CustomUserDetails currentUser, SearchXhDgPhieuXuatKhoReq req) throws Exception {
        req.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgPhieuXuatKho> search = xhDgPhieuXuatKhoRepository.search(req, pageable);
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
    public XhDgPhieuXuatKho save(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhDgPhieuXuatKho> optional = xhDgPhieuXuatKhoRepository.findBySoPhieuXuatKho(objReq.getSoPhieuXuatKho());
        if (optional.isPresent()) {
            throw new Exception("số số phiếu đã tồn tại");
        }
        XhDgPhieuXuatKho data = new XhDgPhieuXuatKho();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        XhDgPhieuXuatKho created = xhDgPhieuXuatKhoRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhDgPhieuXuatKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }

    @Transactional
    public XhDgPhieuXuatKho update(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhDgPhieuXuatKho> optional = xhDgPhieuXuatKhoRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhDgPhieuXuatKho> soDx = xhDgPhieuXuatKhoRepository.findBySoPhieuXuatKho(objReq.getSoPhieuXuatKho());
        if (soDx.isPresent()) {
            if (!soDx.get().getId().equals(objReq.getId())) {
                throw new Exception("số số phiếu đã tồn tại");
            }
        }
        XhDgPhieuXuatKho data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        XhDgPhieuXuatKho created = xhDgPhieuXuatKhoRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhDgPhieuXuatKho.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhDgPhieuXuatKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }


    public List<XhDgPhieuXuatKho> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhDgPhieuXuatKho> optional = xhDgPhieuXuatKhoRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhDgPhieuXuatKho> allById = xhDgPhieuXuatKhoRepository.findAllById(ids);
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
            if (data.getNguoiPduyetId() != null) {
                data.setTenNguoiPduyet(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
            }
            if (data.getIdKtv() != null) {
                data.setTenKtv(ObjectUtils.isEmpty(data.getIdKtv()) ? null : userInfoRepository.findById(data.getIdKtv()).get().getFullName());
            }
            data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDgPhieuXuatKho.TABLE_NAME));
            data.setFileDinhKems(fileDinhKems);

        });

        return allById;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhDgPhieuXuatKho> optional = xhDgPhieuXuatKhoRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhDgPhieuXuatKho data = optional.get();
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhDgPhieuXuatKho.TABLE_NAME));
        xhDgPhieuXuatKhoRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhDgPhieuXuatKho> list = xhDgPhieuXuatKhoRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhDgPhieuXuatKho.TABLE_NAME));
        xhDgPhieuXuatKhoRepository.deleteAll(list);
    }

    @Transient
    public XhDgPhieuXuatKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhDgPhieuXuatKho> optional = xhDgPhieuXuatKhoRepository.findById(Long.valueOf(statusReq.getId()));
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
        XhDgPhieuXuatKho created = xhDgPhieuXuatKhoRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchXhDgPhieuXuatKhoReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhDgPhieuXuatKho> page = this.searchPage(currentUser, objReq);
        List<XhDgPhieuXuatKho> data = page.getContent();

        String title = "Danh sách phiếu xuất kho ";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho", "Lô kho", "Số phiếu xuất kho", "Ngày xuất kho", "Số phiếu KNCL", "Ngày giám đinh", "Trạng thái"};
        String fileName = "danh-sach-phieu-xuat-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhDgPhieuXuatKho dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdGiaoNvXh();
            objs[2] = dx.getNam();
            objs[3] = dx.getNgayQdGiaoNvXh();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoPhieuXuatKho();
            objs[7] = dx.getNgayXuatKho();
            objs[8] = dx.getSoPhieuKnCl();
            objs[9] = dx.getNgayKn();
            objs[10] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        try {
            FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bandaugia/Phiếu xuất kho hế hoạch bán đấu giá.docx");
            List<XhDgPhieuXuatKho> listDetail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            XhDgPhieuXuatKho detail = listDetail.get(0);
            XhQdGiaoNvXh xhQdGiaoNvXh = xhQdGiaoNvXhService.detail(detail.getIdQdGiaoNvXh());
            detail.setTenTtcn(xhQdGiaoNvXh.getTenTtcn());
            detail.setTenDviCha(xhQdGiaoNvXh.getTenDvi());
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}