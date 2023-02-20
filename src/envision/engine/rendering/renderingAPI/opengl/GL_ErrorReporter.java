package envision.engine.rendering.renderingAPI.opengl;

import static org.lwjgl.opengl.AMDDebugOutput.*;
import static org.lwjgl.opengl.ARBDebugOutput.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.opengl.GL43C.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageAMDCallback;
import org.lwjgl.opengl.GLDebugMessageARBCallback;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.opengl.KHRDebug;

import envision.engine.rendering.renderingAPI.error.ErrorReportingLevel;
import envision.engine.rendering.renderingAPI.error.RendererErrorReporter;

public class GL_ErrorReporter {
	
	//--------------
	// Constructors
	//--------------
	
	private GL_ErrorReporter() {}
	
	//--------------
	// Setup Method
	//--------------
	
	public static void setup() {
		GLCapabilities caps = GL.getCapabilities();
		
		if (caps.OpenGL43) {
			apiLog("[GL] Using OpenGL 4.3 for error logging.");
			GLDebugMessageCallback proc = GLDebugMessageCallback
				.create((source, type, id, severity, length, message, userParam) -> {
					GL_ErrorReporter.onCallbackGL43(source, type, id, severity, length, message, userParam);
				});
			glDebugMessageCallback(proc, NULL);
			if ((glGetInteger(GL_CONTEXT_FLAGS) & GL_CONTEXT_FLAG_DEBUG_BIT) == 0) {
				apiLog("[GL] Warning: A non-debug context may not produce any debug output.");
				glEnable(GL_DEBUG_OUTPUT);
			}
			return;
		}
		
		if (caps.GL_KHR_debug) {
			apiLog("[GL] Using KHR_debug for error logging.");
			GLDebugMessageCallback proc = GLDebugMessageCallback
				.create((source, type, id, severity, length, message, userParam) -> {
					GL_ErrorReporter.onCallbackKHR(source, type, id, severity, length, message, userParam);
				});
			KHRDebug.glDebugMessageCallback(proc, NULL);
			if (caps.OpenGL30 && (glGetInteger(GL_CONTEXT_FLAGS) & GL_CONTEXT_FLAG_DEBUG_BIT) == 0) {
				apiLog("[GL] Warning: A non-debug context may not produce any debug output.");
				glEnable(GL_DEBUG_OUTPUT);
			}
			return;
		}
		
		if (caps.GL_ARB_debug_output) {
			apiLog("[GL] Using ARB_debug_output for error logging.");
			GLDebugMessageARBCallback proc = GLDebugMessageARBCallback
				.create((source, type, id, severity, length, message, userParam) -> {
					GL_ErrorReporter.onCallbackARB(source, type, id, severity, length, message, userParam);
				});
			glDebugMessageCallbackARB(proc, NULL);
			return;
		}
		
		if (caps.GL_AMD_debug_output) {
			apiLog("[GL] Using AMD_debug_output for error logging.");
			GLDebugMessageAMDCallback proc = GLDebugMessageAMDCallback
				.create((id, category, severity, length, message, userParam) -> {
					GL_ErrorReporter.onCallbackAMD(id, category, severity, length, message, userParam);
				});
			glDebugMessageCallbackAMD(proc, NULL);
			return;
		}
		
		apiLog("[GL] No debug output implementation is available.");
	}
	
	//-------------------
	// Callback Handlers
	//-------------------
	
	private static void onCallbackGL43(int source, int type, int id, int severity, int length, long message, long userParam) {
		var b = new StringBuilder("[LWJGL] OpenGL debug message\n");
		printDetail(b, "ID", String.format("0x%X", id));
		printDetail(b, "Source", getDebugSource(source));
		printDetail(b, "Type", getDebugType(type));
		printDetail(b, "Severity", getDebugSeverity(severity));
		printDetail(b, "Message", GLDebugMessageCallback.getMessage(length, message));
		distributeOutput(b.toString(), ErrorReportingLevel.from(getDebugSeverity(severity)));
	}
	
	private static void onCallbackKHR(int source, int type, int id, int severity, int length, long message, long userParam) {
		var b = new StringBuilder("[LWJGL] OpenGL debug message\n");
		printDetail(b, "ID", String.format("0x%X", id));
		printDetail(b, "Source", getDebugSource(source));
		printDetail(b, "Type", getDebugType(type));
		printDetail(b, "Severity", getDebugSeverity(severity));
		printDetail(b, "Message", GLDebugMessageCallback.getMessage(length, message));
		distributeOutput(b.toString(), ErrorReportingLevel.from(getDebugSeverity(severity)));
	}
	
	private static void onCallbackARB(int source, int type, int id, int severity, int length, long message, long userParam) {
		var b = new StringBuilder("[LWJGL] ARB_debug_output message\n");
		printDetail(b, "ID", String.format("0x%X", id));
		printDetail(b, "Source", getSourceARB(source));
		printDetail(b, "Type", getTypeARB(type));
		printDetail(b, "Severity", getSeverityARB(severity));
		printDetail(b, "Message", GLDebugMessageARBCallback.getMessage(length, message));
		distributeOutput(b.toString(), ErrorReportingLevel.from(getDebugSeverity(severity)));
	}
	
	private static void onCallbackAMD(int id, int category, int severity, int length, long message, long userParam) {
		var b = new StringBuilder("[LWJGL] AMD_debug_output message\n");
		printDetail(b, "ID", String.format("0x%X", id));
		printDetail(b, "Category", getCategoryAMD(category));
		printDetail(b, "Severity", getSeverityAMD(severity));
		printDetail(b, "Message", GLDebugMessageAMDCallback.getMessage(length, message));
		distributeOutput(b.toString(), ErrorReportingLevel.from(getDebugSeverity(severity)));
	}
	
	//----------------------
	// General Error Output
	//----------------------
	
	private static void distributeOutput(String msg, ErrorReportingLevel level) {
		RendererErrorReporter.onEvent(msg, level);
	}
	
	//--------------------
	// Ripped From GLUtil
	//--------------------
	
	private static void printDetail(StringBuilder builder, String type, String message) {
		builder.append(String.format("\t%s: %s\n", type, message));
	}
	
	private static String getDebugSource(int source) {
		switch (source) {
		case GL_DEBUG_SOURCE_API: 				return "API";
		case GL_DEBUG_SOURCE_WINDOW_SYSTEM: 	return "WINDOW SYSTEM";
		case GL_DEBUG_SOURCE_SHADER_COMPILER: 	return "SHADER COMPILER";
		case GL_DEBUG_SOURCE_THIRD_PARTY: 		return "THIRD PARTY";
		case GL_DEBUG_SOURCE_APPLICATION: 		return "APPLICATION";
		case GL_DEBUG_SOURCE_OTHER: 			return "OTHER";
		default: 								return apiUnknownToken(source);
		}
	}
	
	private static String getDebugType(int type) {
		switch (type) {
		case GL_DEBUG_TYPE_ERROR:					return "ERROR";
		case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:		return "DEPRECATED BEHAVIOR";
		case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:		return "UNDEFINED BEHAVIOR";
		case GL_DEBUG_TYPE_PORTABILITY:				return "PORTABILITY";
		case GL_DEBUG_TYPE_PERFORMANCE:				return "PERFORMANCE";
		case GL_DEBUG_TYPE_OTHER:					return "OTHER";
		case GL_DEBUG_TYPE_MARKER:					return "MARKER";
		default:									return apiUnknownToken(type);
		}
	}
	
	private static String getDebugSeverity(int severity) {
		switch (severity) {
		case GL_DEBUG_SEVERITY_HIGH:			return "HIGH";
		case GL_DEBUG_SEVERITY_MEDIUM:			return "MEDIUM";
		case GL_DEBUG_SEVERITY_LOW:				return "LOW";
		case GL_DEBUG_SEVERITY_NOTIFICATION:	return "NOTIFICATION";
		default:								return apiUnknownToken(severity);
		}
	}
	
	private static String getSourceARB(int source) {
		switch (source) {
		case GL_DEBUG_SOURCE_API_ARB:				return "API";
		case GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB:		return "WINDOW SYSTEM";
		case GL_DEBUG_SOURCE_SHADER_COMPILER_ARB:	return "SHADER COMPILER";
		case GL_DEBUG_SOURCE_THIRD_PARTY_ARB:		return "THIRD PARTY";
		case GL_DEBUG_SOURCE_APPLICATION_ARB:		return "APPLICATION";
		case GL_DEBUG_SOURCE_OTHER_ARB:				return "OTHER";
		default:									return apiUnknownToken(source);
		}
	}
	
	private static String getTypeARB(int type) {
		switch (type) {
		case GL_DEBUG_TYPE_ERROR_ARB:					return "ERROR";
		case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB:		return "DEPRECATED BEHAVIOR";
		case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB:		return "UNDEFINED BEHAVIOR";
		case GL_DEBUG_TYPE_PORTABILITY_ARB:				return "PORTABILITY";
		case GL_DEBUG_TYPE_PERFORMANCE_ARB:				return "PERFORMANCE";
		case GL_DEBUG_TYPE_OTHER_ARB:					return "OTHER";
		default:										return apiUnknownToken(type);
		}
	}
	
	private static String getSeverityARB(int severity) {
		switch (severity) {
		case GL_DEBUG_SEVERITY_HIGH_ARB:		return "HIGH";
		case GL_DEBUG_SEVERITY_MEDIUM_ARB:		return "MEDIUM";
		case GL_DEBUG_SEVERITY_LOW_ARB:			return "LOW";
		default:								return apiUnknownToken(severity);
		}
	}
	
	private static String getCategoryAMD(int category) {
		switch (category) {
		case GL_DEBUG_CATEGORY_API_ERROR_AMD:			return "API ERROR";
		case GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD:		return "WINDOW SYSTEM";
		case GL_DEBUG_CATEGORY_DEPRECATION_AMD:			return "DEPRECATION";
		case GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD:	return "UNDEFINED BEHAVIOR";
		case GL_DEBUG_CATEGORY_PERFORMANCE_AMD:			return "PERFORMANCE";
		case GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD:		return "SHADER COMPILER";
		case GL_DEBUG_CATEGORY_APPLICATION_AMD:			return "APPLICATION";
		case GL_DEBUG_CATEGORY_OTHER_AMD:				return "OTHER";
		default:										return apiUnknownToken(category);
		}
	}
	
	private static String getSeverityAMD(int severity) {
		switch (severity) {
		case GL_DEBUG_SEVERITY_HIGH_AMD:		return "HIGH";
		case GL_DEBUG_SEVERITY_MEDIUM_AMD:		return "MEDIUM";
		case GL_DEBUG_SEVERITY_LOW_AMD:			return "LOW";
		default:								return apiUnknownToken(severity);
		}
	}
	
}
