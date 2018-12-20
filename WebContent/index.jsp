<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<title>字符画</title>
<script>
	function check(){
		if(document.getElementById("picture").files[0].size>(1024*1024*5)){
			alert("图片不能超过5M!");
			return false;
		}else if(!/\.(jpg|jpeg|png|gif)/i.test(document.getElementById("picture").files[0].name)){
			alert("图片只能是png,jpg,jpeg,gif!");
			return false
		}else{
			document.getElementById("tip").innerHTML="正在跳转...";
			return true;
		}
	}
</script>
</head>
<body>
<form name="myform" action="/AsciiPicture/upload.html" method="post" enctype="multipart/form-data" onsubmit="return check();">
	<input type="file" name="picture" id="picture" required>
	<input type="submit" value="上传">
</form>
	<br>
	<p	id="tip"></p>
</body>
</html>