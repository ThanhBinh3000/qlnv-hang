package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongCtReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class XhPhieuKnghiemCluongServiceImpl extends BaseServiceImpl implements XhPhieuKnghiemCluongService {

    @Autowired
    private  XhPhieuKnghiemCluongRepository mainRepository;

    @Autowired
    private  XhPhieuKnghiemCluongCtRepository subRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public Page<XhPhieuKnghiemCluong> searchPage(XhPhieuKnghiemCluongReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhPhieuKnghiemCluong> data = mainRepository.searchPage(
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
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhPhieuKnghiemCluong create(XhPhieuKnghiemCluongReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        XhPhieuKnghiemCluong data = new XhPhieuKnghiemCluong();
        BeanUtils.copyProperties(req,data,"id");

        data.setNguoiTaoId(userInfo.getId());
        data.setNgayTao(new Date());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(data.getSoPhieu().split("/")[0]));
        data.setIdNguoiKiemNghiem(userInfo.getId());
        data.setIdTruongPhong(userInfo.getId());
        XhPhieuKnghiemCluong created = mainRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhPhieuKnghiemCluong.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhPhieuKnghiemCluongReq req, Long idHdr){
        subRepository.deleteAllByIdHdr(idHdr);
        for (XhPhieuKnghiemCluongCtReq ctReq :req.getChildren()) {
            XhPhieuKnghiemCluongCt ct = new XhPhieuKnghiemCluongCt();
            BeanUtils.copyProperties(ctReq,ct,"id");
            ct.setIdHdr(idHdr);
            subRepository.save(ct);
        }
    }

    @Override
    public XhPhieuKnghiemCluong update(XhPhieuKnghiemCluongReq req) throws Exception {
        if(Objects.isNull(req)){
            throw new Exception("Bad reqeust");
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(req.getId());
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuKnghiemCluong dataDB = byId.get();
        BeanUtils.copyProperties(req,dataDB,"id");
        dataDB.setNgaySua(new Date());
        dataDB.setNguoiSuaId(userInfo.getId());

        XhPhieuKnghiemCluong  created = mainRepository.save(dataDB);
        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhPhieuKnghiemCluong.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhPhieuKnghiemCluong.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        this.saveDetail(req,dataDB.getId());
        return dataDB;
    }

    @Override
    public XhPhieuKnghiemCluong detail(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad reqeust");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        XhPhieuKnghiemCluong data = byId.get();

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhPhieuKnghiemCluong.TABLE_NAME));
        data.setFileDinhKems(fileDinhKem);

        data.setTenLoaiVthh(mapDmucHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(mapDmucHh.get(data.getCloaiVthh()));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdThuKho())){
            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdTruongPhong())){
            data.setTenTruongPhong(userInfoRepository.findById(data.getIdTruongPhong()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdNguoiKiemNghiem())){
            data.setTenNguoiKiemNghiem(userInfoRepository.findById(data.getIdNguoiKiemNghiem()).get().getFullName());
        }
        if(!Objects.isNull(data.getNguoiPduyetId())){
            data.setTenNguoiPduyet(userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        }
        Map<String,String> hashMapHthucBquan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        data.setTenHthucBquan(hashMapHthucBquan.getOrDefault(data.getHthucBquan(),null));
        data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(mapDmucDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()));
        data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()));
        data.setChildren(subRepository.findAllByIdHdr(id));

        return data;
    }

    @Override
    public XhPhieuKnghiemCluong approve(XhPhieuKnghiemCluongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(req.getId());
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuKnghiemCluong data = byId.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DA_DUYET_LDC + Contains.CHO_DUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        mainRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        mainRepository.delete(byId.get());
        subRepository.deleteAllByIdHdr(byId.get().getId());
        fileDinhKemService.delete(byId.get().getId(), Collections.singleton(XhPhieuKnghiemCluong.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }

        List<XhPhieuKnghiemCluong> list = mainRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhPhieuKnghiemCluong xhPhieuKnghiemCluong : list){
            this.delete(xhPhieuKnghiemCluong.getId());
        }
    }

    @Override
    public void export(XhPhieuKnghiemCluongReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhPhieuKnghiemCluong> page = this.searchPage(req);
        List<XhPhieuKnghiemCluong> data = page.getContent();
        String title="Danh sách phiếu kiểm nghiệm chất lượng ";
        String[] rowsName = new String[]{"STT","Số QĐ giao NVXH", "Năm KH", "Thời hạn XH trước ngày", "Điển kho", "Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu", "sô BB tịnh kho","Ngày xuất dốc kho", "Trạng thái"};
        String filename="Danh-sach-phieu-kiem-nghiem-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhPhieuKnghiemCluong hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoQdGiaoNvXh();
            objs[2]=hdr.getNam();
            objs[3]=hdr.getNgayQdGiaoNvXh();
            objs[4]=hdr.getTenDiemKho();
            objs[5]=hdr.getTenLoKho();
            objs[6]=hdr.getSoPhieu();
            objs[7]=hdr.getNgayKnghiem();
            objs[8]=hdr.getSoBbLayMau();
            objs[9]=hdr.getNgayLayMau();
            objs[10]=hdr.getSoBbXuatDocKho();
            objs[11]=hdr.getNgayXuatDocKho();
            objs[12]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(XhPhieuKnghiemCluongReq objReq) throws Exception {
        XhPhieuKnghiemCluong optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}


