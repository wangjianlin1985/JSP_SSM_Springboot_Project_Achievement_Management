<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Project" %>
<%@ page import="com.chengxusheji.po.ProjectType" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Project> projectList = (List<Project>)request.getAttribute("projectList");
    //获取所有的projectTyoeObj信息
    List<ProjectType> projectTypeList = (List<ProjectType>)request.getAttribute("projectTypeList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    ProjectType projectTyoeObj = (ProjectType)request.getAttribute("projectTyoeObj");
    String serialnumber = (String)request.getAttribute("serialnumber"); //项目编号查询关键字
    String name = (String)request.getAttribute("name"); //项目名称查询关键字
    String beginTime = (String)request.getAttribute("beginTime"); //开始时间查询关键字
    String endTime = (String)request.getAttribute("endTime"); //结束时间查询关键字
    String createuser = (String)request.getAttribute("createuser"); //项目创建人查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>项目查询</title>
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
			    	<li role="presentation" class="active"><a href="#projectListPanel" aria-controls="projectListPanel" role="tab" data-toggle="tab">项目列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Project/project_frontAdd.jsp" style="display:none;">添加项目</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="projectListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>项目类别</td><td>项目编号</td><td>项目名称</td><td>开始时间</td><td>结束时间</td><td>合同金额</td><td>项目创建人</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<projectList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Project project = projectList.get(i); //获取到项目对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=project.getProjectTyoeObj().getTypeName() %></td>
 											<td><%=project.getSerialnumber() %></td>
 											<td><%=project.getName() %></td>
 											<td><%=project.getBeginTime() %></td>
 											<td><%=project.getEndTime() %></td>
 											<td><%=project.getMoney() %></td>
 											<td><%=project.getCreateuser() %></td>
 											<td>
 												<a href="<%=basePath  %>Project/<%=project.getProjectId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="projectEdit('<%=project.getProjectId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="projectDelete('<%=project.getProjectId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
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
    		<h1>项目查询</h1>
		</div>
		<form name="projectQueryForm" id="projectQueryForm" action="<%=basePath %>Project/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="projectTyoeObj_typeId">项目类别：</label>
                <select id="projectTyoeObj_typeId" name="projectTyoeObj.typeId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(ProjectType projectTypeTemp:projectTypeList) {
	 					String selected = "";
 					if(projectTyoeObj!=null && projectTyoeObj.getTypeId()!=null && projectTyoeObj.getTypeId().intValue()==projectTypeTemp.getTypeId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=projectTypeTemp.getTypeId() %>" <%=selected %>><%=projectTypeTemp.getTypeName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="serialnumber">项目编号:</label>
				<input type="text" id="serialnumber" name="serialnumber" value="<%=serialnumber %>" class="form-control" placeholder="请输入项目编号">
			</div>






			<div class="form-group">
				<label for="name">项目名称:</label>
				<input type="text" id="name" name="name" value="<%=name %>" class="form-control" placeholder="请输入项目名称">
			</div>






			<div class="form-group">
				<label for="beginTime">开始时间:</label>
				<input type="text" id="beginTime" name="beginTime" class="form-control"  placeholder="请选择开始时间" value="<%=beginTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="endTime">结束时间:</label>
				<input type="text" id="endTime" name="endTime" class="form-control"  placeholder="请选择结束时间" value="<%=endTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="createuser">项目创建人:</label>
				<input type="text" id="createuser" name="createuser" value="<%=createuser %>" class="form-control" placeholder="请输入项目创建人">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="projectEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;项目信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="projectEditForm" id="projectEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="project_projectId_edit" class="col-md-3 text-right">项目id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="project_projectId_edit" name="project.projectId" class="form-control" placeholder="请输入项目id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="project_projectTyoeObj_typeId_edit" class="col-md-3 text-right">项目类别:</label>
		  	 <div class="col-md-9">
			    <select id="project_projectTyoeObj_typeId_edit" name="project.projectTyoeObj.typeId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_serialnumber_edit" class="col-md-3 text-right">项目编号:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="project_serialnumber_edit" name="project.serialnumber" class="form-control" placeholder="请输入项目编号">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_name_edit" class="col-md-3 text-right">项目名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="project_name_edit" name="project.name" class="form-control" placeholder="请输入项目名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_content_edit" class="col-md-3 text-right">项目内容:</label>
		  	 <div class="col-md-9">
			 	<textarea name="project.content" id="project_content_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_beginTime_edit" class="col-md-3 text-right">开始时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date project_beginTime_edit col-md-12" data-link-field="project_beginTime_edit">
                    <input class="form-control" id="project_beginTime_edit" name="project.beginTime" size="16" type="text" value="" placeholder="请选择开始时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_endTime_edit" class="col-md-3 text-right">结束时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date project_endTime_edit col-md-12" data-link-field="project_endTime_edit">
                    <input class="form-control" id="project_endTime_edit" name="project.endTime" size="16" type="text" value="" placeholder="请选择结束时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_money_edit" class="col-md-3 text-right">合同金额:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="project_money_edit" name="project.money" class="form-control" placeholder="请输入合同金额">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="project_createuser_edit" class="col-md-3 text-right">项目创建人:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="project_createuser_edit" name="project.createuser" class="form-control" placeholder="请输入项目创建人">
			 </div>
		  </div>
		</form> 
	    <style>#projectEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxProjectModify();">提交</button>
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
var project_content_edit = UE.getEditor('project_content_edit'); //项目内容编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.projectQueryForm.currentPage.value = currentPage;
    document.projectQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.projectQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.projectQueryForm.currentPage.value = pageValue;
    documentprojectQueryForm.submit();
}

/*弹出修改项目界面并初始化数据*/
function projectEdit(projectId) {
	$.ajax({
		url :  basePath + "Project/" + projectId + "/update",
		type : "get",
		dataType: "json",
		success : function (project, response, status) {
			if (project) {
				$("#project_projectId_edit").val(project.projectId);
				$.ajax({
					url: basePath + "ProjectType/listAll",
					type: "get",
					success: function(projectTypes,response,status) { 
						$("#project_projectTyoeObj_typeId_edit").empty();
						var html="";
		        		$(projectTypes).each(function(i,projectType){
		        			html += "<option value='" + projectType.typeId + "'>" + projectType.typeName + "</option>";
		        		});
		        		$("#project_projectTyoeObj_typeId_edit").html(html);
		        		$("#project_projectTyoeObj_typeId_edit").val(project.projectTyoeObjPri);
					}
				});
				$("#project_serialnumber_edit").val(project.serialnumber);
				$("#project_name_edit").val(project.name);
				project_content_edit.setContent(project.content, false);
				$("#project_beginTime_edit").val(project.beginTime);
				$("#project_endTime_edit").val(project.endTime);
				$("#project_money_edit").val(project.money);
				$("#project_createuser_edit").val(project.createuser);
				$('#projectEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除项目信息*/
function projectDelete(projectId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Project/deletes",
			data : {
				projectIds : projectId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#projectQueryForm").submit();
					//location.href= basePath + "Project/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交项目信息表单给服务器端修改*/
function ajaxProjectModify() {
	$.ajax({
		url :  basePath + "Project/" + $("#project_projectId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#projectEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#projectQueryForm").submit();
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

    /*开始时间组件*/
    $('.project_beginTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
    /*结束时间组件*/
    $('.project_endTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

