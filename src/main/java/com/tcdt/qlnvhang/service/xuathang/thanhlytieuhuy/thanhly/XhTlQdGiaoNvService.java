package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhTlQdGiaoNvService extends BaseServiceImpl {
    @Autowired
    private XhTlQdGiaoNvRepository xhTlQdGiaoNvRepository;
    @Autowired
    private XhTlHopDongHdrRepository xhTlHopDongHdrRepository;

    public Page<XhTlQdGiaoNvHdr> searchPage(CustomUserDetails currentUser, XhTlQdGiaoNvHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0, 6));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlQdGiaoNvHdr> search = xhTlQdGiaoNvRepository.search(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        search.getContent().forEach(data -> {
            data.getQuyetDinhDtl().forEach(dtl -> {
                dtl.setMapDmucDvi(mapDmucDvi);
            });
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiXh(data.getTrangThaiXh());
            data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNx()) ? null : mapLoaiHinhNx.get(data.getLoaiHinhNx()));
            data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNx()) ? null : mapKieuNx.get(data.getKieuNx()));
        });
        return search;
    }

    @Transactional
    public XhTlQdGiaoNvHdr create(CustomUserDetails currentUser, XhTlQdGiaoNvHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(req.getSoBbQd())) {
            Optional<XhTlQdGiaoNvHdr> optional = xhTlQdGiaoNvRepository.findBySoBbQd(req.getSoBbQd());
            if (optional.isPresent()) throw new Exception("số quyết định đã tồn tại");
        }
        XhTlQdGiaoNvHdr data = new XhTlQdGiaoNvHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        XhTlQdGiaoNvHdr created = xhTlQdGiaoNvRepository.save(data);
        return created;
    }

    @Transactional
    public XhTlQdGiaoNvHdr update(CustomUserDetails currentUser, XhTlQdGiaoNvHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhTlQdGiaoNvHdr> optional = xhTlQdGiaoNvRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhTlQdGiaoNvHdr> soBbQd = xhTlQdGiaoNvRepository.findBySoBbQd(req.getSoBbQd());
        if (soBbQd.isPresent()) {
            if (!soBbQd.get().getId().equals(req.getId())) {
                throw new Exception("số quyết định đã tồn tại");
            }
        }
        XhTlQdGiaoNvHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhTlQdGiaoNvHdr updated = xhTlQdGiaoNvRepository.save(data);
        return updated;
    }

    public List<XhTlQdGiaoNvHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlQdGiaoNvHdr> list = xhTlQdGiaoNvRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhTlQdGiaoNvHdr> allById = xhTlQdGiaoNvRepository.findAllById(ids);
        allById.forEach(data -> {
            data.getQuyetDinhDtl().forEach(dtl -> {
                dtl.setMapDmucDvi(mapDmucDvi);
                dtl.setMapVthh(mapVthh);
            });
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiXh(data.getTrangThaiXh());
            data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNx()) ? null : mapLoaiHinhNx.get(data.getLoaiHinhNx()));
            data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNx()) ? null : mapKieuNx.get(data.getKieuNx()));
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlQdGiaoNvHdr> optional = xhTlQdGiaoNvRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Banr ghi không tồn tại");
        xhTlQdGiaoNvRepository.delete(optional.get());
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlQdGiaoNvHdr> list = xhTlQdGiaoNvRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        xhTlQdGiaoNvRepository.deleteAll(list);
    }

    public XhTlQdGiaoNvHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlQdGiaoNvHdr> optional = xhTlQdGiaoNvRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhTlQdGiaoNvHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGduyetId(currentUser.getUser().getId());
                data.setNgayGduyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            Optional<XhTlHopDongHdr> hopDongHdr = xhTlHopDongHdrRepository.findById(data.getIdHopDong());
            if (hopDongHdr.isPresent()) {
                hopDongHdr.get().setIdQdNv(data.getId());
                hopDongHdr.get().setSoQdNv(data.getSoBbQd());
                xhTlHopDongHdrRepository.save(hopDongHdr.get());
            }
        }
        XhTlQdGiaoNvHdr created = xhTlQdGiaoNvRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhTlQdGiaoNvHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhTlQdGiaoNvHdr> page = this.searchPage(currentUser, req);
        List<XhTlQdGiaoNvHdr> data = page.getContent();
        String title = "Danh sách quyết định giao nhiệm vụ thanh lý hàng DTQG";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng",
                "Chủng loại hàng hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh Kho",
                "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String fileName = "danh-sach-quyet-dinh-giao-nhiem-vu-thanh-ly-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhTlQdGiaoNvHdr QĐ = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = QĐ.getNam();
            objs[2] = QĐ.getSoBbQd();
            objs[3] = QĐ.getNgayKyQd();
            objs[4] = QĐ.getSoHopDong();
            objs[5] = QĐ.getTenCloaiVthh();
            objs[6] = QĐ.getThoiGianGiaoNhan();
            objs[7] = QĐ.getTrichYeu();
            objs[8] = QĐ.getSoBbTinhKho();
            objs[9] = QĐ.getSoBbHaoDoi();
            objs[10] = QĐ.getTenTrangThai();
            objs[11] = QĐ.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}