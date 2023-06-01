<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.ProjectData" %>
<%@ page import="com.chengxusheji.po.Project" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<ProjectData> projectDataList = (List<ProjectData>)request.getAttribute("projectDataList");
    //获取所有的projectObj信息
    List<Project> projectList = (List<Project>)request.getAttribute("projectList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Project projectObj = (Project)request.getAttribute("projectObj");
    String dataname = (String)request.getAttribute("dataname"); //资料名称查询关键字
    String createuser = (String)request.getAttribute("createuser"); //创建人查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>项目资料查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#projectDataListPanel" aria-controls="projectDataListPanel" role="tab" data-toggle="tab">项目资料列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>ProjectData/projectData_frontAdd.jsp" style="display:none;">添加项目资料</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="projectDataListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>项目</td><td>资料名称</td><td>排序号</td><td>创建人</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<projectDataList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		ProjectData projectData = projectDataList.get(i); //获取到项目资料对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=projectData.getProjectObj().getName() %></td>
 											<td><%=projectData.getDataname() %></td>
 											<td><%=projectData.getSortNumber() %></td>
 											<td><%=projectData.getCreateuser() %></td>
 											<td>
 												<a href="<%=basePath  %>ProjectData/<%=projectData.getDataId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="projectDataEdit('<%=projectData.getDataId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="projectDataDelete('<%=projectData.getDataId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

				    		<div class="row">
					            <div class="col-md-12">
						            <nav class="pull-left">
						                <ul class="pagination">
						                    <li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
						                     <%
						                    	int startPage = currentPage - 5;
						                    	int endPage = currentPage + 5;
						                    	if(startPage < 1) startPage=1;
						                    	if(endPage > totalPage) endPage = totalPage;
						                    	for(int i=startPage;i<=endPage;i++) {
						                    %>
						                    <li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
						                    <%  } %> 
						                    <li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						                </ul>
						            </nav>
						            <div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
					            </div>
				            </div> 
				    </div>
				</div>
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>项目资料查询</h1>
		</div>
		<form name="projectDataQueryForm" id="projectDataQueryForm" action="<%=basePath %>ProjectData/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="projectObj_projectId">项目：</label>
                <select id="projectObj_projectId" name="projectObj.projectId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Project projectTemp:projectList) {
	 					String selected = "";
 					if(projectObj!=null && projectObj.getProjectId()!=null && projectObj.getProjectId().intValue()==projectTemp.getProjectId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=projectTemp.getProjectId() %>" <%=selected %>><%=projectTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="dataname">资料名称:</label>
				<input type="text" id="dataname" name="dataname" value="<%=dataname %>" class="form-control" placeholder="请输入资料名称">
			</div>






			<div class="form-group">
				<label for="createuser">创建人:</label>
				<input type="text" id="createuser" name="createuser" value="<%=createuser %>" class="form-control" placeholder="请输入创建人">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="projectDataEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;项目资料信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="projectDataEditForm" id="projectDataEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="projectData_dataId_edit" class="col-md-3 text-right">资料id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="projectData_dataId_edit" name="projectData.dataId" class="form-control" placeholder="请输入资料id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="projectData_projectObj_projectId_edit" class="col-md-3 text-right">项目:</label>
		  	 <div class="col-md-9">
			    <select id="projectData_projectObj_projectId_edit" name="projectData.projectObj.projectId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="projectData_dataname_edit" class="col-md-3 text-right">资料名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="projectData_dataname_edit" name="projectData.dataname" class="form-control" placeholder="请输入资料名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="projectData_datacontent_edit" class="col-md-3 text-right">资料介绍:</label>
		  	 <div class="col-md-9">
			 	<textarea name="projectData.datacontent" id="projectData_datacontent_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="projectData_dataurl_edit" class="col-md-3 text-right">文件路径:</label>
		  	 <div class="col-md-9">
			    <a id="projectData_dataurlA" target="_blank"></a><br/>
			    <input type="hidden" id="projectData_dataurl" name="projectData.dataurl"/>
			    <input id="dataurlFile" name="dataurlFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="projectData_sortNumber_edit" class="col-md-3 text-right">排序号:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="projectData_sortNumber_edit" name="projectData.sortNumber" class="form-control" placeholder="请输入排序号">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="projectData_remark_edit" class="col-md-3 text-right">备注:</label>
		  	 <div class="col-md-9">
			    <textarea id="projectData_remark_edit" name="projectData.remark" rows="8" class="form-control" placeholder="请输入备注"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="projectData_createuser_edit" class="col-md-3 text-right">创建人:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="projectData_createuser_edit" name="projectData.createuser" class="form-control" placeholder="请输入创建人">
			 </div>
		  </div>
		</form> 
	    <style>#projectDataEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxProjectDataModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script>
//实例化编辑器
var projectData_datacontent_edit = UE.getEditor('projectData_datacontent_edit'); //资料介绍编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.projectDataQueryForm.currentPage.value = currentPage;
    document.projectDataQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.projectDataQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.projectDataQueryForm.currentPage.value = pageValue;
    documentprojectDataQueryForm.submit();
}

/*弹出修改项目资料界面并初始化数据*/
function projectDataEdit(dataId) {
	$.ajax({
		url :  basePath + "ProjectData/" + dataId + "/update",
		type : "get",
		dataType: "json",
		success : function (projectData, response, status) {
			if (projectData) {
				$("#projectData_dataId_edit").val(projectData.dataId);
				$.ajax({
					url: basePath + "Project/listAll",
					type: "get",
					success: function(projects,response,status) { 
						$("#projectData_projectObj_projectId_edit").empty();
						var html="";
		        		$(projects).each(function(i,project){
		        			html += "<option value='" + project.projectId + "'>" + project.name + "</option>";
		        		});
		        		$("#projectData_projectObj_projectId_edit").html(html);
		        		$("#projectData_projectObj_projectId_edit").val(projectData.projectObjPri);
					}
				});
				$("#projectData_dataname_edit").val(projectData.dataname);
				projectData_datacontent_edit.setContent(projectData.datacontent, false);
				$("#projectData_dataurl").val(projectData.dataurl);
				$("#projectData_dataurlA").text(projectData.dataurl);
				$("#projectData_dataurlA").attr("href", basePath +　projectData.dataurl);
				$("#projectData_sortNumber_edit").val(projectData.sortNumber);
				$("#projectData_remark_edit").val(projectData.remark);
				$("#projectData_createuser_edit").val(projectData.createuser);
				$('#projectDataEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除项目资料信息*/
function projectDataDelete(dataId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "ProjectData/deletes",
			data : {
				dataIds : dataId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#projectDataQueryForm").submit();
					//location.href= basePath + "ProjectData/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交项目资料信息表单给服务器端修改*/
function ajaxProjectDataModify() {
	$.ajax({
		url :  basePath + "ProjectData/" + $("#projectData_dataId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#projectDataEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#projectDataQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

})
</script>
</body>
</html>

