<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reports</title>
</head>
<body>
<c:import url="header.jsp" />
<div id="alertmessage" role="alert">
  <strong id="strongalert"></strong>
  <p id="contentalert"></p>
</div>
<div class="container-fluid">
	<div class="jumbotron">
		<form id="submitRequestReport" action="getresults" method="post" style="position: relative;">
			<label for="inumber">Select Employee</label>
			<select name="pk" id="inumber"	class="form-control">
				<c:forEach var="employee" items="${employees}">
					<option value="${employee.PK}">${employee.name} &nbsp;
						${employee.surname}</option>
				</c:forEach>
			</select>
			<br />
			<label for="typeevents">Event</label> 
			<select class="form-control" id="typeevents" name="typeevent">
				<c:forEach var="event" items="${eventsType}">
					<option value="${event}">${event}</option>
				</c:forEach>
			</select>
			<br />
			<label for="monthsid">Year - Months</label> 
			<input id="monthsid" type="text" name="date" class="form-control" required="required" />
			<br />
			<button id="submitbutton" type="button" class="btn btn-default btn-info">Submit</button>
		</form>
	</div>
	<hr />
	<table class="table table-striped">
		<thead>
			<th>#</th>
			<th>From Date</th>
			<th>To Date</th>
			<th>Name</th>
			<th>Surname</th>
			<th>EventType</th>
			<th>Event Sub-Type</th>
		</thead>
		<tbody id="tablebody">
			
		</tbody>		
	</table>
	<script type="text/javascript">
		$(document).ready(function () {
	        $("#monthsid").datetimepicker({
	        	format : 'YYYY-MM'	
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
	
		function createRow(index, val, html){
			//date = new Date(val.fromDate);
			html='<tr> ' +
				    '<th scope="row">'+ index + '</th>'+
				    '<td>'+ moment.utc(val.fromDate).format("DD-MM-YYYY") +'</td>' +
				    '<td>'+ moment.utc(val.toDate).format("DD-MM-YYYY") +'</td>' +
				    '<td>'+ val.employee.name + '</td>' +
				    '<td>'+ val. employee.surname + '</td>' +
				    '<td>'+ val.type + '</td>' +
				    '<td>'+ val.oooType + ' (' + val.description + ') ' + '</td>' +
				'</tr>';
			return html;
		};
		
		function createBodyTable(data){
			var html="";
			$.each(data, function(index, val){
				html+=createRow(index+1,val,html);
			});
			
			$("#tablebody").html(html);
		};
	
		$(document).ready(function () {
			$("#submitbutton").click(function() {
	            var url = $('#submitRequestReport').attr('action');
				var request = $.ajax({
					url:url,
					type:'POST',
					headers:{
	                    'Accept':'application/json'
	                },
	                data: $('#submitRequestReport').serializeArray()
				});
				request.done(function(data){
					createBodyTable(data);
				});
				request.fail(function(data){
					alert(data);
				});
			});	
	    });
	</script>
</div>
</body>
</html>