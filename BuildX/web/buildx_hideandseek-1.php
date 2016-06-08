<?php

$link = mysql_connect('127.0.0.1', 'buildx_user', 'q8mjYSmB') or die('Could not connect: ' . mysql_error());

if ( !isset($_POST['password']) || !isset($_POST['id']) || !isset($_POST['name']) || !isset($_POST['path']) ) {
	
	echo "Authentication failed.";

	die();

}

if ( $_POST['password'] != "Y2mKqTcP" ) {
	
	echo "Authentication failed.";

	die();
	
}

mysql_select_db('buildx_hideandseek') or die('Could not select database');

mysql_query("INSERT INTO paths (ID, Name, PATH) VALUES('" . $_POST['id'] . "', '" . $_POST['name']. "', '" . $_POST['path'] . "') ON DUPLICATE KEY UPDATE path='" . $_POST['path'] . "'") or die(mysql_error()); 

?>
