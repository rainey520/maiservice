package com.ruoyi.project.system.user.service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.PasswordUtil;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.aspectj.lang.annotation.DataSource;
import com.ruoyi.framework.aspectj.lang.enums.DataSourceType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.api.CompanyFeignApi;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.system.config.service.IConfigService;
import com.ruoyi.project.system.post.domain.Post;
import com.ruoyi.project.system.post.mapper.PostMapper;
import com.ruoyi.project.system.role.domain.Role;
import com.ruoyi.project.system.role.mapper.RoleMapper;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.ser.mapper.SerMapper;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.mapper.SerPortMapper;
import com.ruoyi.project.system.user.api.UserFeignApi;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.domain.UserPost;
import com.ruoyi.project.system.user.domain.UserRole;
import com.ruoyi.project.system.user.mapper.UserMapper;
import com.ruoyi.project.system.user.mapper.UserPostMapper;
import com.ruoyi.project.system.user.mapper.UserRoleMapper;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service("user")
public class UserServiceImpl implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserPostMapper userPostMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private DevCompanyMapper companyMapper;

    @Autowired
    private SerPortMapper serPortMapper;

    @Autowired
    private SerMapper serMapper;


    /**
     * 根据条件分页查询用户对象
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(tableAlias = "u")
    public List<User> selectUserList(User user) {
//        // 生成数据权限过滤条件
        List<User> userList = userMapper.selectUserListByCompanyId(user);
        List<Role> userRoles = null;
        for (User user1 : userList) {
            userRoles = roleMapper.selectRolesByUserId(user1.getUserId());
            user1.setRoles(userRoles);
        }
        return userList;
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public User selectUserByLoginName(String userName) {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 根据用户名称只查询用户信息
     * @param userName 用户名称
     * @return
     */
    @Override
    public User selectByLoginName(String userName) {
        return userMapper.selectByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     *
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    @Override
    public User selectUserByPhoneNumber(String phoneNumber) {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public User selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public User selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String ids, HttpServletRequest request) throws BusinessException {
        Long sysUserId = JwtUtil.getTokenUser(request).getUserId();
        Long[] userIds = Convert.toLongArray(ids);
        judgeUserId(sysUserId, userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int insertUser(User user) {
        User sysUser = JwtUtil.getTokenUser(ServletUtils.getRequest());
        // 用户导入设置登录标记为
        user.setDeptId(103L);
        user.randomSalt();
        if (user.getUserName() == null) {
            user.setUserName(user.getLoginName()); // 设置用户名为登录手机号
        }
        user.setPhonenumber(user.getLoginName()); // 设置用户手机号
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(sysUser.getLoginName());
        // 用户公司设置,通过获取系统登录用户设置系统id
        user.setCompanyId(sysUser.getCompanyId());

        // 设置用户标记
        user.setTag(User.COMPANY_ADMIN);

        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
//        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUser(User user, HttpServletRequest request) {
        Long userId = user.getUserId();
        User tokenUser = JwtUtil.getTokenUser(request);
        user.setUpdateBy(tokenUser.getLoginName());
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        if (null == userId) {
            user.setCompanyId(tokenUser.getCompanyId());
            userMapper.updateUserByLoginName(user);
        }
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户个人详细信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(User user) {
        user.randomSalt();
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return userMapper.updateUserByLoginName(user);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(User user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<UserRole> list = new ArrayList<UserRole>();
            for (Long roleId : user.getRoleIds()) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(User user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<UserPost> list = new ArrayList<UserPost>();
            for (Long postId : user.getPostIds()) {
                UserPost up = new UserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName) {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0) {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }



    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(User user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(Long userId) {
        List<Role> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (Role role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(Long userId) {
        List<Post> list = postMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        if (list != null) {
            for (Post post : list) {
                idsStr.append(post.getPostName()).append(",");
            }
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public String importUser(List<User> userList, Boolean isUpdateSupport, HttpServletRequest request) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String operName = JwtUtil.getTokenUser(request).getLoginName();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (User user : userList) {
            try {
                if (!user.getLoginName().matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN)) { // 不是手机格式
                    throw new BusinessException("用户手机格式不正确！");
                }
                // 验证是否存在这个用户
                User u = userMapper.selectUserByLoginName(user.getLoginName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(password);
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user, request);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            //successMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下："+failureMsg);
            throw new BusinessException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 用户状态修改
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(User user, HttpServletRequest request) {
        if (User.isAdmin(user.getUserId())) {
            throw new BusinessException("不允许修改超级管理员用户");
        }
        if (JwtUtil.getTokenUser(request).getUserId() == user.getUserId()) { // 不能自己停用自己
            throw new BusinessException("不允许停用本人");
        }
        return userMapper.updateUser(user);
    }

    @Autowired
    private IDevCompanyService devCompanyService;

    @Override
    public int register(User user) {
        //查询是否有空余的服务器
        SerPort serPort = serPortMapper.selectNotConfigSerPort();
        if (serPort == null) return 0;
        //将服务器信息查询
        Ser ser = serMapper.selectSerById(serPort.getSid());
        if (ser == null) return 0;
//        // 注册用户设置用户登录标记为1
        user.setLoginTag(UserConstants.LOGIN_TAG_REG);
//        // 部门
        user.setDeptId(103L);
//        // 设置首次注册用户为普通管理员权限
        Long[] roleIds = {2L};
        user.setRoleIds(roleIds);
//        // 设置用户标记
        user.setTag(User.COMPANY_OTHER);
//        // 用户首次注册，创建者应该为自己
        user.setCreateBy(user.getLoginName());

//        // 注册默认创立公司
        DevCompany devCompany = new DevCompany();
        devCompany.setComName(user.getComName());
        devCompany.setIndustry(user.getIndustry());
        devCompany.setCreateTime(new Date());
        devCompany.setTotalIso("iso" + ser.getId() +"_"+ serPort.getPort());
        companyMapper.insertDevCompany(devCompany);
        String url = ser.getSpath() + ":" + serPort.getPort();
        //调用对应从服务器公司注册接口进行注册公司信息
        String[] cids = {devCompany.getCompanyId().toString()};
        try {
            devCompany.setCreateTime(null);
            CompanyFeignApi feignApi = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(CompanyFeignApi.class, url);
            HashMap<String, Object> result = feignApi.initCompanyInfo(devCompany);
            if (Double.valueOf(result.get("code").toString()) != 0) {
                //删除公司信息
                companyMapper.deleteDevCompanyByIds(cids);
                return 0;
            }
        } catch (Exception e) {
            //删除公司信息
            companyMapper.deleteDevCompanyByIds(cids);
            return 0;
        }
        // 设置用户所属公司id
        user.setCompanyId(devCompany.getCompanyId());
        user.randomSalt();
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        //从服务器初始化用户
        try {
            UserFeignApi feignApi = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(UserFeignApi.class, url);
            HashMap<String, Object> result = feignApi.initUserInfo(user);
            if (Double.valueOf(result.get("code").toString()) != 0) {
                //删除用户信息
                userMapper.deleteUserById(user.getUserId());
                //删除公司信息
                companyMapper.deleteDevCompanyByIds(cids);
                return 0;
            }
        } catch (Exception e) {
            //删除用户信息
            userMapper.deleteUserById(user.getUserId());
            //删除公司信息
            companyMapper.deleteDevCompanyByIds(cids);
            return 0;
        }
//        // 新增用户岗位关联
//        insertUserPost(user);
//        // 新增用户与角色管理
        insertUserRole(user);
        //将对应端口公司配置id加上
        serPort.setCompanyId(devCompany.getCompanyId());
        serPort.setUpdateTime(new Date());
        serPortMapper.updateSerPort(serPort);
        //将对应服务器用户加一
        ser.setSuserNum(ser.getSuserNum() == null ? 1 : ser.getSuserNum() + 1);
        serMapper.updateSer(ser);
        return rows;
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return
     */
    @Override
    public int registerUser(User user) throws Exception {
        //查询对应用户是否存在
        User u = userMapper.selectByLoginName(user.getLoginName());
        if(u != null){
            throw new Exception("用户已经存在");
        }
        //查询对应邮箱是否存在
        User u2 = userMapper.selectUserByEmail(user.getEmail());
        if(u2 != null){
            throw new Exception("邮箱已存在");
        }
        user.setUserName(user.getEmail().substring(0,user.getEmail().lastIndexOf("@")));
        user.randomSalt();
        user.setPassword(PasswordUtil.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCompanyId(-1);
        user.setSign(1);
        user.setPhonenumber(user.getLoginName());
        user.setCreateBy(user.getLoginName());
        user.setCreateTime(new Date());
        user.setTag("2");
        return userMapper.insertUser(user);
    }

    /**
     * 查询对应的公司的所以的员工信息
     *
     * @return
     */
    @Override
    public List<User> selectComAllUser(Cookie[] cookies) {
        User user = JwtUtil.getTokenCookie(cookies);
        if (user == null) return Collections.emptyList();
        return userMapper.selectComAllUser(user.getCompanyId());
    }

    /**
     * 更新用户的登录标记为0
     *
     * @param user
     * @return
     */
    @Override
    public int changeLoginTag(User user) {
        user.setLoginTag(UserConstants.LOGIN_TAG_ADD); // 更新用户登录标记
        String comName = null;
        //判断邮箱是否存在
        if (!StringUtils.isEmpty(user.getEmail())) {
            User user1 = userMapper.checkEmailUnique(user.getEmail());
            if (user1 != null && user.getUserId() != user1.getUserId()) {
                throw new BusinessException("邮箱已存在，请重新输入");
            }
        }
        if (user.getDevCompany() != null && !StringUtils.isEmpty(user.getDevCompany().getComName())) {
            comName = user.getDevCompany().getComName();
            // 判断公司名称是否已经存在
            DevCompany devCompany = devCompanyService.selectDevCompanyByComName(comName);
            if (StringUtils.isNotNull(devCompany) && devCompany.getCompanyId() != user.getCompanyId()) {
                throw new BusinessException("该公司名称已经存在，请重新输入");
            }

            DevCompany company = devCompanyService.selectDevCompanyById(user.getCompanyId());
            company.setComName(comName); // 更新公司名称

            company.setComAddress(user.getDevCompany().getComAddress()); // 更新公司地址
            companyMapper.updateDevCompany(company);
        }
        return userMapper.updateUser(user);
    }

    /**
     * 获取系统登录用户
     *
     * @return
     */
    @Override
    public User getSysUser() {
        User u = ShiroUtils.getSysUser();
        if(u == null)return null;
        return userMapper.selectByLoginName(u.getLoginName());
    }

    /**
     * 查询所有服务器用户
     * @return
     */
    @Override
    public List<User> selectTagSysUser() {
        return userMapper.selectTagSysUser();
    }

    /*******     操作用户数据    *******/
    /**
     * @param user 用户数据
     * @return
     */
    @Override
    public int apiEdit(User user, HttpServletRequest request) {
        User tokenUser = JwtUtil.getTokenUser(request);
        if (null == user.getUserId()) {
            user.setCompanyId(tokenUser.getCompanyId());
            userMapper.updateUserByLoginName(user);
        }
        return userMapper.updateUser(user);
    }

    /**
     * 新增用户数据
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int apiAdd(User user) {
        userMapper.insertUser(user);
        return user.getUserId().intValue();
    }

    @Override
    public int apiRemove(String ids, HttpServletRequest request) {
        Long sysUserId = JwtUtil.getTokenUser(request).getUserId();
        String replace = ids.replace("\"", "").replace("\"", "");
        Long[] userIds = Convert.toLongArray(replace);
        judgeUserId(sysUserId, userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    private void judgeUserId(Long sysUserId, Long[] userIds) {
        for (Long userId : userIds) {
            if (userId == sysUserId) {
                throw new BusinessException("不允许删除本人");
            }
            if (User.isAdmin(userId)) {
                throw new BusinessException("不允许删除超级管理员用户");
            }
        }
    }

    /**
     * 修改用户删除状态
     * @param id
     * @return
     */
    @Override
    public int updateUserDelFlag(int id,int companyId) {
        return userMapper.updateUserDelFlag(id,companyId);
    }
}
