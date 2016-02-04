<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
<title>Home</title>
</head>
<body>
<body>
	<c:import url="header.jsp" />
	<div class="container-fluid">
		<h3 class="text-center" id="MonthYear"></h3>
		<div class="pull-right form-inline">
			<div class="btn-group">
				<button class="btn btn-primary" data-calendar-nav="prev"><<
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
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel"></h4>
					</div>
					<div class="modal-body">
						<form  id="sendeventform" style="position: relative;">
							<label for="inumber">Select Employee</label> 
							<select id="inumber" class="form-control"></select> 
							<input id="inumberpk" type="hidden" name="pk" value=""/> 
							<br /> 
							<label for="typeevents">Event</label> 
							<select class="form-control"  id="typeevents" name="typeevent"></select> 
							<br /> 
							<label for="fromdate">From Date</label> 
							<input id="fromdate" type="date" name="fromDate" class="form-control" required="required" /> <br />
							<div class="row">
								<div class="col-md-4">
									<label for="fromdate">From Hour</label> 
									<input id="fromhour" type="time" name="fromhour" class="form-control" required="required" /> <br />
								</div>
								<div class="col-md-4">
									<label for="tohour">To Hour</label> 
									<input id="tohour" type="time" name="tohour" class="form-control" required="required" />
								</div>
								<div class="col-md-6"></div>
							</div>
							<br />
							<div id="hidedescription">
								<label for="description">Description</label> 
								<input type="text"	class="form-control" name="description" id="description" /> <br />
							</div>
							<button id="submitbutton" type="submit"	class="btn btn-default btn-info">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="displayModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form action="#" method="post" id="display"	style="position: relative;">
							<label for="listevents">Todays Events</label> 
							<ul id="listevents" name="typeevent">
								<li></li>
								<li></li>
								<li></li>
								<li></li>
							</ul> 
							<br /> 							
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary">Save changes</button>
					</div>
				</div>
			</div>
		</div>

	</div>
	<spring:url value="/resources/js/underscore-min.js" var="underscorejs" />
	<spring:url value="/resources/js/mycalendar.js" var="calendarjs" />
	<spring:url value="/resources/tmpls/" var="tmpls" />
	<script type="text/javascript" src="${underscorejs}"></script>
	<script type="text/javascript" src="${calendarjs}"></script>
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

			$('#myModal').on('show.bs.modal',function(event) {

				var modal = $(this);
				var hidedesc=$('#hidedescription');
				var typeevents=$('#typeevents');
				var inum=$('#inumber');
				var inumpk=$('#inumberpk');
				
				inum.find('option').remove();
				typeevents.find('option').remove();
				hidedesc.hide();

				$.when($.ajax({
					url : 'sapemployees',
					method : 'GET',
					error : function(err) {
						console.log(err);
					}
				})).then(function(data) {
					var names = _.sortBy(data, 'name'),selectedInum;
					_.each(names, function(key, value) {
						inum.append($('<option>', {
							text : key.name + ' ' + key.surname,
							value: key.pk
						}));
						selectedInum=$('#inumber :selected')[0].value;						
						inumpk.val(selectedInum);						
					});
				});
				
				$.when($.ajax({
					url : 'sapevents',
					method : 'GET',
					error : function(err) {
						console.log(err);
					}
				})).then(function(data) {
					var _events = _.sortBy(data);
					_.each(_events, function(key, value) {
						typeevents.append($('<option>', {
							text : key
						}));
					});
				});				
				
				inum.change(function() {
					var inum=$('#inumber :selected')[0].value;
				});
				
				typeevents.change(function() {
					var training = $('#typeevents :selected')
							.text() === 'TRAINING';
					if (training) {
						hidedesc.show();
					} else {
						hidedesc.hide();
					}
				});
				
				$("#fromdate").val($(event.relatedTarget).data("addevent"));				
			});			

			$('#displayModel').on('show.bs.modal', function(event) {
				console.log('########');
			});
			
			$("#submitbutton").click(function(e) {
				console.log($('myModel'));
				var request = $.ajax({
					url:'sendevent',
					type:'POST',
					headers:{
	                    'Accept':'application/json'
	                },
	                data: $('#sendeventform').serializeArray()
				});
				request.done(function(data){
					console.log(data);
					console.log(data.alert);
					if(data.alert==='SUCCESS')
					{
						$('#myModal').modal('hide');
						window.location='/employeecalendar/home';
						
					};
				});
				request.fail(function(data){
					alert("Request Failed");
				});
				e.preventDefault();
			});	
		}(jQuery));
		
		
	</script>
</body>
</body>
</html>