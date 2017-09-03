
PrimeFaces.widget.Schedule.prototype.configureLocale = function() {
	var lang = PrimeFaces.locales[this.cfg.locale];

	this.cfg.weekNumbers = true;
	this.cfg.weekNumberCalculation = 'iso';
	this.cfg.weekNumberTitle = 'Week ';
	if (lang) {
		this.cfg.firstDay = lang.firstDay;
		this.cfg.monthNames = lang.monthNames;
		this.cfg.monthNamesShort = lang.monthNamesShort;
		this.cfg.dayNames = lang.dayNames;
		this.cfg.dayNamesShort = lang.dayNamesShort;
		this.cfg.buttonText = {
		    today : lang.currentText,
		    month : lang.month,
		    week : lang.week,
		    day : lang.day
		};
		this.cfg.allDayText = lang.allDayText;
		this.cfg.weekNumberTitle = lang.calendarWeekText;
	}

};
