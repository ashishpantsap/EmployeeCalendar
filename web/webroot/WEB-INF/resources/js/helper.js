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
				+ key.employee.surname + ':');
		cell.appendChild(text);
		cell2 = row.insertCell(1);
		text2 = document.createTextNode(key.type);
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


