package com.tcdt.qlnvhang.service.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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
import java.util.stream.Collectors;

@Service
public class XhQdDchinhKhBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdDchinhKhBttHdrRepository xhQdDchinhKhBttHdrRepository;
    @Autowired
    private XhQdDchinhKhBttDtlRepository xhQdDchinhKhBttDtlRepository;
    @Autowired
    private XhQdDchinhKhBttSlRepository xhQdDchinhKhBttSlRepository;
    @Autowired
    private XhQdDchinhKhBttSlDtlRepository xhQdDchinhKhBttSlDtlRepository;

    public Page<XhQdDchinhKhBttHdr> searchPage(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdDchinhKhBttHdr> search = xhQdDchinhKhBttHdrRepository.searchPage(req, pageable);
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
    public XhQdDchinhKhBttHdr create(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!StringUtils.isEmpty(req.getSoQdDc())) {
            Optional<XhQdDchinhKhBttHdr> optional = xhQdDchinhKhBttHdrRepository.findBySoQdDc(req.getSoQdDc());
            if (optional.isPresent())
                throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        XhQdDchinhKhBttHdr data = new XhQdDchinhKhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        XhQdDchinhKhBttHdr created = xhQdDchinhKhBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhQdDchinhKhBttHdrReq req, Long idHdr) {
        xhQdDchinhKhBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdDchinhKhBttDtlReq dtlReq : req.getChildren()) {
            XhQdDchinhKhBttDtl dtl = new XhQdDchinhKhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhQdDchinhKhBttDtlRepository.save(dtl);
            xhQdDchinhKhBttSlRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdDchinhKhBttSlReq slReq : dtlReq.getChildren()) {
                XhQdDchinhKhBttSl sl = new XhQdDchinhKhBttSl();
                BeanUtils.copyProperties(slReq, sl, "id");
                sl.setIdDtl(dtl.getId());
                xhQdDchinhKhBttSlRepository.save(sl);
                xhQdDchinhKhBttSlDtlRepository.deleteAllByIdSl(slReq.getId());
                for (XhQdDchinhKhBttSlDtlReq slDtlReq : slReq.getChildren()) {
                    XhQdDchinhKhBttSlDtl slDtl = new XhQdDchinhKhBttSlDtl();
                    BeanUtils.copyProperties(slDtlReq, slDtl, "id");
                    slDtl.setIdSl(sl.getId());
                    xhQdDchinhKhBttSlDtlRepository.save(slDtl);
                }
            }
        }
    }

    @Transactional
    public XhQdDchinhKhBttHdr update(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhQdDchinhKhBttHdr> optional = xhQdDchinhKhBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhQdDchinhKhBttHdr> soQdDc = xhQdDchinhKhBttHdrRepository.findBySoQdDc(req.getSoQdDc());
        if (soQdDc.isPresent()) {
            if (!soQdDc.get().getId().equals(req.getId())) {
                throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
            }
        }
        XhQdDchinhKhBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhQdDchinhKhBttHdr update = xhQdDchinhKhBttHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhQdDchinhKhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhQdDchinhKhBttHdr> list = xhQdDchinhKhBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhQdDchinhKhBttHdr> allById = xhQdDchinhKhBttHdrRepository.findAllById(ids);
        for (XhQdDchinhKhBttHdr data : allById) {
            List<XhQdDchinhKhBttDtl> listDtl = xhQdDchinhKhBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdDchinhKhBttDtl dataDtl : listDtl) {
                List<XhQdDchinhKhBttSl> listSl = xhQdDchinhKhBttSlRepository.findAllByIdDtl(dataDtl.getId());
                for (XhQdDchinhKhBttSl dataSl : listSl) {
                    List<XhQdDchinhKhBttSlDtl> listSlDtl = xhQdDchinhKhBttSlDtlRepository.findAllByIdSl(dataSl.getId());
                    listSlDtl.forEach(dataSlDtl -> {
                        dataSlDtl.setTenDiemKho(StringUtils.isEmpty(dataSlDtl.getMaDiemKho()) ? null : mapDmucDvi.get(dataSlDtl.getMaDiemKho()));
                        dataSlDtl.setTenNhaKho(StringUtils.isEmpty(dataSlDtl.getMaNhaKho()) ? null : mapDmucDvi.get(dataSlDtl.getMaNhaKho()));
                        dataSlDtl.setTenNganKho(StringUtils.isEmpty(dataSlDtl.getMaNganKho()) ? null : mapDmucDvi.get(dataSlDtl.getMaNganKho()));
                        dataSlDtl.setTenLoKho(StringUtils.isEmpty(dataSlDtl.getMaLoKho()) ? null : mapDmucDvi.get(dataSlDtl.getMaLoKho()));
                        dataSlDtl.setTenLoaiVthh(StringUtils.isEmpty(dataSlDtl.getLoaiVthh()) ? null : mapVthh.get(dataSlDtl.getLoaiVthh()));
                        dataSlDtl.setTenCloaiVthh(StringUtils.isEmpty(dataSlDtl.getCloaiVthh()) ? null : mapVthh.get(dataSlDtl.getCloaiVthh()));
                    });
                    dataSl.setTenDvi(StringUtils.isEmpty(dataSl.getMaDvi()) ? null : mapDmucDvi.get(dataSl.getMaDvi()));
                    dataSl.setChildren(listSlDtl);
                }
                dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
                dataDtl.setChildren(listSl);
            }
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
        }
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhQdDchinhKhBttHdr> optional = xhQdDchinhKhBttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhQdDchinhKhBttHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDV)) {
            throw new Exception("Chỉ thực hiện xóa với điều chỉnh ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdDchinhKhBttDtl> listDtl = xhQdDchinhKhBttDtlRepository.findAllByIdHdr(data.getId());
        for (XhQdDchinhKhBttDtl dataDtl : listDtl) {
            List<XhQdDchinhKhBttSl> listSl = xhQdDchinhKhBttSlRepository.findAllByIdDtl(dataDtl.getId());
            for (XhQdDchinhKhBttSl dataSl : listSl) {
                xhQdDchinhKhBttSlDtlRepository.deleteAllByIdSl(dataSl.getId());
            }
            xhQdDchinhKhBttSlRepository.deleteAllByIdDtl(dataDtl.getId());
        }
        xhQdDchinhKhBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdDchinhKhBttHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdDchinhKhBttHdr> list = xhQdDchinhKhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhQdDchinhKhBttHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDV)) {
                throw new Exception("Chỉ thực hiện xóa với điều chỉnh ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> idHdr = list.stream().map(XhQdDchinhKhBttHdr::getId).collect(Collectors.toList());
        List<XhQdDchinhKhBttDtl> listDtl = xhQdDchinhKhBttDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdDchinhKhBttDtl::getId).collect(Collectors.toList());
        List<XhQdDchinhKhBttSl> listSl = xhQdDchinhKhBttSlRepository.findByIdDtlIn(idDtl);
        List<Long> idSl = listSl.stream().map(XhQdDchinhKhBttSl::getId).collect(Collectors.toList());
        List<XhQdDchinhKhBttSlDtl> listSlDtl = xhQdDchinhKhBttSlDtlRepository.findByIdSlIn(idSl);
        xhQdDchinhKhBttSlDtlRepository.deleteAll(listSlDtl);
        xhQdDchinhKhBttSlRepository.deleteAll(listSl);
        xhQdDchinhKhBttDtlRepository.deleteAll(listDtl);
        xhQdDchinhKhBttHdrRepository.deleteAll(list);
    }

    public XhQdDchinhKhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhQdDchinhKhBttHdr> optional = xhQdDchinhKhBttHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhQdDchinhKhBttHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.BAN_HANH + Contains.DADUYET_LDV:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhQdDchinhKhBttHdr created = xhQdDchinhKhBttHdrRepository.save(data);
        return data;
    }

    public void export(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdDchinhKhBttHdr> page = this.searchPage(currentUser, req);
        List<XhQdDchinhKhBttHdr> data = page.getContent();
        String title = " Danh sách điều chỉnh kế hoạch bán trực tiếp ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ điều chỉnh KH bán trực tiếp",
                "Ngày ký QĐ điều chỉnh", "Số quyết định gốc", "Trích yếu",
                "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String fileName = "danh-sach-dieu-chinh-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdDc();
            objs[3] = hdr.getNgayKyDc();
            objs[4] = hdr.getSoQdGoc();
            objs[5] = hdr.getTrichYeu();
            objs[6] = hdr.getTenLoaiVthh();
            objs[7] = hdr.getTenCloaiVthh();
            objs[8] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
