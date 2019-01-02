function formPost(url,jsonObject) {
	var iframe = $("<iframe id='formiframe' onload='load()' style='display:none'></iframe>")[0];
	var form = document.createElement("form");
	form.action = url;
	form.method = "post";
	form.target = "formiframe";
	for(var key in jsonObject){
		var input = document.createElement("input");
		input.name = key;
		input.value = jsonObject[key];
		form.appendChild(input);
	}
	form.style.display = "none";
	iframe.appendChild(form);
	document.body.appendChild(iframe);
	form.submit();
	return form;
}
function load(){
	console.log('load');
}