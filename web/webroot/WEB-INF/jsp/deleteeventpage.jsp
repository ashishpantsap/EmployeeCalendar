<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<spring:url value="/resources/css/bootstrap-datetimepicker.min.css" var="datetimepickercss" />
<spring:url value="/resources/js/moment.min.js" var="moment" />
<spring:url value="/resources/js/bootstrap-datetimepicker.js" var="datetimepicker" />

<link rel="stylesheet" href="${datetimepickercss}" type="text/css">
<script src="${moment}"></script>
<script src="${datetimepicker}"></script>
	
<title>Delete event</title>
</head>
<body>
<c:import url="header.jsp" />
<div id="alertmessage" role="alert">
  <strong id="strongalert"></strong>
  <p id="contentalert"></p>
</div>
<div class="container-fluid">
	<div class="jumbotron">
		<form id="deleteEvents" action="deleteevents" method="post" style="position: relative;">
			<label for="inumber">Select Employee</label>
			<select name="pk" id="inumber"	class="form-control">
				<c:forEach var="employee" items="${employees}">
					<option value="${employee.PK}">${employee.name} &nbsp;
						${employee.surname}</option>
				</c:forEach>
			</select>
			<br />
			<br />
			<label for="dateid">Date</label> 
			<input id="dateid" type="text" name="date" class="form-control" required="required" />
			<br />
			<button id="submitbutton" type="button" class="btn btn-default btn-info">Submit</button>
		</form>
	</div>
	<div class="alert alert-warning alert-dismissible" role="alert">
  		<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
 		 <strong>Warning!</strong> This form allows to you to delete events on the calendar, please use carefully!! 
	</div>
</div>
<script type="text/javascript">
		$(document).ready(function () {
	        $("#dateid").datetimepicker({
	        	format : 'YYYY-MM-DD'	
	        });
	    });
		
		function createAlert(data){
			$("#alertmessage").removeClass();
			$("#alertmessage").addClass("alert alert-" + data.alert.toLowerCase() +" alert-dismissible");
			$("#strongalert").text(data.alert);
			$("#contentalert").text(data.description);
		}
		
		$(document).ready(function () {
			$("#submitbutton").click(function() {
	            var url = $('#deleteEvents').attr('action');
				var request = $.ajax({
					url:url,
					type:'POST',
					headers:{
	                    'Accept':'application/json'
	                },
	                data: $('#deleteEvents').serializeArray()
				});
				request.done(function(data){
					createAlert(data);
				});
				request.fail(function(data){
					alert(data);
				});
			});	
	    });
	</script>
</body>
</html>