package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.tonghop;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.SearchXhThopDxKhBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBttService extends BaseServiceImpl {

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;

    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;

    @Autowired
    private XhThopDxKhBttDtlRepository xhThopDxKhBttDtlRepository;

    public Page<XhThopDxKhBttHdr> searchPage (SearchXhThopDxKhBtt req) throws Exception{
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBttHdr> data = xhThopDxKhBttRepository.searchPage(
                req,
                pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        data.getContent().forEach( f->{
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:mapDmucVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:mapDmucVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return data;
    }

    public XhThopDxKhBttHdr sumarryData(XhThopChiTieuReq req , HttpServletRequest servletRequest) throws Exception{
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        List<XhDxKhBanTrucTiepHdr>  dataDxuatList = xhDxKhBanTrucTiepHdrRepository.listTongHop(req);
        if (dataDxuatList.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        XhThopDxKhBttHdr data = new XhThopDxKhBttHdr();

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        data.setNamKh(req.getNamKh());
        data.setLoaiVthh(req.getLoaiVthh());
        data.setTenLoaiVthh(listDanhMucHangHoa.get(req.getLoaiVthh()));
        data.setCloaiVthh(req.getCloaiVthh());
        data.setTenCloaiVthh(listDanhMucHangHoa.get(req.getCloaiVthh()));
        data.setNgayDuyetTu(req.getNgayDuyetTu());
        data.setNgayDuyetDen(req.getNgayDuyetDen());
        data.setMaDvi(userInfo.getDvql());
        List<XhThopDxKhBttDtl> dataDtlList = new ArrayList<>();
        for (XhDxKhBanTrucTiepHdr dataDxuat : dataDxuatList) {
            XhThopDxKhBttDtl dataDtl = new XhThopDxKhBttDtl();
            BeanUtils.copyProperties(dataDxuat,dataDtl,"id");
            dataDtl.setIdDxHdr(dataDxuat.getId());
            dataDtl.setMaDvi(dataDxuat.getMaDvi());
            dataDtl.setTenDvi(mapDmucDvi.get(dataDxuat.getMaDvi()));
            dataDtl.setSoDxuat(dataDxuat.getSoDxuat());
            dataDtl.setNgayPduyet(dataDxuat.getNgayPduyet());
            dataDtl.setTrichYeu(dataDxuat.getTrichYeu());
            dataDtl.setSlDviTsan(dataDxuat.getSlDviTsan());
            dataDtl.setTrangThai(dataDxuat.getTrangThai());
            dataDtl.setTongSoLuong(dataDxuat.getTongSoLuong());
            dataDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThai()));
            dataDtlList.add(dataDtl);
            data.setLoaiHinhNx(dataDxuat.getLoaiHinhNx());
            data.setKieuNx(dataDxuat.getKieuNx());
        }
        data.setChildren(dataDtlList);
        return data;
    }

    @Transactional()
    public XhThopDxKhBttHdr create(XhThopDxKhBttHdrReq req, HttpServletRequest servletRequest) throws Exception{
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        XhThopDxKhBttHdr data = sumarryData(req,servletRequest);

        data.setId(req.getIdTh());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(getUser().getId());
        data.setNoiDungThop(req.getNoiDungThop());
        data.setTrangThai(Contains.CHUATAO_QD);
        data.setNgayThop(LocalDate.now());
        data.setMaDvi(userInfo.getDvql());

        xhThopDxKhBttRepository.save(data);

        for (XhThopDxKhBttDtl dataDtl : data.getChildren()){
            dataDtl.setIdThopHdr(data.getId());
            xhThopDxKhBttDtlRepository.save(dataDtl);
        }
        if (data.getId() > 0 && data.getChildren().size() > 0) {
            List<String> soDxuatList = data.getChildren().stream().map(XhThopDxKhBttDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanTrucTiepHdrRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP,data.getId());
        }
        return data;
    }

    @Transactional()
    public XhThopDxKhBttHdr update (XhThopDxKhBttHdrReq req) throws Exception{
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (req.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(req.getLoaiVthh())){
            throw new Exception("Loại vật tư hành hóa không phù hợp");
        }

        XhThopDxKhBttHdr data = optional.get();

        XhThopDxKhBttHdr dataTh = ObjectMapperUtils.map(req, XhThopDxKhBttHdr.class);
        dataTh.setNgaySua(LocalDate.now());
        dataTh.setNguoiTaoId(getUser().getId());
        updateObjectToObject(data, dataTh);
        xhThopDxKhBttRepository.save(data);
        return data;
    }


    public XhThopDxKhBttHdr detail(String ids) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(Long.parseLong(ids));
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhThopDxKhBttHdr data = optional.get();
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        List<XhThopDxKhBttDtl> listTh = xhThopDxKhBttDtlRepository.findByIdThopHdr(data.getId());
        listTh.forEach(tongHop -> {
            tongHop.setTenDvi(StringUtils.isEmpty(tongHop.getMaDvi())?null:mapDmucDvi.get(tongHop.getMaDvi()));
            tongHop.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(tongHop.getTrangThai()));
        });
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:mapDmucVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:mapDmucVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setChildren(listTh);
        return data;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception{
        if (StringUtils.isEmpty(idSearchReq.getId())){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu ");
        }
        Optional<XhThopDxKhBttHdr> qOptional = xhThopDxKhBttRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        XhThopDxKhBttHdr data = qOptional.get();
        List<XhThopDxKhBttDtl> listDtl = xhThopDxKhBttDtlRepository.findByIdThopHdr(data.getId());
        if (!CollectionUtils.isEmpty(listDtl)){
            List<Long> idDxList = listDtl.stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanTrucTiepHdr> listDxHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(idDxList);
            if (!CollectionUtils.isEmpty(listDxHdr)) {
                listDxHdr.stream().map(item -> {
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    item.setIdThop(null);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanTrucTiepHdrRepository.saveAll(listDxHdr);
        }
        xhThopDxKhBttDtlRepository.deleteAll(listDtl);
        xhThopDxKhBttRepository.delete(data);
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<XhThopDxKhBttHdr> listThop = xhThopDxKhBttRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBttHdr thopHdr : listThop) {
            List<XhThopDxKhBttDtl> listDls = xhThopDxKhBttDtlRepository.findByIdThopHdr(thopHdr.getId());
            if (!CollectionUtils.isEmpty(listDls)) {
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
                List<XhDxKhBanTrucTiepHdr> listDxHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(idDxList);
                if (!CollectionUtils.isEmpty(listDxHdr)) {
                    listDxHdr.stream().map(item -> {
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        item.setIdThop(null);
                        return item;
                    }).collect(Collectors.toList());
                }
                xhDxKhBanTrucTiepHdrRepository.saveAll(listDxHdr);
            }
            xhThopDxKhBttDtlRepository.deleteAll(listDls);
        }
        xhThopDxKhBttRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchXhThopDxKhBtt searchReq,  HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<XhThopDxKhBttHdr> page = this.searchPage(searchReq);
        List<XhThopDxKhBttHdr> data = page.getContent();
        String title = "Danh sách thông tin tổng hợp kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp", "Năm kế hoạch", "Số QĐ phê duyệt KH bán trực tiếp", "Loại hàng hóa", "Trạng thái"};
        String filename = "Thong_tin_tong_hop_ke_hoach_ban_truc_tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBttHdr thop = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = thop.getId();
            objs[2] = thop.getNgayThop();
            objs[3] = thop.getNoiDungThop();
            objs[4] = thop.getNamKh();
            objs[5] = thop.getSoQdPd();
            objs[6] = thop.getTenLoaiVthh();
            objs[7] = thop.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
