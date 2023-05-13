package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReposytory;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReq;
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
public class XhPhieuXkhoBttServiceImpl extends BaseServiceImpl implements XhPhieuXkhoBttService {

    @Autowired
    private XhPhieuXkhoBttReposytory xhPhieuXkhoBttReposytory;

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
    public XhPhieuXkhoBtt create(XhPhieuXkhoBttReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        XhPhieuXkhoBtt data = new XhPhieuXkhoBtt();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(getDateTimeNow());
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
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        if (StringUtils.isEmpty(req.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<XhPhieuXkhoBtt> qOptional = xhPhieuXkhoBttReposytory.findById(req.getId());
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        XhPhieuXkhoBtt dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(getUser().getId());
        XhPhieuXkhoBtt created = xhPhieuXkhoBttReposytory.save(dataDB);

        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhPhieuXkhoBtt.TABLE_NAME));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhPhieuXkhoBtt.TABLE_NAME);
        created.setFileDinhKem(fileDinhKem);

        return created;
    }

    @Override
    public XhPhieuXkhoBtt detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id))
            throw new Exception("Không tồn tại bản ghi");

        Optional<XhPhieuXkhoBtt> qOptional = xhPhieuXkhoBttReposytory.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhPhieuXkhoBtt data = qOptional.get();

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));


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
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenNganKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(hashMapDvi.get(data.getMaLoKho()));

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
}
