package com.ruoyi.project.device.devDeviceCounts.mapper;

import com.ruoyi.framework.aspectj.lang.annotation.DataSource;
import com.ruoyi.framework.aspectj.lang.enums.DataSourceType;
import com.ruoyi.project.device.devDeviceCounts.domain.DataLogTask;
import com.ruoyi.project.device.devDeviceCounts.domain.DevDataLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 数据上报日志 数据层
 *
 * @author zqm
 * @date 2019-04-12
 */
public interface DevDataLogMapper {
    /**
     * 查询数据上报日志信息
     *
     * @param id 数据上报日志ID
     * @return 数据上报日志信息
     */
    public DevDataLog selectDevDataLogById(Integer id);

    /**
     * 查询数据上报日志列表
     *
     * @param devDataLog 数据上报日志信息
     * @return 数据上报日志集合
     */
    public List<DevDataLog> selectDevDataLogList(DevDataLog devDataLog);

    /**
     * 新增数据上报日志
     *
     * @param devDataLog 数据上报日志信息
     * @return 结果
     */
    @DataSource(value = DataSourceType.SLAVE)
    public int insertDevDataLog(DevDataLog devDataLog);

    /**
     * 查询每天正在生产或者已经完成的工单每小时的IO口数据
     *
     * @param devId          硬件id
     * @param ioId           IO口id
     * @param workId         工单id
     * @param sysDateTimeOld 前一个小时时间
     * @param sysDateTime    当前时间
     * @return 结果包装类
     */
    @DataSource(value = DataSourceType.SLAVE)
    DataLogTask selectDataLogBeInOrFinish(@Param("devId") Integer devId, @Param("ioId") Integer ioId, @Param("workId") Integer workId,
                                          @Param("sysDateTimeOld") Date sysDateTimeOld, @Param("sysDateTime") Date sysDateTime);


    /**
     * 通过工单id查询数据上报列表
     *
     * @param workId    工单id主键
     * @param companyId 公司Id主键
     * @return 数据上报日志信息
     */
    @DataSource(value = DataSourceType.SLAVE)
    public List<DevDataLog> selectDevDataLogByWorkId(@Param("workId") Integer workId, @Param("companyId") Integer companyId);

    /**
     * 查询对应产线工单硬件IO口上传回传的数据
     *
     * @param line_id 查询
     * @param work_id 工单
     * @param dev_id  硬件编号
     * @param io_id   IO口编号
     * @return
     */
    @DataSource(DataSourceType.SLAVE)
    DevDataLog selectLineWorkDevIo(@Param("line_id") int line_id, @Param("work_id") int work_id, @Param("dev_id") int dev_id,
                                   @Param("io_id") int io_id);

}