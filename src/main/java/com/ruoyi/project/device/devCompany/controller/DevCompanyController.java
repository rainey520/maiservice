package com.ruoyi.project.device.devCompany.controller;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 公司 信息操作处理
 *
 * @author ruoyi
 * @date 2019-01-31
 */
@Controller
@RequestMapping("/device/devCompany")
public class DevCompanyController extends BaseController {
    /**
     * 公司控制层日志
     */
    private static final Logger log = LoggerFactory.getLogger(DevCompanyController.class);

    private String prefix = "device/devCompany";

    @Value("${img.url}")
    private String imgUrl;

    @Autowired
    private IDevCompanyService devCompanyService;

    @Autowired
    private IUserService userService;

    @RequiresPermissions("device:devCompany:view")
    @GetMapping()
    public String devCompany() {
        return prefix + "/devCompany";
    }

    /**
     * 查询公司列表
     */
    @RequiresPermissions("device:devCompany:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DevCompany devCompany) {
        startPage();
        List<DevCompany> list = devCompanyService.selectDevCompanyList(devCompany);
        return getDataTable(list);
    }


    /**
     * 导出公司列表
     */
    @RequiresPermissions("device:devCompany:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DevCompany devCompany) {
        List<DevCompany> list = devCompanyService.selectDevCompanyList(devCompany);
        ExcelUtil<DevCompany> util = new ExcelUtil<DevCompany>(DevCompany.class);
        return util.exportExcel(list, "devCompany");
    }

    /**
     * 新增公司
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存公司
     */
    @RequiresPermissions("device:devCompany:add")
    @Log(title = "公司", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DevCompany devCompany) {
        devCompany.setCreateTime(new Date());
        devCompany.setComType(1);
        return toAjax(devCompanyService.insertDevCompany(devCompany));
    }

    /**
     * 修改公司
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        DevCompany devCompany = devCompanyService.selectDevCompanyById(id);
        mmap.put("devCompany", devCompany);
        return prefix + "/edit";
    }

    /**
     * 修改保存公司
     */
    @RequiresPermissions("device:devCompany:edit")
    @Log(title = "公司", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DevCompany devCompany) {
        return toAjax(devCompanyService.updateDevCompany(devCompany));
    }

    /**
     * 删除公司
     */
    @RequiresPermissions("device:devCompany:remove")
    @Log(title = "公司", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(devCompanyService.deleteDevCompanyByIds(ids));
    }

    /**
     * 修改公司LOGO
     *
     * @param mmap
     * @return
     */
    @GetMapping("/comLogo")
    public String comLogo(ModelMap mmap, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        DevCompany devCompany = devCompanyService.selectDevCompanyById(user.getCompanyId());
        mmap.put("company", devCompany);
        return prefix + "/comLogo";
    }

    /**
     * 保存公司lOGO
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateComLogo")
    @ResponseBody
    public AjaxResult updateComLogo(@RequestParam("avatarfile") MultipartFile file,HttpServletRequest request) {
        User currentUser = JwtUtil.getTokenUser(request);
        DevCompany company = devCompanyService.selectDevCompanyById(currentUser.getCompanyId());
        try {
            if (!file.isEmpty()) {
                String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
                company.setComLogo(imgUrl+avatar); // 设置LOGO
                if (devCompanyService.updateDevCompany(company) > 0) {
                    setSysUser(userService.selectUserById(currentUser.getUserId()));
                    return success();
                }
            }
            return error();
        } catch (Exception e) {
            log.error("修改公司LOGO失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 跳转修改公司轮播图片
     *
     * @param mmap
     * @return
     */
    @GetMapping("/comPicture")
    public String comPicture(ModelMap mmap,HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        DevCompany devCompany = devCompanyService.selectDevCompanyById(user.getCompanyId());
        mmap.put("company", devCompany);
        return prefix + "/comPicture";
    }

    /**
     * 公司轮播图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadComPicture")
    @ResponseBody
    public AjaxResult uploadComPicture(MultipartFile file) {
        try {
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
            return success(imgUrl+avatar); // 返回路径
        } catch (Exception e) {
            return error("图片上传失败");
        }
    }

    /**
     * 公司轮播图片保存（保存地址N）
     * @param comPicture
     * @return
     */
    @PostMapping("/updateComPicture")
    @ResponseBody
    public AjaxResult updateComPicture(String comPicture,HttpServletRequest request){
        DevCompany company = devCompanyService.selectDevCompanyById(JwtUtil.getTokenUser(request).getCompanyId());
        company.setComPicture(comPicture);
        return toAjax(devCompanyService.updateDevCompany(company));
    }

    /**
     * 校验公司名称是否唯一
     * @param company
     * @return
     */
    @PostMapping("/checkComNameUnique")
    @ResponseBody
    public String checkComNameUnique(DevCompany company){
        return devCompanyService.checkComNameUnique(company.getComName());
    }
}
