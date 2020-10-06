<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%-- <h3>插件站点:${site.name}</h3> --%>
	<div class="container" style="margin-top: 15px; margin-bottom: 15px;">
		<ul class="nav nav-pills">
			<li role="presentation"><a href="plugin?action=list">已安装插件</a></li>
			<li role="presentation" class="active"><a
				href="plugin?action=site">插件站点</a></li>
		</ul>
	</div>
	<div class="container" style="margin-bottom: 10px">
		<form class="form-inline" action="plugin" method="get">
			<input type="hidden" name="action" value="site">
			<div class="form-group">
				<label>URL地址</label> <input type="text" name="url"
					class="form-control" value="${siteUrl}" style="width: 300px;">
			</div>
			<button type="submit" class="btn btn-danger">打开站点</button>
		</form>
	</div>
	<div class="container">
		<table class="table">
			<tr>
				<th>插件名称</th>
				<th>版本</th>
				<th>安装</th>
			</tr>
			<c:forEach items="${site.plugins}" var="plugin">
				<tr>
					<td>${plugin.name}</td>
					<td>${plugin.version}</td>
					<td><a href="#" onclick="install(${plugin.id})">安装</a></td>

				</tr>
			</c:forEach>
		</table>
	</div>
</body>

<script type="text/javascript">
	function install(id){
	<!--TODO 需要加上 项目上下文 -->
		$.get("/plugin?action=install&id="+id+"&url=${siteUrl}",function(data,status){
			   	 	alert(data);
		});
	}
</script>
</html>