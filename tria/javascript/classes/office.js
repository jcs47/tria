var officeLabels = new Array("Office Code", 
							 "City",
							 "Phone",
							 "Address Line 1",
							 "Address Line 2",
							 "State",
							 "Country",
							 "Postal Code",
							 "Territory",
							 "Action");

function mountOfficeTable(offices) {
	
	var officeDiv = document.getElementById("officeDiv");
	officeDiv.innerHTML = "";
	
	for(var i = 0; i < offices.length; i++) {
		// Add Button
		var p = document.createElement("p");
		p.align = "center";
		var addButton = document.createElement("input");
		addButton.type = "button";
		addButton.value = "Add Office";
		addButton.onclick = function() { mountOfficeFormAdd(); };
		p.appendChild(addButton);
		
		
		officeDiv.appendChild(p);
		
		//Table
		var table = document.createElement("table");
		table.border = 1;
		table.cellpadding = 2;
		table.cellspacing = 2;
		table.width = "90%";
		table.align = "center";
		
		//Head
		var head = document.createElement("tr");
		for(var i = 0; i < officeLabels.length; i++) {
			var th = document.createElement("th");
			th.appendChild(document.createTextNode(officeLabels[i]));
			head.appendChild(th);
		}
		
		table.appendChild(head);
		
		//Body
		for(var i = 0; i < offices.length; i++) {
			var row = document.createElement("tr");

			var td1 = document.createElement("td");
			td1.appendChild(document.createTextNode(offices[i].officeCode));
			row.appendChild(td1);
			
			var td2 = document.createElement("td");
			td2.appendChild(document.createTextNode(offices[i].city));
			row.appendChild(td2);
			
			var td3 = document.createElement("td");
			td3.appendChild(document.createTextNode(offices[i].phone));
			row.appendChild(td3);
			
			var td4 = document.createElement("td");
			td4.appendChild(document.createTextNode(offices[i].addressLine1));
			row.appendChild(td4);
			
			var td5 = document.createElement("td");
			td5.appendChild(document.createTextNode(offices[i].addressLine2));
			row.appendChild(td5);
			
			var td6 = document.createElement("td");
			td6.appendChild(document.createTextNode(offices[i].state));
			row.appendChild(td6);
			
			var td7 = document.createElement("td");
			td7.appendChild(document.createTextNode(offices[i].country));
			row.appendChild(td7);
			
			var td8 = document.createElement("td");
			td8.appendChild(document.createTextNode(offices[i].postalCode));
			row.appendChild(td8);
			
			var td9 = document.createElement("td");
			td9.appendChild(document.createTextNode(offices[i].territory));
			row.appendChild(td9);
			
			var td10 = document.createElement("td");
			var linkEdit = document.createElement("a");
			var imgEdit = document.createElement("img");
			imgEdit.src = "b_edit.png";
			linkEdit.appendChild(imgEdit);
			linkEdit.href = "javascript:GetOffice(" + offices[i].officeCode + ")";
			td10.appendChild(linkEdit);
			var linkDel = document.createElement("a");
			var imgDel = document.createElement("img");
			imgDel.src = "b_drop.png";
			linkDel.appendChild(imgDel);
			linkDel.href = "javascript:DelOffice(" + offices[i].officeCode + ")";
			td10.appendChild(linkDel);
			row.appendChild(td10);
			
			table.appendChild(row);
		}
		
		
		officeDiv.appendChild(table);
		document.getElementById("home").style.display = "none";
		
		officeDiv.style.display = "inline";
		
	}
}

function mountOfficeForm(office) {
	
	//Table
	var table = document.createElement("table");
	table.border = 0;
	table.cellpadding = 0;
	table.cellspacing = 0;
	table.width = "40%";
	table.align = "left";
	
	//Body
	var row1 = document.createElement("tr");
	var td11 = document.createElement("td");
	td11.appendChild(document.createTextNode("Office Code: "));
	row1.appendChild(td11);
	var td12 = document.createElement("td");
	var officeCodeInput = document.createElement("input");
	officeCodeInput.type = "text";
	officeCodeInput.id = "officeCode";
	officeCodeInput.value = office.officeCode;
	officeCodeInput.disabled = "true";
	td12.appendChild(officeCodeInput);
	row1.appendChild(td12);
	table.appendChild(row1);
	
	var row2 = document.createElement("tr");
	var td21 = document.createElement("td");
	td21.appendChild(document.createTextNode("City: "));
	row2.appendChild(td21);
	var td22 = document.createElement("td");
	var cityInput = document.createElement("input");
	cityInput.type = "text";
	cityInput.id = "city";
	cityInput.value = office.city;
	td22.appendChild(cityInput);
	row2.appendChild(td22);
	table.appendChild(row2);
	
	var row3 = document.createElement("tr");
	var td31 = document.createElement("td");
	td31.appendChild(document.createTextNode("Phone: "));
	row3.appendChild(td31);
	var td32 = document.createElement("td");
	var phoneInput = document.createElement("input");
	phoneInput.type = "text";
	phoneInput.id = "phone";
	phoneInput.value = office.phone;
	td32.appendChild(phoneInput);
	row3.appendChild(td32);
	table.appendChild(row3);
	
	var row4 = document.createElement("tr");
	var td41 = document.createElement("td");
	td41.appendChild(document.createTextNode("Address Line 1: "));
	row4.appendChild(td41);
	var td42 = document.createElement("td");
	var addressLine1Input = document.createElement("input");
	addressLine1Input.type = "text";
	addressLine1Input.id = "addressLine1";
	addressLine1Input.value = office.addressLine1;
	td42.appendChild(addressLine1Input);
	row4.appendChild(td42);
	table.appendChild(row4);
	
	var row5 = document.createElement("tr");
	var td51 = document.createElement("td");
	td51.appendChild(document.createTextNode("Address Line 2: "));
	row5.appendChild(td51);
	var td52 = document.createElement("td");
	var addressLine2Input = document.createElement("input");
	addressLine2Input.type = "text";
	addressLine2Input.id = "addressLine2";
	addressLine2Input.value = office.addressLine2;
	td52.appendChild(addressLine2Input);
	row5.appendChild(td52);
	table.appendChild(row5);
	
	var row6 = document.createElement("tr");
	var td61 = document.createElement("td");
	td61.appendChild(document.createTextNode("State: "));
	row6.appendChild(td61);
	var td62 = document.createElement("td");
	var stateInput = document.createElement("input");
	stateInput.type = "text";
	stateInput.id = "state";
	stateInput.value = office.state;
	td62.appendChild(stateInput);
	row6.appendChild(td62);
	table.appendChild(row6);
	
	var row7 = document.createElement("tr");
	var td71 = document.createElement("td");
	td71.appendChild(document.createTextNode("Country: "));
	row7.appendChild(td71);
	var td72 = document.createElement("td");
	var countryInput = document.createElement("input");
	countryInput.type = "text";
	countryInput.id = "country";
	countryInput.value = office.country;
	td72.appendChild(countryInput);
	row7.appendChild(td72);
	table.appendChild(row7);
	
	var row8 = document.createElement("tr");
	var td81 = document.createElement("td");
	td81.appendChild(document.createTextNode("Postal Code: "));
	row8.appendChild(td81);
	var td82 = document.createElement("td");
	var postalCodeInput = document.createElement("input");
	postalCodeInput.type = "text";
	postalCodeInput.id = "postalCode";
	postalCodeInput.value = office.postalCode;
	td82.appendChild(postalCodeInput);
	row8.appendChild(td82);
	table.appendChild(row8);
	
	var row9 = document.createElement("tr");
	var td91 = document.createElement("td");
	td91.appendChild(document.createTextNode("Territory: "));
	row9.appendChild(td91);
	var td92 = document.createElement("td");
	var territoryInput = document.createElement("input");
	territoryInput.type = "text";
	territoryInput.id = "territory";
	territoryInput.value = office.territory;
	td92.appendChild(territoryInput);
	row9.appendChild(td92);
	table.appendChild(row9);
	
	var row10 = document.createElement("tr");
	var td101 = document.createElement("td");
	td101.align = "center";
	var updateButton = document.createElement("input");
	updateButton.type = "button";
	updateButton.value = "Update";
	updateButton.onclick = function () { SetOffice(); };
	td101.appendChild(updateButton);
	row10.appendChild(td101);
	var td102 = document.createElement("td");
	td102.align = "center";
	var backButton = document.createElement("input");
	backButton.type = "button";
	backButton.value = "Back";
	backButton.onclick = function() { Offices(); };
	td102.appendChild(backButton);
	row10.appendChild(td102);
	table.appendChild(row10);
	
	var officeDiv = document.getElementById("officeDiv");
	officeDiv.innerHTML = "";
	officeDiv.appendChild(table);
	document.getElementById("home").style.display = "none";
	officeDiv.style.display = "inline";
}

function mountOfficeFormAdd() {
	//Table
	var table = document.createElement("table");
	table.border = 0;
	table.cellpadding = 0;
	table.cellspacing = 0;
	table.width = "40%";
	table.align = "left";
	
	//Body
	var row1 = document.createElement("tr");
	var td11 = document.createElement("td");
	td11.appendChild(document.createTextNode("Office Code: "));
	row1.appendChild(td11);
	var td12 = document.createElement("td");
	var officeCodeInput = document.createElement("input");
	officeCodeInput.type = "text";
	officeCodeInput.id = "officeCode";
	td12.appendChild(officeCodeInput);
	row1.appendChild(td12);
	table.appendChild(row1);
	
	var row2 = document.createElement("tr");
	var td21 = document.createElement("td");
	td21.appendChild(document.createTextNode("City: "));
	row2.appendChild(td21);
	var td22 = document.createElement("td");
	var cityInput = document.createElement("input");
	cityInput.type = "text";
	cityInput.id = "city";
	td22.appendChild(cityInput);
	row2.appendChild(td22);
	table.appendChild(row2);
	
	var row3 = document.createElement("tr");
	var td31 = document.createElement("td");
	td31.appendChild(document.createTextNode("Phone: "));
	row3.appendChild(td31);
	var td32 = document.createElement("td");
	var phoneInput = document.createElement("input");
	phoneInput.type = "text";
	phoneInput.id = "phone";
	td32.appendChild(phoneInput);
	row3.appendChild(td32);
	table.appendChild(row3);
	
	var row4 = document.createElement("tr");
	var td41 = document.createElement("td");
	td41.appendChild(document.createTextNode("Address Line 1: "));
	row4.appendChild(td41);
	var td42 = document.createElement("td");
	var addressLine1Input = document.createElement("input");
	addressLine1Input.type = "text";
	addressLine1Input.id = "addressLine1";
	td42.appendChild(addressLine1Input);
	row4.appendChild(td42);
	table.appendChild(row4);
	
	var row5 = document.createElement("tr");
	var td51 = document.createElement("td");
	td51.appendChild(document.createTextNode("Address Line 2: "));
	row5.appendChild(td51);
	var td52 = document.createElement("td");
	var addressLine2Input = document.createElement("input");
	addressLine2Input.type = "text";
	addressLine2Input.id = "addressLine2";
	td52.appendChild(addressLine2Input);
	row5.appendChild(td52);
	table.appendChild(row5);
	
	var row6 = document.createElement("tr");
	var td61 = document.createElement("td");
	td61.appendChild(document.createTextNode("State: "));
	row6.appendChild(td61);
	var td62 = document.createElement("td");
	var stateInput = document.createElement("input");
	stateInput.type = "text";
	stateInput.id = "state";
	td62.appendChild(stateInput);
	row6.appendChild(td62);
	table.appendChild(row6);
	
	var row7 = document.createElement("tr");
	var td71 = document.createElement("td");
	td71.appendChild(document.createTextNode("Country: "));
	row7.appendChild(td71);
	var td72 = document.createElement("td");
	var countryInput = document.createElement("input");
	countryInput.type = "text";
	countryInput.id = "country";
	td72.appendChild(countryInput);
	row7.appendChild(td72);
	table.appendChild(row7);
	
	var row8 = document.createElement("tr");
	var td81 = document.createElement("td");
	td81.appendChild(document.createTextNode("Postal Code: "));
	row8.appendChild(td81);
	var td82 = document.createElement("td");
	var postalCodeInput = document.createElement("input");
	postalCodeInput.type = "text";
	postalCodeInput.id = "postalCode";
	td82.appendChild(postalCodeInput);
	row8.appendChild(td82);
	table.appendChild(row8);
	
	var row9 = document.createElement("tr");
	var td91 = document.createElement("td");
	td91.appendChild(document.createTextNode("Territory: "));
	row9.appendChild(td91);
	var td92 = document.createElement("td");
	var territoryInput = document.createElement("input");
	territoryInput.type = "text";
	territoryInput.id = "territory";
	td92.appendChild(territoryInput);
	row9.appendChild(td92);
	table.appendChild(row9);
	
	var row10 = document.createElement("tr");
	var td101 = document.createElement("td");
	td101.align = "center";
	var updateButton = document.createElement("input");
	updateButton.type = "button";
	updateButton.value = "Insert";
	updateButton.onclick = function () { InsertOffice(); };
	td101.appendChild(updateButton);
	row10.appendChild(td101);
	var td102 = document.createElement("td");
	td102.align = "center";
	var backButton = document.createElement("input");
	backButton.type = "button";
	backButton.value = "Back";
	backButton.onclick = function() { Offices(); };
	td102.appendChild(backButton);
	row10.appendChild(td102);
	table.appendChild(row10);
	
	
	var officeDiv = document.getElementById("officeDiv");
	officeDiv.innerHTML = "";
	officeDiv.appendChild(table);
	document.getElementById("home").style.display = "none";
	
	officeDiv.style.display = "inline";
}

function officesTransform(offices) {
	var t = "";
	for(var i = 0; i < offices.length; i++) {
		t += offices[i].officeCode + 
	    	 offices[i].city + 
	    	 offices[i].phone +
	    	 offices[i].addressLine1 + 
	    	 offices[i].addressLine2 +
	    	 offices[i].state +
	    	 offices[i].country +
	    	 offices[i].postalCode +
	    	 offices[i].territory;
	    			
	}
	return t;
}

function officeTransform(office) {
	var t = "";
	t += office.officeCode + 
	 office.city + 
	 office.phone +
	 office.addressLine1 + 
	 office.addressLine2 +
	 office.state +
	 office.country +
	 office.postalCode +
	 office.territory;
	return t;
}
