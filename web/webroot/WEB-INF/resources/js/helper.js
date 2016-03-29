function remove(element, r, g, b) {
	r = r || 0;
	g = g || 0;
	b = b || 0;
	r != 255 ? r++ : r;
	g != 255 ? g++ : g;
	b != 255 ? b++ : b;
	element.style.color = 'rgb(' + r + ',' + g + ',' + b + ')';
	if (element.style.color != "rgb(255, 255, 255)") {
		setTimeout(function() {
			remove(element, r, g, b);
		}, 10);
	}
};

function createTable(target, date, list, table, tbody, row, cell, cell2, cell3,
		button) {
	_.each(list, function(key, value) {
		row = tbody.insertRow(tbody.rows.length);
		cell = row.insertCell(0);
		text = document.createTextNode(key.employee.name + ' '
				+ key.employee.surname);
		cell.appendChild(text);
		cell2 = row.insertCell(1);
		text2 = document.createTextNode(key.type + "  :  " + key.description);
		cell2.appendChild(text2);
		cell3 = row.insertCell(2);
		button = document.createElement('button');
		button.className = 'delete btn-danger glyphicon glyphicon-remove';
		button.tempData = {
			'date' : date,
			'name' : key.employee.name,
			'surname' : key.employee.surname,
			'event' : key.type,
			'pk' : key.pk
		}; // Appending date to handle for delete click event
		cell3.appendChild(button);
		cell3.style.cssFloat = 'right';
		row.appendChild(cell);
		row.appendChild(cell2);
		row.appendChild(cell3);
		$('#listevents').append(row);
	});
}

function createTable2(target, date, list, table, tbody, row, cell, cell2, cell3, button) {
	console.log("i am here");
	_.each(list, function(key, value) {
		row = tbody.insertRow(tbody.rows.length);
		row.className='alert alert-info';
//		cell = row.insertCell(0);
//		var text = document.createTextNode(
//				key.employee.name + ' '	+ key.employee.surname + ':');
//		cell.appendChild(text);
//		cell2 = row.insertCell(1);
		cell2 = row.insertCell(0);//TEMP remove this line when refactored
		text2 = document.createTextNode(key.type);// + " " + key.oooType + " "  + key.description
		cell2.appendChild(text2);
//		cell3 = row.insertCell(2);
//		cell3.style.cssFloat = 'right';
//		
//		if(button && cell3 && date){
//			cell3 = row.insertCell(2);
//			cell3.style.cssFloat = 'right';
//			button = document.createElement('button');
//			button.className = 'delete btn-danger glyphicon glyphicon-remove';
//			button.tempData = {
//				'date' : date,
//				'name' : key.employee.name,
//				'surname' : key.employee.surname,
//				'event' : key.type
//			}; // Appending date to handle for delete click event
//			cell3.appendChild(button);
//		}		
	
//		row.appendChild(cell);
		row.appendChild(cell2);
//		if(cell3){
//			row.appendChild(cell3);
//		}
		target.append(row);

		
	});
}

function dateAsString(n){
    return n > 9 ? "" + n: "0" + n;
}

function ajaxRequest(year, date){
	$('.table-condensed tbody').on({mouseover:function(e){
			var selectedDay=dateAsString($(this).text());
			var newMonth=$('.datepicker-switch').text().match(/[^\s]+/);
			var searchDate=new Date(newMonth[0]+' '+selectedDay+', ' +year)
			console.log('blah');
			console.log(searchDate);
			var request=null;
			
			var inumber=($("#inumber option:selected").attr('value')); //INUMBER IS PK UNTIL RESTRUCTURING OF DAO PENDING DISCUSSION...
			request=$.ajax({
				url:'employeeeventstoday',
				type:'POST',
				headers:{
                    'Accept':'application/json'
                },
                success: function(data){
                	console.log(data);
                	if(request && request.readystate != 4){
						request.abort();
			        }
					var target, table, tbody, row, cell, text, text2, cell2, cell3, button;									
					var list = _.sortBy(data, function(o) { return o; });
					table=$('#todays-events');
					tbody=document.createElement('tbody');
					target=$('#todays-events');
					table.append(tbody);
					$('#todays-events tbody').html('');
					createTable2(target, null, list, table, tbody, row, cell, cell2, null, null);					
				},
                data: {inumber:$("#inumber option:selected").attr('value'),'date':searchDate}
			});	
			
			//Replaced new Date(year+'-'+date[1]+'-'+selectedDay) with searchDate
		},mouseout:function(e){
		
		}			
	},'tr td');
	
}
