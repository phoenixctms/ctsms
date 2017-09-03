var oldSearchTabIndex = 0;

function handleSearchTabChange(index) {

	_deleteIntermediateSetsChartTooltip();
	// FieldCalculation.resetInputFieldVariables();
	switch (index) {
	case 0:

		switch (oldSearchTabIndex) {
		case 0:
			break;
		case 1:
			// changeCriteriaByTag();
			break;
		case 2:
			// changeCriteriaByJournalEntry();
			break;
		default:
			break;
		}

		break;

	case 1:
		changeIntermediateSets();
		break;

	case 2:
		changeCriteriaJournalEntry();
		break;

	default:
		break;
	}
	oldSearchTabIndex = index;
	return true;

}

var droppingCriterion = false;
function handleCriterionDropped(xhr, status, args) {

	droppingCriterion = false;

}

function criterionRowHandleDrop(event, ui) {

	if (!droppingCriterion) {
		droppingCriterion = true;
		var indexRe = /(\d+)$/;
		var sourceId = ui.draggable.attr("id");
		var targetId = this.jq.attr("id");

		var index_source = null;
		if (indexRe.test(sourceId)) {
			index_source = indexRe.exec(sourceId);
		}
		var index_target = null;
		if (indexRe.test(targetId)) {
			index_target = indexRe.exec(targetId);
		}
		if (index_source != null && index_target != null) {
			var params = {};
			params[SOURCE_INDEX] = index_source[1];
			params[TARGET_INDEX] = index_target[1];
			dropCriterionRow(prepareRemoteCommandParameters(params));
		}
	}
}

function handleCriteriaChanged(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_WINDOW_TITLE_BASE64) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		if (!args[AJAX_ROOT_ENTITY_CREATED]) {
			searchTabView.select(0);
		}
		enableTabs(searchTabView, 2, args[AJAX_ROOT_ENTITY_CREATED]);
		handleUpdateCriteriaTabTitles(xhr, status, args);
		if (!_testFlag(args, AJAX_PICKER)) {
			document.title = decodeBase64(args[AJAX_WINDOW_TITLE_BASE64]);
			// changeCriteriaJournalEntry();
		}
	}

}

function handleUpdateCriteriaTabTitles(xhr, status, args) {

	if (_testPropertyExists(args, AJAX_OPERATION_SUCCESS) && _testPropertyExists(args, AJAX_ROOT_ENTITY_CREATED)) {
		searchTabView.emphasizeTab(0, _testFlag(args, AJAX_OPERATION_SUCCESS) && !args[AJAX_ROOT_ENTITY_CREATED]);
	}

	if (_testPropertyExists(args, AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64) && _testPropertyExists(args, AJAX_CRITERIA_JOURNAL_ENTRY_COUNT)) {
		searchTabView.setTabTitle(2, decodeBase64(args[AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64]));
		searchTabView.emphasizeTab(2, args[AJAX_CRITERIA_JOURNAL_ENTRY_COUNT] == 0);
	}

}

var intermediateSetsChart = null;
var intermediateSetsChartTooltip = null;
var cssColorMap = null;
var canvasD3;
var tooltipD3;
var canvas;
var tooltip;

function handleUpdateIntermediateSets(xhr, status, args) {

	if (_testFlag(args, AJAX_OPERATION_SUCCESS)) {
		var intermediateSets = null;
		if (_testPropertyExists(args, AJAX_INTERMEDIATE_SETS_BASE64)) {
			intermediateSets = JSON.parse(decodeBase64(args[AJAX_INTERMEDIATE_SETS_BASE64]));
		}
		// console.log(intermediateSets);

		var criterionRowColors = null;
		if (_testPropertyExists(args, AJAX_CRITERION_ROW_COLORS_BASE64)) {
			criterionRowColors = JSON.parse(decodeBase64(args[AJAX_CRITERION_ROW_COLORS_BASE64]));
		}
		// console.log(criterionRowColors);

		var criterionTieMap = null;
		if (_testPropertyExists(args, AJAX_CRITERION_TIE_MAP_BASE64)) {
			criterionTieMap = JSON.parse(decodeBase64(args[AJAX_CRITERION_TIE_MAP_BASE64]));
		}

		var resultItemLabel;
		if (_testPropertyExists(args, AJAX_CRITERIA_RESULT_ITEM_LABEL_BASE64)) {
			resultItemLabel = decodeBase64(args[AJAX_CRITERIA_RESULT_ITEM_LABEL_BASE64]);
		} else {
			resultItemLabel = "item(s)";
		}

		_createIntermediateSetChart();
		_updateIntermediateSetChartCanvasSize(intermediateSets);

		// console.log(cssColorMap);
		var maps = _updateIntermediateSetChart(intermediateSets, criterionRowColors, criterionTieMap, resultItemLabel);
		// console.log(maps);
		_styleIntermediateSetChart(maps);
		_registerIntermediateSetChartTooltip(maps);
	}

}

function _formatCountCircle(count) {
	return numeral(count).format('0[.]0a');
}
function _formatCountTooltipText(count) {
	return numeral(count).format('0,0');
}

function _updateIntermediateSetChart(intermediateSets, criterionColorClasses, criterionTieMap, resultItemLabel) {
	// var sets = [ {sets: ['A'], size: 12},
	// {sets: ['B'], size: 12},
	// {sets: ['A','B'], size: 2}];
	var colorMap = {};
	var tooltipMap = {};
	var isIntermediateMap = {};
	var intermediateSetMap = {};
	var maps = {
		"criterionTieMap" : criterionTieMap
	};
	if (intermediateSetsChart != null) {
	if (intermediateSets != null && intermediateSets.length > 0) {
		var sets = [];
		for ( var i = 0; i < intermediateSets.length; i++) {
			var intermediateSet = intermediateSets[i];
			if (intermediateSet.operator != null) {
				var a = intermediateSet.a;
				var b = intermediateSet.b;
				var aSet = [ 'a' + i ];
				var bSet = [ 'b' + i ];
				var abSet = aSet.concat(bSet);
				colorMap[aSet.toString()] = _getSelectColor(a, criterionColorClasses);
				colorMap[bSet.toString()] = _getSelectColor(b, criterionColorClasses);
				colorMap[abSet.toString()] = (a.selectStatementCount == 1 ? colorMap[aSet.toString()]
				        : (b.selectStatementCount == 1 ? colorMap[bSet.toString()] : _getCssColor('ctsms-search-intermediateset-intermediate-color')));

				isIntermediateMap[aSet.toString()] = (a.selectStatementCount > 1);
				isIntermediateMap[bSet.toString()] = (b.selectStatementCount > 1);
				isIntermediateMap[abSet.toString()] = (a.selectStatementCount > 1) || (b.selectStatementCount > 1);

				sets.push({
				    sets : aSet,
				    size : intermediateSet.aCount,
				    label : a.setOperationExpression + ": " + _formatCountCircle(intermediateSet.aCount)
				});
				sets.push({
				    sets : bSet,
				    size : intermediateSet.bCount,
				    label : b.setOperationExpression + ": " + _formatCountCircle(intermediateSet.bCount)
				});
				sets.push({
				    sets : abSet,
				    size : intermediateSet.intersectionCount,
				    label : ((intermediateSet.intersectionCount != intermediateSet.aCount && intermediateSet.intersectionCount != intermediateSet.bCount) ?
				            _formatCountCircle(intermediateSet.intersectionCount) : "")
				});
				intermediateSetMap[intermediateSet.criteria.setOperationExpression] = intermediateSet;
			} else {
				var criteria = intermediateSet.criteria;
				var criteriaSet = [ 'criteria' + i ];
				colorMap[criteriaSet.toString()] = _getSelectColor(criteria, criterionColorClasses);
				isIntermediateMap[criteriaSet.toString()] = (criteria.selectStatementCount > 1);
				sets.push({
				    sets : criteriaSet,
				    size : intermediateSet.criteriaCount,
				    label : criteria.setOperationExpression + ": " + _formatCountCircle(intermediateSet.criteriaCount)
				});
			}
		}
		maps["intermediateSetMap"] = intermediateSetMap;

		for ( var i = 0; i < intermediateSets.length; i++) {
			var intermediateSet = intermediateSets[i];
			if (intermediateSet.operator != null) {
				var a = intermediateSet.a;
				var b = intermediateSet.b;
				var aSet = [ 'a' + i ];
				var bSet = [ 'b' + i ];
				var abSet = aSet.concat(bSet);
				tooltipMap[aSet.toString()] = _getIntermediateSetTooltipText(a, intermediateSet.aCount, resultItemLabel, maps, true);
				tooltipMap[bSet.toString()] = _getIntermediateSetTooltipText(b, intermediateSet.bCount, resultItemLabel, maps, true);
				tooltipMap[abSet.toString()] = _getIntermediateSetTooltipText(intermediateSet.intersection, intermediateSet.intersectionCount, resultItemLabel,
				        maps, false);
			} else {
				var criteria = intermediateSet.criteria;
				var criteriaSet = [ 'criteria' + i ];
				tooltipMap[criteriaSet.toString()] = _getIntermediateSetTooltipText(criteria, intermediateSet.criteriaCount, resultItemLabel, maps,
				        criteria.criterions.length > 0);
			}
		}

		// intermediateSetsChart.colours(function(label) {
		// return colorMap[label];
		// });
		// console.log(colorMap);
		// console.log(sets);
		// console.log(JSON.stringify(sets));
		canvasD3.datum(sets).call(intermediateSetsChart);
	} else {
		canvasD3.text('');
	}
	}
	maps["colorMap"] = colorMap;
	maps["tooltipMap"] = tooltipMap;
	maps["isIntermediateMap"] = isIntermediateMap;
	return maps;

}

function _getIntermediateSetTooltipText(criteria, count, resultItemLabel, maps, showExpression) {

	var result = criteria.setOperationExpression + "\n";
	if (maps.criterionTieMap != null) {
		var intermediateSet = maps.intermediateSetMap[criteria.setOperationExpression];
		if (intermediateSet != null && intermediateSet.operator != null) {
			var operator = maps.criterionTieMap[intermediateSet.operator.tieId];
			if (operator != null) {
				switch (operator.tie) {
				case 'UNION':
					result += _formatCountTooltipText(intermediateSet.aCount - intermediateSet.intersectionCount) + " + " + _formatCountTooltipText(intermediateSet.intersectionCount) + " + "
					        + _formatCountTooltipText(intermediateSet.bCount - intermediateSet.intersectionCount) + " = ";
					break;
				case 'EXCEPT':
					result += _formatCountTooltipText(intermediateSet.aCount) + " - " + _formatCountTooltipText(intermediateSet.intersectionCount) + " = ";
					break;
				default:
					break;
				}
			}
		}
	}
	result += _formatCountTooltipText(count) + " " + resultItemLabel;
	if (showExpression && criteria.criterionExpression.length > 0) {
		result += "\n\n" + criteria.criterionExpression;
	}
	return result;

}

function _deleteIntermediateSetsChartTooltip() {
	var intermediateSetsChartTooltip = document.getElementById('intermediateSetsChartTooltip');
	if (intermediateSetsChartTooltip != null) {
		intermediateSetsChartTooltip.parentNode.removeChild(intermediateSetsChartTooltip);
	}
}

function _createIntermediateSetChart() {
	canvas = document.getElementById('intermediateSetsChartCanvas');
	_deleteIntermediateSetsChartTooltip();
	tooltip = null;
	if (canvas != null) {
		canvasD3 = d3.select("#intermediateSetsChartCanvas");
		tooltipD3 = d3.select("body").append("div")
		.attr("id", "intermediateSetsChartTooltip")
		.attr("class", "ui-widget ctsms-intermediatesets-tooltip");
		tooltip = document.getElementById('intermediateSetsChartTooltip');
//		if (tooltip == null) {
//			// tooltipD3 = d3.select("body").append("div") //must be appendend
//			// to body, otherwise mouse coords are relative to tabview's bb
//			tooltipD3 = canvasD3.append("div").attr("id", "intermediateSetsChartTooltip").attr("class", "ui-widget ctsms-intermediatesets-tooltip");
//			tooltip = document.getElementById('intermediateSetsChartTooltip');
//			// tooltipD3 = d3.select("#intermediateSetsChartTooltip");
//		}
//		tooltip.setAttribute("style", "position:relative; left:0px; top:0px;");
		if (intermediateSetsChart == null) {
			// var padding = 10;
			intermediateSetsChart = venn.VennDiagram();
			intermediateSetsChart.padding(30); // border only
			intermediateSetsChart.duration(1000); // transition fx
			intermediateSetsChart.normalize(true); // true); // = true,
			intermediateSetsChart.wrap(true); // labels
			intermediateSetsChart.styled(false);
			// intermediateSetsChart.fontSize('20px'); // = null,
			intermediateSetsChart.orientation(2 * Math.PI / 6);
			// consistent operand order:
			intermediateSetsChart.orientationOrder(function(a, b) {
				return 0.0;
			}); // function (a, b) { return b.radius - a.radius; }
			intermediateSetsChart.layoutFunction(function(d) {
				return venn.venn(d, {
					initialLayout : venn.classicMDSLayout
				});
			});
			return 1;
		}
	}
	return 0;
}

function _updateIntermediateSetChartCanvasSize(intermediateSets) {
	if (intermediateSetsChart != null) {
		var rows = (intermediateSets != null && intermediateSets.length > 0 ? intermediateSets.length : 0);
		// canvas.setAttribute("style","height:" + height + "px");
		// canvas.style.height = height + "px";
		var bb = canvas.getBoundingClientRect();
		var height = Math.min(bb.width, Math.ceil(380.0 * Math.max(1.0, rows / 2.5)));
		// console.log(bb);
		intermediateSetsChart.width(bb.width);
		intermediateSetsChart.height(height);
	}
}

function _styleIntermediateSetChart(maps) {
	if (intermediateSetsChart != null) {
		// var abStrokeColor =
		// _getCssColor('ctsms-search-intermediateset-highlightstroke-color');
		d3.selectAll("#intermediateSetsChartCanvas .venn-area path").style("fill", function(d, i) {
			return maps.colorMap[d.sets.toString()];
		}).style("fill-opacity", function(d, i) {
			return d.sets.length == 1 ? 0.75 : 0.1;
		}).style("stroke-width", 2).style("stroke", function(d, i) {
			return maps.colorMap[d.sets.toString()];
		}).style("stroke-opacity", function(d, i) {
			return d.sets.length == 1 ? 1 : 0.4;
		}).style("font-size", "12px").style("font-weight", "100");
	}
}

function _registerIntermediateSetChartTooltip(maps) {
	if (intermediateSetsChart != null) {
		// add listeners to all the groups to display tooltip on mouseover
		//var svg = canvasD3.select("svg").node();
		var svgBb = null; //canvasD3.select("svg").node().getBoundingClientRect();
		var tooltipBb = null; //tooltip.getBoundingClientRect();
		var tooltipBbMap = {};
		// var abStrokeColor =
		// _getCssColor('ctsms-search-intermediateset-highlightstroke-color');
		var highlightCircleStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-stroke-color');
		var highlightCircleIntermediateStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-circle-intermediate-stroke-color');
		var highlightIntersectionStrokeColor = _getCssColor('ctsms-search-intermediateset-highlight-intersection-stroke-color');
		canvasD3.selectAll("g").on( //canvasD3.selectAll("venn-area").on(
		        "mouseover",
		        function(d, i) {
		        	var setId = d.sets.toString();
			        // console.log("mouseover: " + d.sets.toString());
			        // sort all the areas relative to the current item
			        venn.sortAreas(canvasD3, d);

			        // Display a tooltip with the current size
			        tooltipD3.transition().duration(400).style("opacity", .8);
			        tooltipD3.text(maps.tooltipMap[setId]);
			        // while (tooltip.childNodes.length >= 1) {
			        // tooltip.removeChild(tooltip.firstChild);
			        // }
			        // tooltip.appendChild(document.createTextNode(maps.tooltipMap[d.sets.toString()]));
			        if (svgBb == null) {
			        	svgBb = canvasD3.select("svg").node().getBoundingClientRect();
			        }
			        if (setId in tooltipBbMap) {
			        	tooltipBb = tooltipBbMap[setId];
			        } else {
			        	tooltipBb = tooltip.getBoundingClientRect();
			        	tooltipBbMap[setId] = tooltipBb;
			        }

			        // highlight the current path
			        var selection = d3.select(this).transition("tooltip_transition").duration(400);
			        selection.select("path").style(
			                "stroke",
			                function(d, i) {
				                return d.sets.length == 1 ? (maps.isIntermediateMap[d.sets.toString()] ? highlightCircleIntermediateStrokeColor
				                        : highlightCircleStrokeColor) : highlightIntersectionStrokeColor;
			                });
			        // .style("stroke-width", 3)
			        // .style("stroke-opacity", 1);
			        // .style("fill-opacity", function(d,i) { return
					// d.sets.length == 1 ? 1 : 0.4; })
			        // .style("stroke-width", 3)
			        // .style("stroke-opacity", function(d,i) { return
					// d.sets.length == 1 ? 0.75 : 0.1; })
		        })

		.on("mousemove", function() {

			// console.log(rect.top, rect.right, rect.bottom, rect.left);
			//var coords = d3.mouse(this);
			//tooltipD3.style("left", (svgBb.left + coords[0]) + "px")
			 //.style("top", (svgBb.top + coords[1] - tooltipBb.height) + "px");
			//.style("top", (svgBb.top + coords[1]) + "px");
			//tooltipD3.style("style", "position:relative; left:" + coords[0] + "px; top:" + coords[1] + "px;");
			var left;
			var top;
			//console.log(svgBb.width,svgBb.height,d3.event.pageX,d3.event.pageY);
			if (d3.event.pageX > svgBb.left + (svgBb.width / 2.0)) { //right
				left = d3.event.pageX - tooltipBb.width;
				if (d3.event.pageY > svgBb.top + (svgBb.height / 2.0)) { //bottom
					top = d3.event.pageY - tooltipBb.height;
				} else { //top
					top = d3.event.pageY;
				}
			} else { //left
				left = d3.event.pageX;
				if (d3.event.pageY > svgBb.top + (svgBb.height / 2.0)) { //bottom
					top = d3.event.pageY - tooltipBb.height;
				} else { //top
					top = d3.event.pageY;
				}
			}

			tooltipD3.style("left", left + "px")
			 .style("top", top + "px");
		})

		.on("mouseout", function(d, i) {
			tooltipD3.transition().duration(400).style("opacity", 0);
			var selection = d3.select(this).transition("tooltip_transition").duration(400);
			selection.select("path").style("stroke", function(d, i) {
				return maps.colorMap[d.sets.toString()];
			});
			// .style("stroke-width", 0)
			// .style("stroke", function(d,i) { return d.sets.length == 1 ?
			// maps.colorMap[d.sets.toString()] : abStrokeColor; })
			// .style("stroke-opacity", function(d,i) { return d.sets.length ==
			// 1 ? 1 : 0.5; });
			// .style("stroke-opacity", function(d,i) { return d.sets.length ==
			// 1 ? 1 : 0.4; })
			// .style("stroke-width", 2)
			// .style("fill-opacity", function(d,i) { return d.sets.length == 1
			// ? 0.75 : 0.1; })
		});
	}
}

function _getSelectColor(criteria, criterionColorClasses) {
	if (criteria != null) {
		if (criterionColorClasses != null && criterionColorClasses.length > 0) {
			if (criteria.selectStatementCount > 1) {
				return _getCssColor('ctsms-search-intermediateset-intermediate-color');
			} else {
				var position = null;
				if (criteria.criterions != null && criteria.criterions.length > 0) {
					for ( var i = 0; i < criteria.criterions.length; i++) {
						position = criteria.criterions[i].position;
						if (position != null) {
							break;
						}
					}
				}
				if (position != null && criterionColorClasses != null && criterionColorClasses.length > 0) {
					return _getCssColor(criterionColorClasses[position - LIST_INITIAL_POSITION]);
				}
			}
		} else {
			return _getCssColor('ctsms-search-intermediateset-blank-color');
		}
	}
	return null;
}

function _getCssColor(colorClassName) {
	if (cssColorMap == null) {
		cssColorMap = getCssRuleMap(/(\.ctsms-search-criterion-row|\.ctsms-color-|\.ctsms-search-intermediateset-)/);
	}
	if (colorClassName != null && colorClassName.length > 0) {
		var colorClass = cssColorMap["." + colorClassName];
		if (colorClass != null) {
			var colorValue = colorClass['background-color'];
			if (colorValue == null) {
				colorValue = colorClass['background'];
			}
			if (colorValue != null) {
				return rgb2hex(colorValue);
			}

		}
	}
	return null;
}
