package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrReq;
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
public class XhBbHdoiBttServiceIpml extends BaseServiceImpl implements XhBbHdoiBttService {

    @Autowired
    private XhBbHdoiBttHdrRepository xhBbHdoiBttHdrRepository;

    @Autowired
    private XhBbHdoiBttDtlRepository xhBbHdoiBttDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public Page<XhBbHdoiBttHdr> searchPage(XhBbHdoiBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhBbHdoiBttHdr> data = xhBbHdoiBttHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDvi.get(f.getMaLoKho()));
            f.setChildren(xhBbHdoiBttDtlRepository.findAllByIdHdr(f.getId()));
        });
        return data;
    }


    @Override
    public XhBbHdoiBttHdr create(XhBbHdoiBttHdrReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        XhBbHdoiBttHdr data = new XhBbHdoiBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setMaDvi(userInfo.getDvql());
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.valueOf(data.getSoBbHaoDoi().split("/")[0]));
        XhBbHdoiBttHdr created = xhBbHdoiBttHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbHdoiBttHdr.TABLE_NAME);
            data.setFileDinhKems(fileDinhKemList);
        }

        saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhBbHdoiBttHdrReq req, Long idHdr){
        xhBbHdoiBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhBbHdoiBttDtlReq dtlReq : req.getChildren()){
            XhBbHdoiBttDtl dtl = new XhBbHdoiBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhBbHdoiBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhBbHdoiBttHdr update(XhBbHdoiBttHdrReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhBbHdoiBttHdr> qOptional = xhBbHdoiBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()) throw new Exception("Không tìm thấy biên bản hao dôi cần sửa");

        XhBbHdoiBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(LocalDate.now());
        dataDB.setNguoiSuaId(getUser().getId());
        XhBbHdoiBttHdr created = xhBbHdoiBttHdrRepository.save(dataDB);

        fileDinhKemService.delete(created.getId(), Collections.singleton(XhBbHdoiBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbTinhkBttHdr.TABLE_NAME);
        dataDB.setFileDinhKems(fileDinhKemList);

        this.saveDetail(req, dataDB.getId());
        return created;
    }

    @Override
    public XhBbHdoiBttHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhBbHdoiBttHdr> qOptional = xhBbHdoiBttHdrRepository.findById(id);
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tìm thấy biên bản hao dôi");
        }

        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhBbHdoiBttHdr data = qOptional.get();

        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho())?null:hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho())?null:hashMapDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho())?null:hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho())?null:hashMapDvi.get(data.getMaLoKho()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

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

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbHdoiBttHdr.TABLE_NAME));
        if (!CollectionUtils.isEmpty(fileDinhKems)) data.setFileDinhKems(fileDinhKems);

        data.setChildren(xhBbHdoiBttDtlRepository.findAllByIdHdr(id));

        return data;
    }

    @Override
    public XhBbHdoiBttHdr approve(XhBbHdoiBttHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhBbHdoiBttHdr> optional = xhBbHdoiBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhBbHdoiBttHdr data = optional.get();
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
        xhBbHdoiBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }

        Optional<XhBbHdoiBttHdr> optional = xhBbHdoiBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(optional.get().getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        xhBbHdoiBttHdrRepository.delete(optional.get());
        xhBbHdoiBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbHdoiBttHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu ");
        }

        List<XhBbHdoiBttHdr> list = xhBbHdoiBttHdrRepository.findAllById(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhBbHdoiBttHdr hdr : list){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhBbHdoiBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhBbHdoiBttHdr> page = this.searchPage(req);
        List<XhBbHdoiBttHdr> data = page.getContent();
        String title="Danh sách biên bản hao đôi";
        String[] rowsName = new String[]{"STT","Số QĐ giao NV XH", "Năm KH", "Thời hạn XH trước ngày", "Số BB hao dôi", "Ngày bắt đầu xuất", "Ngày kết thúc xuất", "Số phiếu XK", "Số Bảng kê", "Ngày xuất kho", "Điểm kho", "Lô khô", "Trạng thái"};
        String filename="danh-sach-biển-ban-lay-mau-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhBbHdoiBttHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoQdNv();
            objs[2]=hdr.getNamKh();
            objs[3]=hdr.getNgayQdNv();
            objs[4]=hdr.getSoBbHaoDoi();
            objs[5]=hdr.getNgayKthucNhap();
            objs[6]=hdr.getNgayKthucXuat();
//            objs[7]=hdr.getS;
//            objs[8]=hdr.getNgayXuatKho();
            objs[9]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
