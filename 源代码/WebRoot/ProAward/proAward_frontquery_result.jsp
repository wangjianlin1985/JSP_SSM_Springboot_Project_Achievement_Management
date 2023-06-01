<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.ProAward" %>
<%@ page import="com.chengxusheji.po.Project" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<ProAward> proAwardList = (List<ProAward>)request.getAttribute("proAwardList");
    //获取所有的projectObj信息
    List<Project> projectList = (List<Project>)request.getAttribute("projectList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Project projectObj = (Project)request.getAttribute("projectObj");
    String awardName = (String)request.getAttribute("awardName"); //获奖名称查询关键字
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String awardTime = (String)request.getAttribute("awardTime"); //获奖时间查询关键字
    String createTime = (String)request.getAttribute("createTime"); //创建时间查询关键字
    String createUser = (String)request.getAttribute("createUser"); //创建人查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>项目获奖查询</title>
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
			    	<li role="presentation" class="active"><a href="#proAwardListPanel" aria-controls="proAwardListPanel" role="tab" data-toggle="tab">项目获奖列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>ProAward/proAward_frontAdd.jsp" style="display:none;">添加项目获奖</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="proAwardListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>获奖项目</td><td>获奖名称</td><td>级别</td><td>获奖用户</td><td>获奖时间</td><td>创建时间</td><td>创建人</td><td>附件信息</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<proAwardList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		ProAward proAward = proAwardList.get(i); //获取到项目获奖对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=proAward.getProjectObj().getName() %></td>
 											<td><%=proAward.getAwardName() %></td>
 											<td><%=proAward.getLevel() %></td>
 											<td><%=proAward.getUserObj().getName() %></td>
 											<td><%=proAward.getAwardTime() %></td>
 											<td><%=proAward.getCreateTime() %></td>
 											<td><%=proAward.getCreateUser() %></td>
 											<td><%=proAward.getAccessory().equals("")?"暂无文件":"<a href='" + basePath + proAward.getAccessory() + "' target='_blank'>" + proAward.getAccessory() + "</a>"%>
 											<td>
 												<a href="<%=basePath  %>ProAward/<%=proAward.getAwardId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="proAwardEdit('<%=proAward.getAwardId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="proAwardDelete('<%=proAward.getAwardId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
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
    		<h1>项目获奖查询</h1>
		</div>
		<form name="proAwardQueryForm" id="proAwardQueryForm" action="<%=basePath %>ProAward/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="projectObj_projectId">获奖项目：</label>
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
				<label for="awardName">获奖名称:</label>
				<input type="text" id="awardName" name="awardName" value="<%=awardName %>" class="form-control" placeholder="请输入获奖名称">
			</div>






            <div class="form-group">
            	<label for="userObj_user_name">获奖用户：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="awardTime">获奖时间:</label>
				<input type="text" id="awardTime" name="awardTime" class="form-control"  placeholder="请选择获奖时间" value="<%=awardTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="createTime">创建时间:</label>
				<input type="text" id="createTime" name="createTime" class="form-control"  placeholder="请选择创建时间" value="<%=createTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="createUser">创建人:</label>
				<input type="text" id="createUser" name="createUser" value="<%=createUser %>" class="form-control" placeholder="请输入创建人">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="proAwardEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;项目获奖信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="proAwardEditForm" id="proAwardEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="proAward_awardId_edit" class="col-md-3 text-right">获奖id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="proAward_awardId_edit" name="proAward.awardId" class="form-control" placeholder="请输入获奖id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="proAward_projectObj_projectId_edit" class="col-md-3 text-right">获奖项目:</label>
		  	 <div class="col-md-9">
			    <select id="proAward_projectObj_projectId_edit" name="proAward.projectObj.projectId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_awardName_edit" class="col-md-3 text-right">获奖名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="proAward_awardName_edit" name="proAward.awardName" class="form-control" placeholder="请输入获奖名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_level_edit" class="col-md-3 text-right">级别:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="proAward_level_edit" name="proAward.level" class="form-control" placeholder="请输入级别">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_userObj_user_name_edit" class="col-md-3 text-right">获奖用户:</label>
		  	 <div class="col-md-9">
			    <select id="proAward_userObj_user_name_edit" name="proAward.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_awardTime_edit" class="col-md-3 text-right">获奖时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date proAward_awardTime_edit col-md-12" data-link-field="proAward_awardTime_edit">
                    <input class="form-control" id="proAward_awardTime_edit" name="proAward.awardTime" size="16" type="text" value="" placeholder="请选择获奖时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_createTime_edit" class="col-md-3 text-right">创建时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date proAward_createTime_edit col-md-12" data-link-field="proAward_createTime_edit">
                    <input class="form-control" id="proAward_createTime_edit" name="proAward.createTime" size="16" type="text" value="" placeholder="请选择创建时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_createUser_edit" class="col-md-3 text-right">创建人:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="proAward_createUser_edit" name="proAward.createUser" class="form-control" placeholder="请输入创建人">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="proAward_accessory_edit" class="col-md-3 text-right">附件信息:</label>
		  	 <div class="col-md-9">
			    <a id="proAward_accessoryA" target="_blank"></a><br/>
			    <input type="hidden" id="proAward_accessory" name="proAward.accessory"/>
			    <input id="accessoryFile" name="accessoryFile" type="file" size="50" />
		  	 </div>
		  </div>
		</form> 
	    <style>#proAwardEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxProAwardModify();">提交</button>
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
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.proAwardQueryForm.currentPage.value = currentPage;
    document.proAwardQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.proAwardQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.proAwardQueryForm.currentPage.value = pageValue;
    documentproAwardQueryForm.submit();
}

/*弹出修改项目获奖界面并初始化数据*/
function proAwardEdit(awardId) {
	$.ajax({
		url :  basePath + "ProAward/" + awardId + "/update",
		type : "get",
		dataType: "json",
		success : function (proAward, response, status) {
			if (proAward) {
				$("#proAward_awardId_edit").val(proAward.awardId);
				$.ajax({
					url: basePath + "Project/listAll",
					type: "get",
					success: function(projects,response,status) { 
						$("#proAward_projectObj_projectId_edit").empty();
						var html="";
		        		$(projects).each(function(i,project){
		        			html += "<option value='" + project.projectId + "'>" + project.name + "</option>";
		        		});
		        		$("#proAward_projectObj_projectId_edit").html(html);
		        		$("#proAward_projectObj_projectId_edit").val(proAward.projectObjPri);
					}
				});
				$("#proAward_awardName_edit").val(proAward.awardName);
				$("#proAward_level_edit").val(proAward.level);
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#proAward_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#proAward_userObj_user_name_edit").html(html);
		        		$("#proAward_userObj_user_name_edit").val(proAward.userObjPri);
					}
				});
				$("#proAward_awardTime_edit").val(proAward.awardTime);
				$("#proAward_createTime_edit").val(proAward.createTime);
				$("#proAward_createUser_edit").val(proAward.createUser);
				$("#proAward_accessory").val(proAward.accessory);
				$("#proAward_accessoryA").text(proAward.accessory);
				$("#proAward_accessoryA").attr("href", basePath +　proAward.accessory);
				$('#proAwardEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除项目获奖信息*/
function proAwardDelete(awardId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "ProAward/deletes",
			data : {
				awardIds : awardId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#proAwardQueryForm").submit();
					//location.href= basePath + "ProAward/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交项目获奖信息表单给服务器端修改*/
function ajaxProAwardModify() {
	$.ajax({
		url :  basePath + "ProAward/" + $("#proAward_awardId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#proAwardEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#proAwardQueryForm").submit();
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

    /*获奖时间组件*/
    $('.proAward_awardTime_edit').datetimepicker({
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
    /*创建时间组件*/
    $('.proAward_createTime_edit').datetimepicker({
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

