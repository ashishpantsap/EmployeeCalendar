(function($) {
			"use strict";			
			
			
			function createAlert(data){
				$("#alertmessage").removeClass();
				$("#alertmessage").addClass("alert alert-" + data.alert.toLowerCase() +" alert-dismissible");
				$("#strongalert").text(data.alert);
				$("#contentalert").text(data.description);
				$("#alertmessage").fadeOut(3000, function() { 
					$(this).remove(); 
					location.reload();
				});
			}
			
			$('#displayModel').on('click','.delete',function(e){
				
				e.preventDefault();	
				var data=e.target.tempData;
				console.log(data);
				$.ajax({
					url:'deleteevent',
					type:'POST',					
	                data: {'pk':data.pk }
				}).done(function(data){
					$('#displayModel').modal('hide');
					createAlert(data);			
				}).fail(function(err){
					console.log('ERROR',err);
				});				
			});						
			
			$("#submitbutton").click(function(e) {
				$( "#displaymsg" ).empty();
				$("#displaymsg").removeClass();	
				var request = $.ajax({
					url:'sendevents',
					type:'POST',
					headers:{
	                    'Accept':'application/json'
	                },
	                data: $('#sendeventform').serializeArray()
				});
				request.done(function(data){
					$("#displaymsg").removeClass();
					$("#displaymsg").addClass("alert alert-" + data.alert.toLowerCase() +" alert-dismissible");
					$("#displaymsg").text(data.description);
					console.log(data)
				});
				request.fail(function(data){
					alert("Request Failed");
				});
				e.preventDefault();
			});	
			
			$('#myModal').on('hidden.bs.modal', function (e) {
				  location.reload();
				})
		}(jQuery));
	
	window.onload = function(){	
		var $loading = $('#loadingDiv').hide();
			$(document).ajaxStart(function () {
// 			    $loading.show(); UNCOMMENT TO ADD LOADING MESSAGE (Can change to an icon, spinner etc..)
			  })
			  .ajaxStop(function () {
// 			    $loading.hide();
			  });
		var input = $("#sandbox-container");
		input.datepicker({					
	    	format: 'yyyy-mm-dd',
			todayHighlight:true,
			todayBtn:true,
			multidate : true,
			immediateUpdates : true	
	    });
		input.on("changeDate", function(e) {
			console.log(e);
			$('#dates').val();
			$('#dates').val(
		        ($('#sandbox-container').datepicker('getFormattedDate')).split(',')
		    );
		}).datepicker();
		input.on("changeMonth", function(e) {
// 				var date=$(this).datepicker('getFormattedDate').split('-');
// 				ajaxRequest(date[0],date);
			}).datepicker();
		$('#myModal').on('show.bs.modal',function(event) {
			var date=event.relatedTarget.dataset.addevent.split('-');
			var modal = $(this);
			var hidedesc=$('#hidedescription');
			var oootype=$('#oootype');
			var typeevents=$('#typeevents');
			var inum=$('#inumber');
			var year=date[0];
			var month=parseInt(date[1])-1;
			var day=date[2];
			$( "#displaymsg" ).empty();
			$("#displaymsg").removeClass();
			$('.currentDate').text(date.join('-'));				
			$("#sandbox-container").datepicker('setDate',new Date(year,month,day));
			inum.find('option').remove();
			typeevents.find('option').remove();
			hidedesc.hide();
			oootype.hide();
			$.when($.ajax({
				url : 'sapemployees',
				method : 'GET',
				error : function(err) {
					console.log('MyError',err);
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
					console.log('BLAH ERROR',err);
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
				console.log($('#typeevents :selected').text());
				var training = $('#typeevents :selected').text() === 'OTHERS';
				if (training) {
					hidedesc.show();
					$('#description').focus();
				} else {
					hidedesc.hide();
				}
			});	
			
			typeevents.change(function() {
				var ooo = $('#typeevents :selected').text() === 'OUT_OF_THE_OFFICE';
				if (ooo) {
					oootype.show();
				} else {
					oootype.hide();
				}
			});	
			ajaxRequest(year, date);
			
			/*
			$('.table-condensed tbody').children().hover(function(e) {
				$(this).children().hover(function(e){
					console.log($(this).text(),$(this).attr('class'));
				});
			},
			function() {
				
			});
			*/						
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
				var target, list, table, tbody, row, cell, text, text2, cell2, cell3, button;
				list = _.sortBy(data, function(o) { return o.employee.name; });
				table=$('.modaltable tdhover');
				tbody=document.createElement('tbody');
				target=$('#listevents');
				table.append(tbody);
				console.log(date);
				createTable(target, date, list, table, tbody, row, cell, cell2, cell3, button);
			});
			request.error(function(err){
				console.log('error',err);
			});
		});
		
}