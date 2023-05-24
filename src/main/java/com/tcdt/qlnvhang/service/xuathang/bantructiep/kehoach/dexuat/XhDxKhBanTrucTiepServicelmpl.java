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
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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

        Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        data.getContent().forEach( f->{
            if (mapDmucDvi.containsKey((f.getMaDvi()))) {
                f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
            }
            if (mapDmucHangHoa.get((f.getLoaiVthh())) != null) {
                f.setTenLoaiVthh(mapDmucHangHoa.get(f.getLoaiVthh()));
            }
            if (mapDmucHangHoa.get((f.getCloaiVthh())) != null) {
                f.setTenCloaiVthh(mapDmucHangHoa.get(f.getCloaiVthh()));
            }
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

        log.info("Save ke hoach ban truc tiep");
        XhDxKhBanTrucTiepHdr keHoachBanTrucTiep = new XhDxKhBanTrucTiepHdr();
        BeanUtils.copyProperties(req, keHoachBanTrucTiep, "id");

        keHoachBanTrucTiep.setMaDvi(userInfo.getDvql());
        keHoachBanTrucTiep.setNguoiTaoId(userInfo.getId());
        keHoachBanTrucTiep.setTrangThai(Contains.DU_THAO);
        keHoachBanTrucTiep.setTrangThaiTh(Contains.CHUATONGHOP);

        int slDviTsan = keHoachBanTrucTiep.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        keHoachBanTrucTiep.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

        XhDxKhBanTrucTiepHdr save = xhDxKhBanTrucTiepHdrRepository.save(keHoachBanTrucTiep);

        log.info("Save file dinh kem");
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), save.getId(), XhDxKhBanTrucTiepHdr.TABLE_NAME);
            keHoachBanTrucTiep.setFileDinhKems(fileDinhKemList);
        }

        this.saveDetail(req, save.getId());
        return save;
    }

    void saveDetail(XhDxKhBanTrucTiepHdrReq req, Long idHdr) {

        xhDxKhBanTrucTiepDtlRepository.deleteAllByIdHdr(idHdr);
        log.info("Save ke hoach ban truc tiep chi tiet");
        for (XhDxKhBanTrucTiepDtl keHoachBanTrucTiepDtlReq : req.getChildren()) {
            XhDxKhBanTrucTiepDtl keHoachBanTrucTiepDtl = new XhDxKhBanTrucTiepDtl();
            BeanUtils.copyProperties(keHoachBanTrucTiepDtlReq, keHoachBanTrucTiepDtl, "id");
            keHoachBanTrucTiepDtl.setIdHdr(idHdr);
            xhDxKhBanTrucTiepDtlRepository.save(keHoachBanTrucTiepDtl);

            xhDxKhBanTrucTiepDdiemRepository.deleteAllByIdDtl(keHoachBanTrucTiepDtlReq.getId());
            log.info("Save dia diem ban truc tiep");
            for (XhDxKhBanTrucTiepDdiem keHoachBanTrucTiepDtlDdiemReq : keHoachBanTrucTiepDtlReq.getChildren()) {
                XhDxKhBanTrucTiepDdiem keHoachBanTrucTiepDtlDdiem = new XhDxKhBanTrucTiepDdiem();
                BeanUtils.copyProperties(keHoachBanTrucTiepDtlDdiemReq, keHoachBanTrucTiepDtlDdiem, "id");
                keHoachBanTrucTiepDtlDdiem.setIdDtl(keHoachBanTrucTiepDtl.getId());
                xhDxKhBanTrucTiepDdiemRepository.save(keHoachBanTrucTiepDtlDdiem);
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

        XhDxKhBanTrucTiepHdr keHoachBanTrucTiep = optional.get();

        log.info("Update ke hoach ban truc tiep");
        BeanUtils.copyProperties(req, keHoachBanTrucTiep, "id", "trangThaiTh");
        keHoachBanTrucTiep.setNgaySua(LocalDate.now());
        keHoachBanTrucTiep.setNguoiSuaId(userInfo.getId());

        log.info("Update so luong don vi tai san");
        int slDviTsan = keHoachBanTrucTiep.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanTrucTiepDdiem::getMaDviTsan).collect(Collectors.toSet()).size();
        keHoachBanTrucTiep.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

        XhDxKhBanTrucTiepHdr saveUpdate = xhDxKhBanTrucTiepHdrRepository.save(keHoachBanTrucTiep);

        log.info("Update file dinh kem");
        fileDinhKemService.delete(saveUpdate.getId(), Collections.singleton(XhDxKhBanTrucTiepHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), saveUpdate.getId(), XhDxKhBanTrucTiepHdr.TABLE_NAME);
        keHoachBanTrucTiep.setFileDinhKems(fileDinhKemList);

        this.saveDetail(req, saveUpdate.getId());
        return saveUpdate;
    }

    @Override
    public XhDxKhBanTrucTiepHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhDxKhBanTrucTiepHdr> qOptional = xhDxKhBanTrucTiepHdrRepository.findById(id);
        if (!qOptional.isPresent()) throw new UnsupportedOperationException("Kế hoạch bán trực tiếp không tồn tại");

        XhDxKhBanTrucTiepHdr keHoachBanTrucTiep = qOptional.get();

        Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        List<XhDxKhBanTrucTiepDtl> keHoachBanTrucTiepDtlList = xhDxKhBanTrucTiepDtlRepository.findAllByIdHdr(keHoachBanTrucTiep.getId());
        if(!CollectionUtils.isEmpty(keHoachBanTrucTiepDtlList)){
            for (XhDxKhBanTrucTiepDtl keHoachBanTrucTiepDtl : keHoachBanTrucTiepDtlList){
                if (mapDmucDvi.containsKey(keHoachBanTrucTiepDtl.getMaDvi())) {
                    keHoachBanTrucTiepDtl.setTenDvi(mapDmucDvi.get(keHoachBanTrucTiepDtl.getMaDvi()));
                }
                List<XhDxKhBanTrucTiepDdiem> keHoachBanTrucTiepDdiemList = xhDxKhBanTrucTiepDdiemRepository.findByIdDtl(keHoachBanTrucTiepDtl.getId());
                if(!CollectionUtils.isEmpty(keHoachBanTrucTiepDdiemList)){
                    keHoachBanTrucTiepDdiemList.forEach(diaDiem ->{
                        if (mapDmucDvi.containsKey((diaDiem.getMaDiemKho()))) {
                            diaDiem.setTenDiemKho(mapDmucDvi.get(diaDiem.getMaDiemKho()));
                        }
                        if (mapDmucDvi.containsKey((diaDiem.getMaNhaKho()))) {
                            diaDiem.setTenNhaKho(mapDmucDvi.get(diaDiem.getMaNhaKho()));
                        }
                        if (mapDmucDvi.containsKey((diaDiem.getMaNganKho()))) {
                            diaDiem.setTenNganKho(mapDmucDvi.get(diaDiem.getMaNganKho()));
                        }
                        if (mapDmucDvi.containsKey((diaDiem.getMaLoKho()))) {
                            diaDiem.setTenLoKho(mapDmucDvi.get(diaDiem.getMaLoKho()));
                        }
                        if (mapDmucHangHoa.get((diaDiem.getLoaiVthh())) != null) {
                            diaDiem.setTenLoaiVthh(mapDmucHangHoa.get(diaDiem.getLoaiVthh()));
                        }
                        if (mapDmucHangHoa.get((diaDiem.getCloaiVthh())) != null) {
                            diaDiem.setTenCloaiVthh(mapDmucHangHoa.get(diaDiem.getCloaiVthh()));
                        }
                    });
                    keHoachBanTrucTiepDtl.setChildren(keHoachBanTrucTiepDdiemList);
                }
            }
            keHoachBanTrucTiep.setChildren(keHoachBanTrucTiepDtlList);
        }
        if (mapDmucDvi.containsKey((keHoachBanTrucTiep.getMaDvi()))) {
            keHoachBanTrucTiep.setTenDvi(mapDmucDvi.get(keHoachBanTrucTiep.getMaDvi()));
        }
        if (mapDmucHangHoa.get((keHoachBanTrucTiep.getLoaiVthh())) != null) {
            keHoachBanTrucTiep.setTenLoaiVthh(mapDmucHangHoa.get(keHoachBanTrucTiep.getLoaiVthh()));
        }
        if (mapDmucHangHoa.get((keHoachBanTrucTiep.getCloaiVthh())) != null) {
            keHoachBanTrucTiep.setTenCloaiVthh(mapDmucHangHoa.get(keHoachBanTrucTiep.getCloaiVthh()));
        }
        keHoachBanTrucTiep.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(keHoachBanTrucTiep.getTrangThai()));

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(keHoachBanTrucTiep.getId(), Arrays.asList(XhDxKhBanTrucTiepHdr.TABLE_NAME));
        if (!CollectionUtils.isEmpty(fileDinhKems)) keHoachBanTrucTiep.setFileDinhKems(fileDinhKems);
        return keHoachBanTrucTiep;
    }

    @Override
    public XhDxKhBanTrucTiepHdr approve(XhDxKhBanTrucTiepHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhDxKhBanTrucTiepHdr> optional = xhDxKhBanTrucTiepHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");

        XhDxKhBanTrucTiepHdr keHoachBanTrucTiep = optional.get();
        String status = req.getTrangThai() + keHoachBanTrucTiep.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                keHoachBanTrucTiep.setNguoiGuiDuyetId(userInfo.getId());
                keHoachBanTrucTiep.setNgayGuiDuyet(LocalDate.now());
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                keHoachBanTrucTiep.setNguoiPduyetId(userInfo.getId());
                keHoachBanTrucTiep.setNgayPduyet(LocalDate.now());
                keHoachBanTrucTiep.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                keHoachBanTrucTiep.setNguoiPduyetId(userInfo.getId());
                keHoachBanTrucTiep.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        keHoachBanTrucTiep.setTrangThai(req.getTrangThai());
        return xhDxKhBanTrucTiepHdrRepository.save(keHoachBanTrucTiep);
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
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(listMulti)) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        List<XhDxKhBanTrucTiepHdr> list = xhDxKhBanTrucTiepHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

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
