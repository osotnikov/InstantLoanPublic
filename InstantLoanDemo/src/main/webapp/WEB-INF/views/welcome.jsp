<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  
<html>
  <head>
  
  	<title>InstantLoan - REST Demo</title>
  
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <%@ include file="/WEB-INF/includes/head/jquery.txt" %>

    <script type="text/javascript">
    
    	<%@ include file = "/WEB-INF/includes/head/common-scripts.js" %>
    
        $(function(){
            "use strict";
             
            $(document.forms['loanApplicationForm']).submit(function(event){
                var data = {
                    fullName: this.fullName.value,
                    term: this.term.value,
                    amount: this.amount.value
                }; 
                
                console.log('loanApplicationForm: data:'+JSON.stringify(data));
                var jsonData = JSON.stringify(data);
                
                var destinationUrl = this.action; // The action attribute of the loanApplicationForm form.
                 
                $.ajax({
                    url: destinationUrl,
                    type: "POST",
                    data: jsonData,
                    cache: false,
                    dataType: "json",
                    contentType: "application/json",
                     
                    success: function (data, textStatus, jqXHR){
                        //alert("success");
                        if (data.status == "ACCEPTED" ){
                           //redirect to secured page
                           //window.location.replace("https://"+window.location.host +
                           //"<%=request.getContextPath() %>/secure/authenticated/ProfileJsp");
                           $('.loanApplication .action-report').text('Your loan application was successfully submitted!');
                        }else{
                        	$('.loanApplication .action-report').text('Your loan application failed! Reason: ' + data.rejectionReason);
                        }
                    },
                     
                    error: function (jqXHR, textStatus, errorThrown){
                    	var json = $.parseJSON(jqXHR.responseText);
                    	
                    	/*$('.loanApplication .action-report').text("error - HTTP STATUS: " + 
                    		jqXHR.status + ", jqXHR.responseJSON: " + jqXHR.responseJSON + 
                    		", jqXHR.responseText: " + jqXHR.responseText + 
                    		", json: " + json);*/
                    		
                    	$('.loanApplication .action-report').text('Rejection reason: ' + 
                    		JSON.parse(jqXHR.responseText).rejectionReason);
                         
                    }
                     
                });
                 
                return false;
            });
             
        });
        
    </script>
     
  </head>
  
  <body>
 
	<h1>InstantLoan - REST Demo</h1>
    
    <%-- LOAN APPLICATION FORM --%>
      
    <div class="loanApplication">
      <form id="loanApplicationForm" name="loanApplicationForm" 
      	action="<%=request.getContextPath() %>/rest/loan_applications" method="POST">
      	
        <fieldset>
          <legend>Registration</legend>
              
          <div>
            <label for="fullName">Full Name</label> 
            <input type="text" id="fullName" name="fullName"/>
          </div>
          <div>
            <label for="term">Term</label> 
            <input type="text" id="term" name="term"/>
          </div>
              
          <div>
            <label for="amount">Amount</label> 
            <input type="text" id="amount" name="amount"/>
          </div>
              
          <div class="buttonRow">
            <input type="submit" value="Apply for Loan" />
          </div>
            
        </fieldset>
      </form> 
      
      <div class="action-report"> </div>

    </div>
    
  </body>
    
</html>