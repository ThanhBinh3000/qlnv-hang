package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.bienbantinhkho;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
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
import java.time.LocalDate;
import java.util.*;

@Service
public class XhBbTinhkBttServiceImpl extends BaseServiceImpl implements XhBbTinhkBttService {

    @Autowired
    private XhBbTinhkBttHdrRepository xhBbTinhkBttHdrRepository;

    @Autowired
    private XhBbTinhkBttDtlRepository xhBbTinhkBttDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public Page<XhBbTinhkBttHdr> searchPage(XhBbTinhkBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhBbTinhkBttHdr> data = xhBbTinhkBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDvi.get(f.getMaLoKho()));
            f.setChildren(xhBbTinhkBttDtlRepository.findAllByIdHdr(f.getId()));
        });
        return data;
    }

    @Override
    public XhBbTinhkBttHdr create(XhBbTinhkBttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        XhBbTinhkBttHdr data = new XhBbTinhkBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setMaDvi(userInfo.getDvql());
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.valueOf(data.getSoBbTinhKho().split("/")[0]));
        XhBbTinhkBttHdr created = xhBbTinhkBttHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbTinhkBttHdr.TABLE_NAME);
            data.setFileDinhKems(fileDinhKemList);
        }
        saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhBbTinhkBttHdrReq req, Long idHdr){
        xhBbTinhkBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhBbTinhkBttDtlReq dtlReq : req.getChildren()){
            XhBbTinhkBttDtl dtl = new XhBbTinhkBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhBbTinhkBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhBbTinhkBttHdr update(XhBbTinhkBttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }
        Optional<XhBbTinhkBttHdr> qOptional = xhBbTinhkBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        XhBbTinhkBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(LocalDate.now());
        dataDB.setNguoiSuaId(getUser().getId());
        XhBbTinhkBttHdr created = xhBbTinhkBttHdrRepository.save(dataDB);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhBbTinhkBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbTinhkBttHdr.TABLE_NAME);
        dataDB.setFileDinhKems(fileDinhKemList);
        this.saveDetail(req, dataDB.getId());
        return created;
    }

    @Override
    public XhBbTinhkBttHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Không tồn tại bản ghi");
        }
        Optional<XhBbTinhkBttHdr> qOptional = xhBbTinhkBttHdrRepository.findById(id);
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        XhBbTinhkBttHdr data = qOptional.get();
        if (!Objects.isNull(data.getIdThuKho())){
            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
        }
        if (!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        if (!Objects.isNull(data.getIdKeToan())){
            data.setTenKeToan(userInfoRepository.findById(data.getIdKeToan()).get().getFullName());
        }
        if (!Objects.isNull(data.getNguoiPduyetId())){
            data.setTenNguoiPduyet(userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        }
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenNganKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(hashMapDvi.get(data.getMaLoKho()));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbTinhkBttHdr.TABLE_NAME));
        if (!CollectionUtils.isEmpty(fileDinhKems)) data.setFileDinhKems(fileDinhKems);
        data.setChildren(xhBbTinhkBttDtlRepository.findAllByIdHdr(id));
        return data;
    }

    @Override
    public XhBbTinhkBttHdr approve(XhBbTinhkBttHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }
        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }
        Optional<XhBbTinhkBttHdr> optional = xhBbTinhkBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhBbTinhkBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
            case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
            case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setIdKtv(userInfo.getId());
                data.setNgayGuiDuyet(LocalDate.now());
                data.setIdKeToan(userInfo.getId()   );
                break;
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        xhBbTinhkBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }
        Optional<XhBbTinhkBttHdr> optional = xhBbTinhkBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(optional.get().getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }
        xhBbTinhkBttHdrRepository.delete(optional.get());
        xhBbTinhkBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbTinhkBttHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
       if (StringUtils.isEmpty(listMulti)){
           throw new Exception("Xóa thất bại, không tìm thấy dữ liệu ");
       }
       List<XhBbTinhkBttHdr> list = xhBbTinhkBttHdrRepository.findAllById(listMulti);
       if (list.isEmpty()){
           throw new Exception("Không tìm thấy dữ liệu cần xóa");
       }
       for (XhBbTinhkBttHdr hdr : list){
           this.delete(hdr.getId());
       }
    }

    @Override
    public void export(XhBbTinhkBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhBbTinhkBttHdr> page = this.searchPage(req);
        List<XhBbTinhkBttHdr> data = page.getContent();
        String title="Danh sách biên bản tịnh kho";
        String[] rowsName = new String[]{"STT","Số QĐ giao NV XH", "Năm KH", "Thời hạn XH trước ngày", "Số BB tịnh kho", "Ngày bắt đầu xuất", "Ngày kết thúc xuất", "Số phiếu XK", "Số Bảng kê", "Ngày xuất kho", "Điểm kho", "Lô khô", "Trạng thái"};
        String filename="danh-sach-biển-ban-lay-mau-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhBbTinhkBttHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoQdNv();
            objs[2]=hdr.getNamKh();
            objs[3]=hdr.getNgayQdNv();
            objs[4]=hdr.getSoBbTinhKho();
            objs[5]=hdr.getNgayBdauXuat();
            objs[6]=hdr.getNgayKthucXuat();
//            objs[7]=hdr.getsp;
//            objs[8]=hdr.getNgayXuatKho();
//            objs[9]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
