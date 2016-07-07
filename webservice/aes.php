<?php 
	function encrypt($str, $key) {
		$iv  = '1234567890123456';
		$str = pkcs5_pad($str);
		$td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);
		mcrypt_generic_init($td, $key, $iv);
		$encrypted = mcrypt_generic($td, $str);
		mcrypt_generic_deinit($td);
		mcrypt_module_close($td);
		return bin2hex($encrypted);
	}

	function decrypt($code) {
		$code = hex2bin($code);
		$iv = $iv;
		$td = mcrypt_module_open('rijndael-256', '', 'cbc', $iv);
		mcrypt_generic_init($td, $key, $iv);
		$decrypted = mdecrypt_generic($td, $code);
		mcrypt_generic_deinit($td);
		mcrypt_module_close($td);
		$ut =  utf8_encode(trim($decrypted));
		return pkcs5_unpad($ut);
	}

	function hex2bin($hexdata) {
		$bindata = '';
		for ($i = 0; $i < strlen($hexdata); $i += 2) {
			$bindata .= chr(hexdec(substr($hexdata, $i, 2)));
		}
		return $bindata;
	}

	function pkcs5_pad ($text) {
		$blocksize = 16;
		$pad = $blocksize - (strlen($text) % $blocksize);
		return $text . str_repeat(chr($pad), $pad);
	}

	function pkcs5_unpad($text) {
		$pad = ord($text{strlen($text)-1});
		if ($pad > strlen($text)) {
			return false;
		}
		if (strspn($text, chr($pad), strlen($text) - $pad) != $pad) {
			return false;
		}
		return substr($text, 0, -1 * $pad);
	}

/* $plain = "123456789012345";
$key = "de1310982b646af063e7314e8ddd4787";
$encrypted = encrypt($plain, $key);
echo $encrypted; */

?>