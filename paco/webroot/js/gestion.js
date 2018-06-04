function prueba(d, c){
	
	var usuario = d;
	var contrasena = c;
	
	var req = new XMLHttpRequest();
	var url = "http://192.168.43.124:8083/api/doorDir/"+usuario;
	req.onreadystatechange = function(){
			var response = JSON.parse(req.responseText);
			var valUS = 0;
			if(contrasena == valUS){
				alert(contrasena);
			}else{
				alert(usuario);
		}
	}
	req.open("GET", url, true);
	req.send();
}

function prueba2(d, c){
	
	var usuario = d;
	var contrasena = c;
	if(contrasena == 0){
		alert(contrasena);
	}else{
		jQuery("#aqui").html(usuario);
	}
	location.href("../index.html");
	return false;
}