//The XML HTTP Request Object
var ajaxRequest = new XMLHttpRequest();

//The Web Servers's address
var servers = new Array();
servers[0] = "http://localhost:8084/JWebService/";
//servers[1] = "http://localhost/webservice";

//servers[0] = "http://localhost/webservice";
//servers[1] = "http://localhost:8080/jwebservice";

var currentServer = 0;

//The Database's x509 Certificate (512 bits)
/*var cert = "-----BEGIN CERTIFICATE-----\
MIIDCzCCArWgAwIBAgIJAMrJd1jTb1cKMA0GCSqGSIb3DQEBBQUAMIGNMQswCQYD\
VQQGEwJQVDEPMA0GA1UECBMGTGlzYm9uMQ8wDQYDVQQHEwZMaXNib24xCzAJBgNV\
BAoTAlVMMQ0wCwYDVQQLEwRGQ1VMMRowGAYDVQQDExFBbmRlcnNvbiBCYXJyZXR0\
bzEkMCIGCSqGSIb3DQEJARYVYWJhcnJldHRvNzhAZ21haWwuY29tMB4XDTEzMDMy\
NTE4MzQzNVoXDTEzMDQyNDE4MzQzNVowgY0xCzAJBgNVBAYTAlBUMQ8wDQYDVQQI\
EwZMaXNib24xDzANBgNVBAcTBkxpc2JvbjELMAkGA1UEChMCVUwxDTALBgNVBAsT\
BEZDVUwxGjAYBgNVBAMTEUFuZGVyc29uIEJhcnJldHRvMSQwIgYJKoZIhvcNAQkB\
FhVhYmFycmV0dG83OEBnbWFpbC5jb20wXDANBgkqhkiG9w0BAQEFAANLADBIAkEA\
y5Nxl5c3t4nXiCXGayTochTMdT/pTp1kbjV6qC1L/kXweSjGlypg9u6I2SdenG15\
UTETxjB2NERSeh4l+IKcCQIDAQABo4H1MIHyMB0GA1UdDgQWBBQ57mjCWWUyCV0+\
wg8Qfl8GWmhxzDCBwgYDVR0jBIG6MIG3gBQ57mjCWWUyCV0+wg8Qfl8GWmhxzKGB\
k6SBkDCBjTELMAkGA1UEBhMCUFQxDzANBgNVBAgTBkxpc2JvbjEPMA0GA1UEBxMG\
TGlzYm9uMQswCQYDVQQKEwJVTDENMAsGA1UECxMERkNVTDEaMBgGA1UEAxMRQW5k\
ZXJzb24gQmFycmV0dG8xJDAiBgkqhkiG9w0BCQEWFWFiYXJyZXR0bzc4QGdtYWls\
LmNvbYIJAMrJd1jTb1cKMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADQQAu\
uRqdpLOuR8j1ykCXc1TB1M/oGNCG2a0K35/VbDe8LuxRXQu5UANDs1n+6mLGieNa\
76vgy7cc2kHtWYdmySAp\
-----END CERTIFICATE-----";*/

/** 1024 bits Cert **/
/*var cert = "-----BEGIN CERTIFICATE-----\
MIIDkDCCAvmgAwIBAgIJAJQnDzsw0QrmMA0GCSqGSIb3DQEBBQUAMIGNMQswCQYD\
VQQGEwJQVDEPMA0GA1UECBMGTGlzYm9uMQ8wDQYDVQQHEwZMaXNib24xCzAJBgNV\
BAoTAlVMMQ0wCwYDVQQLEwRGQ1VMMRowGAYDVQQDExFBbmRlcnNvbiBCYXJyZXR0\
bzEkMCIGCSqGSIb3DQEJARYVYWJhcnJldHRvNzhAZ21haWwuY29tMB4XDTEzMDMx\
MzE2NDY0M1oXDTEzMDQxMjE2NDY0M1owgY0xCzAJBgNVBAYTAlBUMQ8wDQYDVQQI\
EwZMaXNib24xDzANBgNVBAcTBkxpc2JvbjELMAkGA1UEChMCVUwxDTALBgNVBAsT\
BEZDVUwxGjAYBgNVBAMTEUFuZGVyc29uIEJhcnJldHRvMSQwIgYJKoZIhvcNAQkB\
FhVhYmFycmV0dG83OEBnbWFpbC5jb20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJ\
AoGBAMsYXL/vVsuHj7H8BleESZDlTGGWsrxZxek5XwYnchT+v1REuxZ8t6iLujAS\
4U8csCYbDWkCd5vWNLSowp2eyWu2xRL7OenIkgZWYWMRUeGA4Uuke//bszgMGwQ7\
7vb5Ty4LRW2r/Tn1j1ILvGCUsnLuVVSXMNLg3RNNcBnOnR5LAgMBAAGjgfUwgfIw\
HQYDVR0OBBYEFC8dvHGDK6kycebW5hyYFhH3srLiMIHCBgNVHSMEgbowgbeAFC8d\
vHGDK6kycebW5hyYFhH3srLioYGTpIGQMIGNMQswCQYDVQQGEwJQVDEPMA0GA1UE\
CBMGTGlzYm9uMQ8wDQYDVQQHEwZMaXNib24xCzAJBgNVBAoTAlVMMQ0wCwYDVQQL\
EwRGQ1VMMRowGAYDVQQDExFBbmRlcnNvbiBCYXJyZXR0bzEkMCIGCSqGSIb3DQEJ\
ARYVYWJhcnJldHRvNzhAZ21haWwuY29tggkAlCcPOzDRCuYwDAYDVR0TBAUwAwEB\
/zANBgkqhkiG9w0BAQUFAAOBgQBXZyOIKQhAicywRhsjljwjNLn+lFB7vMMJi9Qz\
M5xKqWLL5uV3Nr/ox6NEv50eyCuhJowPZ7Q3N2Y0Zyar3ZW66ExE9R6un15ekOdV\
xerF1x+mj46P3YA9HpS2eYESLQbI8EFNYRekX5FISiGAXyhd7FnO4syT8zCHGmWj\
eKiF8g==\
-----END CERTIFICATE-----";*/

/** 2048 bits Cert **/
var cert = "-----BEGIN CERTIFICATE-----\
MIIElTCCA32gAwIBAgIJAIJSP9qJzn9uMA0GCSqGSIb3DQEBBQUAMIGNMQswCQYD\
VQQGEwJQVDEPMA0GA1UECBMGTGlzYm9uMQ8wDQYDVQQHEwZMaXNib24xCzAJBgNV\
BAoTAlVMMQ0wCwYDVQQLEwRGQ1VMMRowGAYDVQQDExFBbmRlcnNvbiBCYXJyZXR0\
bzEkMCIGCSqGSIb3DQEJARYVYWJhcnJldHRvNzhAZ21haWwuY29tMB4XDTEzMDMy\
NTE4NDYxNVoXDTEzMDQyNDE4NDYxNVowgY0xCzAJBgNVBAYTAlBUMQ8wDQYDVQQI\
EwZMaXNib24xDzANBgNVBAcTBkxpc2JvbjELMAkGA1UEChMCVUwxDTALBgNVBAsT\
BEZDVUwxGjAYBgNVBAMTEUFuZGVyc29uIEJhcnJldHRvMSQwIgYJKoZIhvcNAQkB\
FhVhYmFycmV0dG83OEBnbWFpbC5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw\
ggEKAoIBAQDP6QVtF3JAX96wfa1+w158gSpSziyeXvzDPkVeMN1IBLV+kIXKjv6K\
E/UkFziXOz5p1MKwR0u4T4OeNxskY8Pxc+IiQG7uPW4/wGsu6mzkuOS1W8NkTaJ0\
M4bcHUmnHJTuvau+j7MtOQkZ6SXr5Ni//jNOgh1p3QqvepAaA1hhkwRKmRJlTnHF\
q/i45Z0I3Ee5QkzkSiFVhKZ2kU1hAubbx4JyWmcxYfnH3t3GHmCOYNfXtoTNFgO6\
kzwv16LtWu3lM01n3IJ1OuC/8l8Paqyias0QhmAa0EiDnQZzu+pJpG1T8I6bO0DV\
xVLgPsvxUkAADPo/apDFXtVkXCHZOfJ5AgMBAAGjgfUwgfIwHQYDVR0OBBYEFIYl\
N+UabGApjfkwufgJaqGM8DnKMIHCBgNVHSMEgbowgbeAFIYlN+UabGApjfkwufgJ\
aqGM8DnKoYGTpIGQMIGNMQswCQYDVQQGEwJQVDEPMA0GA1UECBMGTGlzYm9uMQ8w\
DQYDVQQHEwZMaXNib24xCzAJBgNVBAoTAlVMMQ0wCwYDVQQLEwRGQ1VMMRowGAYD\
VQQDExFBbmRlcnNvbiBCYXJyZXR0bzEkMCIGCSqGSIb3DQEJARYVYWJhcnJldHRv\
NzhAZ21haWwuY29tggkAglI/2onOf24wDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0B\
AQUFAAOCAQEAgUzii2l0Hv+SLLaTaffGySr2NNagwdEgZvrV5sFUyb1GQfoSRVut\
BhaoFCpKBc162i67s/cpRUw0yFYSQuOyekzoNuNnIo8Tdo/vhE4LM6AO1PnRY3Pt\
Wd1haZCyrkXZ5dA+Sjo7CHJ3IsRG2ulYHK84Z0Tera75P5yaRgGXb6ovr0N3wgdA\
HeHKJudYkIdhIrKKcaUu8uD6KIJALl+m5cFldBv1vZyBUeK3YM8tTVIOtm8Is8LJ\
VDQ2p0ZiWzZXNH7jRPumtZ4XB/5Ad7KFA3vTof2fdLNo00wq6tVo6/wPOHBwjRgO\
yp+SEveWY/0u8C0w7S7rI+GF8bv5bMYyxw==\
-----END CERTIFICATE-----";

//Initial server
var baseUrl = servers[0];

// The Client's ID
var ID;

// The Password's Hash
var P;

//Session Key(s);
var Ks = [];

//Token
var T;

//Initial Vector to AES Decode
var iv = "1234567890123456";

//Number of replicas behind the proxy
var N = 4;

//Number of faults to tolerate
var F = 1;

//Counter
var counter = 0;

//Change server
function changeServer() {
	if((currentServer+1) != servers.length) {
		currentServer++;
		baseUrl = servers[currentServer];
		return true;
	}
	else {
		return false;
	}
}

//RSA Sign Validation
function doVerify(sMsg, hSig) {
	  var x509 = new X509();
	  x509.readCertPEM(cert);
	  var isValid = x509.subjectPublicKeyRSA.verifyString(sMsg, hSig);
	  return isValid;
}

//AES Decode
function aesDecode(cypher, key) {
	
	var ivByteArray = cryptoHelpers.convertStringToByteArray(iv);
	var keyButeArray = cryptoHelpers.convertStringToByteArray(key);
	var bytesToDecrypt = cryptoHelpers.base64.decode(hexToBase64(cypher));
	//document.write(bytesToDecrypt + "<br>");
	
	var decrypt = slowAES.decrypt(bytesToDecrypt, 
	slowAES.modeOfOperation.CBC,
	keyButeArray,
	ivByteArray);
	
	var plain = cryptoHelpers.convertByteArrayToString(decrypt);
	/*if((bytesToDecrypt.length % 16) != 0) { //WTF??????
		plain = plain.substr(0, plain.length-1);
	}*/
	//console.log(cypher + ";" + key + ";" + plain);
	return plain;
}

//AES Encode (afinal desnecessario, mas agora fica aqui)
function aesEncode(text, key) {
	
	var ivByteArray = cryptoHelpers.convertStringToByteArray(iv);
	var keyButeArray = cryptoHelpers.convertStringToByteArray(key);

	var bytesToEncrypt = cryptoHelpers.convertStringToByteArray(text);

	var encrypt = slowAES.encrypt(bytesToEncrypt, 
	slowAES.modeOfOperation.CBC,
	keyButeArray,
	ivByteArray);

	var cypher = base64ToHex(cryptoHelpers.base64.encode(encrypt));
	//document.write(bytesToEncrypt + "<br>");
	
	
	
	/*if((bytesToEncrypt.length % 16) != 0) { // WTF?????
		cypher = cypher.substr(0, cypher.length-1);
	}*/
	//console.log(text + ";" + key + ";" + cypher);
	return cypher;
}

//Request Login to Web Service
function requestLogin (username, password, callback, fallback) {
	ajaxRequest.onreadystatechange = function() {
		if(ajaxRequest.readyState == 4) {
			if(ajaxRequest.status == 200) {
				
				log("RESPONSE", "LOGIN");
				
				/** Check Syntax of JSON Response **/
				var jsonResponse;
				//console.log(ajaxRequest.responseText);
				try {
					jsonResponse = JSON.parse(ajaxRequest.responseText);
					if (
						typeof jsonResponse.STATUS === "undefined"    ||
						typeof jsonResponse.ID === "undefined"        ||
						typeof jsonResponse.EKsT === "undefined"      ||
						typeof jsonResponse.Sa === "undefined"        ||
						typeof jsonResponse.Sb === "undefined"        ||
						typeof jsonResponse.signature === "undefined"
					) {
						var isServerAvaiable = changeServer();
						if(isServerAvaiable) {
							requestLogin(username, password, callback, fallback);
						}
						else {
							fallback("Login: There is no Web Server avaiable! Please try again later. - 1");
						}
					}
				}
				catch (e) {
					var isServerAvaiable = changeServer();
					if(isServerAvaiable) {
						requestLogin(username, password, callback, fallback);
					}
					else {
						fallback("Login: There is no Web Server avaiable! Please try again later. - 2");
					}
				}
				
				/** Check Signature **/
				var signatures = JSON.parse(jsonResponse.signature);
				var Sbs = JSON.parse(jsonResponse.Sb);
				var EKsTs = JSON.parse(jsonResponse.EKsT);
				for (var i = 0; i < N; i++) {

					var signature = signatures[i];
					var transform = jsonResponse.STATUS + jsonResponse.ID + EKsTs[i] + jsonResponse.Sa + Sbs[i];
					var isValid = doVerify(transform, signature);
					/** Signature OK! **/
					if(isValid) {
						/** Check STATUS, ID and Sa **/
						if(jsonResponse.STATUS == "OK") {
							if (jsonResponse.Sa == Sa && jsonResponse.ID == ID) {

								//Set the Session Key
								Ks[i] = hex_sha1(jsonResponse.Sa + Sbs[i] + hex_sha1(password + i));

								//Set the Token
								T = parseInt(aesDecode(EKsTs[i], Ks[i].substr(0,16)));
								//T = parseInt(jsonResponse.EKsT);

								//Show Initial Page
								//var end = new Date().getTime();
								//console.log(end-start);
								//callback();

								//all good with one replica, increment the counter
								counter++;
							}
							// Sa and/or ID wrong!
							else {
								var isServerAvaiable = changeServer();
								if(isServerAvaiable) {
									requestLogin(username, password, callback, fallback);
								}
								else {
									fallback("Login: There is no Web Server avaiable! Please try again later. - 3");
								}
							}
						}
						// STATUS == "NOK"
						else {
							var isServerAvaiable = changeServer();
							fallback("Login: There was a problem in the contact with one of ours servers.<br> Please, login again. - 1");
							if(!isServerAvaiable) {
								currentServer = -1;
								changeServer();
							}
						}
					}
					/** Signature NOK! **/
					else {
						var isServerAvaiable = changeServer();
						if(isServerAvaiable) {
							requestLogin(username, password, callback, fallback);
						}
						else {
							fallback("Login: There is no Web Server avaiable! Please try again later. - 4");
						}
					}
				}
				
				console.log("Out of the loop");
				//enough replicas?
				if (counter >= Math.ceil((N+F+1)/2)) {
					//Show Initial Page
					var end = new Date().getTime();
					//console.log("Enough replicas!");
					console.log("Enough replicas! Total time: " + (end-start) + " miliseconds");
					callback();

				}
				
			}
			/** HTTP Status != 200 **/
			else {
				var isServerAvaiable = changeServer();
				if(isServerAvaiable) {
					requestLogin(username, password, callback, fallback);
				}
				else {
					fallback("Login: There is no Web Server avaiable! Please try again later. - 5");
				}
			}
			
		}
	};
	
	/** PREPARE **/
	// Set the Global Variable ID
	ID = username;
	
	// The Secret's Hash
	P = hex_sha1(password);
	console.log("Original P: " + P);

	//multi P
	var Pi = [];
	for (i = 0; i < N; i++) {
		Pi[i] = hex_sha1(password + i);
		console.log("P" + i + ": " + Pi[i]);
	}

	
	// The Client's Salt
	var Sa = Math.floor((Math.random()*1e+10)+1);
	
	// The Authentication's Hash Field
	//var h = hex_sha256(P + Sa);
	//console.log("Original h: " + h);

	//multi hash fields
	var Hi = [];
	for (i = 0; i < N; i++) {
		Hi[i] = hex_sha256(Pi[i] + Sa);
		console.log("H" + i + ": " + Hi[i]);
	}
	
	
	/** SEND **/

	//ORIGINAL!!!
	//var message = "ID=" + ID + "&Sa=" + Sa + "&h=" + h;

	
	var out = new Array();

	for (key in Hi) {
		out.push("Hi" + key + '=' + encodeURIComponent(Hi[key]));
	}

	var message = "ID=" + ID + "&Sa=" + Sa + "&" + out.join("&");

	var url = baseUrl + "/Login?" + message;
	console.log(url);
	ajaxRequest.open("get", url, true);
	log("REQUEST", "LOGIN");
	ajaxRequest.send(null);
	var start = new Date().getTime();
}

//Request Data to Web Service
function requestData(data, urlRequest, callback, fallback, transformJsonFunction) {
	ajaxRequest.onreadystatechange = function() {
		if(ajaxRequest.readyState == 4) {
			if(ajaxRequest.status == 200) {
				
				log("RESPONSE", urlRequest);
				//console.log(ajaxRequest.responseText);
				var jsonResponse;
				
				/** Sintaxe Validation **/
				try {
					jsonResponse = JSON.parse(ajaxRequest.responseText);
					
					console.log(jsonResponse);
					
					if (
						typeof jsonResponse.Status === "undefined"    ||
						typeof jsonResponse.ID === "undefined"        ||
						typeof jsonResponse.TOp === "undefined"      ||
						typeof jsonResponse.RS === "undefined"        ||
						typeof jsonResponse.EKsT === "undefined"        ||
						typeof jsonResponse.signature === "undefined"
					) {
						console.log("Invalid Syntaxe");
						var isServerAvaiable = changeServer();
						if(isServerAvaiable) {
							requestData(data, urlRequest, callback, fallback, transformJsonFunction);
						}
						else {
							fallback("Request: There is no Web Server avaiable! Please try again later. - 1");
						}
					}
				}
				catch (e) {
					console.log("teste");
					var isServerAvaiable = changeServer();
					if(isServerAvaiable) {
						requestData(data, urlRequest, callback, fallback, transformJsonFunction);
					}
					else {
						fallback("Request: There is no Web Server avaiable! Please try again later. - 2");
					}
				}
				
				/** Check Signature **/
				var signature = jsonResponse.signature;
				var transform = "";
				if(transformJsonFunction != null) {
					transform = transformJsonFunction(jsonResponse.RS);
				}
				else {
					transform = jsonResponse.RS;
				}
				var messageToVerify = jsonResponse.Status + jsonResponse.ID + jsonResponse.TOp + transform + jsonResponse.EKsT;
				var isValid = doVerify(messageToVerify, signature);
				if(isValid) {
					/** Status Validation **/
					if(jsonResponse.Status == "OK") {
						/** ID and T Validation **/
						if(jsonResponse.ID == ID && jsonResponse.TOp == T + 1) {
							T = parseInt(aesDecode(jsonResponse.EKsT, Ks.substr(0,16)));
							
							console.log(T);
							callback(jsonResponse.RS);
						}
						else {
							console.log("ToKen Invalid:" + jsonResponse.TOp + " " + (T + 1) + " " + T);
							var isServerAvaiable = changeServer();
							if(isServerAvaiable) {
								requestData(data, urlRequest, callback, fallback, transformJsonFunction);
							}
							else {
								fallback("Request: There is no Web Server avaiable! Please try again later. - 3");
							}
						}
					}
					// Status Invalid
					else {
						console.log("NOK;Integrity Invalid");
						var isServerAvaiable = changeServer();
						if(isServerAvaiable) {
							fallback("Request: There was a problem in the contact with one of ours servers.<br> Please, login again. - 1");
						}
						else {
							fallback("Request: There is no Web Server available.<br> Please, try again later. - 4");
						}
					}
				}
				// Integrity Invalid
				else {
					console.log(jsonResponse.Status);
					var isServerAvaiable = changeServer();
					if(isServerAvaiable) {
						requestData(data, urlRequest, callback, fallback, transformJsonFunction);
					}
					else {
						fallback("Request: There is no Web Server avaiable! Please try again later. - 5");
					}
				}
			}
			// Timeout
			else {
				var isServerAvaiable = changeServer();
				if(isServerAvaiable) {
					requestData(data, urlRequest, callback, fallback, transformJsonFunction);
				}
				else {
					fallback("Request: There is no Web Server avaiable! Please try again later. - 6");
				}
			}
		}
	};
	
	/** PREPARE **/
	// The Operartion Token encrypted
	var TOp = (parseInt(T) + 1).toString();
	//var TOp = (parseInt(T)).toString();
	
	// The message
	//var message = "ID=" + ID + "&TOp=" + TOp + "&D=" + data;
	var message = "ID=" + ID + "&TOp=" + TOp + "&CMD=" + data;
	
	// The Integrity's Field(s)
	var macs = [];
	for (var i = 0; i < N; i++) {
		var transform = ID + TOp + data + Ks[i];
		var mac = hex_sha256(transform);
		//var mac = aesEncode(hex_sha256(transform),Ks[0]);
		console.log("mac: " + mac);
		console.log(i + "th key: " + Ks[i]);
		macs[i] = mac;
        }

	var out = new Array();

	for (m in macs) {
		out.push("Mi" + m + '=' + encodeURIComponent(macs[m]));
	}

	message += "&" + out.join("&");

	/** SEND **/
	var url = baseUrl + urlRequest + message;
	console.log(encodeURI(url));
	ajaxRequest.open("get", encodeURI(url), true);
	log("REQUEST", urlRequest);
	ajaxRequest.send(null);
}


//Log REQUEST and RESPONSE events
function log(type, operation) {
	var currentdate = new Date(); 
	var datetime = currentdate.getDate() + "/"
	                + (currentdate.getMonth()+1)  + "/" 
	                + currentdate.getFullYear() + " @ "  
	                + currentdate.getHours() + ":"  
	                + currentdate.getMinutes() + ":" 
	                + currentdate.getSeconds() + "."
	                + currentdate.getMilliseconds();
	console.log(type + " to Operation " + operation + " in " + datetime);
}
