<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" 	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<spring:url value="/resources/css/home.css" var="homecss" />
<spring:url value="/resources/css/calendar.min.css"	var="calendarcss" />
<link rel="stylesheet" href="${home}" type="text/css">
<link rel="stylesheet" href="${calendarcss}" type="text/css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.min.js"></script>

<title>Home</title>
</head>
<body>
<body>
		<div class="container-fluid">
			<h2>employeecalendar</h2>
			<ul>
				<li><a href="viewcalendarpage">View calendar</a></li>
				<li><a href="submiteventpage">Submit event</a></li>
				<li><a href="deleteeventpage">Delete event</a></li>
			</ul>
		</div>
		<div class="container-fluid">
			<div class="jumbotron">
				<div class="pull-right form-inline">
					<div class="btn-group">
						<button class="btn btn-warning" data-calendar-view="week">Week</button>
						<button class="btn btn-warning active" data-calendar-view="month">Month</button>
					</div>
				</div>
				<br /><br /><br />
				<div id="calendar"></div>
			</div>
		</div>
		
		
		
		<spring:url value="/resources/js/underscore-min.js"	var="underscorejs" />
		<spring:url value="/resources/js/mycalendar.js"	var="calendarjs" />
		<spring:url value="/resources/tmpls/"	var="tmpls" />
		<script type="text/javascript" src="${underscorejs}"></script>
	    <script type="text/javascript" src="${calendarjs}"></script>
	    <script type="text/javascript">
	        (function($) {

	        	"use strict";

	        	var options = {
	        		events_source: function () { return []; },
	        		view: 'month',
	        		tmpl_path: "${tmpls}",
	        		tmpl_cache: false,
	        		/*
	        		onAfterEventsLoad: function(events) {
	        			if(!events) {
	        				return;
	        			}
	        			var list = $('#eventlist');
	        			list.html('');

	        			$.each(events, function(key, val) {
	        				$(document.createElement('li'))
	        					.html('<a href="' + val.url + '">' + val.title + '</a>')
	        					.appendTo(list);
	        			});
	        		},
	        		*/
	        		onAfterViewLoad: function(view) {
	        			//$('.page-header h3').text(this.getTitle());
	        			$('.btn-group button').removeClass('active');
	        			$('button[data-calendar-view="' + view + '"]').addClass('active');
	        		},
	        		classes: {
	        			months: {
	        				general: 'label'
	        			}
	        		}
	        	};
	        	/*
	        	day: '2013-03-12',
        		onAfterEventsLoad: function(events) {
        			if(!events) {
        				return;
        			}
        			var list = $('#eventlist');
        			list.html('');

        			$.each(events, function(key, val) {
        				$(document.createElement('li'))
        					.html('<a href="' + val.url + '">' + val.title + '</a>')
        					.appendTo(list);
        			});
        		},
        		onAfterViewLoad: function(view) {
        			$('.page-header h3').text(this.getTitle());
        			$('.btn-group button').removeClass('active');
        			$('button[data-calendar-view="' + view + '"]').addClass('active');
        		},
        		classes: {
        			months: {
        				general: 'label'
        			}
        		}
	        	*/

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