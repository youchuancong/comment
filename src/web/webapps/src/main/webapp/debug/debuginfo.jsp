<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@page import="java.util.Map"%>
	<html>
	<head>
	<title>
	线程堆栈信息
	</title>
	</head>
	<body>
	<h3>线程总数：<%out.println("\t"+Thread.getAllStackTraces().entrySet().size()+"\n<br>");%></h3>
	<h3>内存信息：当前分配内存：<%out.println("\t"+Runtime.getRuntime().totalMemory()/1024/1024+"M\t\t");%>;当前剩余内存:<%out.println("\t"+Runtime.getRuntime().freeMemory()/1024/1024+"M\t\t");%>;最大堆内存：<%out.println("\t"+Runtime.getRuntime().maxMemory()/1024/1024+"M\t\t");%></h3>
	<br>
	<%
		for(Map.Entry<Thread, StackTraceElement[]> stackTrace:Thread.getAllStackTraces().entrySet()){
			Thread thread = (Thread)stackTrace.getKey();
			StackTraceElement[] traces = (StackTraceElement[])stackTrace.getValue();
			if(thread.equals(Thread.currentThread())){
				continue;
			}
			out.println("<br>\n线程名称:"+thread.getName()+"  状态："+thread.getState()+" \n<br>");
			for(StackTraceElement element:traces){
				out.println("\t"+element+"\n<br>");
			}
		}
	%>
	</body>
	</html>