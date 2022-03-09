package pl.skrys.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadControl {

    //@Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public RedirectView uploadFile(@Valid @RequestParam("file") MultipartFile file, @RequestParam("fId") long fId, HttpServletRequest request) {
        System.out.println("uploadFile");
        System.out.println(fId);
        try {

            String uploadDir = "/resources/uploads";
            String realPath = request.getServletContext().getRealPath(uploadDir);
            System.out.println(realPath);

            System.out.println(file.getOriginalFilename());

            System.out.println(file.getContentType());
            if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){
                File transferFile;
                String oppositeExtension;
                if(file.getContentType().equals("image/jpeg")){
                    transferFile = new File(realPath + "/" + fId+"."+"jpg");
                    new File(realPath + "/" + fId+"."+"png").delete();
                }else{
                    transferFile = new File(realPath + "/" + fId+"."+FilenameUtils.getExtension(file.getOriginalFilename()));
                    new File(realPath + "/" + fId+"."+"jpg").delete();
                }

                file.transferTo(transferFile);
                System.out.println(file.getSize());
                return new RedirectView("/inRobotManag.html?fId="+fId);
            }else{
                return new RedirectView("/inRobotManag.html?fId="+fId);
            }



        } catch (Exception e) {

            e.printStackTrace();

            return new RedirectView("/inRobotManag.html?fId="+fId);
        }
    }


    @RequestMapping(value = "/upfiles", method = RequestMethod.GET)
    @ResponseBody
    public String uploadFile(HttpServletRequest request) {
        System.out.println("upfiles");
        return "upfiles";
    }

}