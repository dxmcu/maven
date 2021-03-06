<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html class="no-js" lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title>任务控制台</title>
<link rel="stylesheet" href="/s/c/agvmgr/agv.mgr.css">
</head>
<body>
	<div id="msg">
		<div id="tipsOper">
			<span class="yellowFont">欢迎，${userSession.userName}！</span> <span
				class="redFont" id="warning"></span> <a href="logout.shtml">切换账号</a>
		</div>
		<div id="tips"></div>
	</div>
	<div id="op">
		<jsp:include page="../alloc/agvmgrAlloc.jsp"></jsp:include>
		<div>
			<table class="task">
				<tr class="ctrlTr">
					<td>
						<div>
							<button id="PAUSE_USER">暂停</button>
						</div>
					</td>
					<td>
						<div>
							<button id="CONTINUE">继续</button>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<a href="/manager.shtml" id="mgr">管理</a>
	<div id="fade" class="black_overlay">
		<div id="imgdiv">
			<img alt="" src="/s/i/loading2.gif">
		</div>
	</div>
</body>
<script src="/s/j/jquery-1.12.2.min.js"></script>
<script
	src="/s/j/agvmgr/haitianDriver/agvmgrHTDV.js?fv=${staticFileVersion}"></script>
</html>