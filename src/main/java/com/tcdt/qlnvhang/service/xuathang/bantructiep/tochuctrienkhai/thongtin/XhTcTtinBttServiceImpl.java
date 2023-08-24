package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
public class XhTcTtinBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;
    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttDtl> searchPage(CustomUserDetails currentUser, SearchXhTcTtinBttReq req) throws Exception {
        String dvql = currentUser.getDvql();
        Integer lastest = 1;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.HOANTHANHCAPNHAT);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
            req.setLastest(lastest);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdPdKhBttDtl> search = xhQdPdKhBttDtlRepository.searchPage(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        search.getContent().forEach(data -> {
            try {
                if (mapDmucDvi.containsKey((data.getMaDvi()))) {
                    Map<String, Object> objDonVi = mapDmucDvi.get(data.getMaDvi());
                    data.setTenDvi(objDonVi.get("tenDvi").toString());
                }
                if (mapDmucVthh.get((data.getLoaiVthh())) != null) {
                    data.setTenLoaiVthh(mapDmucVthh.get(data.getLoaiVthh()));
                }
                if (mapDmucVthh.get((data.getCloaiVthh())) != null) {
                    data.setTenCloaiVthh(mapDmucVthh.get(data.getCloaiVthh()));
                }
                if (data.getTrangThai() != null) {
                    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
                }
                if (data.getTrangThaiHd() != null) {
                    data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
                }
                if (data.getTrangThaiXh() != null) {
                    data.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiXh()));
                }
                data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public List<XhTcTtinBtt> create(CustomUserDetails currentUser, XhQdPdKhBttDtlReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhQdPdKhBttDtl> optional = xhQdPdKhBttDtlRepository.findById(req.getIdDtl());
        if (!optional.isPresent()) throw new Exception("Bản Ghi không tồn tại");
        XhQdPdKhBttDtl data = optional.get();
        data.setPthucBanTrucTiep(req.getPthucBanTrucTiep());
        data.setNgayNhanCgia(LocalDate.now());
        data.setDiaDiemChaoGia(req.getDiaDiemChaoGia());
        data.setNgayMkho(req.getNgayMkho());
        data.setNgayKthuc(req.getNgayKthuc());
        data.setGhiChuChaoGia(req.getGhiChuChaoGia());
        data.setThoiHanBan(req.getThoiHanBan());
        data.setTrangThai(Contains.DANGCAPNHAT);
        if (req.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
            if (!DataUtils.isNullOrEmpty(req.getFileUyQuyen())) {
                List<FileDinhKem> fileUyQuyen = fileDinhKemService.saveListFileDinhKem(req.getFileUyQuyen(), data.getId(), XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN");
                data.setFileUyQuyen(fileUyQuyen);
            }
        }
        if (req.getPthucBanTrucTiep().equals(Contains.BAN_LE)) {
            if (!DataUtils.isNullOrEmpty(req.getFileBanLe())) {
                List<FileDinhKem> fileBanLe = fileDinhKemService.saveListFileDinhKem(req.getFileBanLe(), data.getId(), XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE");
                data.setFileBanLe(fileBanLe);
            }
        }
        xhQdPdKhBttDtlRepository.save(data);
        List<XhTcTtinBtt> list = new ArrayList<>();
        for (XhQdPdKhBttDviReq dviReq : req.getChildren()) {
            for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                xhTcTtinBttRepository.deleteAllByIdDviDtl(dviDtlReq.getId());
                for (XhTcTtinBttReq chaoGiaReq : dviDtlReq.getChildren()) {
                    XhTcTtinBtt chaoGia = new XhTcTtinBtt();
                    BeanUtils.copyProperties(chaoGiaReq, chaoGia, "id");
                    chaoGia.setId(null);
                    chaoGia.setIdQdPdDtl(req.getIdDtl());
                    chaoGia.setTypeQdKq(false);
                    XhTcTtinBtt create = xhTcTtinBttRepository.save(chaoGia);
                    fileDinhKemService.delete(create.getId(), Collections.singleton(XhTcTtinBtt.TABLE_NAME));
                    if (!DataUtils.isNullObject(chaoGiaReq.getFileDinhKems())) {
                        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(chaoGiaReq.getFileDinhKems()), create.getId(), XhTcTtinBtt.TABLE_NAME);
                        chaoGia.setFileDinhKems(fileDinhKems.get(0));
                    }
                    list.add(chaoGia);
                }
            }
        }
        return list;
    }

    public List<XhQdPdKhBttDtl> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhQdPdKhBttDtl> list = xhQdPdKhBttDtlRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhQdPdKhBttDtl> allById = xhQdPdKhBttDtlRepository.findAllById(ids);
        for (XhQdPdKhBttDtl data : allById) {
            List<XhQdPdKhBttDvi> dvi = xhQdPdKhBttDviRepository.findAllByIdDtl(data.getId());
            for (XhQdPdKhBttDvi dataDvi : dvi) {
                List<XhQdPdKhBttDviDtl> dviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                for (XhQdPdKhBttDviDtl dataDviDtl : dviDtl) {
                    List<XhTcTtinBtt> toChuc = xhTcTtinBttRepository.findAllByIdDviDtl(dataDviDtl.getId());
                    toChuc.forEach(dataToChuc -> {
                        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dataToChuc.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                        if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                            dataToChuc.setFileDinhKems(fileDinhKems.get(0));
                        }
                    });
                    dataDviDtl.setTenDiemKho(StringUtils.isEmpty(dataDviDtl.getMaDiemKho()) ? null : mapDmucDvi.get(dataDviDtl.getMaDiemKho()));
                    dataDviDtl.setTenNhaKho(StringUtils.isEmpty(dataDviDtl.getMaNhaKho()) ? null : mapDmucDvi.get(dataDviDtl.getMaNhaKho()));
                    dataDviDtl.setTenNganKho(StringUtils.isEmpty(dataDviDtl.getMaNganKho()) ? null : mapDmucDvi.get(dataDviDtl.getMaNganKho()));
                    dataDviDtl.setTenLoKho(StringUtils.isEmpty(dataDviDtl.getMaLoKho()) ? null : mapDmucDvi.get(dataDviDtl.getMaLoKho()));
                    dataDviDtl.setTenLoaiVthh(StringUtils.isEmpty(dataDviDtl.getLoaiVthh()) ? null : mapDmucVthh.get(dataDviDtl.getLoaiVthh()));
                    dataDviDtl.setTenCloaiVthh(StringUtils.isEmpty(dataDviDtl.getCloaiVthh()) ? null : mapDmucVthh.get(dataDviDtl.getCloaiVthh()));
                    dataDviDtl.setChildren(toChuc);
                }
                dataDvi.setTenDvi(StringUtils.isEmpty(dataDvi.getMaDvi()) ? null : mapDmucDvi.get(dataDvi.getMaDvi()));
                dataDvi.setChildren(dviDtl);
            }
            data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : mapDmucDvi.get(data.getMaDvi()));
            data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNx()) ? null : mapLoaiHinhNx.get(data.getLoaiHinhNx()));
            data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNx()) ? null : mapKieuNx.get(data.getKieuNx()));
            data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : mapDmucVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : mapDmucVthh.get(data.getCloaiVthh()));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).get());
            data.setListHopDongBtt(xhHopDongBttHdrRepository.findAllByIdQdPdDtl(data.getId()));
            data.setChildren(dvi);
            if (!DataUtils.isNullObject(data.getPthucBanTrucTiep())) {
                if (data.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
                    List<FileDinhKem> fileUyQuyen = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN"));
                    data.setFileUyQuyen(fileUyQuyen);
                }
                if (data.getPthucBanTrucTiep().equals(Contains.BAN_LE)) {
                    List<FileDinhKem> fileBanLe = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE"));
                    data.setFileBanLe(fileBanLe);
                }
            }
        }
        return allById;
    }

    public XhQdPdKhBttDtl approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhQdPdKhBttDtl> optional = xhQdPdKhBttDtlRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhQdPdKhBttDtl data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.HOANTHANHCAPNHAT + Contains.DANGCAPNHAT:
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhQdPdKhBttDtl created = xhQdPdKhBttDtlRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        String title = " Danh sách thông tin triển khai kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Số QĐ phê duyệt KH bán trực tiếp",
                "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền", "Số QĐ PD KQ chào giá",
                "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String fileName = "Danh-sach-thong-tin-trien-khai-ke-hoach-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl dtl = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dtl.getSoQdPd();
            objs[2] = dtl.getPthucBanTrucTiep();
            objs[3] = dtl.getNgayNhanCgia();
            objs[4] = dtl.getSoQdKq();
            objs[5] = dtl.getTenLoaiVthh();
            objs[6] = dtl.getTenCloaiVthh();
            objs[7] = dtl.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}