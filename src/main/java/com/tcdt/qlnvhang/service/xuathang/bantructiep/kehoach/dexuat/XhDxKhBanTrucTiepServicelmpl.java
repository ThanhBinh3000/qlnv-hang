package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.dexuat;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiem;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDxKhBanTrucTiepServicelmpl extends BaseServiceImpl implements XhDxKhBanTrucTiepService {

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;

    @Autowired
    private XhDxKhBanTrucTiepDtlRepository xhDxKhBanTrucTiepDtlRepository;

    @Autowired
    private XhDxKhBanTrucTiepDdiemRepository xhDxKhBanTrucTiepDdiemRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhDxKhBanTrucTiepHdr> searchPage(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhDxKhBanTrucTiepHdr> data = xhDxKhBanTrucTiepHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach( f->{
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:mapDmucDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:mapDmucVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:mapDmucVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            if (DataUtils.isNullObject(f.getIdThop())) {
                f.setTenTrangThaiTh("Chưa tổng hợp");
            } else {
                f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            }
        });
        return data;
    }

    @Override
    public XhDxKhBanTrucTiepHdr create(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (!StringUtils.isEmpty(req.getSoDxuat())){
            Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (optional.isPresent()) throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }

        XhDxKhBanTrucTiepHdr data = new XhDxKhBanTrucTiepHdr();
        BeanUtils.copyProperties(req, data, "id");

        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

        data.setMaDvi(userInfo.getDvql());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);

        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanTrucTiepHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKemList);
        }
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhDxKhBanTrucTiepHdrReq req, Long idHdr) {
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDxKhBanTrucTiepDtl dataDtlReq : req.getChildren()) {
            XhDxKhBanTrucTiepDtl dataDtl = new XhDxKhBanTrucTiepDtl();
            BeanUtils.copyProperties(dataDtlReq, dataDtl, "id");
            dataDtl.setIdHdr(idHdr);
            xhDxKhBanTrucTiepDtlRepository.save(dataDtl);
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(dataDtlReq.getId());
            for (XhDxKhBanTrucTiepDdiem dataDtlDdiemReq : dataDtlReq.getChildren()) {
                XhDxKhBanTrucTiepDdiem dataDtlDdiem = new XhDxKhBanTrucTiepDdiem();
                BeanUtils.copyProperties(dataDtlDdiemReq, dataDtlDdiem, "id");
                dataDtlDdiem.setIdDtl(dataDtl.getId());
                xhDxKhBanTrucTiepDdiemRepository.save(dataDtlDdiem);
            }
        }
    }

    @Override
    public XhDxKhBanTrucTiepHdr update(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Kế hoạch bán trực tiếp không tồn tại");

        if (!StringUtils.isEmpty(req.getSoDxuat())){
            Optional<XhDxKhBanTrucTiepHdr> qOptional = xhDxKhBanTrucTiepHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (qOptional.isPresent()){
                if (!qOptional.get().getId().equals(req.getId())){
                    throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
                }
            }
        }

        XhDxKhBanTrucTiepHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "trangThaiTh");

        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(userInfo.getId());

        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhDxKhBanTrucTiepHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanTrucTiepHdr.TABLE_NAME);
        data.setFileDinhKems(fileDinhKemList);
        this.saveDetail(req, created.getId());
        return created;
    }

    @Override
    public XhDxKhBanTrucTiepHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(id);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Kế hoạch bán trực tiếp không tồn tại");

        XhDxKhBanTrucTiepHdr data = optional.get();

        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        List<XhDxKhBanTrucTiepDtl> dataDtlList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(data.getId());
            for (XhDxKhBanTrucTiepDtl dataDtl : dataDtlList){
                List<XhDxKhBanTrucTiepDdiem> dataDtlDdiemList = xhDxKhBanTrucTiepDdiemRepository.findByIdDtl(dataDtl.getId());
                dataDtlDdiemList.forEach(diaDiem ->{
                    diaDiem.setTenDiemKho(StringUtils.isEmpty(diaDiem.getMaDiemKho())?null:mapDmucDvi.get(diaDiem.getMaDiemKho()));
                    diaDiem.setTenNhaKho(StringUtils.isEmpty(diaDiem.getMaNhaKho())?null:mapDmucDvi.get(diaDiem.getMaNhaKho()));
                    diaDiem.setTenNganKho(StringUtils.isEmpty(diaDiem.getMaNganKho())?null:mapDmucDvi.get(diaDiem.getMaNganKho()));
                    diaDiem.setTenLoKho(StringUtils.isEmpty(diaDiem.getMaLoKho())?null:mapDmucDvi.get(diaDiem.getMaLoKho()));
                    diaDiem.setTenLoaiVthh(StringUtils.isEmpty(diaDiem.getLoaiVthh())?null:mapDmucVthh.get(diaDiem.getLoaiVthh()));
                    diaDiem.setTenCloaiVthh(StringUtils.isEmpty(diaDiem.getCloaiVthh())?null:mapDmucVthh.get(diaDiem.getCloaiVthh()));
                    this.donGiaDuocDuyet(data, diaDiem);
                    });
                dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi())?null:mapDmucDvi.get(dataDtl.getMaDvi()));
                dataDtl.setChildren(dataDtlDdiemList);
            }
            data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:mapDmucDvi.get(data.getMaDvi()));
            data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:mapDmucVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:mapDmucVthh.get(data.getCloaiVthh()));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            data.setChildren(dataDtlList);
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDxKhBanTrucTiepHdr.TABLE_NAME));
            if (!CollectionUtils.isEmpty(fileDinhKems)) data.setFileDinhKems(fileDinhKems);
        return data;
    }

    void donGiaDuocDuyet(XhDxKhBanTrucTiepHdr data, XhDxKhBanTrucTiepDdiem diaDiem){
        BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
        if(data.getLoaiVthh().startsWith("02")){
            donGiaDuocDuyet = xhDxKhBanTrucTiepDdiemRepository.getDonGiaVatVt(data.getCloaiVthh(), data.getNamKh());
            if (!DataUtils.isNullObject(donGiaDuocDuyet)){
                diaDiem.setDonGiaDuocDuyet(donGiaDuocDuyet);
            }
        }else {
            donGiaDuocDuyet = xhDxKhBanTrucTiepDdiemRepository.getDonGiaVatLt(data.getCloaiVthh(), data.getMaDvi(), data.getNamKh());
            if (!DataUtils.isNullObject(donGiaDuocDuyet)){
                diaDiem.setDonGiaDuocDuyet(donGiaDuocDuyet);
            }
        }
    }

    @Override
    public XhDxKhBanTrucTiepHdr approve(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");

        XhDxKhBanTrucTiepHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(LocalDate.now());
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        return xhDxKhBanTrucTiepHdrRepository.save(data);
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(id)) throw new Exception("Xóa thất bại không tìm thấy dữ liệu");

        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(id);
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }

        List<XhDxKhBanTrucTiepDtl> dataDtlList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(id);
        for (XhDxKhBanTrucTiepDtl dataDtl : dataDtlList){
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(dataDtl.getId());
        }

        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(id);
        xhDxKhBanTrucTiepHdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhDxKhBanTrucTiepHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(listMulti)) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        List<XhDxKhBanTrucTiepHdr> dataList = xhDxKhBanTrucTiepHdrRepository.findByIdIn(listMulti);
        if (dataList.isEmpty()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

        for (XhDxKhBanTrucTiepHdr data : dataList){
            this.delete(data.getId());
        }
    }

    @Override
    public void export(XhDxKhBanTrucTiepHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhDxKhBanTrucTiepHdr> page = this.searchPage(req);
        List<XhDxKhBanTrucTiepHdr> data = page.getContent();
        String title="Danh sách đề xuất kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT","Năm KH", "Số KH/đề xuất", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ duyệt KH bán TT", "Ngày ký QĐ", "Trích yếu", "Loại hàng hóa", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Số QĐ giao chỉ tiêu", "Trạng thái"};
        String filename="danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhDxKhBanTrucTiepHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getNamKh();
            objs[2]=hdr.getSoDxuat();
            objs[3]=hdr.getNgayTao();
            objs[4]=hdr.getNgayPduyet();
            objs[5]=hdr.getSoQdPd();
            objs[6]=hdr.getNgayKyQd();
            objs[7]=hdr.getTrichYeu();
            objs[8]=hdr.getTenLoaiVthh();
            objs[9]=hdr.getTenCloaiVthh();
            objs[10]=hdr.getSoQdCtieu();
            objs[11]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception {
        return xhDxKhBanTrucTiepHdrRepository.countSLDalenKh(req.getYear() , req.getLoaiVthh(), req.getMaDvi(), req.getLastest());
    }

    @Override
    public BigDecimal getGiaBanToiThieu(String cloaiVthh, String maDvi, Integer namKh) {
       if (cloaiVthh.startsWith("02")){
           return xhDxKhBanTrucTiepHdrRepository.getGiaBanToiThieuVt(cloaiVthh, namKh);
       }else {
           return xhDxKhBanTrucTiepHdrRepository.getGiaBanToiThieuLt(cloaiVthh, maDvi, namKh);
       }
    }
}
