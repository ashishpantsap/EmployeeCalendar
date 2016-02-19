<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<spring:url value="/resources/css/home.css" var="homecss" />
<spring:url value="/resources/css/mycalendar.css" var="calendarcss" />
<link rel="stylesheet" href="${homecss}" type="text/css">
<link rel="stylesheet" href="${calendarcss}" type="text/css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	
<spring:url value="/resources/css/bootstrap-datepicker.min.css" var="datepickercss" />
<spring:url value="/resources/js/bootstrap-datepicker.min.js" var="datepickerjs" />
<link rel="stylesheet" href="${datepickercss}" type="text/css">
<script src="${datepickerjs}"></script>
<title>Home</title>
</head>
<body>
<body>
	<c:import url="header.jsp" />
	<div id="successmsg"></div>
	<div id="alertmessage" role="alert">
	  <strong id="strongalert"></strong>
	  <p id="contentalert"></p>
	</div>
	<div class="container-fluid">
		<h3 class="text-center" id="MonthYear"></h3>
		<div class="pull-right form-inline">
			<div class="btn-group">
				<button class="btn btn-primary" data-calendar-nav="prev">
					Prev</button>
				<button class="btn" data-calendar-nav="today">Today</button>
				<button class="btn btn-primary" data-calendar-nav="next">Next
					>></button>
			</div>
			<div class="btn-group">
				<button class="btn btn-warning" data-calendar-view="week">Week</button>
				<button class="btn btn-warning active" data-calendar-view="month">Month</button>
			</div>
		</div>
		<br /> <br /> <br />
		<div id="calendar"></div>
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-body">
						<h3 class="currentDate"></h3>
						<hr>
						<form id="sendeventform" style="position: relative;">
							<label for="inumber">Select Employee</label> 
							<select id="inumber" class="form-control" name="pk"></select> 
							<br /> 
							<label for="typeevents">Event</label> 
							<select class="form-control"  id="typeevents" name="typeevent"></select> 
							<br /> 
							<div id="hidedescription">
								<label for="training-time">Time</label>
								<select class="form-control"  id="training-time" name="training-time">
									<c:forEach var="training" items="${trainings}">
										<option value="${training}"> ${training}</option>
									</c:forEach>
								</select>
								<br />
								<label for="description">Description</label> 
								<input id="description" type="text" name="description" class="form-control" /> <br />
							</div>
							<div id="oootype">
								<label for="ooo-time">OOO time</label>
								<select class="form-control"  id="ooo-time" name="ooo-type">
									<c:forEach var="oootype" items="${oootypes}">
										<option value="${oootype}"> ${oootype}</option>
									</c:forEach>
								</select>
								<br />
							</div>
							<label for="sanboxContainer">Choose Dates</label> 
							<div class=sandbox id="sandbox-container"></div>
							<br />
							<input id="dates" type="hidden" name="dates" class="form-control" /> <br />
							
							<div id="displaymsg">
								&nbsp;
							</div>
							<div class="modal-footer">
								<button id="submitbutton" type="submit"	class="btn btn-default btn-info">Submit</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="displayModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-body">
						<h3 class="currentDate"></h3>
						<hr>
						<form action="#" method="post" id="display"	style="position: relative;">
							<label for="listevents">Todays Events</label> 
							<div class=container-fluid>
							<table class="modaltable tdhover" id="listevents"></table> 
							</div>
							<br /> 							
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<spring:url value="/resources/js/underscore-min.js" var="underscorejs" />
	<spring:url value="/resources/js/mycalendar.js" var="calendarjs" />
	<spring:url value="/resources/js/helper.js" var="helperjs"/>
	<spring:url value="/resources/js/employeecalendar.js" var="employeecalendarjs"/>
	<spring:url value="/resources/tmpls/" var="tmpls" />
	<script type="text/javascript" src="${underscorejs}"></script>
	<script type="text/javascript" src="${calendarjs}"></script>
	<script type="text/javascript" src="${helperjs}"></script>
	<script type="text/javascript" src="${employeecalendarjs}"></script>
	<script type="text/javascript">
		(function($) {
			"use strict";			
			var options = {
				events_source : "feedCalendar",
				view : 'month',
				tmpl_path : "${tmpls}",
				tmpl_cache : false,
				onAfterViewLoad : function(view) {
					$('#MonthYear').text(this.getTitle());
					$('.btn-group button').removeClass('active');
					$('button[data-calendar-view="' + view + '"]').addClass(
							'active');
				},
				classes : {
					months : {
						general : 'label'
					}
				}
			};

			var calendar = $('#calendar').calendar(options);

			$('.btn-group button[data-calendar-nav]').each(function() {
				var $this = $(this);
				$this.click(function() {
					calendar.navigate($this.data('calendar-nav'));
				});
			});

			$('.btn-group button[data-calendar-view]').each(function() {
				var $this = $(this);
				$this.click(function() {
					calendar.view($this.data('calendar-view'));
				});
			});
		
		}(jQuery));
		
		
		</script>
</body>
</body>
</html>
