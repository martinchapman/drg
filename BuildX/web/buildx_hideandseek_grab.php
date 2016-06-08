<?php

if ( !isset($_POST['password']) ) {
	
	echo "Authentication failed.";

	die();

}

if ( $_POST['password'] != "<Password>" ) {
	
	echo "Authentication failed.";

	die();
	
}

$link = mysql_connect('127.0.0.1', '<Username>', '<Password>') or die('Could not connect: ' . mysql_error());

mysql_select_db('<Database>') or die('Could not select database');

$result = mysql_query("SELECT * FROM paths");

while($row = mysql_fetch_assoc($result))
{
   print_r($row);
}

?>
