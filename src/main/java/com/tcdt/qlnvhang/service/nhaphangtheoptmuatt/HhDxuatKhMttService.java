package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhMttCcxdg;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhMttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.HhSlNhapHangRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttCcxdgRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttSlddDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttSlddRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.object.HhSlNhapHangReq;
import com.tcdt.qlnvhang.service.HhSlNhapHangService;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttCcxdg;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSldd;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSlddDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhDxuatKhMttService extends BaseServiceImpl {
    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhSlNhapHangService hhSlNhapHangService;

    @Autowired
    private HhDxuatKhMttSlddRepository hhDxuatKhMttSlddRepository;

    @Autowired
    private HhDxuatKhMttCcxdgRepository hhDxuatKhMttCcxdgRepository;
    @Autowired
    private HhDxuatKhMttSlddDtlRepository hhDxuatKhMttSlddDtlRepository;
    @Autowired
    private HhSlNhapHangRepository hhSlNhapHangRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<HhDxuatKhMttHdr> searchPage(SearchHhDxKhMttHdrReq req)throws Exception{
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxuatKhMttHdr> data = hhDxuatKhMttRepository.searchPage(
                 req,
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();

        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Transactional
    public HhDxuatKhMttHdr create(HhDxuatKhMttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");


        if(!StringUtils.isEmpty(req.getSoDxuat())){
            Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findBySoDxuat(req.getSoDxuat());
            if (qOptional.isPresent()){
                throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
            }
        }

        HhDxuatKhMttHdr dataMap = new ModelMapper().map(req, HhDxuatKhMttHdr.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiTh(Contains.CHUATONGHOP);
        dataMap.setNguoiTaoId(userInfo.getId());
        this.validateData(dataMap,dataMap.getTrangThai());
        hhDxuatKhMttRepository.save(dataMap);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKemReq())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), dataMap.getId(), HhDxuatKhMttHdr.TABLE_NAME + "_CAN_CU");
        }

        this.saveDetail(req,dataMap.getId());

        // Lưu quyết định căn cứ
        for (HhDxuatKhMttCcxdgReq cc : req.getCcXdgReq()){
            HhDxuatKhMttCcxdg data = ObjectMapperUtils.map(cc, HhDxuatKhMttCcxdg.class);
            List<FileDKemJoinDxKhMttCcxdg> detailChild = new ArrayList<>();
            if (data.getChildren() != null) {
                detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhMttCcxdg.class);
                detailChild.forEach(f -> {
                    f.setDataType(HhDxuatKhMttCcxdg.TABLE_NAME);
                    f.setCreateDate(new Date());
                });
            }
            data.setChildren(detailChild);
            data.setIdHdr(dataMap.getId());

            hhDxuatKhMttCcxdgRepository.save(data);
        }
        return dataMap;
    }

  @Transactional
  void saveDetail(HhDxuatKhMttHdrReq req, Long idHdr){
        hhDxuatKhMttSlddRepository.deleteAllByIdHdr(idHdr);
        for ( HhDxuatKhMttSlddReq slddReq: req.getChildren()){
            HhDxuatKhMttSldd sldd = new HhDxuatKhMttSldd();
            BeanUtils.copyProperties(slddReq, sldd, "id");
            sldd.setId(null);
            sldd.setIdHdr(idHdr);
            hhDxuatKhMttSlddRepository.save(sldd);
            hhDxuatKhMttSlddDtlRepository.deleteAllByIdDtl(sldd.getId());
            for (HhDxuatKhMttSlddDtlReq slddDtlReq: slddReq.getChildren()){
                HhDxuatKhMttSlddDtl slddDtl = new HhDxuatKhMttSlddDtl();
                BeanUtils.copyProperties(slddDtlReq, slddDtl, "id");
                slddDtl.setIdDtl(sldd.getId());
                hhDxuatKhMttSlddDtlRepository.save(slddDtl);
            }
        }
  }

    public void validateData(HhDxuatKhMttHdr objHdr,String trangThai) throws Exception {
//            if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
//                HhDxuatKhMttHdr dXuat = hhDxuatKhMttRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKh(), objHdr.getMaDvi(),NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//                if(!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())){
//                    throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
//                }
//            }
        if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId())){
            for (HhDxuatKhMttSldd child : objHdr.getChildren()) {
                CountKhlcntSlReq slReq = new CountKhlcntSlReq();
                slReq.setLoaiVthh(objHdr.getLoaiVthh());
                slReq.setYear(objHdr.getNamKh());
                slReq.setMaDvi(child.getMaDvi());
                BigDecimal slKhNamTheoKh = hhSlNhapHangService.countSoLuongKeHoachNamTheoKh(slReq);
                if(child.getTongSoLuong().add(slKhNamTheoKh).compareTo(child.getSoLuongChiTieu()) == 1){
                    throw new Exception("Số lượng đề xuất của + " + child.getTenDvi() + " đã vượt quá số lượng chỉ tiêu kế hoạch");
                }
            }


        }
            if(trangThai.equals(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId())) {
                for(HhDxuatKhMttSldd chiCuc : objHdr.getChildren()){
                    BigDecimal aLong = hhDxuatKhMttRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), chiCuc.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
                    BigDecimal soLuongTotal = aLong.add(chiCuc.getSoLuong());
                    if (chiCuc.getSoLuongChiTieu() == null){
                        throw new Exception("Hiện chưa có số lượng chỉ tiêu kế hoạch năm, vui lòng nhập lại");
                    }
                    if(soLuongTotal.compareTo(chiCuc.getSoLuongChiTieu()) > 0){
                        throw new Exception(chiCuc.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
                    }
                }
            }
    }

    @Transactional
    public HhDxuatKhMttHdr update(HhDxuatKhMttHdrReq req) throws  Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        if (StringUtils.isEmpty(req.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findById(Long.valueOf(req.getId()));
        if (!qOptional.isPresent())
            throw new Exception("không tìm thấy dữ liệu cần sửa");

        if (!StringUtils.isEmpty(req.getSoDxuat()) && !req.getSoDxuat().equals(qOptional.get().getSoDxuat())){
            Optional<HhDxuatKhMttHdr> deXuat = hhDxuatKhMttRepository.findBySoDxuat(req.getSoDxuat());
            if (deXuat.isPresent()){
                if (!deXuat.get().getId().equals(req.getId())){
                    throw new Exception("Số đề xuất" + req.getSoDxuat()+ "đã tồn tại");
                }
            }
        }

        List<FileDKemJoinDxKhMttHdr> fileDinhKemList = new ArrayList<>();
        if (req.getFileDinhKemReq() != null){
            fileDinhKemList= ObjectMapperUtils.mapAll(req.getFileDinhKemReq(), FileDKemJoinDxKhMttHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhMttHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhDxuatKhMttHdr dataDTB = qOptional.get();
        HhDxuatKhMttHdr dataMap = ObjectMapperUtils.map(req, HhDxuatKhMttHdr.class);
        updateObjectToObject(dataDTB, dataMap);
        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSuaId(userInfo.getId());
        this.validateData(dataDTB,dataDTB.getTrangThai());

        hhDxuatKhMttRepository.save(dataDTB);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKemReq())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), dataDTB.getId(), HhDxuatKhMttHdr.TABLE_NAME + "_CAN_CU");
        }

        this.saveDetail(req,dataDTB.getId());

        hhDxuatKhMttCcxdgRepository.deleteAllByIdHdr(dataDTB.getId());
        for (HhDxuatKhMttCcxdgReq cc : req.getCcXdgReq()){
            HhDxuatKhMttCcxdg data = ObjectMapperUtils.map(cc, HhDxuatKhMttCcxdg.class);
            List<FileDKemJoinDxKhMttCcxdg> detailChild = new ArrayList<>();
            if (data.getChildren() != null) {
                detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhMttCcxdg.class);
                detailChild.forEach(f -> {
                    f.setDataType(HhDxuatKhMttCcxdg.TABLE_NAME);
                    f.setCreateDate(new Date());
                });
            }
            data.setChildren(detailChild);
            data.setIdHdr(dataDTB.getId());

            hhDxuatKhMttCcxdgRepository.save(data);
        }

        return dataDTB;
    }

    public HhDxuatKhMttHdr detail(Long ids) throws Exception{
        Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findById(ids);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        HhDxuatKhMttHdr data = qOptional.get();

        data.setTenLoaiVthh( StringUtils.isEmpty(data.getLoaiVthh()) ? null : mapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh( StringUtils.isEmpty(data.getCloaiVthh()) ? null :mapVthh.get(data.getCloaiVthh()));
        data.setTenDvi( StringUtils.isEmpty(data.getMaDvi()) ? null :mapDmucDvi.get(data.getMaDvi()));
        data.setCcXdgDtlList(hhDxuatKhMttCcxdgRepository.findAllByIdHdr(data.getId()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Collections.singletonList(HhDxuatKhMttHdr.TABLE_NAME + "_CAN_CU"));
        data.setFileDinhKems(fileDinhKem);

        List<HhDxuatKhMttSldd> dsSlDdList = hhDxuatKhMttSlddRepository.findAllByIdHdr(qOptional.get().getId());
        for(HhDxuatKhMttSldd dsG : dsSlDdList){
            dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
//            dsG.setSoLuongKhDd(hhSlNhapHangRepository.countSLDalenQd(data.getNamKh(), data.getLoaiVthh(), dsG.getMaDvi()));
            List<HhDxuatKhMttSlddDtl> listDdNhap = hhDxuatKhMttSlddDtlRepository.findAllByIdDtl(dsG.getId());
            listDdNhap.forEach( f -> {
                f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : mapDmucDvi.get(f.getMaDiemKho()));
            });
            dsG.setChildren(listDdNhap);
        }
        qOptional.get().setChildren (dsSlDdList);
        return qOptional.get();
    }

    @Transactional
    public HhDxuatKhMttHdr approve(StatusReq stReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(stReq.getId()))
            throw new Exception("Không tìm thấy dữ liệu");

        HhDxuatKhMttHdr optional = this.detail(stReq.getId());
        String status = stReq.getTrangThai() + optional.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
            case Contains.CHODUYET_TP + Contains.TU_CHOI_CBV:
                this.validateData(optional,Contains.CHODUYET_TP);
                optional.setNguoiGuiDuyetId(userInfo.getId());
                optional.setNgayGuiDuyet(getDateTimeNow());
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.TU_CHOI_CBV + Contains.DADUYET_LDC:
                optional.setNguoiPduyetId(userInfo.getId());
                optional.setNgayPduyet(getDateTimeNow());
                optional.setLyDoTuChoi(stReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
            case Contains.DA_DUYET_CBV + Contains.DADUYET_LDC:
                this.validateData(optional,stReq.getTrangThai());
                optional.setNguoiPduyetId(userInfo.getId());
                optional.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }

        optional.setTrangThai(stReq.getTrangThai());
        hhDxuatKhMttRepository.save(optional);
        if(optional.getTrangThai().equals(Contains.DA_DUYET_LDC)){
            HhSlNhapHangReq hhSlNhapHangReq = new HhSlNhapHangReq();
            BeanUtils.copyProperties(optional, hhSlNhapHangReq, "id");
            hhSlNhapHangReq.setKieuNhap(Contains.NHAP_TRUC_TIEP);
            hhSlNhapHangReq.setIdDxKhlcnt(optional.getId());
            hhSlNhapHangReq.setNamKhoach(optional.getNamKh());
            List<HhDxuatKhMttSldd> slddList = hhDxuatKhMttSlddRepository.findAllByIdHdr(optional.getId());
            for (HhDxuatKhMttSldd hhDxuatKhMttSldd : slddList) {
                hhSlNhapHangReq.setSoLuong(hhDxuatKhMttSldd.getTongSoLuong());
                hhSlNhapHangReq.setMaDvi(hhDxuatKhMttSldd.getMaDvi());
                hhSlNhapHangService.create(hhSlNhapHangReq);
            }
        }
        return optional;
    }

    public void export(SearchHhDxKhMttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhDxuatKhMttHdr> page = this.searchPage(req);
        List<HhDxuatKhMttHdr> data = page.getContent();
        String title = " Danh sách đề xuất kế hoạch mua trực tiếp ";
        String[] rowsName = new String[]{"STT", "Số kế hoạch/đề xuất","Năm kế hoạch", "Ngày tạo", "Ngày phê duyệt", "Trích yếu", "Số QĐ giao chỉ tiêu", "Loại hàng hóa", "Chủng loại hàng hóa", "Số lượng (tấn)", "Số QĐ duyệt KH mua trực tiếp", "Trạng thái đề xuất", "Trạng thái tổng hợp"};
        String fileName = "Danh-sach-de-xuat-ke-hoach-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxuatKhMttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoDxuat();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayTao();
            objs[4] = hdr.getNgayPduyet();
            objs[5] = hdr.getTrichYeu();
            objs[6] = hdr.getSoQdCc();
            objs[7] = hdr.getTenLoaiVthh();
            objs[8] = hdr.getTenCloaiVthh();
            objs[9] = hdr.getTongSoLuong();
            objs[10] = hdr.getSoQdPduyet();
            objs[11] = hdr.getTenTrangThai();
            objs[12] = hdr.getTenTrangThaiTh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }



    public void delete(IdSearchReq idSearchReq) throws Exception{
      if (StringUtils.isEmpty(idSearchReq.getId()))
          throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
      Optional<HhDxuatKhMttHdr> optional = hhDxuatKhMttRepository.findById(idSearchReq.getId());

      if (!optional.isPresent()){
          throw new Exception("Không tìm thấy dữ liệu cần xoá");
      }
      if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
              && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)
              && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
              && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
          throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
      }
      List<HhDxuatKhMttSldd> goiThau = hhDxuatKhMttSlddRepository.findAllByIdHdr(idSearchReq.getId());
      if(goiThau != null && goiThau.size() > 0){
          for (HhDxuatKhMttSldd ct : goiThau) {
              List<HhDxuatKhMttSlddDtl> dsCtiet = hhDxuatKhMttSlddDtlRepository.findAllByIdDtl(ct.getId());
              hhDxuatKhMttSlddDtlRepository.deleteAll(dsCtiet);

          }
          hhDxuatKhMttSlddRepository.deleteAll(goiThau);
      }
      hhDxuatKhMttSlddRepository.deleteAllByIdHdr(idSearchReq.getId());
      List<HhDxuatKhMttCcxdg> cc=hhDxuatKhMttCcxdgRepository.findAllByIdHdr(idSearchReq.getId());
      if (cc != null && cc.size() > 0){
          hhDxuatKhMttCcxdgRepository.deleteAll(cc);
      }
      hhDxuatKhMttRepository.delete(optional.get());
  }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhDxuatKhMttHdr> listDx = hhDxuatKhMttRepository.findByIdIn(idSearchReq.getIdList());
        if (listDx.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhDxuatKhMttHdr dx:listDx) {
            if (!dx.getTrangThai().equals(Contains.DUTHAO)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_LDV)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            List<HhDxuatKhMttSldd> goiThau = hhDxuatKhMttSlddRepository.findAllByIdHdr(dx.getId());
            List<Long> listGoiThau = goiThau.stream().map(HhDxuatKhMttSldd::getId).collect(Collectors.toList());
            hhDxuatKhMttSlddDtlRepository.deleteAllByIdDtlIn(listGoiThau);
        }
        hhDxuatKhMttSlddRepository.deleteAllByIdHdrIn(idSearchReq.getIdList());
        hhDxuatKhMttCcxdgRepository.deleteAllByIdHdrIn(idSearchReq.getIdList());
        hhDxuatKhMttRepository.deleteAllByIdIn(idSearchReq.getIdList());

    }


    public BigDecimal countSoLuongKeHoachNam(CountKhMttSlReq req) throws Exception {
        return hhDxuatKhMttRepository.countSLDalenKh(req.getYear(),req.getLoaiVthh(), req.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
    }
}
