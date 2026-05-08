package org.phoenixctms.ctsms.util;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;

public class JavaScriptCompressor {

	public static String compress(String code) {
		Compiler compiler = new Compiler();
		CompilerOptions options = new CompilerOptions();
		// SIMPLE_OPTIMIZATIONS is best for a direct replacement of your code.
		// It removes comments and whitespace but keeps your variable names.
		CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
		// Define the "file" name for error reporting purposes
		options.setEmitUseStrict(false);
		SourceFile extern = SourceFile.fromCode("externs.js", "");
		SourceFile input = SourceFile.fromCode("input.js", code);
		// Run the compiler
		compiler.compile(extern, input, options);
		if (compiler.hasErrors()) {
			// Handle malformed JS here
			//System.err.println("JS Error: " + compiler.getErrors()[0].description);
			return code; // Or throw an exception
		}
		return compiler.toSource();
	}
}