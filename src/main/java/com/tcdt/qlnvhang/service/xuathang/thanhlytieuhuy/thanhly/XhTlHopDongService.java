package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucDtl;
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
import java.util.*;

@Service
public class XhTlHopDongService extends BaseServiceImpl {
    @Autowired
    private XhTlHopDongHdrRepository hdrRepository;
    @Autowired
    private XhTlHopDongDtlRepository dtlRepository;
    @Autowired
    private XhTlHopDongHdrRepository xhTlHopDongHdrRepository;
    @Autowired
    private XhTlQuyetDinhPdKqHdrRepository xhTlQuyetDinhPdKqHdrRepository;

    @Autowired
    private XhTlToChucDtlRepository xhTlToChucDtlRepository;

    public Page<XhTlHopDongHdr> searchPage(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlHopDongHdr> search = xhTlHopDongHdrRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(f -> {
            f.setMapDmucDvi(mapDmucDvi);
            f.setMapVthh(mapVthh);
            f.setTrangThai(f.getTrangThai());
        });
        return search;
    }

    public List<XhTlHopDongHdr> searchAll(XhTlHopDongHdrReq req) throws Exception {
        List<XhTlHopDongHdr> search = xhTlHopDongHdrRepository.searchAll(req);
        return search;
    }

    public List<XhTlHopDongHdr> dsTaoQdGiaoNvxh(XhTlHopDongHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        req.setMaDvi(userInfo.getDvql());
        List<XhTlHopDongHdr> search = xhTlHopDongHdrRepository.dsTaoQdGiaoNvXh(req);
        return search;
    }

    @Transactional
    public XhTlHopDongHdr create(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(req.getSoHd())) {
            Optional<XhTlHopDongHdr> optional = xhTlHopDongHdrRepository.findBySoHd(req.getSoHd());
            if (optional.isPresent()) throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
        }
        if (!DataUtils.isNullObject(req.getSoQdKqTl())) {
            Optional<XhTlQuyetDinhPdKqHdr> checkSoQdKq = xhTlQuyetDinhPdKqHdrRepository.findFirstBySoQd(req.getSoQdKqTl());
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
        XhTlHopDongHdr created = xhTlHopDongHdrRepository.save(data);
        this.saveDetail(req,data.getId());
        return created;
    }


    void saveDetail(XhTlHopDongHdrReq req,Long idHdr) throws Exception {
        Optional<XhTlQuyetDinhPdKqHdr> byId = xhTlQuyetDinhPdKqHdrRepository.findById(req.getIdQdKqTl());
        List<XhTlToChucDtl> allByIdHdr = xhTlToChucDtlRepository.findAllByIdHdr(byId.get().getIdThongBao());
        List<XhTlHopDongDtl> allHopDongDB = dtlRepository.findAllByIdHdr(idHdr);
        // Loop toàn bộ danh sách hợp đồng dtl trong DB với trường hợp là update
        allHopDongDB.forEach( item -> {
            // Find hợp đồng cũ và hợp đồng request save mới
            XhTlHopDongDtl xhTlHopDongDtl = req.getChildren().stream().filter(x -> Objects.equals(x.getIdDsHdr(),
                    item.getIdDsHdr())).findAny().orElse(null);
            // Nếu null => Phải set lại id hợp đồng = null
            if(Objects.isNull(xhTlHopDongDtl)){
                XhTlToChucDtl xhTlToChucDtl = allByIdHdr.stream().filter(x -> Objects.equals(x.getIdDsHdr(),
                        item.getIdDsHdr())).findAny().orElse(null);
                assert xhTlToChucDtl != null;
                xhTlToChucDtl.setIdHopDongTl(null);
                xhTlToChucDtlRepository.save(xhTlToChucDtl);
            }
        });


        dtlRepository.deleteAllByIdHdr(idHdr);
        for(XhTlHopDongDtl ctReq : req.getChildren()){
            XhTlHopDongDtl ct = new XhTlHopDongDtl();
            BeanUtils.copyProperties(ctReq, ct,"id");
            ct.setId(null);
            ct.setIdHdr(idHdr);
            XhTlToChucDtl xhTlToChucDtl = allByIdHdr.stream().filter(x -> Objects.equals(x.getIdDsHdr(),
                    ctReq.getIdDsHdr())).findAny().orElse(null);
            assert xhTlToChucDtl != null;
            xhTlToChucDtl.setIdHopDongTl(idHdr);
            xhTlToChucDtlRepository.save(xhTlToChucDtl);
            dtlRepository.save(ct);
        }
    }

    @Transactional
    public XhTlHopDongHdr update(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhTlHopDongHdr> optional = xhTlHopDongHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhTlHopDongHdr> soHd = xhTlHopDongHdrRepository.findBySoHd(req.getSoHd());
        if (soHd.isPresent()) {
            if (!soHd.get().getId().equals(req.getId())) throw new Exception("số hợp đồng đã tồn tại");
        }
        XhTlHopDongHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        XhTlHopDongHdr created = xhTlHopDongHdrRepository.save(data);
        this.saveDetail(req,data.getId());
        return created;
    }

    public List<XhTlHopDongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhTlHopDongHdr> list = xhTlHopDongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhTlHopDongHdr> allById = xhTlHopDongHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            if (!DataUtils.isNullObject(data.getMaDviTsan())) {
                data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiXh(data.getTrangThaiXh());
            data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNx()) ? null : mapLoaiHinhNx.get(data.getLoaiHinhNx()));
            data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNx()) ? null : mapKieuNx.get(data.getKieuNx()));
            data.setChildren(dtlRepository.findAllByIdHdr(data.getId()));
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlHopDongHdr> optional = xhTlHopDongHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");

        Optional<XhTlQuyetDinhPdKqHdr> byId = xhTlQuyetDinhPdKqHdrRepository.findById(optional.get().getIdQdKqTl());
        List<XhTlToChucDtl> allByIdHdr = xhTlToChucDtlRepository.findAllByIdHdr(byId.get().getIdThongBao());
        // Loop toàn bộ danh sách hợp đồng dtl trong DB với trường hợp là update
        allByIdHdr.forEach( item -> {
            // Find hợp đồng cũ và hợp đồng request save mới
            item.setIdHopDongTl(null);
            xhTlToChucDtlRepository.save(item);
        });

        xhTlHopDongHdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlHopDongHdr> list = xhTlHopDongHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        xhTlHopDongHdrRepository.deleteAll(list);
    }

    public XhTlHopDongHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlHopDongHdr> optional = xhTlHopDongHdrRepository.findById(Long.valueOf(statusReq.getId()));
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
        XhTlHopDongHdr created = xhTlHopDongHdrRepository.save(optional.get());
        return created;
    }
}
