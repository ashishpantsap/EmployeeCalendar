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
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="${moment}"></script>

<script src="/employeecalendar/resources/js/collapse.js"></script>
<script src="/employeecalendar/resources/js/transition.js"></script>


<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script src="${datetimepicker}"></script>
</head>
<body>
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
				<div class="container-fluid" style="position: relative;">
					<h3 class="text-center">Single Event</h3> <br /> 
					<form action="sendevent" method="post" id="sendeventform">
						<label for="inumber">Select Inumber</label> <select name="inumber" id="inumber"
							class="form-control">
							<c:forEach var="employee" items="${employees}">
								<option value="${employee.PK}">${employee.inumber}
									&nbsp;&nbsp;-- &nbsp;&nbsp; ${employee.name} &nbsp;
									${employee.surname}</option>
							</c:forEach>
						</select> 
						<br /> 
						<label for="singledate">Date</label> 
						<input id="singledate" type="text"
							name="date" class="form-control" required="required"
							pattern="(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}"
							placeholder="dd-MM-yyyy" /> <br /> 
						<label for="typeevents">Event</label> 
						<select class="form-control" id="typeevents" name="typeevent">
							<c:forEach var="event" items="${events}">
								<option value="${event}">${event}</option>
							</c:forEach>
						</select> <br />
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
		$(document).ready(function () {
	        $("#singledate").datetimepicker({
	        	format : 'YYYY-MM-DD'	
	        });
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
				});
				request.fail(function(data){
					alert("fail");
				});
			});	
	    });
	</script>
</body>
</html>