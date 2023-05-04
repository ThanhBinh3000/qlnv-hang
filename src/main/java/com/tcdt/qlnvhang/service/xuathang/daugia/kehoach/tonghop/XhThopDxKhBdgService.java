package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLoRepository;
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
        Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public XhThopDxKhBdg sumarryData(XhThopChiTieuReq req, HttpServletRequest servletRequest) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        List<XhDxKhBanDauGia> dxuatList = xhDxKhBanDauGiaRepository.listTongHop(
                req.getNamKh(),
                req.getLoaiVthh(),
                req.getCloaiVthh(),
                convertDateToString(req.getNgayDuyetTu()),
                convertDateToString(req.getNgayDuyetDen()));
        if (dxuatList.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        XhThopDxKhBdg thopHdr = new XhThopDxKhBdg();

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        thopHdr.setNamKh(req.getNamKh());
        thopHdr.setLoaiVthh(req.getLoaiVthh());
        thopHdr.setTenLoaiVthh(listDanhMucHangHoa.get(req.getLoaiVthh()));
        thopHdr.setCloaiVthh(req.getCloaiVthh());
        thopHdr.setTenCloaiVthh(listDanhMucHangHoa.get(req.getCloaiVthh()));
        thopHdr.setNgayDuyetTu(req.getNgayDuyetTu());
        thopHdr.setNgayDuyetDen(req.getNgayDuyetDen());
        thopHdr.setMaDvi(userInfo.getDvql());

        List<XhThopDxKhBdgDtl> thopDtls = new ArrayList<>();

        for (XhDxKhBanDauGia dxuat : dxuatList) {
            XhThopDxKhBdgDtl thopDtl = new XhThopDxKhBdgDtl();
            BeanUtils.copyProperties(dxuat,thopDtl,"id");
            thopDtl.setIdDxHdr(dxuat.getId());
            thopDtl.setMaDvi(dxuat.getMaDvi());
            thopDtl.setTenDvi(mapDmucDvi.get(dxuat.getMaDvi()));
            thopDtl.setSoDxuat(dxuat.getSoDxuat());
            thopDtl.setNgayPduyet(dxuat.getNgayPduyet());
            thopDtl.setTrichYeu(dxuat.getTrichYeu());
            thopDtl.setSlDviTsan(dxuat.getSlDviTsan());
            thopDtl.setTrangThai(dxuat.getTrangThai());
            thopDtl.setTongSoLuong(dxuat.getTongSoLuong());
            thopDtl.setKhoanTienDatTruoc(dxuat.getKhoanTienDatTruoc());
            BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
            if(dxuat.getLoaiVthh().startsWith("02")){
                donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatVt(dxuat.getCloaiVthh(), dxuat.getNamKh());
                if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(dxuat.getTongSoLuong()) && !StringUtils.isEmpty(dxuat.getKhoanTienDatTruoc())){
                    BigDecimal giaKdTheoDonGiaDd =dxuat.getTongSoLuong().multiply(donGiaDuocDuyet);
                    BigDecimal khoanTienDtruocTheoDgiaDd = dxuat.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(dxuat.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                    thopDtl.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                    thopDtl.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                }
            }else {
                donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatLt(dxuat.getCloaiVthh(), dxuat.getMaDvi(), dxuat.getNamKh());
                if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(dxuat.getTongSoLuong()) && !StringUtils.isEmpty(dxuat.getKhoanTienDatTruoc())){
                    BigDecimal giaKdTheoDonGiaDd =dxuat.getTongSoLuong().multiply(donGiaDuocDuyet);
                    BigDecimal khoanTienDtruocTheoDgiaDd = dxuat.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(dxuat.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                    thopDtl.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                    thopDtl.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                }
            }
            thopDtls.add(thopDtl);
            thopHdr.setLoaiHinhNx(dxuat.getLoaiHinhNx());
            thopHdr.setKieuNx(dxuat.getKieuNx());
        }
        thopHdr.setChildren(thopDtls);
        return thopHdr;
    }

    @Transactional()
    public XhThopDxKhBdg create(XhThopDxKhBdgReq  req, HttpServletRequest servletRequest) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        XhThopDxKhBdg thopHdr = sumarryData(req,servletRequest);
        thopHdr.setId(req.getIdTh());
        thopHdr.setNgayTao(new Date());
        thopHdr.setNguoiTaoId(getUser().getId());
        thopHdr.setNoiDungThop(req.getNoiDungThop());
        thopHdr.setTrangThai(Contains.CHUATAO_QD);
        thopHdr.setNgayThop(new Date());
        thopHdr.setMaDvi(userInfo.getDvql());
        xhThopDxKhBdgRepository.save(thopHdr);
        for (XhThopDxKhBdgDtl dtl : thopHdr.getChildren()) {
            dtl.setIdThopHdr(thopHdr.getId());
            xhThopDxKhBdgDtlRepository.save(dtl);
        }
        if (thopHdr.getId() > 0 && thopHdr.getChildren().size() > 0) {
            List<String> soDxuatList = thopHdr.getChildren().stream().map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanDauGiaRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP,thopHdr.getId());
        }
        return thopHdr;
    }


    @Transactional()
    public XhThopDxKhBdg update(XhThopDxKhBdgReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception(" Sửa thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
            throw new Exception("Loại vật tư hành hóa không phù hợp");

        XhThopDxKhBdg dataDTB = qOptional.get();
        XhThopDxKhBdg dataMap = ObjectMapperUtils.map(objReq, XhThopDxKhBdg.class);
        dataMap.setNgaySua(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        updateObjectToObject(dataDTB, dataMap);
        xhThopDxKhBdgRepository.save(dataDTB);
        return dataDTB;
    }

    public XhThopDxKhBdg detail(String ids) throws Exception {
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhThopDxKhBdg hdrThop = qOptional.get();

//      Lay danh muc chung
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();

        hdrThop.setTenLoaiVthh(hashMapDmHh.get(hdrThop.getLoaiVthh()));
        hdrThop.setTenCloaiVthh(hashMapDmHh.get(hdrThop.getCloaiVthh()));

        List<XhThopDxKhBdgDtl> listTh = xhThopDxKhBdgDtlRepository.findByIdThopHdr(hdrThop.getId());
        Map<String, String> mapDmucDvi = getListDanhMucDvi(Contains.CAP_CUC,null,"01");
        listTh.forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
            BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
            if(hdrThop.getLoaiVthh().startsWith("02")){
                donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatVt(hdrThop.getCloaiVthh(), hdrThop.getNamKh());
                if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(f.getTongSoLuong()) && !StringUtils.isEmpty(f.getKhoanTienDatTruoc())){
                    BigDecimal giaKdTheoDonGiaDd = f.getTongSoLuong().multiply(donGiaDuocDuyet);
                    BigDecimal khoanTienDtruocTheoDgiaDd = f.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(f.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                    f.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                    f.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                }
            }else {
                donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatLt(hdrThop.getCloaiVthh(), f.getMaDvi(), hdrThop.getNamKh());
                if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(f.getTongSoLuong()) && !StringUtils.isEmpty(f.getKhoanTienDatTruoc())){
                    BigDecimal giaKdTheoDonGiaDd =f.getTongSoLuong().multiply(donGiaDuocDuyet);
                    BigDecimal khoanTienDtruocTheoDgiaDd = f.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(f.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                    f.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                    f.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                }
            }
        });
        hdrThop.setChildren(listTh);
        return hdrThop;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
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
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
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
