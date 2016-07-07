function requestLogin (username, password, callback, fallback) {
	ajaxRequest.onreadystatechange = function() {
		if(ajaxRequest.readyState == 4) {
			if(ajaxRequest.status == 200) {
				log("RESPONSE", "LOGIN");
				/** Check Signature **/
				//console.log(ajaxRequest.responseText);
				var jsonResponse = JSON.parse(ajaxRequest.responseText);
				var signature = jsonResponse.signature;
				if(Sa == jsonResponse.Sa) {
					var transform = jsonResponse.ID + jsonResponse.EKsT + jsonResponse.Sa + jsonResponse.Sb;
					var isValid = doVerify(transform, signature);
					/** Signature OK! **/
					if(isValid) {
						//Set the Session Key
						Ks = hex_sha1(jsonResponse.Sa + jsonResponse.Sb + P);
						
						//Set the Token
						T = xorDecode(jsonResponse.EKsT, Ks);
						
						//Show Initial Page
						callback();
					}
					/** Signature NOK! **/
					else {
						var isServerAvaiable = changeServer();
						if(isServerAvaiable) {
							fallback("There was a problem in the contact with one of ours servers.<br> Please, login again.");
						}
						else {
							fallback("There is no Web Server avaiable! Please try again later.");
						}
					}
				}
				else {
					fallback("Invalid Server's Response. Try again.");
				}
			}
			else if (ajaxRequest.status == 401){
				fallback("Invalid ID/Password!");
			}
			else {
				fallback("Error in the contact the ours servers");
			}
		}
	};
	
	/** PREPARE **/
	// Set the Global Variable ID
	ID = username;
	
	// The Secret's Hash
	var P = hex_sha1(password);
	
	// The Client's Salt
	var Sa = Math.floor((Math.random()*1e+10)+1);
	
	// The Authentication's Hash Field
	var h = hex_sha256(P + Sa);
	/** SEND **/
	var message = "ID=" + ID + "&Sa=" + Sa + "&h=" + h;
	var url = baseUrl + "/Login?" + message;
	ajaxRequest.open("get", url, true);
	log("REQUEST", "LOGIN");
	ajaxRequest.send(null);
}

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
