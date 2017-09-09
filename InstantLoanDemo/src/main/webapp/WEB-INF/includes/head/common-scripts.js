$(function(){
	"use strict";
	
	$('#bgColorSelForm #bgColorSelBut').click(function(event){

	    var chosenBgColor = $('#bgColorSelForm input[name="chosen_bg_color"]').attr('value');
	    console.log('bgColorSelForm: chosen_bg_color:'+chosenBgColor);
  
	    $.ajax({
	        url: '<%=request.getContextPath() %>/actions/ChooseCommonBackgroundColorServlet',
	        type: "POST",
	        data: {"chosen_bg_color" : chosenBgColor},
	        cache: false,
	        dataType: "json",
	         
	        success: function (data, textStatus, jqXHR){

	            if (data.status == "SUCCESS" ){
	               
	               $('#bgColorSelForm .action-report').text(
	            	 'Successfully changed background color on all pages! The page is being reloaded...');
	               window.location.href = window.location.href;
	            }else{
	            	$('#bgColorSelForm .action-report').text('Failed to change the background color!');
	            }
	        },
	         
	        error: function (jqXHR, textStatus, errorThrown){
	            alert("error - HTTP STATUS: "+jqXHR.status);
	             
	        }
 
	    });
	     
	    return false;
	});
});