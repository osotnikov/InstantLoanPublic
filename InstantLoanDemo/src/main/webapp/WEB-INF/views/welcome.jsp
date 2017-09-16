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
            
            $(document.forms['interestCalculatorForm']).submit(function(event){
                /*var formData = {
                	typeOfInterest: this.typeOfInterest,
                    amount: this.amount.value,
                    interest: this.interest.value,
                    intervals: this.intervals.value
                };*/ 
                
                var formData = $(this).serialize();
                var amount = this.amount.value;
                var interest = this.interest.value;
                var intervals = this.intervals.value;
                
                var destinationUrl = this.action; // The action attribute of the loanApplicationForm form.
                 
                $.ajax({
                    url: "<%=request.getContextPath() %>/rest/tools/interest_calculator",
                    type: "GET",
                    data: formData,
                    cache: false,
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    dataType: "text",
                     
                    success: function (data){
                    	
                    	console.log('success');
                        $('.interestCalculator .action-report').text('The total interest amount for a loan of: ' + 
                          amount + ' of ' + intervals + ' intervals, is: ' + data);
                        
                    },
                     
                    error: function (jqXHR, textStatus, errorThrown){
                    	var json = $.parseJSON(jqXHR.responseText);
                    		
                    	$('.interestCalculator .action-report').text('Rejection reason: ' + 
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
    
     <div class="interestCalculator">
     
      <form id="interestCalculatorForm" name="interestCalculatorForm" 
      	action="javascript:void(0)" method="GET">
      	
        <fieldset>
          <legend>Interest Calculator</legend>
              
          <div>
            <label for="COMPOUND">Compound</label> 
            <input type="radio" name="typeOfInterest" id="COMPOUND" value="COMPOUND" checked/>
            <label for="SIMPLE">Simple</label> 
            <input type="radio" name="typeOfInterest" id="SIMPLE" value="SIMPLE"/>
          </div>
          <div>
            <label for="amount">Amount</label> 
            <input type="text" id="amount" name="amount"/>
          </div>
          <div>
            <label for="interest">Interest</label> 
            <input type="text" id="interest" name="interest"/>
          </div>
              
          <div>
            <label for="intervals">Intervals</label> 
            <input type="text" id="intervals" name="intervals"/>
          </div>
              
          <div class="buttonRow">
            <input type="submit" value="Calculate" />
          </div>
            
        </fieldset>
      </form> 
      
      <div class="action-report"> </div>

    </div>
    
  </body>
    
</html>