package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.DataAuthService;
import com.chengxusheji.service.ProAuthService;
import com.chengxusheji.service.ProjectDataService;
import com.chengxusheji.po.ProAuth;
import com.chengxusheji.po.ProjectData;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.service.ProjectService;
import com.chengxusheji.po.Project;

//ProjectData管理控制层
@Controller
@RequestMapping("/ProjectData")
public class ProjectDataController extends BaseController {

    /*业务层对象*/
    @Resource ProjectDataService projectDataService;
    @Resource ProAuthService proAuthService;
    @Resource DataAuthService dataAuthService;

    @Resource ProjectService projectService;
	@InitBinder("projectObj")
	public void initBinderprojectObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("projectObj.");
	}
	@InitBinder("projectData")
	public void initBinderProjectData(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("projectData.");
	}
	/*跳转到添加ProjectData视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ProjectData());
		/*查询所有的Project信息*/
		List<Project> projectList = projectService.queryAllProject();
		request.setAttribute("projectList", projectList);
		return "ProjectData_add";
	}

	/*客户端ajax方式提交添加项目资料信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ProjectData projectData, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		projectData.setDataurl(this.handleFileUpload(request, "dataurlFile"));
        projectDataService.addProjectData(projectData);
        message = "项目资料添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询项目资料信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("projectObj") Project projectObj,String dataname,String createuser,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (dataname == null) dataname = "";
		if (createuser == null) createuser = "";
		if(rows != 0)projectDataService.setRows(rows);
		List<ProjectData> projectDataList = projectDataService.queryProjectData(projectObj, dataname, createuser, page);
	    /*计算总的页数和总的记录数*/
	    projectDataService.queryTotalPageAndRecordNumber(projectObj, dataname, createuser);
	    /*获取到总的页码数目*/
	    int totalPage = projectDataService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = projectDataService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ProjectData projectData:projectDataList) {
			JSONObject jsonProjectData = projectData.getJsonObject();
			jsonArray.put(jsonProjectData);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询项目资料信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ProjectData> projectDataList = projectDataService.queryAllProjectData();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ProjectData projectData:projectDataList) {
			JSONObject jsonProjectData = new JSONObject();
			jsonProjectData.accumulate("dataId", projectData.getDataId());
			jsonProjectData.accumulate("dataname", projectData.getDataname());
			jsonArray.put(jsonProjectData);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询项目资料信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("projectObj") Project projectObj,String dataname,String createuser,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (dataname == null) dataname = "";
		if (createuser == null) createuser = "";
		List<ProjectData> projectDataList = projectDataService.queryProjectData(projectObj, dataname, createuser, currentPage);
	    /*计算总的页数和总的记录数*/
	    projectDataService.queryTotalPageAndRecordNumber(projectObj, dataname, createuser);
	    /*获取到总的页码数目*/
	    int totalPage = projectDataService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = projectDataService.getRecordNumber();
	    request.setAttribute("projectDataList",  projectDataList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("projectObj", projectObj);
	    request.setAttribute("dataname", dataname);
	    request.setAttribute("createuser", createuser);
	    List<Project> projectList = projectService.queryAllProject();
	    request.setAttribute("projectList", projectList);
		return "ProjectData/projectData_frontquery_result"; 
	}

     /*前台查询ProjectData信息*/
	@RequestMapping(value="/{dataId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer dataId,Model model,HttpServletRequest request,HttpSession session) throws Exception {
		/*根据主键dataId获取ProjectData对象*/
        ProjectData projectData = projectDataService.getProjectData(dataId);
        
        String user_name = (String)session.getAttribute("user_name");
        if(user_name == null) {
        	projectData = null;
        	return "ProjectData/error";
        }
        Project project = projectData.getProjectObj();
        ArrayList<ProAuth> proAuthList = proAuthService.queryProAuth(project, null, "");
        boolean authFlag = false;
        for(ProAuth proAuth: proAuthList) {
        	if(proAuth.getUserObj().getUser_name().equals(user_name)) {
        		authFlag = true;
        		break;
        	}
        }
        
        UserInfo userObj = new UserInfo();
        userObj.setUser_name(user_name);
        //如果用户没参与项目但是申请了资料的权限也可以查看
        if(dataAuthService.queryDataAuth(projectData, userObj, "", "审核通过").size() > 0) {
        	authFlag = true;
        }
         
        
        if(authFlag == false) projectData = null;
         
        List<Project> projectList = projectService.queryAllProject();
        request.setAttribute("projectList", projectList);
        request.setAttribute("projectData",  projectData);
        return "ProjectData/projectData_frontshow";
	}

	/*ajax方式显示项目资料修改jsp视图页*/
	@RequestMapping(value="/{dataId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer dataId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键dataId获取ProjectData对象*/
        ProjectData projectData = projectDataService.getProjectData(dataId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonProjectData = projectData.getJsonObject();
		out.println(jsonProjectData.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新项目资料信息*/
	@RequestMapping(value = "/{dataId}/update", method = RequestMethod.POST)
	public void update(@Validated ProjectData projectData, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String dataurlFileName = this.handleFileUpload(request, "dataurlFile");
		if(!dataurlFileName.equals(""))projectData.setDataurl(dataurlFileName);
		try {
			projectDataService.updateProjectData(projectData);
			message = "项目资料更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "项目资料更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除项目资料信息*/
	@RequestMapping(value="/{dataId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer dataId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  projectDataService.deleteProjectData(dataId);
	            request.setAttribute("message", "项目资料删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "项目资料删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条项目资料记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String dataIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = projectDataService.deleteProjectDatas(dataIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出项目资料信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("projectObj") Project projectObj,String dataname,String createuser, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(dataname == null) dataname = "";
        if(createuser == null) createuser = "";
        List<ProjectData> projectDataList = projectDataService.queryProjectData(projectObj,dataname,createuser);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "ProjectData信息记录"; 
        String[] headers = { "项目","资料名称","排序号","创建人"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<projectDataList.size();i++) {
        	ProjectData projectData = projectDataList.get(i); 
        	dataset.add(new String[]{projectData.getProjectObj().getName(),projectData.getDataname(),projectData.getSortNumber() + "",projectData.getCreateuser()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"ProjectData.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
