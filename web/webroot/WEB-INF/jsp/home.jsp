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
<!-- 					<div class="modal-header"> -->
<!-- 						<button type="button" class="close" data-dismiss="modal" -->
<!-- 							aria-label="Close"> -->
<!-- 							<span aria-hidden="true">&times;</span> -->
<!-- 						</button> -->
<!-- 						<h4 class="modal-title" id="myModalLabel"></h4> -->
<!-- 					</div> -->
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
							<div id="sandbox-container"></div>
							<br />
							<label for="fromdate">From Date</label> 
							<input id="fromdate" type="date" name="fromDate" class="form-control" required="required" /> <br />
							<label for="todate">To Date</label> 
							<input id="todate" type="date" name="toDate" class="form-control" required="required" /> <br />
							<br />
							<div id="hidedescription">
								<label for="training-time">Training</label>
								<select class="form-control"  id="training-time" name="training-time">
									<c:forEach var="training" items="${trainings}">
										<option value="${training}"> ${training}</option>
									</c:forEach>
								</select>
								<br />
								<label for="description">Description</label> 
								<input id="description" type="text" name="description" class="form-control" /> <br />
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
<!-- 					<div class="modal-header"> -->
<!-- 						<button type="button" class="close" data-dismiss="modal" aria-label="Close"> -->
<!-- 							<span aria-hidden="true">&times;</span> -->
<!-- 						</button> -->
<!-- 					</div> -->
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
<!-- 						<button type="button" class="btn btn-primary">Save changes</button> -->
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
				
				var input = $("#sandbox-container");
			    input.datepicker({
			    	format: 'yyyy-mm-dd',
				    multidate : true
			    });
			    /*
			    input.data('datepicker').hide = function () {};
			    input.datepicker('show');
				*/
				
				var modal = $(this);
				var hidedesc=$('#hidedescription');
				var typeevents=$('#typeevents');
				var inum=$('#inumber');
				
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
								
				typeevents.change(function() {
					var training = $('#typeevents :selected')
							.text() === 'TRAINING';
					if (training) {
						hidedesc.show();
						var description = $('#description :selected');
					} else {
						hidedesc.hide();
					}
				});
				
				$("#fromdate").val($(event.relatedTarget).data("addevent"));				
				$("#todate").val($(event.relatedTarget).data("addevent"));				
			});			

			$('#displayModel').on('show.bs.modal', function(event) {
				var eventtable=$('#listevents'), date=event.relatedTarget.dataset.addevent, row, cell, cell2, cell3, text, text2, button;
				eventtable.find('tr').remove();

				$('.currentDate').text(date);
				
				var request = $.ajax({
					url:'daysevents',
					type:'GET',
					headers:{
	                    'Accept':'application/json'
	                },
	                data: {'date':new Date(date)}
				});
				request.done(function(data){
					_.each(data, function(key, value){
						row=document.createElement('tr');
						cell=document.createElement('td');
						text=document.createTextNode(key.employee.name+' '+key.employee.surname+':');	
						cell.appendChild(text);						
						cell2=document.createElement('td');
						text2=document.createTextNode(key.type);
						cell2.appendChild(text2);
						cell3=document.createElement('td');
						button=document.createElement('button');
						button.className='delete btn-danger glyphicon glyphicon-remove';	
						button.tempData={'date':date,'name':key.employee.name,'surname':key.employee.surname,'event':key.type};    //Appending date to handle for delete click event
						cell3.appendChild(button);
						cell3.style.cssFloat='right';						
						row.appendChild(cell);
						row.appendChild(cell2);
						row.appendChild(cell3);
						$('#listevents').append(row);
					});				
				});
				request.error(function(err){
					console.log(err);
				});
			});
				
				
			$('#displayModel').on('click','.delete',function(e){
				e.preventDefault();	
				var data=e.target.tempData;
								
				console.log(data.name,data.event,data.date);
				$.ajax({
					url:'deleteevent',
					type:'POST',					
	                data: {'name':data.name + ',' + data.surname, 'event':data.event, 'date':new Date(data.date)}
				}).done(function(data){
					$('#myModal').modal('hide');
					window.location='/employeecalendar/home';
					console.log(data);
				}).fail(function(err){
					console.log(err);
				});				
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