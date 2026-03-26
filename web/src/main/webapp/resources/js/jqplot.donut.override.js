/**
 * jqPlot Donut & Legend Overrides
 * Fixes: 
 * 1. Continuous color consumption across nested rings.
 * 2. Legend sorting (shortest first), truncation (max 20), and 2-column force.
 * 3. IndexSizeError fix for 6+ rings by clamping radius at 0.
 * 4. Dynamic thickness scaling to prevent ring collapse.
 */
(function($) {
    if ($.jqplot.DonutRenderer) {
        $.jqplot.DonutRenderer.prototype.draw = function (ctx, gd, options, plot) {
            var i, opts = (options != undefined) ? options : {}, offx = 0, offy = 0, trans = 1;
            
            // Fix 1: Use the plot-level color generator for continuous colors across rings
            var colorGenerator = plot.colorGenerator;

            if (options.legendInfo && options.legendInfo.placement == 'insideGrid') {
                var li = options.legendInfo;
                switch (li.location) {
                    case 'nw': offx = li.width + li.xoffset; break;
                    case 'w': offx = li.width + li.xoffset; break;
                    case 'sw': offx = li.width + li.xoffset; break;
                    case 'ne': offx = li.width + li.xoffset; trans = -1; break;
                    case 'e': offx = li.width + li.xoffset; trans = -1; break;
                    case 'se': offx = li.width + li.xoffset; trans = -1; break;
                    case 'n': offy = li.height + li.yoffset; break;
                    case 's': offy = li.height + li.yoffset; trans = -1; break;
                }
            }
            
            var cw = ctx.canvas.width, ch = ctx.canvas.height;
            var w = cw - offx - 2 * this.padding, h = ch - offy - 2 * this.padding;
            var mindim = Math.min(w, h), d = mindim;
            var ringmargin = (this.ringMargin == null) ? this.sliceMargin * 2.0 : this.ringMargin;
            
            for (i = 0; i < this._previousSeries.length; i++) {
                d -= 2.0 * this._previousSeries[i]._thickness + 2.0 * ringmargin;
            }
            
            this._diameter = this.diameter || d;
            
            // Fix 3 & 4: Improved thickness logic for many rings
            if (this.innerDiameter != null) {
                var od = (this._numberSeries > 1 && this.index > 0) ? this._previousSeries[0]._diameter : this._diameter;
                this._thickness = this.thickness || (od - this.innerDiameter - 2.0 * ringmargin * this._numberSeries) / this._numberSeries / 2.0;
            } else {
                // Scale thickness based on the number of rings to prevent IndexSizeError
                var availableRadius = mindim / 2;
                var totalRings = this._numberSeries;
                this._thickness = this.thickness || (availableRadius / (totalRings + 0.5)) * 0.8;
            }

            var r = this._radius = this._diameter / 2;
            
            // Safety Clamp: Prevent negative radius error
            if (r < 0) { r = 0; this._radius = 0; }
            
            this._innerRadius = r - this._thickness;
            
            // Safety Clamp: Prevent inner radius from going negative
            if (this._innerRadius < 0) {
                this._innerRadius = 0;
                this._thickness = r; 
            }

            var sa = this.startAngle / 180 * Math.PI;
            this._center = [(cw - trans * offx) / 2 + trans * offx, (ch - trans * offy) / 2 + trans * offy];
            
            if (this.shadow) {
                var shadowColor = 'rgba(0,0,0,' + this.shadowAlpha + ')';
                for (i = 0; i < gd.length; i++) {
                    var ang1 = (i == 0) ? sa : gd[i - 1][1] + sa;
                    ang1 += this.sliceMargin / 180 * Math.PI;
                    this.renderer.drawSlice.call(this, ctx, ang1, gd[i][1] + sa, shadowColor, true);
                }
            }
            for (i = 0; i < gd.length; i++) {
                var ang1 = (i == 0) ? sa : gd[i - 1][1] + sa;
                ang1 += this.sliceMargin / 180 * Math.PI;
                var ang2 = gd[i][1] + sa;
                this._sliceAngles.push([ang1, ang2]);
                
                // Use the global colorGenerator
                var sliceColor = colorGenerator.next();
                this.renderer.drawSlice.call(this, ctx, ang1, ang2, sliceColor, false);
                
                if (this.showDataLabels && gd[i][2] * 100 >= this.dataLabelThreshold) {
                    var fstr, avgang = (ang1 + ang2) / 2, label;
                    if (this.dataLabels == 'label') { fstr = this.dataLabelFormatString || '%s'; label = $.jqplot.sprintf(fstr, gd[i][0]); }
                    else if (this.dataLabels == 'value') { fstr = this.dataLabelFormatString || '%d'; label = $.jqplot.sprintf(fstr, this.data[i][1]); }
                    else if (this.dataLabels == 'percent') { fstr = this.dataLabelFormatString || '%d%%'; label = $.jqplot.sprintf(fstr, gd[i][2] * 100); }
                    else if (this.dataLabels.constructor == Array) { fstr = this.dataLabelFormatString || '%s'; label = $.jqplot.sprintf(fstr, this.dataLabels[i]); }
                    
                    var fact = this._innerRadius + this._thickness * this.dataLabelPositionFactor + this.sliceMargin + this.dataLabelNudge;
                    var lx = this._center[0] + Math.cos(avgang) * fact + this.canvas._offsets.left;
                    var ly = this._center[1] + Math.sin(avgang) * fact + this.canvas._offsets.top;
                    var labelelem = $('<span class="jqplot-donut-series jqplot-data-label" style="position:absolute;">' + label + '</span>').insertBefore(plot.eventCanvas._elem);
                    labelelem.css({ left: Math.round(lx - labelelem.width() / 2), top: Math.round(ly - labelelem.height() / 2) });
                }
            }
        };
    }

    if ($.jqplot.DonutLegendRenderer) {
        $.jqplot.DonutLegendRenderer.prototype.init = function(options) {
            this.numberRows = null;
            this.numberColumns = null;
            this.preDraw = true; 
            $.extend(true, this, options);
        };

        $.jqplot.DonutLegendRenderer.prototype.draw = function() {
            var legend = this;
            if (this.show) {
                var series = this._series;
                
                // Fix 2: Retrieve limits from discovery path series[0]
                var maxItems = series[0].maxLegendItems || 0;
                var maxCols = series[0].maxLegendColumns || 1;

                this._elem = $(document.createElement('table')).addClass('jqplot-table-legend');
                var ss = { position:'absolute' };
                if (this.background) ss.background = this.background;
                if (this.border) ss.border = this.border;
                if (this.fontSize) ss.fontSize = this.fontSize;
                if (this.fontFamily) ss.fontFamily = this.fontFamily;
                if (this.textColor) ss.textColor = this.textColor;
                if (this.placement === 'outsideGrid') { ss['white-space'] = 'nowrap'; ss['z-index'] = '101'; }
                this._elem.css(ss);

                var processedLabels = {}, legendItems = [];
                var colorGenerator = new $.jqplot.ColorGenerator(series[0].seriesColors);

                for (var sIdx = 0; sIdx < series.length; sIdx++) {
                    var s = series[sIdx];
                    if (s.show) {
                        var pd = s.data;
                        for (var j = 0; j < pd.length; j++) {
                            var lt = pd[j][0].toString();
                            var color = colorGenerator.next();
                            if (!processedLabels[lt]) {
                                legendItems.push({ label: lt, color: color });
                                processedLabels[lt] = true;
                            }
                        }
                    }
                }

                // Sort shortest to longest
                legendItems.sort(function(a, b) { return a.label.length - b.label.length; });

                // Truncate logic
                var wasTruncated = false;
                if (maxItems > 0 && legendItems.length > maxItems) {
                    legendItems = legendItems.slice(0, maxItems);
                    wasTruncated = true;
                }

                var numItems = legendItems.length;
                var targetCols = wasTruncated ? maxCols : (this.numberColumns || 1);
                var numRows = Math.ceil(numItems / targetCols);

                for (var rIdx = 0; rIdx < numRows; rIdx++) {
                    var tr = $(document.createElement('tr')).addClass('jqplot-table-legend').appendTo(this._elem);
                    for (var cIdx = 0; cIdx < targetCols; cIdx++) {
                        var k = rIdx + (cIdx * numRows); 
                        if (k < numItems) {
                            var item = legendItems[k];
                            var rs = (rIdx > 0) ? this.rowSpacing : '0';
                            var ps = (cIdx > 0) ? '15px' : '0'; 

                            var td1 = $(document.createElement('td')).addClass('jqplot-table-legend jqplot-table-legend-swatch').css({textAlign: 'center', paddingTop: rs, paddingLeft: ps});
                            var divOutline = $(document.createElement('div')).addClass('jqplot-table-legend-swatch-outline');
                            var divSwatch = $(document.createElement('div')).addClass('jqplot-table-legend-swatch').css({backgroundColor: item.color, borderColor: item.color});
                            td1.append(divOutline.append(divSwatch)).appendTo(tr);

                            var td2 = $(document.createElement('td')).addClass('jqplot-table-legend jqplot-table-legend-label').css({paddingTop: rs, paddingLeft: '5px'});
                            if (this.escapeHtml) { td2.text(item.label); } else { td2.html(item.label); }
                            td2.appendTo(tr);
                        }
                    }
                }
            }
            return this._elem;
        };
    }
})(jQuery);