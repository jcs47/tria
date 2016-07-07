<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

/** Connect to Database **/
$db = mysqli_connect("localhost", "root", "", "classicmodels");
if(mysqli_connect_errno($db)) {
	die("Database Connect Error: " + mysqli_connect_error());
}

/** Get Session Key and Token **/
$sql_token = "SELECT Ks, token FROM users WHERE id = '" . $_POST["ID"]."'";
$rs_token = mysqli_query($db, $sql_token);
if(mysqli_num_rows($rs_token) != 0) {
	$row = mysqli_fetch_assoc($rs_token);
	$Ks = $row["Ks"];
	$T = $row["token"];

	/** Check Message Authentication **/
	$message = $_POST["ID"] . $_POST["TOp"] . $_POST["D"];
	$mac = $_POST["MAC"];
	if(hash("sha256", $message . $Ks) == $mac) {

		/** Token Validation **/
		if($_POST["TOp"] == $T + 1) {
			/** Set the new Token **/
			$T  = mt_rand();
			$sql_T = "UPDATE users SET token = " . $T . " WHERE id = '" . $_POST["ID"]."'";
			$rs_T = mysqli_query($db, $sql_T);
			if($rs_T === FALSE) {
				/** Handle Exception **/
				echo "UPDATE new T ERROR!";
			}
			else {
				/** Encode the New Token with XOR **/
				$EKsT = "";
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

				/** Delete the data **/
				$sql = "DELETE FROM offices WHERE officeCode = '" . $_POST["D"]. "'";
				//echo $sql;
				$rs = mysqli_query($db, $sql);
				if($rs === TRUE) {
					$RS = "OK";
					$transform = $_POST["ID"] . $_POST["TOp"] . $RS . $EKsT;
				}
				else {
			 	/** Handle Exceptiopn **/
					echo "Erro INSERT office";
				}
				//echo $transform;
				/** Close connection to Database **/
				mysqli_close($db);
					
				/** Sign **/
				/*$key_path = "/home/barretto/keys/512/private_key_512_pkcs8.pem";
				 $fp = fopen($key_path,"r");
				$private_key = fread($fp, filesize($key_path));
				fclose($fp);*/
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
				openssl_sign($transform, $sig, $pkeyid);
				openssl_free_key($pkeyid);
				//echo bin2hex($sig);

				/** The Response **/
				$response["ID"] = $_POST["ID"];
				$response["TOp"] = $_POST["TOp"]; //The previous Token
				$response["RS"] = $RS;
				$response["EKsT"] = $EKsT; // The next Token
				$response["signature"] = bin2hex($sig);
				echo json_encode($response);
			}
		}
		else {
			/** Handle Exception **/
			echo "Token Invalid!";
		}
	}
}
else {
	/** Handle Exception **/
	echo "Integrity Invalid!";
}





?>