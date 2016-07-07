<?php
include '../aes.php';
ini_set('display_errors', 1);
error_reporting(E_ALL);

header("Content-Type: text/plain");
header("Access-Control-Allow-Origin: *");

//Connect to Database
$db = mysqli_connect("169.254.83.95", "root", "", "classicmodels");
if(mysqli_connect_errno($db)) {
	header(':', true, 404);
}

$private_key = "-----BEGIN PRIVATE KEY-----
MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAy5Nxl5c3t4nXiCXG
ayTochTMdT/pTp1kbjV6qC1L/kXweSjGlypg9u6I2SdenG15UTETxjB2NERSeh4l
+IKcCQIDAQABAkBj1rrV2n6jz8FZbxVBhMdO865WLnLwcDZJ6oZuJlDnsWs9A9L9
W+o12N5DO5PDSe520AXB3aH+gHFOKfh2hnuBAiEA6s0Y01sSCLKAQ9wfZTUxNdWq
b5XxdnYpA1MdSnvW31ECIQDd9KVfKGH5zjDsRDsPqISomVnDNnCOyINpesCLMmHz
OQIgao61i4WzA3tutl05akbflFzpQka8FFS/fCDGFD1rYGECIFBgiQaVxC1HixX0
e4LnHutDZWs5Kre6S0SGbHW8EBaZAiBJEGsAMFUrLS4gImnDmV41iOzSo7W0L97F
XOeAnNWAHQ==
-----END PRIVATE KEY-----";

//Authentication
$sql = "SELECT secret FROM users WHERE id = '" . $_GET["ID"] . "'";
$rs = mysqli_query($db, $sql);
if(mysqli_num_rows($rs) != 0) {
	$row = mysqli_fetch_assoc($rs);
	$P = $row["secret"];

	if(hash("sha256", $P . $_GET["Sa"]) == $_GET["h"]) {
		//The Token
		$T  = mt_rand();
		//$T = 1000;

		//The Server's Salt
		$Sb  = mt_rand();
		//$Sb = 1001;

		//The Session Key
		$Ks = bin2hex(sha1($_GET["Sa"] . $Sb . $P, true));
		//echo ($Ks)."<br>";

		//Update the fields in the database
		$sql_Ks_T = "UPDATE users SET Ks = '" . $Ks ."', token = " . $T . " WHERE id = '" . $_GET["ID"]."'";
		$rs_Ks_T = mysqli_query($db, $sql_Ks_T);
		if($rs_Ks_T === FALSE) {
			echo "UPDATE Ks/T ERROR!";
		}
		else {


		// Encoding the Token with AES
		/*$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
		$iv = substr($P, 0, $iv_size);
		$EKsT = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $Ks, $T, MCRYPT_MODE_CBC, $iv);
		//echo bin2hex($EKsT)."<br>";*/

		//Encoding the Token with AES
		$EKsT = "";
		$EKsT = encrypt($T, substr($Ks, 0, 16));
		
		
		for($i = 0; $i < strlen($T); $i++) {
			$char = ord(substr($T,$i)) ^ ord(substr($Ks, $i));
			if(strlen($char) == 1) {
				$char = "00" . $char;
			};
			if(strlen($char) == 2) {
				$char = "0" . $char;
			};
			$EKsT .= $char;
		}
		//echo ($EKsT)."<br>";

		/** Sign the Data **/
		//$key_path = "/Users/barretto/Documents/Mestrado/Tese/bin/keys/512/private_key_512_pkcs8.pem";
		//$fp = fopen($key_path,"r");
		//$private_key = fread($fp, filesize($key_path));
		
		//fclose($fp);
		$pkeyid = openssl_get_privatekey($private_key);
		$data = "OK" . $_GET["ID"] . $EKsT . $_GET["Sa"] . $Sb;
		//echo $data."<br>";
		openssl_sign($data, $sig, $pkeyid);
		openssl_free_key($pkeyid);

		/** Mount the Response **/
		$response["STATUS"] = "OK";
		$response["ID"] = $_GET["ID"];
		$response["EKsT"] = ($EKsT);
		//$response["EKsT"] = "XXX";
		$response["Sa"] = $_GET["Sa"];
		$response["Sb"] = "" . $Sb;
		$response["signature"] = bin2hex($sig);
		echo json_encode($response);
		}
	}
	else {
		$pkeyid = openssl_get_privatekey($private_key);
		$data = "NOK" . $_GET["ID"] . "0" . $_GET["Sa"] . "0";
		openssl_sign($data, $sig, $pkeyid);
		openssl_free_key($pkeyid);
		/** Mount the Response **/
		$response["STATUS"] = "NOK";
		$response["ID"] = $_GET["ID"];
		$response["EKsT"] = "0";
		//$response["EKsT"] = "XXX";
		$response["Sa"] = $_GET["Sa"];
		$response["Sb"] = "0";
		$response["signature"] = bin2hex($sig);
		echo json_encode($response);
	}
}
else {
	header(':', true, 401);
}

?>
