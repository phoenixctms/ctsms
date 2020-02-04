var FieldCalculation = FieldCalculation || {};

(function(FieldCalculation) {
	var inputFieldVariableMap = {};
	var inputFieldVars = {};
	var errorMessageId = null;

	var silent = false;

	var expressionDeclarationRegExp = /^function\((([a-zA-Z0-9_]+,?)*)\)/;

	var defaultLocale = 'en';
	var localizedMessages = {};
	localizedMessages['de'] = {
	    'errorIndex'          : "%s - %s (Index %d)%s%s",
	    'error'               : "%s - %s%s%s",
	    'required'            : " erforderlich",
	    'mustBeEmpty'         : " muss leer sein",
	    'mustBeUnchecked'     : " darf nicht angekreuzt sein",
	    'mustBeUnselected'    : " darf nicht gesetzt sein",
	    'mustBeUnmarked'      : " darf nicht markiert sein",

	    'true'                : "angekreuzt",
	    'false'               : "nicht angekreuzt",
	    'calculatedIndex'     : "%s - %s (Index %d) erwartet: %s",
	    'calculated'          : "%s - %s erwartet: %s",
	    'enteredIndex'        : "%s - %s (Index %d) eingegeben: %s",
	    'entered'             : "%s - %s eingegeben: %s",
	    'noSelectionLabel'    : "[kein]",

	    'date is empty'       : "Datum ist leer",
	    'wrong date format'   : "falsches Datumsformat",
	    'year required'       : "Jahr erforderlich",
	    'month not regognized': "Monat nicht erkannt",
	    'month required'      : "Monat erforderlich",
	    'day required'        : "Tag erforderlich",
	    'invalid date'        : "ungültiges Datum",

	    'time is empty'       : "Uhrzeit ist leer",
	    'wrong time format'   : "falsches Uhrzeitformat",
	    'hour required'       : "Stunde erforderlich",
	    'minute required'     : "Minute erforderlich",
	    'invalid time'        : "ungültige Uhrzeit",

	    'customMonthNameToNumberMap' : {
	    	'JAN' : '01',
	    	'FEB' : '02',
	    	'MÄR' : '03',
	    	'APR' : '04',
	    	'MAI' : '05',
	    	'JUN' : '06',
	    	'JUL' : '07',
	    	'AUG' : '08',
	    	'SEP' : '09',
	    	'OKT' : '10',
	    	'NOV' : '11',
	    	'DEZ' : '12'
	    },
	    'customMonthNumberToName' : ['JAN','FEB','MÄR','APR','MAI','JUN','JUL','AUG','SEP','OKT','NOV','DEZ'],
	    'customDateSeparator' : '-',
	    'customTimeSeparator' : ':',
	    'customPartialDatePartRegExp' : /NK|NA|ND/,
	    'customPartialTimePartRegExp' : /NK|NA|ND/
	};
	localizedMessages['en'] = {
	    'errorIndex'          : "%s - %s (index %d)%s%s",
	    'error'               : "%s - %s%s%s",
	    'required'            : " required",
	    'mustBeEmpty'         : " must be empty",
	    'mustBeUnchecked'     : " must be unchecked",
	    'mustBeUnselected'    : " must not be selected",
	    'mustBeUnmarked'      : " must not be marked",

	    'true'                : "checked",
	    'false'               : "unchecked",
	    'calculatedIndex'     : "%s - %s (index %d) expected: %s",
	    'calculated'          : "%s - %s expected: %s",
	    'enteredIndex'        : "%s - %s (index %d) entered: %s",
	    'entered'             : "%s - %s entered: %s",
	    'noSelectionLabel'    : "[none]",

	    'date is empty'       : "date is empty",
	    'wrong date format'   : "wrong date format",
	    'year required'       : "year required",
	    'month not regognized': "month not regognized",
	    'month required'      : "month required",
	    'day required'        : "day required",
	    'invalid date'        : "invalid date",

	    'time is empty'       : "time is empty",
	    'wrong time format'   : "wrong time format",
	    'hour required'       : "hour required",
	    'minute required'     : "minute required",
	    'invalid time'        : "invalid time",

	    'customMonthNameToNumberMap' : {
	    	'JAN' : '01',
	    	'FEB' : '02',
	    	'MAR' : '03',
	    	'APR' : '04',
	    	'MAY' : '05',
	    	'JUN' : '06',
	    	'JUL' : '07',
	    	'AUG' : '08',
	    	'SEP' : '09',
	    	'OCT' : '10',
	    	'NOV' : '11',
	    	'DEC' : '12'
	    },
	    'customMonthNumberToName' : ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'],
	    'customDateSeparator' : '-',
	    'customTimeSeparator' : ':',
	    'customPartialDatePartRegExp' : /NK|NA|ND/,
	    'customPartialTimePartRegExp' : /NK|NA|ND/
	};

	function _cloneJSON(obj) {
		//http://stackoverflow.com/questions/4120475/how-to-create-and-clone-a-json-object
	    // basic type deep copy
	    if (obj === null || obj === undefined || typeof obj !== 'object')  {
	        return obj
	    }
	    // array deep copy
	    if (obj instanceof Array) {
	        var cloneA = [];
	        for (var i = 0; i < obj.length; ++i) {
	            cloneA[i] = _cloneJSON(obj[i]);
	        }
	        return cloneA;
	    }
	    if (obj instanceof JSJoda.LocalDate) {
	    	return JSJoda.LocalDate.from(obj);
	    }
	    if (obj instanceof JSJoda.LocalTime) {
	    	return JSJoda.LocalTime.from(obj);
	    }
	    if (obj instanceof JSJoda.LocalDateTime) {
	    	return JSJoda.LocalDateTime.from(obj);
	    }
	    if (obj instanceof JSJoda.ZonedDateTime) {
	    	return JSJoda.ZonedDateTime.from(obj);
	    }
	    // object deep copy
	    var cloneO = {};
	    for (var i in obj) {
	        cloneO[i] = _cloneJSON(obj[i]);
	    }
	    return cloneO;
	}

	function _getInputFieldVariableMapSize() {
		var result = 0;
		for ( var variableName in inputFieldVariableMap) {
			result++;
		}
		return result;
	}

	function _getInputFieldVariable(variableName, index) {
		if (variableName instanceof Array) {
			index = variableName[1];
			variableName = variableName[0];
		} else if (variableName !== null && typeof variableName === 'object') {
			index = variableName.index;
			variableName = variableName.name;
		}
		return _getSeriesInputFieldVariable(variableName, index, false);
	}

	function _getSeriesInputFieldVariable(variableName, index, returnArrays) {
		if (variableName in inputFieldVariableMap) {
			var series = inputFieldVariableMap[variableName];
			if (series instanceof Array) {
				if (index == null) {
					if (returnArrays) {
						return series;
					}
				} else if (index in series) {
					return series[index];
				}
			} else {
				return series;
			}
		}
		return null;
	}

	function _processInputFieldVariableValues() {
		if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			console.log("process variables");
		}
		for ( var variableName in inputFieldVariableMap) {
			var series = inputFieldVariableMap[variableName];
			if (series instanceof Array) {
				for (var index = 0; index < series.length; index++) { //index asc
					if (index in series) {
						var inputFieldVariable = series[index];
						_processInputFieldVariableValue(inputFieldVariable, {});
					}
				}
			} else {
				var inputFieldVariable = series;
				_processInputFieldVariableValue(inputFieldVariable, {});
			}
		}
	}
	function _invalidateInputFieldVariableValues(excludeVariableName, excludeIndex) {
		var msg = "";
		for ( var variableName in inputFieldVariableMap) {
			var series = inputFieldVariableMap[variableName];
			if (series instanceof Array) {
				for (var index = 0; index < series.length; index++) {
					if (index in series) {
						var inputFieldVariable = series[index];
						if ((!excludeVariableName || excludeVariableName != variableName) || (excludeIndex == null || inputFieldVariable.index != excludeIndex)) {
							inputFieldVariable.processed = false;
							if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
								console.log("variable " + _debugVarName(inputFieldVariable) + " invalidated");
							}
						} else {
							if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
								msg = ", ecluding " + _debugVarName(inputFieldVariable);
								if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
									console.log("variable " + _debugVarName(inputFieldVariable) + " NOT invalidated");
								}
							}
						}
					}
				}
			} else {
				var inputFieldVariable = series;
				if (!excludeVariableName || excludeVariableName != variableName) {
					inputFieldVariable.processed = false;
					if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
						console.log("variable " + _debugVarName(inputFieldVariable) + " invalidated");
					}
				} else {
					if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
						msg = ",  ecluding " + _debugVarName(inputFieldVariable);
						if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
							console.log("variable " + _debugVarName(inputFieldVariable) + " NOT invalidated");
						}
					}
				}
			}
		}
		if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			console.log("variables invalidated" + msg);
		}
	}
	function _updateInputFieldVariableOutputs() {
		if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			console.log("update and print outputs");
		}
		var errorMsgs;
		if (errorMessageId != null && errorMessageId.length > 0) {
			errorMsgs = [];
		} else {
			errorMsgs = null;
		}
		for (var variableName in inputFieldVariableMap) {
			var series = inputFieldVariableMap[variableName];
			if (series instanceof Array) {
				for (var index = 0; index < series.length; index++) { //index asc
					if (index in series) {
						var inputFieldVariable = series[index];
						_updateInputFieldVariableOutput(inputFieldVariable, null);
						_printInputFieldVariableOutput(inputFieldVariable);
						if (errorMsgs != null && (INPUT_FIELD_DELTA_SUMMARY_MAX == null || errorMsgs.length < INPUT_FIELD_DELTA_SUMMARY_MAX)) {
							_deltaErrorMsg(errorMsgs,inputFieldVariable);
						}
					}
				}
			} else {
				var inputFieldVariable = series;
				_updateInputFieldVariableOutput(inputFieldVariable, null);
				_printInputFieldVariableOutput(inputFieldVariable);
				if (errorMsgs != null && (INPUT_FIELD_DELTA_SUMMARY_MAX == null || errorMsgs.length < INPUT_FIELD_DELTA_SUMMARY_MAX)) {
					_deltaErrorMsg(errorMsgs,inputFieldVariable);
				}
			}
		}

		if (errorMsgs != null) {
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
				console.log("display delta errors: " + errorMsgs.length);
			}
			setErrorMessageTexts(errorMessageId,errorMsgs);
			if (errorMsgs.length > 0) {
				showErrorMessage(errorMessageId);
			} else {
				hideErrorMessage(errorMessageId);
			}
		}
		return errorMsgs;

	}

	function _deltaErrorMsg(errorMsgs,inputFieldVariable) {
		if (inputFieldVariable.processed && ((inputFieldVariable.delta && (inputFieldVariable.output != null && inputFieldVariable.output.length > 0)) || (inputFieldVariable.valueErrorMessage != null && inputFieldVariable.valueErrorMessage.length > 0) || (inputFieldVariable.outputErrorMessage != null && inputFieldVariable.outputErrorMessage.length > 0))) {
			var msg = { ecrfFieldId: inputFieldVariable.value.ecrfFieldId, inquiryId: inputFieldVariable.value.inquiryId, tagId: inputFieldVariable.value.tagId, series: inputFieldVariable.value.series, index: inputFieldVariable.value.index, output: null };
			if (inputFieldVariable.valueErrorMessage != null && inputFieldVariable.valueErrorMessage.length > 0) {
				msg.output = inputFieldVariable.valueErrorMessage;
			} else if (inputFieldVariable.outputErrorMessage != null && inputFieldVariable.outputErrorMessage.length > 0) {
				msg.output = inputFieldVariable.outputErrorMessage;
			} else if (inputFieldVariable.output != null && inputFieldVariable.output.length > 0) {
				msg.output = inputFieldVariable.output;
			}
			errorMsgs.push(msg);
		}
	}

	function _refreshInputFieldVariables(excludeVariableName, excludeIndex) {
		_invalidateInputFieldVariableValues(excludeVariableName, excludeIndex);
		_processInputFieldVariableValues();
		_updateInputFieldVariableOutputs();
	}

	function _printInputFieldVariableOutput(inputFieldVariable) {
		if (inputFieldVariable && inputFieldVariable.outputId != null && inputFieldVariable.outputId.length > 0) {
			var outputElement = _getElement(inputFieldVariable.outputId);
			if (outputElement != null) {

				outputElement.removeClass('ctsms-inputfield-output-valueerror ctsms-inputfield-output-outputerror ctsms-inputfield-output-delta ctsms-inputfield-output-nodelta ctsms-inputfield-output');
				if (inputFieldVariable.valueErrorMessage != null && inputFieldVariable.valueErrorMessage.length > 0) {
					outputElement.html(inputFieldVariable.valueErrorMessage);
					outputElement.addClass('ctsms-inputfield-output-valueerror');
				} else if (inputFieldVariable.outputErrorMessage != null && inputFieldVariable.outputErrorMessage.length > 0) {
					outputElement.html(inputFieldVariable.outputErrorMessage);
					if (!outputElement.hasClass('ctsms-inputfield-output-disabled')) {
						outputElement.addClass('ctsms-inputfield-output-outputerror');
					}
				} else if (inputFieldVariable.output != null && inputFieldVariable.output.length > 0) {
					outputElement.html(inputFieldVariable.output);
					if (inputFieldVariable.value.jsValueExpression != null && inputFieldVariable.value.jsValueExpression.length > 0) {
						if (!outputElement.hasClass('ctsms-inputfield-output-disabled')) {
    						if (inputFieldVariable.delta) {
    							outputElement.addClass('ctsms-inputfield-output-delta');
    						} else {
    							outputElement.addClass('ctsms-inputfield-output-nodelta');
    						}
    					}
					} else {
						outputElement.addClass('ctsms-inputfield-output');
					}
				} else {
					outputElement.html("");
				}
			}
		}
	}

	function _processInputFieldVariableValue(inputFieldVariable, cycleCheckMap) {

		if (inputFieldVariable) {
			if (inputFieldVariable.processed) {
				return inputFieldVariable.value;
			}
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
				console.log("processing variable value " + _debugVarName(inputFieldVariable));
			}
			inputFieldVariable.valueErrorMessage = null;
			var evaluation = _evalInputFieldVariableExpression(inputFieldVariable, inputFieldVariable.value.jsValueExpression, "value expression", cycleCheckMap, true);
			if (evaluation != null) {
				inputFieldVariable.oldValue = _cloneJSON( inputFieldVariable.value);
				inputFieldVariable.valueErrorMessage = evaluation.errorMessage;
				_setInputFieldVariableValue(inputFieldVariable.value, evaluation.returnValue);
			}


			inputFieldVariable.processed = true;
			inputFieldVariable.delta = !_equalInputFieldVariable(inputFieldVariable);
			_debugVariableValue("variable value updated ", inputFieldVariable);
			return inputFieldVariable.value;

		}
		return null;
	}

	function _updateInputFieldVariableOutput(inputFieldVariable, cycleCheckMap) {

		if (inputFieldVariable) {
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
				console.log("processing variable output " + _debugVarName(inputFieldVariable));
			}
			inputFieldVariable.outputErrorMessage = null;
			var evaluation = _evalInputFieldVariableExpression(inputFieldVariable, inputFieldVariable.value.jsOutputExpression, "output expression", cycleCheckMap, false);
			if (evaluation != null) {
				inputFieldVariable.oldOutput = inputFieldVariable.output;
				inputFieldVariable.outputErrorMessage = evaluation.errorMessage;
				inputFieldVariable.output = evaluation.returnValue;
			}
		}

	}

	function _evalInputFieldVariableExpression(inputFieldVariable, expression, errorMessagePrefix, cycleCheckMap, copyEnteredValue) {

		var errorMessage;
		var returnValue;
		var variableName = inputFieldVariable.value.jsVariableName;
		if (expression != null && expression.length > 0 && expression.indexOf("{") > 0) {
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
				console.log("evaluating " + _debugVarName(inputFieldVariable) + " " + errorMessagePrefix + " ...");
			}
			if (cycleCheckMap != null && (variableName in cycleCheckMap)) {
				errorMessage = errorMessagePrefix + ": circular dependency for variable " + variableName + " detected";
				returnValue = null;
			} else {
				if (cycleCheckMap != null) {
					cycleCheckMap[variableName] = true;
				}

				var definition = expression.substring(0, expression.indexOf("{")).replace(/\s+/gm, '');
				if (expressionDeclarationRegExp.test(definition)) {

					var matches = expressionDeclarationRegExp.exec(definition);
					var argNames = matches[1].split(",");

					var mask = inputFieldVariable.mask;

					var index = inputFieldVariable.value.index;
					var argValue;
					var argInputFieldVariable;
					var argsOk = true;
					var length = 0;
					var i;
					var j;
					for (i = 0; i < argNames.length; i++) {
						if (argNames[i] != null && argNames[i].length > 0) {

							argInputFieldVariable = _getSeriesInputFieldVariable(argNames[i], null, true);
							if (argInputFieldVariable == null) {
								errorMessage = errorMessagePrefix + " " + variableName + ": unknown argument variable " + argNames[i];
								if (index != null) {
									errorMessage += "[" + index + "]";
								}
								returnValue = null;
								argsOk = false;
								break;
							} else if (argInputFieldVariable instanceof Array) {
								if (argInputFieldVariable[0] != null && inputFieldVariable.value.section == argInputFieldVariable[0].value.section) {
									argInputFieldVariable = argInputFieldVariable[index];
									if (argInputFieldVariable == null) {
										errorMessage = errorMessagePrefix + " " + variableName + ": missing argument variable index " + argNames[i];
										if (index != null) {
											errorMessage += "[" + index + "]";
										}
										returnValue = null;
										argsOk = false;
										break;
									} else {
										argValue = _processInputFieldVariableValue(argInputFieldVariable, cycleCheckMap ? cycleCheckMap : {});
									}
								} else {
									argValue = [];
									length = argInputFieldVariable.length;
									for (j = 0; j < length; j++) {
										if (j in argInputFieldVariable) {
											argValue.push(_processInputFieldVariableValue(argInputFieldVariable[j], cycleCheckMap ? cycleCheckMap : {}));
										}
									}
								}
							} else {
								argValue = _processInputFieldVariableValue(argInputFieldVariable, cycleCheckMap ? cycleCheckMap : {});
							}

							if ((!argInputFieldVariable || (argInputFieldVariable.valueErrorMessage != null && argInputFieldVariable.valueErrorMessage.length > 0))) {

								errorMessage = argInputFieldVariable.valueErrorMessage;
								returnValue = null;
								argsOk = false;
								break;
							} else {
								if (argValue instanceof Array) {
									mask[argNames[i]] = [];
									length = argValue.length;
									for (j = 0; j < length; j++) {
										if (j in argValue) {
											mask[argNames[i]].push(_getInputFieldVariableValue(argValue[j]));
										}
									}
								} else {
									mask[argNames[i]] = _getInputFieldVariableValue(argValue);
								}
							}
						}
					}
					if (argsOk) {
						_exportVars(mask, inputFieldVariable);

						returnValue = null;
						try {
							returnValue = (new Function("with(this) {\nreturn (" + expression + ")(" + argNames.join(",") + ");\n}")).call(mask);
							errorMessage = null;
						} catch (e) {
							if (_testPropertyExists(e,'msg')) {
								errorMessage = e.msg;
							} else {
								errorMessage = errorMessagePrefix + " " + variableName + ": " + e.toString();
							}
						}

					}
				} else {
					errorMessage = errorMessagePrefix + ": value expression declaration for variable " + variableName + " invalid";
					returnValue = null;
				}
			}
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
				console.log("evaluating " + _debugVarName(inputFieldVariable) + " " + errorMessagePrefix + " completed: " + (errorMessage ? errorMessage : 'no error'));
			}
			return {
			    'errorMessage' : errorMessage,
			    'returnValue' : returnValue
			};
		} else if (copyEnteredValue) {
			inputFieldVariable.oldValue = _cloneJSON( inputFieldVariable.value);
			inputFieldVariable.value = _cloneJSON( inputFieldVariable.enteredValue);
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
				console.log("entered value of " + _debugVarName(inputFieldVariable) + " copied");
			}
			return null;
		} else {
			return null;
		}

	}

	function _exportVars(mask, inputFieldVariable) {

		var calculated = _getInputFieldVariableValue(inputFieldVariable.value);
		mask["$value"] = calculated;
		var entered = _getInputFieldVariableValue(inputFieldVariable.enteredValue);
		mask["$enteredValue"] = entered;
		mask["$oldValue"] = _getInputFieldVariableValue(inputFieldVariable.oldValue);
		mask["$output"] = inputFieldVariable.output;
		mask["$delta"] = inputFieldVariable.delta;
		mask["$oldOutput"] = inputFieldVariable.oldOutput;
		mask["$created"] = (inputFieldVariable.value.id != null);
		mask["$disabled"] = inputFieldVariable.value.disabled;
		if (inputFieldVariable.value.series) {
			mask["$index"] = inputFieldVariable.value.index;
		}

		mask["$selectionSetValues"] = inputFieldVariable.value.selectionSetValues;
		mask["$proband"] =  inputFieldVars.proband;
		mask["$trial"] = inputFieldVars.trial;
		mask["$probandAddresses"] = inputFieldVars.probandAddresses;
		mask["$listEntry"] = inputFieldVars.probandListEntry;
		mask["$visitSchedule"] = inputFieldVars.visitScheduleItems;
		mask["$probandGroups"] = inputFieldVars.probandGroups;
		mask["$activeUser"] = inputFieldVars.activeUser;
		mask["$locale"] = inputFieldVars.locale;
		mask["$section"] = inputFieldVariable.value.section;
		mask["$category"] = inputFieldVariable.value.category;
		mask["$inputFieldName"] = inputFieldVariable.value.inputFieldName;
		mask["$probandGroup"] = inputFieldVariable.value.probandGroupToken;
		mask["$visit"] = inputFieldVariable.value.visitToken;

		mask["getEnteredValue"] = _getEnteredValue;
		
		var _printSelectionSetValues = function(value, separator, selectionSetValueField) {
			if (!(value instanceof Array)) {
				value = [ value ];
			}
			var result = [];
			if (typeof separator === 'undefined') {
				separator = ", ";
			}
			if (typeof selectionSetValueField === 'undefined') {
				selectionSetValueField = "name";
			}
			for ( var i = 0; i < value.length; i++) {
				result.push((inputFieldVariable.value.selectionSetValues[value[i]])[selectionSetValueField]);
			}
			return result.join(separator);
		};

		mask["printSelectionSetValues"] = _printSelectionSetValues;

		var _printValue = function(value, formatOrSeparator, selectionSetValueFieldOrLocale) {

			if (value == null || value === undefined) {

		    } else if (typeof value === 'string') {
		    	return value;
			} else if(typeof value === "boolean"){
				return localizedMessages[inputFieldVars.locale][value];
			} else if(typeof value === "number"){
				if (isNaN(value)) {

				} else {
					if (typeof formatOrSeparator === 'undefined') {
						if (parseInt(value) === value) {
							formatOrSeparator = "%d";
						} else {
							formatOrSeparator = "%f";
						}
					}
					return _formatDecimal(sprintf(formatOrSeparator,value));
				}
			} else if (value instanceof Array) {
				if (value.length <= 0 || (value.length == 1 && value[0] == "")) {
				    return localizedMessages[inputFieldVars.locale].noSelectionLabel;
			    }
				return _printSelectionSetValues(value, formatOrSeparator, selectionSetValueFieldOrLocale);
		    } else if(value instanceof JSJoda.LocalDate) {
		    	if ('date' == formatOrSeparator) {
		    		return _printDateCustom(value,selectionSetValueFieldOrLocale);
		    	} else {
		    		return value.format(JSJoda.DateTimeFormatter.ofPattern(formatOrSeparator != null ? formatOrSeparator : INPUT_DATE_PATTERN));
		    	}
		    } else if(value instanceof JSJoda.LocalTime) {
		    	if ('time' == formatOrSeparator) {
		    		return _printTimeCustom(value,selectionSetValueFieldOrLocale);
		    	} else {
		    		return value.format(JSJoda.DateTimeFormatter.ofPattern(formatOrSeparator != null ? formatOrSeparator : INPUT_TIME_PATTERN));
		    	}
		    } else if(value instanceof JSJoda.LocalDateTime) {
		    	if ('date' == formatOrSeparator) {
					return _printDateCustom(value,selectionSetValueFieldOrLocale);
				} else if ('time' == formatOrSeparator) {
					return _printTimeCustom(value,selectionSetValueFieldOrLocale);			
		    	} else {
		    		return value.format(JSJoda.DateTimeFormatter.ofPattern(formatOrSeparator != null ? formatOrSeparator : INPUT_DATETIME_PATTERN));
		    	}
		    } else if(value instanceof JSJoda.ZonedDateTime) {
		    	var timestamp;
		    	var pattern = INPUT_DATETIME_PATTERN;
		    	if (inputFieldVariable.value.userTimeZone) {
		    		timestamp = value.withZoneSameInstant(JSJoda.ZoneId.of(INPUT_TIMEZONE_ID));
		    		pattern += ' (VV)';
		    	} else {
		    		timestamp = value;
		    	}
		    	if ('date' == formatOrSeparator) {
					return _printDateCustom(timestamp,selectionSetValueFieldOrLocale);
				} else if ('time' == formatOrSeparator) {
					return _printTimeCustom(timestamp,selectionSetValueFieldOrLocale);			
		    	} else {
		    		return timestamp.format(JSJoda.DateTimeFormatter.ofPattern(formatOrSeparator != null ? formatOrSeparator : pattern));
		    	}
			} else if(typeof value === "object" && 'ids' in value){
				if (value.ids.length <= 0) {
					return localizedMessages[inputFieldVars.locale].noSelectionLabel;
			    }
				return _printSelectionSetValues(value.ids,formatOrSeparator, selectionSetValueFieldOrLocale);
			} else {

			}
			return '';

		};

		mask["printValue"] = _printValue;

		mask["printCalculated"] = function(formatOrSeparator, selectionSetValueFieldOrLocale) {
			var value = _printValue(calculated, formatOrSeparator, selectionSetValueFieldOrLocale);
			if (inputFieldVariable.value.series) {
		        return sprintf(localizedMessages[inputFieldVars.locale].calculatedIndex,inputFieldVariable.value.section,inputFieldVariable.value.inputFieldName,inputFieldVariable.value.index,value);
		    } else {
		        return sprintf(localizedMessages[inputFieldVars.locale].calculated,inputFieldVariable.value.section,inputFieldVariable.value.inputFieldName,value);
		    }
		};
		mask["printEntered"] = function(formatOrSeparator, selectionSetValueFieldOrLocale) {
			var value = _printValue(entered, formatOrSeparator, selectionSetValueFieldOrLocale);
			if (inputFieldVariable.value.series) {
		        return sprintf(localizedMessages[inputFieldVars.locale].enteredIndex,inputFieldVariable.value.section,inputFieldVariable.value.inputFieldName,inputFieldVariable.value.index,value);
		    } else {
		        return sprintf(localizedMessages[inputFieldVars.locale].entered,inputFieldVariable.value.section,inputFieldVariable.value.inputFieldName,value);
		    }
		};

		mask["findSelectionSetValueIds"] = function(condition) {
			var result = [];
			if (_testFunction(condition)) {
				for ( var id in inputFieldVariable.value.selectionSetValues) {
					if (condition(inputFieldVariable.value.selectionSetValues[id])) {
						result.push(id);
					}
				}
			}
			return result;
		};

		mask["findTagValues"] = function(condition) {
			var result = [];
			if (_testFunction(condition)) {
				for ( var position in inputFieldVars.tagValues ) {
					if (condition(inputFieldVars.tagValues[position])) {
						result.push(_getInputFieldVariableValue(inputFieldVars.tagValues[position]));
					}
				}
			}
			return result;
		};

		var _throwError = function(message,localize,noColon) {
		    if (noColon) {
		        noColon = "";
		    } else {
		        noColon = ": ";
		    }
		    if (localize) {
		        message = localizedMessages[inputFieldVars.locale][message];
		    }
		    if (inputFieldVariable.value.series) {
		        throw { msg: sprintf(localizedMessages[inputFieldVars.locale].errorIndex,inputFieldVariable.value.section,inputFieldVariable.value.inputFieldName,inputFieldVariable.value.index,noColon,message) };
		    } else {
		        throw { msg: sprintf(localizedMessages[inputFieldVars.locale].error,inputFieldVariable.value.section,inputFieldVariable.value.inputFieldName,noColon,message) };
		    }
		};

		mask["throwErrorLocalized"] = function(message,noColon) {
			return _throwError(message,true,noColon);
		};
		mask["throwError"] = _throwError;

		var _errorIfEmpty = function() {
		    if (entered == null || entered === undefined) {
		        _throwError('required',true,true);
		    } else if (typeof entered === 'string') {
				if (entered.length <= 0) {
				    _throwError('required',true,true);
				}
			} else if(typeof entered === "boolean"){
			} else if(typeof entered === "number"){
			} else if (entered instanceof Array) {
			    if (entered.length <= 0 || (entered.length == 1 && entered[0] == "")) {
				    _throwError('required',true,true);
			    }
		    } else if(entered instanceof JSJoda.LocalDate) {
		    	
		    } else if(entered instanceof JSJoda.LocalTime) {
		    	
		    } else if(entered instanceof JSJoda.LocalDateTime) {
		    	
		    } else if(entered instanceof JSJoda.ZonedDateTime) {
		    	
			} else if(typeof entered === "object" && 'ids' in entered){
				if (entered.ids.length <= 0) {
				    _throwError('required',true,true);
			    }
			} else {

			}
		};

		mask["errorIfEmpty"] = _errorIfEmpty;

		mask["errorIfUnSet"] = function() {
			_errorIfEmpty();
			if(typeof entered === "boolean"){
				if (!entered) {
					_throwError('required',true,true);
				}
			}
		};

		mask["errorIfSet"] = function() {
		    if (entered == null || entered === undefined) {

		    } else if (typeof entered === 'string') {
				if (entered.length > 0) {
				    _throwError('mustBeEmpty',true,true);
				}
			} else if(typeof entered === "boolean"){
				if (entered) {
				    _throwError('mustBeUnchecked',true,true);
				}
			} else if(typeof entered === "number"){
			    _throwError('mustBeEmpty',true,true);
			} else if (entered instanceof Array) {
			    if (entered.length > 0 && !(entered.length == 1 && entered[0] == "")) {
				    _throwError('mustBeUnselected',true,true);
			    }
			} else if(entered instanceof JSJoda.LocalDate) {
				_throwError('mustBeEmpty',true,true);
		    } else if(entered instanceof JSJoda.LocalTime) {
		    	_throwError('mustBeEmpty',true,true);
		    } else if(entered instanceof JSJoda.LocalDateTime) {
		    	_throwError('mustBeEmpty',true,true);
		    } else if(entered instanceof JSJoda.ZonedDateTime) {
		    	_throwError('mustBeEmpty',true,true);
			} else if(typeof entered === "object" && 'ids' in entered){
				if (entered.ids.length > 0) {
				    _throwError('mustBeUnmarked',true,true);
			    }
			} else {
			    //object, function, ...
			    _throwError('mustBeEmpty',true,true);
			}
		};

	}

	function _exportExpressionUtils(mask) {
		mask["sprintf"] = sprintf;
		mask["vsprintf"] = vsprintf;
		mask["quoteJs"] = _quoteJs;

		mask["getInputFieldSelectionSetValue"] = _getInputFieldSelectionSetValue;
		mask["containsName"] = _testSelectionSetValueName;
		mask["containsValue"] = _testSelectionSetValueValue;
		mask["getSeriesValues"] = _getSeriesValues;
		mask["empty"] = _empty;
		mask["getLocalizedMessage"] = _getLocalizedMessage;

		mask["parseDate"] = _parseDate;
		mask["parseDateTime"] = _parseDateTime;
		mask["parseTime"] = _parseTime;
		mask["formatDecimal"] = _formatDecimal;		

		mask["parseDateCustom"] = _parseDateCustom;
		mask["parseTimeCustom"] = _parseTimeCustom;

		mask["selectionSetValueIdsEqual"] = _selectionSetValueIdsEqual;

		mask["setVariable"] = _setVariable;
		mask["setOutput"] = _setOutput;

		mask["openInventory"] = openInventory;
		mask["openStaff"] = openStaff;
		mask["openCourse"] = openCourse;
		mask["openTrial"] = openTrial;
		mask["openProband"] = openProband;
		mask["openInputField"] = openInputField;
		mask["openUser"] = openUser;
		mask["openMassMail"] = openMassMail;

		mask["JSJoda"] = JSJoda;
		mask["INPUT_DATE_PATTERN"] = INPUT_DATE_PATTERN;
		mask["INPUT_TIME_PATTERN"] = INPUT_TIME_PATTERN;
		mask["INPUT_DATETIME_PATTERN"] = INPUT_DATETIME_PATTERN;
		mask["INPUT_JSON_DATETIME_PATTERN"] = INPUT_JSON_DATETIME_PATTERN;
		mask["INPUT_DECIMAL_SEPARATOR"] = INPUT_DECIMAL_SEPARATOR;
		mask["INPUT_TIMEZONE_ID"] = INPUT_TIMEZONE_ID;
		mask["SYSTEM_TIMEZONE_ID"] = SYSTEM_TIMEZONE_ID;
		mask["JSON"] = JSON;
		mask["jQuery"] = jQuery;
		mask["RestApi"] = RestApi;
		if (ENABLE_GEOLOCATION_SERVICES && window.LocationDistance) {
			mask["LocationDistance"] = window.LocationDistance;
		}
		if (window.console) {
			mask["console"] = window.console;
		} else {
			mask["console"] = null;
		}
	}

	function _empty(input) {
	    if (input == null || input === undefined) {
	        return true;
	    } else if (typeof input === 'string') {
			return input.length <= 0;
		} else if(typeof input === "boolean"){
			return !input;
		} else if(typeof input === "number"){
			return false;
		} else if (input instanceof Array) {
		    return (input.length <= 0 || (input.length == 1 && input[0] == ""));
		} else if(input instanceof JSJoda.LocalDate) {
			return false;
	    } else if(input instanceof JSJoda.LocalTime) {
	    	return false;
	    } else if(input instanceof JSJoda.LocalDateTime) {
	    	return false;
	    } else if(input instanceof JSJoda.ZonedDateTime) {
	    	return false;
		} else if(typeof input === "object" && 'ids' in input){
			return input.ids.length <= 0;
		} else {
		    //object, function, ...
		    return false;
		}
	}

	function _quoteJs(str){
	    return (str + '').replace(/[\\"']/g, '\\$&').replace(/\u0000/g, '\\0');
	}

	function _getInputFieldSelectionSetValue(variable, id) {
	    if (variable instanceof Array) {
	        variable = variable[0];
	    } else if (variable !== null && typeof variable === 'object') {
	        variable = variable.name;
	    }
	    var inputFieldVar = _getSeriesInputFieldVariable(variable,null,true);
	    if (inputFieldVar) {
	        if (inputFieldVar instanceof Array) {
	            inputFieldVar = inputFieldVar[0];
	        }
	        if (inputFieldVar) {
	            var inputFieldSelectionSetVals = inputFieldVar.value.inputFieldSelectionSetValues;
	            if (inputFieldSelectionSetVals) {
	                for ( var i = 0; i < inputFieldSelectionSetVals.length; i++) {
	                    if (inputFieldSelectionSetVals[i].id == id) {
	                        return inputFieldSelectionSetVals[i];
	                    }
	                }
	            }
	        }
	    }
	    return {};
	}

	function _testSelectionSetValueName(variable, value, selectionSetValueName) {
	    if (value != null && value.length > 0) {
			if (variable instanceof Array) {
			    variable = variable[0];
	        } else if (variable !== null && typeof variable === 'object') {
			    variable = variable.name;
			}
	        var inputFieldVar = _getSeriesInputFieldVariable(variable,null,true);
			if (inputFieldVar) {
				if (inputFieldVar instanceof Array) {
				    inputFieldVar = inputFieldVar[0];
			    }
				if (inputFieldVar) {
				    var inputFieldSelectionSetValues = inputFieldVar.value.inputFieldSelectionSetValues;
					if (inputFieldSelectionSetValues) {
					    var selectionSetValues = {};
						var i;
						for ( i = 0; i < inputFieldSelectionSetValues.length; i++) {
						    selectionSetValues[inputFieldSelectionSetValues[i].id] = inputFieldSelectionSetValues[i];
						}
						for ( i = 0; i < value.length; i++) {
						    var val = selectionSetValues[value[i]];
							if (val != null && val.name == selectionSetValueName) {
							    return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	function _testSelectionSetValueValue(variable, value, selectionSetValueValue) {
	    if (value != null && value.length > 0) {
		    if (variable instanceof Array) {
		        variable = variable[0];
		    } else if (variable !== null && typeof variable === 'object') {
		        variable = variable.name;
		    }
		    var inputFieldVar = _getSeriesInputFieldVariable(variable,null,true);
		    if (inputFieldVar) {
			    if (inputFieldVar instanceof Array) {
			        inputFieldVar = inputFieldVar[0];
			    }
			    if (inputFieldVar) {
			        var inputFieldSelectionSetValues = inputFieldVar.value.inputFieldSelectionSetValues;
				    if (inputFieldSelectionSetValues) {
				        var selectionSetValues = {};
					    var i;
					    for ( i = 0; i < inputFieldSelectionSetValues.length; i++) {
					        selectionSetValues[inputFieldSelectionSetValues[i].id] = inputFieldSelectionSetValues[i];
					    }
					    for ( i = 0; i < value.length; i++) {
					        var val = selectionSetValues[value[i]];
						    if (val != null && val.value == selectionSetValueValue) {
						        return true;
						    }
	    				}
		    		}
			    }
		    }
	    }
	    return false;
	}

	function _getSeriesValues(variable) {
		if (variable instanceof Array) {
		    variable = variable[0];
		} else if (variable !== null && typeof variable === 'object') {
		    variable = variable.name;
		}
		var inputFieldVar = _getSeriesInputFieldVariable(variable,null,true);
		var result = [];
		if (inputFieldVar) {
			if (inputFieldVar instanceof Array) {
			    for (var index = 0; index < inputFieldVar.length; index++) {
				    if (index in inputFieldVar) {
					    result.push(_getInputFieldVariableValue(inputFieldVar[index].value));
					}
				}
			}
		}
		return result;
	}

	function _parseDate(input,pattern) {
		if (input == null || input.length == 0) {
			return null;
		}
		try {
			return JSJoda.LocalDate.parse(input,pattern != null ? JSJoda.DateTimeFormatter.ofPattern(pattern) : JSJoda.DateTimeFormatter.ISO_LOCAL_DATE);
		} catch (e) {
			return null;
		}
	}

	function _parseTime(input,pattern) {
		if (input == null || input.length == 0) {
			return null;
		}
		try {
			return JSJoda.LocalTime.parse(input,pattern != null ? JSJoda.DateTimeFormatter.ofPattern(pattern) : JSJoda.DateTimeFormatter.ISO_LOCAL_TIME);
		} catch (e) {
			return null;
		}
	}
	
	function _parseDateTime(input,pattern,zone) {
		if (input == null || input.length == 0) {
			return null;
		}
		try {
			return JSJoda.ZonedDateTime.of(
					JSJoda.LocalDateTime.parse(input,pattern != null ? JSJoda.DateTimeFormatter.ofPattern(pattern) : JSJoda.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
					JSJoda.ZoneId.of(zone != null ? zone : SYSTEM_TIMEZONE_ID)
				);
		} catch (e) {
			return null;
		}
	}

	function _getLocale(locale) {
		if (locale == null || locale === undefined) {
			return defaultLocale;
		} else if (!(locale in localizedMessages)) {
        	return defaultLocale;
        }
		return locale;
	}

	function _getLocalizedMessage(message,locale) {
		return localizedMessages[_getLocale(locale)][message];
	}

	function _parseDecimal(input) {
		if (input != null && INPUT_DECIMAL_SEPARATOR != null && INPUT_DECIMAL_SEPARATOR.length > 0) {
			input = (input+'').replace(INPUT_DECIMAL_SEPARATOR,'.');
		}
		return input;
	}
	
	function _formatDecimal(input) {
		if (input != null && INPUT_DECIMAL_SEPARATOR != null && INPUT_DECIMAL_SEPARATOR.length > 0) {
			input = (input+'').replace('.',INPUT_DECIMAL_SEPARATOR);
		}
		return input;
	}
	
	function _printDateCustom(input,locale) {
		if (input == null || input === undefined) {

		} else if ((input instanceof JSJoda.LocalDate) || (input instanceof JSJoda.LocalDateTime) || (input instanceof JSJoda.ZonedDateTime)) {
			var customDateSeparator = _getLocalizedMessage('customDateSeparator',locale);
			return zeroFill(input.dayOfMonth(),2) + customDateSeparator + _getLocalizedMessage('customMonthNumberToName',locale)[input.monthValue() - 1] + customDateSeparator + input.year();
		}
		return '';
	}

	function _printTimeCustom(input,locale) {
		if (input == null || input === undefined) {

		} else if ((input instanceof JSJoda.LocalTime) || (input instanceof JSJoda.LocalDateTime) || (input instanceof JSJoda.ZonedDateTime)) {
			return zeroFill(input.hour(),2) + _getLocalizedMessage('customTimeSeparator',locale) + zeroFill(input.minute(),2);
		}
		return '';
	}

	function _parseDateCustom(input,error,nkDay,nkMonth,locale) {
	    if (input == null || input.length == 0) {
		    if (_testFunction(error)) {
			    error('date is empty');
			}
			return null;
		}
		var ary = input.split(_getLocalizedMessage('customDateSeparator',locale));
		if (ary == null || ary.length != 3 || ary[0].length == 0 || ary[1].length == 0 || ary[2].length == 0) {
		    if (_testFunction(error)) {
			    error('wrong date format');
			}
			return null;
		}
		var regexp = _getLocalizedMessage('customPartialDatePartRegExp',locale);
		var dayRequired = (typeof(nkDay) == typeof(true) ? nkDay : (nkDay != null && nkDay == "required"));
		var monthRequired = (typeof(nkMonth) == typeof(true) ? nkMonth : (nkMonth != null && nkMonth == "required"));
		if (regexp.test(ary[2])) {
			if (regexp.test(ary[0]) && regexp.test(ary[1]) && !dayRequired && !monthRequired) {

			} else {
				if (_testFunction(error)) {
					error('year required');
				}
			}
		    return null;
		}
		var y = ary[2];
		var m;
		var m_nk = false;
		var customMonthNameToNumberMap = _getLocalizedMessage('customMonthNameToNumberMap',locale);
		if (regexp.test(ary[1])) {
		    if (!monthRequired && (nkMonth == null || nkMonth.length == 0)) {
			    nkMonth = '01';
		    } else if (monthRequired) {
				if (_testFunction(error)) {
					error('month required');
				}
				return null;
			}
			m = ary[1].replace(regexp,nkMonth);
			m_nk = true;
		} else if (ary[1] in customMonthNameToNumberMap) {
			m = customMonthNameToNumberMap[ary[1]];
		} else {
		    if (_testFunction(error)) {
			    error('month not regognized');
			}
			return null;
		}
		if (!dayRequired && (nkDay == null || nkDay.length == 0)) {
		    nkDay = '01';
		} else if (dayRequired && regexp.test(ary[0])) {
			if (_testFunction(error)) {
				error('day required');
			}
			return null;
		} else {
			var lastDayOfMonth = JSJoda.LocalDate.of(+y,+m,1).lengthOfMonth();
			if (+nkDay > lastDayOfMonth) {
			    nkDay = lastDayOfMonth + '';
			}
		}
		var d;
		if (regexp.test(ary[0])) {
			d = ary[0].replace(regexp,nkDay);
		} else {
			if (m_nk) {
				if (_testFunction(error)) {
					error('month required');
				}
				return null;
			}
			d = ary[0];
		}
		var date = JSJoda.LocalDate.of(+y,+m,+d);
		if (date && (date.year() == +y) && (+y >= 1900) && date.monthValue() == m && date.dayOfMonth() == +d) {
		    return date;
		} else {
		    if (_testFunction(error)) {
			    error('invalid date');
			}
			return null;
		}
	}

	function _parseTimeCustom(input,error,nkMinute,locale) {
	    if (input == null || input.length == 0) {
		    if (_testFunction(error)) {
			    error('time is empty');
			}
			return null;
		}
		var ary = input.split(_getLocalizedMessage('customTimeSeparator',locale));
	    if (ary == null || ary.length != 2 || ary[0].length == 0 || ary[1].length == 0) {
		    if (_testFunction(error)) {
			    error('wrong time format');
	        }
			return null;
	    }
		var regexp = _getLocalizedMessage('customPartialTimePartRegExp',locale);
		var minuteRequired = (typeof(minuteRequired) == typeof(true) ? minuteRequired : (minuteRequired != null && minuteRequired == "required"));
		if (regexp.test(ary[0])) {
			if (regexp.test(ary[1]) && !minuteRequired) {

			} else {
			    if (_testFunction(error)) {
				    error('hour required');
		        }
			}
			return null;
	    }
		var h = ary[0];
		if (!minuteRequired && (nkMinute == null || nkMinute.length == 0)) {
		    nkMinute = '00';
		} else if (minuteRequired && regexp.test(ary[1])) {
			if (_testFunction(error)) {
				error('minute required');
			}
			return null;
		}
		var m = ary[1].replace(regexp,nkMinute);
		if (+h > 23 || +h < 0 || +m > 59 || +m < 0) {
		    if (_testFunction(error)) {
			    error('invalid time');
	        }
			return null;
	    } else {
	    	return JSJoda.LocalTime.of(+h,+m);
	    }
	}

	function _setVariable(variable, value, apply) {
	    var inputFieldVar = _getInputFieldVariable(variable);
		if (inputFieldVar) {
		    inputFieldVar.oldValue = _cloneJSON( inputFieldVar.value);
			_setInputFieldVariableValue(inputFieldVar.value, value);
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			    _debugVariableValue("user code sets variable value ", inputFieldVar);
			}
			if (apply) {
			    _applyCalculatedValue(inputFieldVar);
			} else {
			    _refreshInputFieldVariables(inputFieldVar.value.jsVariableName,inputFieldVar.value.index);
			}
		}
	}
	
	function _getEnteredValue(variable,index) {
	    var inputFieldVar = _getInputFieldVariable(variable, index);
		if (inputFieldVar) {
			return _getInputFieldVariableValue(inputFieldVar.enteredValue);
		}
		return null;
	}

	function _setOutput(variable, output) {
	    var inputFieldVar = _getInputFieldVariable(variable);
		if (inputFieldVar) {
		    inputFieldVar.oldOutput = inputFieldVar.output;
			inputFieldVar.output = output;
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			    console.log("user code sets variable output " + _debugVarName(inputFieldVar));
			}
			_printInputFieldVariableOutput(inputFieldVar);
		}
	}

	function _setInputFieldVariableValue(inputFieldVariableValue, newValue) {
		if (inputFieldVariableValue != null) {
			switch (inputFieldVariableValue.inputFieldType) {
			case "SINGLE_LINE_TEXT":
			case "MULTI_LINE_TEXT":
			case "AUTOCOMPLETE":
				inputFieldVariableValue.textValue = (newValue == null ? null : ('' + newValue));
				break;
			case "SKETCH":
				inputFieldVariableValue.inkValues = newValue.ink;
				newValue = newValue.ids;
			case "SELECT_ONE_DROPDOWN":
			case "SELECT_ONE_RADIO_H":
			case "SELECT_ONE_RADIO_V":
			case "SELECT_MANY_H":
			case "SELECT_MANY_V":
				if (!(newValue instanceof Array)) {
					newValue = (typeof newValue === 'string') ? parseInt(newValue) : newValue;
					if (isNaN(newValue)) {
						newValue = [];
					} else {
						newValue = [ newValue ];
					}
				}
				for ( var i = 0; i < newValue.length; i++) {
					if (typeof newValue[i] === 'string') {
						if (!isNaN(parseInt(newValue[i]))) {
							newValue[i] = newValue[i];
						}
					}
				}
				inputFieldVariableValue.selectionValueIds = newValue;
				break;
			case "CHECKBOX":
				inputFieldVariableValue.booleanValue = !!newValue;
				break;
			case "INTEGER":
				inputFieldVariableValue.longValue = (typeof newValue === 'string') ? (isNaN(parseInt(newValue)) ? null : parseInt(newValue)) : newValue;
				break;
			case "FLOAT":
				inputFieldVariableValue.floatValue = (typeof newValue === 'string') ? (isNaN(parseFloat(newValue)) ? null : parseFloat(newValue)) : newValue;
				break;
			case "DATE":
				if (typeof newValue === 'string') {
					inputFieldVariableValue.dateValue = _parseDate(newValue,INPUT_DATE_PATTERN);
				} else if (newValue instanceof JSJoda.LocalDate) {
					inputFieldVariableValue.dateValue = newValue;
				} else if (newValue instanceof Date) {
					inputFieldVariableValue.dateValue = JSJoda.LocalDate.from(JSJoda.nativeJs(newValue));
				} else {
					inputFieldVariableValue.dateValue = null;
				}
				break;
			case "TIME":
				if (typeof newValue === 'string') {
					inputFieldVariableValue.timeValue = _parseTime(newValue,INPUT_TIME_PATTERN);
				} else if (newValue instanceof JSJoda.LocalTime) {
					inputFieldVariableValue.timeValue = newValue;
				} else if (newValue instanceof Date) {
					inputFieldVariableValue.timeValue = JSJoda.LocalTime.from(JSJoda.nativeJs(newValue));
				} else {
					inputFieldVariableValue.timeValue = null;
				}
				break;
			case "TIMESTAMP":
				if (typeof newValue === 'string') {
					inputFieldVariableValue.timestampValue = _parseDateTime(newValue,INPUT_DATETIME_PATTERN);
				} else if (newValue instanceof JSJoda.LocalDateTime) {
					inputFieldVariableValue.timestampValue = JSJoda.ZonedDateTime.of(
							newValue,
							JSJoda.ZoneId.of(SYSTEM_TIMEZONE_ID)
					);
				} else if (newValue instanceof JSJoda.ZonedDateTime) {
					inputFieldVariableValue.timestampValue = newValue;
				} else if (newValue instanceof Date) {
					inputFieldVariableValue.timestampValue = JSJoda.ZonedDateTime.of(
							JSJoda.LocalDateTime.from(JSJoda.nativeJs(newValue)),
							JSJoda.ZoneId.of(SYSTEM_TIMEZONE_ID)
					);
				} else {
					inputFieldVariableValue.timestampValue = null;
				}
				break;				
			default:

			}
		}
	}

	function _debugVarName(inputFieldVariable) {
		if (inputFieldVariable != null) {
			var value = inputFieldVariable.value;
			if (value != null) {
				var varName = "'" + value.jsVariableName;
				if (value.series) {
					varName += "[" + value.index + "]";
				}
				varName += "'";
				return varName;
			}
		}
		return '';
	}

	function _debugVariableValue(prefix,inputFieldVariable) {
		if (inputFieldVariable != null) {
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 3) {
				console.log(prefix + _debugVarName(inputFieldVariable) + " = " + JSON.stringify(_getInputFieldVariableValue(inputFieldVariable.value)));
			}
		}
	}

	function _getInputFieldVariableValue(inputFieldVariableValue) {
		if (inputFieldVariableValue != null) {
			switch (inputFieldVariableValue.inputFieldType) {
			case "SINGLE_LINE_TEXT":
			case "MULTI_LINE_TEXT":
			case "AUTOCOMPLETE":
				return inputFieldVariableValue.textValue;
			case "SELECT_ONE_DROPDOWN":
			case "SELECT_ONE_RADIO_H":
			case "SELECT_ONE_RADIO_V":
			case "SELECT_MANY_H":
			case "SELECT_MANY_V":
				return inputFieldVariableValue.selectionValueIds;
			case "CHECKBOX":
				return inputFieldVariableValue.booleanValue;
			case "INTEGER":
				return inputFieldVariableValue.longValue;
			case "FLOAT":
				return inputFieldVariableValue.floatValue;
			case "DATE":
				return inputFieldVariableValue.dateValue;
			case "TIME":
				return inputFieldVariableValue.timeValue;
			case "TIMESTAMP":
				return inputFieldVariableValue.timestampValue;
			case "SKETCH":
				var result = {};
				result.ink = inputFieldVariableValue.inkValues;
				result.ids = inputFieldVariableValue.selectionValueIds;
				return result;
			default:

			}
		}
		return null;
	}

	function _equalInputFieldVariable(inputFieldVariable) {
		if (inputFieldVariable != null) {
			var value = inputFieldVariable.value;
			var enteredValue = inputFieldVariable.enteredValue;
			if (value != null && enteredValue != null) {
				switch (value.inputFieldType) {
				case "SINGLE_LINE_TEXT":
				case "MULTI_LINE_TEXT":
				case "AUTOCOMPLETE":
					return value.textValue === enteredValue.textValue || ((value.textValue == null || value.textValue.length == 0) && (enteredValue.textValue == null || enteredValue.textValue.length == 0));
				case "SELECT_ONE_DROPDOWN":
				case "SELECT_ONE_RADIO_H":
				case "SELECT_ONE_RADIO_V":
				case "SELECT_MANY_H":
				case "SELECT_MANY_V":
					return _selectionSetValueIdsEqual(value.selectionValueIds, enteredValue.selectionValueIds);
				case "CHECKBOX":
					return value.booleanValue == enteredValue.booleanValue;
				case "INTEGER":
					return value.longValue == enteredValue.longValue;
				case "FLOAT":
					return Math.abs(value.floatValue - enteredValue.floatValue) <= floatEpsilon;
				case "DATE":
					return _dateEqual(value.dateValue, enteredValue.dateValue);
				case "TIME":
					return _dateEqual(value.timeValue, enteredValue.timeValue);
				case "TIMESTAMP":
					return _dateEqual(value.timestampValue, enteredValue.timestampValue);
				case "SKETCH":
					return value.inkValues == enteredValue.inkValues && _selectionSetValueIdsEqual(value.selectionValueIds, enteredValue.selectionValueIds);
				default:

				}
			}
		}
		return null;
	}

	function _dateEqual(date1, date2) {
		if (date1 != null && date2 != null) {
			return date1.equals(date2);
		} else if (date1 == null && date2 != null) {
			return false;
		} else if (date1 != null && date2 == null) {
			return false;
		} else {
			return true;
		}
	}
	
	function _selectionSetValueIdsEqual(ids1, ids2) {
		var idMap1 = {};
		var idCount1 = 0;
		if (ids1 != null) {
			for ( var i = 0; i < ids1.length; i++) {
				idMap1[ids1[i]] = true;
				idCount1++;
			}
		}
		var idMap2 = {};
		var idCount2 = 0;
		if (ids2 != null) {
			for ( var i = 0; i < ids2.length; i++) {
				idMap2[ids2[i]] = true;
				idCount2++;
			}
		}
		if (idCount1 != idCount2) {
			return false;
		}
		for ( var id in idMap1) {
			if (!(id in idMap2)) {
				return false;
			}
		}
		return true;
	}

	function handleInitInputFieldVariables(xhr, status, args) {

		if (_testFlag(args, AJAX_OPERATION_SUCCESS)) {
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
				console.log("############### INITIALIZE SYMBOL TABLE ###############");
			}

			resetInputFieldVariables();
			if (_testPropertyExists(args, AJAX_FIELD_DELTA_ERROR_MESSAGE_ID)) {
				errorMessageId = args[AJAX_FIELD_DELTA_ERROR_MESSAGE_ID];
			}
			if (errorMessageId != null && errorMessageId.length > 0) {
				hideErrorMessage(errorMessageId);
			}

			_initInputFieldVars(args);
			_initInputFieldVariableValues(args);
			_processInputFieldVariableValues();
			return _updateInputFieldVariableOutputs();
		} else {
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
				console.log("############### SERVER-SIDE ERROR, SKIP ###############");
			}
			return null;
		}

	}

	function resetInputFieldVariables() {
		for (var variableName in inputFieldVariableMap) { //help GC..
			var series = inputFieldVariableMap[variableName];
			if (series instanceof Array) {
				for (var index = 0; index < series.length; index++) {
					if (index in series) {
						series[index].series = null;
					}
				}
			}
		}
		inputFieldVariableMap = {};
		inputFieldVars = {};
		errorMessageId = null;
		silent = false;
		if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			console.log("symbol table cleared");
		}
	}

	function _decode(json) {
		if (FIELD_CALCULATION_DECODE_BASE64) {
			json = decodeBase64(json);
		}
		return JSON.parse(json);
	}

	function _initInputFieldVars(args) {

		if (_testPropertyExists(args, AJAX_INPUT_FIELD_PROBAND_BASE64)) {
			inputFieldVars.proband = _decode(args[AJAX_INPUT_FIELD_PROBAND_BASE64]);
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_TRIAL_BASE64)) {
			inputFieldVars.trial = _decode(args[AJAX_INPUT_FIELD_TRIAL_BASE64]);
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64)) {
			inputFieldVars.probandAddresses = _decode(args[AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64]);
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64)) {
			var probandListEntryTagValues = _decode(args[AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64]);
			inputFieldVars.tagValues = {};
			if (probandListEntryTagValues != null) {
				for (var i = 0; i < probandListEntryTagValues.length; i++) {
					_sanitizeJsonValues(probandListEntryTagValues[i]);
					inputFieldVars.tagValues[probandListEntryTagValues[i].position] = probandListEntryTagValues[i];
				}
			}
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64)) {
			inputFieldVars.probandListEntry = _decode(args[AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64]);
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64)) {
			inputFieldVars.visitScheduleItems = _decode(args[AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64]);
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64)) {
			inputFieldVars.probandGroups = _decode(args[AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64]);
		}
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_ACTIVE_USER_BASE64)) {
			inputFieldVars.activeUser = _decode(args[AJAX_INPUT_FIELD_ACTIVE_USER_BASE64]);
		}

        inputFieldVars.locale = null;
        if (_testPropertyExists(args, AJAX_INPUT_FIELD_ACTIVE_USER_BASE64)) {
			inputFieldVars.activeUser = _decode(args[AJAX_INPUT_FIELD_ACTIVE_USER_BASE64]);
			if (inputFieldVars.activeUser != null) {
			    inputFieldVars.locale = inputFieldVars.activeUser.locale;
            }
		}
        if (_testPropertyExists(args, AJAX_INPUT_FIELD_LOCALE)) {
			inputFieldVars.locale = args[AJAX_INPUT_FIELD_LOCALE];
		}
        inputFieldVars.locale = _getLocale(inputFieldVars.locale);

	}

	function _sanitizeJsonValues(inputFieldVariableValue) {
        if (inputFieldVariableValue.inkValues != null) {
			inputFieldVariableValue.inkValues = _getStringFromUTF8Bytes(inputFieldVariableValue.inkValues);
		}
		if (inputFieldVariableValue.inputFieldSelectionSetValues != null) {
			inputFieldVariableValue.selectionSetValues = {};
			for (var j = 0; j < inputFieldVariableValue.inputFieldSelectionSetValues.length; j++) {
				if (inputFieldVariableValue.inputFieldSelectionSetValues[j].inkRegions != null) {
					inputFieldVariableValue.inputFieldSelectionSetValues[j].inkRegions = _getStringFromUTF8Bytes(inputFieldVariableValue.inputFieldSelectionSetValues[j].inkRegions);
				}
				inputFieldVariableValue.selectionSetValues[inputFieldVariableValue.inputFieldSelectionSetValues[j].id] = inputFieldVariableValue.inputFieldSelectionSetValues[j];
			}
		}

		if (typeof inputFieldVariableValue.dateValue === 'string') {
			if (inputFieldVariableValue.dateValue == null || inputFieldVariableValue.dateValue.length == 0) {
				inputFieldVariableValue.dateValue = null;
			} else {
				try {
					inputFieldVariableValue.dateValue = JSJoda.LocalDateTime.parse(
							inputFieldVariableValue.dateValue,
							JSJoda.DateTimeFormatter.ofPattern(INPUT_JSON_DATETIME_PATTERN)).toLocalDate();
				} catch (e) {
					inputFieldVariableValue.dateValue = null;
				}
			}
		} else if (inputFieldVariableValue.dateValue instanceof Date) {
			inputFieldVariableValue.dateValue = JSJoda.LocalDate.from(JSJoda.nativeJs(inputFieldVariableValue.dateValue));
		}
		
		if (typeof inputFieldVariableValue.timeValue === 'string') {
			if (inputFieldVariableValue.timeValue == null || inputFieldVariableValue.timeValue.length == 0) {
				inputFieldVariableValue.timeValue = null;
			} else {
				try {
					inputFieldVariableValue.timeValue = JSJoda.LocalDateTime.parse(
							inputFieldVariableValue.timeValue,
							JSJoda.DateTimeFormatter.ofPattern(INPUT_JSON_DATETIME_PATTERN)).toLocalTime();
				} catch (e) {
					inputFieldVariableValue.timeValue = null;
				}
			}
		} else if (inputFieldVariableValue.timeValue instanceof Date) {
			inputFieldVariableValue.timeValue = JSJoda.LocalTime.from(JSJoda.nativeJs(inputFieldVariableValue.timeValue));
		}
		
		if (typeof inputFieldVariableValue.timestampValue === 'string') {
			if (inputFieldVariableValue.timestampValue == null || inputFieldVariableValue.timestampValue.length == 0) {
				inputFieldVariableValue.timestampValue = null;
			} else {
				try {
					inputFieldVariableValue.timestampValue = JSJoda.ZonedDateTime.of(
							JSJoda.LocalDateTime.parse(inputFieldVariableValue.timestampValue,
							JSJoda.DateTimeFormatter.ofPattern(INPUT_JSON_DATETIME_PATTERN)),
							JSJoda.ZoneId.of(SYSTEM_TIMEZONE_ID)
					);
				} catch (e) {
					inputFieldVariableValue.timestampValue = null;
				}
			}
		} else if (inputFieldVariableValue.timestampValue instanceof Date) {
			inputFieldVariableValue.timestampValue = JSJoda.ZonedDateTime.of(
					JSJoda.LocalDateTime.from(JSJoda.nativeJs(inputFieldVariableValue.timestampValue)),
					JSJoda.ZoneId.of(SYSTEM_TIMEZONE_ID)
			);
		}
		
		if (typeof inputFieldVariableValue.userTimeZone === 'string') {
			inputFieldVariableValue.userTimeZone = !!inputFieldVariableValue.userTimeZone;
		}

		if (typeof inputFieldVariableValue.floatValue === 'string') {
			inputFieldVariableValue.floatValue = parseFloat(inputFieldVariableValue.floatValue);
		}

		if (typeof inputFieldVariableValue.longValue === 'string') {
			inputFieldVariableValue.longValue = parseInt(inputFieldVariableValue.longValue);
		}

		if (typeof inputFieldVariableValue.booleanValue === 'string') {
			inputFieldVariableValue.booleanValue = !!inputFieldVariableValue.booleanValue;
		}

		if (!inputFieldVariableValue.series) {
			inputFieldVariableValue.series = false;
			inputFieldVariableValue.index = null;
		}

		if (inputFieldVariableValue.category != null) {
			inputFieldVariableValue.section = inputFieldVariableValue.category;
		}

    }

	function _initInputFieldVariableValues(args) {
		if (_testPropertyExists(args, AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64)) {
			var inputFieldVariableValues = _decode(args[AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64]);
			var msg = "merged " + inputFieldVariableValues.length + " variable values";
			var added = 0;
			var updated = 0;
			var oldMapSize = _getInputFieldVariableMapSize();
			var newMapSize;
			var cs = new CommentStripper();
			for (var i = 0; i < inputFieldVariableValues.length; i++) {
				var inputFieldVariableValue = inputFieldVariableValues[i];
				if (inputFieldVariableValue.jsVariableName != null && inputFieldVariableValue.jsVariableName.length > 0) {
					_sanitizeJsonValues(inputFieldVariableValue);

					var inputFieldVariable = {};
					inputFieldVariableValue.jsValueExpression = cs.strip(inputFieldVariableValue.jsValueExpression);
					inputFieldVariableValue.jsOutputExpression = cs.strip(inputFieldVariableValue.jsOutputExpression);
					if (_testPropertyExists(inputFieldVariableValue, "inquiryId")) {
						inputFieldVariable.outputId = INPUT_FIELD_OUTPUT_ID_PREFIX + inputFieldVariableValue.inquiryId;
					} else if (_testPropertyExists(inputFieldVariableValue, "tagId")) {
						inputFieldVariable.outputId = INPUT_FIELD_OUTPUT_ID_PREFIX + inputFieldVariableValue.tagId;
					} else if (_testPropertyExists(inputFieldVariableValue, "ecrfFieldId")) {
						inputFieldVariable.outputId = INPUT_FIELD_OUTPUT_ID_PREFIX + inputFieldVariableValue.ecrfFieldId;
						if (inputFieldVariableValue.series) {
							inputFieldVariable.outputId += INPUT_FIELD_OUTPUT_ID_INDEX_SEPARATOR + inputFieldVariableValue.index;
						}
					}

					if (_testPropertyExists(inputFieldVariableValue, "inquiryId")) {
						inputFieldVariable.widgetVarName = INPUT_FIELD_WIDGET_VAR_PREFIX + inputFieldVariableValue.inquiryId;
					} else if (_testPropertyExists(inputFieldVariableValue, "tagId")) {
						inputFieldVariable.widgetVarName = INPUT_FIELD_WIDGET_VAR_PREFIX + inputFieldVariableValue.tagId;
					} else if (_testPropertyExists(inputFieldVariableValue, "ecrfFieldId")) {
						inputFieldVariable.widgetVarName = INPUT_FIELD_WIDGET_VAR_PREFIX + inputFieldVariableValue.ecrfFieldId;
						if (inputFieldVariableValue.series) {
							inputFieldVariable.widgetVarName += INPUT_FIELD_WIDGET_VAR_INDEX_SEPARATOR + inputFieldVariableValue.index;
						}
					}

					inputFieldVariable.processed = false;
					inputFieldVariable.valueErrorMessage = null;
					inputFieldVariable.outputErrorMessage = null;
					inputFieldVariable.output = null;
					inputFieldVariable.oldOutput = null;
					inputFieldVariable.delta = false;
					inputFieldVariable.value = inputFieldVariableValue;
					inputFieldVariable.oldValue = _cloneJSON( inputFieldVariableValue);

					var mask = {};
					//mask global properties:
					for ( var p in this) {
						mask[p] = undefined;
					}
					_exportExpressionUtils(mask);
					inputFieldVariable.mask = mask;

					if (inputFieldVariableValue.series) {
						if (inputFieldVariableValue.jsVariableName in inputFieldVariableMap) {
							if (inputFieldVariableValue.index in inputFieldVariableMap[inputFieldVariableValue.jsVariableName]) {
								if (FIELD_CALCULATION_DEBUG_LEVEL >= 2) {
									console.log("variable " + _debugVarName(inputFieldVariable) + " updated");
								}
								updated++;
							} else {
								if (FIELD_CALCULATION_DEBUG_LEVEL >= 2) {
									console.log("variable " + _debugVarName(inputFieldVariable) + " added");
								}
								added++;
							}
						} else {
							inputFieldVariableMap[inputFieldVariableValue.jsVariableName] = [];
							if (FIELD_CALCULATION_DEBUG_LEVEL >= 2) {
								console.log("variable " + _debugVarName(inputFieldVariable) + " added");
							}
							added++;
						}
						inputFieldVariable.series = inputFieldVariableMap[inputFieldVariableValue.jsVariableName];
						inputFieldVariableMap[inputFieldVariableValue.jsVariableName][inputFieldVariableValue.index] = inputFieldVariable;
					} else {
						if (inputFieldVariableValue.jsVariableName in inputFieldVariableMap) {
							if (FIELD_CALCULATION_DEBUG_LEVEL >= 2) {
								console.log("variable " + _debugVarName(inputFieldVariable) + " updated");
							}
							updated++;
						} else {
							if (FIELD_CALCULATION_DEBUG_LEVEL >= 2) {
								console.log("variable " + _debugVarName(inputFieldVariable) + " added");
							}
							added++;
						}
						inputFieldVariableMap[inputFieldVariableValue.jsVariableName] = inputFieldVariable;
					}
					inputFieldVariable.enteredValue = _cloneJSON( inputFieldVariableValue);
				}
			}
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
				newMapSize = _getInputFieldVariableMapSize();
				msg += ": " + added + " added, " + updated + " updated";
				console.log(msg);
				console.log("symbol table size: " + oldMapSize + " before, " + newMapSize + " after");
			}
		}
	}

	function handleUpdateInputFieldVariables(xhr, status, args) {

		if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
			console.log("############### UPDATE SYMBOL TABLE ###################");
		}

		_invalidateInputFieldVariableValues();

		_initInputFieldVariableValues(args);
		_processInputFieldVariableValues();
		return _updateInputFieldVariableOutputs();

	}

	function singleLineTextOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getValue());

	}

	function multiLineTextOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getValue());

	}

	function selectOneDropdownOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, [ widget.getValue() ]);

	}
	function selectOneRadioOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, [ widget.getValue() ]);

	}

	function selectManyOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getValue());

	}

	function autoCompleteOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getValue());

	}

	function checkBoxOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getValue());

	}

	function integerOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getValue());

	}

	function floatOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, _parseDecimal(widget.getValue()));

	}

	function dateOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getDate());

	}

	function timeOnChange(variableName, index, widget, outputId) {

		if (!silent) _inputFieldOnChange(variableName, index, widget.getTime());

	}

	function timestampOnChange(variableName, index, widget, outputId) {
		
		if (!silent) {
			var timestamp = widget.getDate();
			if (timestamp != null) {
				timestamp = JSJoda.LocalDateTime.from(JSJoda.nativeJs(timestamp));
				var inputFieldVariable = _getSeriesInputFieldVariable(variableName, index, false);
				if (inputFieldVariable != null && inputFieldVariable.value.userTimeZone) {
					timestamp = JSJoda.ZonedDateTime.of(
						timestamp,
						JSJoda.ZoneId.of(INPUT_TIMEZONE_ID)
					).withZoneSameInstant(JSJoda.ZoneId.of(SYSTEM_TIMEZONE_ID));
				} else {
					timestamp = JSJoda.ZonedDateTime.of(
						timestamp,
						JSJoda.ZoneId.of(SYSTEM_TIMEZONE_ID)
					);
				}
			}
			_inputFieldOnChange(variableName, index, timestamp);
		}

	}


	function sketchOnChange(variableName, index, widget, outputId) {

		if (!silent) {
			var inputFieldVariable = _getSeriesInputFieldVariable(variableName, index, false);
			var unpackedValue = Sketch.unpackValue(Sketch.getSketchValue(widget));
			var ids = [];
			if (inputFieldVariable) {
				var inputFieldSelectionSetValues = inputFieldVariable.value.inputFieldSelectionSetValues;
				var selectionSetValues = {};
				if (inputFieldSelectionSetValues) {
					for ( var i = 0; i < inputFieldSelectionSetValues.length; i++) {
						selectionSetValues[inputFieldSelectionSetValues[i].strokesId] = inputFieldSelectionSetValues[i];
					}
				}
				if (!(unpackedValue.ids instanceof Array)) {
					unpackedValue.ids = [ unpackedValue.ids ];
				}
				for ( var i = 0; i < unpackedValue.ids.length; i++) {
					if (unpackedValue.ids[i] && selectionSetValues[unpackedValue.ids[i]]) {
						ids[i] = selectionSetValues[unpackedValue.ids[i]].id;
					}
				}
			}
			_inputFieldOnChange(variableName, index, {
			    'ink' : unpackedValue.ink,
			    'ids' : ids
			});
		}

	}

	function _inputFieldOnChange(variableName, index, newValue) {
		var inputFieldVariable = _getSeriesInputFieldVariable(variableName, index, false);
		if (inputFieldVariable) {
			_setInputFieldVariableValue(inputFieldVariable.enteredValue, newValue);
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
				console.log("onchange " + _debugVarName(inputFieldVariable));
			}
			_refreshInputFieldVariables();
		}
	}

	function _inputFieldApplyCalculatedValue(variableName, index) {
		var inputFieldVariable = _getSeriesInputFieldVariable(variableName, index, false);
		if (inputFieldVariable) {
			inputFieldVariable.enteredValue = _cloneJSON( inputFieldVariable.value);
			inputFieldVariable.delta = false;
			if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
				console.log("apply calculated value " + _debugVarName(inputFieldVariable));
			}
			_refreshInputFieldVariables(variableName, index);
			return _getInputFieldVariableValue(inputFieldVariable.value);
		}
		return null;
	}

	function singleLineTextApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}

	function multiLineTextApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}

	function selectOneDropdownApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue ? newValue[0] : null);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function selectOneRadioApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue ? newValue[0] : null);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}

	function selectManyApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function autoCompleteApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function checkBoxApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function integerApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(newValue);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function floatApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setValue(_formatDecimal(newValue));
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function dateApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setDate(newValue != null ? JSJoda.convert(newValue).toDate() : null);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function timeApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		widget.setTime(newValue != null ? JSJoda.convert(JSJoda.LocalDateTime.of(1970,1,1,newValue.hour(),newValue.minute())).toDate() : null);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	function timestampApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		var timestamp = null;
		if (newValue != null) {
			var inputFieldVariable = _getSeriesInputFieldVariable(variableName, index, false);
			if (inputFieldVariable != null && inputFieldVariable.value.userTimeZone) {
				timestamp = newValue.withZoneSameInstant(JSJoda.ZoneId.of(INPUT_TIMEZONE_ID));
			} else {
				timestamp = newValue;
			}
			timestamp = JSJoda.convert(timestamp.toLocalDateTime()).toDate();
		}
		widget.setDate(timestamp);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}
	
	
	function sketchApplyCalculatedValue(variableName, index, widget, sourceId, rowId) {
		silent = true;
		var newValue = _inputFieldApplyCalculatedValue(variableName, index);
		Sketch.setSketchValue(widget,newValue.ink);
		silent = false;
		if (sourceId != null && sourceId.length > 0) {
			ajaxRequest(sourceId, sourceId, null, null);
		}
	}

	function _applyCalculatedValue(inputFieldVar) {
		if (inputFieldVar != null) {
			var widget = window[inputFieldVar.widgetVarName];
			var value = inputFieldVar.value;
			if (value != null && widget != null) {
				var sourceId = widget.id;
				var variableName = value.jsVariableName;
				var index = value.index;
				switch (value.inputFieldType) {
					case "SINGLE_LINE_TEXT":
						singleLineTextApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "MULTI_LINE_TEXT":
						multiLineTextApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "AUTOCOMPLETE":
						autoCompleteApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "SELECT_ONE_DROPDOWN":
						selectOneDropdownApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "SELECT_ONE_RADIO_H":
					case "SELECT_ONE_RADIO_V":
						selectOneRadioApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "SELECT_MANY_H":
					case "SELECT_MANY_V":
						selectManyApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "CHECKBOX":
						checkBoxApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "INTEGER":
						integerApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "FLOAT":
						floatApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "DATE":
						dateApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "TIME":
						timeApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "TIMESTAMP":
						timestampApplyCalculatedValue(variableName,index,widget,sourceId);
						break;
					case "SKETCH":
						sketchApplyCalculatedValue(variableName,index,widget,widget.inputId());
						break;
					default:

				}
			}
		}
	}

	function _getStringFromUTF8Bytes(input) {
		var i, str = '';

		for (i = 0; i < input.length; i++) {
			str += '%' + ('0' + input[i].toString(16)).slice(-2);
		}
		return decodeURIComponent(str);
	}

	FieldCalculation.handleInitInputFieldVariables = handleInitInputFieldVariables;
	FieldCalculation.resetInputFieldVariables = resetInputFieldVariables;
	FieldCalculation.handleUpdateInputFieldVariables = handleUpdateInputFieldVariables;

	FieldCalculation.singleLineTextOnChange = singleLineTextOnChange;
	FieldCalculation.multiLineTextOnChange = multiLineTextOnChange;
	FieldCalculation.selectOneDropdownOnChange = selectOneDropdownOnChange;
	FieldCalculation.selectOneRadioOnChange = selectOneRadioOnChange;
	FieldCalculation.selectManyOnChange = selectManyOnChange;
	FieldCalculation.autoCompleteOnChange = autoCompleteOnChange;
	FieldCalculation.checkBoxOnChange = checkBoxOnChange;
	FieldCalculation.integerOnChange = integerOnChange;
	FieldCalculation.floatOnChange = floatOnChange;
	FieldCalculation.dateOnChange = dateOnChange;
	FieldCalculation.timestampOnChange = timestampOnChange;
	FieldCalculation.timeOnChange = timeOnChange;
	FieldCalculation.sketchOnChange = sketchOnChange;

	FieldCalculation.singleLineTextApplyCalculatedValue = singleLineTextApplyCalculatedValue;
	FieldCalculation.multiLineTextApplyCalculatedValue = multiLineTextApplyCalculatedValue;
	FieldCalculation.selectOneDropdownApplyCalculatedValue = selectOneDropdownApplyCalculatedValue;
	FieldCalculation.selectOneRadioApplyCalculatedValue = selectOneRadioApplyCalculatedValue;
	FieldCalculation.selectManyApplyCalculatedValue = selectManyApplyCalculatedValue;
	FieldCalculation.autoCompleteApplyCalculatedValue = autoCompleteApplyCalculatedValue;
	FieldCalculation.checkBoxApplyCalculatedValue = checkBoxApplyCalculatedValue;
	FieldCalculation.integerApplyCalculatedValue = integerApplyCalculatedValue;
	FieldCalculation.floatApplyCalculatedValue = floatApplyCalculatedValue;
	FieldCalculation.dateApplyCalculatedValue = dateApplyCalculatedValue;
	FieldCalculation.timestampApplyCalculatedValue = timestampApplyCalculatedValue;
	FieldCalculation.timeApplyCalculatedValue = timeApplyCalculatedValue;
	FieldCalculation.sketchApplyCalculatedValue = sketchApplyCalculatedValue;

	_exportExpressionUtils(FieldCalculation);

	if (FIELD_CALCULATION_DEBUG_LEVEL >= 1) {
		console.log("field calculation utilities loaded");
	}

})(window.FieldCalculation);