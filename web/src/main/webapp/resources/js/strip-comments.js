var CommentStripper = (function(window) {

	var SLASH = '/';
	var BACK_SLASH = '\\';
	var STAR = '*';
	var DOUBLE_QUOTE = '"';
	var SINGLE_QUOTE = "'";
	var NEW_LINE = '\n';
	var CARRIAGE_RETURN = '\r';

	var CommentStripper = function() {

	};

	CommentStripper.prototype = {

		string: '',
		length: 0,
		position: 0,
		output: null,

		getCurrentCharacter: function() {
			return this.string.charAt(this.position);
		},

		getPreviousCharacter: function() {
			return this.string.charAt(this.position - 1);
		},

		getNextCharacter: function() {
			return this.string.charAt(this.position + 1);
		},

		add: function() {
			this.output.push(this.getCurrentCharacter());
		},

		next: function() {
			this.position++;
		},

		atEnd: function() {
			return this.position >= this.length;
		},

		isEscaping: function() {
			if (this.getPreviousCharacter() == BACK_SLASH) {
				var caret = this.position - 1;
				var escaped = false;
				while (caret > 0) {
					if (this.string.charAt(caret--) != BACK_SLASH) {
						return escaped;
					}
					escaped = !escaped;
				}
				return escaped;
			}
			return false;
		},

		processSingleQuotedString: function() {
			if (this.getCurrentCharacter() == SINGLE_QUOTE) {
				this.add();
				this.next();
				while (!this.atEnd()) {
					if (this.getCurrentCharacter() == SINGLE_QUOTE && !this.isEscaping()) {
						return;
					}
					this.add();
					this.next();
				}
			}
		},

		processDoubleQuotedString: function() {
			if (this.getCurrentCharacter() == DOUBLE_QUOTE) {
				this.add();
				this.next();
				while (!this.atEnd()) {
					if (this.getCurrentCharacter() == DOUBLE_QUOTE && !this.isEscaping()) {
						return;
					}
					this.add();
					this.next();
				}
			}
		},

		processSingleLineComment: function() {
			if (this.getCurrentCharacter() == SLASH) {
				if (this.getNextCharacter() == SLASH) {
					this.next();
					while (!this.atEnd()) {
						this.next();
						if (this.getCurrentCharacter() == NEW_LINE || this.getCurrentCharacter() == CARRIAGE_RETURN) {
							return;
						}
					}
				}
			}
		},

		processMultiLineComment: function() {
			if (this.getCurrentCharacter() == SLASH) {
				if (this.getNextCharacter() == STAR) {
					while (!this.atEnd()) {
						this.next();
						if (this.getCurrentCharacter() == STAR) {
							if (this.getNextCharacter() == SLASH) {
								this.next();
								this.next();
								return;
							}
						}
					}
				}
			}
		},

		process: function() {
			while (!this.atEnd()) {
				this.processDoubleQuotedString();
				this.processSingleQuotedString();
				this.processSingleLineComment();
				this.processMultiLineComment();
				if (!this.atEnd()) {
					this.add();
					this.next();
				}
			}
		},

		reset: function() {
			this.string = '';
			this.length = 0;
			this.position = 0;
			this.output = [];
		},

		strip: function(string) {
			this.reset();
			if (string != null && string.length > 0) {
				this.string = string;
				this.length = this.string.length;
				this.process();
				return jQuery.trim(this.output.join(''));
			} else {
				return string;
			}
		}

	};

	if (typeof define === 'function') {
		define('commentstripper', [], function() {
			return CommentStripper;
		});
	};

	return window.CommentStripper = CommentStripper;

})(window);