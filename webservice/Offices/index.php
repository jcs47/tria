<?php
include '../aes.php';
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

/** Connect to Database **/
//$db = mysqli_connect("localhost", "root", "", "classicmodels");
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
$pkeyid = openssl_get_privatekey($private_key);


/** Get Session Key and Token **/
$sql_token = "SELECT Ks, token FROM users WHERE id = '" . $_GET["ID"]."'";
$rs_token = mysqli_query($db, $sql_token);
if(mysqli_num_rows($rs_token) != 0) {
	$row = mysqli_fetch_assoc($rs_token);
	$Ks = $row["Ks"];
	$T = $row["token"];

	/** Check Message Authentication **/
	$message = $_GET["ID"] . $_GET["TOp"];
	$mac = $_GET["MAC"];

	if(hash("sha256", $message . $Ks) == $mac) {
		/** Check the Token **/
		if($_GET["TOp"] == $T + 1) {
			/** Set the new Token **/
			$T  = mt_rand();
			$sql_T = "UPDATE users SET token = " . $T . " WHERE id = '" . $_GET["ID"]."'";
			$rs_T = mysqli_query($db, $sql_T);
			if($rs_T === FALSE) {
				/** Handle Exception **/
				echo "UPDATE new T ERROR!";
			}
			else {
				/** Encode the New Token with AES **/
				$EKsT = "";
				$EKsT = encrypt($T, substr($Ks, 0, 16));
				/* for($i = 0; $i < strlen($T); $i++) {
					$char = ord(substr($T,$i)) ^ ord(substr($Ks, $i));
					if(strlen($char) == 1) {
						$char = "00" . $char;
					};
					if(strlen($char) == 2) {
						$char = "0" . $char;
					};
					$EKsT .= $char;
				} */
				
				/** Get the data **/
				$sql = "SELECT * FROM offices";
				$rs = mysqli_query($db, $sql);
				$offices = array();
				while($row = mysqli_fetch_assoc($rs)) {
					if($row["officeCode"] != null) {
						$offices[] = $row;
					}
				}
				
				/** Close connection to Database **/
				mysqli_close($db);
				
				/** Transform ResultSet **/
				$transform = "";
				for($i=0; $i < count($offices); $i++) {
					$transform .= $offices[$i]["officeCode"];
					$transform .= $offices[$i]["city"];
					$transform .= $offices[$i]["phone"];
					$transform .= $offices[$i]["addressLine1"];
					$transform .= $offices[$i]["addressLine2"];
					$transform .= $offices[$i]["state"];
					$transform .= $offices[$i]["country"];
					$transform .= $offices[$i]["postalCode"];
					$transform .= $offices[$i]["territory"];
				}
			
				/** Sign **/
				/*$key_path = "/home/barretto/keys/512/private_key_512_pkcs8.pem";
				 $fp = fopen($key_path,"r");
				$private_key = fread($fp, filesize($key_path));
				fclose($fp);*/
			
				
				$status = "OK";
				$messageToSign = $status . $_GET["ID"] . $_GET["TOp"] . $transform . $EKsT;
				openssl_sign($messageToSign, $sig, $pkeyid);
				openssl_free_key($pkeyid);
				//echo bin2hex($sig);
				
				/** The Response **/
				$response["Status"] = $status;
				$response["ID"] = $_GET["ID"];
				$response["TOp"] = $_GET["TOp"]; //The previous Token
				$response["RS"] = $offices;
				$response["EKsT"] = $EKsT; // The next Token
				$response["signature"] = bin2hex($sig);
				echo json_encode($response);
			}
		}
		else {
			$status = "NOK;Token Invalid";
			$messageToSign = $status . $_GET["ID"] . $_GET["TOp"];
			openssl_sign($messageToSign, $sig, $pkeyid);
			openssl_free_key($pkeyid);
			
			$response["Status"] = $status;
			$response["ID"] = $_GET["ID"];
			$response["TOp"] = $_GET["TOp"]; //The previous Token
			$response["signature"] = bin2hex($sig);
			echo json_encode($response);
		}
	}
	else {
		$status = "NOK;MAC Invalid";
		$messageToSign = $status . $_GET["ID"] . $_GET["TOp"];
		openssl_sign($messageToSign, $sig, $pkeyid);
		openssl_free_key($pkeyid);
			
		$response["Status"] = $status;
		$response["ID"] = $_GET["ID"];
		$response["TOp"] = $_GET["TOp"]; //The previous Token
		$response["signature"] = bin2hex($sig);
		echo json_encode($response);
	}
}

?>
