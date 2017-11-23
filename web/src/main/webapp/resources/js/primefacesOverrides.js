var highlightDelay = 2000;

PrimeFaces.clearSelection = function() {
    if(window.getSelection) {
        if(window.getSelection().empty) {
            window.getSelection().empty();
        } else if(window.getSelection().removeAllRanges && window.getSelection().rangeCount > 0 && window.getSelection().getRangeAt(0).getClientRects().length > 0) {
            window.getSelection().removeAllRanges();
        }
    }
    else if(document.selection && document.selection.empty) {
        try {
            document.selection.empty();
        } catch(error) {
            //ignore IE bug
        }
    }
};

PrimeFaces.ajax.AjaxUtils.send = function(cfg) {
  PrimeFaces.debug('Initiating ajax request.');

  if (cfg.onstart) {
    var retVal = cfg.onstart.call(this, cfg);
    if (retVal == false) {
      PrimeFaces.debug('Ajax request cancelled by onstart callback.');

      //remove from queue
      if (!cfg.async) {
        PrimeFaces.ajax.Queue.poll();
      }

      return; //cancel request
    }
  }

  var form = null, sourceId = null;

  //source can be a client id or an element defined by this keyword
  if (typeof (cfg.source) == 'string') {
    sourceId = cfg.source;
  } else {
    sourceId = $(cfg.source).attr('id');
  }

  if (cfg.formId) {
    form = $(PrimeFaces.escapeClientId(cfg.formId)); //Explicit form is defined
  } else {
    form = $(PrimeFaces.escapeClientId(sourceId)).parents('form:first'); //look for a parent of source

    //source has no parent form so use first form in document
    if (form.length == 0) {
      form = $('form').eq(0);
    }
  }

  PrimeFaces.debug('Form to post ' + form.attr('id') + '.');

  var postURL = form.attr('action'), encodedURLfield = form.children("input[name='javax.faces.encodedURL']"), postParams = [];

  //portlet support
  var pForms = null;
  if (encodedURLfield.length > 0) {
    postURL = encodedURLfield.val();
    pForms = $('form[action="' + form.attr('action') + '"]'); //find forms of the portlet
  }

  PrimeFaces.debug('URL to post ' + postURL + '.');

  //partial ajax
  postParams.push({
      name : PrimeFaces.PARTIAL_REQUEST_PARAM,
      value : true
  });

  //source
  postParams.push({
      name : PrimeFaces.PARTIAL_SOURCE_PARAM,
      value : sourceId
  });

  //process
  var process = [];
  if (cfg.process) {
    process.push(cfg.process);
  }
  if (cfg.ext && cfg.ext.process) {
    process.push(cfg.ext.process);
  }

  //process selector
  if (cfg.processSelector) {
    $.merge(process, PrimeFaces.ajax.AjaxUtils.findComponents(cfg.processSelector));
  }

  var processIds = process.length > 0 ? process.join(' ') : '@all';
  postParams.push({
      name : PrimeFaces.PARTIAL_PROCESS_PARAM,
      value : processIds
  });

  //update
  var update = [];
  if (cfg.update) {
    update.push(cfg.update);
  }
  if (cfg.ext && cfg.ext.update) {
    update.push(cfg.ext.update);
  }

  //update selector
  if (cfg.updateSelector) {
    $.merge(update, PrimeFaces.ajax.AjaxUtils.findComponents(cfg.updateSelector));
  }

  if (update.length > 0) {
    postParams.push({
        name : PrimeFaces.PARTIAL_UPDATE_PARAM,
        value : update.join(' ')
    });
  }

  //behavior event
  if (cfg.event) {
    postParams.push({
        name : PrimeFaces.BEHAVIOR_EVENT_PARAM,
        value : cfg.event
    });

    var domEvent = cfg.event;

    if (cfg.event == 'valueChange')
      domEvent = 'change';
    else if (cfg.event == 'action')
      domEvent = 'click';

    postParams.push({
        name : PrimeFaces.PARTIAL_EVENT_PARAM,
        value : domEvent
    });
  } else {
    postParams.push({
        name : sourceId,
        value : sourceId
    });
  }

  //params
  if (cfg.params) {
    $.merge(postParams, cfg.params);
  }
  if (cfg.ext && cfg.ext.params) {
    $.merge(postParams, cfg.ext.params);
  }

  /**
   * Only add params of process components and their children
   * if partial submit is enabled(undefined or true) and there are components to process partially
   */
  if (cfg.partialSubmit != false && processIds != '@all') {
    var hasViewstate = false;

    if (processIds != '@none') {
      var processIdsArray = processIds.split(' ');

      $.each(processIdsArray, function(i, item) {
        var jqProcess = $(PrimeFaces.escapeClientId(item)), componentPostParams = null;

        if (jqProcess.is('form')) {
          componentPostParams = jqProcess.serializeArray();
          hasViewstate = true;
        } else if (jqProcess.is(':input')) {
          componentPostParams = jqProcess.serializeArray();
        } else {
          componentPostParams = jqProcess.find(':input').serializeArray();
        }

        $.merge(postParams, componentPostParams);
      });
    }

    //add viewstate if necessary
    if (!hasViewstate) {
      postParams.push({
          name : PrimeFaces.VIEW_STATE,
          value : form.children("input[name='javax.faces.ViewState']").val()
      });
    }

  } else {
    $.merge(postParams, form.serializeArray());
  }

  //serialize
  var postData = $.param(postParams);

  PrimeFaces.debug('Post Data:' + postData);

  var xhrOptions = {
      url : postURL,
      type : "POST",
      cache : false,
      dataType : "xml",
      data : postData,
      portletForms : pForms,
      source : cfg.source,
      beforeSend : function(xhr) {
        showWaitDialog();
        xhr.setRequestHeader('Faces-Request', 'partial/ajax');
      },
      error : function(xhr, status, errorThrown) {

        hideWaitDialog();
        resetSessionTimers();

        if (cfg.onerror) {
          cfg.onerror.call(xhr, status, errorThrown);
        }

        PrimeFaces.error('Request return with error:' + status + '.');
      },
      success : function(data, status, xhr) {

        hideWaitDialog();
        resetSessionTimers();

        PrimeFaces.debug('Response received succesfully.');

        var parsed;

        //call user callback
        if (cfg.onsuccess) {
          parsed = cfg.onsuccess.call(this, data, status, xhr);
        }

        //extension callback that might parse response
        if (cfg.ext && cfg.ext.onsuccess && !parsed) {
          parsed = cfg.ext.onsuccess.call(this, data, status, xhr);
        }

        //do not execute default handler as response already has been parsed
        if (parsed) {
          return;
        } else {
          PrimeFaces.ajax.AjaxResponse.call(this, data, status, xhr);
        }

        PrimeFaces.debug('DOM is updated.');
      },
      complete : function(xhr, status) {

        hideWaitDialog();
        resetSessionTimers();

        if (cfg.oncomplete) {
          cfg.oncomplete.call(this, xhr, status, this.args);
        }

        if (cfg.ext && cfg.ext.oncomplete) {
          cfg.ext.oncomplete.call(this, xhr, status, this.args);
        }

        PrimeFaces.debug('Response completed.');

        if (!cfg.async) {
          PrimeFaces.ajax.Queue.poll();
        }
      }
  };

  xhrOptions.global = cfg.global == true || cfg.global == undefined ? true : false;

  $.ajax(xhrOptions);
};

function _highlightText(jq) {

  if (jq != null) {
    jq.highlightTextarea({
      words : [

      {
          color : '#ffff00',
          words : [ '\\s+(?=\\S)' ]

      },

      {
          color : '#ff0000',
          words : [ '\\s+$' ]
      },

      ]
    });

  }

}

PrimeFaces.widget.Spinner.prototype.spin = function(step) {
  var newValue = this.value;
  if(jQuery.trim(newValue) == '') {
        //if(this.cfg.min != undefined)
        //	newValue = this.cfg.min;
        //else
          newValue = 0;
    } else {
      newValue = newValue + step;
    }

    if(this.cfg.min != undefined && newValue < this.cfg.min) {
        newValue = this.cfg.min;
    }

    if(this.cfg.max != undefined && newValue > this.cfg.max) {
        newValue = this.cfg.max;
    }

    this.input.val(newValue);
    this.value = newValue;
    this.input.attr('aria-valuenow', newValue);

    this.input.change();
};

PrimeFaces.widget.Spinner.prototype.updateValue = function() {
    var value = this.input.val();

    if(jQuery.trim(value) == '') {
        if(this.cfg.min != undefined)
            this.value = this.cfg.min;
        else
            this.value = ''; //0;
    }
    else {
        if(this.cfg.step)
            value = parseFloat(value);
        else
            value = parseInt(value);

        if(!isNaN(value)) {
            this.value = value;
        }
    }
};

PrimeFaces.widget.Spinner.prototype.initValue = function() {
    var value = this.input.val();

    if(jQuery.trim(value) == '') {
        if(this.cfg.min != undefined)
            this.value = this.cfg.min;
        else
            this.value = ''; //0;
    }
    else {
        if(this.cfg.prefix)
            value = value.split(this.cfg.prefix)[1];

        if(this.cfg.suffix)
            value = value.split(this.cfg.suffix)[0];

        if(this.cfg.step)
            this.value = parseFloat(value);
        else
            this.value = parseInt(value);
    }
};

PrimeFaces.widget.Spinner.prototype.getValue = function() {
  return this.input.val();
}

PrimeFaces.widget.Spinner.prototype.setValue = function(value) {
  this.input.val(value);
}

PrimeFaces.widget.InputTextarea = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

        this.cfg.rowsDefault = this.jq.attr('rows');
        this.cfg.colsDefault = this.jq.attr('cols');

        //Visuals
        PrimeFaces.skinInput(this.jq);

        //AutoResize
        if(this.cfg.autoResize) {
            this.setupAutoResize();
        }

        if($.browser.msie || /Edge/.test(navigator.userAgent)) {
        	this.jq.css('width','720px');
        	this.jq.resizable();
        }

        //max length
        if(this.cfg.maxlength) {
            this.applyMaxlength();
        }

        //Client behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.jq, this.cfg.behaviors);
        }

        //Counter
        if(this.cfg.counter) {
            var _self = this;
            $(function() {
                _self.counter = _self.cfg.counter ? $(PrimeFaces.escapeClientId(_self.cfg.counter)) : null;
                _self.cfg.counterTemplate = _self.cfg.counterTemplate||'{0}';

                _self.updateCounter();
            });
        }
    },

    setupAutoResize: function() {
        var _self = this;

        this.jq.keyup(function() {
            _self.resize();
        }).focus(function() {
            _self.resize();
        }).blur(function() {
            _self.resize();
        });
    },

    resize: function() {
        var linesCount = 0,
        lines = this.jq.val().split('\n');

        for(var i = lines.length-1; i >= 0 ; --i) {
            linesCount += Math.floor((lines[i].length / this.cfg.colsDefault) + 1);
        }

        var newRows = (linesCount >= this.cfg.rowsDefault) ? (linesCount + 1) : this.cfg.rowsDefault;

        this.jq.attr('rows', newRows);
    },

    applyMaxlength: function() {
        var _self = this;

        this.jq.keyup(function(e) {
            var value = _self.jq.val(),
            length = value.length;

            if(length > _self.cfg.maxlength) {
                _self.jq.val(value.substr(0, _self.cfg.maxlength));
            }

            if(_self.counter) {
                _self.updateCounter();
            }
        });
    },

    updateCounter: function() {
        var value = this.jq.val(),
        length = value.length;

        if(this.counter) {
            var remaining = this.cfg.maxlength - length,
            remainingText = this.cfg.counterTemplate.replace('{0}', remaining);

            this.counter.html(remainingText);
        }
    }
});

PrimeFaces.widget.InputTextarea.prototype.getValue = function() {
  return this.jq.val();
}

PrimeFaces.widget.InputTextarea.prototype.setValue = function(value) {
  this.jq.val(value);
}

PrimeFaces.widget.InputText = PrimeFaces.widget.BaseWidget.extend({

  init : function(cfg) {
    this._super(cfg);

    //Client behaviors
    if (this.cfg.behaviors) {
      PrimeFaces.attachBehaviors(this.jq, this.cfg.behaviors);
    }

    //Visuals
    PrimeFaces.skinInput(this.jq);

    //setTimeout(_highlightText.bind(null,this.jq),highlightDelay);
    var jq = this.jq;
    setTimeout(function(){_highlightText(jq);},highlightDelay);
  }
});

PrimeFaces.widget.InputText.prototype.getValue = function() {
  return this.jq.val();
}

PrimeFaces.widget.InputText.prototype.setValue = function(value) {
  this.jq.val(value);
}

PrimeFaces.widget.InputText.prototype.enable = function() {
  this.jq.removeClass('ui-state-disabled');

  this.jq.removeAttr('disabled');
}

PrimeFaces.widget.InputText.prototype.disable = function() {
  this.jq.addClass('ui-state-disabled');

  this.jq.attr("disabled", "disabled");
}

PrimeFaces.widget.TabView.prototype.setTabTitle = function(index, title) {
  if (index >= 0 && index < this.getLength()) {
    this.navContainer.children()[index].firstChild.firstChild.nodeValue = title;
  }
}
PrimeFaces.widget.TabView.prototype.emphasizeTab = function(index, emphasize) {
  if (index >= 0 && index < this.getLength()) {
    var tabTitle = this.navContainer.children()[index];
    tabTitle.className = tabTitle.className.replace(/(?:^|\s)ctsms-tabtitle-emphasized(?!\S)/g, '');
    tabTitle.className = tabTitle.className.replace(/(?:^|\s)ctsms-tabtitle(?!\S)/g, '');
    if (emphasize) {

      tabTitle.className += ' ctsms-tabtitle-emphasized';
    } else {

      tabTitle.className += ' ctsms-tabtitle';
    }
  }
}

PrimeFaces.widget.TabView.prototype.getTabTitle = function(index) {
  if (index >= 0 && index < this.getLength()) {
    return this.navContainer.children()[index].firstChild.firstChild.nodeValue;
  } else {
    return '';
  }
}

//http://forum.primefaces.org/viewtopic.php?f=3&t=17987
PrimeFaces.widget.TabView.prototype.loadDynamicTab = function(newPanel) {
    var _self = this,
    options = {
        source: this.id,
        process: this.id + "_activeIndex",
        update: this.id
    },
    tabindex = newPanel.index();

    options.onsuccess = function(responseXML) {
        var xmlDoc = $(responseXML.documentElement),
        updates = xmlDoc.find("update");

        for(var i=0; i < updates.length; i++) {
            var update = updates.eq(i),
            id = update.attr('id'),
            content = update.text();

            if(id == _self.id){
                newPanel.html(content);

                if(_self.cfg.cache) {
                    _self.markAsLoaded(newPanel);
                }
            }
            else {
                PrimeFaces.ajax.AjaxUtils.updateElement.call(this, id, content);
            }
        }

        PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, xmlDoc);

        return true;
    };

    options.oncomplete = function() {
        _self.show(newPanel);
    };

    options.params = [
        {name: this.id + '_contentLoad', value: true},
        {name: this.id + '_newTab', value: newPanel.attr('id')},
        {name: this.id + '_tabindex', value: tabindex}
    ];

    if(this.hasBehavior('tabChange')) {
        var tabChangeBehavior = this.cfg.behaviors['tabChange'];

        tabChangeBehavior.call(this, newPanel, options);
    }
    else {
        PrimeFaces.ajax.AjaxRequest(options);
    }
};

//PrimeFaces.widget.TabView.prototype.select = function(index) {
//
//    var newPanel = this.panelContainer.children().eq(index),
//    	shouldLoad = this.cfg.dynamic && !this.isLoaded(newPanel);
//
//    //update state
//    this.stateHolder.val(index);
//    this.cfg.selected = index;
//
//    if(shouldLoad) {
//        this.loadDynamicTab(newPanel);
//    }
//    else {
//        if(this.hasBehavior('tabChange')) {
//            this.fireTabChangeEvent(newPanel);
//        }
//        else {
//            this.show(newPanel);
//        }
//    }
//
//    //Call user onTabChange callback
//    if(this.cfg.onTabChange) {
//        var result = this.cfg.onTabChange.call(this, index);
//        if(result == false)
//            return false;
//    }
//    return true;
//};

PrimeFaces.widget.Dialog.prototype.getTitle = function() {

  return this.titlebar.children()[0].firstChild.nodeValue;
}
PrimeFaces.widget.Dialog.prototype.setTitle = function(title) {

  this.titlebar.children()[0].firstChild.nodeValue = title;
}

PrimeFaces.widget.SelectBooleanCheckbox.prototype.getValue = function() {

  return this.input.is(':checked');

}

PrimeFaces.widget.SelectBooleanCheckbox.prototype.setValue = function(value) {
  if (value) {
    this.check();
  } else {
    this.uncheck();
  }
}

PrimeFaces.widget.SelectManyCheckbox.prototype.getValue = function() {

  var result = [];
  $(this.inputs).each(function() {

    var input = $(this);
    if (input.is(':checked')) {
      result.push(input.attr('value'));
    }

  });
  return result;

}

PrimeFaces.widget.SelectManyCheckbox.prototype.setValue = function(value) {

  if (value instanceof Array) {
    $(this.inputs).each(function() {

      var input = $(this);
      var checkbox = input.parent().next();
      var hasFocus = input.is(':focus');
      var itemValue = input.attr('value');

      var found = false;
      for ( var i = 0; i < value.length; i++) {
        if (value[i] === itemValue) {
          found = true;
          break;
        }
      }
      if (found) {
        input.attr('checked', 'checked');
        checkbox.children('.ui-chkbox-icon').addClass('ui-icon ui-icon-check');

        if (!hasFocus) {
          checkbox.addClass('ui-state-active');
        }
      } else {
        input.removeAttr('checked');
        checkbox.removeClass('ui-state-active').children('.ui-chkbox-icon').removeClass('ui-icon ui-icon-check');
      }

    });

  }

}

PrimeFaces.widget.SelectOneMenu.prototype.getValue = function() {

  var selectedOption = this.options.filter(':selected');

  return selectedOption.attr('value');

}

PrimeFaces.widget.SelectOneMenu.prototype.setValue = function(value) {

  var _self = this;
  var index = -1;
  for ( var i = 0; i < _self.options.length; i++) {
    if (_self.options[i].value === value) {
      index = _self.options[i].index;
      break;
    }
  }
  if (index > 0) {
    _self.selectItem(_self.items.eq(index));
  }

}

PrimeFaces.widget.SelectOneRadio.prototype.getValue = function() {

  var selectedOption = this.inputs.filter(':checked');

  return selectedOption.attr('value');

}

PrimeFaces.widget.SelectOneRadio.prototype.setValue = function(value) {

  var _self = this;
  // unselect previous
  _self.checkedRadio.removeClass('ui-state-active').children('.ui-radiobutton-icon').removeClass('ui-icon ui-icon-bullet');

  $(this.inputs).each(function() {

    var input = $(this);
    var radio = input.parent().next();

    var found = (input.attr('value') === value);
    if (found) {
      input.attr('checked', 'checked');
      radio.children('.ui-radiobutton-icon').addClass('ui-icon ui-icon-bullet');

      if (!input.is(':focus')) {
        radio.addClass('ui-state-active');
      }
      _self.checkedRadio = radio;
    }

  });

}

PrimeFaces.widget.SelectBooleanCheckbox.prototype.enable = function() {
  this.jq.removeClass('ui-state-disabled');
  this.disabled = false;
  this.input.removeAttr('disabled');
}

PrimeFaces.widget.SelectBooleanCheckbox.prototype.disable = function() {
  this.jq.addClass('ui-state-disabled');
  this.disabled = true;
  this.input.attr("disabled", "disabled");
}

PrimeFaces.widget.Dialog.prototype.focusFirstInput = function() {
  //leave blank. calendar popup avoided.
}

PrimeFaces.widget.SelectOneMenu.prototype.bindKeyEvents = function() {
  var _self = this;

  this.input.keyup(function(e) {
    var keyCode = $.ui.keyCode, mozilla = $.browser.mozilla;

    switch (e.which) {
    case keyCode.UP:
    case keyCode.LEFT:
    case keyCode.DOWN:
    case keyCode.RIGHT:
      if (mozilla) {
        var highlightedItem = _self.items.filter('.ui-state-highlight');
        _self.options.filter(':selected').removeAttr('selected');
        _self.options.eq(highlightedItem.index()).attr('selected', 'selected');
      }

      e.preventDefault();
      break;

    case keyCode.TAB:
    case keyCode.ESCAPE:
      //do nothing
      break;

    default:
      var currentOption = _self.options.filter(':selected'), item = _self.items.eq(currentOption.index());

      _self.highlightItem(item, true);

      _self.changed = (currentOption.val() != _self.value);

      e.preventDefault();
      break;
    }
  }).keydown(function(e) {
    var keyCode = $.ui.keyCode;

    switch (e.which) {
    case keyCode.UP:
    case keyCode.LEFT:
      var highlightedItem = _self.items.filter('.ui-state-highlight'), prev = highlightedItem.prevAll(':not(.ui-state-disabled):first');

      if (prev.length == 1) {
        _self.highlightItem(prev, true);

        _self.selectItem(prev);

      }

      e.preventDefault();

      break;

    case keyCode.DOWN:
    case keyCode.RIGHT:
      var highlightedItem = _self.items.filter('.ui-state-highlight'), next = highlightedItem.nextAll(':not(.ui-state-disabled):first');

      if (next.length == 1) {
        _self.highlightItem(next, true);

        _self.selectItem(next);
      }

      e.preventDefault();

      break;

    case keyCode.TAB:
    case keyCode.ESCAPE:
      if (_self.panel.is(":visible")) {
        _self.hide();
      }

      if (_self.changed) {
        _self.triggerChange();
      }
      break;

    case keyCode.ENTER:
    case keyCode.NUMPAD_ENTER:
      if (_self.panel.is(":visible")) {
        _self.hide();
      }

      if (_self.changed) {
        _self.triggerChange();
      }

      e.preventDefault();
      break;
    }
  });
}

PrimeFaces.widget.AutoComplete = PrimeFaces.widget.BaseWidget
        .extend({

            init : function(cfg) {
              this._super(cfg);

              this.panelId = this.jqId + '_panel';
              this.input = $(this.jqId + '_input');
              this.hinput = $(this.jqId + '_hinput');
              this.panel = this.jq.children(this.panelId);
              this.dropdown = this.jq.children('.ui-button');
              this.disabled = this.input.is(':disabled');
              this.active = true;
              this.cfg.pojo = this.hinput.length == 1;
              this.cfg.minLength = this.cfg.minLength != undefined ? this.cfg.minLength : 1;
              this.cfg.delay = this.cfg.delay != undefined ? this.cfg.delay : 300;
              var _self = this;

              //pfs metadata
              this.input.data(PrimeFaces.CLIENT_ID_DATA, this.id);
              this.hinput.data(PrimeFaces.CLIENT_ID_DATA, this.id);

              if (!this.disabled) {
                if (this.cfg.multiple) {
                  this.setupMultipleMode();

                  this.multiItemContainer.data('primefaces-overlay-target', true).find('*').data('primefaces-overlay-target', true);
                } else {
                  //visuals
                  PrimeFaces.skinInput(this.input);

                  if (this.dropdown.length == 0) {
                    //setTimeout(_highlightText.bind(null,this.input),highlightDelay);
                	var jq = this.jq;
                	setTimeout(function(){_highlightText(jq);},highlightDelay);
                  }

                  this.input.data('primefaces-overlay-target', true).find('*').data('primefaces-overlay-target', true);
                  this.dropdown.data('primefaces-overlay-target', true).find('*').data('primefaces-overlay-target', true);
                }

                //core events
                this.bindStaticEvents();

                //client Behaviors
                if (this.cfg.behaviors) {
                  PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
                }

                //force selection
                if (this.cfg.forceSelection) {
                  this.setupForceSelection();
                }

                //Panel management
                $(document.body).children(this.panelId).remove();
                this.panel.appendTo(document.body);

                //itemtip
                if (this.cfg.itemtip) {
                  this.itemtip = $(
                          '<div id="' + this.id + '_itemtip" class="ui-autocomplete-itemtip ui-state-highlight ui-widget ui-corner-all ui-shadow"></div>')
                          .appendTo(document.body);
                }

                //Hide overlay on resize
                var resizeNS = 'resize.' + this.id;
                $(window).unbind(resizeNS).bind(resizeNS, function() {
                  if (_self.panel.is(':visible')) {
                    _self.hide();
                  }
                });

                //dialog support
                this.setupDialogSupport();
              }

            },

            /**
             * Binds events for multiple selection mode
             */
            setupMultipleMode : function() {
              var _self = this;
              this.multiItemContainer = this.jq.children('ul');
              this.inputContainer = this.multiItemContainer.children('.ui-autocomplete-input-token');

              this.multiItemContainer.hover(function() {
                $(this).addClass('ui-state-hover');
              }, function() {
                $(this).removeClass('ui-state-hover');
              }).click(function() {
                _self.input.focus();
              });

              //delegate events to container
              this.input.focus(function() {
                _self.multiItemContainer.addClass('ui-state-focus');
              }).blur(function(e) {
                _self.multiItemContainer.removeClass('ui-state-focus');
              });

              //remove token
              $(this.jqId + ' li.ui-autocomplete-token .ui-autocomplete-token-icon').die().live('click', function(e) {
                _self.removeItem(e, $(this).parent());
              });
            },

            setupDialogSupport : function() {
              var dialog = this.jq.parents('.ui-dialog:first');

              if (dialog.length == 1) {
                this.panel.css('position', 'fixed');

                if (this.cfg.itemtip) {
                  this.itemtip.css('position', 'fixed');
                }
              }
            },

            bindStaticEvents : function() {
              var _self = this;

              this.bindKeyEvents();

              this.dropdown.mouseover(function() {
                if (!_self.disabled) {
                  $(this).addClass('ui-state-hover');
                }
              }).mouseout(function() {
                if (!_self.disabled) {
                  $(this).removeClass('ui-state-hover');
                }
              }).mousedown(function() {
                if (!_self.disabled && _self.active) {
                  $(this).addClass('ui-state-active');
                }
              }).mouseup(function() {
                if (!_self.disabled && _self.active) {
                  $(this).removeClass('ui-state-active');

                  _self.search('');
                  _self.input.focus();
                }
              });

              //hide overlay when outside is clicked
              var offset;
              $(document.body).bind(
                      'mousedown.ui-autocomplete',
                      function(e) {
                        if (_self.panel.is(":hidden")) {
                          return;
                        }
                        offset = _self.panel.offset();
                        if (e.target === _self.input.get(0)) {
                          return;
                        }
                        if (e.pageX < offset.left || e.pageX > offset.left + _self.panel.width() || e.pageY < offset.top
                                || e.pageY > offset.top + _self.panel.height()) {
                          _self.hide();
                        }
                      });
            },

            bindKeyEvents : function() {
              var _self = this;

              //bind keyup handler
              this.input.keyup(
                      function(e) {
                        var keyCode = $.ui.keyCode, key = e.which, shouldSearch = true;

                        if (key == keyCode.UP || key == keyCode.LEFT || key == keyCode.DOWN || key == keyCode.RIGHT || key == keyCode.TAB
                                || key == keyCode.SHIFT || key == keyCode.ENTER || key == keyCode.NUMPAD_ENTER) {
                          shouldSearch = false;
                        } else if (_self.cfg.pojo && !_self.cfg.multiple) {
                          _self.hinput.val($(this).val());
                        }

                        if (shouldSearch) {
                          var value = _self.input.val();

                          if (!value.length) {
                            _self.hide();
                          }

                          if (value.length >= _self.cfg.minLength) {

                            //Cancel the search request if user types within the timeout
                            if (_self.timeout) {
                              clearTimeout(_self.timeout);
                            }

                            _self.timeout = setTimeout(function() {
                              _self.search(value);
                            }, _self.cfg.delay);
                          }
                        }

                      }).keydown(function(e) {
                if (_self.panel.is(':visible')) {
                  var keyCode = $.ui.keyCode, highlightedItem = _self.items.filter('.ui-state-highlight');

                  switch (e.which) {
                  case keyCode.UP:
                  case keyCode.LEFT:
                    var prev = highlightedItem.length == 0 ? _self.items.eq(0) : highlightedItem.prevAll('.ui-autocomplete-item:first');

                    if (prev.length == 1) {
                      highlightedItem.removeClass('ui-state-highlight');
                      prev.addClass('ui-state-highlight');

                      if (_self.cfg.itemtip) {
                        _self.showItemtip(prev);
                      }

                      if (_self.cfg.scrollHeight) {
                        _self.alignScrollbar(prev);
                      }
                    }

                    e.preventDefault();
                    break;

                  case keyCode.DOWN:
                  case keyCode.RIGHT:
                    var next = highlightedItem.length == 0 ? _self.items.eq(0) : highlightedItem.nextAll('.ui-autocomplete-item:first');

                    if (next.length == 1) {
                      highlightedItem.removeClass('ui-state-highlight');
                      next.addClass('ui-state-highlight');

                      if (_self.cfg.itemtip) {
                        _self.showItemtip(next);
                      }

                      if (_self.cfg.scrollHeight) {
                        _self.alignScrollbar(next);
                      }
                    }

                    e.preventDefault();
                    break;

                  case keyCode.ENTER:
                  case keyCode.NUMPAD_ENTER:
                    highlightedItem.click();

                    e.preventDefault();
                    break;

                  case keyCode.ALT:
                  case 224:
                    break;

                  case keyCode.TAB:
                    highlightedItem.trigger('click');
                    _self.hide();
                    break;
                  }
                }

              });
            },

            bindDynamicEvents : function() {
              var _self = this;

              //visuals and click handler for items
              this.items.bind('mouseover', function() {
                var item = $(this);

                if (!item.hasClass('ui-state-highlight')) {
                  _self.items.filter('.ui-state-highlight').removeClass('ui-state-highlight');
                  item.addClass('ui-state-highlight');

                  if (_self.cfg.itemtip) {
                    _self.showItemtip(item);
                  }
                }
              }).bind(
                      'click',
                      function(event) {
                        var item = $(this), itemValue = item.attr('data-item-value');

                        if (_self.cfg.multiple) {
                          var itemDisplayMarkup = '<li data-token-value="' + item.attr('data-item-value')
                                  + '"class="ui-autocomplete-token ui-state-active ui-corner-all ui-helper-hidden">';
                          itemDisplayMarkup += '<span class="ui-autocomplete-token-icon ui-icon ui-icon-close" />';
                          itemDisplayMarkup += '<span class="ui-autocomplete-token-label">' + item.attr('data-item-label') + '</span></li>';

                          _self.inputContainer.before(itemDisplayMarkup);
                          _self.multiItemContainer.children('.ui-helper-hidden').fadeIn();
                          _self.input.val('').focus();

                          _self.hinput.append('<option value="' + itemValue + '" selected="selected"></option>');
                        } else {
                          _self.input.val(item.attr('data-item-label')).focus();

                          if (_self.cfg.pojo) {
                            _self.hinput.val(itemValue);
                          }
                        }

                        _self.invokeItemSelectBehavior(event, itemValue);

                        _self.hide();
                      });
            },

            showItemtip : function(item) {
              var content = item.is('li') ? item.next('.ui-autocomplete-itemtip-content') : item.children('td:last');

              this.itemtip.html(content.html()).css({
                  'left' : '',
                  'top' : '',
                  'z-index' : ++PrimeFaces.zindex,
                  'width' : content.outerWidth()
              }).position({
                  my : 'left top',
                  at : 'right bottom',
                  of : item
              }).show();
            },

            search : function(query) {
              if (!this.active) {
                return;
              }

              var _self = this;

              //start callback
              if (this.cfg.onstart) {
                this.cfg.onstart.call(this, query);
              }

              if (this.cfg.itemtip) {
                this.itemtip.hide();
              }

              var options = {
                  source : this.id,
                  update : this.id,
                  formId : this.cfg.formId,
                  onsuccess : function(responseXML) {
                    var xmlDoc = $(responseXML.documentElement), updates = xmlDoc.find("update");

                    for ( var i = 0; i < updates.length; i++) {
                      var update = updates.eq(i), id = update.attr('id'), data = update.text();

                      if (id == _self.id) {
                        _self.panel.html(data);
                        _self.items = _self.panel.find('.ui-autocomplete-item');

                        _self.bindDynamicEvents();

                        if (_self.items.length > 0) {
                          var firstItem = _self.items.eq(0);

                          //highlight first item
                          firstItem.addClass('ui-state-highlight');

                          //highlight query string
                          if (_self.panel.children().is('ul')) {
                            _self.items
                                    .each(function() {
                                      var item = $(this), text = item.text(), re = new RegExp(PrimeFaces.escapeRegExp(query), 'gi'), highlighedText = text
                                              .replace(re, '<span class="ui-autocomplete-query">$&</span>');

                                      item.html(highlighedText);
                                    });
                          }

                          if (_self.cfg.forceSelection) {
                            _self.cachedResults = [];
                            _self.items.each(function(i, item) {
                              _self.cachedResults.push($(item).attr('data-item-label'));
                            });
                          }

                          //adjust height
                          if (_self.cfg.scrollHeight && _self.panel.height() > _self.cfg.scrollHeight) {
                            _self.panel.height(_self.cfg.scrollHeight);
                          }

                          if (_self.panel.is(':hidden')) {
                            _self.show();
                          } else {
                            _self.alignPanel(); //with new items
                          }

                          //show itemtip if defined
                          if (_self.cfg.itemtip && firstItem.length == 1) {
                            _self.showItemtip(firstItem);
                          }
                        } else {
                          _self.panel.hide();
                        }
                      } else {
                        PrimeFaces.ajax.AjaxUtils.updateElement.call(this, id, data);
                      }
                    }

                    PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, xmlDoc);

                    return true;
                  }
              };

              //complete callback
              if (this.cfg.oncomplete) {
                options.oncomplete = this.cfg.oncomplete;
              }

              //process
              options.process = this.cfg.process ? this.id + ' ' + this.cfg.process : this.id;

              if (this.cfg.global === false) {
                options.global = false;
              }

              options.params = [ {
                  name : this.id + '_query',
                  value : query
              } ];

              PrimeFaces.ajax.AjaxRequest(options);
            },

            show : function() {
              this.alignPanel();

              if (this.cfg.effect)
                this.panel.show(this.cfg.effect, {}, this.cfg.effectDuration);
              else
                this.panel.show();
            },

            hide : function() {
              this.panel.hide();

              if (this.cfg.itemtip) {
                this.itemtip.hide();
              }
            },

            invokeItemSelectBehavior : function(event, itemValue) {
              if (this.cfg.behaviors) {
                var itemSelectBehavior = this.cfg.behaviors['itemSelect'];

                if (itemSelectBehavior) {
                  var ext = {
                    params : [ {
                        name : this.id + '_itemSelect',
                        value : itemValue
                    } ]
                  };

                  itemSelectBehavior.call(this, event, ext);
                }
              }
            },

            invokeItemUnselectBehavior : function(event, itemValue) {
              if (this.cfg.behaviors) {
                var itemUnselectBehavior = this.cfg.behaviors['itemUnselect'];

                if (itemUnselectBehavior) {
                  var ext = {
                    params : [ {
                        name : this.id + '_itemUnselect',
                        value : itemValue
                    } ]
                  };

                  itemUnselectBehavior.call(this, event, ext);
                }
              }
            },

            removeItem : function(event, item) {
              var itemValue = item.attr('data-token-value'), _self = this;

              //remove from options
              this.hinput.children('option').filter('[value="' + itemValue + '"]').remove();

              //remove from items
              item.fadeOut('fast', function() {
                var token = $(this);

                token.remove();

                _self.invokeItemUnselectBehavior(event, itemValue);
              });
            },

            setupForceSelection : function() {
              this.cachedResults = [ this.input.val() ];
              var _self = this;

              this.input.blur(function() {
                var value = $(this).val(), valid = false;

                for ( var i = 0; i < _self.cachedResults.length; i++) {
                  if (_self.cachedResults[i] == value) {
                    valid = true;
                    break;
                  }
                }

                if (!valid) {
                  _self.input.val('');
                  _self.hinput.val('');
                }
              });
            },

            disable : function() {
              this.disabled = true;
              this.input.addClass('ui-state-disabled').attr('disabled', 'disabled');
            },

            enable : function() {
              this.disabled = false;
              this.input.removeClass('ui-state-disabled').removeAttr('disabled');
            },

            close : function() {
              this.hide();
            },

            deactivate : function() {
              this.active = false;
            },

            activate : function() {
              this.active = true;
            },

            alignScrollbar : function(item) {
              var relativeTop = item.offset().top - this.items.eq(0).offset().top, visibleTop = relativeTop + item.height(), scrollTop = this.panel
                      .scrollTop(), scrollBottom = scrollTop + this.cfg.scrollHeight, viewportCapacity = parseInt(this.cfg.scrollHeight
                      / item.outerHeight(true));

              //scroll up
              if (visibleTop < scrollTop) {
                this.panel.scrollTop(relativeTop);
              }
              //scroll down
              else if (visibleTop > scrollBottom) {
                var viewportTopitem = this.items.eq(item.index() - viewportCapacity + 1);

                this.panel.scrollTop(viewportTopitem.offset().top - this.items.eq(0).offset().top);
              }
            },

            alignPanel : function() {
              var fixedPosition = this.panel.css('position') == 'fixed', win = $(window), positionOffset = fixedPosition ? '-' + win.scrollLeft() + ' -'
                      + win.scrollTop() : null, panelWidth = null;

              if (this.cfg.multiple) {
                panelWidth = this.multiItemContainer.innerWidth() - (this.input.position().left - this.multiItemContainer.position().left);
              } else {
                panelWidth = this.input.innerWidth();
              }

              this.panel.css({
                  'left' : '',
                  'top' : '',
                  'width' : panelWidth,
                  'z-index' : ++PrimeFaces.zindex
              }).position({
                  my : 'left top',
                  at : 'left bottom',
                  of : this.input,
                  offset : positionOffset
              });
            }

        });

PrimeFaces.widget.AutoComplete.prototype.getValue = function() {
  return this.input.val();
}

PrimeFaces.widget.AutoComplete.prototype.setValue = function(value) {
  var _self = this;

  _self.input.val(value);
  _self.hinput.val(value);
  if (_self.cfg.forceSelection) {

    valid = false;

    for ( var i = 0; i < _self.cachedResults.length; i++) {
      if (_self.cachedResults[i] == value) {
        valid = true;
        break;
      }
    }

    if (!valid) {
      _self.input.val('');
      _self.hinput.val('');
    }
  }

}

PrimeFaces.widget.LineChart = PrimeFaces.widget.BaseWidget.extend({

    init : function(cfg) {
      this._super(cfg);

      this.jqpId = this.id.replace(/:/g, "\\:");
      this.cfg.seriesDefaults = {};
      var _self = this;
      this.cfg.seriesColors = $.map(this.cfg.seriesColors,function(val, i) {
    	  return $.jqplot.normalize2rgb(val,0.5); //  + ''
      });

      //axes
      this.cfg.axes = this.cfg.axes || {};
      this.cfg.axes.xaxis = this.cfg.axes.xaxis || {};
      this.cfg.axes.yaxis = this.cfg.axes.yaxis || {};

      this.cfg.axes.xaxis.min = this.cfg.minX;
      this.cfg.axes.xaxis.max = this.cfg.maxX;

      this.cfg.axes.yaxis.min = this.cfg.minY;
      this.cfg.axes.yaxis.max = this.cfg.maxY;

      if (this.cfg.categories) {
        this.cfg.axes.xaxis.renderer = $.jqplot.CategoryAxisRenderer;
        this.cfg.axes.xaxis.ticks = this.cfg.categories;
      }

      this.cfg.legend.placement = "outsideGrid";

      this.cfg.axes.xaxis.renderer = $.jqplot.DateAxisRenderer;

      this.cfg.axes.xaxis.tickRenderer = $.jqplot.CanvasAxisTickRenderer;
      this.cfg.axes.xaxis.tickOptions = {
          angle : -30,
          formatString : JQPLOT_DATE_PATTERN,
      };
      this.cfg.axes.yaxis.tickRenderer = $.jqplot.CanvasAxisTickRenderer;
      this.cfg.axes.yaxis.tickOptions = {
        formatString : '%d',
      };

      if (this.cfg.breakOnNull) {
        this.seriesDefaults.breakOnNull = true;
      }

      if (this.cfg.fillToZero) {
        this.cfg.seriesDefaults.fillToZero = true;
        this.cfg.seriesDefaults.fill = true;
      } else if (this.cfg.fill) {
        this.cfg.seriesDefaults.fill = true;
      }

      this.cfg.highlighter = {
          show : true,
          formatString : '%s, %s',
          showTooltip : true
      };

      if (this.jq.is(':visible')) {
        this.draw();
      } else {
        var hiddenParent = this.jq.parents('.ui-hidden-container:first'), hiddenParentWidget = hiddenParent.data('widget');

        if (hiddenParentWidget) {
          hiddenParentWidget.addOnshowHandler(function() {
            return _self.draw();
          });
        }
      }
    },

    draw : function() {
      if (this.jq.is(':visible')) {
        //events
        PrimeFaces.widget.ChartUtils.bindItemSelectListener(this);

        //render chart
        this.plot = $.jqplot(this.jqpId, this.cfg.data, this.cfg);

        return true;
      } else {
        return false;
      }
    }

});



/**
 * PrimeFaces ProgressBar widget
 */
PrimeFaces.widget.ProgressBar = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

        this.jqValue = this.jq.children('.ui-progressbar-value');
        this.jqLabel = this.jq.children('.ui-progressbar-label');
        this.value = 0;

        if(this.cfg.ajax) {
            this.cfg.formId = this.jq.parents('form:first').attr('id');
        }

        this.enableARIA();

        this.setValue(this.cfg.value);
    },

    setValue: function(value) {
        if(value >= 0 && value<=100) {
            if(value == 0) {
                this.jqValue.hide().css('width', '0%').removeClass('ui-corner-right');

                this.jqLabel.hide();
            }
            else {
                this.jqValue.show().animate({
                    'width': value + '%'
                }, 500, 'easeInOutCirc');

                if(this.cfg.labelTemplate) {
                    var formattedLabel = this.cfg.labelTemplate.replace(/{value}/gi, value);

                    this.jqLabel.html(formattedLabel).show();
                }
            }

            this.value = value;
            this.jq.attr('aria-valuenow', value);
        }
    },

    getValue: function() {
        return this.value;
    },

    start: function() {
        var _self = this;

        if(this.cfg.ajax) {

            this.progressPoll = setInterval(function() {
                var options = {
                    source: _self.id,
                    process: _self.id,
                    formId: _self.cfg._formId,
                    async: true,
                    oncomplete: function(xhr, status, args) {
                        var value = args[_self.id + '_value'];
                        _self.setValue(value);

                        //trigger complete listener
                        if(value === 100) {
                            _self.fireCompleteEvent();
                        }
                    }
                };

                PrimeFaces.ajax.AjaxRequest(options);

            }, this.cfg.interval);
        }
    },

    fireCompleteEvent: function() {
        clearInterval(this.progressPoll);

        if(this.cfg.behaviors) {
            var completeBehavior = this.cfg.behaviors['complete'];

            if(completeBehavior) {
                completeBehavior.call(this);
            }
        }
    },

    cancel: function() {
        clearInterval(this.progressPoll);
        this.setValue(0);
    },

    enableARIA: function() {
        this.jq.attr('role', 'progressbar')
                .attr('aria-valuemin', 0)
                .attr('aria-valuenow', 0)
                .attr('aria-valuemax', 100);
    }

});

PrimeFaces.widget.Password.prototype.getValue = function() {
  return this.jq.val();
}

PrimeFaces.widget.Password.prototype.setValue = function(value) {
  this.jq.val(value);
}

if (typeof links !== 'undefined') {
  links.Timeline.prototype.applyRange = function (start, end, zoomAroundDate) {
      // calculate new start and end value
      var startValue = start.valueOf(); // number
      var endValue = end.valueOf();     // number
      var interval = (endValue - startValue);

      // determine maximum and minimum interval
      var options = this.options;
      var year = 1000 * 60 * 60 * 24 * 365;
      var zoomMin = Number(options.zoomMin) || 10;
      if (zoomMin < 10) {
          zoomMin = 10;
      }
      var zoomMax = Number(options.zoomMax) || 10000 * year;
      if (zoomMax > 10000 * year) {
          zoomMax = 10000 * year;
      }
      if (zoomMax < zoomMin) {
          zoomMax = zoomMin;
      }

      // determine min and max date value
      var min = options.min ? options.min.valueOf() : undefined; // number
      var max = options.max ? options.max.valueOf() : undefined; // number
      if (min != undefined && max != undefined) {
          if (min >= max) {
              // empty range
              var day = 1000 * 60 * 60 * 24;
              max = min + day;
          }
          if (zoomMax > (max - min)) {
              zoomMax = (max - min);
          }
          if (zoomMin > (max - min)) {
              zoomMin = (max - min);
          }
      }

      // prevent empty interval
      if (startValue >= endValue) {
          endValue += 1000 * 60 * 60 * 24;
      }

      // prevent too small scale
      // TODO: IE has problems with milliseconds
      if (interval < zoomMin) {
          var diff = (zoomMin - interval);
          var f = zoomAroundDate ? (zoomAroundDate.valueOf() - startValue) / interval : 0.5;
          startValue -= Math.round(diff * f);
          endValue   += Math.round(diff * (1 - f));
      }

      // prevent too large scale
      if (interval > zoomMax) {
          var diff = (interval - zoomMax);
          var f = zoomAroundDate ? (zoomAroundDate.valueOf() - startValue) / interval : 0.5;
          startValue += Math.round(diff * f);
          endValue   -= Math.round(diff * (1 - f));
      }

      // prevent to small start date
      if (min != undefined) {
          var diff = (startValue - min);
          if (diff < 0) {
              startValue -= diff;
              endValue -= diff;
          }
      }

      // prevent to large end date
      if (max != undefined) {
          var diff = (max - endValue);
          if (diff < 0) {
              startValue += diff;
              endValue += diff;
          }
      }

      this.oldStart = this.start;
    this.oldEnd = this.end;
      // apply new dates
      this.start = new Date(startValue);
      this.end = new Date(endValue);
  };

  links.Timeline.prototype.getVisibleChartRange = function() {
      return {
        'extended': this.oldStart == null || this.oldEnd == null || this.oldStart.valueOf() > this.start.valueOf() || this.end.valueOf() > this.oldEnd.valueOf(),
        //'oldStart': (this.oldStart != null ? new Date(this.oldStart.valueOf()) : null);
          //'oldEnd': (this.oldEnd != null ? new Date(this.oldEnd.valueOf()) : null);
          'start': new Date(this.start.valueOf()),
          'end': new Date(this.end.valueOf())
      };
  };
}


/*
 * PrimeFaces SplitButton Widget
 */
PrimeFaces.widget.SplitButton = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

        this.button = $(this.jqId + '_button');
        this.menuButton = $(this.jqId + '_menuButton');
        this.menu = $(this.jqId + '_menu');
        this.menuitems = this.menu.find('.ui-menuitem:not(.ui-state-disabled)');
        this.cfg.disabled = this.button.is(':disabled');

        if(!this.cfg.disabled) {
            this.cfg.position = {
                my: 'left top'
                ,at: 'left bottom'
                ,of: this.button
            };

            this.menu.appendTo(document.body);

            this.bindEvents();

            this.setupDialogSupport();
        }

        //pfs metadata
        this.button.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.menuButton.data(PrimeFaces.CLIENT_ID_DATA, this.id);
    },

    //override
    refresh: function(cfg) {
        //remove previous overlay
        $(document.body).children(PrimeFaces.escapeClientId(cfg.id + '_menu')).remove();

        this.init(cfg);
    },

    bindEvents: function() {
        var _self = this;

        PrimeFaces.skinButton(this.button).skinButton(this.menuButton);

        //mark button and descandants of button as a trigger for a primefaces overlay
        this.button.data('primefaces-overlay-target', true).find('*').data('primefaces-overlay-target', true);

        //toggle menu
        this.menuButton.click(function() {
            if(_self.menu.is(':hidden')) {
                _self.show();
            }
            else {
                _self.hide();
            }
        });

        //menuitem visuals
        this.menuitems.mouseover(function(e) {
            var menuitem = $(this),
            menuitemLink = menuitem.children('.ui-menuitem-link');

            if(!menuitemLink.hasClass('ui-state-disabled')) {
                menuitem.addClass('ui-state-hover');
            }
        }).mouseout(function(e) {
            $(this).removeClass('ui-state-hover');
        }).click(function() {
            _self.hide();
        });

        /**
        * handler for document mousedown to hide the overlay
        **/
        $(document.body).bind('mousedown.ui-menubutton', function (e) {
            //do nothing if hidden already
            if(_self.menu.is(":hidden")) {
                return;
            }

            //do nothing if mouse is on button
            var target = $(e.target);
            if(target.is(_self.button)||_self.button.has(target).length > 0) {
                return;
            }

            //hide overlay if mouse is outside of overlay except button
            var offset = _self.menu.offset();
            if(offset != null && (e.pageX < offset.left ||
                e.pageX > offset.left + _self.menu.width() ||
                e.pageY < offset.top ||
                e.pageY > offset.top + _self.menu.height())) {

                _self.button.removeClass('ui-state-focus ui-state-hover');
                _self.hide();
            }
        });

        //hide overlay on window resize
        var resizeNS = 'resize.' + this.id;
        $(window).unbind(resizeNS).bind(resizeNS, function() {
            if(_self.menu.is(':visible')) {
                _self.menu.hide();
            }
        });
    },

    setupDialogSupport: function() {
        var dialog = this.button.parents('.ui-dialog:first');

        if(dialog.length == 1) {
            this.menu.css('position', 'fixed');
        }
    },

    show: function() {
        this.alignPanel();

        this.menuButton.focus();

        this.menu.show();
    },

    hide: function() {
        this.menuButton.removeClass('ui-state-focus');

        this.menu.fadeOut('fast');
    },

    alignPanel: function() {
        var fixedPosition = this.menu.css('position') == 'fixed',
        win = $(window),
        positionOffset = fixedPosition ? '-' + win.scrollLeft() + ' -' + win.scrollTop() : null;

        this.cfg.position.offset = positionOffset;

        this.menu.css({left:'', top:'','z-index': ++PrimeFaces.zindex}).position(this.cfg.position);
    }

});


/*
 * PrimeFaces MenuButton Widget
 */
PrimeFaces.widget.MenuButton = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

        this.menuId = this.jqId + '_menu';
        this.button = this.jq.children('button');
        this.menu = this.jq.children('.ui-menu');
        this.menuitems = this.jq.find('.ui-menuitem');
        this.cfg.disabled = this.button.is(':disabled');

        if(!this.cfg.disabled) {
            this.bindEvents();

            $(document.body).children(this.menuId).remove();
            this.menu.appendTo(document.body);

            //dialog support
            this.setupDialogSupport();
        }
    },

    bindEvents: function() {
        var _self = this;

        //button visuals
        this.button.mouseover(function(){
            if(!_self.button.hasClass('ui-state-focus')) {
                _self.button.addClass('ui-state-hover');
            }
        }).mouseout(function() {
            if(!_self.button.hasClass('ui-state-focus')) {
                _self.button.removeClass('ui-state-hover ui-state-active');
            }
        }).mousedown(function() {
            $(this).removeClass('ui-state-focus ui-state-hover').addClass('ui-state-active');
        }).mouseup(function() {
            var el = $(this);
            el.removeClass('ui-state-active')

            if(_self.menu.is(':visible')) {
                el.addClass('ui-state-hover');
                _self.hide();
            }
            else {
                el.addClass('ui-state-focus');
                _self.show();
            }
        }).focus(function() {
            $(this).addClass('ui-state-focus');
        }).blur(function() {
            $(this).removeClass('ui-state-focus');
        });

        //mark button and descandants of button as a trigger for a primefaces overlay
        this.button.data('primefaces-overlay-target', true).find('*').data('primefaces-overlay-target', true);

        //menuitem visuals
        this.menuitems.mouseover(function(e) {
            var element = $(this);
            if(!element.hasClass('ui-state-disabled')) {
                element.addClass('ui-state-hover');
            }
        }).mouseout(function(e) {
            $(this).removeClass('ui-state-hover');
        }).click(function() {
            _self.button.removeClass('ui-state-focus');
            _self.hide();
        });

        this.cfg.position = {
            my: 'left top'
            ,at: 'left bottom'
            ,of: this.button
        }

        /**
        * handler for document mousedown to hide the overlay
        **/
        $(document.body).bind('mousedown.ui-menubutton', function (e) {
            //do nothing if hidden already
            if(_self.menu.is(":hidden")) {
                return;
            }

            //do nothing if mouse is on button
            var target = $(e.target);
            if(target.is(_self.button)||_self.button.has(target).length > 0) {
                return;
            }

            //hide overlay if mouse is outside of overlay except button
            var offset = _self.menu.offset();
            if(offset != null && (e.pageX < offset.left ||
                e.pageX > offset.left + _self.menu.width() ||
                e.pageY < offset.top ||
                e.pageY > offset.top + _self.menu.height())) {

                _self.button.removeClass('ui-state-focus ui-state-hover');
                _self.hide();
            }
        });

        //hide overlay on window resize
        var resizeNS = 'resize.' + this.id;
        $(window).unbind(resizeNS).bind(resizeNS, function() {
            if(_self.menu.is(':visible')) {
                _self.menu.hide();
            }
        });

        //aria
        this.button.attr('role', 'button').attr('aria-disabled', this.button.is(':disabled'));
    },

    setupDialogSupport: function() {
        var dialog = this.button.parents('.ui-dialog:first');

        if(dialog.length == 1) {
            this.menu.css('position', 'fixed');
        }
    },

    show: function() {
        this.alignPanel();

        this.menu.show();
    },

    hide: function() {
        this.menu.fadeOut('fast');
    },

    alignPanel: function() {
        var fixedPosition = this.menu.css('position') == 'fixed',
        win = $(window),
        positionOffset = fixedPosition ? '-' + win.scrollLeft() + ' -' + win.scrollTop() : null;

        this.cfg.position.offset = positionOffset;

        this.menu.css({left:'', top:'','z-index': ++PrimeFaces.zindex}).position(this.cfg.position);
    }

});
