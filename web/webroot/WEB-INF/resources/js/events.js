(function($) {
	"use strict";

	var eventSaved = "${eventSaved}";
	console.log(eventSaved);
	if (eventSaved) {
		var successDiv = document.getElementById('successmsg');
		console.log(successDiv);
		var text = document.createTextNode('\u00A0 Event Saved Successfully!!');
		successDiv.appendChild(text);
		var r = 0;
		var g = 255;
		var b = 0;
		successDiv.style.color = 'rgb(' + r + ', ' + g + ', ' + b + ')';
		setTimeout(function() {
			remove(successDiv, r, g, b);
		}, 1000);
	}

//	var options = {
//		events_source : "feedCalendar",
//		view : 'month',
//		tmpl_path : "${tmpls}",
//		tmpl_cache : false,
//		onAfterViewLoad : function(view) {
//			$('#MonthYear').text(this.getTitle());
//			$('.btn-group button').removeClass('active');
//			$('button[data-calendar-view="' + view + '"]').addClass('active');
//		},
//		classes : {
//			months : {
//				general : 'label'
//			}
//		}
//	};

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

	$('#myModal').on('show.bs.modal', function(event) {

		var modal = $(this);
		var hidedesc = $('#hidedescription');
		var typeevents = $('#typeevents');
		var inum = $('#inumber');

		inum.find('option').remove();
		typeevents.find('option').remove();
		hidedesc.hide();

		$.when($.ajax({
			url : 'sapemployees',
			method : 'GET',
			error : function(err) {
				console.log('MyError', err);
			}
		})).then(function(data) {
			var names = _.sortBy(data, 'name'), selectedInum;
			_.each(names, function(key, value) {
				inum.append($('<option>', {
					text : key.name + ' ' + key.surname,
					value : key.pk
				}));
				selectedInum = $('#inumber :selected')[0].value;
			});
		});

		$.when($.ajax({
			url : 'sapevents',
			method : 'GET',
			error : function(err) {
				console.log('BLAH ERROR', err);
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
			var training = $('#typeevents :selected').text() === 'TRAINING';
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

	$('#displayModel')
			.on(
					'show.bs.modal',
					function(event) {
						var eventtable = $('#listevents'), date = event.relatedTarget.dataset.addevent, row, cell, cell2, cell3, text, text2, button;
						eventtable.find('tr').remove();

						$('.currentDate').text(date);

						var request = $.ajax({
							url : 'daysevents',
							type : 'GET',
							headers : {
								'Accept' : 'application/json'
							},
							data : {
								'date' : new Date(date)
							}
						});

						request
								.done(function(data) {
									var target, list, table, tbody, row, cell, text, text2, cell2, cell3, button;
									list = _.sortBy(data, function(o) {
										return o.employee.name;
									});
									table = $('.modaltable tdhover');
									tbody = document.createElement('tbody');
									target = $('#listevents');
									table.append(tbody);
									createTable(target, date, list, table,
											tbody, row, cell, cell2, cell3,
											button);
								});
						request.error(function(err) {
							console.log('error', err);
						});
					});

	$('#displayModel').on('click', '.delete', function(e) {
		e.preventDefault();
		var data = e.target.tempData;
		$.ajax({
			url : 'deleteevent',
			type : 'POST',
			data : {
				'name' : data.name + ',' + data.surname,
				'event' : data.event,
				'date' : new Date(data.date)
			}
		}).done(function(data) {
			$('#myModal').modal('hide');
			window.location = '/employeecalendar/home';
		}).fail(function(err) {
			console.log('ERROR', err);
		});
	});

	$("#submitbutton").click(function(e) {
		var request = $.ajax({
			url : 'sendevent',
			type : 'POST',
			headers : {
				'Accept' : 'application/json'
			},
			data : $('#sendeventform').serializeArray()
		});
		request.done(function(data) {
			if (data.alert === 'SUCCESS') {
				$('#myModal').modal('hide');
				window.location = '/employeecalendar/home?eventSaved=Success';
			} else if (data.alert === 'DANGER') {
				var r = 255;
				var g = 0;
				var b = 0;
				var errorDiv = document.getElementById('error');
				errorDiv.style.color = 'rgb(' + r + ', ' + g + ', ' + b + ')';
				$('#error').text('Error check todays scheduled events.');
				setTimeout(function() {
					remove(errorDiv, r, g, b);
				}, 1000);

			}
		});
		request.fail(function(data) {
			alert("Request Failed");
		});
		e.preventDefault();
	});
}(jQuery));