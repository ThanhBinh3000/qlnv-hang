package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.phieuxuatkho;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReposytory;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttServiceImpI;
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
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhPhieuXkhoBttServiceImpl extends BaseServiceImpl implements XhPhieuXkhoBttService {

    @Autowired
    private XhPhieuXkhoBttReposytory xhPhieuXkhoBttReposytory;

    @Autowired
    private XhQdNvXhBttServiceImpI xhQdNvXhBttServiceImpI;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;


    @Override
    public Page<XhPhieuXkhoBtt> searchPage(XhPhieuXkhoBttReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhPhieuXkhoBtt> data = xhPhieuXkhoBttReposytory.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDvi.get(f.getMaLoKho()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhPhieuXkhoBtt create(XhPhieuXkhoBttReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        XhPhieuXkhoBtt data = new XhPhieuXkhoBtt();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setMaDvi(userInfo.getDvql());
        data.setIdNguoiLapPhieu(userInfo.getId());
        data.setId(Long.valueOf(data.getSoPhieuXuat().split("/")[0]));
        XhPhieuXkhoBtt created = xhPhieuXkhoBttReposytory.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhPhieuXkhoBtt.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem);
        }
        return created;
    }

    @Override
    public XhPhieuXkhoBtt update(XhPhieuXkhoBttReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhPhieuXkhoBtt> optional = xhPhieuXkhoBttReposytory.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy phiếu xuất kho cần sửa");

        XhPhieuXkhoBtt data = optional.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(getUser().getId());
        XhPhieuXkhoBtt created = xhPhieuXkhoBttReposytory.save(data);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhPhieuXkhoBtt.TABLE_NAME));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhPhieuXkhoBtt.TABLE_NAME);
        created.setFileDinhKem(fileDinhKem);

        return created;
    }

    @Override
    public XhPhieuXkhoBtt detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhPhieuXkhoBtt> optional = xhPhieuXkhoBttReposytory.findById(id);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tìm thấy phiếu xuất kho");

        XhPhieuXkhoBtt data = optional.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho())?null:hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho())?null:hashMapDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho())?null:hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho())?null:hashMapDvi.get(data.getMaLoKho()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

        if(!Objects.isNull(data.getIdNguoiLapPhieu())){
            data.setTenNguoiLapPhieu(userInfoRepository.findById(data.getIdNguoiLapPhieu()).get().getFullName());
        }
        if (!Objects.isNull(data.getNguoiPduyetId())){
            data.setTenNguoiPduyet(userInfoRepository.findById(data.getIdNguoiLapPhieu()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        if (!Objects.isNull(data.getNguoiPduyetId())){
            data.setTenNguoiPduyet(userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        }

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhPhieuXkhoBtt.TABLE_NAME));
        data.setFileDinhKem(fileDinhKem);

        return data;
    }

    @Override
    public XhPhieuXkhoBtt approve(XhPhieuXkhoBttReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }
        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }
        Optional<XhPhieuXkhoBtt> optional = xhPhieuXkhoBttReposytory.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuXkhoBtt data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
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
        xhPhieuXkhoBttReposytory.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }
        Optional<XhPhieuXkhoBtt> optional = xhPhieuXkhoBttReposytory.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(optional.get().getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }
        xhPhieuXkhoBttReposytory.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhPhieuXkhoBtt.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if(StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu ");
        }
        List<XhPhieuXkhoBtt> list = xhPhieuXkhoBttReposytory.findAllById(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhPhieuXkhoBtt hdr : list){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhPhieuXkhoBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhPhieuXkhoBtt> page = this.searchPage(req);
        List<XhPhieuXkhoBtt> data = page.getContent();
        String title="Danh sách phiếu xuất kho bán trực tiếp";
        String[] rowsName = new String[]{"STT","Số QĐ giao NVXH", "Năm KH", "Thời hạn XH trước ngày", "Điển kho", "Lô kho", "Số phiếu xuất kho", "Ngày xuất kho", "Số phiếu KNCL", "Ngày giám định", "Trạng thái"};
        String filename="danh-sach-phieu-xuat-kho-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhPhieuXkhoBtt hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoQdNv();
            objs[2]=hdr.getNamKh();
            objs[3]=hdr.getNgayQdNv();
            objs[4]=hdr.getTenDiemKho();
            objs[5]=hdr.getTenLoKho();
            objs[6]=hdr.getSoPhieuXuat();
            objs[7]=hdr.getNgayXuatKho();
            objs[8]=hdr.getSoPhieu();
            objs[9]=hdr.getNgayKnghiem();
            objs[10]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        try {
            FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bantructiep/Phiếu xuất kho bán trực tiếp.docx");
            XhPhieuXkhoBtt detail = this.detail(DataUtils.safeToLong(body.get("id")));
            XhQdNvXhBttHdr xhQdNvXhBttHdr = xhQdNvXhBttServiceImpI.detail(detail.getIdQdNv());
            detail.setTenDviCungCap(xhQdNvXhBttHdr.getTenTccn());
            detail.setTenDviCha(xhQdNvXhBttHdr.getTenDvi());
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}