package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrReq;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhBbLayMauBttServiceImpl extends BaseServiceImpl implements  XhBbLayMauBttService {

    @Autowired
    private XhBbLayMauBttHdrRepository xhBbLayMauBttHdrRepository;

    @Autowired
    private XhBbLayMauBttDtlRepository xhBbLayMauBttDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public Page<XhBbLayMauBttHdr> searchPage(XhBbLayMauBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhBbLayMauBttHdr> data = xhBbLayMauBttHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDvi.get(f.getMaLoKho()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhBbLayMauBttHdr create(XhBbLayMauBttHdrReq req) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (!StringUtils.isEmpty(req.getSoBienBan())){
            Optional<XhBbLayMauBttHdr> qOptional = xhBbLayMauBttHdrRepository.findBySoBienBan(req.getSoBienBan());
            if (qOptional.isPresent()){
                throw new Exception("Số biên bản " + req.getSoBienBan() + " đã tồn tại ");
            }
        }

        XhBbLayMauBttHdr data = new XhBbLayMauBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(getDateTimeNow());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(data.getSoBienBan().split("/")[0]));
        data.setIdKtv(userInfo.getId());
        XhBbLayMauBttHdr created = xhBbLayMauBttHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbLayMauBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhBbLayMauBttHdr.TABLE_NAME + "_CAN_CU");
            created.setCanCuPhapLy(canCuPhapLy);
        }

        if (!DataUtils.isNullOrEmpty(req.getFileNiemPhong())) {
            List<FileDinhKem> fileNienPhong = fileDinhKemService.saveListFileDinhKem(req.getFileNiemPhong(), created.getId(), XhBbLayMauBttHdr.TABLE_NAME + "_NIEM_PHONG");
            created.setFileNiemPhong(fileNienPhong);
        }
        saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhBbLayMauBttHdrReq req, Long idHdr){
        xhBbLayMauBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhBbLayMauBttDtlReq dtlReq : req.getChildren()){
            XhBbLayMauBttDtl dtl = new XhBbLayMauBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhBbLayMauBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhBbLayMauBttHdr update(XhBbLayMauBttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhBbLayMauBttHdr> qOptional = xhBbLayMauBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        XhBbLayMauBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(userInfo.getId());
        XhBbLayMauBttHdr created = xhBbLayMauBttHdrRepository.save(dataDB);

        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhBbLayMauBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhBbLayMauBttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhBbLayMauBttHdr.TABLE_NAME+ "_CAN_CU"));
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhBbLayMauBttHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCuPhapLy(canCuPhapLy);

        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhBbLayMauBttHdr.TABLE_NAME+ "_NIEM_PHONG"));
        List<FileDinhKem> fileNiemPhong = fileDinhKemService.saveListFileDinhKem(req.getFileNiemPhong(), created.getId(), XhBbLayMauBttHdr.TABLE_NAME + "_NIEM_PHONG");
        created.setFileNiemPhong(fileNiemPhong);


        this.saveDetail(req, dataDB.getId());
        return created;
    }

    @Override
    public XhBbLayMauBttHdr detail(Long id) throws Exception {
       if (StringUtils.isEmpty(id))
           throw new Exception("Không tồn tại bản ghi");

       Optional<XhBbLayMauBttHdr> qOptional = xhBbLayMauBttHdrRepository.findById(id);

       if (!qOptional.isPresent()){
           throw new UnsupportedOperationException("Bản ghi không tồn tại");
       }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhBbLayMauBttHdr data = qOptional.get();

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbLayMauBttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKem);

        List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbLayMauBttHdr.TABLE_NAME + "_CAN_CU"));
        data.setCanCuPhapLy(canCuPhapLy);

        List<FileDinhKem> fileNiemPhong = fileDinhKemService.search(data.getId(), Arrays.asList(XhBbLayMauBttHdr.TABLE_NAME + "_NIEM_PHONG"));
        data.setFileNiemPhong(fileNiemPhong);

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenNganKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(hashMapDvi.get(data.getMaLoKho()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        if(!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        data.setChildren(xhBbLayMauBttDtlRepository.findAllByIdHdr(id));

        return data;
    }

    @Override
    public XhBbLayMauBttHdr approve(XhBbLayMauBttHdrReq req) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo();

        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhBbLayMauBttHdr> optional = xhBbLayMauBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhBbLayMauBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        xhBbLayMauBttHdrRepository.save(data);
        return data;

    }

    @Override
    public void delete(Long id) throws Exception {

        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }

        Optional<XhBbLayMauBttHdr> optional = xhBbLayMauBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(optional.get().getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        xhBbLayMauBttHdrRepository.delete(optional.get());
        xhBbLayMauBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbLayMauBttHdr.TABLE_NAME));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbLayMauBttHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhBbLayMauBttHdr.TABLE_NAME + "_NIEM_PHONG"));

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }

        List<XhBbLayMauBttHdr> list = xhBbLayMauBttHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhBbLayMauBttHdr hdr : list){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhBbLayMauBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhBbLayMauBttHdr> page = this.searchPage(req);
        List<XhBbLayMauBttHdr> data = page.getContent();
        String title="Danh sách biên bản lấy mẫu bán trực tiếp";
        String[] rowsName = new String[]{"STT","Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điển kho", "Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho", "Ngày xuất dốc kho", "sô BB hao dôi", "Trạng thái"};
        String filename="danh-sach-biển-ban-lay-mau-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhBbLayMauBttHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoQd();
            objs[2]=hdr.getNamKh();
            objs[3]=hdr.getNgayQd();
            objs[4]=hdr.getTenDiemKho();
            objs[5]=hdr.getTenLoKho();
            objs[6]=hdr.getSoBienBan();
            objs[7]=hdr.getNgayLayMau();
            objs[8]=hdr.getSoBbTinhKho();
            objs[9]=hdr.getNgayXuatDocKho();
            objs[10]=hdr.getSoBbHaoDoi();
            objs[11]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
