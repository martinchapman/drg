<?php

if ( !isset($_POST['password']) || !isset($_POST['id']) || !isset($_POST['name']) || !isset($_POST['path']) ) {
	
	echo "Authentication failed.";

	die();

}

if ( $_POST['password'] != "<Password>" ) {
	
	echo "Authentication failed.";

	die();
	
}

$link = mysql_connect('127.0.0.1', '<Username>', '<Password>') or die('Could not connect: ' . mysql_error());

mysql_select_db('<Database>') or die('Could not select database');

mysql_query("INSERT INTO paths (ID, Name, PATH) VALUES('" . $_POST['id'] . "', '" . $_POST['name']. "', '" . $_POST['path'] . "') ON DUPLICATE KEY UPDATE path='" . $_POST['path'] . "'") or die(mysql_error()); 

?>
