//The XML HTTP Request Object
var ajaxRequest = new XMLHttpRequest();

//The Web Servers's address
var servers = new Array();
servers[0] = "http://localhost:8080/jwebservice";
servers[1] = "http://localhost/webservice";

//servers[0] = "http://localhost/webservice";
//servers[1] = "http://localhost:8080/jwebservice";

var currentServer = 0;

//The Database's x509 Certificate (512 bits)
var cert = "-----BEGIN CERTIFICATE-----\
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
-----END CERTIFICATE-----";

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


//Initial server
var baseUrl = servers[0];

// The Client's ID
var ID;

// The Password's Hash
var P;

//Session Key;
var Ks;

//Token
var T;

//Initial Vector to AES Decode
var iv = "1234567890123456";

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
	if((bytesToDecrypt.length % 16) != 0) {
		plain = plain.substr(0, plain.length-1);
	}
	//console.log(cypher + ";" + key + ";" + plain);
	return plain;
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
							fallback("There is no Web Server avaiable! Please try again later.");
						}
					}
				}
				catch (e) {
					var isServerAvaiable = changeServer();
					if(isServerAvaiable) {
						requestLogin(username, password, callback, fallback);
					}
					else {
						fallback("There is no Web Server avaiable! Please try again later.");
					}
				}
				
				/** Check Signature **/
				var signature = jsonResponse.signature;
				var transform = jsonResponse.STATUS + jsonResponse.ID + jsonResponse.EKsT + jsonResponse.Sa + jsonResponse.Sb;
				var isValid = doVerify(transform, signature);
				/** Signature OK! **/
				if(isValid) {
					/** Check STATUS, ID and Sa **/
					if(jsonResponse.STATUS == "OK") {
						if (jsonResponse.Sa == Sa && jsonResponse.ID == ID) {
							//Set the Session Key
							Ks = hex_sha1(jsonResponse.Sa + jsonResponse.Sb + P);
							//Set the Token
							T = parseInt(aesDecode(jsonResponse.EKsT, Ks.substr(0,16)));
							//console.log(T);
							//Show Initial Page
							var end = new Date().getTime();
							console.log(end-start);
							callback();
						}
						// Sa and/or ID wrong!
						else {
							var isServerAvaiable = changeServer();
							if(isServerAvaiable) {
								requestLogin(username, password, callback, fallback);
							}
							else {
								fallback("There is no Web Server avaiable! Please try again later.");
							}
						}
					}
					// STATUS == "NOK"
					else {
						var isServerAvaiable = changeServer();
						fallback("There was a problem in the contact with one of ours servers.<br> Please, login again.");
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
						fallback("There is no Web Server avaiable! Please try again later.");
					}
				}
				
			}
			/** HTTP Status != 200 **/
			else {
				var isServerAvaiable = changeServer();
				if(isServerAvaiable) {
					requestLogin(username, password, callback, fallback);
				}
				else {
					fallback("There is no Web Server avaiable! Please try again later.");
				}
			}
			
		}
	};
	
	/** PREPARE **/
	// Set the Global Variable ID
	ID = username;
	
	// The Secret's Hash
	P = hex_sha1(password);
	
	// The Client's Salt
	var Sa = Math.floor((Math.random()*1e+10)+1);
	
	// The Authentication's Hash Field
	var h = hex_sha256(P + Sa);
	
	
	/** SEND **/
	var message = "ID=" + ID + "&Sa=" + Sa + "&h=" + h;
	var url = baseUrl + "/Login?" + message;
	//console.log(url);
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
							fallback("There is no Web Server avaiable! Please try again later.");
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
						fallback("There is no Web Server avaiable! Please try again later.");
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
							console.log("ToKen Invalid");
							var isServerAvaiable = changeServer();
							if(isServerAvaiable) {
								requestData(data, urlRequest, callback, fallback, transformJsonFunction);
							}
							else {
								fallback("There is no Web Server avaiable! Please try again later.");
							}
						}
					}
					// Status Invalid
					else {
						console.log("NOK;Integrity Invalid");
						var isServerAvaiable = changeServer();
						if(isServerAvaiable) {
							fallback("There was a problem in the contact with one of ours servers.<br> Please, login again.");
						}
						else {
							fallback("There is no Web Server available.<br> Please, try again later.");
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
						fallback("There is no Web Server avaiable! Please try again later.");
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
					fallback("There is no Web Server avaiable! Please try again later.");
				}
			}
		}
	};
	
	/** PREPARE **/
	// The Operartion Token encrypted
	var TOp = (parseInt(T) + 1).toString();
	//var TOp = (parseInt(T)).toString();
	
	// The message
	var message = "ID=" + ID + "&TOp=" + TOp + "&D=" + data;
	
	// The Integrity's Field
	var transform = ID + TOp + data;
	var mac = hex_sha256(transform + Ks);
	message += "&MAC=" + mac;
	
	/** SEND **/
	var url = baseUrl + urlRequest + message;
	ajaxRequest.open("get", url, true);
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
