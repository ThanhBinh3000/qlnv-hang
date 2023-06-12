package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.tonghop;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.tonghop.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBdgService extends BaseServiceImpl {

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhThopDxKhBdgDtlRepository xhThopDxKhBdgDtlRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    public Page<XhThopDxKhBdg> searchPage(SearchXhThopDxKhBdg req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBdg> data = xhThopDxKhBdgRepository.searchPage(
                req,
                pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:mapDmucVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:mapDmucVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return data;
    }

    public XhThopDxKhBdg sumarryData(XhThopChiTieuReq req, HttpServletRequest servletRequest) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        List<XhDxKhBanDauGia> dataDxList = xhDxKhBanDauGiaRepository.listTongHop(req);
        if (dataDxList.isEmpty()) throw new Exception("Không tìm thấy dữ liệu để tổng hợp");

        XhThopDxKhBdg data = new XhThopDxKhBdg();

        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        data.setNamKh(req.getNamKh());
        data.setLoaiVthh(req.getLoaiVthh());
        data.setTenLoaiVthh(mapDmucVthh.get(req.getLoaiVthh()));
        data.setCloaiVthh(req.getCloaiVthh());
        data.setTenCloaiVthh(mapDmucVthh.get(req.getCloaiVthh()));
        data.setNgayDuyetTu(req.getNgayDuyetTu());
        data.setNgayDuyetDen(req.getNgayDuyetDen());
        data.setMaDvi(userInfo.getDvql());

        List<XhThopDxKhBdgDtl> dataThDtlList = new ArrayList<>();
        for (XhDxKhBanDauGia dataDx : dataDxList) {
            XhThopDxKhBdgDtl dataThDtl = new XhThopDxKhBdgDtl();
            BeanUtils.copyProperties(dataDx,dataThDtl,"id");
            dataThDtl.setIdDxHdr(dataDx.getId());
            dataThDtl.setMaDvi(dataDx.getMaDvi());
            dataThDtl.setTenDvi(mapDmucDvi.get(dataDx.getMaDvi()));
            dataThDtl.setSoDxuat(dataDx.getSoDxuat());
            dataThDtl.setNgayPduyet(dataDx.getNgayPduyet());
            dataThDtl.setTrichYeu(dataDx.getTrichYeu());
            dataThDtl.setSlDviTsan(dataDx.getSlDviTsan());
            dataThDtl.setTrangThai(dataDx.getTrangThai());
            dataThDtl.setTongSoLuong(dataDx.getTongSoLuong());
            dataThDtl.setKhoanTienDatTruoc(dataDx.getKhoanTienDatTruoc());
            this.donGiaDuocDuyet(dataDx.getId(), dataThDtl);
            dataThDtlList.add(dataThDtl);
            data.setLoaiHinhNx(dataDx.getLoaiHinhNx());
            data.setKieuNx(dataDx.getKieuNx());
        }
        data.setChildren(dataThDtlList);
        return data;
    }

    void donGiaDuocDuyet(Long idDx, XhThopDxKhBdgDtl dataThDtl){
        Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(idDx);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Kế hoạch bán đấu giá không tồn tại");
        XhDxKhBanDauGia dataDx = optional.get();
        BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
        if(dataDx.getLoaiVthh().startsWith("02")){
            donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatVt(dataDx.getCloaiVthh(), dataDx.getNamKh());
            if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(dataDx.getTongSoLuong()) && !StringUtils.isEmpty(dataDx.getKhoanTienDatTruoc())){
                BigDecimal tongTienGiaKdTheoDgiaDd = dataDx.getTongSoLuong().multiply(donGiaDuocDuyet);
                BigDecimal tongKhoanTienDtTheoDgiaDd = dataDx.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(dataDx.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                dataThDtl.setTongTienGiaKdTheoDgiaDd(tongTienGiaKdTheoDgiaDd);
                dataThDtl.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
            }
        }else {
            donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatLt(dataDx.getCloaiVthh(), dataDx.getMaDvi(), dataDx.getNamKh());
            if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(dataDx.getTongSoLuong()) && !StringUtils.isEmpty(dataDx.getKhoanTienDatTruoc())){
                BigDecimal tongTienGiaKdTheoDgiaDd = dataDx.getTongSoLuong().multiply(donGiaDuocDuyet);
                BigDecimal tongKhoanTienDtTheoDgiaDd = dataDx.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(dataDx.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                dataThDtl.setTongTienGiaKdTheoDgiaDd(tongTienGiaKdTheoDgiaDd);
                dataThDtl.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
            }
        }
    }

    @Transactional()
    public XhThopDxKhBdg create(XhThopDxKhBdgReq  req, HttpServletRequest servletRequest) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        XhThopDxKhBdg data = sumarryData(req,servletRequest);

        data.setId(req.getIdTh());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(getUser().getId());
        data.setNoiDungThop(req.getNoiDungThop());
        data.setTrangThai(Contains.CHUATAO_QD);
        data.setNgayThop(LocalDate.now());
        data.setMaDvi(userInfo.getDvql());

        xhThopDxKhBdgRepository.save(data);
        for (XhThopDxKhBdgDtl tongHopKeHoachDauGiaDtl : data.getChildren()) {
            tongHopKeHoachDauGiaDtl.setIdThopHdr(data.getId());
            xhThopDxKhBdgDtlRepository.save(tongHopKeHoachDauGiaDtl);
        }
        if (data.getId() > 0 && data.getChildren().size() > 0) {
            List<String> soDxuatList = data.getChildren().stream().map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanDauGiaRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP,data.getId());
        }
        return data;
    }

    @Transactional()
    public XhThopDxKhBdg update(XhThopDxKhBdgReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(Long.valueOf(req.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (req.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(req.getLoaiVthh())){
            throw new Exception("Loại vật tư hành hóa không phù hợp");
        }

        XhThopDxKhBdg data = optional.get();
        XhThopDxKhBdg  dataMap = ObjectMapperUtils.map(req, XhThopDxKhBdg.class);
        dataMap.setNgaySua(LocalDate.now());
        dataMap.setNguoiTaoId(getUser().getId());
        updateObjectToObject(data, dataMap);
        xhThopDxKhBdgRepository.save(data);
        return data;
    }

    public XhThopDxKhBdg detail(String ids) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(Long.parseLong(ids));
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhThopDxKhBdg data = optional.get();

        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        List<XhThopDxKhBdgDtl> dataDtlList = xhThopDxKhBdgDtlRepository.findByIdThopHdr(data.getId());
            dataDtlList.forEach(tongHop -> {
                tongHop.setTenDvi(StringUtils.isEmpty(tongHop.getMaDvi())?null:mapDmucDvi.get(tongHop.getMaDvi()));
                this.donGiaDuocDuyet(tongHop.getIdDxHdr(), tongHop);
            });
            data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:mapDmucVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:mapDmucVthh.get(data.getCloaiVthh()));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            data.setChildren(dataDtlList);
            return data;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(idSearchReq.getId())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

        List<XhThopDxKhBdgDtl> listDls = xhThopDxKhBdgDtlRepository.findByIdThopHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(listDls)) {
            List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
            if (!CollectionUtils.isEmpty(listDxHdr)) {
                listDxHdr.stream().map(item -> {
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    item.setIdThop(null);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
        }
        xhThopDxKhBdgDtlRepository.deleteAll(listDls);
        xhThopDxKhBdgRepository.delete(optional.get());

    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(idSearchReq.getIdList())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        List<XhThopDxKhBdg> listThop = xhThopDxKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBdg thopHdr : listThop) {
            List<XhThopDxKhBdgDtl> listDls = xhThopDxKhBdgDtlRepository.findByIdThopHdr(thopHdr.getId());
            if (!CollectionUtils.isEmpty(listDls)) {
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
                List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
                if (!CollectionUtils.isEmpty(listDxHdr)) {
                    listDxHdr.stream().map(item -> {
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        return item;
                    }).collect(Collectors.toList());
                }
                xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
            }
            xhThopDxKhBdgDtlRepository.deleteAll(listDls);
        }
        xhThopDxKhBdgRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }


    public void export(SearchXhThopDxKhBdg searchReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<XhThopDxKhBdg> page = this.searchPage(searchReq);
        List<XhThopDxKhBdg> data = page.getContent();
        String title = "Danh sách tổng hợp kế hoạch bán đấu giá";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch", "Số QĐ phê duyêt KH BDG ", "Loại hàng hóa", "Trạng thái"};
        String filename = "Tong-hop-de-xuat-ke-hoach-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBdg dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getId();
            objs[2] = dx.getNgayThop();
            objs[3] = dx.getNoiDungThop();
            objs[4] = dx.getNamKh();
            objs[5] = dx.getSoQdPd();
            objs[6] = dx.getTenLoaiVthh();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
