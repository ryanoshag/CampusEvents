<?php 
if(isset($_POST['name']) && isset($_POST['building'])){
$strDate = '2013-'.$_POST['month'].'-'.$_POST['day'];
var_dump($strDate);
$mysqli  = new mysqli('localhost','projectuser','projectmysqlUser','project');
$query = "insert into event(lid,event_name,date,start_time,end_time) values ((select id from location where name = ? limit 1),?,?,?,?)";
$stmt = $mysqli->stmt_init();
if($stmt = $mysqli->prepare($query)){
	$stmt->bind_param("sssss",$_POST['building'],$_POST['name'],$strDate,$_POST['starttime'],$_POST['endtime']);
	$stmt->execute();
	$stmt->close();
}
}
header("Location: add.php");
