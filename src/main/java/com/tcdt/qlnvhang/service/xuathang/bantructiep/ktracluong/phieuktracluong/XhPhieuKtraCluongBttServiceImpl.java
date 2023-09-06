package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.phieuktracluong;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhPhieuKtraCluongBttServiceImpl extends BaseServiceImpl implements XhPhieuKtraCluongBttService {

    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;

    @Autowired
    private XhPhieuKtraCluongBttDtlRepository xhPhieuKtraCluongBttDtlRepository;

    @Autowired
    private XhBbLayMauBttServiceImpl xhBbLayMauBttService;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;


    @Override
    public Page<XhPhieuKtraCluongBttHdr> searchPage(XhPhieuKtraCluongBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhPhieuKtraCluongBttHdr> data = xhPhieuKtraCluongBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDvi.get(f.getMaLoKho()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return data;
    }

    @Override
    public XhPhieuKtraCluongBttHdr create(XhPhieuKtraCluongBttHdrReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(req.getSoPhieu())){
            Optional<XhPhieuKtraCluongBttHdr> qOptional = xhPhieuKtraCluongBttHdrRepository.findBySoPhieu(req.getSoPhieu());
            if (qOptional.isPresent()) throw new Exception("Số Phiếu " + req.getSoPhieu() + "đã tồn tại");
        }

        XhPhieuKtraCluongBttHdr data = new XhPhieuKtraCluongBttHdr();
        BeanUtils.copyProperties(req, data,"id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setId(Long.parseLong(data.getSoPhieu().split("/")[0]));
        data.setIdNgKnghiem(userInfo.getId());
        data.setIdTruongPhong(userInfo.getId());

        XhPhieuKtraCluongBttHdr created = xhPhieuKtraCluongBttHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhPhieuKtraCluongBttHdr.TABLE_NAME);
            data.setFileDinhKems(fileDinhKemList);
        }

        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhPhieuKtraCluongBttHdrReq req, Long idHdr){
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhPhieuKtraCluongBttDtlReq dtlReq : req.getChildren()){
            XhPhieuKtraCluongBttDtl dtl = new XhPhieuKtraCluongBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhPhieuKtraCluongBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhPhieuKtraCluongBttHdr update(XhPhieuKtraCluongBttHdrReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhPhieuKtraCluongBttHdr> optional = xhPhieuKtraCluongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent())throw new Exception("Không tìm thấy phiếu kiểm tra chất lượng");

        XhPhieuKtraCluongBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(userInfo.getId());
        XhPhieuKtraCluongBttHdr created = xhPhieuKtraCluongBttHdrRepository.save(data);

        fileDinhKemService.delete(created.getId(), Collections.singleton(XhPhieuKtraCluongBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhPhieuKtraCluongBttHdr.TABLE_NAME);
        data.setFileDinhKems(fileDinhKemList);

        this.saveDetail(req, data.getId());
        return created;
    }

    @Override
    public XhPhieuKtraCluongBttHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhPhieuKtraCluongBttHdr> optional = xhPhieuKtraCluongBttHdrRepository.findById(id);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tìm thấy phiếu kiểm tra chất lượng");

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        Map<String,String> hastMapHthucBquan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        XhPhieuKtraCluongBttHdr data = optional.get();

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhPhieuKtraCluongBttHdr.TABLE_NAME));
        if (!CollectionUtils.isEmpty(fileDinhKems)) data.setFileDinhKems(fileDinhKems);

        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho())?null:hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho())?null:hashMapDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho())?null:hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho())?null:hashMapDvi.get(data.getMaLoKho()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        data.setTenHthucBquan(StringUtils.isEmpty(data.getHthucBquan())?null:hastMapHthucBquan.get(data.getHthucBquan()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

        if(!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdTruongPhong())){
            data.setTenTruongPhong(userInfoRepository.findById(data.getIdTruongPhong()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdNgKnghiem())){
            data.setTenNguoiKiemNghiem(userInfoRepository.findById(data.getIdNgKnghiem()).get().getFullName());
        }
        if (!Objects.isNull(data.getNguoiPduyetId())){
            data.setTenNguoiPheDuyet(userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        }

        data.setChildren(xhPhieuKtraCluongBttDtlRepository.findAllByIdHdr(id));
        return data;
    }

    @Override
    public XhPhieuKtraCluongBttHdr approve(XhPhieuKtraCluongBttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhPhieuKtraCluongBttHdr> optional = xhPhieuKtraCluongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        XhPhieuKtraCluongBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DA_DUYET_LDC + Contains.CHO_DUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        xhPhieuKtraCluongBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }
        Optional<XhPhieuKtraCluongBttHdr> optional = xhPhieuKtraCluongBttHdrRepository.findById(id);
        if (!optional.isPresent()){
             throw new Exception("Không tìm thấy dữ liệu");
        }
        xhPhieuKtraCluongBttHdrRepository.delete(optional.get());
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhPhieuKtraCluongBttHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }
        List<XhPhieuKtraCluongBttHdr> list =xhPhieuKtraCluongBttHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhPhieuKtraCluongBttHdr hdr : list){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhPhieuKtraCluongBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhPhieuKtraCluongBttHdr> page = this.searchPage(req);
        List<XhPhieuKtraCluongBttHdr> data = page.getContent();
        String title="Danh sách phiếu kiểm tra chất lượng bán trực tiếp";
        String[] rowsName = new String[]{"STT","Số QĐ giao NCXH", "Năm KH", "Thời hạn XH trước ngày", "Điển kho", "Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB xuất dốc kho","Ngày xuất dốc kho", "Trạng thái"};
        String filename="danh-sach-phieu-kiem-tra-chat-luong-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhPhieuKtraCluongBttHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoQdNv();
            objs[2]=hdr.getNamKh();
            objs[3]=hdr.getNgayQdNv();
            objs[4]=hdr.getTenDiemKho();
            objs[5]=hdr.getTenLoKho();
            objs[6]=hdr.getSoPhieu();
            objs[7]=hdr.getNgayKnghiem();
            objs[8]=hdr.getSoBienBan();
            objs[9]=hdr.getNgayLayMau();
            objs[10]=hdr.getSoBbXuatDoc();
            objs[11]=hdr.getNgayXuatDocKho();
            objs[12]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        try {
            FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bantructiep/Phiếu kiểm nghiệm chất lượng bán trực tiếp.docx");
            XhPhieuKtraCluongBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            XhBbLayMauBttHdr xhBbLayMauBttHdr = xhBbLayMauBttService.detail(detail.getIdBienBan());
            detail.setTenChiCuc(xhBbLayMauBttHdr.getTenDvi());
            detail.setSoLuongBaoQuan(xhBbLayMauBttHdr.getSoLuongLayMau());
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}