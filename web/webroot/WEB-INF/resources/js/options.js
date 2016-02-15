var options = {
	events_source : "feedCalendar",
	view : 'month',
	tmpl_path : "${tmpls}",
	tmpl_cache : false,
	onAfterViewLoad : function(view) {
		$('#MonthYear').text(this.getTitle());
		$('.btn-group button').removeClass('active');
		$('button[data-calendar-view="' + view + '"]').addClass('active');
	},
	classes : {
		months : {
			general : 'label'
		}
	}
};