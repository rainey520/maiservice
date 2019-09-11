package com.ruoyi.project.device.api.service;

import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.device.api.form.ApiWorkForm;
import com.ruoyi.project.device.api.form.WorkDataForm;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devDeviceCounts.domain.DevDataLog;
import com.ruoyi.project.device.devDeviceCounts.mapper.DevDataLogMapper;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devIo.mapper.DevIoMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.ser.mapper.SerMapper;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.mapper.SerPortMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class InitDataManageServiceImpl implements IInitDataManageService {

    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private DevIoMapper devIoMapper;


    @Autowired
    private DevCompanyMapper devCompanyMapper;


    @Autowired
    private DevDataLogMapper devDataLogMapper;

    @Autowired
    private SerPortMapper serPortMapper;

    @Autowired
    private SerMapper serMapper;

    /**
     * 根据硬件编码查询对应的工单信息
     * @param code 硬件编码
     * @return
     */
    @Override
    public Map<String, Object> workOrder(String code) {
        Map<String,Object> map = new HashMap<>();
        try {
            //判断对应的硬件编码是否存在
            DevList devList = devListMapper.selectDevListByCode(code);
            if(devList == null || devList.getCompanyId() ==null){
                map.put("code",3);//硬件编码不存在
                map.put("data",null);
                return  map;
            }
            //查询对应硬件产线配置信息
            DevIo devIo = devIoMapper.selectByDevId(devList.getId());
            if(devIo == null || devIo.getLineId() == null){
                map.put("code",4);//硬件未配置产线
                map.put("data",null);
                return  map;
            }
            ApiWorkForm workForm = findLineAndWork(devList,devIo);
            if(workForm == null){
                map.put("code",2);//硬件为归属公司或者硬件未配置产线
                map.put("data",null);
                return map;
            }
            map.put("code",1);
            map.put("data",workForm);
        }catch (Exception e){
               map.put("code",0);
               map.put("data",null);
        }
        return map;
    }

    @Override
    public Map<String, Object> workData(WorkDataForm data) {
        Map<String,Object> map = new HashMap<>();
//        try {
//            Integer ids[] ={data.getD1(),data.getD2(),data.getD3(),data.getD4(),data.getD5(),data.getD6(),data.getD7(),data.getD8()};
//            //判断对应的硬件编码是否存在
//            DevList devList = devListMapper.selectDevListByCode(data.getCode());
//            if(devList == null || devList.getCompanyId() ==null){
//                map.put("code",3);//硬件编码不存在
//                map.put("status",0);//没有正在进行的工单
//                map.put("num",0);//没有正在进行的工单
//                map.put("workCode",null);//工单编号为空
//                return  map;
//            }
//            //查询对应硬件产线配置信息
//            DevIo devIo = devIoMapper.selectByDevId(devList.getId());
//            if(devIo == null || devIo.getLineId() == null){
//                map.put("code",4);//硬件未配置产线
//                map.put("status",0);//没有正在进行的工单
//                map.put("num",0);//没有正在进行的工单
//                map.put("workCode",null);//工单编号为空
//                return  map;
//            }
//            int j = 1;
//            DevDataLog devDataLog = null;
//            for (Integer val : ids) {
//                try {
//                    devDataLog = new DevDataLog();
//                    devDataLog.setCompanyId(devList.getCompanyId());
//                    devDataLog.setDataTotal(val);
//                    devDataLog.setDevId(devList.getId());
//                    devDataLog.setDelData(0);
//                    //查询对应的顺序的IO口
//                    DevIo io = devIoMapper.selectDevIoByDevIdAndOrder(devList.getId(),j);
//                    devDataLog.setIoOrder(j);
//                    if(io != null){
//                        devDataLog.setIoId(io.getId());
//                        if(io.getLineId() >0){
//                            //查询对应的产线是否存在
//                            ProductionLine line = productionLineMapper.selectProductionLineById(io.getLineId());
//                            if(line != null)devDataLog.setLineId(line.getId());
//                            //查询对应产线正在工作的工单
//                            DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByCompandAndLine(devList.getCompanyId(),io.getLineId());
//                            //工单正在进行，并且工单是开始状态
//                            if(workOrder != null && workOrder.getOperationStatus() == WorkConstants.OPERATION_STATUS_STARTING){
//                                devDataLog.setWorkId(workOrder.getId());
//                                //对相关数据进行记录
//                                DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(devList.getCompanyId(),io.getLineId(),workOrder.getId(),
//                                        devList.getId(),io.getId());
//                                if(workData != null){
//                                    if(io.getIsSign() == 1){
//                                        workData.setIoSign(1);//标记数据 为报表数据
//                                    }else{
//                                        workData.setIoSign(0);
//                                    }
//                                    //记录累计产量
//                                    devWorkDataMapper.saveTotalWorkData(workData.getDataId(),val,workData.getIoSign());
//                                    workOrder.setCumulativeNumber(val);
//                                    devWorkOrderMapper.updateDevWorkOrder(workOrder);
//                                }
//                                //添加日志
//                                if(workData != null  && workOrder.getOperationStatus() == WorkConstants.OPERATION_STATUS_STARTING && devDataLog.getLineId() != null && devDataLog.getWorkId() != null){
//                                    //查询对应日志上传数据数据
//                                    DevDataLog log = devDataLogMapper.selectLineWorkDevIo(devDataLog.getLineId(),devDataLog.getWorkId(),devDataLog.getDevId(),devDataLog.getIoId());
//                                    if(log != null){
//                                        devDataLog.setDelData(devDataLog.getDataTotal() - log.getDataTotal());
//                                    }
//                                }
//                            }else if(workOrder == null){
//                                //查询最近完成工单信息
//                                workOrder = devWorkOrderMapper.selectLatelyCompleteWork(devList.getCompanyId(),io.getLineId());
//                                if(workOrder != null && workOrder.getSign() == 1){
//                                    //对相关数据进行记录
//                                    DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(devList.getCompanyId(),io.getLineId(),workOrder.getId(),
//                                            devList.getId(),io.getId());
//                                    if(workData != null){
//                                        if(io.getIsSign() == 1){
//                                            workData.setIoSign(1);//标记数据 为报表数据
//                                        }else{
//                                            workData.setIoSign(0);
//                                        }
//                                        //记录累计产量
//                                        devWorkDataMapper.saveTotalWorkData(workData.getDataId(),val,workData.getIoSign());
//                                        workOrder.setCumulativeNumber(val);
//                                        devWorkOrderMapper.updateDevWorkOrder(workOrder);
//                                    }
//                                    //添加日志
//                                    if(workData != null  && workOrder.getOperationStatus() == WorkConstants.OPERATION_STATUS_STARTING && devDataLog.getLineId() != null && devDataLog.getWorkId() != null){
//                                        //查询对应日志上传数据数据
//                                        DevDataLog log = devDataLogMapper.selectLineWorkDevIo(devDataLog.getLineId(),devDataLog.getWorkId(),devDataLog.getDevId(),devDataLog.getIoId());
//                                        if(log != null){
//                                            devDataLog.setDelData(devDataLog.getDataTotal() - log.getDataTotal());
//                                        }
//                                    }
//                                    devWorkOrderMapper.editLatelyCompleteWorkSign(workOrder.getId());
//                                }
//                            }
//                        }
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                devDataLog.setCreateDate(new Date());
//                devDataLog.setCreateTime(new Date());
//                devDataLogMapper.insertDevDataLog(devDataLog);
//                j++;
//            }
//
//            ApiWorkForm workForm = findLineAndWork(devList,devIo);
//            if(workForm == null || StringUtils.isEmpty(workForm.getWorkCode())){
//                map.put("code",2);//硬件为归属公司或者硬件未配置产线
//                map.put("status",0);//没有正在进行的工单
//                map.put("num",0);//没有正在进行的工单
//                map.put("workCode",null);//工单编号为空
//                return map;
//            }
//            map.put("code",1);//成功
//            map.put("status",0);//没有正在进行的工单
//            map.put("num",0);//没有正在进行的工单
//            map.put("workCode",null);//工单编号为空
//            //查询对应工单的累计产量
//            DevWorkData workData = devWorkDataMapper.selectWorkDataByCompanyLineWorkDev(workForm.getCompanyId(),workForm.getLineId(),
//                    workForm.getWorkId(),devList.getId(),devIo.getId());
//            if(workData != null && workData.getCumulativeNum() != null){
//                map.put("num",workData.getCumulativeNum());//没有正在进行的工单
//                map.put("status",workForm.getOp());//工单正在进行
//                map.put("workCode",workForm.getWorkCode());//工单编号为空
//            }
//            return map;
//        }catch (Exception e){
//            map.put("code",0);//异常错误
//            map.put("status",0);//没有正在进行的工单
//            map.put("num",0);//没有正在进行的工单
//            map.put("workCode",null);//工单编号为空
//        }
        return map;
    }

    /**
     * 异常上报
     * @param code 硬件编码
     * @return
     */
    @Override
    public Map<String, Object> workEx(String code) {
        Map<String,Object> map = new HashMap<>();
//        try {
//            //判断对应的硬件编码是否存在
//            DevList devList = devListMapper.selectDevListByCode(code);
//            if(devList == null || devList.getCompanyId() ==null){
//                map.put("code",3);//硬件编码不存在
//                map.put("status",0);//上报失败
//                return  map;
//            }
//            //查询对应硬件产线配置信息
//            DevIo devIo = devIoMapper.selectByDevId(devList.getId());
//            if(devIo == null || devIo.getLineId() == null){
//                map.put("code",4);//硬件未配置产线
//                map.put("status",0);//上报失败
//                return  map;
//            }
//            ApiWorkForm workForm = findLineAndWork(devList,devIo);
//            if(workForm == null || workForm.getWorkId() == null){
//                map.put("code",2);//不存在对应工单信息
//                map.put("status",0);//上报失败
//                return  map;
//            }
//            //查询对应异常类型是否存在
//            WorkExceptionType exceptionType = exceptionTypeMapper.selectByCompanyAndTypeName(workForm.getCompanyId(),"异常上报");
//            if(exceptionType == null){
//                exceptionType = new WorkExceptionType();
//                exceptionType.setCompanyId(workForm.getCompanyId());
//                exceptionType.setTypeName("异常上报");
//                exceptionType.setDefId(0);
//                exceptionType.setCreateTime(new Date());
//                exceptionTypeMapper.insertWorkExceptionType(exceptionType);
//            }
//            //添加异常信息
//            WorkExceptionList exceptionList = new WorkExceptionList();
//            exceptionList.setCompanyId(workForm.getCompanyId());
//            exceptionList.setExceStatut(0);
//            exceptionList.setExceType(exceptionType.getId());
//            exceptionList.setWorkId(workForm.getWorkId());
//            exceptionList.setRemark("设备ID："+code+",上报异常");
//            exceptionList.setCreateTime(new Date());
//            workExceptionListMapper.insertWorkExceptionList(exceptionList);
//            map.put("code",1);//创建成功
//            map.put("status",1);//上报成功
//        }catch (Exception e){
//            map.put("code",0);//异常错误
//            map.put("status",0);//上报失败
//        }
        return map;
    }

    /**
     * 根据硬件查询对应的产线信息和正在进行的工单信息
     * @param devIo
     * @return
     */
    private ApiWorkForm findLineAndWork(DevList devList,DevIo devIo) throws Exception{
        ApiWorkForm workForm = new ApiWorkForm();
//        //查询公司信息
//        DevCompany devCompany = devCompanyMapper.selectDevCompanyById(devList.getCompanyId());
//        if(devCompany ==null)return null;
//        workForm.setCompanyId(devCompany.getCompanyId());
//        workForm.setCompanyLogo(devCompany.getComLogo());
//        workForm.setCompanyName(devCompany.getComName());
//        //查询对应的产线信息
//        ProductionLine line = productionLineMapper.selectProductionLineById(devIo.getLineId());
//        if(line == null)return  null;
//        workForm.setLineName(line.getLineName());
//        workForm.setLineId(line.getId());
//        //查询对应产线正在进行的工单信息
//        DevWorkOrder workOrder = workOrderMapper.selectWorkByCompandAndLine(devCompany.getCompanyId(),line.getId());
//        if(workOrder != null){
//            workForm.setWorkId(workOrder.getId());
//            workForm.setWorkCode(workOrder.getWorkorderNumber());//工单号
//            workForm.setProductCode(workOrder.getProductCode());//产品编码
//            workForm.setProductName(workOrder.getProductName());//产品名称
//            workForm.setWorkNumber(workOrder.getProductNumber());
//            workForm.setWorkorderStatus(workOrder.getWorkorderStatus()); // 生产状态
//            workForm.setOp(workOrder.getOperationStatus());
//        }
//        // 查询对应工单累计生产产量信息
//        DevWorkData workData = devWorkDataMapper.selectWorkDataByIosign(devCompany.getCompanyId(),workOrder.getId(),line.getId(),devList.getId());
//        if (workData != null) {
//            workForm.setActualNum(workData.getCumulativeNum());
//        }
        return workForm;
    }

    /**
     * 获取路径
     * @param code
     * @return
     */
    @Override
    public Map<String, Object> getPath(String code) {
        Map<String,Object> map = new HashMap<>();
        try {

            DevList devList = devListMapper.selectDevListByCode(code);
            if(devList ==null){
                map.put("status",0);
                map.put("path","");
                return map;
            }
            //查询公司信息
            DevCompany devCompany = devCompanyMapper.selectDevCompanyById2(devList.getCompanyId());
            if(devCompany ==null){
                map.put("status",0);
                map.put("path","");
                return map;
            }
            //查询服务端口
            SerPort serPort = serPortMapper.selectSerPortByCompanyId(devCompany.getCompanyId());
            if(serPort ==null){
                map.put("status",0);
                map.put("path","");
                return map;
            }
            //查询服务器
            Ser ser = serMapper.selectSerById2(serPort.getSid());
            if(ser ==null){
                map.put("status",0);
                map.put("path","");
                return map;
            }
            String url = ser.getSpath()+":"+serPort.getPort();
            map.put("status",1);
            map.put("path",url);
            return map;

        }catch (Exception e){
            map.put("status",0);
            map.put("path","");
            e.printStackTrace();
        }
        return map;
    }
}
