package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhQdPdRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlToChucRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlQuyetDinhPdKqService extends BaseServiceImpl {


    @Autowired
    private XhTlQuyetDinhQdPdRepository xhTlQuyetDinhQdPdRepository;

    @Autowired
    private XhTlToChucRepository xhTlToChucRepository;

    @Autowired
    private XhTlHopDongRepository xhTlHopDongRepository;

    @Autowired
    private XhTlHoSoHdrRepository xhTlHoSoHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhTlQuyetDinhPdKqHdr> searchPage(CustomUserDetails currentUser, XhTlQuyetDinhPdKqHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0, 6));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlQuyetDinhPdKqHdr> search = xhTlQuyetDinhQdPdRepository.search(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapHinhThuDg = getListDanhMucChung("HINH_THUC_DG");
        Map<String, String> mapPhuongThucDg = getListDanhMucChung("PHUONG_THUC_DG");
        search.getContent().forEach(s -> {
            s.setMapVthh(mapDmucVthh);
            s.setMapDmucDvi(mapDmucDvi);
            s.setTrangThai(s.getTrangThai());
            s.setTenTrangThaiHd(s.getTrangThaiHd());
            s.setTenTrangThaiXh(s.getTrangThaiXh());
            s.setTenLoaiHinhNx(StringUtils.isEmpty(s.getLoaiHinhNhapXuat()) ? null : mapLoaiHinhNx.get(s.getLoaiHinhNhapXuat()));
            s.setTenKieuNx(StringUtils.isEmpty(s.getKieuNhapXuat()) ? null : mapKieuNx.get(s.getKieuNhapXuat()));
            s.setTenHthucDgia(StringUtils.isEmpty(s.getHthucDgia()) ? null : mapHinhThuDg.get(s.getHthucDgia()));
            s.setTenPthucDgia(StringUtils.isEmpty(s.getPthucDgia()) ? null : mapPhuongThucDg.get(s.getPthucDgia()));
        });
        return search;
    }

    @Transactional
    public XhTlQuyetDinhPdKqHdr save(CustomUserDetails currentUser, XhTlQuyetDinhPdKqHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(objReq.getSoQd())) {
            Optional<XhTlQuyetDinhPdKqHdr> optional = xhTlQuyetDinhQdPdRepository.findBySoQd(objReq.getSoQd());
            if (optional.isPresent()) {
                throw new Exception("số quyết định đã tồn tại");
            }
        }
        XhTlQuyetDinhPdKqHdr data = new XhTlQuyetDinhPdKqHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhTlQuyetDinhPdKqHdr created = xhTlQuyetDinhQdPdRepository.save(data);
        return created;
    }

    @Transactional
    public XhTlQuyetDinhPdKqHdr update(CustomUserDetails currentUser, XhTlQuyetDinhPdKqHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhTlQuyetDinhPdKqHdr> optional = xhTlQuyetDinhQdPdRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhTlQuyetDinhPdKqHdr> soQd = xhTlQuyetDinhQdPdRepository.findBySoQd(objReq.getSoQd());
        if (soQd.isPresent()) {
            if (!soQd.get().getId().equals(objReq.getId())) {
                throw new Exception("số quyết định đã tồn tại");
            }
        }

        XhTlQuyetDinhPdKqHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        XhTlQuyetDinhPdKqHdr updated = xhTlQuyetDinhQdPdRepository.save(data);
        return updated;
    }


    public List<XhTlQuyetDinhPdKqHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlQuyetDinhPdKqHdr> optional = xhTlQuyetDinhQdPdRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhTlQuyetDinhPdKqHdr> allById = xhTlQuyetDinhQdPdRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiHd(data.getTrangThaiHd());
            data.setTrangThaiXh(data.getTrangThaiXh());
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNhapXuat()) ? null : mapLoaiHinhNx.get(data.getLoaiHinhNhapXuat()));
            data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNhapXuat()) ? null : mapKieuNx.get(data.getKieuNhapXuat()));
            data.getQuyetDinhDtl().forEach(d1 -> {
                d1.setMapDmucDvi(mapDmucDvi);
                d1.setMapVthh(mapVthh);
            });
            List<XhTlHopDongHdr> hopDongTlHdr = xhTlHopDongRepository.findAllByIdQdKqTl(data.getId());
            hopDongTlHdr.forEach(f -> {
                f.setMapVthh(mapVthh);
                f.setMapDmucDvi(mapDmucDvi);
                f.setTrangThai(f.getTrangThai());
                f.setTrangThaiXh(f.getTrangThaiXh());
            });
            data.setListHopDong(hopDongTlHdr);
        });
        return allById;
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlQuyetDinhPdKqHdr> list = xhTlQuyetDinhQdPdRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
//
//    List<Long> listHoSo = list.stream().map(XhTlQuyetDinhPdKqHdr::getIdHoSo).collect(Collectors.toList());
//    List<XhTlHoSoHdr> listObjQdPd = xhTlHoSoRepository.findByIdIn(listHoSo);
//    listObjQdPd.forEach(s -> {
//      s.setIdQd(null);
//      s.setSoQd(null);
//    });
//    xhTlHoSoRepository.saveAll(listObjQdPd);
//
//    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlQuyetDinhPdKqHdr.TABLE_NAME));
//    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_CAN_CU"));
        xhTlQuyetDinhQdPdRepository.deleteAll(list);

    }


    public XhTlQuyetDinhPdKqHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhTlQuyetDinhPdKqHdr> optional = xhTlQuyetDinhQdPdRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlQuyetDinhPdKqHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        if (statusReq.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
            data.setTrangThaiHd(statusReq.getTrangThai());
        } else {
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    data.setNguoiGduyetId(currentUser.getUser().getId());
                    data.setNgayGduyet(LocalDate.now());
                    break;
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                    break;
                case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                case Contains.BAN_HANH + Contains.DADUYET_LDC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(statusReq.getTrangThai());
            if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
                Optional<XhTlToChucHdr> xhTlToChucHdr = xhTlToChucRepository.findById(data.getIdThongBao());
                if (xhTlToChucHdr.isPresent()) {
                    xhTlToChucHdr.get().setIdQdPdKq(data.getId());
                    xhTlToChucHdr.get().setSoQdPdKq(data.getSoQd());
                    xhTlToChucRepository.save(xhTlToChucHdr.get());
                }
            }
        }
        XhTlQuyetDinhPdKqHdr created = xhTlQuyetDinhQdPdRepository.save(data);
        return created;
    }


    public void export(CustomUserDetails currentUser, XhTlQuyetDinhPdKqHdrReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhTlQuyetDinhPdKqHdr> page = this.searchPage(currentUser, objReq);
        List<XhTlQuyetDinhPdKqHdr> data = page.getContent();

        String title = "Danh sách quyết định thanh lý hàng DTQG ";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký", "Hồ sơ đề nghị thanh lý", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhTlQuyetDinhPdKqHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
          /*objs[2] = qd.getTrichYeu();
            objs[3] = qd.getNgayKy();
            objs[4] = qd.getSoHoSo();
            objs[5] = qd.getTenTrangThai();*/
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
