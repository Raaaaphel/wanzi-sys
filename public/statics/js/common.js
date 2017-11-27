'use strict';
//动态设置html字体大小
var clientWidth = 0;
if(document.documentElement.clientWidth >= 600){
	clientWidth = 600;
}else{
	clientWidth = document.documentElement.clientWidth;
}
document.documentElement.style.fontSize = 50 * clientWidth/375 + 'px';
	
window.onresize = function(){
	if(document.documentElement.clientWidth >= 600){
		clientWidth = 600;
	}else{
		clientWidth = document.documentElement.clientWidth;
	}
	document.documentElement.style.fontSize = 50 * clientWidth/375 + 'px';
}

function leoLoadShow() {
	var thisloadObj = document.getElementById("leo_load_icon");
	if(thisloadObj.style.display=="none"){
		thisloadObj.style.display="block";
	}
}

function leoLoadClose() {
	var thisloadObj = document.getElementById("leo_load_icon");
	if(thisloadObj.style.display=="block"){
		thisloadObj.style.display="none";
	}
}
mui.ready(function(){
	//判断是否是微信浏览器的函数
	isWeiXin();
	function isWeiXin(){
	  	var ua = window.navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			if(mui('.title_header')[0]){
				mui('.title_header')[0].classList.add('title_weixin');
			}
		}else{
			if(mui('.title_header')[0]){
				mui('.title_header')[0].classList.remove('title_weixin');
			}
		}
	}
	
	//mui初始化
	mui.init({});
	//后退
	if(mui('.back')[0]){
		mui('.back')[0].addEventListener('tap',function(){
			history.back(-1);
		},false)
	}
	if($('a').length > 0){
		$('a').click(function(){
			window.location.href = this.href;
		});
	}
})
