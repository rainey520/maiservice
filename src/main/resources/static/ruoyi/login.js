
$(function() {
    validateRule();
	$('.imgcode').click(function() {
		var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
		$(".imgcode").attr("src", url);
	});
});

$.validator.setDefaults({
    submitHandler: function() {
		login();
    }
});

function login() {
	$.modal.loading($("#btnSubmit").data("loading"));
	var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    var rememberMe = $("input[name='rememberme']").is(':checked');
    var data ={"username": username,
        "password": password}
    $.ajax({
        type: "post",
        url: ctx + "login",
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(r) {
            console.log(r)
            if (r.code == 0) {
                // if(r.sign == 1){
                //     document.cookie="token="+r.token;
                //     window.location.href=r.path
                // }else{
                //     window.location.href=r.path+"?token="+r.token;
                // }
                window.location.href=r.path+"?token="+r.token;
            } else {
            	$.modal.closeLoading();
            	$('.imgcode').click();
            	$(".code").val("");
            	$.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}
