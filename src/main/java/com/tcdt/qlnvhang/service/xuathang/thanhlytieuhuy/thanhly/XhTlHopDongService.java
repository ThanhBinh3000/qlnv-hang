package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhQdPdRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlHopDongService extends BaseServiceImpl {
    @Autowired
    private XhTlHopDongRepository xhTlHopDongRepository;
    @Autowired
    private XhTlQuyetDinhQdPdRepository xhTlQuyetDinhQdPdRepository;

    public Page<XhTlHopDongHdr> searchPage(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlHopDongHdr> search = xhTlHopDongRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(f -> {
            f.setMapDmucDvi(mapDmucDvi);
            f.setMapVthh(mapVthh);
            f.setTrangThai(f.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhTlHopDongHdr create(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(req.getSoHd())) {
            Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findBySoHd(req.getSoHd());
            if (optional.isPresent()) throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
        }
        if (!DataUtils.isNullObject(req.getSoQdKqTl())) {
            Optional<XhTlQuyetDinhPdKqHdr> checkSoQdKq = xhTlQuyetDinhQdPdRepository.findFirstBySoQd(req.getSoQdKqTl());
            if (!checkSoQdKq.isPresent()) {
                throw new Exception("Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + req.getSoQdKqTl() + " không tồn tại");
            } else {
                checkSoQdKq.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
            }
        }
        XhTlHopDongHdr data = new XhTlHopDongHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhTlHopDongHdr created = xhTlHopDongRepository.save(data);
        return created;
    }

    @Transactional
    public XhTlHopDongHdr update(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhTlHopDongHdr> soHd = xhTlHopDongRepository.findBySoHd(req.getSoHd());
        if (soHd.isPresent()) {
            if (!soHd.get().getId().equals(req.getId())) throw new Exception("số hợp đồng đã tồn tại");
        }
        XhTlHopDongHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        XhTlHopDongHdr created = xhTlHopDongRepository.save(data);
        return created;
    }

    public List<XhTlHopDongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlHopDongHdr> list = xhTlHopDongRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhTlHopDongHdr> allById = xhTlHopDongRepository.findAllById(ids);
        allById.forEach(data -> {
            data.getHopDongDtl().forEach(f -> {
                f.setMapDmucDvi(mapDmucDvi);
            });
            if (!DataUtils.isNullObject(data.getMaDviTsan())) {
                data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
            data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNx()) ? null : mapLoaiHinhNx.get(data.getLoaiHinhNx()));
            data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNx()) ? null : mapKieuNx.get(data.getKieuNx()));
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        xhTlHopDongRepository.delete(optional.get());
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlHopDongHdr> list = xhTlHopDongRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        xhTlHopDongRepository.deleteAll(list);
    }

    public XhTlHopDongHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.DAKY + Contains.DUTHAO:
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhTlHopDongHdr created = xhTlHopDongRepository.save(optional.get());
        return created;
    }
}
