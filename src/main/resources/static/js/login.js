layui.use([ 'layer', 'form'],function() {
	var form = layui.form;
	var layer = layui.layer;
	form.on('submit(login)', function(data) {
		var formdata = data;
		var index;
		$.ajax({
		    type: "post",
		    data: data.field,
		    url: "login",
		    beforeSend: function () {
		    	index = layer.load(1);
		    },
		    success: function (data) {
		    	if("0000" == data.code){
			    	window.location.href="index";
			    }else{
			    	$('#yzm').attr("src",'captcha?tm='+Math.random());
			    	layer.msg(data.msg,{icon:2})
			    }
		    },
		    error: function (data) {
		    	layer.msg("登录异常",{icon:2});
		    },
		    complete:function(XMLHttpRequest, textStatus){
		    	layer.close(index);
		    }
		});
		return false;
	});
	
	$('#yzm').click(function(){
		this.src='captcha?tm='+Math.random();
	})
})