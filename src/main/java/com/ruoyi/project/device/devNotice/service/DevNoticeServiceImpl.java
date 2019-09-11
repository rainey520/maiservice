package com.ruoyi.project.device.devNotice.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.constant.NoticeConstants;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.device.devNotice.mapper.DevNoticeMapper;
import com.ruoyi.project.device.devNotice.domain.DevNotice;
import com.ruoyi.project.device.devNotice.service.IDevNoticeService;
import com.ruoyi.common.support.Convert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 公司消息通知 服务层实现
 *
 * @author zqm
 * @date 2019-04-18
 */
@Service("notice")
public class DevNoticeServiceImpl implements IDevNoticeService {
    @Autowired
    private DevNoticeMapper devNoticeMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询公司消息通知信息
     *
     * @param id 公司消息通知ID
     * @return 公司消息通知信息
     */
    @Override
    public DevNotice selectDevNoticeById(Integer id) {
        return devNoticeMapper.selectDevNoticeById(id);
    }

    /**
     * 查询公司消息通知列表
     *
     * @param devNotice 公司消息通知信息
     * @return 公司消息通知集合
     */
    @Override
    public List<DevNotice> selectDevNoticeList(DevNotice devNotice, HttpServletRequest request) {
        User u = JwtUtil.getTokenUser(request);
        if (User.isSys(u) == false ) { // 非系统用户只能查看自己公司的消息
            devNotice.setCompanyId(u.getCompanyId());
        }
        List<DevNotice> devNotices = devNoticeMapper.selectDevNoticeList(devNotice);
        for (DevNotice notice : devNotices) {
            notice.setCreateUser(userMapper.selectUserById(notice.getCreateId().longValue()));   // 创建者对象
        }
        return devNotices;
    }

    /**
     * 新增公司消息通知
     *
     * @param devNotice 公司消息通知信息
     * @return 结果
     */
    @Override
    public int insertDevNotice(DevNotice devNotice,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        devNotice.setCreateId(user.getUserId().intValue()); // 设置创建者
        devNotice.setCompanyId(user.getCompanyId()); // 消息所属公司
        devNotice.setCreateTime(new Date()); // 消息创建时间
        devNotice.setNoticeStatus(NoticeConstants.NOTICE_NO_PUBLISH); // 设置未发布
        return devNoticeMapper.insertDevNotice(devNotice);
    }

    /**
     * 修改公司消息通知
     *
     * @param devNotice 公司消息通知信息
     * @return 结果
     */
    @Override
    public int updateDevNotice(DevNotice devNotice) {
        return devNoticeMapper.updateDevNotice(devNotice);
    }

    /**
     * 删除公司消息通知对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDevNoticeByIds(String ids) {
        return devNoticeMapper.deleteDevNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * 发布消息
     * @param id
     * @return
     */
    @Override
    public int publish(Integer id) {
        DevNotice notice = devNoticeMapper.selectDevNoticeById(id);
        notice.setNoticeStatus(NoticeConstants.NOTICE_PUBLISH); // 设置消息状态为已经发布
        return devNoticeMapper.updateDevNotice(notice);
    }

    /**
     * 下线消息
     * @param id
     * @return
     */
    @Override
    public int publishEnd(Integer id) {
        DevNotice notice = devNoticeMapper.selectDevNoticeById(id);
        notice.setNoticeStatus(NoticeConstants.NOTICE_PUBLISH_END); // 设置消息状态为已经下线
        return devNoticeMapper.updateDevNotice(notice);
    }

    /**
     * 查询公司所有的消息
     * @return
     */
    public List<DevNotice> selectAllNotice(Cookie[] cookies){
        User user = JwtUtil.getTokenCookie(cookies);
        List<DevNotice> devNotices = devNoticeMapper.selectAllNotice(user.getCompanyId());
        for (DevNotice notice : devNotices) {
            notice.setCreateUser(userMapper.selectUserById(notice.getCreateId().longValue())); // 创建者对象
        }
        return devNotices;
    }
}
