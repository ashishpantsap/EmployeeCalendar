<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<spring:url value="/resources/css/submiteventpage.css"
	var="submiteventpage" />
<spring:url value="/resources/css/bootstrap-datetimepicker.min.css"
	var="datetimepickercss" />

<spring:url value="/resources/js/moment.min.js" var="moment" />
<spring:url value="/resources/js/bootstrap-datetimepicker.js"
	var="datetimepicker" />

<title>Submit event</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${submiteventpage}" type="text/css">
<link rel="stylesheet" href="${datetimepickercss}" type="text/css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="${moment}"></script>

<script src="/employeecalendar/resources/js/collapse.js"></script>
<script src="/employeecalendar/resources/js/transition.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="${datetimepicker}"></script>
</head>
<body>
	<c:import url="header.jsp" />
	<div id="alertmessage" role="alert">
	  <strong id="strongalert"></strong>
	  <p id="contentalert"></p>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#sendsingleevent">Send Single
				Event</a></li>
		<li><a href="#sendmultipleevents">Send Multiple Event</a></li>
	</ul>
	<div class="tab-content">
		<div id="sendsingleevent" class="tab-pane fade in active">
			<div class="jumbotron">
				<div class="container">
					<h3 class="text-center">Single Event</h3> <br /> 
					<form action="sendevent" method="post" id="sendeventform" style="position: relative;">
						<label for="inumber">Select Employee</label> <select name="pk" id="inumber"
							class="form-control">
							<c:forEach var="employee" items="${employees}">
								<option value="${employee.PK}"> ${employee.name} &nbsp;
									${employee.surname}</option>
							</c:forEach>
						</select> 
						<br /> 
						<label for="typeevents">Event</label> 
						<select class="form-control" id="typeevents" name="typeevent">
							<c:forEach var="event" items="${events}">
								<option value="${event}">${event}</option>
							</c:forEach>
						</select> <br />
						<label for="fromdate">From Date</label> 
						<input id="fromdate" type="text"
							name="fromDate" class="form-control" required="required" /> <br />
						<div class="row">
						  <div class="col-md-2">
						  	<label for="fromdate">From Hour</label> 
								<input id="fromhour" type="text"
									name="fromhour" class="form-control" required="required" /> <br />
						  </div>
						  <div class="col-md-2">
						  	<label for="tohour">To Hour</label> 
							<input id="tohour" type="text" name="tohour" class="form-control" required="required" /> 
						  </div>
						  <div class="col-md-8"></div>
						</div>	 
						<br /> 
						<label for="description">Description</label> 
						<input type="text" class="form-control" name="description" id="description" /> <br />
						<button id="submitbutton" type="button" class="btn btn-default btn-info">Submit</button>
					</form>
				</div>
			</div>
		</div>
		<div id="sendmultipleevents" class="tab-pane fade">
			<%-- 
			<div class="container" style="height: 100%">
				<div class="jumbotron">
					<h3>Multiple Event</h3>
					<form action="sendevent" method="post">
						<label>Select Inumber</label> <select name="inumber"
							class="form-control">
							<c:forEach var="employee" items="${employees}">
								<option value="${employee.PK}">${employee.inumber}
									&nbsp;&nbsp;-- &nbsp;&nbsp; ${employee.name} &nbsp;
									${employee.surname}</option>
							</c:forEach>
						</select> <label>Date</label> <input type="text" name="date"
							class="form-control" required="required"
							pattern="(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}"
							placeholder="dd-MM-yyyy" /> <br />
						<button type="submit" class="btn btn-default btn-info">Submit</button>
					</form>
				</div>
			</div> --%>
		</div>
	</div>
	<script>
		$(document).ready(function(){
		    $(".nav-tabs a").click(function(){
		        $(this).tab('show');
		    });
		});
	</script>
	<script type="text/javascript">
		
		function enableOnlyWeekend(){
			/*
			$("#fromdate").datetimepicker({
	        	format : 'YYYY-MM-DD',
	        	daysOfWeekDisabled : [ "1" , "2", "3", "4", "5"]
	        });
	        */
	        dayOff();
		}
		
		function workingMorning(){
			$("#fromhour").prop('disabled', true);
	        $("#fromhour").val('10:00');
	        $("#tohour").prop('disabled', true);
	        $("#tohour").val('18:00');
		}
		
		function workingAfternoon(){
			$("#fromhour").prop('disabled', true);
	        $("#fromhour").val('12:00');
	        $("#tohour").prop('disabled', true);
	        $("#tohour").val('20:00');
		}
		
		function dayOff(){
			$("#fromhour").prop('disabled', true);
	        $("#fromhour").val('00:00');
	        $("#tohour").prop('disabled', true);
	        $("#tohour").val('00:00');
		}
		
		function queueManager(){
			$("#fromhour").prop('disabled', true);
	        $("#fromhour").val('12:00');
	        $("#tohour").prop('disabled', true);
	        $("#tohour").val('18:00');			
		}
	
		function checkConditions(optionSelected){
			if (optionSelected == "ON_CALL"){
				enableOnlyWeekend();
			}
			else if (optionSelected == "MORNING_SHIFT"){
				workingMorning();
			}
			else if (optionSelected == "AFTERNOON_SHIFT"){
				workingAfternoon();
			}
			else if (optionSelected == "SICK_LEAVE" || optionSelected == "OUT_OF_THE_OFFICE" 
					|| optionSelected == "WORKING_FROM_HOME"){
				dayOff();
			}
			else if (optionSelected == "QUEUE_MANAGER"){
				queueManager();
			}
		}
		
		$(document).ready(function () {
			
			var optionSelected = $("#typeevents").find(":selected").text();
			
			checkConditions(optionSelected);
			
	        $("#fromdate").datetimepicker({
	        	format : 'YYYY-MM-DD'	
	        });
	        $("#fromhour").datetimepicker({
	        	format : 'HH:00'
	        });
	        $("#tohour").datetimepicker({
	        	format : 'HH:00'
	        });
	        	        
	    });
		
		function enableTime(){
			$("#fromhour").prop('disabled', false);
	        $("#tohour").prop('disabled', false);
	        
		}
		
		$("#typeevents").on("change", function(){
			var optionSelected = $("#typeevents").find(":selected").text();
			enableTime();
			checkConditions(optionSelected);
		});
	</script>
	<script type="text/javascript">
		function createAlert(data){
			$("#alertmessage").removeClass();
			$("#alertmessage").addClass("alert alert-" + data.alert.toLowerCase() +" alert-dismissible");
			$("#strongalert").text(data.alert);
			$("#contentalert").text(data.description);
		}
	
	
		$(document).ready(function () {
			$("#submitbutton").click(function() {
	            var url = $('#sendeventform').attr('action');
	            enableTime();
				var request = $.ajax({
					url:url,
					type:'POST',
					headers:{
	                    'Accept':'application/json'
	                },
	                data: $('#sendeventform').serializeArray()
				});
				request.done(function(data){
					createAlert(data);
					optionSelected = $("#typeevents").find(":selected").text();
					checkConditions(optionSelected);
				});
				request.fail(function(data){
					alert("fail");
				});
			});	
	    });
	</script>
</body>
</html>