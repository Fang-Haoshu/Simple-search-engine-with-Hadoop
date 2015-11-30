<?php
//获取搜索关键字
$keyword=trim($_POST["keyword"]);
//检查是否为空
if($keyword==""){
echo"您要搜索的关键字不能为空";
exit;//结束程序
}
?> 



<div class="content tc" >
		<p class="logo"><img width="56" height="56" src="logo.png" alt="logo" /></p>
		<div style="position:absolute; left:80px; top:30px; width:1000px; height:100px;">
			<form method="post" action="http://localhost/search.php">
				<input type="text" style="width:560px;height:35px" name="keyword" maxlength="100" autocomplete="off"/>
				<input type="submit" style="width:100px;height:35px" value="再看一下" />
			</form>
		</div>
						
</div>

<div style="background-color: lightgray; width: 100%; height: 5%;">
</div>
<br>
<br>

<?php
//$dir是搜索的目录,$keyword是搜索的关键字 ,$array是存放的数组
function listFiles($dir,$keyword,&$array,&$array1){
//$handle=opendir($dir);
//读取文件内容
$f=fopen("file","r");
$flag=false;
while (!feof($f)){
$data=fgets($f,1024);
$str=explode("\t",$data);
//是否匹配
if($keyword==$str['0']){//if(eregi("$keyword",$str['0'])){
$array=explode('|',$str[1]);
$flag = true;
break;
}
}
// page rank
rsort($array);


//打印搜索结果
if($flag){
//$array[count($array)-1] = $array[count($array)-1] -' ';
for($temp = 1;$temp < count($array)-1;$temp++){//start from 1 , because array[0]=0;
$str =explode(':',$array[$temp]);
echo "<font 'size='0.3' color='gray'>$str[0] </font>";
$array[$temp]= $str[1];
$f=fopen("web/$array[$temp]","r");
$data=fgets($f,140);
$array[$temp]=$array[$temp]-".txt";
echo "<a href='http://tieba.baidu.com/p/$array[$temp]?pn=1'><font style='position:relative; left:75px;'size='4' color='blue'>$data... </font></a>"."<br>";
$data=fread($f,212);
echo "<div style='position:relative; left:80px; width:500px; '><font size='2' color='black'>$data</font></div>";

echo "<font style='position:relative; left:80px;'size='2' color='gray'> http://tieba.baidu.com/p/$array[$temp]?pn=1 </font>"."<br>"."<br>";
}
}
else
echo "<font style='position:relative; left:80px;'size='2' color='gray'> Miao! there is no result for you.</font>"."<br>"."<br>";

}



//定义数组$array
$array=array();
$arary1=array();
//执行函数
listFiles(".",$keyword,$array,$array1);



?> 
