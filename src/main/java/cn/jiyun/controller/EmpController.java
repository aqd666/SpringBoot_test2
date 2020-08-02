package cn.jiyun.controller;

import cn.jiyun.pojo.Dept;
import cn.jiyun.pojo.Emp;
import cn.jiyun.service.DeptService;
import cn.jiyun.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("emp")
public class EmpController {

    @Autowired
    private DeptService deptService;
    @Autowired
    private EmpService empService;

    @Value("${file.upload.path}")
    private String filePath;

    @GetMapping("toAddEmp")
    public String toAddEmp(Model model){
        List<Dept> depts = deptService.findAll();
        model.addAttribute("depts",depts);
        return "addEmp";
    }

    @PostMapping("addEmp")
    public String addEmp(@ModelAttribute(value = "emp")Emp emp,
                         @RequestParam(value = "file")MultipartFile file)throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        File photoFile = new File(filePath,fileName);
        //判断当前系统是否存在上传路径，如果不存在就新建
        if(!photoFile.getParentFile().exists()){
            photoFile.getParentFile().mkdir();
        }
        file.transferTo(new File(filePath+File.separator+fileName));
        emp.setPhoto("/photo/"+fileName);
        empService.addEmp(emp);
        //保存完重定向到findAll
        return  "redirect:/emp/findAll";
    }
    @GetMapping("findAll")
    public  String findAll(Model model){
        List<Emp> emps = empService.findAll();
        model.addAttribute("emps",emps);
        return  "empList";
    }

    @GetMapping("delEmpById")
    public String deleteEmp(@RequestParam(value="eid")Integer eid){
        empService.delEmpById(eid);
        return "redirect:/emp/findAll";
    }

    @GetMapping("findById")
    public String findById(@RequestParam(value = "eid") Integer eid,Model model){
        List<Dept> depts = deptService.findAll();
        model.addAttribute("depts",depts);
        Emp emp = empService.findById(eid);
        model.addAttribute("emp",emp);
        return "update";
    }

    @PostMapping("updateEmp")
    public String updateEmp(@ModelAttribute(value = "emp")Emp emp,
                         @RequestParam(value = "file")MultipartFile file)throws IOException {
        if (file.getSize()>0) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            File photoFile = new File(filePath,fileName);
            //判断当前系统是否存在上传路径，如果不存在就新建
            if(!photoFile.getParentFile().exists()){
                photoFile.getParentFile().mkdir();
            }
            file.transferTo(new File(filePath+File.separator+fileName));
            emp.setPhoto("/photo/"+fileName);
        }
        empService.updateEmp(emp);
        //保存完重定向到findAll
        return  "redirect:/emp/findAll";
    }
//    @GetMapping("test")
//    public String test(){
//        return "我是大傻子";
//    }
}
