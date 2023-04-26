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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach( f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhDxKhBanTrucTiepHdr create(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoDxuat())){
            Optional<XhDxKhBanTrucTiepHdr> qOptional = xhDxKhBanTrucTiepHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (qOptional.isPresent()){
                throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
            }
        }
        XhDxKhBanTrucTiepHdr data = new XhDxKhBanTrucTiepHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNguoiTaoId(userInfo.getId());
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanTrucTiepHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKemList);
        }
        this.saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhDxKhBanTrucTiepHdrReq objReq, Long idHdr) {
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDxKhBanTrucTiepDtl dtlReq : objReq.getChildren()) {
            XhDxKhBanTrucTiepDtl dtl = new XhDxKhBanTrucTiepDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhDxKhBanTrucTiepDtlRepository.save(dtl);
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhDxKhBanTrucTiepDdiem ddiemReq : dtlReq.getChildren()) {
                XhDxKhBanTrucTiepDdiem ddiem = new XhDxKhBanTrucTiepDdiem();
                BeanUtils.copyProperties(ddiemReq, ddiem, "id");
                ddiem.setIdDtl(dtl.getId());
                xhDxKhBanTrucTiepDdiemRepository.save(ddiem);
            }
        }
    }

    @Override
    public XhDxKhBanTrucTiepHdr update(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }
        Optional<XhDxKhBanTrucTiepHdr> qOptional = xhDxKhBanTrucTiepHdrRepository.findById(req.getId());
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        if (!StringUtils.isEmpty(req.getSoDxuat())){
            Optional<XhDxKhBanTrucTiepHdr> hdr = xhDxKhBanTrucTiepHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (hdr.isPresent()){
                if (!hdr.get().getId().equals(req.getId())){
                    throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
                }
            }
        }
        XhDxKhBanTrucTiepHdr data = qOptional.get();
        BeanUtils.copyProperties(req, data, "id", "trangThaiTh");
        data.setNgaySua(getDateTimeNow());
        data.setNguoiSuaId(userInfo.getId());
        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanTrucTiepHdr created = xhDxKhBanTrucTiepHdrRepository.save(data);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhDxKhBanTrucTiepHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanTrucTiepHdr.TABLE_NAME);
        data.setFileDinhKems(fileDinhKemList);
        this.saveDetail(req, data.getId());
        return created;
    }

    @Override
    public XhDxKhBanTrucTiepHdr detail(Long id) throws Exception {
     Optional<XhDxKhBanTrucTiepHdr> qOptional = xhDxKhBanTrucTiepHdrRepository.findById(id);
     if (!qOptional.isPresent()){
         throw new UnsupportedOperationException("Không tồn tại bản ghi");
     }
     XhDxKhBanTrucTiepHdr data = qOptional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTh()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDxKhBanTrucTiepHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);
        List<XhDxKhBanTrucTiepDtl> dtlList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(data.getId());
        for (XhDxKhBanTrucTiepDtl dtl : dtlList){
            dtl.setTenDvi(hashMapDvi.get(dtl.getMaDvi()));
            List<XhDxKhBanTrucTiepDdiem> ddiemList = xhDxKhBanTrucTiepDdiemRepository.findByIdDtl(dtl.getId());
            ddiemList.forEach(f ->{
                f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                f.setTenNhaKho(hashMapDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(hashMapDvi.get(f.getMaNganKho()));
                f.setTenLoKho(hashMapDvi.get(f.getMaLoKho()));
                f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
            });
            dtl.setChildren(ddiemList);
        }
        data.setChildren(dtlList);
        return data;
    }

    @Override
    public XhDxKhBanTrucTiepHdr approve(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhDxKhBanTrucTiepHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(getDateTimeNow());
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        return xhDxKhBanTrucTiepHdrRepository.save(data);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }
        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhDxKhBanTrucTiepDtl> dtlList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(id);
        for (XhDxKhBanTrucTiepDtl dtl : dtlList){
            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(id);
        xhDxKhBanTrucTiepHdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhDxKhBanTrucTiepHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }
        List<XhDxKhBanTrucTiepHdr> list = xhDxKhBanTrucTiepHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhDxKhBanTrucTiepHdr hdr : list){
            this.delete(hdr.getId());
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
